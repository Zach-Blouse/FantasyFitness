package com.zblouse.fantasyfitness.user;

import com.zblouse.fantasyfitness.core.Event;
import com.zblouse.fantasyfitness.core.EventType;

import java.util.Map;

public class UserGameStateUpdateEvent extends Event {

    private String fieldUpdated;
    private Object fieldValue;

    public UserGameStateUpdateEvent(Map<String, Object> metadata){
        super(EventType.USER_GAME_STATE_UPDATE_EVENT, metadata);
    }

}
