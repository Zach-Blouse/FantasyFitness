package com.zblouse.fantasyfitness.user;

import com.zblouse.fantasyfitness.core.Event;
import com.zblouse.fantasyfitness.core.EventType;

public class CreateAccountResponseEvent extends Event {

    private final boolean accountCreated;

    public CreateAccountResponseEvent(boolean accountCreated){
        super(EventType.CREATE_ACCOUNT_RESPONSE);
        this.accountCreated = accountCreated;
    }

    public boolean isAccountCreated(){
        return accountCreated;
    }
}
