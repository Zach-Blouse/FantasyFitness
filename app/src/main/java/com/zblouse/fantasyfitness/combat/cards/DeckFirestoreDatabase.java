package com.zblouse.fantasyfitness.combat.cards;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.zblouse.fantasyfitness.core.FirestoreDatabase;
import com.zblouse.fantasyfitness.core.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeckFirestoreDatabase extends FirestoreDatabase {

    private static final String COLLECTION = "decks";
    private static final String DECK_LIST_KEY = "deckList";
    private static final String TOP_COLLECTION = "userCards";

    public DeckFirestoreDatabase(){
        super();
    }

    public DeckFirestoreDatabase(FirebaseFirestore firestore){
        super(firestore);
    }

    public void writeDeck(Deck deck, DeckRepository deckRepository, Map<String, Object> metadata){
        Map<String, Object> newDeck = new HashMap<>();
        newDeck.put(DECK_LIST_KEY,deck.getCardUuids());
        firestore.collection(TOP_COLLECTION).document(deck.getUserId()).collection(COLLECTION).document(deck.getDeckName()).set(newDeck).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    deckRepository.writeCallback(deck, metadata);
                } else {
                    deckRepository.writeCallback(null, metadata);
                }
            }
        });
    }

    public void readDeck(String userId, String deckName, Repository<Deck> repository, Map<String, Object> metadata){
        firestore.collection(TOP_COLLECTION).document(userId).collection(COLLECTION).document(deckName).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot result = task.getResult();
                    if(result.exists()) {
                        Deck deck = new Deck(userId, deckName, (List)result.get(DECK_LIST_KEY));
                        repository.readCallback(deck, metadata);
                    } else {
                        repository.readCallback(null, metadata);
                    }
                } else {
                    //TODO handle firebase error states
                    repository.readCallback(null, metadata);
                }
            }
        });
    }
}
