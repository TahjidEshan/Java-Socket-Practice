//package com.bracu.server.services;

import java.util.Calendar;
import java.util.Date;

public class CalendarService {

    private Calendar calendar;

    public CalendarService() {
        this.calendar = Calendar.getInstance();
    }

    public String getCurrentTime() {
        Date d = this.calendar.getTime();
        return "Current time is: " + d.toString();
    }
}
