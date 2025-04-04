package com.zblouse.fantasyfitness.combat.cards;

public class DamageAbility extends Ability {

    public static final String DAMAGE_TYPE_FIELD = "damageType";
    public static final String ATTACK_TYPE_FIELD = "attackType";
    public static final String DAMAGE_AMOUNT_FIELD = "damageAmount";
    private final DamageType damageType;
    private final AttackType attackType;
    private final int damageAmount;

    public DamageAbility(String abilityName, String abilityDescription, AbilityTarget abilityTarget, DamageType damageType, AttackType attackType, int damageAmount){
        super(abilityName,abilityDescription, AbilityType.DAMAGE, abilityTarget);
        this.damageAmount = damageAmount;
        this.damageType = damageType;
        this.attackType = attackType;
    }

    public DamageType getDamageType(){
        return this.damageType;
    }

    public int getDamageAmount(){
        return this.damageAmount;
    }

    public AttackType getAttackType(){
        return this.attackType;
    }
}
