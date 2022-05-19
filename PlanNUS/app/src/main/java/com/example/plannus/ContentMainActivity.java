package com.example.plannus;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ContentMainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton checklistImgView;
    private RecyclerView recyclerView;
    private FirebaseFirestore fireStore;
    private AnnouncementsAdapter taskListAdapter;
    private ArrayList<ToDoTask> taskList;
    private FirebaseAuth fAuth;
    private DatabaseReference dRef;
    private TextView wlcMsg;
    private User user;
    private String name;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_main);

        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        initProfile();

        recyclerView = findViewById(R.id.taskListAnnouncements);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fireStore = FirebaseFirestore.getInstance();
        taskList = new ArrayList<ToDoTask>();
        taskListAdapter = new AnnouncementsAdapter(ContentMainActivity.this, taskList);
        recyclerView.setAdapter(taskListAdapter);

        checklistImgView = findViewById(R.id.checklistImgView);
        checklistImgView.setOnClickListener(this);

        EventChangeListener();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.checklistImgView:
                startActivity(new Intent(this, ToDoList.class));
        }
    }

    private void initProfile() {
        dRef = FirebaseDatabase
                .getInstance("https://plannus-cad5f-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Users")
                .child(userID);
        dRef.addValueEventListener(new ValueEventListener() {
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

    private void EventChangeListener() {
        fireStore.collection("Users")
                .document(this.userID)
                .collection("Tasks")
                .orderBy("deadLineDate", Query.Direction.ASCENDING)
                .orderBy("deadLineTime", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e("Firestore Error", error.getMessage());
                            return;
                        }
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                taskList.add(dc.getDocument().toObject(ToDoTask.class));
                            }
                        }
                        taskListAdapter.notifyDataSetChanged();
                    }
                });
    }

}