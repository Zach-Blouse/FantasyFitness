package com.zblouse.fantasyfitness.user;

import com.zblouse.fantasyfitness.core.Event;
import com.zblouse.fantasyfitness.core.EventType;

import java.util.Map;

public class CreateAccountResponseEvent extends Event {

    private final boolean accountCreated;

    public CreateAccountResponseEvent(boolean accountCreated, Map<String, Object> metadata){
        super(EventType.CREATE_ACCOUNT_RESPONSE, metadata);
        this.accountCreated = accountCreated;
    }

    public boolean isAccountCreated(){
        return accountCreated;
    }
}
