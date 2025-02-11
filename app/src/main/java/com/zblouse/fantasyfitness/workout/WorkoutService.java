package com.zblouse.fantasyfitness.workout;

import android.Manifest;
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

public class WorkoutService implements EventListener {

    private final Handler handler;
    private boolean paused;
    private final TimeTracker timeTracker;
    private final DistanceTracker distanceTracker;
    private MainActivity mainActivity;

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
                mainActivity.publishEvent(new WorkoutTimeUpdateEvent(time));
            }
            handler.postDelayed(this, 500);
        }
    };

    public void pause() {
        paused = true;
        long updatedTime = timeTracker.pause();
        distanceTracker.pause();
        mainActivity.publishEvent(new WorkoutTimeUpdateEvent(updatedTime));
    }

    public void unpause() {
        paused = false;
        timeTracker.unpause();
        distanceTracker.unpause();
    }

    public boolean startWorkout() {
        //ensure we have location permissions before starting
        if(((PermissionDeviceService)mainActivity.getDeviceService(DeviceServiceType.PERMISSION)).hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)){
            paused = false;
            timeTracker.start();
            distanceTracker.start();
            handler.post(workoutRunnable);
            ((LocationDeviceService)mainActivity.getDeviceService(DeviceServiceType.LOCATION)).subscribe(this);
            return true;
        } else{
            ((PermissionDeviceService) mainActivity.getDeviceService(DeviceServiceType.PERMISSION)).requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, 5);
            return false;
        }
    }

    public void stopWorkout(){
        distanceTracker.stop();
        mainActivity.publishEvent(new WorkoutTimeUpdateEvent(timeTracker.stop()));
        paused = true;
        handler.removeCallbacks(workoutRunnable);
    }

    @Override
    public void publishEvent(Event event) {
        if(event.getEventType().equals(EventType.DEVICE_LOCATION_EVENT)){
            if (!paused) {
                mainActivity.publishEvent(new WorkoutDistanceUpdateEvent(distanceTracker.update(((LocationEvent)event).getLocation())));
            }
        }
    }
}
