package com.zblouse.fantasyfitness.user;

public class UserGameState {

    private final String userId;
    private final String currentGameLocationName;
    private final double savedWorkoutDistanceMeters;
    private final int userGameCurrency;

    public UserGameState(String userId, String currentGameLocationName, double savedWorkoutDistanceMeters, int userGameCurrency){
        this.userId = userId;
        this.currentGameLocationName = currentGameLocationName;
        this.savedWorkoutDistanceMeters = savedWorkoutDistanceMeters;
        this.userGameCurrency = userGameCurrency;
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

    public int getUserGameCurrency(){
        return this.userGameCurrency;
    }

}
