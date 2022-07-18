package com.example.plannus.Objects;

import com.google.firebase.database.PropertyName;

import org.json.JSONArray;
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

    @PropertyName("Saturday")
    private ArrayList<String> saturdayClass;

    private String monClass;
    private String tueClass;
    private String wedClass;
    private String thurClass;
    private String friClass;
    private String satClass;


    public NUSTimetable() {

    }

    @SuppressWarnings("unchecked")
    public NUSTimetable(JSONObject jsonObject) {
        try {
            this.stringRep = (String) jsonObject.get("string");
            this.mondayClass =  ArrayListConverter((JSONArray) jsonObject.get("MON"));
            monClass = representText(this.mondayClass, "MONDAY");
            this.tuesdayClass = ArrayListConverter((JSONArray) jsonObject.get("TUE"));
            tueClass = representText(this.tuesdayClass, "TUESDAY");
            this.wednesdayClass = ArrayListConverter((JSONArray) jsonObject.get("WED"));
            wedClass = representText(this.wednesdayClass, "WEDNESDAY");
            this.thursdayClass =  ArrayListConverter((JSONArray) jsonObject.get("THUR"));
            thurClass = representText(this.thursdayClass, "THURSDAY");
            this.fridayClass = ArrayListConverter((JSONArray) jsonObject.get("FRI"));
            friClass = representText(this.fridayClass, "FRIDAY");
            this.saturdayClass = ArrayListConverter((JSONArray) jsonObject.get("SAT"));
            satClass = representText(this.saturdayClass, "SATURDAY");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static String representText(ArrayList<String> arr, String start) {
        if (arr.size() == 0) {
            return start + "\n NO CLASS TODAY";
        }
        String ss = arr.stream().reduce(start, (s, x) -> s + "\n" + x);
        System.out.println(ss);
        return ss;
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

    public ArrayList<String> getSaturdayClass() {
        return saturdayClass;
    }

    public String getSatClass() {
        return satClass;
    }

    public ArrayList<String> getFridayClass() {
        return fridayClass;
    }

    public String getMonClass() {
        return monClass;
    }

    public String getTueClass() {
        return tueClass;
    }

    public String getWedClass() {
        return wedClass;
    }

    public String getThurClass() {
        return thurClass;
    }

    public String getFriClass() {
        return friClass;
    }
}
