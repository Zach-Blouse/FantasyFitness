package com.zblouse.fantasyfitness.workout;

import android.os.Handler;
import android.os.Looper;

import com.zblouse.fantasyfitness.MainActivity;

public class WorkoutService {

    Handler handler;
    private boolean paused;
    private TimeTracker timeTracker;
    private MainActivity mainActivity;

    public WorkoutService(MainActivity mainActivity){
        this.mainActivity = mainActivity;
        handler = new Handler(Looper.myLooper());
        timeTracker  = new TimeTracker();
    }

    private final Runnable workoutRunnable = new Runnable() {
        @Override
        public void run() {
            //If the workout is paused, don't update the time or distance
            if (!paused) {
                long time = timeTracker.update();
                mainActivity.publishEvent(new TimeUpdateEvent(time));
            }
            handler.postDelayed(this, 500);
        }
    };

    public void pause(){
        paused = true;
        long updatedTime = timeTracker.pause();
        mainActivity.publishEvent(new TimeUpdateEvent(updatedTime));
    }

    public void unpause(){
        paused = false;
        timeTracker.unpause();
    }

    public void startWorkout(){

        paused = false;
        timeTracker.start();
        //Runable is repeated every second
        handler.post(workoutRunnable);
    }

    public void stopWorkout(){
        long lastTime = timeTracker.stop();
        mainActivity.publishEvent(new TimeUpdateEvent(lastTime));
        paused = true;
        handler.removeCallbacks(workoutRunnable);
    }

}
