package com.zblouse.fantasyfitness.combat;

import com.zblouse.fantasyfitness.core.Event;
import com.zblouse.fantasyfitness.core.EventType;

import java.util.HashMap;

public class EnemyTurnCompleteEvent extends Event {

    public EnemyTurnCompleteEvent(){
        super(EventType.ENEMY_TURN_COMPLETE_EVENT,new HashMap<>());
    }
}
