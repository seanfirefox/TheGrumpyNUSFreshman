package com.example.plannus;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class SessionManager {

    private static SessionManager sm = null;

    private final FirebaseAuth fAuth;
    private final FirebaseFirestore fireStore;
    private final DatabaseReference dRef;
    private final FirebaseDatabase database;
    private String userID;
    private User user;

    private static boolean loginStatus = false;
    private static boolean registerStatus = false;

    private SessionManager(FirebaseAuth fAuth, FirebaseFirestore fireStore, FirebaseDatabase database) {
        this.fAuth = fAuth;
        this.fireStore = fireStore;
        this.database = database;
        this.dRef = database.getReference("Users");
    }

    public static SessionManager get() {
        if (sm == null) {
            loginStatus = false;
            registerStatus = false;
            sm = new SessionManager(FirebaseAuth.getInstance(),
                    FirebaseFirestore.getInstance(),
                    FirebaseDatabase.getInstance("https://plannus-cad5f-default-rtdb.asia-southeast1.firebasedatabase.app/"));
        }
        return sm;
    }


    public void register(User user) {
        fAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            dRef.child(fAuth.getCurrentUser().getUid())
                                    .setValue(user)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                registerStatus = true;
                                                Log.d("Successful login", "Successful Login");
                                            } else {
                                                registerStatus = false;
                                                Log.d("Unsuccessful login", "Unsuccessful login");
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    public void login(String email, String password) {;
        fAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loginStatus = true;
                        } else {
                            loginStatus = false;
                        }
                    }
                });
    }

    private String getUserID() {
        if (userID == null) {
            userID = fAuth.getCurrentUser().getUid();
        }
        return userID;
    }

    public User getUser() {
            dRef.child(getUserID())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            user = snapshot.getValue(User.class);
                            Log.d("Expose Name", user.fullName);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.d("Failed", "unable to access name");
                        }
                    });

        return user;
    }

    public boolean getLoginStatus() {
        return loginStatus;
    }

    public boolean getRegisterStatus() {
        return registerStatus;
    }
}
