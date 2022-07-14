package com.example.plannus.utils;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTimeDialog {

    private Calendar calendar = Calendar.getInstance();
    private final int year = calendar.get(Calendar.YEAR);
    private final int month = calendar.get(Calendar.MONTH);
    private final int day = calendar.get(Calendar.DAY_OF_MONTH);
    private final int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
    private final Date time = calendar.getTime();
    private int hour;
    private int min;
    private static DateTimeDialog dateTimeDialog = null;

    private DateTimeDialog() {
    }

    public static DateTimeDialog getInstance() {
        if (dateTimeDialog == null) {
            dateTimeDialog = new DateTimeDialog();
        }
        return dateTimeDialog;
    }
    public TimePickerDialog.OnTimeSetListener initTIme(Button dialog) {
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                hour = hourOfDay;
                min = minute;
                dialog.setText(String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute));
            }
        };

        return timeSetListener;
    }

    public DatePickerDialog.OnDateSetListener initDate(Button dialog) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth)
                        + "/" + (month < 10 ? "0" + month : month)
                        + "/" + year;
                dialog.setText(date);
            }
        };
        return dateSetListener;
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

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public int getMinute() {
        return min;
    }

    public int getHour() {
        return hour;
    }

    public Date getTime() {
        return time;
    }
}
