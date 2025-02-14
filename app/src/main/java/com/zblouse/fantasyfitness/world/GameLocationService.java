package com.zblouse.fantasyfitness.world;

import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.core.DomainService;

import java.util.Map;

public class GameLocationService implements DomainService<GameLocation> {

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
        mainActivity.publishEvent(new GameLocationFetchEvent(responseBody, metadata));
    }

    public void initializeLocationDatabase(){

    }
}
