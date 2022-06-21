package com.example.plannus.Activities;

import static com.example.plannus.utils.MetricsConverter.convertDpToPixel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plannus.Objects.TimetableSettings;
import com.example.plannus.R;
import com.example.plannus.SessionManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;


import java.util.ArrayList;
import java.util.HashMap;

public class TimetableSettingsActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private Button saveTimetableSettings, addRow;
    private LinearLayout wrappingLayout;
    private SessionManager sessionManager;
    private String userID;
    private int numMods;
    private CheckBox no8amConstraint, oneFreeDayConstraint;
    private Spinner aySpinner, semesterSpinner;
    private ArrayAdapter<CharSequence> ayAdapter, semAdapter;
    private String ay, semester;
    private HashMap<String, Boolean> constraints;

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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        if (adapterView.getId() == R.id.aySpinner) {
            ay = adapterView.getItemAtPosition(position).toString();
            Log.d("AY spinner check", ay);
        } else if (adapterView.getId() == R.id.semesterSpinner) {
            semester = adapterView.getItemAtPosition(position).toString();
            Log.d("Semester spinner check", semester);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @SuppressLint("NewApi")
    private void generateRow() {
        numMods++;
        LinearLayout linearLayout = new LinearLayout(TimetableSettingsActivity.this);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        TextView textView = new TextView(TimetableSettingsActivity.this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(convertDpToPixel(82), convertDpToPixel(46));
        params.setMargins(convertDpToPixel(20),0, 0, 0);
        textView.setLayoutParams(params);
        textView.setAutoSizeTextTypeUniformWithConfiguration(10, 20, 1, 1);
        textView.setText(String.format("Module %s:", numMods));
        linearLayout.addView(textView);

        EditText editText = new EditText(TimetableSettingsActivity.this);
        LinearLayout.LayoutParams editParams = new LinearLayout.LayoutParams(convertDpToPixel(285), convertDpToPixel(48));
        editText.setLayoutParams(editParams);
        editText.setAutoSizeTextTypeUniformWithConfiguration(10, 20, 1, 1);
        editText.setHint("Enter module code");
        editText.setTag("moduleCode" + numMods);
        linearLayout.addView(editText);

        wrappingLayout.addView(linearLayout);
    }

    private void initVars() {
        wrappingLayout = findViewById(R.id.wrappingLayout);
        sessionManager = SessionManager.get();
        userID = sessionManager.getAuth().getCurrentUser().getUid();
        numMods = 5;

        saveTimetableSettings = findViewById(R.id.saveTimetableSettingsButton);
        saveTimetableSettings.setOnClickListener(this);

        addRow = findViewById(R.id.addRow);
        addRow.setOnClickListener(this);

        init_checkboxes();

        init_spinners();

        constraints = new HashMap<String, Boolean>();
        constraints.put("no8amLessons", false);
        constraints.put("oneFreeDay", false);
    }

    public void init_spinners() {
        aySpinner = findViewById(R.id.aySpinner);
        aySpinner.setOnItemSelectedListener(this);
        ayAdapter = ArrayAdapter.createFromResource(TimetableSettingsActivity.this, R.array.AY_array, android.R.layout.simple_spinner_item);
        ayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        aySpinner.setAdapter(ayAdapter);

        semesterSpinner = findViewById(R.id.semesterSpinner);
        semesterSpinner.setOnItemSelectedListener(this);
        semAdapter = ArrayAdapter.createFromResource(TimetableSettingsActivity.this, R.array.SEM_array, android.R.layout.simple_spinner_item);
        semAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        semesterSpinner.setAdapter(semAdapter);
    }

    public void init_checkboxes() {

        oneFreeDayConstraint = findViewById(R.id.oneFreeDay);
        oneFreeDayConstraint.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                constraints.put("oneFreeDay", b);
            }
        });

        no8amConstraint = findViewById(R.id.no8amLessons);
        no8amConstraint.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                constraints.put("no8amLessons", b);
            }
        });
    }

    private void saveSettings() {
        ArrayList<String> mods = new ArrayList<>();
        for (int i = 0; i < numMods; i++) {
            int moduleCodeNumber = i + 1;
            EditText moduleCode = wrappingLayout.findViewWithTag("moduleCode" + moduleCodeNumber);
            String module = moduleCode.getText().toString().trim();
            mods.add(module);
        }
        TimetableSettings timetableSettings = new TimetableSettings(mods, constraints, ay, semester);
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