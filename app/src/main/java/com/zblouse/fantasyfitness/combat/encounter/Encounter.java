package com.zblouse.fantasyfitness.combat.encounter;

import com.zblouse.fantasyfitness.combat.cards.Card;

import java.util.List;

public class Encounter {

    public static final String ENCOUNTER_NAME_FIELD = "encounterName";
    public static final String ENCOUNTER_DIFFICULTY_LEVEL_FIELD = "encounterDifficultyLevel";
    public static final String ENEMY_CARDS_FIELD = "enemyCards";
    private final EncounterDifficultyLevel encounterDifficultyLevel;
    private final List<Card> enemyCards;
    private final String encounterName;

    public Encounter(String encounterName, EncounterDifficultyLevel encounterDifficultyLevel, List<Card> cards){
        this.encounterName = encounterName;
        this.encounterDifficultyLevel = encounterDifficultyLevel;
        this.enemyCards = cards;
    }

    public String getEncounterName(){
        return this.encounterName;
    }

    public EncounterDifficultyLevel getEncounterDifficultyLevel(){
        return this.encounterDifficultyLevel;
    }

    public List<Card> getEnemyCards(){
        return this.enemyCards;
    }
}
