/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bracu.server.services;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Eshan
 */
public class Student {

    public String Name;
    public String Department;
    List<String> Courses = new ArrayList<>();
    Iterator<String> it;

    public Student() {
    }

    public Student(String a, String b, List<String> c) {
        Name = a;
        Department = b;
        Courses = c;

    }

    public String printCourses() {
        String str = " ";
        List<String> CourseList = Courses;
        it = CourseList.iterator();
        while (it.hasNext()) {
            if (str.equals(" ")) {
                str = it.next();
            } else {
                str = str + ", " + it.next();
            }
        }
        return str;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public void setDepartment(String department) {
        this.Department = department;
    }

    public void setCourses(List<String> c) {
        Courses = c;
    }

    public void setCourse(String c) {
        Courses.add(c);
    }

    public String getName() {
        return Name;
    }

    public String getDepartment() {
        return Department;
    }

    public List<String> getCourses() {
        return Courses;
    }
}
