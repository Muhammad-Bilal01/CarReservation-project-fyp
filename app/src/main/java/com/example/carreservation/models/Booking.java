package com.example.carreservation.models;

import java.util.List;

public class Booking {

    private String id;
    private String spotName;
    private String spotAddress;
    private String bookingStatus;

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    private String vehicleRegistrationNumber;
    private String paymentScreenShotUrl;
    private String selectedDate;
    private Double totalAmount;
    private String paymentMode;
    private String vendorId;
    private String spotId;
    private List<SelectedSlot> selectedSlots;
    private String customerId; // New field added

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    private String customerName; // New field added

    public String getSpotImages() {
        return spotImages;
    }

    public void setSpotImages(String spotImages) {
        this.spotImages = spotImages;
    }

    private String spotImages; // New field added

    public boolean isBookingCompleted() {
        return isBookingCompleted;
    }

    public void setBookingCompleted(boolean bookingCompleted) {
        isBookingCompleted = bookingCompleted;
    }

    private boolean isBookingCompleted;

    public Booking() {
    }

    public Booking(String id, String spotName, String spotAddress,
                   String bookingStatus,
                   String vehicleRegistrationNumber, String paymentScreenShotUrl,
                   String selectedDate, Double totalAmount, String paymentMode, String vendorId, String spotId,
                   List<SelectedSlot> selectedSlots, String customerId, boolean isBookingCompleted, String customerName,String spotImages) {
        this.id = id;
        this.spotName = spotName;
        this.spotAddress = spotAddress;
        this.bookingStatus = bookingStatus;
        this.vehicleRegistrationNumber = vehicleRegistrationNumber;
        this.paymentScreenShotUrl = paymentScreenShotUrl;
        this.selectedDate = selectedDate;
        this.totalAmount = totalAmount;
        this.paymentMode = paymentMode;
        this.vendorId = vendorId;
        this.spotId = spotId;
        this.selectedSlots = selectedSlots;
        this.customerId = customerId;
        this.isBookingCompleted=isBookingCompleted;
        this.customerName=customerName;
        this.spotImages=spotImages;
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

    @Override
    public String toString() {
        return "Booking{" +
                "id='" + id + '\'' +
                ", spotName='" + spotName + '\'' +
                ", bookingStatus='" + bookingStatus + '\'' +
                ", spotAddress='" + spotAddress + '\'' +
                ", vehicleRegistrationNumber='" + vehicleRegistrationNumber + '\'' +
                ", paymentScreenShotUrl='" + paymentScreenShotUrl + '\'' +
                ", selectedDate='" + selectedDate + '\'' +
                ", totalAmount=" + totalAmount +
                ", paymentMode='" + paymentMode + '\'' +
                ", vendorId='" + vendorId + '\'' +
                ", spotId='" + spotId + '\'' +
                ", selectedSlots=" + selectedSlots +
                ", customerId='" + customerId + '\'' +
                ", customerName='" + customerName + '\'' +
                ", spotImages='" + spotImages + '\'' +
                ", isBookingCompleted=" + isBookingCompleted +
                '}';
    }
}
