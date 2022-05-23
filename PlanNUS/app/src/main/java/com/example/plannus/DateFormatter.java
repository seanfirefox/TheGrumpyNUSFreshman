package com.example.plannus;

public class DateFormatter {
    public static String dateToNumber(String date) {
        String day = date.substring(0,2);
        String month = date.substring(3,5);
        String year = date.substring(6,10);
        String numberDate = year + month + day;

        return numberDate;
    }

    public static String numberToDate(String number) {
        String day = number.substring(6,8);
        String month = number.substring(4,6);
        String year = number.substring(0,4);
        String date = day + "/" + month + "/" + year;

        return date;
    }
}
