package com.example.plannus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;


public class RegisterUser extends AppCompatActivity implements View.OnClickListener {

    private TextView registerUser;
    private ImageView banner;
    private EditText editTextFullName, editTextAge, editTextEmail, editTextPassword;
    private ProgressBar progressBar;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        sessionManager = SessionManager.get();
        initVars();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.registerUserLogo) {
            startActivity(new Intent(this, MainActivity.class));
        } else {
            registerUser();
            if (sessionManager.getRegisterStatus()) {
                Toast.makeText(this, "Registered successfully, please go back to Main Page to login", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Failed to register, please try again", Toast.LENGTH_LONG).show();
            }
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

        progressBar.setVisibility(View.GONE);

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