package com.example.plannus;

import androidx.annotation.NonNull;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ToDoList extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recyclerView;
    FirebaseFirestore fireStore;
    MyAdapter myAdapter;
    ArrayList<ToDoTask> list;
    private Button createTask;
    private FirebaseAuth mAuth;
    private String userID;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);

        recyclerView = findViewById(R.id.taskList);
        fireStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data...");
        progressDialog.show();

        createTask = findViewById(R.id.createTask);
        createTask.setOnClickListener(this);
        list = new ArrayList<>();
        myAdapter = new MyAdapter(this, list);
        recyclerView.setAdapter(myAdapter);




        EventChangeListener();
    }

    private void EventChangeListener() {
        fireStore.collection("Users").document(this.userID).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                                if (documentSnapshot.exists()) {
                                    Log.d("Successful", "DocumentSnapshot data: " + documentSnapshot.getData());
                                } else {
                                    Log.d("Fail", "No Such Document");
                                }
                            } else {
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                                Log.d("Fail", "get failed with", task.getException());
                            }
                        }
                    });
//    Listener(new EventListener<QuerySnapshot>() {
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
//                        myAdapter.notifyDataSetChanged();
//                        if(progressDialog.isShowing()) {
//                            progressDialog.dismiss();
//                        }
//                    }
//                });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.createTask:
                startActivity(new Intent(this, AddTaskActivity.class));
        }
    }
}