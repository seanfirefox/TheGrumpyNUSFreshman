package com.example.plannus.Activities.TimetableGenerator;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.plannus.Objects.NUSClass;
import com.example.plannus.Objects.NUSTimetable;
import com.example.plannus.Objects.TimetableSettings;
import com.example.plannus.R;
import com.example.plannus.SessionManager;
import com.example.plannus.utils.RequestBuilder;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GenerateTimetableActivity extends AppCompatActivity implements View.OnClickListener {
    private Button settings, generate, next, save;
    private SessionManager sessionManager;
    private String userID;
    private OkHttpClient okHttpClient;
    private TimetableSettings timetableSettings;
    private TextView mondayTextView, tuesdayTextView, wednesdayTextView, thursdayTextView, fridayTextView, saturdayTextView, nothingTextView;
    private final String actualURL = "https://plannus-sat-solver.herokuapp.com/z3runner";
    private final String URL = "https://plannus-satsolver-backup.herokuapp.com/z3runner";
    private static int iterations = 0;
    private Call call;
    private NUSTimetable nusTimetable;
    private ArrayList<String> constraintStrings;
    private ProgressBar progressBar;
    private DocumentReference timetableDocRef, settingsDocRef;
    private ArrayList<String> mondayDocumentNames;
    private ArrayList<String> tuesdayDocumentNames;
    private ArrayList<String> wednesdayDocumentNames;
    private ArrayList<String> thursdayDocumentNames;
    private ArrayList<String> fridayDocumentNames;
    private ArrayList<String> saturdayDocumentNames;
    private HashMap<Integer, String> hashMapCollectionPath;
    private HashMap<Integer, ArrayList<String>> hashMapArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_timetable);
        initVars();
        deflateTextViews(mondayTextView, tuesdayTextView, wednesdayTextView, thursdayTextView, fridayTextView, saturdayTextView);
        inflateTextViews(nothingTextView);
        nothingTextView.setText("No Timetable Generated Yet");
        obtainSettings();
    }

    @Override
    public void onResume() {
        super.onResume();
        obtainSettings();
    }

    @Override
    public void onBackPressed() {
        if (progressBar.getVisibility() == View.VISIBLE) {
            Toast.makeText(this, "Timetable is Still generating / saving.", Toast.LENGTH_LONG).show();
            return;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if  (item.getItemId() == android.R.id.home) {
            if (progressBar.getVisibility() == View.VISIBLE) {
                Toast.makeText(this, "PROCESS INTERRUPTED AND ABORTED", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "CALENDAR DATA MAY BE COMPROMISED", Toast.LENGTH_LONG).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.settingsButton) {
            startActivity(new Intent(this, TimetableSettingsActivity.class));
        } else if (v.getId() == R.id.generateButton) {
            if (timetableSettings == null) {
                Toast.makeText(GenerateTimetableActivity.this, "Settings page empty/ still getting rendering data, please wait...", Toast.LENGTH_LONG).show();
            } else {
                disableButtonBlocker(false);
                iterations = 0;
                obtainSettings();
                RequestBody requestBody = new RequestBuilder(timetableSettings, userID, actualURL)
                        .buildRequestBody(iterations);
                if (requestBody == null) {
                    deflateTextViews(mondayTextView, tuesdayTextView, wednesdayTextView, thursdayTextView, fridayTextView, saturdayTextView);
                    inflateTextViews(nothingTextView);
                    nothingTextView.setText("Settings page empty");
                    disableButtonBlocker(true);
                } else {
                    if (call != null) {
                        call.cancel();
                    }
                    Request builtRequest = new RequestBuilder(timetableSettings, userID, actualURL)
                            .buildPostRequest(actualURL, iterations);
                    getRequest(builtRequest);
                }
            }
        } else if (v.getId() == R.id.nextButton) {
            if (timetableSettings == null) {
                Toast.makeText(GenerateTimetableActivity.this, "Please click generate button first", Toast.LENGTH_LONG).show();
            } else {
                disableButtonBlocker(false);
                iterations++;
                Request nextSolutionRequest = new RequestBuilder(timetableSettings, userID, actualURL)
                        .buildPostRequest(actualURL, iterations);
                getRequest(nextSolutionRequest);
            }
        } else if (v.getId() == R.id.saveTimetableButton) {
            progressBar.setVisibility(View.VISIBLE);
            getDocumentNames(1);
        }

    }

    private int getDocumentNames(int r) {
        if (r < 7) {
            timetableDocRef.collection(hashMapCollectionPath.get(r))
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                NUSClass nusClass = document.toObject(NUSClass.class);
                                hashMapArrayList.get(r).add(nusClass.getClassString());
                                Log.d("Success", "Class: " + nusClass.getClassString());
                            }
                            deleteCollectionInFireStore(r);

                        } else {
                            Log.d("Fail", "Error getting documents: ", task.getException());
                        }
                    });
        } else {
            saveTimeTable();
        }
        return 1;
    }



    private void deleteCollectionInFireStore(int r) {
        ArrayList<String> classList = hashMapArrayList.get(r);
        String collectionPath = hashMapCollectionPath.get(r);
        if (classList == null || classList.size() == 0) {
            Log.d("Timetable NULL", "No Old Class in FireStore");
            getDocumentNames(r + 1);
        } else {
            for (int i = 0; i < classList.size(); i++) {
                String s = classList.get(i);
                deleteClassInFireStore(s, collectionPath, classList.size(), i, r);
            }
        }
    }

    private void deleteClassInFireStore(String documentName, String collectionName, int size, int i, int r) {
        timetableDocRef.collection(collectionName)
                .document(documentName)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Log.d("Success", "DocumentSnapshot successfully deleted!");
                    if (i == size - 1) {
                        getDocumentNames(r + 1);
                    }
                })
                .addOnFailureListener(e -> Log.d("Failure", "Error deleting document", e));
    }

    private int saveTimeTable() {
        CompletableFuture.supplyAsync(() -> saveTimeTableClass(nusTimetable.getMondayClass(), "mondayClass"))
                .thenAcceptAsync(x -> saveTimeTableClass(nusTimetable.getTuesdayClass(), "tuesdayClass"))
                .thenAcceptAsync(x -> saveTimeTableClass(nusTimetable.getWednesdayClass(), "wednesdayClass"))
                .thenAcceptAsync(x -> saveTimeTableClass(nusTimetable.getThursdayClass(), "thursdayClass"))
                .thenAcceptAsync(x -> saveTimeTableClass(nusTimetable.getFridayClass(), "fridayClass"))
                .thenAcceptAsync(x -> saveTimeTableClass(nusTimetable.getSaturdayClass(), "saturdayClass"))
                .join();
        return 1;
    }

    private int saveTimeTableClass(ArrayList<String> classList, String collectionPath) {
        if (classList == null) {
            Log.d("Timetable NULL", "Timetable is Null, Not saving it");
            return 1;
        }
        for (int i = 0; i < classList.size(); i++) {
            String s = classList.get(i);
            NUSClass nusClass = new NUSClass(s);
            saveClassIntoFireStore(nusClass, s, collectionPath);
        }

        timetableDocRef.set(nusTimetable)
                .addOnSuccessListener(unused -> {
                    if (collectionPath.equals("saturdayClass")) {
                        Toast.makeText(GenerateTimetableActivity.this, "Timetable Saved Successfully",Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                    Log.d("SAVE SUCCESS", "Timetable Saved successfully!");
                }).addOnFailureListener(e -> {
                    Toast.makeText(GenerateTimetableActivity.this, "Timetable did not save", Toast.LENGTH_SHORT).show();
                    Log.d("SAVE FAIL", "Timetable Did NOT Save !");
                    progressBar.setVisibility(View.GONE);
                });
        return 1;
    }

    private void saveClassIntoFireStore(NUSClass nusClass, String s, String collectionPath) {
        timetableDocRef.collection(collectionPath)
                .document(s)
                .set(nusClass, SetOptions.merge())
                .addOnSuccessListener(unused -> Log.d("CLASS SAVED", "onSuccess: Class is saved"))
                .addOnFailureListener(e -> Log.d("CLASS UNSAVED", "onFailure : Class not saved!"));
    }

    private void initVars() {

        mondayTextView = findViewById(R.id.mondayClass);
        tuesdayTextView = findViewById(R.id.tuesdayClass);
        wednesdayTextView = findViewById(R.id.wednesdayClass);
        thursdayTextView = findViewById(R.id.thursdayClass);
        fridayTextView = findViewById(R.id.fridayClass);
        saturdayTextView = findViewById(R.id.saturdayClass);

        nothingTextView = findViewById(R.id.nothingCard);

        progressBar = findViewById(R.id.progressBar2);
        settings = findViewById(R.id.settingsButton);
        settings.setOnClickListener(this);
        generate = findViewById(R.id.generateButton);
        generate.setOnClickListener(this);
        next  = findViewById(R.id.nextButton);
        next.setOnClickListener(this);

        sessionManager = SessionManager.get();
        userID = sessionManager.getUserID();
        timetableDocRef = sessionManager.getDocRef(userID,"NUS_Schedule", "NUS_Schedule");
        settingsDocRef = sessionManager.getDocRef(userID, "timetableSettings", "timetableSettings");
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        constraintStrings = new ArrayList<>();
        constraintStrings.add("no8amLessons");
        constraintStrings.add("oneFreeDay");
        mondayDocumentNames = new ArrayList<>();
        tuesdayDocumentNames = new ArrayList<>();
        wednesdayDocumentNames = new ArrayList<>();
        thursdayDocumentNames = new ArrayList<>();
        fridayDocumentNames = new ArrayList<>();
        saturdayDocumentNames = new ArrayList<>();

        hashMapCollectionPath = new HashMap<>();
        hashMapCollectionPath.put(1, "mondayClass");
        hashMapCollectionPath.put(2, "tuesdayClass");
        hashMapCollectionPath.put(3, "wednesdayClass");
        hashMapCollectionPath.put(4, "thursdayClass");
        hashMapCollectionPath.put(5, "fridayClass");
        hashMapCollectionPath.put(6, "saturdayClass");

        hashMapArrayList = new HashMap<>();
        hashMapArrayList.put(1, mondayDocumentNames);
        hashMapArrayList.put(2, tuesdayDocumentNames);
        hashMapArrayList.put(3, wednesdayDocumentNames);
        hashMapArrayList.put(4, thursdayDocumentNames);
        hashMapArrayList.put(5, fridayDocumentNames);
        hashMapArrayList.put(6, saturdayDocumentNames);

        nusTimetable = new NUSTimetable();

        save = findViewById(R.id.saveTimetableButton);
        save.setOnClickListener(this);
    }

    @SafeVarargs
    private final void deflateTextViews(TextView... textViews) {
        for (TextView t : textViews) {
            t.setVisibility(View.GONE);
        }
    }

    @SafeVarargs
    private final void inflateTextViews(TextView... textViews) {
        for (TextView t : textViews) {
            t.setVisibility(View.VISIBLE);
        }
    }

    private void getRequest(Request request) {
        call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("NETWORK_FAIL", "NETWORK FAIL");
                deflateTextViews(mondayTextView, tuesdayTextView, wednesdayTextView, thursdayTextView, fridayTextView, saturdayTextView);
                inflateTextViews(nothingTextView);
                nothingTextView.setText("Network Fail");
                runOnUiThread(() -> disableButtonBlocker(true));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                runOnUiThread(() -> {
                    try {
                        String jsonReturnString = response.body().string();
                        System.out.println(jsonReturnString);
                        if (jsonReturnString.equals("No Feasible Timetable!")) {
                            deflateTextViews(mondayTextView, tuesdayTextView, wednesdayTextView, thursdayTextView, fridayTextView, saturdayTextView);
                            inflateTextViews(nothingTextView);
                            nothingTextView.setText("No Feasible Timetable!\n Change your study plan in Settings!");
                        } else {
                            JSONObject jsonObject = new JSONObject(jsonReturnString);
                            String displayText = (String) jsonObject.get("string");
                            nusTimetable = new NUSTimetable(jsonObject);
                            Log.d("RESPONSE_BODY", displayText);
                            inflateTextViews(mondayTextView, tuesdayTextView, wednesdayTextView, thursdayTextView, fridayTextView, saturdayTextView);
                            deflateTextViews(nothingTextView);
                            mondayTextView.setText(nusTimetable.getMonClass());
                            tuesdayTextView.setText(nusTimetable.getTueClass());
                            wednesdayTextView.setText(nusTimetable.getWedClass());
                            thursdayTextView.setText(nusTimetable.getThurClass());
                            fridayTextView.setText(nusTimetable.getFriClass());
                            saturdayTextView.setText(nusTimetable.getSatClass());
                        }
                    } catch (IOException e) {
                        e.getStackTrace();
                    } catch (JSONException e) {
                        deflateTextViews(mondayTextView, tuesdayTextView, wednesdayTextView, thursdayTextView, fridayTextView, saturdayTextView);
                        inflateTextViews(nothingTextView);
                        nothingTextView.setText("Invalid Combination!\n There is NO OTHER solution!");
                    } finally {
                        disableButtonBlocker(true);
                    }
                });
            }
        });

    }

    public void obtainSettings() {
        settingsDocRef.get().addOnSuccessListener(documentSnapshot -> {
            timetableSettings = documentSnapshot.toObject(TimetableSettings.class);
            if (timetableSettings == null) {
                return;
            } else {
                Log.d("toString Settings", timetableSettings.toString());
                Log.d("SETTINGS SIZE", ((Integer)timetableSettings.getSize()).toString());
                Log.d("MODULE LIST", timetableSettings.getModuleList().toString());
                Log.d("CONSTRAINTS", timetableSettings.getConstraints().toString());
            }
        }).addOnFailureListener(e -> Log.d("SETTINGS FAILURE", "Not able to get settings from Firestore"));
    }

    public void disableButtonBlocker(boolean b) {
        next.setEnabled(b);
        generate.setEnabled(b);
        progressBar.setVisibility(b ? View.GONE : View.VISIBLE);
    }
}