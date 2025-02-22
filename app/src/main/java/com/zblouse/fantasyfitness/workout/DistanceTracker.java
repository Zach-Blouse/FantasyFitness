package com.zblouse.fantasyfitness.workout;

import android.location.Location;

public class DistanceTracker {

    private Location lastLocation;
    private double totalDistanceMeters;
    private boolean paused;

    public double update(Location location){
        if(!paused) {
            //If last location equals null, we don't want to add any distance.
            if(lastLocation == null){
                lastLocation = location;
            } else if(lastLocation != location){
                float[] results = new float[3];
                Location.distanceBetween(lastLocation.getLatitude(),lastLocation.getLongitude(),location.getLatitude(),location.getLongitude(),results);
                totalDistanceMeters += results[0];
                lastLocation = location;
            }
        }
        return totalDistanceMeters;
    }

    public void start(){
        lastLocation = null;
        totalDistanceMeters = 0;
        paused = false;
    }

    public double stop(){
        pause();
        return totalDistanceMeters;
    }

    public void pause(){
        paused = true;
        lastLocation = null;
    }

    public void unpause(){
        paused = false;
    }

    public double getTotalDistanceMeters(){
        return totalDistanceMeters;
    }
}
