package com.zblouse.fantasyfitness.world;

import java.util.List;

public class Path {

    private final double distanceKm;
    private final List<GameLocation> locationPath;

    public Path(double distanceKm, List<GameLocation> locationPath){
        this.distanceKm = distanceKm;
        this.locationPath = locationPath;
    }

    public double getDistanceKm(){
        return this.distanceKm;
    }

    public List<GameLocation> getLocationPath(){
        return this.locationPath;
    }

}
