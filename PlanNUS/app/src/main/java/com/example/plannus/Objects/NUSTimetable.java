package com.example.plannus.Objects;

import com.google.firebase.database.PropertyName;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NUSTimetable {

    @PropertyName("toString")
    private String stringRep;

    @PropertyName("Monday")
    private ArrayList<String> mondayClass;

    @PropertyName("Tuesday")
    private ArrayList<String> tuesdayClass;

    @PropertyName("Wednesday")
    private ArrayList<String> wednesdayClass;

    @PropertyName("Thursday")
    private ArrayList<String> thursdayClass;

    @PropertyName("Friday")
    private ArrayList<String> fridayClass;

    public NUSTimetable() {

    }

    @SuppressWarnings("unchecked")
    public NUSTimetable(JSONObject jsonObject) {
        try {
            this.mondayClass = (ArrayList<String>) jsonObject.get("MON");
            this.tuesdayClass = (ArrayList<String>) jsonObject.get("TUE");
            this.wednesdayClass = (ArrayList<String>) jsonObject.get("WED");
            this.thursdayClass = (ArrayList<String>) jsonObject.get("THUR");
            this.fridayClass = (ArrayList<String>) jsonObject.get("FRI");
            this.stringRep = (String) jsonObject.get("string");
        } catch (JSONException e) {
            e.getStackTrace();
        }

    }

    public String getStringRep() {
        return stringRep;
    }

    public ArrayList<String> getMondayClass() {
        return mondayClass;
    }

    public ArrayList<String> getTuesdayClass() {
        return tuesdayClass;
    }

    public ArrayList<String> getWednesdayClass() {
        return wednesdayClass;
    }

    public ArrayList<String> getThursdayClass() {
        return thursdayClass;
    }

    public ArrayList<String> getFridayClass() {
        return fridayClass;
    }
}
