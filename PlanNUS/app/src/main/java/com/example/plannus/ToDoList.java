package com.example.plannus;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ToDoList extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private FirebaseFirestore fireStore;
    private ToDoListAdapter toDoListAdapter;
    private ArrayList<ToDoTask> list;
    private Button createTask;
    private ProgressDialog progressDialog;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String userID;
    private CollectionReference taskRef;
    private ToDoListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);

        userID = mAuth.getCurrentUser().getUid();
        taskRef = db.collection("Users")
                .document(this.userID)
                .collection("Tasks");

        setUpRecyclerView();

//        recyclerView = findViewById(R.id.taskListAnnouncements);
//        fireStore = FirebaseFirestore.getInstance();
//        mAuth = FirebaseAuth.getInstance();
//        userID = mAuth.getCurrentUser().getUid();
//
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        progressDialog = new ProgressDialog(this);
//        progressDialog.setCancelable(false);
//        progressDialog.setMessage("Fetching Data...");
//        progressDialog.show();
//
//        createTask = findViewById(R.id.createTask);
//        createTask.setOnClickListener(this);
//        list = new ArrayList<ToDoTask>();
//        toDoListAdapter = new ToDoListAdapter(ToDoList.this, list);
//        recyclerView.setAdapter(toDoListAdapter);
//
//        EventChangeListener();
    }

//    private void EventChangeListener() {
//        fireStore.collection("Users")
//                .document(this.userID)
//                .collection("Tasks")
//                .orderBy("deadLineTime", Query.Direction.ASCENDING)
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                        if (error != null) {
//                            if(progressDialog.isShowing()) {
//                                progressDialog.dismiss();
//                            }
//                            Log.e("Firestore error", error.getMessage());
//                            return;
//                        }
//                        for (DocumentChange dc : value.getDocumentChanges()) {
//                            if(dc.getType() == DocumentChange.Type.ADDED) {
//                                list.add(dc.getDocument().toObject(ToDoTask.class));
//                            }
//                        }
//
//                        toDoListAdapter.notifyDataSetChanged();
//                        if(progressDialog.isShowing()) {
//                            progressDialog.dismiss();
//                        }
//                    }
//                });
//    }

    private void setUpRecyclerView() {
        Query query = taskRef.orderBy("deadLineDate", Query.Direction.ASCENDING)
                .orderBy("deadLineTime", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<ToDoTask> options = new FirestoreRecyclerOptions.Builder<ToDoTask>()
                .setQuery(query, ToDoTask.class)
                .build();
        adapter = new ToDoListAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.taskListAnnouncements);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.createTask:
                startActivity(new Intent(this, AddTaskActivity.class));
        }
    }
}