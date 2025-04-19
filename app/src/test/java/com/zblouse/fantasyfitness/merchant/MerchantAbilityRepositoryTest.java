package com.zblouse.fantasyfitness.merchant;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import com.zblouse.fantasyfitness.combat.cards.AbilityType;

import org.junit.Test;
import org.mockito.Mockito;

public class MerchantAbilityRepositoryTest {

    @Test
    public void getBuffMerchantAbilityByNameTest(){
        MerchantHealAbilitySqlDatabase mockHealDatabase = Mockito.mock(MerchantHealAbilitySqlDatabase.class);
        MerchantDamageAbilitySqlDatabase mockDamageDatabase = Mockito.mock(MerchantDamageAbilitySqlDatabase.class);
        MerchantBuffAbilitySqlDatabase mockBuffDatabase = Mockito.mock(MerchantBuffAbilitySqlDatabase.class);

        MerchantAbilityRepository merchantAbilityRepository = new MerchantAbilityRepository(mockBuffDatabase,mockDamageDatabase,mockHealDatabase);

        merchantAbilityRepository.getMerchantAbilityByName("testName", AbilityType.BUFF);
        verify(mockBuffDatabase).readByAbilityName(eq("testName"));
    }

    @Test
    public void getDamageMerchantAbilityByNameTest(){
        MerchantHealAbilitySqlDatabase mockHealDatabase = Mockito.mock(MerchantHealAbilitySqlDatabase.class);
        MerchantDamageAbilitySqlDatabase mockDamageDatabase = Mockito.mock(MerchantDamageAbilitySqlDatabase.class);
        MerchantBuffAbilitySqlDatabase mockBuffDatabase = Mockito.mock(MerchantBuffAbilitySqlDatabase.class);

        MerchantAbilityRepository merchantAbilityRepository = new MerchantAbilityRepository(mockBuffDatabase,mockDamageDatabase,mockHealDatabase);

        merchantAbilityRepository.getMerchantAbilityByName("testName", AbilityType.DAMAGE);
        verify(mockDamageDatabase).readByAbilityName(eq("testName"));
    }

    @Test
    public void getHealMerchantAbilityByNameTest(){
        MerchantHealAbilitySqlDatabase mockHealDatabase = Mockito.mock(MerchantHealAbilitySqlDatabase.class);
        MerchantDamageAbilitySqlDatabase mockDamageDatabase = Mockito.mock(MerchantDamageAbilitySqlDatabase.class);
        MerchantBuffAbilitySqlDatabase mockBuffDatabase = Mockito.mock(MerchantBuffAbilitySqlDatabase.class);

        MerchantAbilityRepository merchantAbilityRepository = new MerchantAbilityRepository(mockBuffDatabase,mockDamageDatabase,mockHealDatabase);

        merchantAbilityRepository.getMerchantAbilityByName("testName", AbilityType.HEAL);
        verify(mockHealDatabase).readByAbilityName(eq("testName"));
    }
}
