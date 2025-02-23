package com.zblouse.fantasyfitness.workout;

import com.zblouse.fantasyfitness.core.Event;
import com.zblouse.fantasyfitness.core.EventType;

import java.util.Map;

public class WorkoutCompleteEvent extends Event {

    private final long workoutTime;
    private final double workoutDistanceMeters;

    public WorkoutCompleteEvent(long workoutTime, double workoutDistanceMeters, Map<String,Object> metadata){
        super(EventType.WORKOUT_COMPLETE_EVENT,metadata);
        this.workoutTime = workoutTime;
        this.workoutDistanceMeters = workoutDistanceMeters;
    }

    public long getWorkoutTime(){
        return this.workoutTime;
    }

    public double getWorkoutDistanceMeters(){
        return this.workoutDistanceMeters;
    }
}
