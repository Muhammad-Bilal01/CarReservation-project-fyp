package com.example.carreservation.models;

public class Reviews {
    String rating;
    String message;

    String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Reviews(String rating, String message, String username) {
        this.rating = rating;
        this.message = message;
        this.username = username;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Reviews{" +
                "rating=" + rating +
                ", message='" + message + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
