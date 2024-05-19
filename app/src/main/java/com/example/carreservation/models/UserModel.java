package com.example.carreservation.models;

public class UserModel {

String userName;
String profileUrl;

String userId;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public UserModel(String userName, String profileUrl, String userId) {
        this.userName = userName;
        this.profileUrl = profileUrl;
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }
}
