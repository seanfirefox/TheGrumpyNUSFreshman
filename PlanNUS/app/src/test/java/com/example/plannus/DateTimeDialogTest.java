package com.example.plannus;

import org.joda.time.DateTime;
import org.junit.Test;

import static org.junit.Assert.*;

import com.example.plannus.utils.DateTimeDialog;

public class DateTimeDialogTest {

    @Test
    public void isSingletonClassTest() {
        DateTimeDialog a = DateTimeDialog.getInstance();
        DateTimeDialog b = DateTimeDialog.getInstance();
        assertSame(a, b);
    }

}
