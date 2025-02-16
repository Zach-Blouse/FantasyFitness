package com.zblouse.fantasyfitness.core;

import java.util.Map;

public abstract class Event {

    protected EventType eventType;
    protected Map<String, Object> metadata;

    public Event(EventType eventType, Map<String,Object> metadata){
        this.eventType = eventType;
        this.metadata = metadata;
    }

    public EventType getEventType() {
        return eventType;
    }

    public Map<String, Object> getMetadata(){
        return this.metadata;
    }
}
