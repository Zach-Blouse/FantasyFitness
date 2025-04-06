package com.zblouse.fantasyfitness.combat.cards;

public class BuffAbility extends Ability {

    public static final String BUFF_TYPE_FIELD = "buffType";
    public static final String BUFF_AMOUNT_FIELD = "buffAmount";

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
