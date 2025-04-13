package com.zblouse.fantasyfitness.user;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.zblouse.fantasyfitness.core.FirestoreDatabase;
import com.zblouse.fantasyfitness.core.Repository;

import java.util.HashMap;
import java.util.Map;

public class UserGameStateFirestoreDatabase extends FirestoreDatabase {

    private static final String COLLECTION = "gameState";
    public static final String USER_LOCATION_FIELD = "gameLocation";
    public static final String USER_SAVED_DISTANCE = "savedDistance";
    public static final String USER_GAME_CURRENCY = "userGameCurrency";

    public UserGameStateFirestoreDatabase(){
        super();
    }

    public UserGameStateFirestoreDatabase(FirebaseFirestore firestore){
        super(firestore);
    }

    public void write(UserGameState userGameState, Repository<UserGameState> repository, Map<String, Object> metadata){
        Map<String, Object> newUserGameState = new HashMap<>();
        newUserGameState.put(USER_LOCATION_FIELD,userGameState.getCurrentGameLocationName());
        newUserGameState.put(USER_SAVED_DISTANCE,userGameState.getSavedWorkoutDistanceMeters());
        newUserGameState.put(USER_GAME_CURRENCY,userGameState.getUserGameCurrency());
        firestore.collection(COLLECTION).document(userGameState.getUserId()).set(newUserGameState).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    repository.writeCallback(userGameState, metadata);
                } else {
                    repository.writeCallback(null, metadata);
                }
            }
        });
    }

    public void updateField(String userId, String fieldId, Object newValue, Repository<UserGameState> repository, Map<String, Object> metadata){
        metadata.put(DATABASE_UPDATE_FIELD, fieldId);
        metadata.put(DATABASE_UPDATE_VALUE, newValue);
        firestore.collection(COLLECTION).document(userId).update(fieldId, newValue).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    repository.updateCallback(true, metadata);
                } else {
                    repository.updateCallback(false, metadata);
                }
            }
        });
    }

    public void read(String userId, Repository<UserGameState> repository, Map<String, Object> metadata){
        firestore.collection(COLLECTION).document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot result = task.getResult();
                    if(result.exists()) {
                        UserGameState returnedUserGameState = new UserGameState(userId, result.get(USER_LOCATION_FIELD, String.class), result.get(USER_SAVED_DISTANCE, Double.class), result.get(USER_GAME_CURRENCY, Integer.class));
                        repository.readCallback(returnedUserGameState, metadata);
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