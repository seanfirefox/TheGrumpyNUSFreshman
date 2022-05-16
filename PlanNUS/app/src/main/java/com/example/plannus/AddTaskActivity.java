package com.example.plannus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

public class AddTaskActivity extends AppCompatActivity implements View.OnClickListener {

    private Button saveTask;
    private ImageButton backButton;
    private EditText newTask, newStatus, newTag, dueDate, dueTime, plannedDate, plannedTime;
    private FirebaseDatabase dataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        dataBase = FirebaseDatabase.getInstance("https://plannus-cad5f-default-rtdb.asia-southeast1.firebasedatabase.app/");

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

        DatabaseReference ref = dataBase.getReference("Users");
        ToDoTask t = new ToDoTask(tag, task, stats, deadlineDate);
        ref.child("Users").child(task).setValue(t);

    }

}

