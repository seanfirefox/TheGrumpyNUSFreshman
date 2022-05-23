package com.example.plannus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;

public class ContentMainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button checklistButton, timetableGenerator, calendarButton;
    private ImageButton signOutButton;
    private AnnouncementsAdapter taskListAdapter;
    private TextView wlcMsg;
    private User user;
    private String userID;
    private CollectionReference taskRef;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_main);

        initVars();
        initProfile();

        setUpRecyclerView();
    }

    public void initVars() {
        sessionManager = SessionManager.get();
        userID = sessionManager.getAuth().getCurrentUser().getUid();

        taskRef = sessionManager.getFireStore()
                .collection("Users")
                .document(this.userID)
                .collection("Tasks");

        checklistButton = findViewById(R.id.checklistImgView);
        checklistButton.setOnClickListener(this);

        timetableGenerator = findViewById(R.id.nusModsImgView);
        timetableGenerator.setOnClickListener(this);

        calendarButton = findViewById(R.id.calendarImgView);
        calendarButton.setOnClickListener(this);

        signOutButton = findViewById(R.id.logoutButton);
        signOutButton.setOnClickListener(this);
    }

    private void initProfile() {
        sessionManager.getdRef()
                .child(userID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        user = snapshot.getValue(User.class);
                        wlcMsg = findViewById(R.id.hiName);
                        wlcMsg.setText("Hi " + user.fullName + " !");
                        Log.d("Expose Name", user.fullName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ContentMainActivity.this, "Access got cancelled", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setUpRecyclerView() {
        Query query = taskRef.orderBy("deadLineDate", Query.Direction.ASCENDING)
                .orderBy("deadLineTime", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<ToDoTask> options = new FirestoreRecyclerOptions.Builder<ToDoTask>()
                .setQuery(query, ToDoTask.class)
                .build();
        taskListAdapter = new AnnouncementsAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.mainTaskListAnnouncements);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(taskListAdapter);
    }

    private void logout() {
        sessionManager.getAuth().signOut();
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        taskListAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        taskListAdapter.stopListening();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.checklistImgView) {
            startActivity(new Intent(this, ToDoList.class));
        } else if (v.getId() == R.id.calendarImgView) {
            Toast.makeText(this, "Coming Soon !", Toast.LENGTH_LONG).show();
        } else if (v.getId() == R.id.nusModsImgView) {
            Toast.makeText(this, "Coming Soon !", Toast.LENGTH_LONG).show();
        } else if (v.getId() == R.id.logoutButton){
            logout();
        } else {

        }
    }

}