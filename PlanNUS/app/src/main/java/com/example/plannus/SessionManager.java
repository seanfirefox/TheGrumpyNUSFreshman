package com.example.plannus;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class SessionManager {

    private static SessionManager sm = null;

    private final FirebaseAuth fAuth;
    private final FirebaseFirestore fireStore;
    private final DatabaseReference dRef;
    private final FirebaseDatabase database;

    private SessionManager(FirebaseAuth fAuth, FirebaseFirestore fireStore, FirebaseDatabase database) {
        this.fAuth = fAuth;
        this.fireStore = fireStore;
        this.database = database;
        this.dRef = database.getReference("Users");
    }

    public static SessionManager get() {
        if (sm == null) {
            sm = new SessionManager(FirebaseAuth.getInstance(),
                    FirebaseFirestore.getInstance(),
                    FirebaseDatabase.getInstance("https://plannus-cad5f-default-rtdb.asia-southeast1.firebasedatabase.app/"));
        }
        return sm;
    }

    public FirebaseAuth getAuth() {
        return fAuth;
    }

    public FirebaseFirestore getFireStore() {
        return fireStore;
    }

    public FirebaseDatabase getDatabase() {
        return database;
    }

    public String getUserID() {
        return fAuth.getCurrentUser().getUid();
    }

    public DatabaseReference getdRef() {
        return dRef;
    }

    public Query getTimetableQuery(String userID, String collectionPath) {
        return fireStore.collection("Users")
                .document(userID)
                .collection("NUS_Schedule")
                .document("NUS_Schedule")
                .collection(collectionPath)
                .orderBy("start", Query.Direction.ASCENDING);
    }

    public DocumentReference getDocRef(String userID, String collectionPath, String documentPath) {
        return fireStore.collection("Users")
                .document(userID)
                .collection(collectionPath)
                .document(documentPath);
    }

    public CollectionReference getColRef(String userID, String collectionPath) {
        return fireStore.collection("Users")
                .document(userID)
                .collection(collectionPath);
    }

}
