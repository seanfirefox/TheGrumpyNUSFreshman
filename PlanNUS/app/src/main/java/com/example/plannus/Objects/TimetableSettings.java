package com.example.plannus.Objects;

import com.google.firebase.database.PropertyName;

import java.util.ArrayList;

public class TimetableSettings {

    @PropertyName("moduleList")
    private ArrayList<String> moduleList;

    @PropertyName("size")
    private int size;

    public TimetableSettings() {

    }

    public TimetableSettings(ArrayList<String> moduleList) {
        this.moduleList = moduleList;
        this.size = moduleList.size();
    }

    public ArrayList<String> getModuleList() {
        return moduleList;
    }

    public int getSize() {
        return size;
    }
}
