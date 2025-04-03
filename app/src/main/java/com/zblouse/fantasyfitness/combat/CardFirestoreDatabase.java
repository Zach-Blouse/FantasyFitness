package com.zblouse.fantasyfitness.combat;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.zblouse.fantasyfitness.core.FirestoreDatabase;
import com.zblouse.fantasyfitness.core.Repository;

import java.util.HashMap;
import java.util.Map;

public class CardFirestoreDatabase extends FirestoreDatabase {

    private static final String TOP_COLLECTION = "userCards";
    private static final String USER_COLLECTION = "cards";

    public CardFirestoreDatabase(){
        super();
    }

    public CardFirestoreDatabase(FirebaseFirestore firestore){
        super(firestore);
    }

    public void write(Card card, Repository<Card> cardRepository, Map<String, Object> metadata){

        firestore.collection(TOP_COLLECTION).document(card.getUserId()).collection(USER_COLLECTION).document(card.getCardUuid()).set(card).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    cardRepository.writeCallback(card, metadata);
                } else {
                    cardRepository.writeCallback(null, metadata);
                }
            }
        });
    }

    public void read(){

    }
}
