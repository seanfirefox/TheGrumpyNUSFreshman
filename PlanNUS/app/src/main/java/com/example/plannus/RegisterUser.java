package com.example.plannus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import javax.inject.Inject;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {

    private TextView registerUser;
    private ImageView banner;
    private EditText editTextFullName, editTextAge, editTextEmail, editTextPassword;
    private ProgressBar progressBar;
    private FirebaseFirestore mStore;
    private FirebaseAuth mAuth;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        sessionManager = SessionManager.get();

        initVars();
        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.registerUserLogo) {
            startActivity(new Intent(this, MainActivity.class));
        } else {
            registerUser();
        }
    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String fullName = editTextFullName.getText().toString().trim();
        String age = editTextAge.getText().toString().trim();

        if (!credentialsCheck(fullName, age, email, password)) {
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        sessionManager.register(new User(fullName, age, email, password));

    }

    public void initVars() {
        banner = findViewById(R.id.registerUserLogo);
        banner.setOnClickListener(this);

        registerUser = findViewById(R.id.registerUser);
        registerUser.setOnClickListener(this);

        editTextFullName = findViewById(R.id.fullName);
        editTextAge = findViewById(R.id.age);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);

        progressBar = findViewById(R.id.progressBar);
    }

    public boolean emailCheck(String email) {
        return !(email.isEmpty()) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public boolean particularsCheck(String fullName, String age) {
        return !(fullName.isEmpty()) && !(age.isEmpty());
    }

    public boolean passwordCheck(String password) {
        return !(password.isEmpty()) && !(password.length() < 6);
    }

    public boolean credentialsCheck(String fullName, String age, String email, String password) {
        return particularsCheck(fullName, age) && emailCheck(email) && passwordCheck(password);
    }
}