package com.zblouse.fantasyfitness.activity;

import com.zblouse.fantasyfitness.core.Event;
import com.zblouse.fantasyfitness.core.EventType;
import com.zblouse.fantasyfitness.workout.TimeUpdateEvent;

import android.location.Location;

public class LocationEvent extends Event {

    private final Location location;

    public LocationEvent(Location location){
        super(EventType.DEVICE_LOCATION_EVENT);
        this.location = location;
    }

    public Location getLocation(){
        return this.location;
    }
}
