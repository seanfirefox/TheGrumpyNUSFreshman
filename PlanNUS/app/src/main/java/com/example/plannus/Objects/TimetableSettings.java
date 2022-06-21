package com.example.plannus.Objects;

import com.google.firebase.database.PropertyName;

import java.util.ArrayList;
import java.util.HashMap;

public class TimetableSettings {

    @PropertyName("moduleList")
    private ArrayList<String> moduleList;

    @PropertyName("size")
    private int size;

    @PropertyName("constraints")
    private HashMap<String, Boolean> constraints;

    @PropertyName("academicYear")
    private String academicYear;

    @PropertyName("sem")
    private String sem;

    public TimetableSettings() {

    }

    public TimetableSettings(ArrayList<String> moduleList, HashMap<String, Boolean> constraints, String AY, String sem) {
        this.moduleList = moduleList;
        this.size = moduleList.size();
        this.constraints = constraints;
        this.sem = sem;
        this.academicYear = AY;
    }

    public ArrayList<String> getModuleList() {
        return moduleList;
    }

    public int getSize() {
        return size;
    }

    public HashMap<String, Boolean> getConstraints() {
        return constraints;
    }

    public String getSem() {
        return sem;
    }

    public void setConstraints(HashMap<String, Boolean> constraints) {
        this.constraints = constraints;
    }

    public String getAcademicYear() {
        return academicYear;
    }
}
