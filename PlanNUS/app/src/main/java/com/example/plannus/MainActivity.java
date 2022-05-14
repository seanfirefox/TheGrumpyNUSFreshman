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

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.OnCompleteListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView register;
    private EditText emailAddress;
    private EditText passWord;
    private ProgressBar progressBar;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(this);

        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);

        emailAddress = (EditText) findViewById(R.id.emailAddress);
        passWord = (EditText) findViewById(R.id.passWord);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register:
                startActivity(new Intent(this, RegisterUser.class));
                break;
            case R.id.loginButton:
                tryLogin();
                break;
        }
    }

    public void tryLogin() {
        String email = this.emailAddress.getText().toString().trim();
        String password = this.passWord.getText().toString().trim();

        // Email Format check
        if (email.equals("")) {
            this.emailAddress.setError("Email is required to login!");
            this.emailAddress.requestFocus();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            this.emailAddress.setError("Invalid Email Format!");
            this.emailAddress.requestFocus();
        } else {}

        // Password Format Check
        if(password.isEmpty()) {
            this.passWord.setError("Password is required!");
            this.passWord.requestFocus();
            return;
        } else if(password.length() < 6) {
            this.passWord.setError("Min password length should be 6 characters!");
            this.passWord.requestFocus();
            return;
        } else {}

        progressBar.setVisibility(View.VISIBLE);

        FirebaseAuth dataBase = FirebaseAuth.getInstance();
        dataBase.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                         if (task.isSuccessful()) {
                             startActivity(new Intent(MainActivity.this, ContentMainActivity.class));
                         } else {
                             Toast.makeText(MainActivity.this, "Failed to login, try again. At least one of your email address or password is invalid", Toast.LENGTH_LONG).show();
                             progressBar.setVisibility(View.GONE);
                         }
                    }
                                       }
                );
    }
}