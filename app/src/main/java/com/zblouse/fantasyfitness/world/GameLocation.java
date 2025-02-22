package com.zblouse.fantasyfitness.world;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class GameLocation implements Serializable {

    private Integer id;
    private final String locationName;
    private final String locationDescription;
    private final Map<GameLocation, Double> connectedLocations;

    public GameLocation(String locationName, String locationDescription){
        this.locationName = locationName;
        this.locationDescription = locationDescription;
        this.connectedLocations = new HashMap<>();
    }

    public GameLocation(Integer id, String locationName, String locationDescription){
        this.id = id;
        this.locationName = locationName;
        this.locationDescription = locationDescription;
        this.connectedLocations = new HashMap<>();
    }

    public GameLocation(Integer id, String locationName, String locationDescription, Map<GameLocation,Double> connectedLocations){
        this.id = id;
        this.locationName = locationName;
        this.locationDescription = locationDescription;
        this.connectedLocations = connectedLocations;
    }

    public Integer getId(){
        return this.id;
    }

    public String getLocationName(){
        return this.locationName;
    }

    public String getLocationDescription(){
        return this.locationDescription;
    }

    public Map<GameLocation, Double> getConnectedLocations(){
        return this.connectedLocations;
    }

    @Override
    public boolean equals(Object object){
        if(object == null){
            return false;
        } else if (object instanceof GameLocation){
            GameLocation otherLocation = (GameLocation)object;
            return otherLocation.getLocationName().equals(locationName);
        } else {
            return false;
        }
    }
}
