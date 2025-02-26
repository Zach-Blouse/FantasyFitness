package com.zblouse.fantasyfitness.workout;

import com.zblouse.fantasyfitness.core.Event;
import com.zblouse.fantasyfitness.core.EventType;

import java.util.Map;

public class WorkoutUpdateEvent extends Event {

    private final long time;
    private final double distanceMeters;

    public WorkoutUpdateEvent(long time, double distanceMeters, Map<String, Object> metadata) {
        super(EventType.WORKOUT_UPDATE_EVENT, metadata);
        this.time = time;
        this.distanceMeters = distanceMeters;
    }

    public long getTime(){
        return this.time;
    }

    public double getDistanceMeters(){
        return this.distanceMeters;
    }
}
