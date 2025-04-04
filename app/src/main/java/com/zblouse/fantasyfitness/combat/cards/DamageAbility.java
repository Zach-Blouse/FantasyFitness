package com.zblouse.fantasyfitness.combat.cards;

public class DamageAbility extends Ability {

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
