package com.example.carreservation.models;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class BookingViewModel extends ViewModel {

    private MutableLiveData<String> id=new MutableLiveData<>();
    private MutableLiveData<String> spotName=new MutableLiveData<>();
    private MutableLiveData<String> spotImages=new MutableLiveData<>();
    private MutableLiveData<String> spotAddress=new MutableLiveData<>();
    private MutableLiveData<String> vehicleRegistrationNumber=new MutableLiveData<>();
    private MutableLiveData<String> paymentScreenShotUrl=new MutableLiveData<>();
    public MutableLiveData<String> getPaymentScreenShotUrl() {
        return paymentScreenShotUrl;
    }

    public void setPaymentScreenShotUrl(String url) {
        this.paymentScreenShotUrl.setValue(url);
    }
    public MutableLiveData<String> getVehicleRegistrationNumber() {
        return vehicleRegistrationNumber;
    }

    public void setVehicleRegistrationNumber(String registrationNumber) {
        this.vehicleRegistrationNumber.setValue(registrationNumber);
    }
    public MutableLiveData<String> gtSpotImages() {
        return spotImages;
    }

    public void setSpotImages(String images) {
        this.spotImages.setValue(images);
    }

    public MutableLiveData<String> getId() {
        return id;
    }

    public void setId(MutableLiveData<String> id) {
        this.id = id;
    }

    public MutableLiveData<String> getSpotName() {
        return spotName;
    }

    public void setSpotName(String spotName) {
        this.spotName.setValue(spotName);
    }

    public MutableLiveData<String> getSpotAddress() {
        return spotAddress;
    }

    public void setSpotAddress(String spotAddress) {
        this.spotAddress.setValue(spotAddress);
    }

    public MutableLiveData<String> getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(String selectedDate) {
        this.selectedDate.setValue(selectedDate);
    }

    public MutableLiveData<Double> getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount.setValue(totalAmount);
    }

    public MutableLiveData<String> getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode.setValue(paymentMode);
    }

    public MutableLiveData<String> getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId.setValue(vendorId);
    }

    public MutableLiveData<String> getVendorToken() {
        return vendorToken;
    }

    public void setVendorToken(String vendorToken) {
        this.vendorToken.setValue(vendorToken);
    }

    public MutableLiveData<String> getSpotId() {
        return spotId;
    }

    public void setSpotId(String spotId) {
        this.spotId.setValue(spotId);
    }

    public MutableLiveData<List<SelectedSlot>> getSelectedSlots() {
        return selectedSlots;
    }

    public void setSelectedSlots(List<SelectedSlot> selectedSlots) {
        this.selectedSlots.setValue(selectedSlots);
    }

    private MutableLiveData<String> selectedDate=new MutableLiveData<>();
    private MutableLiveData<Double> totalAmount=new MutableLiveData<>();
    private MutableLiveData<String> paymentMode=new MutableLiveData<>();
    private MutableLiveData<String> vendorId=new MutableLiveData<>();
    private MutableLiveData<String> vendorToken=new MutableLiveData<>();
    private MutableLiveData<String> spotId=new MutableLiveData<>();
    private MutableLiveData<List<SelectedSlot>> selectedSlots=new MutableLiveData<>();

}
