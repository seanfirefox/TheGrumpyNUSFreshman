package com.example.plannus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class AddTaskActivity extends AppCompatActivity implements View.OnClickListener {

    private Button saveTask;
    private ImageButton backButton;
    private EditText newTask, newStatus, newTag, dueDate, dueTime, plannedDate, plannedTime;
    private FirebaseFirestore fDataBase;
    private FirebaseAuth fAuth;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        fAuth = FirebaseAuth.getInstance();
        fDataBase = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();

        saveTask = findViewById(R.id.saveButton);
        saveTask.setOnClickListener(this);

        newTask = findViewById(R.id.taskDescEditText);
        newStatus = findViewById(R.id.taskDescEditStatus);
        newTag = findViewById(R.id.taskTypeEditText);
        dueDate = findViewById(R.id.dueDateEditText);
        dueTime = findViewById(R.id.dueTimeEditText);
        plannedDate = findViewById(R.id.plannedDateEditText);
        plannedTime = findViewById(R.id.plannedTimeEditText);

        backButton = findViewById(R.id.imageButtonAddTask);
        backButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.saveButton:
                addTask();
                // finish();
                break;
        }
    }

    private void addTask() {
        String task = newTask.getText().toString().trim();
        String stats = newStatus.getText().toString().trim();
        String tag = newTag.getText().toString().trim();
        String deadlineDate = dueDate.getText().toString().trim();
        String deadLineTime = dueTime.getText().toString().trim();
        String planDate = plannedDate.getText().toString().trim();
        String planTime = plannedTime.getText().toString().trim();

        ToDoTask t = new ToDoTask(tag, task, stats, deadlineDate, deadLineTime, planDate, planTime);
        DocumentReference docRef = fDataBase.collection("Users").document(userID).collection("Tasks").document(task);
        docRef.set(t, SetOptions.merge()).addOnSuccessListener((OnSuccessListener<? super Void>) (aVoid) -> {
            Log.d("TaskCreated", "onSuccess: Task is created");
        } ).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TaskFail", "onFailure: "+ e.toString());
            }
        });
    }
}

