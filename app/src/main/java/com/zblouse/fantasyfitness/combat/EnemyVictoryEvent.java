package com.zblouse.fantasyfitness.combat;

import com.zblouse.fantasyfitness.core.Event;
import com.zblouse.fantasyfitness.core.EventType;

import java.util.HashMap;

public class EnemyVictoryEvent extends Event {

    public EnemyVictoryEvent(){
        super(EventType.ENEMY_VICTORY_EVENT, new HashMap<>());
    }
}
