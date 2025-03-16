package com.zblouse.fantasyfitness.workout;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.zblouse.fantasyfitness.activity.DeviceServiceType;
import com.zblouse.fantasyfitness.activity.LocationDeviceService;
import com.zblouse.fantasyfitness.activity.LocationEvent;
import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.activity.PermissionDeviceService;
import com.zblouse.fantasyfitness.core.Event;
import com.zblouse.fantasyfitness.core.EventListener;
import com.zblouse.fantasyfitness.core.EventType;

import java.util.HashMap;

public class WorkoutService implements EventListener {

    private final Handler handler;
    private boolean paused;
    private boolean workoutInProgress;
    private final TimeTracker timeTracker;
    private final DistanceTracker distanceTracker;
    private MainActivity mainActivity;
    private static final String SAVE_STATE_WORKOUT_DISTANCE = "workoutDistance";
    private static final String SAVE_STATE_WORKOUT_TIME = "workoutTime";
    private static final String SAVE_STATE_WORKOUT_IN_PROGRESS = "workoutInProgress";
    private static final String SAVE_STATE_LAST_LOCATION = "lastLocation";

    public WorkoutService() {
        this.handler = new Handler(Looper.myLooper());
        this.timeTracker = new TimeTracker();
        this.distanceTracker = new DistanceTracker();
    }

    public WorkoutService(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.handler = new Handler(Looper.myLooper());
        this.timeTracker = new TimeTracker();
        this.distanceTracker = new DistanceTracker();
    }

    public WorkoutService(MainActivity mainActivity, Handler handler, TimeTracker timeTracker, DistanceTracker distanceTracker) {
        this.mainActivity = mainActivity;
        this.handler = handler;
        this.timeTracker = timeTracker;
        this.distanceTracker = distanceTracker;
        paused = true;
        workoutInProgress = false;
    }

    public void setMainActivity(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    private final Runnable workoutRunnable = new Runnable() {
        @Override
        public void run() {
            //If the workout is paused, don't update the time or distance
            if (!paused) {
                long time = timeTracker.update();
                double distance = distanceTracker.getTotalDistanceMeters();
                mainActivity.publishEvent(new WorkoutUpdateEvent(time, distance, new HashMap<>()));
            }
            handler.postDelayed(this, 1000);
        }
    };

    public void pause() {
        paused = true;
        long updatedTime = timeTracker.pause();
        double updatedDistance = distanceTracker.pause();
        mainActivity.publishEvent(new WorkoutUpdateEvent(updatedTime, updatedDistance, new HashMap<>()));
    }

    public void unpause() {
        paused = false;
        timeTracker.unpause();
        distanceTracker.unpause();
    }

    public boolean startWorkout() {
        //ensure we have location permissions before starting

        if (((PermissionDeviceService) mainActivity.getDeviceService(DeviceServiceType.PERMISSION)).hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            paused = false;
            timeTracker.start();
            distanceTracker.start();
            handler.post(workoutRunnable);
            workoutInProgress = true;
            ((LocationDeviceService) mainActivity.getDeviceService(DeviceServiceType.LOCATION)).subscribe(this);
            return true;
        } else {
            ((PermissionDeviceService) mainActivity.getDeviceService(DeviceServiceType.PERMISSION)).requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, 5);
            return false;
        }

    }

    public void stopWorkout(){
        double finalDistance = distanceTracker.stop();
        long finalTime = timeTracker.stop();
        paused = true;
        handler.removeCallbacks(workoutRunnable);
        workoutInProgress = false;
        ((LocationDeviceService) mainActivity.getDeviceService(DeviceServiceType.LOCATION)).unsubscribe(this);
        mainActivity.publishEvent(new WorkoutCompleteEvent(finalTime, finalDistance, new HashMap<>()));
    }

    public Bundle onSaveInstanceState(Bundle outBundle){

        outBundle.putBoolean(SAVE_STATE_WORKOUT_IN_PROGRESS, workoutInProgress);
        if(workoutInProgress){
            outBundle.putDouble(SAVE_STATE_WORKOUT_DISTANCE,distanceTracker.getTotalDistanceMeters());
            outBundle.putLong(SAVE_STATE_WORKOUT_TIME, timeTracker.getTotalTimeMillis());
            outBundle.putParcelable(SAVE_STATE_LAST_LOCATION,distanceTracker.getLastLocation());
        }

        return outBundle;
    }

    public boolean onRestoreInstanceState(Bundle savedState){
        workoutInProgress = savedState.getBoolean(SAVE_STATE_WORKOUT_IN_PROGRESS);
        if(workoutInProgress){
            startWorkout();
            distanceTracker.setTotalDistanceMeters(savedState.getDouble(SAVE_STATE_WORKOUT_DISTANCE,0));
            distanceTracker.setLastLocation(savedState.getParcelable(SAVE_STATE_LAST_LOCATION, Location.class));
            timeTracker.setTotalTimeMillis(savedState.getLong(SAVE_STATE_WORKOUT_TIME,0));
        }
        return workoutInProgress;
    }

    @Override
    public void publishEvent(Event event) {
        if(event.getEventType().equals(EventType.DEVICE_LOCATION_EVENT)){
            if (!paused) {
                mainActivity.publishEvent(new WorkoutUpdateEvent(timeTracker.getTotalTimeMillis(), distanceTracker.update(((LocationEvent)event).getLocation()), new HashMap<>()));
            }
        }
    }
}
