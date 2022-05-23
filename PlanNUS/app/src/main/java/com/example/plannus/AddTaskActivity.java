package com.example.plannus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.app.DatePickerDialog;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.SetOptions;


public class AddTaskActivity extends AppCompatActivity implements View.OnClickListener {

    private Button saveTask, dueDate, dueTime, plannedDate, plannedTime;
    private EditText newTask, newStatus, newTag;
    private String userID;
    private SessionManager sessionManager;
    private DateTimeDialog dateTimePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        initVars();
    }

    public void initVars() {
        sessionManager = SessionManager.get();
        dateTimePicker = DateTimeDialog.getInstance();
        userID = sessionManager.getAuth()
                .getCurrentUser()
                .getUid();

        saveTask = findViewById(R.id.saveButton);
        saveTask.setOnClickListener(this);

        newTask = findViewById(R.id.taskDescEditText);
        newStatus = findViewById(R.id.taskDescEditStatus);
        newTag = findViewById(R.id.taskTypeEditText);
        dueDate = findViewById(R.id.dueDateButton);
        dueDate.setOnClickListener(this);

        dueTime = findViewById(R.id.dueTimeButton);
        dueTime.setOnClickListener(this);

        plannedDate = findViewById(R.id.plannedDateButton);
        plannedDate.setOnClickListener(this);

        plannedTime = findViewById(R.id.plannedTimeButton);
        plannedTime.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.saveButton) {
            addTask();
        } else if (v.getId() == R.id.dueDateButton) {
            calendarDialog(dueDate);
        } else if (v.getId() == R.id.plannedDateButton) {
            calendarDialog(plannedDate);
        } else if (v.getId() == R.id.dueTimeButton) {
            timeDialog(dueTime);
        } else if (v.getId() == R.id.plannedTimeButton) {
            timeDialog(plannedTime);
        } else {
            Log.d("No Other Button", "No Other button");
        }
    }

    private void addTask() {
        String task = newTask.getText().toString().trim();
        String stats = newStatus.getText().toString().trim();
        String tag = newTag.getText().toString().trim();

        String deadlineDate = dueDate.getText().toString().trim();
        String deadLineDateStore = DateFormatter.dateToNumber(deadlineDate);

        String deadLineTime = dueTime.getText().toString().trim();

        String planDate = plannedDate.getText().toString().trim();
        String planDateStore = DateFormatter.dateToNumber(planDate);

        String planTime = plannedTime.getText().toString().trim();

        Log.d("QUERY SUCCESS", "Query of Addition was successful");
        ToDoTask newTask = new ToDoTask(tag, task, stats, deadLineDateStore, deadLineTime, planDateStore, planTime);
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
                Log.d("TaskFail", "onFailure: "+ e);
                Toast.makeText(AddTaskActivity.this, "Failed to Create Task", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void calendarDialog(Button dialog) {
        DatePickerDialog.OnDateSetListener dateSetListener = dateTimePicker.initDate(dialog);
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                AddTaskActivity.this,
                dateSetListener,
                dateTimePicker.getYear(),
                dateTimePicker.getMonth(),
                dateTimePicker.getDay());
        datePickerDialog.show();
    }

    public void timeDialog(Button dialog) {
        TimePickerDialog.OnTimeSetListener timeSetListener = dateTimePicker.initTIme(dialog);
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                AddTaskActivity.this,
                timeSetListener,
                dateTimePicker.getHour(),
                dateTimePicker.getMinute(),
                true);
        timePickerDialog.show();
    }
}

