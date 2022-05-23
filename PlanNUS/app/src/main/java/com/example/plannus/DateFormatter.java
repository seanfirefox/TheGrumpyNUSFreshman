package com.example.plannus;

public class DateFormatter {
    public static String dateToNumber(String date) {
        // 01/01/2022 -> 20220101
        String day = date.substring(0,2);
        String month = date.substring(3,5);
        String year = date.substring(6,10);
        String numberDate = year + month + day;

        return numberDate;
    }

    public static String numberToDate(String number) {
        // 20220101 -> 01/01/2022
        Integer day = getDay(number);
        Integer month = getMonth(number);
        Integer year = getYear(number);
        String date = (day < 10 ? "0" + day : day)
                + "/" + (month < 10 ? "0" + month : month)
                + "/" + year;
        return date;
    }

    public static int getYear(String date) {
        return date.length() == 10
                ? Integer.valueOf(date.substring(6,10))
                : Integer.valueOf(date.substring(0,4));
    }

    public static int getDay(String date) {
        return date.length() == 10
                ? Integer.valueOf(date.substring(0,2))
                : Integer.valueOf(date.substring(6,8));
    }

    public static int getMonth(String date) {
        return date.length() == 10
                ? Integer.valueOf(date.substring(3,5))
                : Integer.valueOf(date.substring(4,6));
    }
}
