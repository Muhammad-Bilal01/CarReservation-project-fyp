package com.example.carreservation.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;



public class ParkingSpots {
    private String id;
    private String vendorID;
    private int pricePerHalfHour;
    private String spotTitle;
    private String spotAddress;
    private String spotLocation;
    private String spotImages;
    private Double lat;
    private Double lang;
    private String fromDate;
    private String toDate;
    private int numberOfSlots;
    private List<SpotDetails> slots;

    // Add getters and setters for all fields

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVendorID() {
        return vendorID;
    }

    public void setVendorID(String vendorID) {
        this.vendorID = vendorID;
    }

    public int getPricePerHalfHour() {
        return pricePerHalfHour;
    }

    public void setPricePerHalfHour(int pricePerHalfHour) {
        this.pricePerHalfHour = pricePerHalfHour;
    }

    public String getSpotTitle() {
        return spotTitle;
    }

    public void setSpotTitle(String spotTitle) {
        this.spotTitle = spotTitle;
    }

    public String getSpotAddress() {
        return spotAddress;
    }

    public void setSpotAddress(String spotAddress) {
        this.spotAddress = spotAddress;
    }

    public String getSpotLocation() {
        return spotLocation;
    }

    public void setSpotLocation(String spotLocation) {
        this.spotLocation = spotLocation;
    }

    public String getSpotImages() {
        return spotImages;
    }

    public void setSpotImages(String spotImages) {
        this.spotImages = spotImages;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLang() {
        return lang;
    }

    public void setLang(Double lang) {
        this.lang = lang;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public int getNumberOfSlots() {
        return numberOfSlots;
    }

    public void setNumberOfSlots(int numberOfSlots) {
        this.numberOfSlots = numberOfSlots;
    }

    public List<SpotDetails> getSlots() {
        return slots;
    }

    public void setSlots(List<SpotDetails> slots) {
        this.slots = slots;
    }

    @Override
    public String toString() {
        return "Spot{" +
                "id='" + id + '\'' +
                ", vendorID='" + vendorID + '\'' +
                ", pricePerHalfHour=" + pricePerHalfHour +
                ", spotTitle='" + spotTitle + '\'' +
                ", spotAddress='" + spotAddress + '\'' +
                ", spotLocation='" + spotLocation + '\'' +
                ", spotImages=" + spotImages +
                ", lat='" + lat + '\'' +
                ", lang='" + lang + '\'' +
                ", fromDate='" + fromDate + '\'' +
                ", toDate='" + toDate + '\'' +
                ", numberOfSlots=" + numberOfSlots +
                ", slots=" + slots +
                '}';
    }
}

class SpotDetails {
    private String date;
    private String day;
    private List<SpotTiming> timing;

    // Add getters and setters for all fields

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public List<SpotTiming> getTiming() {
        return timing;
    }

    public void setTiming(List<SpotTiming> timing) {
        this.timing = timing;
    }
}

class SpotTiming {
    private String time;
    private List<FreeAvailableSlot> freeAvailableSlots;

    // Add getters and setters for all fields

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<FreeAvailableSlot> getFreeAvailableSlots() {
        return freeAvailableSlots;
    }

    public void setFreeAvailableSlots(List<FreeAvailableSlot> freeAvailableSlots) {
        this.freeAvailableSlots = freeAvailableSlots;
    }
}

class FreeAvailableSlot {
    private int slotNo;
    private boolean isBooked;

    // Add getters and setters for all fields

    public int getSlotNo() {
        return slotNo;
    }

    public void setSlotNo(int slotNo) {
        this.slotNo = slotNo;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
    }


}



