package com.example.plannus.Activities.ToDoList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.plannus.R;
import com.example.plannus.SessionManager;
import com.example.plannus.utils.TimeFormatter;
import com.example.plannus.Objects.ToDoTask;
import com.example.plannus.utils.DateFormatter;
import com.example.plannus.utils.DateTimeDialog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.SetOptions;

import java.util.Arrays;

public class EditTaskActivity extends AppCompatActivity implements View.OnClickListener {

    private Button editButton, editDueDate, editDueTime , editPlannedDate, editPlannedTime;
    private String userId;
    private String[] taskInfo;
    private String task, tagName, statusValue;
    private SessionManager sessionManager;
    private EditText editTask, editTag;
    private TextView editStatusText;
    private SeekBar editStatus;
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
        } else if (v.getId() == R.id.editDueDateButton) {
            calendarDialog(editDueDate);
        } else if (v.getId() == R.id.editPlannedDateButton) {
            calendarDialog(editPlannedDate);
        } else if (v.getId() == R.id.editDueTimeButton) {
            timeDialog(editDueTime);
        } else if (v.getId() == R.id.editPlannedTimeButton) {
            timeDialog(editPlannedTime);
        }
    }

    public void initVars() {
        sessionManager = SessionManager.get();
        dateTimePicker = DateTimeDialog.getInstance();
        userId = sessionManager.getUserID();
        taskInfo = getIntent().getStringArrayExtra("taskInfo");
        task = taskInfo[1];
        tagName = taskInfo[0];
        Log.e("check", Arrays.toString(taskInfo));

        editStatusText = findViewById(R.id.textViewStatus);
        statusValue = editStatusText.getText().toString().substring(0, 1);;
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

        editStatus.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                statusValue = String.valueOf(i);
                editStatusText.setText(statusValue);
                Log.d("Edit Task Seek Bar Status Change", statusValue);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void renderVars() {
        editTask.setText(task, TextView.BufferType.EDITABLE);
        editStatus.setProgress(Integer.valueOf(taskInfo[2]));
        editStatusText.setText(taskInfo[2] + "%", TextView.BufferType.EDITABLE);
        editTag.setText(taskInfo[0], TextView.BufferType.EDITABLE);
        editDueDate.setText(taskInfo[3], TextView.BufferType.EDITABLE);
        editDueTime.setText(taskInfo[4], TextView.BufferType.EDITABLE);
        editPlannedDate.setText(taskInfo[5], TextView.BufferType.EDITABLE);
        editPlannedTime.setText(taskInfo[6], TextView.BufferType.EDITABLE);
        ((TextView) findViewById(R.id.editTasks)).setText("Edit " + task);
    }

    private void editTask() {

        String editedTask = editTask.getText().toString().trim();
        String stats = statusValue;
        String tag = editTag.getText().toString().trim();
        String deadlineDate = editDueDate.getText().toString().trim();
        String deadLineDateStore = DateFormatter.dateToNumber(deadlineDate);

        String deadLineTime = editDueTime.getText().toString().trim();
        String deadLineTimeStore = TimeFormatter.timeToNumber(deadLineTime);

        String deadLineDateTime = deadLineDateStore + deadLineTimeStore;

        String planDate = editPlannedDate.getText().toString().trim();
        String planDateStore = DateFormatter.dateToNumber(planDate);

        String planTime = editPlannedTime.getText().toString().trim();
        String planTimeStore = TimeFormatter.timeToNumber(planTime);

        String planDateTime = planDateStore + planTimeStore;
        String editedTaskHeader = editedTask + tag;
        String oldTaskHeader = task + tagName;
        if (!editedTaskHeader.equals(oldTaskHeader)) {
            deleteTask();
        }

        ToDoTask t = new ToDoTask(tag, editedTask, stats, deadLineDateTime, planDateTime);
        sessionManager.getDocRef(userId, "Tasks", editedTaskHeader)
                .set(t, SetOptions.merge()).addOnSuccessListener((OnSuccessListener<? super Void>) (aVoid) -> {
                    Log.d("TaskCreated", "onSuccess: Task is created");
                    finish();
                } ).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TaskFail", "onFailure: "+ e);
                    }
                });
        }

    private void deleteTask() {
        sessionManager.getDocRef(userId, "Tasks", task + tagName)
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