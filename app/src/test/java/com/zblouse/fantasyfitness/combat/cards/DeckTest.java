package com.zblouse.fantasyfitness.combat.cards;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class DeckTest {

    @Test
    public void needToLoadCardsTest(){
        Deck testedDeck = new Deck("testUser","testDeck", Arrays.asList("1", "2", "3"));
        assert(testedDeck.needToLoadCards());
    }

    @Test
    public void isEmptyTest(){
        Deck testedDeck = new Deck("testUser","testDeck", new ArrayList<>());
        assert(testedDeck.isEmpty());
    }

    @Test
    public void notNeedToLoadCardsTest(){
        Deck testedDeck = new Deck("testUser","testDeck", Arrays.asList("1", "2", "3"));
        HealAbility testCharacterHealAbility =  new HealAbility("HealAbility","testAbility",AbilityTarget.SINGLE_ALLY,5);
        DamageAbility testCharacterDamageAbility = new DamageAbility("burn","burn description",AbilityTarget.SINGLE_ENEMY,DamageType.FIRE,AttackType.RANGED,5);
        CharacterCard testCharacterCard = new CharacterCard("testUser", UUID.randomUUID().toString(),
                "characterCard","testCharacterDescription",10,
                Arrays.asList( testCharacterHealAbility, testCharacterDamageAbility)
                ,new ArrayList<>());
        testedDeck.setCards(Arrays.asList(testCharacterCard));

        assertFalse(testedDeck.needToLoadCards());
    }
}
