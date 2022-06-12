package com.example.plannus.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.plannus.Objects.TimetableSettings;
import com.example.plannus.R;
import com.example.plannus.SessionManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
//import com.squareup.okhttp.Callback;
//import com.squareup.okhttp.OkHttpClient;
//import com.squareup.okhttp.Request;
//import com.squareup.okhttp.RequestBody;
//import com.squareup.okhttp.Response;


import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Request;
import okhttp3.OkHttpClient;
import okhttp3.Callback;
import okhttp3.Response;

public class GenerateTimetableActivity extends AppCompatActivity implements View.OnClickListener {
    private Button settings, generate;
    private SessionManager sessionManager;
    private String userID;
    private OkHttpClient okHttpClient;
    private TimetableSettings timetableSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_timetable);
        initVars();
        obtainSettings();
        RequestBody requestBody = buildRequestBody();
//        Request request = new Request.Builder()
//                .url("https://plannus-sat-solver.herokuapp.com/test")
//                .post(requestBody)
//                .build();
//
//        getRequest(request);
    }

    private void getRequest(Request request) {
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("NETWORK_FAIL", "NETWORK FAIL");
                TextView textView = findViewById(R.id.textView);
                textView.setText("Network Fail");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                TextView textView = findViewById(R.id.textView);
                String text = response.body().string();
                Log.d("RESPONSE_BODY", text);
                textView.setText(text);
            }
        });
    }

    private void initVars() {
        settings = findViewById(R.id.settingsButton);
        settings.setOnClickListener(this);
        generate = findViewById(R.id.generateButton);
        generate.setOnClickListener(this);

        sessionManager = SessionManager.get();
        userID = sessionManager.getAuth().getCurrentUser().getUid();
        okHttpClient = new OkHttpClient();
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.settingsButton) {
            startActivity(new Intent(this, TimetableSettingsActivity.class));
        } else if (v.getId() == R.id.generateButton) {

        }

    }

    public void obtainSettings() {
        DocumentReference docRef = sessionManager.getFireStore().collection("Users").document(userID).collection("timetableSettings").document("timetableSettings");
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                timetableSettings = documentSnapshot.toObject(TimetableSettings.class);
                Log.d("toString Settings", timetableSettings.toString());
                Log.d("SETTINGS SIZE", ((Integer)timetableSettings.getSize()).toString());
                Log.d("MODULE LIST", timetableSettings.getModuleList().toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("SETTINGS FAILURE", "Not able to get settings from Firestore");
            }
        });
    }

    public RequestBody buildRequestBody() {
        FormBody.Builder builder = new FormBody.Builder();
        System.out.println(timetableSettings);
        Log.d("REQUEST SIZE", ((Integer)timetableSettings.getSize()).toString());
        Log.d("REQUEST MODULE LIST", timetableSettings.getModuleList().toString());
        int numMods = timetableSettings.getSize();
        ArrayList<String> mods = timetableSettings.getModuleList();
        int actual_count = timetableSettings.getSize();
        for (int i = 1, j = 0; i <= numMods; i++) {
            if (mods.get(i - 1).isEmpty()) {
                actual_count--;
                continue;
            }
            builder.add("mod" + String.valueOf(j), mods.get(i - 1));
            j++;
        }
        builder.add("numMods", String.valueOf(actual_count));
        builder.add("AY", String.valueOf("2021-2022"));
        builder.add("Sem", String.valueOf(2));
        return builder.build();
    }
}