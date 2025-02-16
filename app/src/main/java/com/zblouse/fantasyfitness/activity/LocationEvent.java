package com.zblouse.fantasyfitness.activity;

import com.zblouse.fantasyfitness.core.Event;
import com.zblouse.fantasyfitness.core.EventType;

import android.location.Location;

import java.util.Map;

public class LocationEvent extends Event {

    private final Location location;

    public LocationEvent(Location location, Map<String, Object> metadata){
        super(EventType.DEVICE_LOCATION_EVENT, metadata);
        this.location = location;
    }

    public Location getLocation(){
        return this.location;
    }
}
