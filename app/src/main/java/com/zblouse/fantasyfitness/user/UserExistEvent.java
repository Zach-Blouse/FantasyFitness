package com.zblouse.fantasyfitness.user;

import com.zblouse.fantasyfitness.core.Event;
import com.zblouse.fantasyfitness.core.EventType;

public class UserExistEvent extends Event {

    private final boolean userExists;

    public UserExistEvent(boolean userExists){
        super(EventType.USER_EXIST_EVENT);
        this.userExists = userExists;
    }

    public boolean exists(){
        return userExists;
    }
}
