// /package com.bracu.server.services;

public class Course {

    public String courseName;
    public String courseFaculty;

    public Course() {

    }

    public Course(String courseName, String courseFaculty) {
        this.courseName = courseName;
        this.courseFaculty = courseFaculty;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseFaculty() {
        return courseFaculty;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setCourseFaculty(String courseFaculty) {
        this.courseFaculty = courseFaculty;
    }

}
