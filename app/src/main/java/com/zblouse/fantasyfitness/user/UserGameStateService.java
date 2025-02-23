package com.zblouse.fantasyfitness.user;

import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.core.DomainService;
import com.zblouse.fantasyfitness.world.GameLocationDistance;

import java.util.List;
import java.util.Map;

public class UserGameStateService implements DomainService<UserGameState> {

    public static final String CALLING_FUNCTION_KEY = "callingFunctionKey";
    public static final String LOCATION_FIELD = "userGameLocation";
    public static final String USER_DISTANCE_FIELD = "userStoredDistance";
    public static final String STATE_FIELD_UPDATED = "stateFieldUpdated";
    public static final String NEW_LOCATION = "newLocation";

    private static final String FETCH_GAME_STATE_FUNCTION = "fetchGameState";
    private static final String INITIALIZE_GAME_STATE_FUNCTION = "initializeGameState";
    private static final String ADD_USER_GAME_DISTANCE = "addUserGameDistance";
    private static final String UPDATE_USER_GAME_LOCATION = "updateUserGameLocation";

    private final MainActivity activity;
    private final UserGameStateRepository userGameStateRepository;

    public UserGameStateService(MainActivity mainActivity){
        this.activity = mainActivity;
        userGameStateRepository = new UserGameStateRepository(this);
    }

    public UserGameStateService(MainActivity mainActivity, UserGameStateRepository userGameStateRepository){
        this.activity = mainActivity;
        this.userGameStateRepository = userGameStateRepository;
    }

    public void fetchUserGameState(String userId, Map<String, Object> metadata){
        metadata.put(CALLING_FUNCTION_KEY, FETCH_GAME_STATE_FUNCTION);
        userGameStateRepository.getUserGameState(userId, metadata);
    }

    public void addUserGameDistance(String userId, double distanceKm, Map<String, Object> metadata){
        metadata.put(CALLING_FUNCTION_KEY,ADD_USER_GAME_DISTANCE);
        userGameStateRepository.modifyUserDistance(userId, distanceKm, metadata);
    }

    public void initializeUserGameState(String userId, String initialLocationName,Map<String, Object> metadata ){
        metadata.put(CALLING_FUNCTION_KEY, INITIALIZE_GAME_STATE_FUNCTION);
        userGameStateRepository.writeUserGameState(userId, initialLocationName,0, metadata);
    }

    public void updateUserGameLocation(String userId, String newLocationName, Map<String, Object> metadata){
        metadata.put(CALLING_FUNCTION_KEY, UPDATE_USER_GAME_LOCATION);
        metadata.put(NEW_LOCATION, newLocationName);
        userGameStateRepository.updateUserGameLocation(userId, newLocationName, metadata);
    }

    @Override
    public void repositoryResponse(UserGameState responseBody, Map<String, Object> metadata) {
        if(metadata.containsKey(CALLING_FUNCTION_KEY)) {
            if (metadata.get(CALLING_FUNCTION_KEY).equals(FETCH_GAME_STATE_FUNCTION)) {
                if(metadata.containsKey(INTER_DOMAIN_SERVICE_ORIGIN_KEY)){
                    metadata.put(INTER_DOMAIN_SERVICE_RESPONSE_CLASS_KEY, UserGameState.class);
                    ((DomainService)metadata.get(INTER_DOMAIN_SERVICE_ORIGIN_KEY)).interDomainServiceResponse(responseBody,metadata);
                } else {
                    activity.publishEvent(new UserGameStateFetchResponseEvent(responseBody, metadata));
                }
            } else if(metadata.get(CALLING_FUNCTION_KEY).equals(UPDATE_USER_GAME_LOCATION)){
                metadata.put(STATE_FIELD_UPDATED, LOCATION_FIELD);
                activity.publishEvent(new UserGameStateUpdateEvent(metadata));
            }else if(metadata.get(CALLING_FUNCTION_KEY).equals(ADD_USER_GAME_DISTANCE)){
                metadata.put(STATE_FIELD_UPDATED, USER_DISTANCE_FIELD);
                activity.publishEvent(new UserGameStateUpdateEvent(metadata));
            }
        }
    }

    @Override
    public void interDomainServiceResponse(Object responseObject, Map<String, Object> metadata) {

    }
}
