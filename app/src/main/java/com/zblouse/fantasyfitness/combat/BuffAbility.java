package com.zblouse.fantasyfitness.combat;

public class BuffAbility extends Ability {

    private BuffType buffType;
    private int buffAmount;

    public BuffAbility(String abilityName, String abilityDescription, AbilityTarget abilityTarget, BuffType buffType, int buffAmount) {
        super(abilityName, abilityDescription, AbilityType.BUFF, abilityTarget);
        this.buffType = buffType;
        this.buffAmount = buffAmount;
    }

    public BuffType getBuffType(){
        return this.buffType;
    }

    public int getBuffAmount(){
        return this.buffAmount;
    }
}
