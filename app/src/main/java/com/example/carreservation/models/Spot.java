package com.example.carreservation.models;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Spot  {

    private String id;
    private String spotName;
    private String spotAddress;
    private String spotLocation;
    private double spotLat;
    private double spotLong;
    private String  vendorId;
    private String  vendorToken;
    private Map<String, List<Slot>> weeklySlots;
    private String fromDate;
    private String toDate;

    public List<AvailableSlot> getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(List<AvailableSlot> availableSlots) {
        this.availableSlots = availableSlots;
    }

    private List<AvailableSlot> availableSlots;

    public String getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(String pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public String getNumOfSpot() {
        return numOfSpot;
    }

    public void setNumOfSpot(String numOfSpot) {
        this.numOfSpot = numOfSpot;
    }

    private String pricePerHour;
    private String numOfSpot;

    public String getVendorToken() {
        return vendorToken;
    }

    public void setVendorToken(String vendorToken) {
        this.vendorToken = vendorToken;
    }

    public String getId() {
        return id;
    }

    public void setId(String Id) {
        this.id=Id;
    }
    public String getSpotImages() {
        return spotImages;
    }

    public void setSpotImages(String spotImages) {
        this.spotImages = spotImages;
    }

    private String spotImages;
    public Spot() {
        // Default constructor required for Firestore
    }

    public Spot(String id,
                String spotName,
                String spotAddress,
                String spotLocation,
                double spotLat,
                double spotLong,
                String vendorId,
                String vendorToken,
                Map<String, List<Slot>> weeklySlots,
                String fromDate,
                String toDate,
                String spotImages,
                String pricePerHour,
                List<AvailableSlot> availableSlots
    ) {
        this.spotName=spotName;
        this.spotAddress=spotAddress;
        this.spotLocation = spotLocation;
        this.spotLat = spotLat;
        this.spotLong = spotLong;
        this.vendorId = vendorId;
        this.vendorToken = vendorToken;
        this.weeklySlots = weeklySlots;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.spotImages=spotImages;
        this.id=id;
        this.pricePerHour=pricePerHour;
        this.availableSlots=availableSlots;
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

    public void setSpotName(String spotName) {
        this.spotName = spotName;
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

    public double getSpotLat() {
        return spotLat;
    }

    public void setSpotLat(double spotLat) {
        this.spotLat = spotLat;
    }

    public double getSpotLong() {
        return spotLong;
    }

    public void setSpotLong(double spotLong) {
        this.spotLong = spotLong;
    }

    public void setWeeklySlots(Map<String, List<Slot>> weeklySlots) {
        this.weeklySlots = weeklySlots;
    }

    private void initializeWeeklySlots() {
        String[] daysOfWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        for (String day : daysOfWeek) {
            weeklySlots.put(day, new ArrayList<Slot>());
        }
    }

    public String getSpotName() {
        return spotName;
    }

    public Map<String, List<Slot>> getWeeklySlots() {
        return weeklySlots;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    @Override
    public String toString() {
        return "Spot{" +
                "id='" + id + '\'' +
                ", spotName='" + spotName + '\'' +
                ", spotAddress='" + spotAddress + '\'' +
                ", spotLocation='" + spotLocation + '\'' +
                ", spotLat=" + spotLat +
                ", spotLong=" + spotLong +
                ", vendorId='" + vendorId + '\'' +
                ", weeklySlots=" + weeklySlots +
                ", fromDate='" + fromDate + '\'' +
                ", toDate='" + toDate + '\'' +
                ", availableSlots=" + availableSlots +
                ", pricePerHour='" + pricePerHour + '\'' +
                ", numOfSpot='" + numOfSpot + '\'' +
                ", spotImages='" + spotImages + '\'' +
                '}';
    }
}
