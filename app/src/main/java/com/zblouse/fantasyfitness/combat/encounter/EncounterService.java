package com.zblouse.fantasyfitness.combat.encounter;

import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.core.DomainService;

import java.util.Map;

public class EncounterService implements DomainService<Encounter> {

    private MainActivity mainActivity;
    private EncounterRepository encounterRepository;

    public EncounterService(MainActivity mainActivity){
        this.mainActivity = mainActivity;
        this.encounterRepository = new EncounterRepository(this);
    }

    public void fetchEncounter(String encounterName, Map<String, Object> metadata){
        encounterRepository.fetchEncounter(encounterName, metadata);
    }

    @Override
    public void repositoryResponse(Encounter encounter, Map<String, Object> metadata) {
        if(encounter != null){
            mainActivity.publishEvent(new EncounterFetchEvent(encounter,metadata));
        }
    }

    @Override
    public void interDomainServiceResponse(Object responseObject, Map<String, Object> metadata) {

    }

    //Should not be called during ops, only called during development until encounters are finalized
    public void initializeEncounters(){

    }
}
