package com.zblouse.fantasyfitness.user;

public class UserGameState {

    private final String userId;
    private final String currentGameLocationName;
    private final double savedWorkoutDistanceMeters;

    public UserGameState(String userId, String currentGameLocationName, double savedWorkoutDistanceMeters){
        this.userId = userId;
        this.currentGameLocationName = currentGameLocationName;
        this.savedWorkoutDistanceMeters = savedWorkoutDistanceMeters;
    }

    public String getCurrentGameLocationName(){
        return this.currentGameLocationName;
    }

    public String getUserId(){
        return this.userId;
    }

    public double getSavedWorkoutDistanceMeters(){
        return this.savedWorkoutDistanceMeters;
    }

}
