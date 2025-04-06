package com.zblouse.fantasyfitness.combat.cards;

public class DebuffAbility extends Ability {

    public static final String BUFF_TYPE_FIELD = "buffType";
    public static final String DEBUFF_AMOUNT_FIELD = "debuffAmount";

    private BuffType buffType;
    private int debuffAmount;

    public DebuffAbility(String abilityName, String abilityDescription, AbilityTarget abilityTarget, BuffType buffType, int debuffAmount) {
        super(abilityName, abilityDescription, AbilityType.DEBUFF, abilityTarget);
        this.buffType = buffType;
        this.debuffAmount = debuffAmount;
    }

    public BuffType getBuffType(){
        return this.buffType;
    }

    public int getDeuffAmount(){
        return this.debuffAmount;
    }
}
