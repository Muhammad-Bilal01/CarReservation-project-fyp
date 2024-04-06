package com.example.carreservation.interfaces;

import com.example.carreservation.models.Booking;

public interface VendorBookingListner {

    void onItemApproveClicked(Booking model, int position);
    void onItemClicked(Booking model, int position);
}
