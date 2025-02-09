package com.zblouse.fantasyfitness.workout;

import com.zblouse.fantasyfitness.core.Event;
import com.zblouse.fantasyfitness.core.EventType;

public class WorkoutDistanceUpdateEvent extends Event {

    private final double distanceMeters;

    public WorkoutDistanceUpdateEvent(double distanceMeters){
        super(EventType.WORKOUT_DISTANCE_UPDATE_EVENT);
        this.distanceMeters = distanceMeters;
    }

    public double getDistanceMeters(){
        return this.distanceMeters;
    }
}
