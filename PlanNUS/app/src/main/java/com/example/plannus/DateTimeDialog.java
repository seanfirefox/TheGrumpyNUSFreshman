package com.example.plannus;

import java.util.Calendar;

public class DateTimeDialog {
    private Calendar calendar = Calendar.getInstance();
    private final int year = calendar.get(Calendar.YEAR);
    private final int month = calendar.get(Calendar.MONTH);
    private final int day = calendar.get(Calendar.DAY_OF_MONTH);
    private final int hour = calendar.get(Calendar.HOUR);
    private final int minute = calendar.get(Calendar.MINUTE);
    private static DateTimeDialog dateTimeDialog = null;

    private DateTimeDialog() {
    }

    public static DateTimeDialog getInstance() {
        if (dateTimeDialog == null) {
            dateTimeDialog = new DateTimeDialog();
        }
        return dateTimeDialog;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getMinute() {
        return minute;
    }

    public int getHour() {
        return hour;
    }
}
