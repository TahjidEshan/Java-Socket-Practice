//package com.bracu.server.services;

import server.services.exceptionset.FacultyNotFoundException;
import server.services.exceptionset.NoSuchCourseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class CourseManager {

    public Map<String, List<Course>> courseDb = new HashMap<String, List<Course>>();
    Scanner scanner = new Scanner(System.in);
    FacultyManager facultyManager;

    public CourseManager(FacultyManager m) {
        facultyManager = m;
    }

    public void addCourse(String departmentName, String courseName, String facultyName)
            throws FacultyNotFoundException {

        Course course = new Course(courseName, facultyName);
        List<Course> courseList = courseDb.get(departmentName);
        if (courseList == null) {
            courseList = new ArrayList<Course>();
        }

        if (facultyManager.facultyExists(course.getCourseFaculty())) {
            courseList.add(course);
        } else {
            throw new FacultyNotFoundException("Faculty " + facultyName + " does not exist");
        }
        courseDb.put(departmentName, courseList);
        System.out.println("Course saved.");
    }

    public void deleteCourse(Course aCourse) throws NoSuchCourseException {
        boolean exists = false;
        for (String key : courseDb.keySet()) {
            List<Course> courseList = courseDb.get(key);

            for (Course course : courseList) {
                if (course.getCourseName().equals(aCourse.getCourseName())) {
                    courseList.remove(course);
                    exists = true;
                    courseDb.put(key, courseList);
                    System.out.println("Course deleted");
                    break;
                }
            }
        }

        if (!exists) {
            throw new NoSuchCourseException("Course '" + aCourse + "' does not exist.");
        }

    }

    public Course getCourse(String courseName) throws NoSuchCourseException {
        for (String key : courseDb.keySet()) {
            List<Course> courseList = courseDb.get(key);

            for (Course course : courseList) {
                if (course.getCourseName().equals(courseName)) {
                    return course;
                }
            }
        }
        throw new NoSuchCourseException("Course '" + courseName + "' does not exist.");
    }

    public List<String> printAllCourses() {
        List<String> courses = new ArrayList<>();
        for (String key : courseDb.keySet()) {
            List<Course> courseList = courseDb.get(key);

            String coursesToPrint = null;
            for (Course course : courseList) {
                if (coursesToPrint == null) {
                    coursesToPrint = course.getCourseName();
                } else {
                    coursesToPrint = coursesToPrint + ", " + course.getCourseName();
                }
            }
            courses.add(key + ": " + coursesToPrint);
        }
        return courses;
    }

}
