package com.zblouse.fantasyfitness.world;

import android.util.Log;

import com.zblouse.fantasyfitness.activity.DeviceServiceType;
import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.activity.ToastDeviceService;
import com.zblouse.fantasyfitness.combat.encounter.EncounterDifficultyLevel;
import com.zblouse.fantasyfitness.core.DomainService;
import com.zblouse.fantasyfitness.user.UserGameState;
import com.zblouse.fantasyfitness.user.UserGameStateRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameLocationService implements DomainService<GameLocation> {

    public static final String VALLEY_OF_MONSTERS = "Valley of Monsters";
    public static final String LAST_TOWER = "Last Tower";
    public static final String MONASTARY = "Monastary";
    public static final String ARDUWYN = "Arduwyn";
    public static final String NORTH_ROAD = "North Road";
    public static final String FAOLYN = "Faolyn";
    public static final String RIVERLANDS = "Riverlands";
    public static final String BRIDGETON = "Bridgeton";
    public static final String THANADEL_VILLAGE = "Thanadel Village";
    public static final String WOODLANDS = "Woodlands";
    public static final String HILLS = "Hills";
    public static final String MOUNTAIN_PASS = "Mountain Pass";
    public static final String FARMLANDS = "Farmlands";

    private static final String TRAVEL_DESTINATION = "travelDestination";

    private final MainActivity mainActivity;
    private final GameLocationRepository gameLocationRepository;


    public GameLocationService(MainActivity mainActivity){
        this.mainActivity = mainActivity;
        gameLocationRepository = new GameLocationRepository(mainActivity);
    }

    public GameLocationService(MainActivity mainActivity, GameLocationRepository gameLocationRepository){
        this.mainActivity = mainActivity;
        this.gameLocationRepository = gameLocationRepository;
    }

    public void fetchLocation(String locationName, Map<String, Object> metadata){
        gameLocationRepository.getLocationByName(locationName, this, metadata);
    }

    @Override
    public void repositoryResponse(GameLocation responseBody, Map<String, Object> metadata) {
        mainActivity.publishEvent(new GameLocationFetchEvent(responseBody, metadata));
    }

    @Override
    public void interDomainServiceResponse(Object responseObject, Map<String, Object> metadata) {
        if(metadata.get(INTER_DOMAIN_SERVICE_RESPONSE_CLASS_KEY).equals(UserGameState.class)){
            UserGameState userGameState = (UserGameState) responseObject;
            if(metadata.get(ORIGIN_FUNCTION).equals("travel")){
                travel(((GameLocation)metadata.get(TRAVEL_DESTINATION)),userGameState);
            }
        }
    }

    //implements Dijkstra's Algorithm
    public GameLocationPaths generatePaths(String startingLocationName){
        List<GameLocation> allLocations = gameLocationRepository.getAllGameLocations();

        GameLocation startingLocation = null;
        for(GameLocation location: allLocations){
            if(location.getLocationName().equals(startingLocationName)){
                startingLocation = location;
                break;
            }
        }
        if(startingLocation == null){
            return null;
        }

        allLocations.remove(startingLocation);
        GameLocationPaths paths = new GameLocationPaths(startingLocation);
        GameLocation lastLocationAdded = startingLocation;
        double lastLocationTotalDistance = 0;
        List<GameLocation> lastLocationPath = new ArrayList<>();
        Map<GameLocation, Path> availableLocations = new HashMap<>();

        while(!allLocations.isEmpty()){
            Map<GameLocation, Double> newLocations = lastLocationAdded.getConnectedLocations();
            for(GameLocation location: newLocations.keySet()){
                //making sure we haven't yet added this location to the Path
                if(allLocations.contains(location)){
                    List<GameLocation> newLocationPath = new ArrayList<>(lastLocationPath);
                    newLocationPath.add(location);
                    if(availableLocations.containsKey(location)){
                        if(availableLocations.get(location).getDistanceKm() > lastLocationTotalDistance + newLocations.get(location)){
                            availableLocations.remove(location);
                            availableLocations.put(location, new Path(lastLocationTotalDistance + newLocations.get(location), newLocationPath));
                        }
                    }else {
                        availableLocations.put(location, new Path(lastLocationTotalDistance + newLocations.get(location), newLocationPath));
                    }
                }
            }
            //Find the closest location still in available locations
            GameLocation closestLocation = null;
            for(GameLocation location: availableLocations.keySet()){
                if(closestLocation == null){
                    closestLocation = location;
                } else if(availableLocations.get(location).getDistanceKm() < availableLocations.get(closestLocation).getDistanceKm()) {
                    closestLocation = location;
                }
            }

            paths.addPath(closestLocation.getLocationName(), availableLocations.get(closestLocation));
            lastLocationAdded = gameLocationRepository.getLocationById(closestLocation.getId());
            lastLocationTotalDistance = availableLocations.get(closestLocation).getDistanceKm();
            lastLocationPath = availableLocations.get(closestLocation).getLocationPath();
            availableLocations.remove(closestLocation);
            allLocations.remove(closestLocation);
        }

        return paths;
    }

    public void travel(GameLocation gameLocation){
        Map<String, Object> metadata = new HashMap<>();
        metadata.put(INTER_DOMAIN_SERVICE_ORIGIN_KEY, this);
        metadata.put(ORIGIN_FUNCTION, "travel");
        metadata.put(TRAVEL_DESTINATION, gameLocation);
        mainActivity.getUserGameStateService().fetchUserGameState(mainActivity.getCurrentUser().getUid(),metadata);
    }

    private void travel(GameLocation travelDestination, UserGameState userGameState){
        GameLocationPaths gameLocationPaths = generatePaths(userGameState.getCurrentGameLocationName());
        Path path = gameLocationPaths.getPath(travelDestination.getLocationName());
        if(path.getDistanceKm() <= (userGameState.getSavedWorkoutDistanceMeters()/1000)){
            mainActivity.getUserGameStateService().addUserGameDistance(mainActivity.getCurrentUser().getUid(),(-1)*path.getDistanceKm() *1000,new HashMap<>());
            mainActivity.getUserGameStateService().updateUserGameLocation(mainActivity.getCurrentUser().getUid(),travelDestination.getLocationName(),new HashMap<>());
        }else {
            //This shouldn't be possible, as the button should be disabled, but send the toast anyway
            ((ToastDeviceService)mainActivity.getDeviceService(DeviceServiceType.TOAST)).sendToast("Unable to travel, you do not have enough saved distance. Go on a run or walk to get more distance.");
        }
    }

    public void initializeLocationDatabase(){
        if(gameLocationRepository.databaseInitialized()){
            return;
        }
        GameLocation valleyOfMonsters = new GameLocation(VALLEY_OF_MONSTERS,"Beware adventurer, in this valley across the mountains in the far north reside some of the most dangerous known monsters.");
        gameLocationRepository.createGameLocation(valleyOfMonsters);

        GameLocation mountainTower = new GameLocation(LAST_TOWER,"This tower is situated high in the mountains. It guards the mountain pass between the " + VALLEY_OF_MONSTERS + " and the civilized world.");
        gameLocationRepository.createGameLocation(mountainTower);

        GameLocation monastary = new GameLocation(MONASTARY, "A quiet monastery nestled in the wilderness, many miles from any other settlement.");
        gameLocationRepository.createGameLocation(monastary);

        GameLocation arduwyn = new GameLocation(ARDUWYN, "A small coastal fishing village in the far north.");
        gameLocationRepository.createGameLocation(arduwyn);

        GameLocation northRoad = new GameLocation(NORTH_ROAD,"The main road between Faolyn and Arduwyn. Travelers should be wary and travel in groups as monster attacks are common.");
        gameLocationRepository.createGameLocation(northRoad);

        GameLocation faolyn = new GameLocation(FAOLYN, "The largest settlement in the duchy and the only one that could rightfully be called a city. Faolyn is home to the Duke and is the main destination for travelers to the Duchy of the North from the rest of the Empire, most of whom arrive by ship.");
        gameLocationRepository.createGameLocation(faolyn);

        GameLocation riverlands = new GameLocation(RIVERLANDS, "The riverlands is a wild area between Faolyn and Bridgeton.");
        gameLocationRepository.createGameLocation(riverlands);

        GameLocation bridgeton = new GameLocation(BRIDGETON, "Bridgeton is a town built surrounding a bridge over the White River. It is the only crossing between Faolyn and the river's origin in the mountains.");
        gameLocationRepository.createGameLocation(bridgeton);

        GameLocation thanadelVillage = new GameLocation(THANADEL_VILLAGE, "Thanadel village is a quaint village nestled in the forest.");
        gameLocationRepository.createGameLocation(thanadelVillage);

        GameLocation woodlands = new GameLocation(WOODLANDS, "The woodlands are a sparsely populated area containing several varieties of low level monsters.");
        gameLocationRepository.createGameLocation(woodlands);

        GameLocation hills = new GameLocation(HILLS, "The hills are a bleak area between the mountains and the forest. Monsters from the mountains have been known to occasionally forage here.");
        gameLocationRepository.createGameLocation(hills);

        GameLocation mountainPass = new GameLocation(MOUNTAIN_PASS, "The mountain pass serves as the main land entry into the Duchy of the North. Dwarves frequent the area to trade with merchants bound for Faolyn.");
        gameLocationRepository.createGameLocation(mountainPass);

        GameLocation farmlands = new GameLocation(FARMLANDS, "An area of farmlands surrounding Faolyn.");
        gameLocationRepository.createGameLocation(farmlands);

        gameLocationRepository.addGameLocationConnection(valleyOfMonsters,mountainTower,10);
        gameLocationRepository.addGameLocationConnection(mountainTower,arduwyn,10);
        gameLocationRepository.addGameLocationConnection(arduwyn,monastary,5);
        gameLocationRepository.addGameLocationConnection(arduwyn,northRoad,5);
        gameLocationRepository.addGameLocationConnection(monastary,hills,10);
        gameLocationRepository.addGameLocationConnection(hills,mountainPass,15);
        gameLocationRepository.addGameLocationConnection(hills,bridgeton,15);
        gameLocationRepository.addGameLocationConnection(bridgeton,mountainPass,20);
        gameLocationRepository.addGameLocationConnection(bridgeton,riverlands,5);
        gameLocationRepository.addGameLocationConnection(bridgeton,woodlands,5);
        gameLocationRepository.addGameLocationConnection(woodlands,thanadelVillage,1);
        gameLocationRepository.addGameLocationConnection(thanadelVillage,farmlands,2);
        gameLocationRepository.addGameLocationConnection(farmlands,faolyn,1);
        gameLocationRepository.addGameLocationConnection(riverlands,faolyn,5);
        gameLocationRepository.addGameLocationConnection(faolyn,northRoad,5);
        gameLocationRepository.addGameLocationConnection(monastary,northRoad,5);
    }

    public static boolean isWildernessLocation(String gameLocationName){
        List<String> wildernessLocations = Arrays.asList(VALLEY_OF_MONSTERS,NORTH_ROAD,RIVERLANDS, WOODLANDS, HILLS);

        return wildernessLocations.contains(gameLocationName);
    }

    public static EncounterDifficultyLevel getLocationDifficulty(String gameLocationName){
        //TODO update these to use the correct difficulty when encounters with that difficulty are implemented
        if(gameLocationName.equals(VALLEY_OF_MONSTERS)){
            return EncounterDifficultyLevel.MEDIUM;
        } else if(gameLocationName.equals(NORTH_ROAD)){
            return EncounterDifficultyLevel.MEDIUM;
        } else if(gameLocationName.equals(RIVERLANDS)){
            return EncounterDifficultyLevel.MEDIUM;
        } else if(gameLocationName.equals(HILLS)){
            return EncounterDifficultyLevel.MEDIUM;
        } else if(gameLocationName.equals(WOODLANDS)){
            return EncounterDifficultyLevel.EASY;
        }
        return EncounterDifficultyLevel.EASY;
    }
}
