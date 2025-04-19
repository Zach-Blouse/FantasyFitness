package com.zblouse.fantasyfitness.merchant;

import android.content.Context;
import android.util.Log;

import com.zblouse.fantasyfitness.combat.cards.Ability;
import com.zblouse.fantasyfitness.combat.cards.AbilityType;
import com.zblouse.fantasyfitness.combat.cards.BuffAbility;
import com.zblouse.fantasyfitness.combat.cards.DamageAbility;
import com.zblouse.fantasyfitness.combat.cards.HealAbility;

public class MerchantAbilityRepository {

    private final MerchantBuffAbilitySqlDatabase merchantBuffAbilitySqlDatabase;
    private final MerchantDamageAbilitySqlDatabase merchantDamageAbilitySqlDatabase;
    private final MerchantHealAbilitySqlDatabase merchantHealAbilitySqlDatabase;

    public MerchantAbilityRepository(Context context){
        merchantBuffAbilitySqlDatabase = new MerchantBuffAbilitySqlDatabase(context);
        merchantDamageAbilitySqlDatabase = new MerchantDamageAbilitySqlDatabase(context);
        merchantHealAbilitySqlDatabase = new MerchantHealAbilitySqlDatabase(context);
    }

    public MerchantAbilityRepository(MerchantBuffAbilitySqlDatabase merchantBuffAbilitySqlDatabase, MerchantDamageAbilitySqlDatabase merchantDamageAbilitySqlDatabase, MerchantHealAbilitySqlDatabase merchantHealAbilitySqlDatabase){
        this.merchantBuffAbilitySqlDatabase = merchantBuffAbilitySqlDatabase;
        this.merchantDamageAbilitySqlDatabase = merchantDamageAbilitySqlDatabase;
        this.merchantHealAbilitySqlDatabase = merchantHealAbilitySqlDatabase;
    }

    public Ability getMerchantAbilityByName(String name, AbilityType abilityType){
        Log.e("MerchantAbilityRepository","Getting merchant by name: " + name + " with ability type: " + abilityType.toString());
        if(abilityType.equals(AbilityType.DAMAGE)){
            return merchantDamageAbilitySqlDatabase.readByAbilityName(name);
        } else if(abilityType.equals(AbilityType.HEAL)){
            return merchantHealAbilitySqlDatabase.readByAbilityName(name);
        } else if(abilityType.equals(AbilityType.BUFF)){
            return merchantBuffAbilitySqlDatabase.readByAbilityName(name);
        } else {
            Log.e("MerchantAbilityRepository", "Requesting ability type that has not been implemented in the merchant databases");
            return null;
        }
    }

    public void writeMerchantAbility(Ability ability){
        if(ability.getAbilityType().equals(AbilityType.DAMAGE)){
            merchantDamageAbilitySqlDatabase.write((DamageAbility) ability);
        } else if(ability.getAbilityType().equals(AbilityType.HEAL)){
            merchantHealAbilitySqlDatabase.write((HealAbility) ability);
        } else if(ability.getAbilityType().equals(AbilityType.BUFF)){
            merchantBuffAbilitySqlDatabase.write((BuffAbility) ability);
        } else {
            Log.e("MerchantAbilityRepository", "Writing ability type that has not been implemented in the merchant databases");
        }
    }
}
