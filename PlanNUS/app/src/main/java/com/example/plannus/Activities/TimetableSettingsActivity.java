package com.example.plannus.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plannus.Objects.TimetableSettings;
import com.example.plannus.R;
import com.example.plannus.SessionManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;

public class TimetableSettingsActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText moduleCode1, moduleCode2, moduleCode3;
    private Button saveGenerate;
    private SessionManager sessionManager;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable_settings);
        sessionManager = SessionManager.get();
        userID = sessionManager.getAuth().getCurrentUser().getUid();
        moduleCode1 = findViewById(R.id.moduleCode1);
        moduleCode2 = findViewById(R.id.moduleCode2);
        moduleCode3 = findViewById(R.id.moduleCode3);
        saveGenerate = findViewById(R.id.saveGenerate);
        saveGenerate.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.saveGenerate) {
            saveGenerate();
            finish();
        }
    }

    private void saveGenerate() {
        String module1 = moduleCode1.getText().toString().trim();
        String module2 = moduleCode2.getText().toString().trim();
        String module3 = moduleCode3.getText().toString().trim();
        ArrayList<String> mods = new ArrayList<String>();
        mods.add(module1);
        mods.add(module2);
        mods.add(module3);
        TimetableSettings timetableSettings = new TimetableSettings(mods);
        saveSettingsIntoFireStore(timetableSettings);

    }

    private void saveSettingsIntoFireStore(TimetableSettings settings) {
        DocumentReference docRef = sessionManager.getFireStore()
                .collection("Users")
                .document(userID)
                .collection("timetableSettings")
                .document("timetableSettings");

        docRef.set(settings)
                .addOnSuccessListener((OnSuccessListener<? super Void>) (aVoid) -> {
                    Log.d("SaveCreated", "onSuccess: Settings is saved");
                    Toast.makeText(TimetableSettingsActivity.this, "Settings saved Successfully",Toast.LENGTH_LONG).show();
                    finish();
                } ).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("SaveFail", "onFailure: "+ e);
                        Toast.makeText(TimetableSettingsActivity.this, "Failed to save settings", Toast.LENGTH_LONG).show();
                    }
                });
    }
}