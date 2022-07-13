package com.example.plannus.Objects;

import com.google.firebase.database.PropertyName;

import java.util.Arrays;

public class NUSClass {

    @PropertyName("string")
    private String classString;

    @PropertyName("module")
    private String module;

    @PropertyName("classType")
    private String classType;

    @PropertyName("slot")
    private String slot;

    @PropertyName("start")
    private int start;

    @PropertyName("end")
    private int end;

    public NUSClass() {}

    public NUSClass(String classString) {
        this.classString = classString;
        String[] tokens = classString.split(" ");
        // ["CS2030S", "LEC", "01", "@", "0800", "-", "1000"]
        this.module = tokens[1];
        System.out.println(Arrays.toString(tokens));
        this.classType = tokens[2];
        this.slot = tokens[3];
        this.start = Integer.parseInt(tokens[5]);
        this.end = Integer.parseInt(tokens[7]);
    }

    public String getModule() {
        return module;
    }

    public String getClassType() {
        return classType;
    }

    public String getSlot() {
        return slot;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public String getClassString() {
        return classString;
    }
}
