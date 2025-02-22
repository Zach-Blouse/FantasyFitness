package com.zblouse.fantasyfitness.world;

import android.content.Context;
import android.util.Log;

import com.zblouse.fantasyfitness.core.DomainService;
import com.zblouse.fantasyfitness.core.Repository;

import java.util.ArrayList;
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

    public GameLocationRepository(GameLocationSqlDatabase gameLocationSqlDatabase, GameLocationDistanceSqlDatabase gameLocationDistanceSqlDatabase){
        this.gameLocationSqlDatabase = gameLocationSqlDatabase;
        this.gameLocationDistanceSqlDatabase = gameLocationDistanceSqlDatabase;
    }

    public boolean databaseInitialized(){
        return !gameLocationSqlDatabase.getAllLocations().isEmpty();
    }

    public void getLocationByName(String locationName, DomainService<GameLocation> domainService, Map<String, Object> metadata) {
        GameLocation location = gameLocationSqlDatabase.getLocationByName(locationName);
        if (location == null) {
            domainService.repositoryResponse(null, metadata);
        } else {
            List<GameLocationDistance> locationDistances = gameLocationDistanceSqlDatabase.getAllLocationDistancesForLocation(location);
            Map<GameLocation, Double> locationMap = new HashMap<>();
            for (GameLocationDistance locationDistance : locationDistances) {
                if (locationDistance.getLocation1Id().equals(location.getId())) {
                    locationMap.put(gameLocationSqlDatabase.getLocationById(locationDistance.getLocation2Id()), locationDistance.getDistanceMiles());
                } else {
                    locationMap.put(gameLocationSqlDatabase.getLocationById(locationDistance.getLocation1Id()), locationDistance.getDistanceMiles());
                }
            }

            domainService.repositoryResponse(new GameLocation(location.getId(), location.getLocationName(), location.getLocationDescription(), locationMap), metadata);
        }
    }

    public List<GameLocation> getAllGameLocations(){
        List<GameLocation> gameLocations = gameLocationSqlDatabase.getAllLocations();
        List<GameLocation> gameLocationsWithDistances = new ArrayList<>();
        for(GameLocation location:gameLocations){
            List<GameLocationDistance> locationDistances = gameLocationDistanceSqlDatabase.getAllLocationDistancesForLocation(location);
            Map<GameLocation, Double> locationMap = new HashMap<>();
            for (GameLocationDistance locationDistance : locationDistances) {
                if (locationDistance.getLocation1Id().equals(location.getId())) {
                    locationMap.put(gameLocationSqlDatabase.getLocationById(locationDistance.getLocation2Id()), locationDistance.getDistanceMiles());
                } else {
                    locationMap.put(gameLocationSqlDatabase.getLocationById(locationDistance.getLocation1Id()), locationDistance.getDistanceMiles());
                }
            }
            gameLocationsWithDistances.add(new GameLocation(location.getId(), location.getLocationName(), location.getLocationDescription(), locationMap));
        }

        return gameLocationsWithDistances;
    }

    public GameLocation getLocationById(int locationId) {
        GameLocation location = gameLocationSqlDatabase.getLocationById(locationId);
        if (location == null) {
            return null;
        } else {
            List<GameLocationDistance> locationDistances = gameLocationDistanceSqlDatabase.getAllLocationDistancesForLocation(location);
            Map<GameLocation, Double> locationMap = new HashMap<>();
            for (GameLocationDistance locationDistance : locationDistances) {
                if (locationDistance.getLocation1Id().equals(location.getId())) {
                    locationMap.put(gameLocationSqlDatabase.getLocationById(locationDistance.getLocation2Id()), locationDistance.getDistanceMiles());
                } else {
                    locationMap.put(gameLocationSqlDatabase.getLocationById(locationDistance.getLocation1Id()), locationDistance.getDistanceMiles());
                }
            }

            return new GameLocation(location.getId(), location.getLocationName(), location.getLocationDescription(), locationMap);
        }
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

    @Override
    public void updateCallback(boolean success, Map<String, Object> metadata) {

    }
}
