package com.example.plannus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


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
    private String userID;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        initVars();
    }

    public void initVars() {
        sessionManager = SessionManager.get();
        userID = sessionManager.getAuth()
                .getCurrentUser()
                .getUid();

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
        if (v.getId() == R.id.saveButton) {
            addTask();
        } else {
            Log.d("No Other Button", "No Other button");
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

        Log.d("QUERY SUCCESS", "Query of Addition was successful");
        ToDoTask newTask = new ToDoTask(tag, task, stats, deadlineDate, deadLineTime, planDate, planTime);
        DocumentReference docRef = sessionManager.getFireStore()
                .collection("Users")
                .document(userID)
                .collection("Tasks")
                .document(task);
        docRef.set(newTask, SetOptions.merge())
                .addOnSuccessListener((OnSuccessListener<? super Void>) (aVoid) -> {
            Log.d("TaskCreated", "onSuccess: Task is created");
            Toast.makeText(AddTaskActivity.this, "Task added Successfully",Toast.LENGTH_LONG).show();
            startActivity(new Intent(AddTaskActivity.this, ToDoList.class));
        } ).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TaskFail", "onFailure: "+ e.toString());
                Toast.makeText(AddTaskActivity.this, "Failed to Create Task", Toast.LENGTH_LONG).show();
            }
        });
    }
}

