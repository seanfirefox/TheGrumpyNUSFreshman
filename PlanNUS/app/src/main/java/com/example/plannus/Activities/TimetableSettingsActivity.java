package com.example.plannus.Activities;

import static com.example.plannus.utils.MetricsConverter.convertDpToPixel;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.plannus.Objects.TimetableSettings;
import com.example.plannus.R;
import com.example.plannus.SessionManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.HashMap;

public class TimetableSettingsActivity extends AppCompatActivity implements View.OnClickListener    {
    private Button saveTimetableSettings, addRow;
    private LinearLayout wrappingLayout;
    private SessionManager sessionManager;
    private String userID;
    private int numMods;
    private CheckBox no8amConstraint, oneFreeDayConstraint;
//    private Spinner aySpinner, semesterSpinner;
//    private ArrayAdapter<CharSequence> ayAdapter, semAdapter;
    private AutoCompleteTextView aySpinner, semesterSpinner;
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

    @SuppressLint("NewApi")
    private void generateRow() {
        numMods++;
        LinearLayout linearLayout = new LinearLayout(TimetableSettingsActivity.this);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        TextView textView = new TextView(TimetableSettingsActivity.this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(convertDpToPixel(95), convertDpToPixel(46));
        params.setMargins(convertDpToPixel(15),0, 0, 0);
        textView.setLayoutParams(params);
        textView.setAutoSizeTextTypeUniformWithConfiguration(10, 20, 1, 1);
        textView.setText(String.format("Module %s:", numMods));
        textView.setTextColor(Color.parseColor("#FF000000"));
        linearLayout.addView(textView);

        EditText editText = new EditText(TimetableSettingsActivity.this);
        LinearLayout.LayoutParams editParams = new LinearLayout.LayoutParams(convertDpToPixel(285), convertDpToPixel(48));
        editParams.setMargins(0,0, convertDpToPixel(20), 0);
        editText.setLayoutParams(editParams);
        editText.setAutoSizeTextTypeUniformWithConfiguration(10, 20, 1, 1);
        editText.setHint("Enter module code");
        editText.setTag("moduleCode" + numMods);
        editText.setTextColor(Color.parseColor("#FF000000"));
        editText.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        linearLayout.addView(editText);

        wrappingLayout.addView(linearLayout);
    }

    private void initVars() {
        wrappingLayout = findViewById(R.id.wrappingLayout);
        sessionManager = SessionManager.get();
        userID = sessionManager.getUserID();
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

        ayAdapter = ArrayAdapter.createFromResource(TimetableSettingsActivity.this, R.array.AY_array, android.R.layout.simple_spinner_item);
        ayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        aySpinner.setAdapter(ayAdapter);
        System.out.println("initSpinnersRunning--------------------------");

        aySpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ay = ayAdapter.getItem(position).toString();
                Log.d("AY spinner check", ay);
            }
        });

        semesterSpinner = findViewById(R.id.semesterSpinner);
        semAdapter = ArrayAdapter.createFromResource(TimetableSettingsActivity.this, R.array.SEM_array, android.R.layout.simple_spinner_item);
        semAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        semesterSpinner.setAdapter(semAdapter);
        semesterSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                semester = semAdapter.getItem(position).toString();
                Log.d("Semester spinner check", semester);
            }
        });
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
        sessionManager.getDocRef(userID, "timetableSettings", "timetableSettings")
                .set(settings)
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