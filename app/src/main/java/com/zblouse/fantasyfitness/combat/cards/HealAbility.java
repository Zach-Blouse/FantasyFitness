package com.zblouse.fantasyfitness.combat.cards;

public class HealAbility extends Ability {

    public static final String HEAL_AMOUNT_FIELD = "healAmount";

    private final int healAmount;

    public HealAbility(String abilityName, String abilityDescription, AbilityTarget abilityTarget, int healAmount){
        super(abilityName, abilityDescription, AbilityType.HEAL, abilityTarget);
        this.healAmount = healAmount;
    }

    public int getHealAmount(){
        return this.healAmount;
    }
}
