package com.zblouse.fantasyfitness.user;

import com.zblouse.fantasyfitness.core.Event;
import com.zblouse.fantasyfitness.core.EventType;

import java.util.Map;

public class UserExistEvent extends Event {

    private final boolean userExists;

    public UserExistEvent(boolean userExists, Map<String, Object> metadata){
        super(EventType.USER_EXIST_EVENT, metadata);
        this.userExists = userExists;
    }

    public boolean exists(){
        return userExists;
    }
}
