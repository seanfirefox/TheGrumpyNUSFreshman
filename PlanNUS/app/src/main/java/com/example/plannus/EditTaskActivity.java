package com.example.plannus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.SetOptions;

import java.util.Arrays;

public class EditTaskActivity extends AppCompatActivity implements View.OnClickListener {

    private Button editButton, editDueDate, editDueTime , editPlannedDate, editPlannedTime;
    private String userId;
    private String[] taskInfo;
    private String task;
    private SessionManager sessionManager;
    private EditText editTask, editStatus, editTag;
    private DateTimeDialog dateTimePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        initVars();
        renderVars();
        editButton = findViewById(R.id.editButton);
        editButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.editButton) {
            editTask();
            finish();
        } else if (v.getId() == R.id.editDueDateButton) {
            calendarDialog(editDueDate);
        } else if (v.getId() == R.id.editPlannedDateButton) {
            calendarDialog(editPlannedDate);
        } else if (v.getId() == R.id.editDueTimeButton) {
            timeDialog(editDueTime);
        } else if (v.getId() == R.id.editPlannedTimeButton) {
            timeDialog(editPlannedTime);
        } else {}
    }

    public void initVars() {
        sessionManager = SessionManager.get();
        dateTimePicker = DateTimeDialog.getInstance();
        userId = sessionManager.getAuth().getCurrentUser().getUid();
        taskInfo = getIntent().getStringArrayExtra("taskInfo");
        task = taskInfo[1];
        Log.e("check", Arrays.toString(taskInfo));

        editTask = findViewById(R.id.editTaskDesc);
        editStatus = findViewById(R.id.editStatusDesc);
        editTag = findViewById(R.id.editTag);
        editDueDate = findViewById(R.id.editDueDateButton);
        editDueDate.setOnClickListener(this);

        editDueTime = findViewById(R.id.editDueTimeButton);
        editDueTime.setOnClickListener(this);

        editPlannedDate = findViewById(R.id.editPlannedDateButton);
        editPlannedDate.setOnClickListener(this);

        editPlannedTime = findViewById(R.id.editPlannedTimeButton);
        editPlannedTime.setOnClickListener(this);
    }

    public void renderVars() {
        editTask.setText(task, TextView.BufferType.EDITABLE);
        editStatus.setText(taskInfo[2], TextView.BufferType.EDITABLE);
        editTag.setText(taskInfo[0], TextView.BufferType.EDITABLE);
        editDueDate.setText(taskInfo[3], TextView.BufferType.EDITABLE);
        editDueTime.setText(taskInfo[4], TextView.BufferType.EDITABLE);
        editPlannedDate.setText(taskInfo[5], TextView.BufferType.EDITABLE);
        editPlannedTime.setText(taskInfo[6], TextView.BufferType.EDITABLE);
    }

    private void editTask() {
        String editedTask = editTask.getText().toString().trim();
        String stats = editStatus.getText().toString().trim();
        String tag = editTag.getText().toString().trim();
        String deadlineDate = editDueDate.getText().toString().trim();
        String deadlineDateStore = DateFormatter.dateToNumber(deadlineDate);
        String deadLineTime = editDueTime.getText().toString().trim();
        String planDate = editPlannedDate.getText().toString().trim();
        String planDateStore = DateFormatter.dateToNumber(planDate);
        String planTime = editPlannedTime.getText().toString().trim();

        if (!(editedTask.equals(task))) {
            deleteTask();
        }

        ToDoTask t = new ToDoTask(tag, editedTask, stats, deadlineDateStore, deadLineTime, planDateStore, planTime);
        DocumentReference docRef = sessionManager.getFireStore()
                .collection("Users")
                .document(userId)
                .collection("Tasks")
                .document(editedTask);
        docRef.set(t, SetOptions.merge()).addOnSuccessListener((OnSuccessListener<? super Void>) (aVoid) -> {
            Log.d("TaskCreated", "onSuccess: Task is created");
        } ).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TaskFail", "onFailure: "+ e);
            }
        });
    }

    private void deleteTask() {
        sessionManager.getFireStore()
                .collection("Users")
                .document(userId)
                .collection("Tasks")
                .document(task)
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
        Log.e("Successful", task);
    }

    public void calendarDialog(Button dialog) {
        DatePickerDialog.OnDateSetListener dateSetListener = dateTimePicker.initDate(dialog);
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                EditTaskActivity.this,
                dateSetListener,
                dateTimePicker.getYear(),
                dateTimePicker.getMonth(),
                dateTimePicker.getDay());
        datePickerDialog.show();
    }

    public void timeDialog(Button dialog) {
        TimePickerDialog.OnTimeSetListener timeSetListener = dateTimePicker.initTIme(dialog);
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                EditTaskActivity.this,
                timeSetListener,
                dateTimePicker.getHour(),
                dateTimePicker.getMinute(),
                true);
        timePickerDialog.show();
    }
}