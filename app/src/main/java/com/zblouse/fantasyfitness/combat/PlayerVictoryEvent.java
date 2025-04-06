package com.zblouse.fantasyfitness.combat;

import com.zblouse.fantasyfitness.core.Event;
import com.zblouse.fantasyfitness.core.EventType;

import java.util.HashMap;

public class PlayerVictoryEvent extends Event {

    public PlayerVictoryEvent(){
        super(EventType.PLAYER_VICTORY_EVENT, new HashMap<>());
    }
}
