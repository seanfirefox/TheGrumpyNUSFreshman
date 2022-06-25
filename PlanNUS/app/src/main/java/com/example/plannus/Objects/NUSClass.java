package com.example.plannus.Objects;

import com.google.firebase.database.PropertyName;

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
    private String start;

    @PropertyName("end")
    private String end;

    public NUSClass() {}

    public NUSClass(String classString) {
        this.classString = classString;
        String[] tokens = classString.split(" ");
        // ["CS2030S", "LEC", "01", "@", "0800", "-", "1000"]
        this.module = tokens[0];
        this.classType = tokens[1];
        this.slot = tokens[2];
        this.start = tokens[4];
        this.end = tokens[6];
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

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public String getClassString() {
        return classString;
    }
}
