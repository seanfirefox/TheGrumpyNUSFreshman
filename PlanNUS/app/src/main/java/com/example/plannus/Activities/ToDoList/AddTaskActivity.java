package com.example.plannus.Activities.ToDoList;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.plannus.Objects.ToDoTask;
import com.example.plannus.R;
import com.example.plannus.SessionManager;
import com.example.plannus.utils.DateFormatter;
import com.example.plannus.utils.DateTimeDialog;
import com.example.plannus.utils.TimeFormatter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.SetOptions;


public class AddTaskActivity extends AppCompatActivity implements View.OnClickListener {

    private Button saveTask, dueDate, dueTime, plannedDate, plannedTime;
    private EditText newTask, newTag;
    private TextView statusValue;
    private SeekBar newStatus;
    private String status_text;
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
        userID = sessionManager.getUserID();

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

        statusValue = findViewById(R.id.textViewNewStatus);
        status_text = statusValue.getText().toString().substring(0, 1);
        newStatus.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                statusValue.setText(i + " %");
                status_text = String.valueOf(i);
                Log.e("GET STATUS TEXT",status_text);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
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
        String stats = status_text;
        String tag = newTag.getText().toString().trim();

        String deadlineDate = dueDate.getText().toString().trim();
        if (deadlineDate.equals("Due Date")) {
            Toast.makeText(this, "DUE DATE EMPTY", Toast.LENGTH_SHORT).show();
            return;
        }
        String deadLineDateStore = DateFormatter.dateToNumber(deadlineDate);

        String deadLineTime = dueTime.getText().toString().trim();
        if (deadLineTime.equals("Due Time")) {
            Toast.makeText(this, "DUE TIME EMPTY", Toast.LENGTH_SHORT).show();
            return;
        }
        String deadLineTimeStore = TimeFormatter.timeToNumber(deadLineTime);

        String deadLineDateTime = deadLineDateStore + deadLineTimeStore;

        String planDate = plannedDate.getText().toString().trim();
        if (planDate.equals("Planned Date")) {
            Toast.makeText(this, "PLANNED DATE EMPTY", Toast.LENGTH_SHORT).show();
            return;
        }
        String planDateStore = DateFormatter.dateToNumber(planDate);

        String planTime = plannedTime.getText().toString().trim();
        if (planTime.equals("Planned Time")) {
            Toast.makeText(this, "PLANNED Time EMPTY", Toast.LENGTH_SHORT).show();
            return;
        }
        String planTimeStore = TimeFormatter.timeToNumber(planTime);

        String planDateTime = planDateStore + planTimeStore;

        Log.d("QUERY SUCCESS", "Query of Addition was successful");
        ToDoTask newTask = new ToDoTask(tag, task, stats, deadLineDateTime, planDateTime);
        sessionManager.getDocRef(userID, "Tasks", task + tag)
                .set(newTask, SetOptions.merge())
                .addOnSuccessListener((OnSuccessListener<? super Void>) (aVoid) -> {
            Log.d("TaskCreated", "onSuccess: Task is created");
            Toast.makeText(AddTaskActivity.this, "Task added Successfully",Toast.LENGTH_LONG).show();
            finish();
        } ).addOnFailureListener(e -> {
            Log.d("TaskFail", "onFailure: "+ e);
            Toast.makeText(AddTaskActivity.this, "Failed to Create Task", Toast.LENGTH_LONG).show();
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

