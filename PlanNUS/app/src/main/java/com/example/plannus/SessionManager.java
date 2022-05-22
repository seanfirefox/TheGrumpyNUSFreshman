package com.example.plannus;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import javax.inject.Singleton;

@Singleton
public class SessionManager extends AppCompatActivity {

    private final FirebaseAuth fAuth;
    private final FirebaseFirestore fireStore;
    private final DatabaseReference dRef;
    private final FirebaseDatabase database;

    public SessionManager(FirebaseAuth fAuth, FirebaseFirestore fireStore, FirebaseDatabase database) {
        this.fAuth = fAuth;
        this.fireStore = fireStore;
        this.database = database;
        this.dRef = database.getReference("Users");
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
                                                Log.d("Successful login", "Successful Login");
                                            } else {
                                                Log.d("Unsuccessful login", "Unsuccessful login");
                                            }
                                        }
                                    });
                        }
                    }
                });
    }
}
