package com.zblouse.fantasyfitness.user;

public class UserGameState {

    private final String userId;
    private final String currentGameLocationName;
    private final double savedWorkoutDistanceKm;

    public UserGameState(String userId, String currentGameLocationName, double savedWorkoutDistanceKm){
        this.userId = userId;
        this.currentGameLocationName = currentGameLocationName;
        this.savedWorkoutDistanceKm = savedWorkoutDistanceKm;
    }

    public String getCurrentGameLocationName(){
        return this.currentGameLocationName;
    }

    public String getUserId(){
        return this.userId;
    }

    public double getSavedWorkoutDistanceKm(){
        return this.savedWorkoutDistanceKm;
    }

}
