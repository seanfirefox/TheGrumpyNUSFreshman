package com.example.plannus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Button;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView register;
    private EditText emailAddress;
    private EditText passWord;
    private ProgressBar progressBar;
    private Button loginButton;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sessionManager = SessionManager.get();
        initVars();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.register) {
            startActivity(new Intent(this, RegisterUser.class));
        } else {
            tryLogin();
        }
    }


    public void tryLogin() {
        String email = this.emailAddress.getText().toString().trim();
        String password = this.passWord.getText().toString().trim();

        if (!credentialCheck(email, password)) {
            Toast.makeText(MainActivity.this, "Invalid Credentials", Toast.LENGTH_LONG).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        sessionManager.login(email, password);


        if (sessionManager.getLoginStatus()) {
            startActivity(new Intent(MainActivity.this, ContentMainActivity.class));
        } else {
            Toast.makeText(MainActivity.this, "Failed to login, try again. At least one of your email address or password is invalid", Toast.LENGTH_LONG).show();
        }
    }

    public void initVars() {
        register = findViewById(R.id.register);
        register.setOnClickListener(this);

        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);

        emailAddress = findViewById(R.id.emailAddress);
        passWord = findViewById(R.id.passWord);
        progressBar = findViewById(R.id.progressBar);
    }

    public boolean passwordCheck(String password) {
        return !(password.isEmpty()) && !(password.length() < 6);
    }

    public boolean emailCheck(String email) {
        return !(email.isEmpty()) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public boolean credentialCheck(String email, String password) {
        return passwordCheck(password) && emailCheck(email);
    }



}