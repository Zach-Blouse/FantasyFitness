package com.zblouse.fantasyfitness.world;

import android.content.Context;
import android.util.Log;

import com.zblouse.fantasyfitness.core.DomainService;
import com.zblouse.fantasyfitness.core.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameLocationRepository implements Repository<GameLocation> {

    private final GameLocationSqlDatabase gameLocationSqlDatabase;
    private final GameLocationDistanceSqlDatabase gameLocationDistanceSqlDatabase;

    public GameLocationRepository(Context context){
        gameLocationSqlDatabase = new GameLocationSqlDatabase(context);
        gameLocationDistanceSqlDatabase = new GameLocationDistanceSqlDatabase(context);
    }

    public boolean databaseInitialized(){
        return !gameLocationSqlDatabase.getAllLocations().isEmpty();
    }

    public void getLocationByName(String locationName, DomainService<GameLocation> domainService, Map<String, Object> metadata){
        GameLocation location = gameLocationSqlDatabase.getLocationByName(locationName);
        if(location == null){
            Log.e("GAMELOCATIONREPOSITORY", "Location: " + locationName + " is null");
            domainService.repositoryResponse(null, metadata);
        }
        List<GameLocationDistance> locationDistances = gameLocationDistanceSqlDatabase.getAllLocationDistancesForLocation(location);
        Map<GameLocation, Double> locationMap = new HashMap<>();
        for(GameLocationDistance locationDistance: locationDistances){
            if(locationDistance.getLocation1Id().equals(location.getId())){
                locationMap.put(gameLocationSqlDatabase.getLocationById(locationDistance.getLocation2Id()), locationDistance.getDistanceMiles());
            } else {
                locationMap.put(gameLocationSqlDatabase.getLocationById(locationDistance.getLocation1Id()), locationDistance.getDistanceMiles());
            }
        }

        domainService.repositoryResponse(new GameLocation(location.getId(),location.getLocationName(),location.getLocationDescription(),locationMap), metadata);
    }

    public void createGameLocation(GameLocation location){
        gameLocationSqlDatabase.addLocationToDatabase(location);
    }

    public void addGameLocationConnection(GameLocation location1, GameLocation location2, double distanceMiles){
        if(location1.getId() == null){
            location1 = gameLocationSqlDatabase.getLocationByName(location1.getLocationName());
        }
        if(location2.getId() == null){
            location2 = gameLocationSqlDatabase.getLocationByName(location2.getLocationName());
        }

        gameLocationDistanceSqlDatabase.addLocationDistanceToDatabase(new GameLocationDistance(location1.getId(), location2.getId(), distanceMiles));
    }

    @Override
    public void readCallback(GameLocation object, Map<String, Object> metadata) {

    }

    @Override
    public void writeCallback(GameLocation object, Map<String, Object> metadata) {

    }
}
