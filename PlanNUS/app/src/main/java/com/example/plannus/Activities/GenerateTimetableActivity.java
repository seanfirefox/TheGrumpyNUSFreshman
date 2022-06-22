package com.example.plannus.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.SetOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Request;
import okhttp3.OkHttpClient;
import okhttp3.Callback;
import okhttp3.Response;

public class GenerateTimetableActivity extends AppCompatActivity implements View.OnClickListener {
    private Button settings, generate, next, save;
    private SessionManager sessionManager;
    private String userID;
    private OkHttpClient okHttpClient;
    private TimetableSettings timetableSettings;
    private TextView textView;
    private final String URL = "https://plannus-satsolver-backup.herokuapp.com/z3runner";
    private static int iterations = 0;
    private Call call;
    private NUSTimetable nusTimetable, oldNusTimetable;
    private ArrayList<String> constraintStrings;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_timetable);
        initVars();
        obtainSettings();
    }

    @Override
    public void onResume() {
        super.onResume();
        obtainSettings();
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
                RequestBody requestBody = new RequestBuilder(timetableSettings, userID, URL)
                        .buildRequestBody(iterations);
                if (requestBody == null) {
                    textView.setText("Settings page empty");
                    disableButtonBlocker(true);
                } else {
                    if (call != null) {
                        call.cancel();
                    }
                    Request builtRequest = new RequestBuilder(timetableSettings, userID, URL)
                            .buildPostRequest(URL, iterations);
                    getRequest(builtRequest);
                }
            }
        } else if (v.getId() == R.id.nextButton) {
            if (timetableSettings == null) {
                Toast.makeText(GenerateTimetableActivity.this, "Please click generate button first", Toast.LENGTH_LONG).show();
            } else {
                disableButtonBlocker(false);
                iterations++;
                Request nextSolutionRequest = new RequestBuilder(timetableSettings, userID, URL)
                        .buildPostRequest(URL, iterations);
                getRequest(nextSolutionRequest);
            }
        } else if (v.getId() == R.id.saveTimetableButton) {
            deleteOldTimeTable();
            saveTimeTable(nusTimetable.getMondayClass(), "mondayClass");
            saveTimeTable(nusTimetable.getTuesdayClass(), "tuesdayClass");
            saveTimeTable(nusTimetable.getWednesdayClass(), "wednesdayClass");
            saveTimeTable(nusTimetable.getThursdayClass(), "thursdayClass");
            saveTimeTable(nusTimetable.getFridayClass(), "fridayClass");
        }

    }

    private void deleteOldTimeTable() {
        DocumentReference docRef = sessionManager
                .getFireStore()
                .collection("Users")
                .document(userID)
                .collection("NUS_Schedule")
                .document("NUS_Schedule");
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            oldNusTimetable = documentSnapshot.toObject(NUSTimetable.class);
            if (oldNusTimetable == null) {
                return;
            }
        }).addOnFailureListener(e -> Log.d("SETTINGS FAILURE", "Not able to get oldTimetable from Firestore"));
        if (oldNusTimetable != null) {
            deleteCollectionInFireStore(oldNusTimetable.getMondayClass(), "mondayClass");
            deleteCollectionInFireStore(oldNusTimetable.getTuesdayClass(), "tuesdayClass");
            deleteCollectionInFireStore(oldNusTimetable.getWednesdayClass(), "wednesdayClass");
            deleteCollectionInFireStore(oldNusTimetable.getThursdayClass(), "thursdayClass");
            deleteCollectionInFireStore(oldNusTimetable.getFridayClass(), "fridayClass");
        }
    }

    private void deleteCollectionInFireStore(ArrayList<String> classList, String collectionPath) {
        if (classList == null) {
            Log.d("Timetable NULL", "No Old Class in FireStore");
            return;
        }
        for (int i = 0; i < classList.size(); i++) {
            String s = classList.get(i);
            deleteClassInFireStore(s, collectionPath);
        }
    }

    private void deleteClassInFireStore(String documentName, String collectionName) {
        sessionManager
                .getFireStore()
                .collection("Users")
                .document(userID)
                .collection("NUS_Schedule")
                .document("NUS_Schedule")
                .collection(collectionName)
                .document(documentName)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Success", "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Failure", "Error deleting document", e);
                    }
                });
    }

    private void saveTimeTable(ArrayList<String> classList, String collectionPath) {
        if (classList == null) {
            Log.d("Timetable NULL", "Timetable is Null, Not saving it");
            return;
        }
        for (int i = 0; i < classList.size(); i++) {
            String s = classList.get(i);
            NUSClass nusClass = new NUSClass(s);
            saveClassIntoFireStore(nusClass, s, collectionPath);
        }

        sessionManager.getFireStore()
                .collection("Users")
                .document(userID)
                .collection("NUS_Schedule")
                .document("NUS_Schedule")
                .set(nusTimetable)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(GenerateTimetableActivity.this, "Timetable Saved Successfully",Toast.LENGTH_LONG).show();
                        Log.d("SAVE SUCCESS", "Timetable Saved successfully!");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(GenerateTimetableActivity.this, "Timetable did not save", Toast.LENGTH_LONG).show();
                        Log.d("SAVE FAIL", "Timetable Did NOT Save !");
                    }
                });
    }

    private void saveClassIntoFireStore(NUSClass nusClass, String s, String collectionPath) {
        sessionManager.getFireStore()
                .collection("Users")
                .document(userID)
                .collection("NUS_Schedule")
                .document("NUS_Schedule")
                .collection(collectionPath)
                .document(s)
                .set(nusClass, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("CLASS SAVED", "onSuccess: Class is saved");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("CLASS UNSAVED", "onFailure : Class not saved!");
                    }
                });
    }

    private void initVars() {
        textView = findViewById(R.id.textView);
        progressBar = findViewById(R.id.progressBar2);
        settings = findViewById(R.id.settingsButton);
        settings.setOnClickListener(this);
        generate = findViewById(R.id.generateButton);
        generate.setOnClickListener(this);
        next  = findViewById(R.id.nextButton);
        next.setOnClickListener(this);

        sessionManager = SessionManager.get();
        userID = sessionManager.getAuth().getCurrentUser().getUid();
        okHttpClient = new OkHttpClient();

        constraintStrings = new ArrayList<>();
        constraintStrings.add("no8amLessons");
        constraintStrings.add("oneFreeDay");

        nusTimetable = new NUSTimetable();

        save = findViewById(R.id.saveTimetableButton);
        save.setOnClickListener(this);
    }

    private void getRequest(Request request) {
        call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("NETWORK_FAIL", "NETWORK FAIL");
                textView.setText("Network Fail");
                runOnUiThread(() -> {
                    disableButtonBlocker(true);
                });
            }

            @Override
            public void onResponse(Call call, Response response) {
                runOnUiThread(() -> {
                    try {
                        String jsonReturnString = response.body().string();
                        System.out.println(jsonReturnString);
                        if (jsonReturnString.equals("No Feasible Timetable!")) {
                            textView.setText("No Feasible Timetable!\n Change your study plan in Settings!");
                        } else {
                            JSONObject jsonObject = new JSONObject(jsonReturnString);
                            String displayText = (String) jsonObject.get("string");
                            nusTimetable = new NUSTimetable(jsonObject);
                            Log.d("RESPONSE_BODY", displayText);
                            textView.setText(nusTimetable.getStringRep());
                        }
                    } catch (IOException e) {
                        e.getStackTrace();
                    } catch (JSONException e) {
                        textView.setText("Invalid Combination!\n There is NO OTHER solution!");
                    } finally {
                        disableButtonBlocker(true);
                    }
                });
            }
        });

    }

    public void obtainSettings() {
        DocumentReference docRef = sessionManager
                .getFireStore()
                .collection("Users")
                .document(userID)
                .collection("timetableSettings")
                .document("timetableSettings");
        docRef.get().addOnSuccessListener(documentSnapshot -> {
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