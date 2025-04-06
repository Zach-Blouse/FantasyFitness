package com.zblouse.fantasyfitness.combat;

import com.zblouse.fantasyfitness.core.Event;
import com.zblouse.fantasyfitness.core.EventType;

import java.util.Map;

public class CombatStateUpdateEvent extends Event{

    private final CombatStateModel combatStateModel;

    public CombatStateUpdateEvent(CombatStateModel combatStateModel, Map<String, Object> metadata){
        super(EventType.COMBAT_STATE_UPDATE_EVENT, metadata);
        this.combatStateModel = combatStateModel;
    }

    public CombatStateModel getCombatStateModel(){
        return this.combatStateModel;
    }

}
