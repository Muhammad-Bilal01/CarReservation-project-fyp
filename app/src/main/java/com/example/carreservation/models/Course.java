package com.example.carreservation.models;

public class Course {

    // variables for storing our data.
    private String courseName, courseDescription, courseDuration;

    public Course() {
        // empty constructor
        // required for Firebase.
    }

    // Constructor for all variables.
    public Course(String courseName, String courseDescription, String courseDuration) {
        this.courseName = courseName;
        this.courseDescription = courseDescription;
        this.courseDuration = courseDuration;
    }

    // getter methods for all variables.
    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    // setter method for all variables.
    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public String getCourseDuration() {
        return courseDuration;
    }

    public void setCourseDuration(String courseDuration) {
        this.courseDuration = courseDuration;
    }
}
