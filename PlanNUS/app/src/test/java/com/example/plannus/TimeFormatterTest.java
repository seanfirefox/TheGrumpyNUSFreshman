package com.example.plannus;

import static com.example.plannus.utils.TimeFormatter.numberToTime;
import static com.example.plannus.utils.TimeFormatter.timeToNumber;

import org.junit.Test;

import static org.junit.Assert.*;


public class TimeFormatterTest {

    @Test
    public void timeToNumberDoubleDigitHourTest() {
        String time = "11:10";
        String number = timeToNumber(time);
        assertEquals(number, "1110");
    }

    @Test
    public void numberToTimeDoubleDigitHourTest() {
        String number = "1110";
        String time = numberToTime(number);
        assertEquals(time, "11:10");
    }

    @Test
    public void timeToNumberSingleDigitHourTest() {
        String time = "09:09";
        String number = timeToNumber(time);
        assertEquals(number, "0909");
    }

    @Test
    public void numberToTimeSingleDigitHourTest() {
        String number = "0909";
        String time = numberToTime(number);
        assertEquals(time, "09:09");
    }

}
