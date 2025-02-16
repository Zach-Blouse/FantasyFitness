package com.zblouse.fantasyfitness.workout;

import com.zblouse.fantasyfitness.core.Event;
import com.zblouse.fantasyfitness.core.EventType;

import java.util.Map;

public class WorkoutTimeUpdateEvent extends Event {

    private final long time;

    public WorkoutTimeUpdateEvent(long time, Map<String, Object> metadata) {
        super(EventType.TIME_UPDATE_EVENT, metadata);
        this.time = time;
    }

    public long getTime(){
        return this.time;
    }
}
