package com.example.plannus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Arrays;

public class EditTaskActivity extends AppCompatActivity implements View.OnClickListener {

    private Button editButton;
    private FirebaseFirestore fireStore;
    private FirebaseAuth mAuth;
    private String userId;
    private String[] taskInfo;
    private String task;
    private EditText editTask, editStatus, editTag, editDueDate, editDueTime , editPlannedDate, editPlannedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        fireStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        taskInfo = (String[]) getIntent().getStringArrayExtra("taskInfo");
        task = taskInfo[1];
        Log.e("check", Arrays.toString(taskInfo));

        editTask = findViewById(R.id.editTaskDesc);
        editStatus = findViewById(R.id.editStatusDesc);
        editTag = findViewById(R.id.editTag);
        editDueDate = findViewById(R.id.editDueDate);
        editDueTime = findViewById(R.id.editDueTime);
        editPlannedDate = findViewById(R.id.editPlannedDate);
        editPlannedTime = findViewById(R.id.editPlannedTime);

        editTask.setText(task, TextView.BufferType.EDITABLE);
        editStatus.setText(taskInfo[2], TextView.BufferType.EDITABLE);
        editTag.setText(taskInfo[0], TextView.BufferType.EDITABLE);
        editDueDate.setText(taskInfo[3], TextView.BufferType.EDITABLE);
        editDueTime.setText(taskInfo[4], TextView.BufferType.EDITABLE);
        editPlannedDate.setText(taskInfo[5], TextView.BufferType.EDITABLE);
        editPlannedTime.setText(taskInfo[6], TextView.BufferType.EDITABLE);

        editButton = findViewById(R.id.editButton);
        editButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editButton:
                editask();
                break;
        }
    }

    private void editask() {
        String editedTask = editTask.getText().toString().trim();
        String stats = editStatus.getText().toString().trim();
        String tag = editTag.getText().toString().trim();
        String deadlineDate = editDueDate.getText().toString().trim();
        String deadLineTime = editDueTime.getText().toString().trim();
        String planDate = editPlannedDate.getText().toString().trim();
        String planTime = editPlannedTime.getText().toString().trim();

        if (!(editedTask.equals(task))) {
            deleteTask();
        }

        ToDoTask t = new ToDoTask(tag, editedTask, stats, deadlineDate, deadLineTime, planDate, planTime);
        DocumentReference docRef = fireStore.collection("Users")
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
        fireStore.collection("Users")
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
}