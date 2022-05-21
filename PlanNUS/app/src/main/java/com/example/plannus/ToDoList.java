package com.example.plannus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class ToDoList extends AppCompatActivity implements View.OnClickListener {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String userID;
    private CollectionReference taskRef;
    private ToDoListAdapter adapter;
    private Button createTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);

        userID = mAuth.getCurrentUser().getUid();
        taskRef = db.collection("Users")
                .document(this.userID)
                .collection("Tasks");
        createTask = findViewById(R.id.createTask);
        createTask.setOnClickListener(this);

        setUpRecyclerView();
    }

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

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.deleteItem(viewHolder.getBindingAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);

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