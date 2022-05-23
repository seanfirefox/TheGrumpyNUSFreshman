package com.example.plannus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;
import android.app.DatePickerDialog;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.SetOptions;

import java.util.Locale;


public class AddTaskActivity extends AppCompatActivity implements View.OnClickListener {

    private Button saveTask;
    private ImageButton backButton;
    private EditText newTask, newStatus, newTag, dueDate, dueTime, plannedDate, plannedTime;
    private String userID;
    private SessionManager sessionManager;
    private DateTimeDialog dateTimePicker;
    private int hour;
    private int min;

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

        dueTime = findViewById(R.id.dueTimeEditText);
        dueTime.setOnClickListener(this);

        plannedDate = findViewById(R.id.plannedDateEditText);
        plannedDate.setOnClickListener(this);

        plannedTime = findViewById(R.id.plannedTimeEditText);
        plannedTime.setOnClickListener(this);

        backButton = findViewById(R.id.imageButtonAddTask);
        backButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.saveButton) {
            addTask();
        } else if (v.getId() == R.id.dueDateButton) {
            calendarDialog(dueDate);
        } else if (v.getId() == R.id.plannedDateEditText) {
            calendarDialog(plannedDate);
        } else if (v.getId() == R.id.dueTimeEditText) {
            timeDialog(dueTime);
        } else if (v.getId() == R.id.plannedTimeEditText) {
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
                Log.d("TaskFail", "onFailure: "+ e);
                Toast.makeText(AddTaskActivity.this, "Failed to Create Task", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void calendarDialog(EditText dialog) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                AddTaskActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth + "/" + month + "/" + year;
                dialog.setText(date);
            }
        }, dateTimePicker.getYear(), dateTimePicker.getMonth(), dateTimePicker.getDay());
        datePickerDialog.show();
    }

    public void timeDialog(EditText dialog) {
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                hour = hourOfDay;
                min = minute;
                dialog.setText(String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute));
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, timeSetListener, hour, min, true);
        timePickerDialog.show();
    }
}

