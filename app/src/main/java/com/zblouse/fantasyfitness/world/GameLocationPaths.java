package com.zblouse.fantasyfitness.world;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GameLocationPaths {

    private final GameLocation startLocation;
    private final Map<String, Path> pathsFromHere;

    public GameLocationPaths(GameLocation startLocation){
        this.startLocation = startLocation;
        pathsFromHere = new HashMap<>();
    }

    public void addPath(String destinationName, Path pathToDestination){
        pathsFromHere.put(destinationName, pathToDestination);
    }

    public Path getPath(String gameLocation){
        return pathsFromHere.get(gameLocation);
    }

    public Set<String> getReachableLocationNames(){
        return pathsFromHere.keySet();
    }

    public GameLocation getStartLocation(){
        return this.startLocation;
    }
}
