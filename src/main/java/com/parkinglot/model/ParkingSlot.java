package com.parkinglot.model;

public class ParkingSlot {
    private final int slotId;
    private final SlotType slotType;
    private boolean occupied;
    private Vehicle parkedVehicle;

    public ParkingSlot(int slotId, SlotType slotType) {
        this.slotId = slotId;
        this.slotType = slotType;
        this.occupied = false;
        this.parkedVehicle = null;
    }

    public int getSlotId() {
        return slotId;
    }

    public SlotType getSlotType() {
        return slotType;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public Vehicle getParkedVehicle() {
        return parkedVehicle;
    }

    public void park(Vehicle vehicle) {
        this.parkedVehicle = vehicle;
        this.occupied = true;
    }

    public void unpark() {
        this.parkedVehicle = null;
        this.occupied = false;
    }
}