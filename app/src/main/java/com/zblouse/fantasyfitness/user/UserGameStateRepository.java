package com.zblouse.fantasyfitness.user;

import com.zblouse.fantasyfitness.core.DomainService;
import com.zblouse.fantasyfitness.core.Repository;

import java.util.Map;

public class UserGameStateRepository implements Repository<UserGameState> {

    private final UserGameStateFirestoreDatabase database;
    private final DomainService<UserGameState> userGameStateDomainService;
    private static final String MULTI_STAGE_UPDATE = "multiStageUpdate";
    private static final String USER_DISTANCE = "userDistance";
    private static final String USER_CURRENCY = "userCurrency";
    private static final String MODIFY_VALUE = "modifyValue";

    public UserGameStateRepository(DomainService<UserGameState> userGameStateDomainService){
        this.userGameStateDomainService = userGameStateDomainService;
        database = new UserGameStateFirestoreDatabase();
    }

    public UserGameStateRepository(UserGameStateFirestoreDatabase database, DomainService<UserGameState> userGameStateDomainService){
        this.database = database;
        this.userGameStateDomainService = userGameStateDomainService;
    }

    public void addUserDistance(String userId, double distanceKm, Map<String, Object> metadata){
        metadata.put(MULTI_STAGE_UPDATE, USER_DISTANCE);
        metadata.put(MODIFY_VALUE, distanceKm);
        database.read(userId,this,metadata);
    }

    public void addUserCurrency(String userId, int currency, Map<String, Object> metadata){
        metadata.put(MULTI_STAGE_UPDATE, USER_CURRENCY);
        metadata.put(MODIFY_VALUE, currency);
        database.read(userId,this,metadata);
    }

    private void modifyUserDistance(UserGameState userGameState, Map<String, Object> metadata){
        double modifyValue = (Double)metadata.get(MODIFY_VALUE);
        database.updateField(userGameState.getUserId(), UserGameStateFirestoreDatabase.USER_SAVED_DISTANCE,userGameState.getSavedWorkoutDistanceMeters()+modifyValue,this,metadata);
    }

    private void modifyUserCurrency(UserGameState userGameState, Map<String, Object> metadata){
        int modifyValue = (Integer)metadata.get(MODIFY_VALUE);
        database.updateField(userGameState.getUserId(), UserGameStateFirestoreDatabase.USER_GAME_CURRENCY,userGameState.getUserGameCurrency()+modifyValue,this,metadata);
    }

    public void getUserGameState(String userId, Map<String, Object> metadata){
        database.read(userId,this,metadata);
    }

    public void writeUserGameState(String userId, String userLocation, double savedDistance, int userGameCurrency, Map<String, Object> metadata){
        database.write(new UserGameState(userId,userLocation,savedDistance, userGameCurrency), this, metadata);
    }

    public void updateUserGameLocation(String userId, String userLocation, Map<String, Object> metadata){
        database.updateField(userId, UserGameStateFirestoreDatabase.USER_LOCATION_FIELD, userLocation, this, metadata);
    }

    @Override
    public void readCallback(UserGameState userGameState, Map<String, Object> metadata) {
        if(metadata.containsKey(MULTI_STAGE_UPDATE)){
            if(metadata.get(MULTI_STAGE_UPDATE).equals(USER_DISTANCE)){
                modifyUserDistance(userGameState,metadata);
            } else if(metadata.get(MULTI_STAGE_UPDATE).equals(USER_CURRENCY)){
                modifyUserCurrency(userGameState, metadata);
            }
        } else {
            userGameStateDomainService.repositoryResponse(userGameState, metadata);
        }
    }

    @Override
    public void writeCallback(UserGameState userGameState, Map<String, Object> metadata) {
        userGameStateDomainService.repositoryResponse(userGameState, metadata);
    }

    @Override
    public void updateCallback(boolean success, Map<String, Object> metadata) {
        if(success){
            userGameStateDomainService.repositoryResponse(null,metadata);
        }
        //TODO HANDLE FIREBASE ERROR
    }
}
