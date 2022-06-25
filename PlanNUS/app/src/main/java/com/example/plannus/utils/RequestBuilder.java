package com.example.plannus.utils;

import com.example.plannus.Objects.TimetableSettings;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

public class RequestBuilder {

    private TimetableSettings timetableSettings;
    private String userID;
    private String URL;

    public RequestBuilder(TimetableSettings timetableSettings, String userID, String URL) {
        this.timetableSettings = timetableSettings;
        this.userID = userID;
        this.URL = URL;
    }

    public Request buildPostRequest(String URL, int iterations) {
        RequestBody requestBody = buildRequestBody(iterations);
        return new Request.Builder()
                .url(URL)
                .post(requestBody)
                .build();
    }

    public RequestBody buildRequestBody(int iterations) {
        FormBody.Builder builder = new FormBody.Builder();
        System.out.println(this.timetableSettings);
        int actual_count = actualNumberOfMods(this.timetableSettings);
        builder = buildRequestFromMods(builder);
        if (actual_count == 0) {
            return null;
        } else {
            builder = buildRequestFromBasicData(builder, actual_count, iterations);
            builder = buildRequestFromConstraints(builder);
            return builder.build();
        }
    }

    public FormBody.Builder buildRequestFromMods(FormBody.Builder builder) {
        int actualNumber = actualNumberOfMods(this.timetableSettings);
        ArrayList<String> mods = this.timetableSettings.getModuleList();
        for(int i = 0; i < actualNumber; i++) {
            builder.add("mod" + String.valueOf(i), mods.get(i));
        }
        return builder;
    }

    public int actualNumberOfMods(TimetableSettings settings) {
        int actualCount = settings.getSize();
        ArrayList<String> mods = settings.getModuleList();
        for (int i = 1; i <= settings.getSize(); i++) {
            if (mods.get(i - 1).isEmpty()) {
                actualCount--;
            }
        }
        return actualCount;
    }

    public FormBody.Builder buildRequestFromBasicData(FormBody.Builder builder, int count, int iterations) {
        builder.add("numMods", String.valueOf(count));
        builder.add("AY", this.timetableSettings.getAcademicYear());
        builder.add("Sem", this.timetableSettings.getSem());
        builder.add("userID", this.userID);
        builder.add("iter", String.valueOf(iterations));
        return builder;
    }

    public FormBody.Builder buildRequestFromConstraints(FormBody.Builder builder) {
        HashMap<String, Boolean> constraints = this.timetableSettings.getConstraints();
        for(String constraint : constraints.keySet()) {
            builder.add(constraint, constraints.get(constraint) ? "true" : "");
        }
        return builder;
    }
}
