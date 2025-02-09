package com.zblouse.fantasyfitness.activity;

import com.zblouse.fantasyfitness.core.Event;
import com.zblouse.fantasyfitness.core.EventListener;

import java.util.HashSet;
import java.util.Set;

public abstract class DeviceService {

    protected final DeviceServiceType deviceServiceType;
    protected Set<EventListener> eventListeners;

    public DeviceService(DeviceServiceType deviceServiceType){
        this.deviceServiceType = deviceServiceType;
        eventListeners = new HashSet<>();
    }

    public DeviceServiceType getDeviceServiceType(){
        return this.deviceServiceType;
    }

    public void subscribe(EventListener eventListener){
        eventListeners.add(eventListener);
    }

    public void unsubscribe(EventListener eventListener){
        if(eventListeners.contains(eventListener)){
            eventListeners.remove(eventListener);
        }
    }

    protected void sendEvent(Event event){
        eventListeners.stream().forEach(listener -> listener.publishEvent(event));
    }
}
