package com.zblouse.fantasyfitness.workout;

public class TimeTracker {

    private long lastTimestamp;
    private long totalTimeMillis;
    private boolean paused;

    public long update(){
        if(!paused) {
            long currentTime = System.currentTimeMillis();
            totalTimeMillis += (currentTime - lastTimestamp);
            lastTimestamp = currentTime;
        }
        return totalTimeMillis;
    }

    public void start(){
        lastTimestamp = System.currentTimeMillis();
        totalTimeMillis = 0;
        paused = false;
    }

    public long stop(){
        return pause();
    }

    public long pause(){
        long totalTime = update();
        paused = true;
        return totalTime;
    }

    public void unpause(){
        paused = false;
        lastTimestamp = System.currentTimeMillis();
    }

    public long getTotalTimeMillis(){
        return totalTimeMillis;
    }
}
