package com.zblouse.fantasyfitness.workout;

import com.zblouse.fantasyfitness.core.Event;
import com.zblouse.fantasyfitness.core.EventType;

public class TimeUpdateEvent extends Event {

    private final long time;

    public TimeUpdateEvent(long time) {
        super(EventType.TIME_UPDATE_EVENT);
        this.time = time;
    }

    public long getTime(){
        return this.time;
    }
}
