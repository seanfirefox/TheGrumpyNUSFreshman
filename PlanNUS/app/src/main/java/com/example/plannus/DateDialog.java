package com.example.plannus;

import java.util.Calendar;

public class DateDialog {
    Calendar calendar = Calendar.getInstance();
    final int year = calendar.get(Calendar.YEAR);
    final int month = calendar.get(Calendar.MONTH);
    final int day = calendar.get(Calendar.DAY_OF_MONTH);
    private static DateDialog dp = null;

    private DateDialog() {
    }

    public static DateDialog getInstance() {
        if (dp == null) {
            dp = new DateDialog();
        }
        return dp;
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

}
