package com.zblouse.fantasyfitness.user;

import com.zblouse.fantasyfitness.core.Event;
import com.zblouse.fantasyfitness.core.EventType;

import java.util.Map;

public class UserGameStateUpdateEvent extends Event {

    private final UserGameState userGameState;

    public UserGameStateUpdateEvent(UserGameState userGameState, Map<String, Object> metadata){
        super(EventType.USER_GAME_STATE_UPDATE_EVENT, metadata);
        this.userGameState = userGameState;
    }

    public UserGameState getUserGameState(){
        return this.userGameState;
    }
}
