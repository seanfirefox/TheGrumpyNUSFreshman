package com.example.plannus.Activities.LoginRegister;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.plannus.R;
import com.example.plannus.SessionManager;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button register;
    private EditText emailAddress;
    private EditText passWord;
    private ProgressBar progressBar;
    private Button loginButton;
    private SessionManager sessionManager;
    private TextView forgetPassword;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sessionManager = SessionManager.get();

        initVars();
    }

    public void cleanActivity() {
        emailAddress.setText("");
        passWord.setText("");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.register) {
            startActivity(new Intent(this, RegisterUser.class));
        } else if (v.getId() == R.id.loginButton) {
            tryLogin();
        } else if (v.getId() == R.id.forgotpassword) {
            alertDialoger();
        }
    }

    public void verifyEmail(String email) {
        dialogBuilder = new AlertDialog.Builder(this);
        final View verifyView = getLayoutInflater().inflate(R.layout.popup_verifyemail,  null);
        Button bb = verifyView.findViewById(R.id.verifyEmailButton);
        dialogBuilder.setView(verifyView);
        dialog = dialogBuilder.create();
        dialog.show();
        bb.setOnClickListener(view -> {
            sessionManager.getAuth().getCurrentUser().sendEmailVerification()
                    .addOnCompleteListener(task -> Toast.makeText(MainActivity.this, "Verification Email Sent", Toast.LENGTH_SHORT).show());
            dialog.dismiss();
        });
    }

    public void alertDialoger() {
        dialogBuilder = new AlertDialog.Builder(this);
        final View popUpView = getLayoutInflater().inflate(R.layout.popup_forgotpassword, null);
        Button b  = popUpView.findViewById(R.id.resetPasswordButton);
        EditText a = popUpView.findViewById(R.id.emailA);
        dialogBuilder.setView(popUpView);
        dialog = dialogBuilder.create();
        dialog.show();

        b.setOnClickListener(view -> {
            String emailA = a.getText().toString().trim();
            if (emailA.isEmpty()) {
                Toast.makeText(MainActivity.this,"Email Field is Empty!",Toast.LENGTH_SHORT).show();
                return;
            }
            sessionManager.getAuth().sendPasswordResetEmail(emailA)
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(MainActivity.this,"Email Sent!",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }).addOnFailureListener(e -> {
                        Toast.makeText(MainActivity.this,"No Account Registered with the Email",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    });
        });

    }

    public void tryLogin() {
        String email = this.emailAddress.getText().toString().trim();
        String password = this.passWord.getText().toString().trim();

        if (!credentialCheck(email, password)) {
            Toast.makeText(MainActivity.this, "Invalid Credentials", Toast.LENGTH_LONG).show();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        sessionManager.getAuth()
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    cleanActivity();
                    if (task.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);
                        System.out.println("verified User" + sessionManager.getAuth().getCurrentUser().isEmailVerified());
                        if (email.equals("admin@gmail.com") || sessionManager.getAuth().getCurrentUser().isEmailVerified()) {
                            startActivity(new Intent(MainActivity.this, ContentMainActivity.class));
                        } else {
                            verifyEmail(email);
                        }
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, "Failed to login, try again. At least one of your email address or password is invalid", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void initVars() {
        register = findViewById(R.id.register);
        register.setOnClickListener(this);

        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);

        forgetPassword = findViewById(R.id.forgotpassword);
        forgetPassword.setOnClickListener(this);

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

    @Override
    protected void onStart() {
        super.onStart();
        if (sessionManager.getAuth().getCurrentUser() != null && sessionManager.getAuth().getCurrentUser().isEmailVerified()) {
            startActivity(new Intent(MainActivity.this, ContentMainActivity.class));
        }
    }

}