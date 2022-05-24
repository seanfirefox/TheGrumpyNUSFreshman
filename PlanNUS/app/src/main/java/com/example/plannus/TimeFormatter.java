package com.example.plannus;

public class TimeFormatter {

    public static String timeToNumber(String time) {
        // 11:10 -> 1110
        return String.format("%s%s",time.substring(0,2), time.substring(3,5));
    }

    public static String numberToTime(String number) {
        // 1110 -> 11:10
        return String.format("%s:%s", number.substring(0,2), number.substring(2,4));
    }
}
