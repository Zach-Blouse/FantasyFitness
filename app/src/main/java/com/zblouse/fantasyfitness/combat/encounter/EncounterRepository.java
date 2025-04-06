package com.zblouse.fantasyfitness.combat.encounter;

import com.zblouse.fantasyfitness.core.Repository;

import java.util.Map;

public class EncounterRepository implements Repository<Encounter> {

    private EncounterFirestoreDatabase encounterFirestoreDatabase;
    private EncounterService encounterService;

    public EncounterRepository(EncounterService encounterService){
        this.encounterService = encounterService;
        this.encounterFirestoreDatabase = new EncounterFirestoreDatabase();
    }

    public EncounterRepository(EncounterService encounterService, EncounterFirestoreDatabase encounterFirestoreDatabase){
        this.encounterService = encounterService;
        this.encounterFirestoreDatabase = encounterFirestoreDatabase;
    }

    public void fetchEncounter(String encounterName, Map<String, Object> metadata){
        encounterFirestoreDatabase.read(encounterName, this, metadata);
    }

    public void writeEncounter(Encounter encounter, Map<String, Object> metadata){
        encounterFirestoreDatabase.write(encounter, this, metadata);
    }

    @Override
    public void readCallback(Encounter encounter, Map<String, Object> metadata) {
        encounterService.repositoryResponse(encounter, metadata);
    }

    @Override
    public void writeCallback(Encounter object, Map<String, Object> metadata) {

    }

    @Override
    public void updateCallback(boolean success, Map<String, Object> metadata) {

    }
}
