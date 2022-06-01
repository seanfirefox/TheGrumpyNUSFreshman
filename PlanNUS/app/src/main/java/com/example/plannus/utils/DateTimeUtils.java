package com.example.plannus.utils;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Minutes;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtils {

    public static String getDuration(String dateTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
        try {
            Date deadLineD = format.parse(dateTime);
            DateTime deadLine = new DateTime(deadLineD);

            DateTime current = DateTime.now();
            int days = Days.daysBetween(current, deadLine).getDays();
            int hours = Hours.hoursBetween(current, deadLine).getHours() % 24;
            int minutes = Minutes.minutesBetween(current, deadLine).getMinutes() % 60;
            if (days == 0) {
                return hours <= 0 && minutes <= 0
                        ? "Task Expired"
                        : hours > 0
                        ? String.format("%d hrs, %d mins", hours, minutes)
                        : String.format("%d mins", minutes);
            } else if (days > 0) {
                return String.format("%d days, %d hrs", days, hours);
            } else {
                return "TASK EXPIRED";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "TASK EXPIRED";
    }
}
