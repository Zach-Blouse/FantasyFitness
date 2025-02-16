package com.zblouse.fantasyfitness.world;

import com.zblouse.fantasyfitness.core.Event;
import com.zblouse.fantasyfitness.core.EventType;

import java.util.Map;

public class GameLocationFetchEvent extends Event {

    private GameLocation location;

    public GameLocationFetchEvent(GameLocation location, Map<String, Object> metadata) {
        super(EventType.LOCATION_FETCH_EVENT, metadata);
        this.location = location;
    }

    public GameLocation getLocation(){
        return this.location;
    }
}
