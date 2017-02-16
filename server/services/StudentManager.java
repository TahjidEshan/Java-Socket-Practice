/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package com.bracu.server.services;

import server.services.exceptionset.StudentNotFoundException;
import server.services.exceptionset.NoSuchDepartmentException;
import server.services.exceptionset.StudentAlreadyExistsException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Iterator;

/**
 *
 * @author Eshan
 */
public class StudentManager {

    List<String> departmentList;
    Scanner scanner = new Scanner(System.in);

    public StudentManager(List<String> b) {
        departmentList = b;
    }
    Map<Integer, Student> StudentDb = new HashMap<Integer, Student>();

    public void addStudent(String name, String department, List<String> courses) throws StudentAlreadyExistsException {
        if (StudentExists(name)) {
            throw new StudentAlreadyExistsException();
        }
        Student student = new Student();
        student.setName(name);
        student.setDepartment(department);
        student.setCourses(courses);
        int size = StudentDb.size();
        StudentDb.put(size + 1, student);
        System.out.println("Student saved. " + StudentDb.size());

    }

    public List<String> printAllStudents() {
        List<String> student = new ArrayList<>();
        student.add("Total students" + StudentDb.size());
        for (Integer key : StudentDb.keySet()) {
            student.add(StudentDb.get(key).Name + ":\n" + StudentDb.get(key).Department + ":\n" + StudentDb.get(key).printCourses());
        }
        return student;
    }

    public String getStudent(String studentname) throws StudentNotFoundException {
        boolean exists = false;
        String s = " ";
        for (Integer key : StudentDb.keySet()) {
            Student student = StudentDb.get(key);
            if (student.getName().equals(studentname)) {
                s = (StudentDb.get(key).Name + "\n" + StudentDb.get(key).Department + ":\n" + StudentDb.get(key).printCourses());
                exists = true;
                break;
            }
        }
        if (!exists) {
            throw new StudentNotFoundException("Student " + studentname + " does not exist");
        }
        return s;
    }

    public boolean StudentExists(String name) {
        for (Integer key : StudentDb.keySet()) {
            Student student = StudentDb.get(key);
            if (student.Name.equals(name)) {
                return true;
            }
        }

        return false;
    }

    public void deleteStudent(String name) throws StudentNotFoundException {
        if (StudentExists(name)) {
            Iterator<Map.Entry<Integer, Student>> it = StudentDb.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<Integer, Student> entry = it.next();
                if (name.equals(entry.getValue().getName())) {
                    it.remove();
                    System.out.println("Student successfully removed");
                    break;
                }
            }
        } else {
            throw new StudentNotFoundException("Faculty " + name + " does not exist");
        }

    }

    public void updateStudent(String studentName) throws StudentNotFoundException {
        if (StudentExists(studentName)) {
            Student temp;
            for (Integer key : StudentDb.keySet()) {
                Student student = StudentDb.get(key);
                if (student.getName().equals(studentName)) {
                    temp = student;
                    deleteStudent(student.getName());
                    while (true) {
                        System.out.println("Press 1 to update Student Name");
                        System.out.println("Press 2 to update Student department");
                        System.out.println("Press 3 to update student courses");
                        int val = scanner.nextInt();
                        String dummy = scanner.nextLine();
                        if (val == 1) {
                            System.out.println("Type new Student name");
                            temp.setName(scanner.nextLine());
                        } else if (val == 3) {
                            student.printCourses();
                            while (true) {
                                System.out.println("Press a to remove courses");
                                System.out.println("Press b to add courses");
                                String value = scanner.nextLine();
                                if (value.equals("a")) {
                                    System.out.println("Type the course Name");
                                    String ma = scanner.nextLine();
                                    temp.getCourses().remove(ma);
                                } else if (value.equals("b")) {
                                    System.out.println("Type the course Name");
                                    String ma = scanner.nextLine();
                                    temp.setCourse(ma);
                                }
                                System.out.println("Press 'y' to update more, press 'n' otherwise");
                                String st = scanner.nextLine();
                                if (st.equals("n")) {
                                    break;
                                }
                            }
                        } else if (val == 2) {
                            System.out.println("Type new Student department");
                            String dp = scanner.nextLine();
                            if (departmentList.contains(dp)) {
                                temp.setDepartment(dp);
                            } else {
                                try {
                                    throw new NoSuchDepartmentException();
                                } catch (NoSuchDepartmentException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                        System.out.println("Press 'y' to update more, press 'n' otherwise");
                        String st = scanner.nextLine();
                        if (st.equals("n")) {
                            break;
                        }
                    }
                    try {
                        addStudent(temp.getName(), temp.getDepartment(), temp.getCourses());
                    } catch (StudentAlreadyExistsException ex) {
                        ex.printStackTrace();
                    }
                    break;
                }
            }

        } else {
            throw new StudentNotFoundException("Faculty " + studentName + " does not exist");
        }
    }
}
