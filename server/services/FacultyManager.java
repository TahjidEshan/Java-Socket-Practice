//package com.bracu.server.services;

import server.services.exceptionset.FacultyInitialAlreadyExistsException;
import server.services.exceptionset.FacultyNotFoundException;
import server.services.exceptionset.NoSuchDepartmentException;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class FacultyManager {

    public Map<Integer, Faculty> facultyDb = new HashMap<Integer, Faculty>();
    Scanner scanner = new Scanner(System.in);
    List<String> dept;

    public FacultyManager(List<String> a) {
        dept = a;
    }

    public void addFaculty(String facultyInitial, String facultyName, String department)
            throws FacultyInitialAlreadyExistsException {
        if (facultyExists(facultyInitial)) {
            throw new FacultyInitialAlreadyExistsException();
        }

        Faculty faculty = new Faculty();
        faculty.setFacultyInitial(facultyInitial);
        faculty.setFacultyName(facultyName);
        if (dept.contains(department)) {
            faculty.setDepartment(department);
        } else {
            try {
                throw new NoSuchDepartmentException();
            } catch (NoSuchDepartmentException ex) {
                ex.printStackTrace();
            }
        }
        int size = facultyDb.size();
        facultyDb.put(size + 1, faculty);
        System.out.println("Faculty saved. " + facultyDb.size());
    }

    public List<String> printAllFaculties() {
        List<String> faculty = new ArrayList<>();
        faculty.add("Number of faculties" + facultyDb.size());
        for (Integer key : facultyDb.keySet()) {
            faculty.add(key + ":\n" + facultyDb.get(key).toString());
        }
        return faculty;
    }

    public String getFaculty(String facultyInitial) throws FacultyNotFoundException {
        boolean exists = false;
        String s = " ";
        for (Integer key : facultyDb.keySet()) {
            Faculty faculty = facultyDb.get(key);
            if (faculty.getFacultyInitial().equals(facultyInitial)) {
                s = (key + ":\n" + facultyDb.get(key).toString());
                exists = true;
                break;
            }
        }
        if (!exists) {
            throw new FacultyNotFoundException("Faculty " + facultyInitial + " does not exist");
        }
        return s;

    }

    public boolean facultyExists(String facultyInitial) {
        for (Integer key : facultyDb.keySet()) {
            Faculty faculty = facultyDb.get(key);
            if (faculty.getFacultyInitial().equals(facultyInitial)) {
                return true;
            }
        }

        return false;
    }

    public void deleteFaculty(String facultyInitial) throws FacultyNotFoundException {
        if (facultyExists(facultyInitial)) {
            Iterator<Map.Entry<Integer, Faculty>> it = facultyDb.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<Integer, Faculty> entry = it.next();
                if (facultyInitial.equals(entry.getValue().facultyInitial)) {
                    it.remove();
                    break;
                }
            }
        } else {
            throw new FacultyNotFoundException("Faculty " + facultyInitial + " does not exist");
        }

    }

}
