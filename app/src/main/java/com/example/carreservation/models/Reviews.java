package com.example.carreservation.models;

public class Reviews {
    String rating;
    String message;

    public Reviews(String rating, String message) {
        this.rating = rating;
        this.message = message;
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
                '}';
    }
}
