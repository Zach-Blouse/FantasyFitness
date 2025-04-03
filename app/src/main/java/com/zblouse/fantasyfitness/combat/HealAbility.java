package com.zblouse.fantasyfitness.combat;

public class HealAbility extends Ability {

    private final int healAmount;

    public HealAbility(String abilityName, String abilityDescription, AbilityTarget abilityTarget, int healAmount){
        super(abilityName, abilityDescription, AbilityType.HEAL, abilityTarget);
        this.healAmount = healAmount;
    }

    public int getHealAmount(){
        return this.healAmount;
    }
}
