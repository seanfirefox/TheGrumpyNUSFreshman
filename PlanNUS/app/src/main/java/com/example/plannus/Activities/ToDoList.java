package com.example.plannus.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.plannus.R;
import com.example.plannus.SessionManager;
import com.example.plannus.Adaptors.ToDoListAdapter;
import com.example.plannus.Objects.ToDoTask;
import com.example.plannus.WrapContentLinearLayoutManager;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;


public class ToDoList extends AppCompatActivity implements View.OnClickListener {

    private String userID;
    private CollectionReference taskRef;
    private ToDoListAdapter adapter;
    private Button createTask, completedTask;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);
        initVars();
        setUpRecyclerView();
    }

    public void initVars() {

        sessionManager = SessionManager.get();
        userID = sessionManager.getAuth()
                .getCurrentUser()
                .getUid();

        createTask = findViewById(R.id.createTask);
        createTask.setOnClickListener(this);

        completedTask = findViewById(R.id.milestone);
        completedTask.setOnClickListener(this);

        taskRef = sessionManager.getFireStore()
                .collection("Users")
                .document(this.userID)
                .collection("Tasks");
    }

    private void setUpRecyclerView() {
        Query query = taskRef.orderBy("status", Query.Direction.ASCENDING)
                .orderBy("deadLineDateTime", Query.Direction.ASCENDING)
                .whereNotEqualTo("status","100");

        FirestoreRecyclerOptions<ToDoTask> options = new FirestoreRecyclerOptions.Builder<ToDoTask>()
                .setQuery(query, ToDoTask.class)
                .build();
        adapter = new ToDoListAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.taskListAnnouncements);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(adapter);

        slider().attachToRecyclerView(recyclerView);

    }

    private ItemTouchHelper slider() {
        return new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.deleteItem(viewHolder.getBindingAdapterPosition());
            }
        });
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
        if (v.getId() == R.id.createTask) {
            startActivity(new Intent(this, AddTaskActivity.class));
        } else if (v.getId() == R.id.milestone) {
            startActivity(new Intent(this, CompletedTaskActivity.class));
        }
    }
}