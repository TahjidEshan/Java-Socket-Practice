package com.bracu.server.handlers;

import com.bracu.server.services.CalendarService;
import com.bracu.server.services.Course;
import com.bracu.server.services.CourseManager;
import com.bracu.server.services.Faculty;
import com.bracu.server.services.FacultyManager;
import com.bracu.server.services.StudentManager;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import com.bracu.server.services.exceptionset.DepartmentAlreadyExistsException;
import com.bracu.server.services.exceptionset.FacultyInitialAlreadyExistsException;
import com.bracu.server.services.exceptionset.FacultyNotFoundException;
import com.bracu.server.services.exceptionset.NoSuchCourseException;
import com.bracu.server.services.exceptionset.NoSuchDepartmentException;
import com.bracu.server.services.exceptionset.StudentAlreadyExistsException;
import com.bracu.server.services.exceptionset.StudentNotFoundException;

public class ConnectionHandler {

    private Socket clientSocket = null;				// Client socket object
    private ObjectInputStream is = null;			// Input stream
    private ObjectOutputStream os = null;			// Output stream
    private CalendarService theDateService;

    // The constructor for the connection handler
    public ConnectionHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        //Set up a service object to get the current date and time
        theDateService = new CalendarService();
    }

    // Will eventually be the thread execution method - can't pass the exception back
    public void init() {
        try {
            this.is = new ObjectInputStream(clientSocket.getInputStream());
            this.os = new ObjectOutputStream(clientSocket.getOutputStream());
            while (this.readCommand()) {
            }
        } catch (IOException e) {
            System.out.println("XX. There was a problem with the Input/Output Communication:");
            e.printStackTrace();
        }
    }

    // Receive and process incoming string commands from client socket 
    private boolean readCommand() {
        String s = null;
        try {
            s = (String) is.readObject();
        } catch (Exception e) {    // catch a general exception
            this.closeSocket();
            return false;
        }
        System.out.println("01. <- Received a String object from the client (" + s + ").");

        // At this point there is a valid String object
        // invoke the appropriate function based on the command 
        if (s.equalsIgnoreCase("GetDate")) {
            this.getDate();
        } else if (s.equalsIgnoreCase("Student Server")) {
            this.run();
        } else if (s.equalsIgnoreCase("Exit")) {
            this.closeSocket();
        } else {
            this.sendError("Invalid command: " + s);
        }

        return true;
    }

    // Use our custom DateTimeService Class to get the date and time
    private void getDate() {	// use the date service to get the date
        String currentDateTimeText = theDateService.getCurrentTime();
        this.send(currentDateTimeText);
    }

    // Send a generic object back to the client 
    private void send(Object o) {
        try {
            System.out.println("02. -> Sending (" + o + ") to the client.");
            this.os.writeObject(o);
            this.os.flush();
        } catch (Exception e) {
            System.out.println("XX." + e.getStackTrace());
        }
    }

    private Object receive() {
        Object o = null;
        try {
            System.out.println("03. -- About to receive an object...");
            o = is.readObject();
            System.out.println("04. <- Object received...");
        } catch (Exception e) {
            System.out.println("XX. Exception Occurred on Receiving:" + e.getStackTrace());
        }
        return o;
    }

    // Send a pre-formatted error message to the client 
    public void sendError(String message) {
        this.send("Error:" + message);	//remember a String IS-A Object!
    }

    // Close the client socket 
    public void closeSocket() { //gracefully close the socket connection
        try {
            this.os.close();
            this.is.close();
            this.clientSocket.close();
        } catch (Exception e) {
            System.out.println("XX. " + e.getStackTrace());
        }
    }

    public void run() {
        String[] commands = new String[]{""};

        List<String> DepartmentList = new ArrayList<>();
        FacultyManager facultyManager = new FacultyManager(DepartmentList);
        CourseManager courseManager = new CourseManager(facultyManager);
        StudentManager studentManager = new StudentManager(DepartmentList);
        String keyInput = "";
        while (true) {
            commands = new String[]{"faculty", "course"};

            keyInput = (String) this.receive();

            String subs[] = keyInput.split(" ");

            if (subs.length < 2 && !keyInput.equals("exit")) {
                continue;
            }

            if (keyInput.equals("exit")) {
                System.exit(0);
            } else if (subs[0].equals("course")) {
                if (subs[1].equals("add")) {
                    this.send("Type department name,course name and faculty initial");
                    String[] array = (String[]) this.receive();
                    String deptName;
                    deptName = array[0];
                    if (!DepartmentList.contains(deptName)) {
                        try {
                            throw new NoSuchDepartmentException();
                        } catch (NoSuchDepartmentException ex) {
                            this.send("Department not found");

                            ex.printStackTrace();
                        }
                    }
                    String courseName = array[1];
                    String facultyName = array[2];
                    try {
                        courseManager.addCourse(deptName, courseName, facultyName);
                        this.send("Course Succesfully Added");
                    } catch (FacultyNotFoundException ex) {
                        this.send("Faculty not found");
                        ex.printStackTrace();
                    }

                } else if (subs[1].equals("print")) {
                    List<String> courses = courseManager.printAllCourses();
                    this.send(courses);
                } else if (subs[1].equals("delete")) {
                    this.send("Type course name: ");
                    keyInput = (String) this.receive();
                    try {
                        Course course = courseManager.getCourse(keyInput);
                        courseManager.deleteCourse(course);
                        this.send("Course Deleted");
                    } catch (NoSuchCourseException ex) {
                        this.send("Course does not exist");
                        ex.printStackTrace();
                    }

                } else if (subs[1].equals("get")) {
                    this.send("Type course name: ");
                    keyInput = (String) this.receive();
                    try {
                        Course course = courseManager.getCourse(keyInput);
                        this.send(course.getCourseName() + " (" + course.getCourseFaculty() + ")");
                    } catch (NoSuchCourseException ex) {
                        this.send("Course Does not Exist");
                        ex.printStackTrace();
                    }
                } else if (subs[1].equals("update")) {
                    this.send("Type department name,course name and faculty initial");
                    String[] array = (String[]) this.receive();
                    String deptName;
                    deptName = array[0];
                    if (!DepartmentList.contains(deptName)) {
                        try {
                            throw new NoSuchDepartmentException();
                        } catch (NoSuchDepartmentException ex) {
                            this.send("Department not found");
                            ex.printStackTrace();
                        }
                    }
                    String courseName = array[1];
                    String facultyName = array[2];
                    try {
                        Course course = courseManager.getCourse(courseName);
                        courseManager.deleteCourse(course);
                    } catch (NoSuchCourseException ex) {
                        ex.printStackTrace();
                    }
                    try {
                        courseManager.addCourse(deptName, courseName, facultyName);
                        this.send("Course Succesfully Updated");
                    } catch (FacultyNotFoundException ex) {
                        this.send("Faculty not found");
                        ex.printStackTrace();
                    }
                } else {
                    this.send("Valid sub-commands are: add, print, delete, get, update");
                }
            } else if (subs[0].equals("faculty")) {
                if (subs[1].equals("add")) {
                    this.send("Type faculty name,department name, faculty initial");
                    String[] array = (String[]) this.receive();
                    String facultyName = array[0];
                    String departmentName = array[1];
                    if (!DepartmentList.contains(departmentName)) {
                        try {
                            throw new NoSuchDepartmentException();
                        } catch (NoSuchDepartmentException ex) {
                            this.send("Department not found");
                            ex.printStackTrace();
                        }
                    }
                    String facultyInitial = array[2];
                    try {
                        facultyManager.addFaculty(facultyInitial, facultyName, departmentName);
                        this.send("Faculty succesfully added");
                    } catch (FacultyInitialAlreadyExistsException ex) {
                        this.send("Faculty initial already exists");
                        ex.printStackTrace();
                    }

                } else if (subs[1].equals("print")) {
                    List<String> faculty = facultyManager.printAllFaculties();
                    this.send(faculty);
                } else if (subs[1].equals("get")) {
                    this.send("Type faculty initial");
                    keyInput = (String) this.receive();
                    try {
                        String s = facultyManager.getFaculty(keyInput);
                        this.send(s);
                    } catch (FacultyNotFoundException ex) {
                        this.send("Faculty not found");
                        ex.printStackTrace();
                    }
                } else if (subs[1].equals("delete")) {
                    this.send("Type faculty initial");
                    keyInput = (String) this.receive();
                    try {
                        facultyManager.deleteFaculty(keyInput);
                        this.send("Faculty successfully deleted");
                    } catch (FacultyNotFoundException ex) {
                        this.send("Faculty not found");
                        ex.printStackTrace();
                    }
                } else if (subs[1].equals("update")) {
                    this.send("Type faculty name,department name, faculty initial");
                    String[] array = (String[]) this.receive();
                    String facultyName = array[0];
                    String departmentName = array[1];
                    if (!DepartmentList.contains(departmentName)) {
                        try {
                            throw new NoSuchDepartmentException();
                        } catch (NoSuchDepartmentException ex) {
                            this.send("Department not found");
                            ex.printStackTrace();
                        }

                    }
                    String facultyInitial = array[2];
                    try {
                        facultyManager.deleteFaculty(facultyInitial);
                    } catch (FacultyNotFoundException ex) {
                        this.send("Faculty not found");
                        ex.printStackTrace();
                    }
                    try {
                        facultyManager.addFaculty(facultyInitial, facultyName, departmentName);
                        this.send("Faculty successfully updated");
                    } catch (FacultyInitialAlreadyExistsException ex) {
                        this.send("Faculty initial already exists");
                        ex.printStackTrace();
                    }
                } else {
                    this.send("Valid sub-commands are: add, print, delete, get,update");
                }
            } else if (subs[0].equals("department")) {
                if (subs[1].equals("add")) {
                    this.send("Type department name: ");
                    String[] array = (String[]) this.receive();
                    keyInput = array[0];
                    boolean exists = false;
                    for (String key : courseManager.courseDb.keySet()) {
                        if (key.equals(keyInput)) {
                            exists = true;
                            try {
                                throw new DepartmentAlreadyExistsException();
                            } catch (DepartmentAlreadyExistsException ex) {
                                this.send("Department already exists");
                                ex.printStackTrace();
                            }
                        }
                    }
                    if (!exists) {
                        DepartmentList.add(keyInput);
                        List<Course> courseList = new ArrayList<Course>();
                        courseManager.courseDb.put(keyInput, courseList);
                        this.send("Department saved");
                    }

                } else if (subs[1].equals("delete")) {
                    this.send("Type department name: ");
                    keyInput = (String) this.receive();
                    DepartmentList.remove(keyInput);
                    boolean exists = false;
                    for (String key : courseManager.courseDb.keySet()) {
                        if (key.equals(keyInput)) {
                            courseManager.courseDb.remove(key);
                            for (Integer val : facultyManager.facultyDb.keySet()) {
                                Faculty faculty = facultyManager.facultyDb.get(val);
                                if (faculty.getDepartment().equals(key)) {
                                    try {
                                        facultyManager.deleteFaculty(faculty.facultyInitial);
                                    } catch (FacultyNotFoundException ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            }

                            exists = true;
                            this.send("Department deleted");
                            break;
                        }
                    }
                    if (!exists) {
                        try {
                            throw new NoSuchDepartmentException();
                        } catch (NoSuchDepartmentException ex) {
                            this.send("Department not found");
                            ex.printStackTrace();
                        }
                    }
                } else if (subs[1].equals("get")) {
                    this.send("Type department name: ");
                    keyInput = (String) this.receive();
                    boolean exists = false;
                    for (String key : courseManager.courseDb.keySet()) {
                        if (key.equals(keyInput)) {
                            List<Course> courseList = courseManager.courseDb.get(key);

                            String coursesToPrint = null;
                            for (Course course : courseList) {
                                if (coursesToPrint == null) {
                                    coursesToPrint = course.getCourseName();
                                } else {
                                    coursesToPrint = coursesToPrint + ", " + course.getCourseName();
                                }
                            }
                            this.send(key + ": " + coursesToPrint);
                            exists = true;
                        }
                    }
                    if (!exists) {
                        try {
                            throw new NoSuchDepartmentException();
                        } catch (NoSuchDepartmentException ex) {
                            this.send("Department not found");
                            ex.printStackTrace();
                        }
                    }
                } else if (subs[1].equals("print")) {
                    List<String> courses = courseManager.printAllCourses();
                    this.send(courses);
                } else {
                    this.send("Valid sub-commands are: add, print, delete, get");
                }
            } else if (subs[0].equals("student")) {
                if (subs[1].equals("add")) {
                    this.send("Type student name,department name and courses ");
                    String[] array = (String[]) this.receive();
                    String studentName = array[1];
                    String departmentName;
                    departmentName = array[2];
                    if (!DepartmentList.contains(departmentName)) {
                        try {
                            throw new NoSuchDepartmentException();
                        } catch (NoSuchDepartmentException ex) {
                            this.send("Department not found");
                            ex.printStackTrace();
                        }
                    }
                    List<String> courses = new ArrayList<>();
                    int n = 3;
                    while (n < array.length) {
                        courses.add(array[n]);
                        n++;
                    }
                    try {
                        studentManager.addStudent(studentName, departmentName, courses);
                        this.send("Student successfully added");
                    } catch (StudentAlreadyExistsException ex) {
                        this.send("Student already exists");
                        ex.printStackTrace();
                    }
                } else if (subs[1].equals("print")) {
                    List<String> s = studentManager.printAllStudents();
                    this.send(s);
                } else if (subs[1].equals("get")) {
                    this.send("Type student name");
                    keyInput = (String) this.receive();
                    try {
                        String s = studentManager.getStudent(keyInput);
                        this.send(s);
                    } catch (StudentNotFoundException ex) {
                        this.send("Student not found");
                        ex.printStackTrace();
                    }
                } else if (subs[1].equals("delete")) {
                    this.send("Type student name");
                    keyInput = (String) this.receive();
                    try {
                        studentManager.deleteStudent(keyInput);
                        this.send("Student deleted");
                    } catch (StudentNotFoundException ex) {
                        this.send("Student not found");
                        ex.printStackTrace();
                    }
                } else if (subs[1].equals("update")) {
                    this.send("Type student name,department name and courses ");
                    String[] array = (String[]) this.receive();
                    String studentName = array[1];
                    String departmentName;
                    departmentName = array[2];
                    if (!DepartmentList.contains(departmentName)) {
                        try {
                            throw new NoSuchDepartmentException();
                        } catch (NoSuchDepartmentException ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        break;
                    }
                    List<String> courses = new ArrayList<>();
                    int n = 3;
                    while (n < array.length) {

                        courses.add(array[n]);
                        n++;
                    }
                    try {
                        studentManager.deleteStudent(studentName);
                    } catch (StudentNotFoundException ex) {
                        this.send("Student not found");
                        ex.printStackTrace();
                    }
                    try {
                        studentManager.addStudent(studentName, departmentName, courses);
                        this.send("Student Updated");
                    } catch (StudentAlreadyExistsException ex) {
                        ex.printStackTrace();
                    }

                } else {
                    this.send("Valid sub-commands are: add, print, delete, get,update");
                }
            }
        }
    }
}
