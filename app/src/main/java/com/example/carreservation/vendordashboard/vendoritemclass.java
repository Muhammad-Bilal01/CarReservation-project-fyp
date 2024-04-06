package com.example.carreservation.vendordashboard;

import android.net.Uri;

import com.google.firebase.auth.FirebaseUser;

public class vendoritemclass {
    String _name;
    String _location;
    String _slotnumber;
    String _date;
    String _hourly_Rate;
    Uri _image;
    FirebaseUser _currentUserId;

    public vendoritemclass() {
        // Default constructor required for Firebase
    }

    public vendoritemclass(String _name, String _location, String _slotnumber, String _date, String _hourly_Rate, Uri _image, FirebaseUser _currentUserId) {
        this._name = _name;
        this._location = _location;
        this._slotnumber = _slotnumber;
        this._date = _date;
        this._hourly_Rate = _hourly_Rate;
        this._image = _image;
        this._currentUserId = _currentUserId;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_location() {
        return _location;
    }

    public void set_location(String _location) {
        this._location = _location;
    }

    public String get_slotnumber() {
        return _slotnumber;
    }

    public void set_slotnumber(String _slotnumber) {
        this._slotnumber = _slotnumber;
    }

    public String get_date() {
        return _date;
    }

    public void set_date(String _date) {
        this._date = _date;
    }

    public String get_hourly_Rate() {
        return _hourly_Rate;
    }

    public void set_hourly_Rate(String _hourly_Rate) {
        this._hourly_Rate = _hourly_Rate;
    }

    public Uri get_image() {
        return _image;
    }

    public void set_image(Uri _image) {
        this._image = _image;
    }

    public FirebaseUser get_currentUserId() {
        return _currentUserId;
    }

    public void set_currentUserId(FirebaseUser _currentUserId) {
        this._currentUserId = _currentUserId;
    }
}
