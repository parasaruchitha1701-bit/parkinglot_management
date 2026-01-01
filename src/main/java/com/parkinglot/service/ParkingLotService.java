package com.parkinglot.service;

import com.parkinglot.model.*;
import com.parkinglot.storage.ParkingLotStorage;

import java.util.ArrayList;
import java.util.List;

public class ParkingLotService {
    private final List<ParkingSlot> slots;
    private final ParkingLotStorage storage;

    public ParkingLotService(int totalSlots, ParkingLotStorage storage) {
        this.storage = storage;
        this.slots = new ArrayList<>();
        initializeSlots(totalSlots); 
    }

    private void initializeSlots(int totalSlots) {
        int smallCount = (int) Math.floor(totalSlots * 0.4);
        int largeCount = (int) Math.floor(totalSlots * 0.4);
        int oversizeCount = totalSlots - smallCount - largeCount;
        if(oversizeCount > smallCount) {
        	oversizeCount--;
        	smallCount++;
        }

        int id = 1;
        for (int i = 0; i < smallCount; i++) {
            slots.add(new ParkingSlot(id++, SlotType.SMALL));
        }
        for (int i = 0; i < largeCount; i++) {
            slots.add(new ParkingSlot(id++, SlotType.LARGE));
        }
        for (int i = 0; i < oversizeCount; i++) {
            slots.add(new ParkingSlot(id++, SlotType.OVERSIZE));
        }
    }

    public void parkVehicle(Vehicle vehicle) {
        SlotType requiredSlotType = SlotType.valueOf(vehicle.getVehicleType().name());

        if (isVehicleAlreadyParked(vehicle.getVehicleId())) {
            System.out.println("Vehicle " + vehicle.getVehicleId() + " is already parked.");
            return;
        }

        for (ParkingSlot slot : slots) {
            if (!slot.isOccupied() && slot.getSlotType() == requiredSlotType) {
                slot.park(vehicle);
                persist();
                System.out.println("Parked " + vehicle.getVehicleId() +
                        " (" + vehicle.getVehicleType() + ") in Slot #" + slot.getSlotId());
                return;
            }
        }

        System.out.println("No available " + requiredSlotType + " slot for vehicle " + vehicle.getVehicleId());
    }

    public void removeVehicle(String vehicleId) {
        for (ParkingSlot slot : slots) {
            if (slot.isOccupied() && slot.getParkedVehicle() != null
                    && slot.getParkedVehicle().getVehicleId().equalsIgnoreCase(vehicleId)) {
                slot.unpark();
                persist();
                System.out.println("Vehicle " + vehicleId + " removed from Slot #" + slot.getSlotId());
                return;
            }
        }
        System.out.println("Vehicle " + vehicleId + " not found.");
    }

    public void displayStatus() {
        int total = slots.size();
        int occupied = 0;

        int smallFree = 0, largeFree = 0, oversizeFree = 0;
        int smallOcc = 0, largeOcc = 0, oversizeOcc = 0;

        for (ParkingSlot slot : slots) {
            if (slot.isOccupied()) {
                occupied++;
                switch (slot.getSlotType()) {
                    case SMALL:
                        smallOcc++;
                        break;
                    case LARGE:
                        largeOcc++;
                        break;
                    case OVERSIZE:
                        oversizeOcc++;
                        break;
                }
            } else {
                switch (slot.getSlotType()) {
                    case SMALL:
                        smallFree++;
                        break;
                    case LARGE:
                        largeFree++;
                        break;
                    case OVERSIZE:
                        oversizeFree++;
                        break;
                }
            }
        }

        System.out.println("\n========= PARKING LOT STATUS =========");
        System.out.println("Total Slots: " + total);
        System.out.println("Occupied Slots: " + occupied);
        System.out.println("Available Slots: " + (total - occupied));
        System.out.println("-------------------------------------");
        System.out.println("SMALL    -> Free: " + smallFree + " | Occupied: " + smallOcc);
        System.out.println("LARGE    -> Free: " + largeFree + " | Occupied: " + largeOcc);
        System.out.println("OVERSIZE -> Free: " + oversizeFree + " | Occupied: " + oversizeOcc);
        System.out.println("=====================================\n");
    }

    public void displayDetailedSlots() {
        System.out.println("\n========= SLOT DETAILS =========");
        for (ParkingSlot slot : slots) {
            if (slot.isOccupied() && slot.getParkedVehicle() != null) {
                System.out.println("Slot #" + slot.getSlotId() + " [" + slot.getSlotType() + "] -> "
                        + slot.getParkedVehicle().getVehicleId() + " (" + slot.getParkedVehicle().getVehicleType() + ")");
            } else {
                System.out.println("Slot #" + slot.getSlotId() + " [" + slot.getSlotType() + "] -> EMPTY");
            }
        }
        System.out.println("================================\n");
    }

    private boolean isVehicleAlreadyParked(String vehicleId) {
        for (ParkingSlot slot : slots) {
            if (slot.isOccupied() && slot.getParkedVehicle() != null
                    && slot.getParkedVehicle().getVehicleId().equalsIgnoreCase(vehicleId)) {
                return true;
            }
        }
        return false;
    }

    private void persist() {
        if (storage != null) {
            storage.save(slots);
        }
    }
}