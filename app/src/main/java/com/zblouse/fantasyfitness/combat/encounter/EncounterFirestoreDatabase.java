package com.zblouse.fantasyfitness.combat.encounter;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.zblouse.fantasyfitness.combat.cards.Card;
import com.zblouse.fantasyfitness.combat.cards.CardFirestoreDatabase;
import com.zblouse.fantasyfitness.combat.cards.CardType;
import com.zblouse.fantasyfitness.core.FirestoreDatabase;
import com.zblouse.fantasyfitness.core.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EncounterFirestoreDatabase extends FirestoreDatabase {

    private static final String COLLECTION = "encounters";

    public EncounterFirestoreDatabase(){
        super();
    }

    public EncounterFirestoreDatabase(FirebaseFirestore firestore){
        super(firestore);
    }

    //Should only be called during development, in ops, this would be set manually as there is one instance for all users
    public void write(Encounter encounter, Repository<Encounter> encounterRepository, Map<String, Object> metadata){
        firestore.collection(COLLECTION).document(encounter.getEncounterName()).set(encounter).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    encounterRepository.writeCallback(encounter, metadata);
                } else {
                    encounterRepository.writeCallback(null, metadata);
                }
            }
        });
    }

    public void read(String encounterName, Repository<Encounter> encounterRepository, Map<String, Object> metadata){
        firestore.collection(COLLECTION).document(encounterName).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){
                    DocumentSnapshot result = task.getResult();
                    if(result.exists()) {
                        Map<String, Object> documentData = result.getData();
                        String encounterName = (String)documentData.get(Encounter.ENCOUNTER_NAME_FIELD);
                        EncounterDifficultyLevel encounterDifficultyLevel = EncounterDifficultyLevel.valueOf((String)documentData.get(Encounter.ENCOUNTER_DIFFICULTY_LEVEL_FIELD));
                        List<Card> enemyDeck = new ArrayList<>();
                        for(Map<String,Object> cardData: (List<Map<String,Object>>)documentData.get(Encounter.ENEMY_CARDS_FIELD)){
                            CardType cardType = CardType.valueOf((String) cardData.get(Card.CARD_TYPE_FIELD));
                            switch(cardType){
                                case ITEM:{
                                    Card card = CardFirestoreDatabase.constructItemCardFromMap(cardData);
                                    enemyDeck.add(card);
                                    break;
                                }
                                case EFFECT:{
                                    Card card = CardFirestoreDatabase.constructEffectCardFromMap(cardData);
                                    enemyDeck.add(card);
                                    break;
                                }
                                case CHARACTER:{
                                    Card card = CardFirestoreDatabase.constructCharacterCardFromMap(cardData);
                                    enemyDeck.add(card);
                                    break;
                                }
                                case PERMANENT:{
                                    Card card = CardFirestoreDatabase.constructPermanentCardFromMap(cardData);
                                    enemyDeck.add(card);
                                    break;
                                }
                            }
                        }
                        Encounter encounter = new Encounter(encounterName, encounterDifficultyLevel, enemyDeck);
                        encounterRepository.readCallback(encounter, metadata);
                    } else {
                        encounterRepository.readCallback(null, metadata);
                    }
                } else {
                    //TODO handle firebase error states
                    encounterRepository.readCallback(null, metadata);
                }
            }
        });
    }
}
