package com.zblouse.fantasyfitness.workout;

import com.zblouse.fantasyfitness.core.Event;
import com.zblouse.fantasyfitness.core.EventType;

public class WorkoutTimeUpdateEvent extends Event {

    private final long time;

    public WorkoutTimeUpdateEvent(long time) {
        super(EventType.TIME_UPDATE_EVENT);
        this.time = time;
    }

    public long getTime(){
        return this.time;
    }
}
