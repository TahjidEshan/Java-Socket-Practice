package com.bracu.clients;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.Exception;
import java.lang.Object;
import java.lang.String;
import java.lang.System;
import java.net.Socket;
import java.util.Scanner;
import java.util.List;
import java.util.Iterator;

public class Client {

    private static int portNumber = 8889;
    private Socket socket = null;
    private ObjectOutputStream os = null;
    private ObjectInputStream is = null;

    // the constructor expects the IP address of the server - the port is fixed
    public Client(String serverIP) {
        if (!connectToServer(serverIP)) {
            System.out.println("XX. Failed to open socket connection to: " + serverIP);
        }
    }

    private boolean connectToServer(String serverIP) {
        try { // open a new socket to the server 
            this.socket = new Socket(serverIP, portNumber);
            this.os = new ObjectOutputStream(this.socket.getOutputStream());
            this.is = new ObjectInputStream(this.socket.getInputStream());
            System.out.println("00. -> Connected to Server:" + this.socket.getInetAddress()
                    + " on port: " + this.socket.getPort());
            System.out.println("    -> from local address: " + this.socket.getLocalAddress()
                    + " and port: " + this.socket.getLocalPort());
        } catch (Exception e) {
            System.out.println("XX. Failed to Connect to the Server at port: " + portNumber);
            System.out.println("    Exception: " + e.toString());
            return false;
        }
        return true;
    }

    private void getDate() {
        String theDateCommand = "GetDate", theDateAndTime;
        System.out.println("01. -> Sending Command (" + theDateCommand + ") to the server...");
        this.send(theDateCommand);
        try {
            theDateAndTime = (String) receive();
            System.out.println("05. <- The Server responded with: ");
            System.out.println("    <- " + theDateAndTime);
        } catch (Exception e) {
            System.out.println("XX. There was an invalid object sent back from the server");
        }
        System.out.println("06. -- Disconnected from Server.");
    }

    // method to send a generic object.
    private void send(Object o) {
        try {
            System.out.println("02. -> Sending an object...");
            os.writeObject(o);
            os.flush();
        } catch (Exception e) {
            System.out.println("XX. Exception Occurred on Sending:" + e.toString());
        }
    }

    // method to receive a generic object.
    private Object receive() {
        Object o = null;
        try {
            System.out.println("03. -- About to receive an object...");
            o = is.readObject();
            System.out.println("04. <- Object received...");
        } catch (Exception e) {
            System.out.println("XX. Exception Occurred on Receiving:" + e.toString());
        }
        return o;
    }

