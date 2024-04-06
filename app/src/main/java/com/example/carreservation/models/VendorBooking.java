package com.example.carreservation.models;

import java.util.List;

public class VendorBooking {

    private String spotName;
    private String spotAddress;
    private String vehicleRegistrationNumber;
    private String paymentScreenShotUrl;
    private String selectedDate;
    private Double totalAmount;
    private String paymentMode;
    private String vendorId;
    private String spotId;
    private List<SelectedSlot> selectedSlots;

    public boolean isBookingCompleted() {
        return isBookingCompleted;
    }

    public void setBookingCompleted(boolean bookingCompleted) {
        isBookingCompleted = bookingCompleted;
    }

    private boolean isBookingCompleted;

    private String customerId; // New field added

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    private String customerName; // New field added

    public VendorBooking() {
    }

    public VendorBooking(String spotName, String spotAddress, String vehicleRegistrationNumber, String paymentScreenShotUrl, String selectedDate, Double totalAmount, String paymentMode, String vendorId, String spotId, List<SelectedSlot> selectedSlots, String customerId,String customerName, boolean isBookingCompleted) {
        this.customerName = customerName;
        this.isBookingCompleted=isBookingCompleted;
        this.spotName = spotName;
        this.spotAddress = spotAddress;
        this.vehicleRegistrationNumber = vehicleRegistrationNumber;
        this.paymentScreenShotUrl = paymentScreenShotUrl;
        this.selectedDate = selectedDate;
        this.totalAmount = totalAmount;
        this.paymentMode = paymentMode;
        this.vendorId = vendorId;
        this.spotId = spotId;
        this.selectedSlots = selectedSlots;
        this.customerId = customerId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    public String getSpotName() {
        return spotName;
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

    public String getVehicleRegistrationNumber() {
        return vehicleRegistrationNumber;
    }

    public void setVehicleRegistrationNumber(String vehicleRegistrationNumber) {
        this.vehicleRegistrationNumber = vehicleRegistrationNumber;
    }

    public String getPaymentScreenShotUrl() {
        return paymentScreenShotUrl;
    }

    public void setPaymentScreenShotUrl(String paymentScreenShotUrl) {
        this.paymentScreenShotUrl = paymentScreenShotUrl;
    }

    public String getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(String selectedDate) {
        this.selectedDate = selectedDate;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getSpotId() {
        return spotId;
    }

    public void setSpotId(String spotId) {
        this.spotId = spotId;
    }

    public List<SelectedSlot> getSelectedSlots() {
        return selectedSlots;
    }

    public void setSelectedSlots(List<SelectedSlot> selectedSlots) {
        this.selectedSlots = selectedSlots;
    }
}
