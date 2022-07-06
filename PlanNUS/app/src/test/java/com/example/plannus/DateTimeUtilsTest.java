package com.example.plannus;

import static org.junit.Assert.assertEquals;

import com.example.plannus.utils.DateTimeUtils;

import org.junit.Test;

public class DateTimeUtilsTest {

    @Test
    public void testForDaysandHours() {
        String dateTime = "202302011200";
        String duration = DateTimeUtils.getDuration(dateTime);
        assertEquals("days", duration.substring(4, 8));
    }

    @Test
    public void expiredTaskTest() {
        String dateTime = "202101010000";
        String duration = DateTimeUtils.getDuration(dateTime);
        assertEquals("TASK EXPIRED", duration);
    }
}
