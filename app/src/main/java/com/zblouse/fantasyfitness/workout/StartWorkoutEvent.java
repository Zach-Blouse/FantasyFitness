package com.zblouse.fantasyfitness.workout;

import com.zblouse.fantasyfitness.core.Event;
import com.zblouse.fantasyfitness.core.EventType;

import java.util.Map;

public class StartWorkoutEvent extends Event {

    public StartWorkoutEvent(Map<String, Object> metadata){
        super(EventType.WORKOUT_START_EVENT, metadata);
    }
}
