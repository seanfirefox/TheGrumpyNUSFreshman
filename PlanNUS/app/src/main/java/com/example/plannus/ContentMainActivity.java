package com.example.plannus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ContentMainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton checklistImgView;
    private FirebaseFirestore fireStore;
    private AnnouncementsAdapter taskListAdapter;

    private FirebaseAuth fAuth;
    private DatabaseReference dRef;
    private TextView wlcMsg;
    private User user;
    private String userID;
    private CollectionReference taskRef;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_main);

        sessionManager = SessionManager.get();
        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        initProfile();

        fireStore = FirebaseFirestore.getInstance();
        taskRef = fireStore.collection("Users")
                .document(this.userID)
                .collection("Tasks");

        checklistImgView = findViewById(R.id.checklistImgView);
        checklistImgView.setOnClickListener(this);

        setUpRecyclerView();
    }


    private void initProfile() {
//        dRef = FirebaseDatabase
//                .getInstance("https://plannus-cad5f-default-rtdb.asia-southeast1.firebasedatabase.app/")
//                .getReference("Users")
//                .child(userID);
//        dRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                user = snapshot.getValue(User.class);
//                wlcMsg = findViewById(R.id.hiName);
//                wlcMsg.setText("Hi " + user.fullName + " !");
//                Log.d("Expose Name", user.fullName);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        user = sessionManager.getUser();
        if (user == null) {
            Toast.makeText(ContentMainActivity.this, "Access got cancelled", Toast.LENGTH_LONG).show();
        } else {
            wlcMsg = findViewById(R.id.hiName);
            wlcMsg.setText("Hi " + user.fullName + " !");
        }

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
        switch (v.getId()) {
            case R.id.checklistImgView:
                startActivity(new Intent(this, ToDoList.class));
        }
    }

}