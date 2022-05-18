package com.example.plannus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class ContentMainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton checklistImgView;
    private FirebaseAuth fAuth;
    private DatabaseReference dRef;
    private TextView wlcMsg;
    User user;
    String name;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_main);

        fAuth = FirebaseAuth.getInstance();
        // name = fAuth.getCurrentUser().getDisplayName();
        userID = fAuth.getCurrentUser().getUid();
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

        Log.d("see dRef", dRef.toString());


        checklistImgView = findViewById(R.id.checklistImgView);
        checklistImgView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.checklistImgView:
                startActivity(new Intent(this, AddTaskActivity.class));
        }
    }

}