package com.zblouse.fantasyfitness.core;

import com.google.firebase.firestore.FirebaseFirestore;

public abstract class FirestoreDatabase {

    protected FirebaseFirestore firestore;

    protected FirestoreDatabase(){
        firestore = FirebaseFirestore.getInstance();
    }

    protected FirestoreDatabase(FirebaseFirestore firestore){
        this.firestore = firestore;
    }
}
