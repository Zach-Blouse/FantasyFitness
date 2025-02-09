package com.zblouse.fantasyfitness.workout;

import android.os.Handler;
import android.os.Looper;

import com.zblouse.fantasyfitness.activity.MainActivity;

public class WorkoutService {

    private final Handler handler;
    private boolean paused;
    private final TimeTracker timeTracker;
    private final MainActivity mainActivity;

    public WorkoutService(MainActivity mainActivity){
        this.mainActivity = mainActivity;
        this.handler = new Handler(Looper.myLooper());
        this.timeTracker  = new TimeTracker();
    }

    public WorkoutService(MainActivity mainActivity, Handler handler, TimeTracker timeTracker){
        this.mainActivity = mainActivity;
        this.handler = handler;
        this.timeTracker = timeTracker;
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
