package com.example.plannus.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.example.plannus.Objects.TimetableSettings;
import com.example.plannus.R;
import com.example.plannus.SessionManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;

import androidx.legacy.widget.*;

import java.util.ArrayList;

public class TimetableSettingsActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText moduleCode1, moduleCode2, moduleCode3, moduleCode4, moduleCode5;
    private Button saveTimetableSettings, addRow;
    private LinearLayout layoutAttributeGenerator, wrappingLayout;
    private SessionManager sessionManager;
    private String userID;
    private int numMods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable_settings);
        initVars();

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.saveTimetableSettingsButton) {
            saveSettings();
            finish();
        } else if (v.getId() == R.id.addRow) {
            generateRow();
        }
    }

    @SuppressLint("NewApi")
    private void generateRow() {
        LinearLayout linearLayout = new LinearLayout(TimetableSettingsActivity.this);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        TextView textView = new TextView(TimetableSettingsActivity.this);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(convertDpToPixel(82), convertDpToPixel(46));
        textView.setLayoutParams(params);
        textView.setAutoSizeTextTypeUniformWithConfiguration(10, 20, 1, 1);
        textView.setText("module6");
        linearLayout.addView(textView);
        wrappingLayout.addView(linearLayout);
        EditText editText = new EditText(this);
        editText.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

    }

    public static int convertDpToPixel(float dp){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }

    private void initVars() {
        layoutAttributeGenerator = findViewById(R.id.linearLayout);
        wrappingLayout = findViewById(R.id.wrappingLayout);
        sessionManager = SessionManager.get();
        userID = sessionManager.getAuth().getCurrentUser().getUid();
        layoutAttributeGenerator = findViewById(R.id.wrappingLayout);
        moduleCode1 = findViewById(R.id.moduleCode);
        moduleCode2 = findViewById(R.id.moduleCode2);
        moduleCode3 = findViewById(R.id.moduleCode3);
        moduleCode4 = findViewById(R.id.moduleCode4);
        moduleCode5 = findViewById(R.id.moduleCode5);
        saveTimetableSettings = findViewById(R.id.saveTimetableSettingsButton);
        saveTimetableSettings.setOnClickListener(this);
        addRow = findViewById(R.id.addRow);
        addRow.setOnClickListener(this);
        numMods = 5;
    }

    private void saveSettings() {
        String module1 = moduleCode1.getText().toString().trim();
        String module2 = moduleCode2.getText().toString().trim();
        String module3 = moduleCode3.getText().toString().trim();
        String module4 = moduleCode4.getText().toString().trim();
        String module5 = moduleCode5.getText().toString().trim();
        ArrayList<String> mods = new ArrayList<String>();
        mods.add(module1);
        mods.add(module2);
        mods.add(module3);
        mods.add(module4);
        mods.add(module5);
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