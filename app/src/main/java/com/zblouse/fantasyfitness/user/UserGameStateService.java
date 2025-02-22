package com.zblouse.fantasyfitness.user;

import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.core.DomainService;
import com.zblouse.fantasyfitness.world.GameLocationDistance;

import java.util.List;
import java.util.Map;

public class UserGameStateService implements DomainService<UserGameState> {

    public static final String CALLING_FUNCTION_KEY = "callingFunctionKey";

    private static final String FETCH_GAME_STATE_FUNCTION = "fetchGameState";
    private static final String INITIALIZE_GAME_STATE_FUNCTION = "initializeGameState";
    private static final String ADD_USER_GAME_DISTANCE = "addUserGameDistance";
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

    @Override
    public void repositoryResponse(UserGameState responseBody, Map<String, Object> metadata) {
        if(metadata.containsKey(CALLING_FUNCTION_KEY)) {
            if (metadata.get(CALLING_FUNCTION_KEY).equals(FETCH_GAME_STATE_FUNCTION)) {
                activity.publishEvent(new UserGameStateUpdateEvent(responseBody,metadata));
            }
        }
    }
}
