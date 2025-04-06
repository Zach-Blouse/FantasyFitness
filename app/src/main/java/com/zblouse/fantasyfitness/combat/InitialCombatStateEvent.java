package com.zblouse.fantasyfitness.combat;

import com.zblouse.fantasyfitness.core.Event;
import com.zblouse.fantasyfitness.core.EventType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class InitialCombatStateEvent extends Event {

    private List<CombatCardModel> playerHand;
    private List<CombatCardModel> enemyHand;

    public InitialCombatStateEvent(CombatStateModel combatStateModel){
        super(EventType.INITIAL_COMBAT_STATE_SET_EVENT,new HashMap<>());
        playerHand = new ArrayList<>(combatStateModel.getPlayerHand());
        enemyHand = new ArrayList<>(combatStateModel.getEnemyHand());
    }

    public List<CombatCardModel> getPlayerHand(){
        return this.playerHand;
    }

    public List<CombatCardModel> getEnemyHand(){
        return this.enemyHand;
    }
}
