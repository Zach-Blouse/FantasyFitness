package com.zblouse.fantasyfitness.combat;

public class DamageAbility extends Ability {

    private final DamageType damageType;
    private final int damageAmount;

    public DamageAbility(String abilityName, String abilityDescription, DamageType damageType, int damageAmount){
        super(abilityName,abilityDescription,AbilityType.DAMAGE);
        this.damageAmount = damageAmount;
        this.damageType = damageType;
    }

    public DamageType getDamageType(){
        return this.damageType;
    }

    public int getDamageAmount(){
        return this.damageAmount;
    }
}
