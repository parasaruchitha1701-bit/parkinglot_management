package com.parkinglot.storage;

import com.parkinglot.model.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class ParkingLotStorage {
    private final Path filePath;

    public ParkingLotStorage(String filePath) {
        this.filePath = Paths.get(filePath);
    }

    public Path getFilePath() {
        return filePath;
    }

    public boolean exists() {
        return Files.exists(filePath);
    }

    public void ensureParentDirExists() {
        try {
            Path parent = filePath.getParent();
            if (parent != null && !Files.exists(parent)) {
                Files.createDirectories(parent);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to create data directory: " + e.getMessage(), e);
        }
    }

    public void save(List<ParkingSlot> slots) {
        ensureParentDirExists();

        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            writer.write("slotId,slotType,occupied,vehicleId,vehicleType");
            writer.newLine();

            for (ParkingSlot slot : slots) {
                String vehicleId = "";
                String vehicleType = "";

                if (slot.isOccupied() && slot.getParkedVehicle() != null) {
                    vehicleId = slot.getParkedVehicle().getVehicleId();
                    vehicleType = slot.getParkedVehicle().getVehicleType().name();
                }

                writer.write(slot.getSlotId() + "," +
                        slot.getSlotType().name() + "," +
                        slot.isOccupied() + "," +
                        escape(vehicleId) + "," +
                        escape(vehicleType));
                writer.newLine();
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to save parking state: " + e.getMessage(), e);
        }
    }

    public List<ParkingSlot> load() {
        if (!exists()) return new ArrayList<>();

        List<ParkingSlot> slots = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String header = reader.readLine(); // header
            if (header == null) return slots;

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length < 5) continue;

                int slotId = Integer.parseInt(parts[0].trim());
                SlotType slotType = SlotType.valueOf(parts[1].trim());
                boolean occupied = Boolean.parseBoolean(parts[2].trim());
                String vehicleId = unescape(parts[3].trim());
                String vehicleTypeStr = unescape(parts[4].trim());

                ParkingSlot slot = new ParkingSlot(slotId, slotType);

                if (occupied) {
                    VehicleType vt = VehicleType.valueOf(vehicleTypeStr);
                    slot.park(new Vehicle(vehicleId, vt));
                }

                slots.add(slot);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load parking state: " + e.getMessage(), e);
        }

        return slots;
    }

    private String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace(",", "\\,").replace("\n", "\\n");
    }

    private String unescape(String s) {
        if (s == null) return "";
        return s.replace("\\n", "\n").replace("\\,", ",").replace("\\\\", "\\");
    }
}