    public static void main(String args[]) {
        System.out.println("**. Java Client Application - EE402 OOP Module, DCU");
        Scanner s = new Scanner(System.in);
        if (args.length == 1) {
            Client theApp = new Client(args[0]);
            theApp.getDate();
            while (true) {
                System.out.println("Please give command");
                String string = s.nextLine();
                if (string.equals("exit")) {
                    theApp.send(string);
                    break;
                } else {
                    String subs[] = string.split(" ");
                    theApp.send(string);
                    if (subs[0].equals("course")) {
                        if (subs[1].equals("add")) {
                            System.out.println(theApp.receive());
                            String[] array = new String[3];
                            array[0] = s.nextLine();
                            array[1] = s.nextLine();
                            array[2] = s.nextLine();
                            theApp.send(array);
                            System.out.println(theApp.receive());
                        } else if (subs[1].equals("print")) {
                            List<String> list = (List<String>) theApp.receive();
                            Iterator e = list.iterator();
                            while (e.hasNext()) {
                                System.out.println(e.next());
                            }
                        } else if (subs[1].equals("delete")) {
                            System.out.println(theApp.receive());
                            theApp.send(s.nextLine());
                            System.out.println(theApp.receive());
                        } else if (subs[1].equals("get")) {
                            System.out.println(theApp.receive());
                            theApp.send(s.nextLine());
                            System.out.println(theApp.receive());
                        } else if (subs[1].equals("update")) {
                            System.out.println(theApp.receive());
                            String[] array = new String[3];
                            array[0] = s.nextLine();
                            array[1] = s.nextLine();
                            array[2] = s.nextLine();
                            theApp.send(array);
                            System.out.println(theApp.receive());
                        } else {
                            System.out.println(theApp.receive());
                        }
                    } else if (subs[0].equals("faculty")) {
                        if (subs[1].equals("add")) {
                            System.out.println(theApp.receive());
                            String[] array = new String[3];
                            array[0] = s.nextLine();
                            array[1] = s.nextLine();
                            array[2] = s.nextLine();
                            theApp.send(array);
                            System.out.println(theApp.receive());
                        } else if (subs[1].equals("print")) {
                            List<String> list = (List<String>) theApp.receive();
                            Iterator e = list.iterator();
                            while (e.hasNext()) {
                                System.out.println(e.next());
                            }
                        } else if (subs[1].equals("delete")) {
                            System.out.println(theApp.receive());
                            theApp.send(s.nextLine());
                            System.out.println(theApp.receive());
                        } else if (subs[1].equals("get")) {
                            System.out.println(theApp.receive());
                            theApp.send(s.nextLine());
                            System.out.println(theApp.receive());
                        } else if (subs[1].equals("update")) {
                            System.out.println(theApp.receive());
                            String[] array = new String[3];
                            array[0] = s.nextLine();
                            array[1] = s.nextLine();
                            array[2] = s.nextLine();
                            theApp.send(array);
                            System.out.println(theApp.receive());
                        } else {
                            System.out.println(theApp.receive());
                        }
                    } else if (subs[0].equals("department")) {
                        if (subs[1].equals("add")) {
                            System.out.println(theApp.receive());
                            String[] array = new String[1];
                            array[0] = s.nextLine();
                            theApp.send(array);
                            System.out.println(theApp.receive());
                        } else if (subs[1].equals("print")) {
                            List<String> list = (List<String>) theApp.receive();
                            Iterator e = list.iterator();
                            while (e.hasNext()) {
                                System.out.println(e.next());
                            }
                        } else if (subs[1].equals("delete")) {
                            System.out.println(theApp.receive());
                            theApp.send(s.nextLine());
                            System.out.println(theApp.receive());
                        } else if (subs[1].equals("get")) {
                            System.out.println(theApp.receive());
                            theApp.send(s.nextLine());
                            System.out.println(theApp.receive());
                        } else {
                            System.out.println(theApp.receive());
                        }
                    } else if (subs[0].equals("student")) {
                        if (subs[1].equals("add")) {
                            System.out.println("Type number of courses for student");
                            int n = s.nextInt();
                            System.out.println(theApp.receive());
                            String[] array = new String[n + 3];
                            int count = 0;
                            while (count < array.length) {
                                array[count] = s.nextLine();
                                count++;
                            }
                            theApp.send(array);
                            System.out.println(theApp.receive());
                        } else if (subs[1].equals("print")) {
                            List<String> list = (List<String>) theApp.receive();
                            Iterator e = list.iterator();
                            while (e.hasNext()) {
                                System.out.println(e.next());
                            }
                        } else if (subs[1].equals("delete")) {
                            System.out.println(theApp.receive());
                            theApp.send(s.nextLine());
                            System.out.println(theApp.receive());
                        } else if (subs[1].equals("get")) {
                            System.out.println(theApp.receive());
                            theApp.send(s.nextLine());
                            System.out.println(theApp.receive());

                        } else if (subs[1].equals("update")) {
                            System.out.println("Type number of courses for student");
                            int n = s.nextInt();
                            System.out.println(theApp.receive());
                            String[] array = new String[n + 3];
                            int count = 0;
                            while (count < array.length) {
                                array[count] = s.nextLine();
                                count++;
                            }
                            theApp.send(array);
                            System.out.println(theApp.receive());
                        } else {
                            System.out.println(theApp.receive());
                        }
                    }
                }
            }
        } else {
            System.out.println("Error: you must provide the address of the server");
            System.out.println("Usage is:  java Client x.x.x.x  (e.g. java Client 192.168.7.2)");
            System.out.println("      or:  java Client hostname (e.g. java Client localhost)");
        }
        System.out.println("**. End of Application.");
    }
}
