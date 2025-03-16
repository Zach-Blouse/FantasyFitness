package com.zblouse.fantasyfitness.workout;

import android.location.Location;
import android.util.Log;

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
                if(location.getElapsedRealtimeNanos() > lastLocation.getElapsedRealtimeNanos()){
                    Log.e("DISTANCE TRACKER", "LOCATION NEWER THAN LAST LOCATION");
                    float[] results = new float[3];
                    Location.distanceBetween(lastLocation.getLatitude(),lastLocation.getLongitude(),location.getLatitude(),location.getLongitude(),results);
                    totalDistanceMeters += results[0];
                    lastLocation = location;
                } else {
                    Log.e("DISTANCE TRACKER", "LOCATION OLDER THAN LAST LOCATION");
                }
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
        return pause();
    }

    public double pause(){
        paused = true;
        lastLocation = null;
        return totalDistanceMeters;
    }

    public void unpause(){
        paused = false;
    }

    public double getTotalDistanceMeters(){
        return totalDistanceMeters;
    }

    public Location getLastLocation(){
        return this.lastLocation;
    }

    public void setLastLocation(Location location){
        this.lastLocation = location;
    }

    public void setTotalDistanceMeters(double totalDistanceMeters){
        this.totalDistanceMeters = totalDistanceMeters;
    }
}
