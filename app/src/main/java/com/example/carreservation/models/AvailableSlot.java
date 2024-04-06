package com.example.carreservation.models;

public class AvailableSlot {

    private String id;
    private String date;
    private String weekDay;
    private String slotName;

    public boolean getIsbooked() {
        return isbooked;
    }

    public void setIsbooked(boolean isbooked) {
        this.isbooked = isbooked;
    }

    private boolean isbooked;

    public AvailableSlot(String id, String date, String weekDay, String slotName, Boolean isbooked) {
        this.id = id;
        this.date = date;
        this.weekDay = weekDay;
        this.slotName = slotName;
        this.isbooked = isbooked;
    }

    public AvailableSlot() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    public String getSlotName() {
        return slotName;
    }

    public void setSlotName(String slotName) {
        this.slotName = slotName;
    }

    public AvailableSlot(String id, String date, String weekDay, String slotName) {
        this.id = id;
        this.date = date;
        this.weekDay = weekDay;
        this.slotName = slotName;
    }
}
