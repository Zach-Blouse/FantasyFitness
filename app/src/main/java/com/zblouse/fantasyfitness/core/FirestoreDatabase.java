package com.zblouse.fantasyfitness.core;

import com.google.firebase.firestore.FirebaseFirestore;

public abstract class FirestoreDatabase {

    public static final String DATABASE_UPDATE_FIELD = "updateField";
    public static final String DATABASE_UPDATE_VALUE = "updateValue";

    protected FirebaseFirestore firestore;

    protected FirestoreDatabase(){
        firestore = FirebaseFirestore.getInstance();
    }

    protected FirestoreDatabase(FirebaseFirestore firestore){
        this.firestore = firestore;
    }
}
