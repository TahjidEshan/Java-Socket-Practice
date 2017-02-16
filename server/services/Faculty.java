//package com.bracu.server.services;

/**
 * Created by eshan on 1/26/15.
 */
public class Faculty {

    public String facultyInitial;
    public String facultyName;
    public String department;

    public Faculty(String facultyInitial, String facultyName, String department) {
        this.facultyInitial = facultyInitial;
        this.facultyName = facultyName;
        this.department = department;
    }

    public Faculty() {

    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    public void setFacultyInitial(String facultyInitial) {
        this.facultyInitial = facultyInitial;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public String getFacultyInitial() {
        return facultyInitial;
    }

    public String getDepartment() {
        return department;
    }

    public String toString() {
        return "Name: " + facultyName + "\n" + "Initial: " + facultyInitial + "\n" + "Department: " + department;
    }

}
