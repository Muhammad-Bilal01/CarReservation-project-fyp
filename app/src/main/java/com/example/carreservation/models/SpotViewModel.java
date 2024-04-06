package com.example.carreservation.models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpotViewModel extends ViewModel {
    private MutableLiveData<String> spotName = new MutableLiveData<>();

    public LiveData<String> getSpotImages() {
        return spotImages;
    }

    public void setSpotImages(String spotImages) {
        this.spotImages.setValue(spotImages);
    }

    private MutableLiveData<String> spotImages = new MutableLiveData<>();

    public LiveData<String> getId() {
        return id;
    }

    public void setId(String id) {
        this.id.setValue(id);
    }

    private MutableLiveData<String> id = new MutableLiveData<>();

    public LiveData<Boolean> getIsSpotUpdate() {
        return isSpotUpdate;
    }

    public void setIsSpotUpdate(boolean isSpotUpdate) {
        this.isSpotUpdate.setValue(isSpotUpdate);
    }

    private MutableLiveData<String> pricePerHour = new MutableLiveData<>();

    public LiveData<String> getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(String pricePerHour) {
        this.pricePerHour.setValue(pricePerHour);
    }

    private MutableLiveData<String> numOfSpot = new MutableLiveData<>();

    public LiveData<String> getNumOfSpot() {
        return numOfSpot;
    }

    public void setNumOfSpot(String numOfSpot) {
        this.numOfSpot.setValue(numOfSpot);
    }

    private MutableLiveData<Boolean> isSpotUpdate = new MutableLiveData<>();
    private MutableLiveData<String> spotAddress = new MutableLiveData<>();
    private MutableLiveData<String> spotLocation = new MutableLiveData<>();
    private MutableLiveData<Double> spotLat = new MutableLiveData<>();
    private MutableLiveData<Double> spotLong = new MutableLiveData<>();
    private MutableLiveData<String> vendorId = new MutableLiveData<>();
    private MutableLiveData<Map<String, List<Slot>>> weeklySlots = new MutableLiveData<>();
    private MutableLiveData<String> fromDate = new MutableLiveData<>();
    private MutableLiveData<String> toDate = new MutableLiveData<>();

    public SpotViewModel() {
        // Default constructor required for Firestore
    }

    public SpotViewModel(String spotName, String spotAddress, String spotLocation, double spotLat, double spotLong, String vendorId, Map<String, List<Slot>> weeklySlots, String fromDate, String toDate) {
        this.spotName.setValue(spotName);
        this.spotAddress.setValue(spotAddress);
        this.spotLocation.setValue(spotLocation);
        this.spotLat.setValue(spotLat);
        this.spotLong.setValue(spotLong);
        this.vendorId.setValue(vendorId);
        this.weeklySlots.setValue(weeklySlots);
        this.fromDate.setValue(fromDate);
        this.toDate.setValue(toDate);
    }

    public LiveData<String> getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate.setValue(fromDate);
    }

    public LiveData<String> getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate.setValue(toDate);
    }

    public void setSpotName(String spotName) {
        this.spotName.setValue(spotName);
    }

    public LiveData<String> getSpotAddress() {
        return spotAddress;
    }

    public void setSpotAddress(String spotAddress) {
        this.spotAddress.setValue(spotAddress);
    }

    public LiveData<String> getSpotLocation() {
        return spotLocation;
    }

    public void setSpotLocation(String spotLocation) {
        this.spotLocation.setValue(spotLocation);
    }

    public LiveData<Double> getSpotLat() {
        return spotLat;
    }

    public void setSpotLat(double spotLat) {
        this.spotLat.setValue(spotLat);
    }

    public LiveData<Double> getSpotLong() {
        return spotLong;
    }

    public void setSpotLong(double spotLong) {
        this.spotLong.setValue(spotLong);
    }

    public void setWeeklySlots(Map<String, List<Slot>> weeklySlots) {
        this.weeklySlots.setValue(weeklySlots);
    }

    private void initializeWeeklySlots() {
        Map<String, List<Slot>> weeklySlots = new HashMap<>();
        String[] daysOfWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        for (String day : daysOfWeek) {
            weeklySlots.put("Monday", new ArrayList<Slot>());
        }
        this.weeklySlots.setValue(weeklySlots);
    }

    public LiveData<String> getSpotName() {
        return spotName;
    }

    public LiveData<Map<String, List<Slot>>> getWeeklySlots() {
        return weeklySlots;
    }

    public LiveData<String> getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId.setValue(vendorId);
    }
}
