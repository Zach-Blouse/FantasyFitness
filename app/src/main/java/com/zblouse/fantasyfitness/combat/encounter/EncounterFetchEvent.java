package com.zblouse.fantasyfitness.combat.encounter;

import com.zblouse.fantasyfitness.core.Event;
import com.zblouse.fantasyfitness.core.EventType;

import java.util.Map;

public class EncounterFetchEvent extends Event {

    private final Encounter encounter;

    public EncounterFetchEvent(Encounter encounter, Map<String, Object> metadata){
        super(EventType.ENCOUNTER_FETCH_EVENT, metadata);
        this.encounter = encounter;
    }

    public Encounter getEncounter(){
        return this.encounter;
    }
}
