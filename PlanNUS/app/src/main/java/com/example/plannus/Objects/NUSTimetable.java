package com.example.plannus.Objects;

import com.google.firebase.database.PropertyName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
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
            this.stringRep = (String) jsonObject.get("string");
            this.mondayClass =  ArrayListConverter((JSONArray) jsonObject.get("MON"));
            this.tuesdayClass = ArrayListConverter((JSONArray) jsonObject.get("TUE"));
            this.wednesdayClass = ArrayListConverter((JSONArray) jsonObject.get("WED"));
            this.thursdayClass =  ArrayListConverter((JSONArray) jsonObject.get("THUR"));
            this.fridayClass = ArrayListConverter((JSONArray) jsonObject.get("FRI"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static ArrayList<String> ArrayListConverter(JSONArray jsonObject) throws JSONException {
        ArrayList<String> lst = new ArrayList<>();
        if (jsonObject != null) {
            for (int i = 0; i < jsonObject.length(); i++) {
                lst.add(jsonObject.getString(i));
            }
        }
        return lst;
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
