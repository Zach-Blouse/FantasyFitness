package com.zblouse.fantasyfitness.core;

public abstract class Event {

    protected EventType eventType;

    public Event(EventType eventType){
        this.eventType = eventType;
    }

    public EventType getEventType() {
        return eventType;
    }
}
