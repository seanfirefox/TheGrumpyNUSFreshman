package com.example.plannus.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.plannus.Adaptors.CompletedTaskAdapter;
import com.example.plannus.R;
import com.example.plannus.SessionManager;
import com.example.plannus.Objects.ToDoTask;
import com.example.plannus.WrapContentLinearLayoutManager;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;

public class CompletedTaskActivity extends AppCompatActivity {

    private CompletedTaskAdapter completedListAdapter;
    private SessionManager sessionManager;
    private String userID;
    private CollectionReference taskRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_task);
        initVars();
        setUpRecyclerView();
    }

    public void initVars() {
        sessionManager = SessionManager.get();
        userID = sessionManager.getAuth().getCurrentUser().getUid();
        taskRef = sessionManager.getFireStore()
                .collection("Users")
                .document(this.userID)
                .collection("Tasks");
    }

    private void setUpRecyclerView() {
        Query query = taskRef.whereEqualTo("status", "100");

        FirestoreRecyclerOptions<ToDoTask> options = new FirestoreRecyclerOptions.Builder<ToDoTask>()
                .setQuery(query, ToDoTask.class)
                .build();
        completedListAdapter = new CompletedTaskAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.completedTaskList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(completedListAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        completedListAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        completedListAdapter.stopListening();
    }
}