package com.zblouse.fantasyfitness.combat;

public abstract class Ability {

    protected String abilityName;
    protected String abilityDescription;
    protected AbilityType abilityType;

    public Ability(String abilityName, String abilityDescription, AbilityType abilityType){
        this.abilityName = abilityName;
        this.abilityDescription = abilityDescription;
        this.abilityType = abilityType;
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
}
