package com.zblouse.fantasyfitness.combat.encounter;

import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import com.zblouse.fantasyfitness.combat.cards.AbilityTarget;
import com.zblouse.fantasyfitness.combat.cards.AttackType;
import com.zblouse.fantasyfitness.combat.cards.BuffAbility;
import com.zblouse.fantasyfitness.combat.cards.BuffType;
import com.zblouse.fantasyfitness.combat.cards.Card;
import com.zblouse.fantasyfitness.combat.cards.CharacterCard;
import com.zblouse.fantasyfitness.combat.cards.DamageAbility;
import com.zblouse.fantasyfitness.combat.cards.DamageType;
import com.zblouse.fantasyfitness.combat.cards.ItemCard;
import com.zblouse.fantasyfitness.combat.cards.ItemType;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class EncounterRepositoryTest {

    @Test
    public void fetchEncounterTest(){
        String testEncounterName = "testEncounter";
        EncounterService mockService = Mockito.mock(EncounterService.class);
        EncounterFirestoreDatabase mockDatabase = Mockito.mock(EncounterFirestoreDatabase.class);

        EncounterRepository testedRepository = new EncounterRepository(mockService,mockDatabase);

        testedRepository.fetchEncounter(testEncounterName,new HashMap<>());

        verify(mockDatabase).read(eq(testEncounterName),eq(testedRepository),anyMap());
    }

    @Test
    public void writeEncounterTest(){
        String testEncounterName = "testEncounter";
        EncounterService mockService = Mockito.mock(EncounterService.class);
        EncounterFirestoreDatabase mockDatabase = Mockito.mock(EncounterFirestoreDatabase.class);

        EncounterRepository testedRepository = new EncounterRepository(mockService,mockDatabase);
        List<Card> encounterCards = new ArrayList<>();

        CharacterCard encounterCharacterCard1 = new CharacterCard("encounter", UUID.randomUUID().toString(),"Dark Zach","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.MELEE,1),3);
        encounterCards.add(encounterCharacterCard1);
        CharacterCard encounterCharacterCard2 = new CharacterCard("encounter", UUID.randomUUID().toString(),"Dark Zach 2","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.MELEE,1),15);
        encounterCards.add(encounterCharacterCard2);
        CharacterCard encounterCharacterCard3 = new CharacterCard("encounter", UUID.randomUUID().toString(),"Dark Ranged Zach","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.RANGED,1),5);
        encounterCards.add(encounterCharacterCard3);
        ItemCard testItem1 = new ItemCard("encounter", UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 4));
        encounterCards.add(testItem1);
        ItemCard testItem2 = new ItemCard("encounter", UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 4));
        encounterCards.add(testItem2);
        ItemCard testItem3 = new ItemCard("encounter", UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 4));
        encounterCards.add(testItem3);
        Encounter encounter = new Encounter("testEncounter", EncounterDifficultyLevel.EASY,encounterCards);
        testedRepository.writeEncounter(encounter,new HashMap<>());

        verify(mockDatabase).write(eq(encounter),eq(testedRepository),anyMap());
    }

    @Test
    public void readCallbackTest(){
        String testEncounterName = "testEncounter";
        EncounterService mockService = Mockito.mock(EncounterService.class);
        EncounterFirestoreDatabase mockDatabase = Mockito.mock(EncounterFirestoreDatabase.class);

        EncounterRepository testedRepository = new EncounterRepository(mockService,mockDatabase);
        List<Card> encounterCards = new ArrayList<>();

        CharacterCard encounterCharacterCard1 = new CharacterCard("encounter", UUID.randomUUID().toString(),"Dark Zach","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.MELEE,1),3);
        encounterCards.add(encounterCharacterCard1);
        CharacterCard encounterCharacterCard2 = new CharacterCard("encounter", UUID.randomUUID().toString(),"Dark Zach 2","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.MELEE,1),15);
        encounterCards.add(encounterCharacterCard2);
        CharacterCard encounterCharacterCard3 = new CharacterCard("encounter", UUID.randomUUID().toString(),"Dark Ranged Zach","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.RANGED,1),5);
        encounterCards.add(encounterCharacterCard3);
        ItemCard testItem1 = new ItemCard("encounter", UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 4));
        encounterCards.add(testItem1);
        ItemCard testItem2 = new ItemCard("encounter", UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 4));
        encounterCards.add(testItem2);
        ItemCard testItem3 = new ItemCard("encounter", UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 4));
        encounterCards.add(testItem3);
        Encounter encounter = new Encounter("testEncounter", EncounterDifficultyLevel.EASY,encounterCards);
        testedRepository.readCallback(encounter,new HashMap<>());

        verify(mockService).repositoryResponse(eq(encounter),anyMap());
    }
}
