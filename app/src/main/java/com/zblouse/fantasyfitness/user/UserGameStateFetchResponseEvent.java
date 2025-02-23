package com.zblouse.fantasyfitness.user;

import com.zblouse.fantasyfitness.core.Event;
import com.zblouse.fantasyfitness.core.EventType;

import java.util.Map;

public class UserGameStateFetchResponseEvent extends Event {

    private final UserGameState userGameState;

    public UserGameStateFetchResponseEvent(UserGameState userGameState, Map<String, Object> metadata){
        super(EventType.USER_GAME_STATE_FETCH_RESPONSE_EVENT, metadata);
        this.userGameState = userGameState;
    }

    public UserGameState getUserGameState(){
        return this.userGameState;
    }
}
