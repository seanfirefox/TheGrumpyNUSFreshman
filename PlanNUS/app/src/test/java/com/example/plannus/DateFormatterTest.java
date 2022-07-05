package com.example.plannus;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.plannus.utils.DateFormatter;

public class DateFormatterTest {

    @Test
    public void dateToNumberTest1() {
        String date = "01/01/2022";
        assertEquals("20220101", DateFormatter.dateToNumber(date));
    }

    @Test
    public void dateToNumberTest2() {
        String date = "07/05/2022";
        assertEquals("20220507", DateFormatter.dateToNumber(date));
    }

    @Test
    public void dateToNumberTest3() {
        String date = "17/12/2022";
        assertEquals("20221217", DateFormatter.dateToNumber(date));
    }

    @Test
    public void numberToDateTest1() {
        String date = "20221217";
        assertEquals("17/12/2022", DateFormatter.numberToDate(date));
    }

    @Test
    public void numberToDateTest2() {
        String date = "20220101";
        assertEquals("01/01/2022", DateFormatter.numberToDate(date));
    }

    @Test
    public void numberToDateTest3() {
        String date = "20220507";
        assertEquals("07/05/2022", DateFormatter.numberToDate(date));
    }

    @Test
    public void getYearTest1() {
        String date = "20220507";
        assertEquals(2022,DateFormatter.getYear(date));
    }

    @Test
    public void getYearTest2() {
        String date = "07/05/2022";
        assertEquals(2022,DateFormatter.getYear(date));
    }

    @Test
    public void getMonth1() {
        String date = "20220507";
        assertEquals(5,DateFormatter.getMonth(date));
    }

    @Test
    public void getMonth2() {
        String date = "07/05/2022";
        assertEquals(5,DateFormatter.getMonth(date));
    }

    @Test
    public void getDay1() {
        String date = "20220507";
        assertEquals(7,DateFormatter.getDay(date));
    }

    @Test
    public void getDay2() {
        String date = "07/05/2022";
        assertEquals(7,DateFormatter.getDay(date));
    }

}
