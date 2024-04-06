package com.example.carreservation.models;

public class Slot {
    private String slotTiming;
    private boolean isBooked;

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
    }



    public Slot() {
        // Default constructor required for Firestore
    }

    public Slot(String slotTiming) {
        this.slotTiming = slotTiming;
    }

    public String getSlotTiming() {
        return slotTiming;
    }

    @Override
    public String toString() {
        return "Slot{" +
                "slotTiming='" + slotTiming + '\'' +
                ", isBooked=" + isBooked +
                '}';
    }
}
