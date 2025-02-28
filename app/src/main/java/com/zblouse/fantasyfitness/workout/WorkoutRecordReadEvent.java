package com.zblouse.fantasyfitness.workout;

import com.zblouse.fantasyfitness.core.Event;
import com.zblouse.fantasyfitness.core.EventType;

import java.util.Map;

public class WorkoutRecordReadEvent extends Event {

    private final WorkoutRecord workoutRecord;

    public WorkoutRecordReadEvent(WorkoutRecord workoutRecord, Map<String, Object> metadata){
        super(EventType.WORKOUT_RECORD_READ_EVENT,metadata);
        this.workoutRecord = workoutRecord;
    }

    public WorkoutRecord getWorkoutRecord(){
        return this.workoutRecord;
    }
}
