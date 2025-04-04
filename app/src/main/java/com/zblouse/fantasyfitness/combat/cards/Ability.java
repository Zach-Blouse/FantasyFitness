package com.zblouse.fantasyfitness.combat.cards;

public abstract class Ability {

    public static final String NAME_FIELD = "abilityName";
    public static final String DESCRIPTION_FIELD = "abilityDescription";
    public static final String ABILITY_TYPE_FIELD = "abilityType";
    public static final String ABILITY_TARGET_FIELD = "abilityTarget";

    protected String abilityName;
    protected String abilityDescription;
    protected AbilityType abilityType;
    protected AbilityTarget abilityTarget;

    public Ability(String abilityName, String abilityDescription, AbilityType abilityType, AbilityTarget abilityTarget){
        this.abilityName = abilityName;
        this.abilityDescription = abilityDescription;
        this.abilityType = abilityType;
        this.abilityTarget = abilityTarget;
    }

    public String getAbilityName(){
        return this.abilityName;
    }

    public String getAbilityDescription(){
        return this.abilityDescription;
    }

    public AbilityType getAbilityType(){
        return this.abilityType;
    }

    public AbilityTarget getAbilityTarget(){
        return this.abilityTarget;
    }
}
