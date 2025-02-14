package com.zblouse.fantasyfitness.world;

import android.util.Log;

import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.core.DomainService;

import java.util.Map;

public class GameLocationService implements DomainService<GameLocation> {

    public static final String VALLEY_OF_MONSTERS = "Valley of Monsters";
    public static final String LAST_TOWER = "Last Tower";
    private final MainActivity mainActivity;
    private final GameLocationRepository gameLocationRepository;

    public GameLocationService(MainActivity mainActivity){
        this.mainActivity = mainActivity;
        gameLocationRepository = new GameLocationRepository(mainActivity);
    }

    public void fetchLocation(String locationName, Map<String, Object> metadata){
        gameLocationRepository.getLocationByName(locationName, this, metadata);
    }

    @Override
    public void repositoryResponse(GameLocation responseBody, Map<String, Object> metadata) {
        Log.e("GAMELOCATIONSERVICE", "REPOSTIORY RESPONSE " + responseBody.getLocationName());
        mainActivity.publishEvent(new GameLocationFetchEvent(responseBody, metadata));
    }

    public void initializeLocationDatabase(){
        if(gameLocationRepository.databaseInitialized()){
            return;
        }
        GameLocation valleyOfMonsters = new GameLocation(VALLEY_OF_MONSTERS,"Beware adventurer, in this valley across the mountains in the far north reside some of the most dangerous known monsters.");
        gameLocationRepository.createGameLocation(valleyOfMonsters);

        GameLocation mountainTower = new GameLocation(LAST_TOWER,"This tower is situated high in the mountains. It guards the mountain pass between the " + VALLEY_OF_MONSTERS + " and the civilized world.");
        gameLocationRepository.createGameLocation(mountainTower);

        gameLocationRepository.addGameLocationConnection(valleyOfMonsters,mountainTower,20);
    }
}
