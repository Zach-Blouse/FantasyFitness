package com.zblouse.fantasyfitness.combat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.os.CountDownTimer;
import android.util.Log;

import com.google.firebase.auth.FirebaseUser;
import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.combat.cards.Ability;
import com.zblouse.fantasyfitness.combat.cards.AbilityTarget;
import com.zblouse.fantasyfitness.combat.cards.AbilityType;
import com.zblouse.fantasyfitness.combat.cards.AttackType;
import com.zblouse.fantasyfitness.combat.cards.BuffAbility;
import com.zblouse.fantasyfitness.combat.cards.BuffType;
import com.zblouse.fantasyfitness.combat.cards.Card;
import com.zblouse.fantasyfitness.combat.cards.CardType;
import com.zblouse.fantasyfitness.combat.cards.CharacterCard;
import com.zblouse.fantasyfitness.combat.cards.DamageAbility;
import com.zblouse.fantasyfitness.combat.cards.DamageType;
import com.zblouse.fantasyfitness.combat.cards.Deck;
import com.zblouse.fantasyfitness.combat.cards.DeckService;
import com.zblouse.fantasyfitness.combat.cards.HealAbility;
import com.zblouse.fantasyfitness.combat.cards.ItemCard;
import com.zblouse.fantasyfitness.combat.cards.ItemType;
import com.zblouse.fantasyfitness.combat.encounter.Encounter;
import com.zblouse.fantasyfitness.combat.encounter.EncounterDifficultyLevel;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.internal.verification.AtLeast;
import org.mockito.verification.VerificationMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class CombatServiceTest {

    @Test
    public void initializeCombatTest(){
        String mockUserId ="testUser1";
        MainActivity mockMainActivity = Mockito.mock(MainActivity.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        when(mockMainActivity.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.getUid()).thenReturn(mockUserId);
        DeckService mockDeckService = Mockito.mock(DeckService.class);
        when(mockMainActivity.getDeckService()).thenReturn(mockDeckService);

        CombatService testedService = new CombatService(mockMainActivity);
        testedService.initializeCombat();

        verify(mockDeckService).fetchDeck(eq(mockUserId), eq("userDeck"));
        assertTrue(testedService.isInSetup());
    }

    @Test
    public void combatDeckInitialShuffleTest(){
        String mockUserId ="testUser1";
        MainActivity mockMainActivity = Mockito.mock(MainActivity.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        when(mockMainActivity.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.getUid()).thenReturn(mockUserId);
        DeckService mockDeckService = Mockito.mock(DeckService.class);
        when(mockMainActivity.getDeckService()).thenReturn(mockDeckService);

        CombatService testedService = new CombatService(mockMainActivity);
        testedService.initializeCombat();

        Deck testUserDeck = new Deck(mockUserId,"userDeck");
        List<Card> userCards = new ArrayList<>();

        for(int i=0; i<60; i++) {
            ItemCard testItem1 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
            userCards.add(testItem1);
        }
        CharacterCard characterCard1 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Zach","Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.ICE, AttackType.MELEE,1),5);
        userCards.add(characterCard1);
        testUserDeck.setCards(userCards);

        List<Card> encounterCards = new ArrayList<>();

        for(int i=0; i<60; i++) {
            ItemCard testItem1 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
            encounterCards.add(testItem1);
        }
        CharacterCard encounterCharacterCard1 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Dark Zach","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.MELEE,1),5);
        encounterCards.add(encounterCharacterCard1);

        Encounter testEncounter = new Encounter("testEncounter", EncounterDifficultyLevel.EASY, encounterCards);

        testedService.deckFetchReturned(testUserDeck);
        testedService.encounterFetchReturned(testEncounter);
        ArgumentCaptor<InitialCombatStateEvent> initialCombatStateEventArgumentCaptor = ArgumentCaptor.forClass(InitialCombatStateEvent.class);

        verify(mockMainActivity).publishEvent(initialCombatStateEventArgumentCaptor.capture());
        assertNotNull(initialCombatStateEventArgumentCaptor.getValue());

        boolean playerHasCharacterCard = false;
        for(CombatCardModel card: initialCombatStateEventArgumentCaptor.getValue().getPlayerHand()){
            if(card.getCardType().equals(CardType.CHARACTER)){
                playerHasCharacterCard= true;
                break;
            }
        }

        assertTrue("Player must have at least one character card in hand", playerHasCharacterCard);

        boolean enemyHasCharacterCard = false;
        for(CombatCardModel card: initialCombatStateEventArgumentCaptor.getValue().getEnemyHand()){
            if(card.getCardType().equals(CardType.CHARACTER)){
                enemyHasCharacterCard= true;
                break;
            }
        }

        assertTrue("Enemy must have at least one character card in hand", enemyHasCharacterCard);
    }

    @Test
    public void enemyLogicInitialPlacementTest(){
        String mockUserId ="testUser1";
        MainActivity mockMainActivity = Mockito.mock(MainActivity.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        when(mockMainActivity.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.getUid()).thenReturn(mockUserId);
        DeckService mockDeckService = Mockito.mock(DeckService.class);
        when(mockMainActivity.getDeckService()).thenReturn(mockDeckService);

        CombatService testedService = new CombatService(mockMainActivity);
        testedService.initializeCombat();

        Deck testUserDeck = new Deck(mockUserId,"userDeck");
        List<Card> userCards = new ArrayList<>();

        for(int i=0; i<6; i++) {
            ItemCard testItem1 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
            userCards.add(testItem1);
        }
        CharacterCard characterCard1 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Zach","Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.ICE, AttackType.MELEE,1),5);
        userCards.add(characterCard1);
        testUserDeck.setCards(userCards);

        List<Card> encounterCards = new ArrayList<>();

        CharacterCard encounterCharacterCard1 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Dark Zach","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.MELEE,1),5);
        encounterCards.add(encounterCharacterCard1);
        CharacterCard encounterCharacterCard2 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Dark Zach 2","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.MELEE,1),5);
        encounterCards.add(encounterCharacterCard2);
        CharacterCard encounterCharacterCard3 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Dark Ranged Zach","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.RANGED,1),5);
        encounterCards.add(encounterCharacterCard3);
        ItemCard testItem1 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
        encounterCards.add(testItem1);
        ItemCard testItem2 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
        encounterCards.add(testItem2);
        ItemCard testItem3 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
        encounterCards.add(testItem3);

        Encounter testEncounter = new Encounter("testEncounter", EncounterDifficultyLevel.EASY, encounterCards);

        testedService.deckFetchReturned(testUserDeck);
        testedService.encounterFetchReturned(testEncounter);
        ArgumentCaptor<InitialCombatStateEvent> initialCombatStateEventArgumentCaptor = ArgumentCaptor.forClass(InitialCombatStateEvent.class);
        verify(mockMainActivity).publishEvent(initialCombatStateEventArgumentCaptor.capture());
        ArgumentCaptor<CombatStateUpdateEvent> combatStateUpdateEventArgumentCaptor = ArgumentCaptor.forClass(CombatStateUpdateEvent.class);
        assertNotNull(initialCombatStateEventArgumentCaptor.getValue());
        verify(mockMainActivity,atLeastOnce()).publishEvent(combatStateUpdateEventArgumentCaptor.capture());
        assertNotNull(combatStateUpdateEventArgumentCaptor.getValue());
        CombatStateModel initialSetupModel = combatStateUpdateEventArgumentCaptor.getValue().getCombatStateModel();
        assertTrue("Dark Zach should be in front line", lineContains(initialSetupModel.getEnemyFrontLine(), encounterCharacterCard1));
        assertTrue("Dark Zach 2 should be in front line", lineContains(initialSetupModel.getEnemyFrontLine(), encounterCharacterCard2));
        assertTrue("Dark Ranger Zach should be in back line", lineContains(initialSetupModel.getEnemyBackLine(), encounterCharacterCard3));
    }

    @Test
    public void enemyLogicTargetingTest(){
        String mockUserId ="testUser1";
        MainActivity mockMainActivity = Mockito.mock(MainActivity.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        when(mockMainActivity.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.getUid()).thenReturn(mockUserId);
        DeckService mockDeckService = Mockito.mock(DeckService.class);
        when(mockMainActivity.getDeckService()).thenReturn(mockDeckService);

        CombatService testedService = new CombatService(mockMainActivity);
        testedService.initializeCombat();

        Deck testUserDeck = new Deck(mockUserId,"userDeck");
        List<Card> userCards = new ArrayList<>();

        for(int i=0; i<4; i++) {
            ItemCard testItem1 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
            userCards.add(testItem1);
        }
        CharacterCard characterCard1 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Zach","Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.ICE, AttackType.MELEE,1),20);
        userCards.add(characterCard1);
        CharacterCard characterCard2 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Zach2","Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.ICE, AttackType.MELEE,1),25);
        userCards.add(characterCard2);
        testUserDeck.setCards(userCards);

        List<Card> encounterCards = new ArrayList<>();

        CharacterCard encounterCharacterCard1 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Dark Zach","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.MELEE,1),5);
        encounterCards.add(encounterCharacterCard1);
        CharacterCard encounterCharacterCard2 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Dark Zach 2","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.MELEE,1),5);
        encounterCards.add(encounterCharacterCard2);
        CharacterCard encounterCharacterCard3 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Dark Ranged Zach","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.RANGED,1),5);
        encounterCards.add(encounterCharacterCard3);
        ItemCard testItem1 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
        encounterCards.add(testItem1);
        ItemCard testItem2 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
        encounterCards.add(testItem2);
        ItemCard testItem3 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
        encounterCards.add(testItem3);

        Encounter testEncounter = new Encounter("testEncounter", EncounterDifficultyLevel.EASY, encounterCards);

        testedService.deckFetchReturned(testUserDeck);
        testedService.encounterFetchReturned(testEncounter);
        ArgumentCaptor<CombatStateUpdateEvent> combatStateUpdateEventArgumentCaptor = ArgumentCaptor.forClass(CombatStateUpdateEvent.class);
        verify(mockMainActivity,atLeastOnce()).publishEvent(combatStateUpdateEventArgumentCaptor.capture());
        assertNotNull(combatStateUpdateEventArgumentCaptor.getValue());
        CombatCardModel zach1 = null;
        CombatCardModel zach2 = null;
        for(CombatCardModel cardModel: combatStateUpdateEventArgumentCaptor.getValue().getCombatStateModel().getPlayerHand()){
            if(cardModel.getCardName().equals(characterCard1.getCardName())){
                zach1 = cardModel;
            } else if(cardModel.getCardName().equals(characterCard2.getCardName())){
                zach2 = cardModel;
            }
        }

        testedService.cardDroppedOnLine(zach1,CombatLine.PLAYER_FRONT_LINE);
        testedService.cardDroppedOnLine(zach2,CombatLine.PLAYER_FRONT_LINE);
        if(testedService.endSetup()){
            testedService.endPlayerTurn();
        }
        verify(mockMainActivity,atLeastOnce()).publishEvent(combatStateUpdateEventArgumentCaptor.capture());
        assertNotNull(combatStateUpdateEventArgumentCaptor.getValue());
        assertEquals("Expected Zach1 to be targeted by all 3 1 hp attacks.", 17, zach1.getCurrentHealth());
        assertEquals("Expected Zach2 to not be target of attacks.", 25, zach2.getCurrentHealth());

    }

    @Test
    public void enemyLogicItemUseTest(){
        String mockUserId ="testUser1";
        MainActivity mockMainActivity = Mockito.mock(MainActivity.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        when(mockMainActivity.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.getUid()).thenReturn(mockUserId);
        DeckService mockDeckService = Mockito.mock(DeckService.class);
        when(mockMainActivity.getDeckService()).thenReturn(mockDeckService);

        CombatService testedService = new CombatService(mockMainActivity);
        testedService.initializeCombat();

        Deck testUserDeck = new Deck(mockUserId,"userDeck");
        List<Card> userCards = new ArrayList<>();

        for(int i=0; i<6; i++) {
            ItemCard testItem1 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
            userCards.add(testItem1);
        }
        CharacterCard characterCard1 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Zach","Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.ICE, AttackType.MELEE,1),5);
        userCards.add(characterCard1);
        testUserDeck.setCards(userCards);

        List<Card> encounterCards = new ArrayList<>();

        CharacterCard encounterCharacterCard1 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Dark Zach","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.MELEE,1),3);
        encounterCards.add(encounterCharacterCard1);
        CharacterCard encounterCharacterCard2 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Dark Zach 2","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.MELEE,1),15);
        encounterCards.add(encounterCharacterCard2);
        CharacterCard encounterCharacterCard3 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Dark Ranged Zach","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.RANGED,1),5);
        encounterCards.add(encounterCharacterCard3);
        ItemCard testItem1 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 4));
        encounterCards.add(testItem1);
        ItemCard testItem2 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 4));
        encounterCards.add(testItem2);
        ItemCard testItem3 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 4));
        encounterCards.add(testItem3);

        Encounter testEncounter = new Encounter("testEncounter", EncounterDifficultyLevel.EASY, encounterCards);

        testedService.deckFetchReturned(testUserDeck);
        testedService.encounterFetchReturned(testEncounter);
        ArgumentCaptor<InitialCombatStateEvent> initialCombatStateEventArgumentCaptor = ArgumentCaptor.forClass(InitialCombatStateEvent.class);
        verify(mockMainActivity).publishEvent(initialCombatStateEventArgumentCaptor.capture());
        ArgumentCaptor<CombatStateUpdateEvent> combatStateUpdateEventArgumentCaptor = ArgumentCaptor.forClass(CombatStateUpdateEvent.class);
        assertNotNull(initialCombatStateEventArgumentCaptor.getValue());
        verify(mockMainActivity,atLeastOnce()).publishEvent(combatStateUpdateEventArgumentCaptor.capture());
        assertNotNull(combatStateUpdateEventArgumentCaptor.getValue());
        CombatStateModel initialSetupModel = combatStateUpdateEventArgumentCaptor.getValue().getCombatStateModel();
        CombatCardModel darkZach = null;
        CombatCardModel darkZach2 = null;
        CombatCardModel darkRangedZach = null;
        List<CombatCardModel> allEnemyCards = new ArrayList<>();
        allEnemyCards.addAll(combatStateUpdateEventArgumentCaptor.getValue().getCombatStateModel().getEnemyFrontLine());
        allEnemyCards.addAll(combatStateUpdateEventArgumentCaptor.getValue().getCombatStateModel().getEnemyBackLine());
        for(CombatCardModel cardModel: allEnemyCards){
            if(cardModel.getCardName().equals("Dark Zach")){
                darkZach = cardModel;
            } else if(cardModel.getCardName().equals("Dark Zach 2")){
                darkZach2 = cardModel;
            }else if(cardModel.getCardName().equals("Dark Ranged Zach")){
                darkRangedZach = cardModel;
            }

        }

        assertEquals("Dark Zach given 2 buff items", 11, darkZach.getCurrentHealth());
        assertEquals("Dark Zach 2 given no hp buff item", 15, darkZach2.getCurrentHealth());
        assertEquals("Dark Ranged Zach given 1 buff item", 9, darkRangedZach.getCurrentHealth());
    }

    @Test
    public void abilityUsedSelfHealTest(){
        String mockUserId ="testUser1";
        MainActivity mockMainActivity = Mockito.mock(MainActivity.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        when(mockMainActivity.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.getUid()).thenReturn(mockUserId);
        DeckService mockDeckService = Mockito.mock(DeckService.class);
        when(mockMainActivity.getDeckService()).thenReturn(mockDeckService);

        CombatService testedService = new CombatService(mockMainActivity);
        testedService.initializeCombat();

        Deck testUserDeck = new Deck(mockUserId,"userDeck");
        List<Card> userCards = new ArrayList<>();

        for(int i=0; i<4; i++) {
            ItemCard testItem1 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
            userCards.add(testItem1);
        }
        CharacterCard characterCard1 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Zach","Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.ICE, AttackType.MELEE,1),30);
        userCards.add(characterCard1);
        CharacterCard characterCard2 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Zach2","Zach is a software engineer",new HealAbility("SelfHeal","slap", AbilityTarget.SELF,2),25);
        userCards.add(characterCard2);
        testUserDeck.setCards(userCards);

        List<Card> encounterCards = new ArrayList<>();

        CharacterCard encounterCharacterCard1 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Dark Zach","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.MELEE,1),5);
        encounterCards.add(encounterCharacterCard1);
        CharacterCard encounterCharacterCard2 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Dark Zach 2","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.MELEE,1),5);
        encounterCards.add(encounterCharacterCard2);
        CharacterCard encounterCharacterCard3 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Dark Ranged Zach","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.RANGED,1),5);
        encounterCards.add(encounterCharacterCard3);
        ItemCard testItem1 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
        encounterCards.add(testItem1);
        ItemCard testItem2 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
        encounterCards.add(testItem2);
        ItemCard testItem3 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
        encounterCards.add(testItem3);

        Encounter testEncounter = new Encounter("testEncounter", EncounterDifficultyLevel.EASY, encounterCards);

        testedService.deckFetchReturned(testUserDeck);
        testedService.encounterFetchReturned(testEncounter);
        ArgumentCaptor<CombatStateUpdateEvent> combatStateUpdateEventArgumentCaptor = ArgumentCaptor.forClass(CombatStateUpdateEvent.class);
        verify(mockMainActivity,atLeastOnce()).publishEvent(combatStateUpdateEventArgumentCaptor.capture());
        assertNotNull(combatStateUpdateEventArgumentCaptor.getValue());
        CombatCardModel zach1 = null;
        CombatCardModel zach2 = null;
        for(CombatCardModel cardModel: combatStateUpdateEventArgumentCaptor.getValue().getCombatStateModel().getPlayerHand()){
            if(cardModel.getCardName().equals(characterCard1.getCardName())){
                zach1 = cardModel;
            } else if(cardModel.getCardName().equals(characterCard2.getCardName())){
                zach2 = cardModel;
            }
        }

        testedService.cardDroppedOnLine(zach1,CombatLine.PLAYER_FRONT_LINE);
        testedService.cardDroppedOnLine(zach2,CombatLine.PLAYER_FRONT_LINE);
        if(testedService.endSetup()){
            testedService.endPlayerTurn();
        }
        verify(mockMainActivity,atLeastOnce()).publishEvent(combatStateUpdateEventArgumentCaptor.capture());
        assertNotNull(combatStateUpdateEventArgumentCaptor.getValue());
        assertEquals("Expected Zach1 to not be targeted", 30, zach1.getCurrentHealth());
        assertEquals("Expected Zach2 to be targeted by all 3 1 hp attacks.", 22, zach2.getCurrentHealth());
        Ability zach2HealAbility = null;
        for(Ability ability: zach2.getAbilities()){
            if(ability.getAbilityType().equals(AbilityType.HEAL)){
                zach2HealAbility = ability;
            }
        }
        testedService.abilityUsed(zach2,zach2HealAbility);
        verify(mockMainActivity,atLeastOnce()).publishEvent(combatStateUpdateEventArgumentCaptor.capture());
        assertNotNull(combatStateUpdateEventArgumentCaptor.getValue());
        assertEquals("Expected Zach1 to not be targeted", 30, zach1.getCurrentHealth());
        assertEquals("Expected Zach2 to be healed by heal amount", 24, zach2.getCurrentHealth());
    }

    @Test
    public void abilityUsedAllyHealTest(){
        String mockUserId ="testUser1";
        MainActivity mockMainActivity = Mockito.mock(MainActivity.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        when(mockMainActivity.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.getUid()).thenReturn(mockUserId);
        DeckService mockDeckService = Mockito.mock(DeckService.class);
        when(mockMainActivity.getDeckService()).thenReturn(mockDeckService);

        CombatService testedService = new CombatService(mockMainActivity);
        testedService.initializeCombat();

        Deck testUserDeck = new Deck(mockUserId,"userDeck");
        List<Card> userCards = new ArrayList<>();

        for(int i=0; i<4; i++) {
            ItemCard testItem1 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
            userCards.add(testItem1);
        }
        CharacterCard characterCard1 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Zach","Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.ICE, AttackType.MELEE,1),20);
        userCards.add(characterCard1);
        CharacterCard characterCard2 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Zach2","Zach is a software engineer",new HealAbility("AllyHeal","slap", AbilityTarget.SINGLE_ALLY,2),25);
        userCards.add(characterCard2);
        testUserDeck.setCards(userCards);

        List<Card> encounterCards = new ArrayList<>();

        CharacterCard encounterCharacterCard1 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Dark Zach","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.MELEE,1),5);
        encounterCards.add(encounterCharacterCard1);
        CharacterCard encounterCharacterCard2 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Dark Zach 2","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.MELEE,1),5);
        encounterCards.add(encounterCharacterCard2);
        CharacterCard encounterCharacterCard3 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Dark Ranged Zach","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.RANGED,1),5);
        encounterCards.add(encounterCharacterCard3);
        ItemCard testItem1 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
        encounterCards.add(testItem1);
        ItemCard testItem2 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
        encounterCards.add(testItem2);
        ItemCard testItem3 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
        encounterCards.add(testItem3);

        Encounter testEncounter = new Encounter("testEncounter", EncounterDifficultyLevel.EASY, encounterCards);

        testedService.deckFetchReturned(testUserDeck);
        testedService.encounterFetchReturned(testEncounter);
        ArgumentCaptor<CombatStateUpdateEvent> combatStateUpdateEventArgumentCaptor = ArgumentCaptor.forClass(CombatStateUpdateEvent.class);
        verify(mockMainActivity,atLeastOnce()).publishEvent(combatStateUpdateEventArgumentCaptor.capture());
        assertNotNull(combatStateUpdateEventArgumentCaptor.getValue());
        CombatCardModel zach1 = null;
        CombatCardModel zach2 = null;
        for(CombatCardModel cardModel: combatStateUpdateEventArgumentCaptor.getValue().getCombatStateModel().getPlayerHand()){
            if(cardModel.getCardName().equals(characterCard1.getCardName())){
                zach1 = cardModel;
            } else if(cardModel.getCardName().equals(characterCard2.getCardName())){
                zach2 = cardModel;
            }
        }

        testedService.cardDroppedOnLine(zach1,CombatLine.PLAYER_FRONT_LINE);
        testedService.cardDroppedOnLine(zach2,CombatLine.PLAYER_FRONT_LINE);
        if(testedService.endSetup()){
            testedService.endPlayerTurn();
        }
        verify(mockMainActivity,atLeastOnce()).publishEvent(combatStateUpdateEventArgumentCaptor.capture());
        assertNotNull(combatStateUpdateEventArgumentCaptor.getValue());
        assertEquals("Expected Zach1 to be targeted by all 3 1 hp attacks", 17, zach1.getCurrentHealth());
        assertEquals("Expected Zach2 to not be targeted.", 25, zach2.getCurrentHealth());
        Ability zach2HealAbility = null;
        for(Ability ability: zach2.getAbilities()){
            if(ability.getAbilityType().equals(AbilityType.HEAL)){
                zach2HealAbility = ability;
            }
        }
        testedService.abilityUsed(zach2,zach2HealAbility);
        testedService.attemptCardAbilityTarget(zach1);
        verify(mockMainActivity,atLeastOnce()).publishEvent(combatStateUpdateEventArgumentCaptor.capture());
        assertNotNull(combatStateUpdateEventArgumentCaptor.getValue());
        assertEquals("Expected Zach1 to be healed by heal amount", 19, zach1.getCurrentHealth());
        assertEquals("Expected Zach2 to not be affected", 25, zach2.getCurrentHealth());
    }

    @Test
    public void abilityUsedRowAllyHealTest(){
        String mockUserId ="testUser1";
        MainActivity mockMainActivity = Mockito.mock(MainActivity.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        when(mockMainActivity.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.getUid()).thenReturn(mockUserId);
        DeckService mockDeckService = Mockito.mock(DeckService.class);
        when(mockMainActivity.getDeckService()).thenReturn(mockDeckService);

        CombatService testedService = new CombatService(mockMainActivity);
        testedService.initializeCombat();

        Deck testUserDeck = new Deck(mockUserId,"userDeck");
        List<Card> userCards = new ArrayList<>();

        for(int i=0; i<4; i++) {
            ItemCard testItem1 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
            userCards.add(testItem1);
        }
        CharacterCard characterCard1 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Zach","Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.ICE, AttackType.MELEE,1),20);
        userCards.add(characterCard1);
        CharacterCard characterCard2 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Zach2","Zach is a software engineer",new HealAbility("AllyHeal","slap", AbilityTarget.ROW_ALLY,2),25);
        userCards.add(characterCard2);
        testUserDeck.setCards(userCards);

        List<Card> encounterCards = new ArrayList<>();

        CharacterCard encounterCharacterCard1 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Dark Zach","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.MELEE,1),5);
        encounterCards.add(encounterCharacterCard1);
        CharacterCard encounterCharacterCard2 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Dark Zach 2","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.MELEE,1),5);
        encounterCards.add(encounterCharacterCard2);
        CharacterCard encounterCharacterCard3 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Dark Ranged Zach","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.RANGED,1),5);
        encounterCards.add(encounterCharacterCard3);
        ItemCard testItem1 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
        encounterCards.add(testItem1);
        ItemCard testItem2 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
        encounterCards.add(testItem2);
        ItemCard testItem3 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
        encounterCards.add(testItem3);

        Encounter testEncounter = new Encounter("testEncounter", EncounterDifficultyLevel.EASY, encounterCards);

        testedService.deckFetchReturned(testUserDeck);
        testedService.encounterFetchReturned(testEncounter);
        ArgumentCaptor<CombatStateUpdateEvent> combatStateUpdateEventArgumentCaptor = ArgumentCaptor.forClass(CombatStateUpdateEvent.class);
        verify(mockMainActivity,atLeastOnce()).publishEvent(combatStateUpdateEventArgumentCaptor.capture());
        assertNotNull(combatStateUpdateEventArgumentCaptor.getValue());
        CombatCardModel zach1 = null;
        CombatCardModel zach2 = null;
        for(CombatCardModel cardModel: combatStateUpdateEventArgumentCaptor.getValue().getCombatStateModel().getPlayerHand()){
            if(cardModel.getCardName().equals(characterCard1.getCardName())){
                zach1 = cardModel;
            } else if(cardModel.getCardName().equals(characterCard2.getCardName())){
                zach2 = cardModel;
            }
        }

        testedService.cardDroppedOnLine(zach1,CombatLine.PLAYER_FRONT_LINE);
        testedService.cardDroppedOnLine(zach2,CombatLine.PLAYER_FRONT_LINE);
        if(testedService.endSetup()){
            testedService.endPlayerTurn();
        }
        verify(mockMainActivity,atLeastOnce()).publishEvent(combatStateUpdateEventArgumentCaptor.capture());
        assertNotNull(combatStateUpdateEventArgumentCaptor.getValue());
        assertEquals("Expected Zach1 to be targeted by all 3 1 hp attacks", 17, zach1.getCurrentHealth());
        assertEquals("Expected Zach2 to not be targeted.", 25, zach2.getCurrentHealth());
        zach2.setCurrentHealth(22);
        Ability zach2HealAbility = null;
        for(Ability ability: zach2.getAbilities()){
            if(ability.getAbilityType().equals(AbilityType.HEAL)){
                zach2HealAbility = ability;
            }
        }
        testedService.abilityUsed(zach2,zach2HealAbility);
        testedService.attemptCardAbilityTarget(zach1);
        verify(mockMainActivity,atLeastOnce()).publishEvent(combatStateUpdateEventArgumentCaptor.capture());
        assertNotNull(combatStateUpdateEventArgumentCaptor.getValue());
        assertEquals("Expected Zach1 to be healed by heal amount", 19, zach1.getCurrentHealth());
        assertEquals("Expected Zach2 to be healed by heal amount", 24, zach2.getCurrentHealth());
    }


    @Test
    public void abilityUsedRowAllyHealBacklineTest(){
        String mockUserId ="testUser1";
        MainActivity mockMainActivity = Mockito.mock(MainActivity.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        when(mockMainActivity.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.getUid()).thenReturn(mockUserId);
        DeckService mockDeckService = Mockito.mock(DeckService.class);
        when(mockMainActivity.getDeckService()).thenReturn(mockDeckService);

        CombatService testedService = new CombatService(mockMainActivity);
        testedService.initializeCombat();

        Deck testUserDeck = new Deck(mockUserId,"userDeck");
        List<Card> userCards = new ArrayList<>();

        for(int i=0; i<4; i++) {
            ItemCard testItem1 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
            userCards.add(testItem1);
        }
        CharacterCard characterCard1 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Zach","Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.ICE, AttackType.MELEE,1),20);
        userCards.add(characterCard1);
        CharacterCard characterCard2 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Zach2","Zach is a software engineer",new HealAbility("AllyHeal","slap", AbilityTarget.ROW_ALLY,2),25);
        userCards.add(characterCard2);
        testUserDeck.setCards(userCards);

        List<Card> encounterCards = new ArrayList<>();

        CharacterCard encounterCharacterCard1 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Dark Zach","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.MELEE,1),5);
        encounterCards.add(encounterCharacterCard1);
        CharacterCard encounterCharacterCard2 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Dark Zach 2","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.MELEE,1),5);
        encounterCards.add(encounterCharacterCard2);
        CharacterCard encounterCharacterCard3 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Dark Ranged Zach","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.RANGED,1),5);
        encounterCards.add(encounterCharacterCard3);
        ItemCard testItem1 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
        encounterCards.add(testItem1);
        ItemCard testItem2 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
        encounterCards.add(testItem2);
        ItemCard testItem3 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
        encounterCards.add(testItem3);

        Encounter testEncounter = new Encounter("testEncounter", EncounterDifficultyLevel.EASY, encounterCards);

        testedService.deckFetchReturned(testUserDeck);
        testedService.encounterFetchReturned(testEncounter);
        ArgumentCaptor<CombatStateUpdateEvent> combatStateUpdateEventArgumentCaptor = ArgumentCaptor.forClass(CombatStateUpdateEvent.class);
        verify(mockMainActivity,atLeastOnce()).publishEvent(combatStateUpdateEventArgumentCaptor.capture());
        assertNotNull(combatStateUpdateEventArgumentCaptor.getValue());
        CombatCardModel zach1 = null;
        CombatCardModel zach2 = null;
        for(CombatCardModel cardModel: combatStateUpdateEventArgumentCaptor.getValue().getCombatStateModel().getPlayerHand()){
            if(cardModel.getCardName().equals(characterCard1.getCardName())){
                zach1 = cardModel;
            } else if(cardModel.getCardName().equals(characterCard2.getCardName())){
                zach2 = cardModel;
            }
        }

        testedService.cardDroppedOnLine(zach1,CombatLine.PLAYER_BACK_LINE);
        testedService.cardDroppedOnLine(zach2,CombatLine.PLAYER_BACK_LINE);
        if(testedService.endSetup()){
            testedService.endPlayerTurn();
        }
        verify(mockMainActivity,atLeastOnce()).publishEvent(combatStateUpdateEventArgumentCaptor.capture());
        assertNotNull(combatStateUpdateEventArgumentCaptor.getValue());
        assertEquals("Expected Zach1 to be targeted by all 3 1 hp attacks", 17, zach1.getCurrentHealth());
        assertEquals("Expected Zach2 to not be targeted.", 25, zach2.getCurrentHealth());
        zach2.setCurrentHealth(22);
        Ability zach2HealAbility = null;
        for(Ability ability: zach2.getAbilities()){
            if(ability.getAbilityType().equals(AbilityType.HEAL)){
                zach2HealAbility = ability;
            }
        }
        testedService.abilityUsed(zach2,zach2HealAbility);
        testedService.attemptCardAbilityTarget(zach1);
        verify(mockMainActivity,atLeastOnce()).publishEvent(combatStateUpdateEventArgumentCaptor.capture());
        assertNotNull(combatStateUpdateEventArgumentCaptor.getValue());
        assertEquals("Expected Zach1 to be healed by heal amount", 19, zach1.getCurrentHealth());
        assertEquals("Expected Zach2 to be healed by heal amount", 24, zach2.getCurrentHealth());
    }

    @Test
    public void abilityUsedDamageTest(){
        String mockUserId ="testUser1";
        MainActivity mockMainActivity = Mockito.mock(MainActivity.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        when(mockMainActivity.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.getUid()).thenReturn(mockUserId);
        DeckService mockDeckService = Mockito.mock(DeckService.class);
        when(mockMainActivity.getDeckService()).thenReturn(mockDeckService);

        CombatService testedService = new CombatService(mockMainActivity);
        testedService.initializeCombat();

        Deck testUserDeck = new Deck(mockUserId,"userDeck");
        List<Card> userCards = new ArrayList<>();

        for(int i=0; i<4; i++) {
            ItemCard testItem1 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
            userCards.add(testItem1);
        }
        CharacterCard characterCard1 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Zach","Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.ICE, AttackType.MELEE,1),30);
        userCards.add(characterCard1);
        CharacterCard characterCard2 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Zach2","Zach is a software engineer",new HealAbility("SelfHeal","slap", AbilityTarget.SELF,2),25);
        userCards.add(characterCard2);
        testUserDeck.setCards(userCards);

        List<Card> encounterCards = new ArrayList<>();

        CharacterCard encounterCharacterCard1 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Dark Zach","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.MELEE,1),5);
        encounterCards.add(encounterCharacterCard1);
        CharacterCard encounterCharacterCard2 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Dark Zach 2","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.MELEE,1),5);
        encounterCards.add(encounterCharacterCard2);
        CharacterCard encounterCharacterCard3 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Dark Ranged Zach","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.RANGED,1),5);
        encounterCards.add(encounterCharacterCard3);
        ItemCard testItem1 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
        encounterCards.add(testItem1);
        ItemCard testItem2 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
        encounterCards.add(testItem2);
        ItemCard testItem3 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
        encounterCards.add(testItem3);

        Encounter testEncounter = new Encounter("testEncounter", EncounterDifficultyLevel.EASY, encounterCards);

        testedService.deckFetchReturned(testUserDeck);
        testedService.encounterFetchReturned(testEncounter);
        ArgumentCaptor<CombatStateUpdateEvent> combatStateUpdateEventArgumentCaptor = ArgumentCaptor.forClass(CombatStateUpdateEvent.class);
        verify(mockMainActivity,atLeastOnce()).publishEvent(combatStateUpdateEventArgumentCaptor.capture());
        assertNotNull(combatStateUpdateEventArgumentCaptor.getValue());
        CombatCardModel zach1 = null;
        CombatCardModel zach2 = null;
        for(CombatCardModel cardModel: combatStateUpdateEventArgumentCaptor.getValue().getCombatStateModel().getPlayerHand()){
            if(cardModel.getCardName().equals(characterCard1.getCardName())){
                zach1 = cardModel;
            } else if(cardModel.getCardName().equals(characterCard2.getCardName())){
                zach2 = cardModel;
            }
        }

        CombatCardModel darkZach = null;
        for(CombatCardModel combatCardModel: combatStateUpdateEventArgumentCaptor.getValue().getCombatStateModel().getEnemyFrontLine()){
            if(combatCardModel.getCardName().equals("Dark Zach")){
                darkZach = combatCardModel;
            }
        }

        testedService.cardDroppedOnLine(zach1,CombatLine.PLAYER_FRONT_LINE);
        testedService.cardDroppedOnLine(zach2,CombatLine.PLAYER_FRONT_LINE);
        if(testedService.endSetup()){
            testedService.endPlayerTurn();
        }
        verify(mockMainActivity,atLeastOnce()).publishEvent(combatStateUpdateEventArgumentCaptor.capture());
        assertNotNull(combatStateUpdateEventArgumentCaptor.getValue());
        assertEquals("Expected Zach1 to not be targeted", 30, zach1.getCurrentHealth());
        assertEquals("Expected Zach2 to be targeted by all 3 1 hp attacks.", 22, zach2.getCurrentHealth());
        Ability zach1AttackAbility = null;
        for(Ability ability: zach1.getAbilities()){
            if(ability.getAbilityType().equals(AbilityType.DAMAGE)){
                zach1AttackAbility = ability;
            }
        }
        testedService.abilityUsed(zach1,zach1AttackAbility);
        testedService.attemptCardAbilityTarget(darkZach);
        verify(mockMainActivity,atLeastOnce()).publishEvent(combatStateUpdateEventArgumentCaptor.capture());
        assertNotNull(combatStateUpdateEventArgumentCaptor.getValue());
        assertEquals("Expected Dark Zach to be damaged", 7, darkZach.getCurrentHealth());

    }


    @Test
    public void abilityUsedDamageKillTest(){
        String mockUserId ="testUser1";
        MainActivity mockMainActivity = Mockito.mock(MainActivity.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        when(mockMainActivity.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.getUid()).thenReturn(mockUserId);
        DeckService mockDeckService = Mockito.mock(DeckService.class);
        when(mockMainActivity.getDeckService()).thenReturn(mockDeckService);

        CombatService testedService = new CombatService(mockMainActivity);
        testedService.initializeCombat();

        Deck testUserDeck = new Deck(mockUserId,"userDeck");
        List<Card> userCards = new ArrayList<>();

        for(int i=0; i<4; i++) {
            ItemCard testItem1 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
            userCards.add(testItem1);
        }
        CharacterCard characterCard1 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Zach","Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.ICE, AttackType.MELEE,1),30);
        userCards.add(characterCard1);
        CharacterCard characterCard2 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Zach2","Zach is a software engineer",new HealAbility("SelfHeal","slap", AbilityTarget.SELF,2),25);
        userCards.add(characterCard2);
        testUserDeck.setCards(userCards);

        List<Card> encounterCards = new ArrayList<>();

        CharacterCard encounterCharacterCard1 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Dark Zach","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.MELEE,1),1);
        encounterCards.add(encounterCharacterCard1);
        CharacterCard encounterCharacterCard2 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Dark Zach 2","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.MELEE,1),5);
        encounterCards.add(encounterCharacterCard2);
        CharacterCard encounterCharacterCard3 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Dark Ranged Zach","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.RANGED,1),5);
        encounterCards.add(encounterCharacterCard3);
        ItemCard testItem1 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.ATTACK, 3));
        encounterCards.add(testItem1);
        ItemCard testItem2 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.ATTACK, 3));
        encounterCards.add(testItem2);
        ItemCard testItem3 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.ATTACK, 3));
        encounterCards.add(testItem3);

        Encounter testEncounter = new Encounter("testEncounter", EncounterDifficultyLevel.EASY, encounterCards);

        testedService.deckFetchReturned(testUserDeck);
        testedService.encounterFetchReturned(testEncounter);
        ArgumentCaptor<CombatStateUpdateEvent> combatStateUpdateEventArgumentCaptor = ArgumentCaptor.forClass(CombatStateUpdateEvent.class);
        verify(mockMainActivity,atLeastOnce()).publishEvent(combatStateUpdateEventArgumentCaptor.capture());
        assertNotNull(combatStateUpdateEventArgumentCaptor.getValue());
        CombatCardModel zach1 = null;
        CombatCardModel zach2 = null;
        for(CombatCardModel cardModel: combatStateUpdateEventArgumentCaptor.getValue().getCombatStateModel().getPlayerHand()){
            if(cardModel.getCardName().equals(characterCard1.getCardName())){
                zach1 = cardModel;
            } else if(cardModel.getCardName().equals(characterCard2.getCardName())){
                zach2 = cardModel;
            }
        }

        CombatCardModel darkZach = null;
        for(CombatCardModel combatCardModel: combatStateUpdateEventArgumentCaptor.getValue().getCombatStateModel().getEnemyFrontLine()){
            if(combatCardModel.getCardName().equals("Dark Zach")){
                darkZach = combatCardModel;
            }
        }

        testedService.cardDroppedOnLine(zach1,CombatLine.PLAYER_FRONT_LINE);
        testedService.cardDroppedOnLine(zach2,CombatLine.PLAYER_FRONT_LINE);
        if(testedService.endSetup()){
            testedService.endPlayerTurn();
        }
        verify(mockMainActivity,atLeastOnce()).publishEvent(combatStateUpdateEventArgumentCaptor.capture());
        assertNotNull(combatStateUpdateEventArgumentCaptor.getValue());
        assertEquals("Expected Zach1 to not be targeted", 30, zach1.getCurrentHealth());
        assertEquals("Expected Zach2 to be targeted by all 3 4 hp attacks.", 13, zach2.getCurrentHealth());
        Ability zach1AttackAbility = null;
        for(Ability ability: zach1.getAbilities()){
            if(ability.getAbilityType().equals(AbilityType.DAMAGE)){
                zach1AttackAbility = ability;
            }
        }
        testedService.abilityUsed(zach1,zach1AttackAbility);
        testedService.attemptCardAbilityTarget(darkZach);
        verify(mockMainActivity,atLeastOnce()).publishEvent(combatStateUpdateEventArgumentCaptor.capture());
        assertNotNull(combatStateUpdateEventArgumentCaptor.getValue());
        assertEquals("Expected Dark Zach to be damaged", 0, darkZach.getCurrentHealth());
        assertFalse(combatStateUpdateEventArgumentCaptor.getValue().getCombatStateModel().getEnemyFrontLine().contains(darkZach));

    }

    @Test
    public void abilityUsedRowDamageFrontLineTest(){
        String mockUserId ="testUser1";
        MainActivity mockMainActivity = Mockito.mock(MainActivity.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        when(mockMainActivity.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.getUid()).thenReturn(mockUserId);
        DeckService mockDeckService = Mockito.mock(DeckService.class);
        when(mockMainActivity.getDeckService()).thenReturn(mockDeckService);

        CombatService testedService = new CombatService(mockMainActivity);
        testedService.initializeCombat();

        Deck testUserDeck = new Deck(mockUserId,"userDeck");
        List<Card> userCards = new ArrayList<>();

        for(int i=0; i<4; i++) {
            ItemCard testItem1 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
            userCards.add(testItem1);
        }
        CharacterCard characterCard1 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Zach","Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.ROW_ENEMY, DamageType.ICE, AttackType.MELEE,1),30);
        userCards.add(characterCard1);
        CharacterCard characterCard2 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Zach2","Zach is a software engineer",new HealAbility("SelfHeal","slap", AbilityTarget.SELF,2),25);
        userCards.add(characterCard2);
        testUserDeck.setCards(userCards);

        List<Card> encounterCards = new ArrayList<>();

        CharacterCard encounterCharacterCard1 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Dark Zach","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.MELEE,1),5);
        encounterCards.add(encounterCharacterCard1);
        CharacterCard encounterCharacterCard2 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Dark Zach 2","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.MELEE,1),5);
        encounterCards.add(encounterCharacterCard2);
        CharacterCard encounterCharacterCard3 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Dark Ranged Zach","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.RANGED,1),5);
        encounterCards.add(encounterCharacterCard3);
        ItemCard testItem1 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
        encounterCards.add(testItem1);
        ItemCard testItem2 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
        encounterCards.add(testItem2);
        ItemCard testItem3 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
        encounterCards.add(testItem3);

        Encounter testEncounter = new Encounter("testEncounter", EncounterDifficultyLevel.EASY, encounterCards);

        testedService.deckFetchReturned(testUserDeck);
        testedService.encounterFetchReturned(testEncounter);
        ArgumentCaptor<CombatStateUpdateEvent> combatStateUpdateEventArgumentCaptor = ArgumentCaptor.forClass(CombatStateUpdateEvent.class);
        verify(mockMainActivity,atLeastOnce()).publishEvent(combatStateUpdateEventArgumentCaptor.capture());
        assertNotNull(combatStateUpdateEventArgumentCaptor.getValue());
        CombatCardModel zach1 = null;
        CombatCardModel zach2 = null;
        for(CombatCardModel cardModel: combatStateUpdateEventArgumentCaptor.getValue().getCombatStateModel().getPlayerHand()){
            if(cardModel.getCardName().equals(characterCard1.getCardName())){
                zach1 = cardModel;
            } else if(cardModel.getCardName().equals(characterCard2.getCardName())){
                zach2 = cardModel;
            }
        }

        CombatCardModel darkZach = null;
        CombatCardModel darkZach2 = null;
        CombatCardModel darkRangedZach = null;
        List<CombatCardModel> allEnemyCards = new ArrayList<>();
        allEnemyCards.addAll(combatStateUpdateEventArgumentCaptor.getValue().getCombatStateModel().getEnemyFrontLine());
        allEnemyCards.addAll(combatStateUpdateEventArgumentCaptor.getValue().getCombatStateModel().getEnemyBackLine());
        for(CombatCardModel combatCardModel: allEnemyCards){
            if(combatCardModel.getCardName().equals("Dark Zach")){
                darkZach = combatCardModel;
            } else if(combatCardModel.getCardName().equals("Dark Zach 2")){
                darkZach2 = combatCardModel;
            } else if(combatCardModel.getCardName().equals("Dark Ranged Zach")){
                darkRangedZach = combatCardModel;
            }
        }

        testedService.cardDroppedOnLine(zach1,CombatLine.PLAYER_FRONT_LINE);
        testedService.cardDroppedOnLine(zach2,CombatLine.PLAYER_FRONT_LINE);
        if(testedService.endSetup()){
            testedService.endPlayerTurn();
        }
        verify(mockMainActivity,atLeastOnce()).publishEvent(combatStateUpdateEventArgumentCaptor.capture());
        assertNotNull(combatStateUpdateEventArgumentCaptor.getValue());
        assertEquals("Expected Zach1 to not be targeted", 30, zach1.getCurrentHealth());
        assertEquals("Expected Zach2 to be targeted by all 3 1 hp attacks.", 22, zach2.getCurrentHealth());
        Ability zach1AttackAbility = null;
        for(Ability ability: zach1.getAbilities()){
            if(ability.getAbilityType().equals(AbilityType.DAMAGE)){
                zach1AttackAbility = ability;
            }
        }
        testedService.abilityUsed(zach1,zach1AttackAbility);
        testedService.attemptCardAbilityTarget(darkZach);
        verify(mockMainActivity,atLeastOnce()).publishEvent(combatStateUpdateEventArgumentCaptor.capture());
        assertNotNull(combatStateUpdateEventArgumentCaptor.getValue());
        assertEquals("Expected Dark Zach to be damaged", 7, darkZach.getCurrentHealth());
        assertEquals("Expected Dark Zach 2 to be damaged", 7, darkZach2.getCurrentHealth());
        assertEquals("Expected Dark Ranged Zach to not be damaged", 8, darkRangedZach.getCurrentHealth());

    }

    @Test
    public void abilityUsedRowDamageBackLineTest(){
        String mockUserId ="testUser1";
        MainActivity mockMainActivity = Mockito.mock(MainActivity.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        when(mockMainActivity.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.getUid()).thenReturn(mockUserId);
        DeckService mockDeckService = Mockito.mock(DeckService.class);
        when(mockMainActivity.getDeckService()).thenReturn(mockDeckService);

        CombatService testedService = new CombatService(mockMainActivity);
        testedService.initializeCombat();

        Deck testUserDeck = new Deck(mockUserId,"userDeck");
        List<Card> userCards = new ArrayList<>();

        for(int i=0; i<4; i++) {
            ItemCard testItem1 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
            userCards.add(testItem1);
        }
        CharacterCard characterCard1 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Zach","Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.ROW_ENEMY, DamageType.ICE, AttackType.MELEE,1),30);
        userCards.add(characterCard1);
        CharacterCard characterCard2 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Zach2","Zach is a software engineer",new HealAbility("SelfHeal","slap", AbilityTarget.SELF,2),25);
        userCards.add(characterCard2);
        testUserDeck.setCards(userCards);

        List<Card> encounterCards = new ArrayList<>();

        CharacterCard encounterCharacterCard1 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Dark Zach","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.MELEE,1),5);
        encounterCards.add(encounterCharacterCard1);
        CharacterCard encounterCharacterCard2 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Dark Zach 2","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.MELEE,1),5);
        encounterCards.add(encounterCharacterCard2);
        CharacterCard encounterCharacterCard3 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Dark Ranged Zach","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.RANGED,1),5);
        encounterCards.add(encounterCharacterCard3);
        ItemCard testItem1 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
        encounterCards.add(testItem1);
        ItemCard testItem2 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
        encounterCards.add(testItem2);
        ItemCard testItem3 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
        encounterCards.add(testItem3);

        Encounter testEncounter = new Encounter("testEncounter", EncounterDifficultyLevel.EASY, encounterCards);

        testedService.deckFetchReturned(testUserDeck);
        testedService.encounterFetchReturned(testEncounter);
        ArgumentCaptor<CombatStateUpdateEvent> combatStateUpdateEventArgumentCaptor = ArgumentCaptor.forClass(CombatStateUpdateEvent.class);
        verify(mockMainActivity,atLeastOnce()).publishEvent(combatStateUpdateEventArgumentCaptor.capture());
        assertNotNull(combatStateUpdateEventArgumentCaptor.getValue());
        CombatCardModel zach1 = null;
        CombatCardModel zach2 = null;
        for(CombatCardModel cardModel: combatStateUpdateEventArgumentCaptor.getValue().getCombatStateModel().getPlayerHand()){
            if(cardModel.getCardName().equals(characterCard1.getCardName())){
                zach1 = cardModel;
            } else if(cardModel.getCardName().equals(characterCard2.getCardName())){
                zach2 = cardModel;
            }
        }

        CombatCardModel darkZach = null;
        CombatCardModel darkZach2 = null;
        CombatCardModel darkRangedZach = null;
        List<CombatCardModel> allEnemyCards = new ArrayList<>();
        allEnemyCards.addAll(combatStateUpdateEventArgumentCaptor.getValue().getCombatStateModel().getEnemyFrontLine());
        allEnemyCards.addAll(combatStateUpdateEventArgumentCaptor.getValue().getCombatStateModel().getEnemyBackLine());
        for(CombatCardModel combatCardModel: allEnemyCards){
            if(combatCardModel.getCardName().equals("Dark Zach")){
                darkZach = combatCardModel;
            } else if(combatCardModel.getCardName().equals("Dark Zach 2")){
                darkZach2 = combatCardModel;
            } else if(combatCardModel.getCardName().equals("Dark Ranged Zach")){
                darkRangedZach = combatCardModel;
            }
        }

        testedService.cardDroppedOnLine(zach1,CombatLine.PLAYER_FRONT_LINE);
        testedService.cardDroppedOnLine(zach2,CombatLine.PLAYER_FRONT_LINE);
        if(testedService.endSetup()){
            testedService.endPlayerTurn();
        }
        verify(mockMainActivity,atLeastOnce()).publishEvent(combatStateUpdateEventArgumentCaptor.capture());
        assertNotNull(combatStateUpdateEventArgumentCaptor.getValue());
        assertEquals("Expected Zach1 to not be targeted", 30, zach1.getCurrentHealth());
        assertEquals("Expected Zach2 to be targeted by all 3 1 hp attacks.", 22, zach2.getCurrentHealth());
        Ability zach1AttackAbility = null;
        for(Ability ability: zach1.getAbilities()){
            if(ability.getAbilityType().equals(AbilityType.DAMAGE)){
                zach1AttackAbility = ability;
            }
        }
        testedService.abilityUsed(zach1,zach1AttackAbility);
        testedService.attemptCardAbilityTarget(darkRangedZach);
        verify(mockMainActivity,atLeastOnce()).publishEvent(combatStateUpdateEventArgumentCaptor.capture());
        assertNotNull(combatStateUpdateEventArgumentCaptor.getValue());
        assertEquals("Expected Dark Zach to not be damaged", 8, darkZach.getCurrentHealth());
        assertEquals("Expected Dark Zach 2 to not be damaged", 8, darkZach2.getCurrentHealth());
        assertEquals("Expected Dark Ranged Zach to be damaged", 7, darkRangedZach.getCurrentHealth());

    }

    @Test
    public void itemUsedHealTest(){
        String mockUserId ="testUser1";
        MainActivity mockMainActivity = Mockito.mock(MainActivity.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        when(mockMainActivity.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.getUid()).thenReturn(mockUserId);
        DeckService mockDeckService = Mockito.mock(DeckService.class);
        when(mockMainActivity.getDeckService()).thenReturn(mockDeckService);

        CombatService testedService = new CombatService(mockMainActivity);
        testedService.initializeCombat();

        Deck testUserDeck = new Deck(mockUserId,"userDeck");
        List<Card> userCards = new ArrayList<>();

        for(int i=0; i<4; i++) {
            ItemCard testItem1 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "healItem", "test", ItemType.CONSUMABLE, new HealAbility("test", "test", AbilityTarget.SINGLE_ALLY, 2));
            userCards.add(testItem1);
        }
        CharacterCard characterCard1 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Zach","Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.ICE, AttackType.MELEE,1),20);
        userCards.add(characterCard1);
        CharacterCard characterCard2 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Zach2","Zach is a software engineer",new HealAbility("AllyHeal","slap", AbilityTarget.SINGLE_ALLY,2),25);
        userCards.add(characterCard2);
        testUserDeck.setCards(userCards);

        List<Card> encounterCards = new ArrayList<>();

        CharacterCard encounterCharacterCard1 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Dark Zach","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.MELEE,1),5);
        encounterCards.add(encounterCharacterCard1);
        CharacterCard encounterCharacterCard2 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Dark Zach 2","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.MELEE,1),5);
        encounterCards.add(encounterCharacterCard2);
        CharacterCard encounterCharacterCard3 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Dark Ranged Zach","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.RANGED,1),5);
        encounterCards.add(encounterCharacterCard3);
        ItemCard testItem1 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
        encounterCards.add(testItem1);
        ItemCard testItem2 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
        encounterCards.add(testItem2);
        ItemCard testItem3 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
        encounterCards.add(testItem3);

        Encounter testEncounter = new Encounter("testEncounter", EncounterDifficultyLevel.EASY, encounterCards);

        testedService.deckFetchReturned(testUserDeck);
        testedService.encounterFetchReturned(testEncounter);
        ArgumentCaptor<CombatStateUpdateEvent> combatStateUpdateEventArgumentCaptor = ArgumentCaptor.forClass(CombatStateUpdateEvent.class);
        verify(mockMainActivity,atLeastOnce()).publishEvent(combatStateUpdateEventArgumentCaptor.capture());
        assertNotNull(combatStateUpdateEventArgumentCaptor.getValue());
        CombatCardModel zach1 = null;
        CombatCardModel zach2 = null;
        for(CombatCardModel cardModel: combatStateUpdateEventArgumentCaptor.getValue().getCombatStateModel().getPlayerHand()){
            if(cardModel.getCardName().equals(characterCard1.getCardName())){
                zach1 = cardModel;
            } else if(cardModel.getCardName().equals(characterCard2.getCardName())){
                zach2 = cardModel;
            }
        }

        testedService.cardDroppedOnLine(zach1,CombatLine.PLAYER_FRONT_LINE);
        testedService.cardDroppedOnLine(zach2,CombatLine.PLAYER_FRONT_LINE);
        if(testedService.endSetup()){
            testedService.endPlayerTurn();
        }
        verify(mockMainActivity,atLeastOnce()).publishEvent(combatStateUpdateEventArgumentCaptor.capture());
        assertNotNull(combatStateUpdateEventArgumentCaptor.getValue());
        assertEquals("Expected Zach1 to be targeted by all 3 1 hp attacks", 17, zach1.getCurrentHealth());
        assertEquals("Expected Zach2 to not be targeted.", 25, zach2.getCurrentHealth());

        CombatCardModel healItem = null;
        for(CombatCardModel cardModel: combatStateUpdateEventArgumentCaptor.getValue().getCombatStateModel().getPlayerHand()){
            if(cardModel.getCardName().equals("healItem")){
                healItem = cardModel;
            }
        }
        testedService.cardDroppedOnCard(healItem,zach1);
        verify(mockMainActivity,atLeastOnce()).publishEvent(combatStateUpdateEventArgumentCaptor.capture());
        assertNotNull(combatStateUpdateEventArgumentCaptor.getValue());
        assertEquals("Expected Zach1 to be healed by heal amount", 19, zach1.getCurrentHealth());
        assertEquals("Expected Zach2 to not be affected", 25, zach2.getCurrentHealth());
    }

    @Test
    public void rowItemUsedHealTest(){
        String mockUserId ="testUser1";
        MainActivity mockMainActivity = Mockito.mock(MainActivity.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        when(mockMainActivity.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.getUid()).thenReturn(mockUserId);
        DeckService mockDeckService = Mockito.mock(DeckService.class);
        when(mockMainActivity.getDeckService()).thenReturn(mockDeckService);

        CombatService testedService = new CombatService(mockMainActivity);
        testedService.initializeCombat();

        Deck testUserDeck = new Deck(mockUserId,"userDeck");
        List<Card> userCards = new ArrayList<>();

        for(int i=0; i<4; i++) {
            ItemCard testItem1 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "healItem", "test", ItemType.CONSUMABLE, new HealAbility("test", "test", AbilityTarget.ROW_ALLY, 2));
            userCards.add(testItem1);
        }
        CharacterCard characterCard1 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Zach","Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.ICE, AttackType.MELEE,1),20);
        userCards.add(characterCard1);
        CharacterCard characterCard2 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Zach2","Zach is a software engineer",new HealAbility("AllyHeal","slap", AbilityTarget.SINGLE_ALLY,2),25);
        userCards.add(characterCard2);
        testUserDeck.setCards(userCards);

        List<Card> encounterCards = new ArrayList<>();

        CharacterCard encounterCharacterCard1 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Dark Zach","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.MELEE,1),5);
        encounterCards.add(encounterCharacterCard1);
        CharacterCard encounterCharacterCard2 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Dark Zach 2","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.MELEE,1),5);
        encounterCards.add(encounterCharacterCard2);
        CharacterCard encounterCharacterCard3 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Dark Ranged Zach","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.RANGED,1),5);
        encounterCards.add(encounterCharacterCard3);
        ItemCard testItem1 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
        encounterCards.add(testItem1);
        ItemCard testItem2 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
        encounterCards.add(testItem2);
        ItemCard testItem3 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
        encounterCards.add(testItem3);

        Encounter testEncounter = new Encounter("testEncounter", EncounterDifficultyLevel.EASY, encounterCards);

        testedService.deckFetchReturned(testUserDeck);
        testedService.encounterFetchReturned(testEncounter);
        ArgumentCaptor<CombatStateUpdateEvent> combatStateUpdateEventArgumentCaptor = ArgumentCaptor.forClass(CombatStateUpdateEvent.class);
        verify(mockMainActivity,atLeastOnce()).publishEvent(combatStateUpdateEventArgumentCaptor.capture());
        assertNotNull(combatStateUpdateEventArgumentCaptor.getValue());
        CombatCardModel zach1 = null;
        CombatCardModel zach2 = null;
        for(CombatCardModel cardModel: combatStateUpdateEventArgumentCaptor.getValue().getCombatStateModel().getPlayerHand()){
            if(cardModel.getCardName().equals(characterCard1.getCardName())){
                zach1 = cardModel;
            } else if(cardModel.getCardName().equals(characterCard2.getCardName())){
                zach2 = cardModel;
            }
        }

        testedService.cardDroppedOnLine(zach1,CombatLine.PLAYER_FRONT_LINE);
        testedService.cardDroppedOnLine(zach2,CombatLine.PLAYER_FRONT_LINE);
        if(testedService.endSetup()){
            testedService.endPlayerTurn();
        }
        verify(mockMainActivity,atLeastOnce()).publishEvent(combatStateUpdateEventArgumentCaptor.capture());
        assertNotNull(combatStateUpdateEventArgumentCaptor.getValue());
        assertEquals("Expected Zach1 to be targeted by all 3 1 hp attacks", 17, zach1.getCurrentHealth());
        assertEquals("Expected Zach2 to not be targeted.", 25, zach2.getCurrentHealth());

        CombatCardModel healItem = null;
        for(CombatCardModel cardModel: combatStateUpdateEventArgumentCaptor.getValue().getCombatStateModel().getPlayerHand()){
            if(cardModel.getCardName().equals("healItem")){
                healItem = cardModel;
            }
        }
        testedService.cardDroppedOnLine(healItem,CombatLine.PLAYER_FRONT_LINE);
        verify(mockMainActivity,atLeastOnce()).publishEvent(combatStateUpdateEventArgumentCaptor.capture());
        assertNotNull(combatStateUpdateEventArgumentCaptor.getValue());
        assertEquals("Expected Zach1 to be healed by heal amount", 19, zach1.getCurrentHealth());
        assertEquals("Expected Zach2 to not be affected", 25, zach2.getCurrentHealth());
    }

    @Test
    public void rowItemUsedHealBacklineTest(){
        String mockUserId ="testUser1";
        MainActivity mockMainActivity = Mockito.mock(MainActivity.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        when(mockMainActivity.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.getUid()).thenReturn(mockUserId);
        DeckService mockDeckService = Mockito.mock(DeckService.class);
        when(mockMainActivity.getDeckService()).thenReturn(mockDeckService);

        CombatService testedService = new CombatService(mockMainActivity);
        testedService.initializeCombat();

        Deck testUserDeck = new Deck(mockUserId,"userDeck");
        List<Card> userCards = new ArrayList<>();

        for(int i=0; i<4; i++) {
            ItemCard testItem1 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "healItem", "test", ItemType.CONSUMABLE, new HealAbility("test", "test", AbilityTarget.ROW_ALLY, 2));
            userCards.add(testItem1);
        }
        CharacterCard characterCard1 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Zach","Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.ICE, AttackType.MELEE,1),20);
        userCards.add(characterCard1);
        CharacterCard characterCard2 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Zach2","Zach is a software engineer",new HealAbility("AllyHeal","slap", AbilityTarget.SINGLE_ALLY,2),25);
        userCards.add(characterCard2);
        testUserDeck.setCards(userCards);

        List<Card> encounterCards = new ArrayList<>();

        CharacterCard encounterCharacterCard1 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Dark Zach","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.MELEE,1),5);
        encounterCards.add(encounterCharacterCard1);
        CharacterCard encounterCharacterCard2 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Dark Zach 2","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.MELEE,1),5);
        encounterCards.add(encounterCharacterCard2);
        CharacterCard encounterCharacterCard3 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Dark Ranged Zach","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.RANGED,1),5);
        encounterCards.add(encounterCharacterCard3);
        ItemCard testItem1 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
        encounterCards.add(testItem1);
        ItemCard testItem2 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
        encounterCards.add(testItem2);
        ItemCard testItem3 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
        encounterCards.add(testItem3);

        Encounter testEncounter = new Encounter("testEncounter", EncounterDifficultyLevel.EASY, encounterCards);

        testedService.deckFetchReturned(testUserDeck);
        testedService.encounterFetchReturned(testEncounter);
        ArgumentCaptor<CombatStateUpdateEvent> combatStateUpdateEventArgumentCaptor = ArgumentCaptor.forClass(CombatStateUpdateEvent.class);
        verify(mockMainActivity,atLeastOnce()).publishEvent(combatStateUpdateEventArgumentCaptor.capture());
        assertNotNull(combatStateUpdateEventArgumentCaptor.getValue());
        CombatCardModel zach1 = null;
        CombatCardModel zach2 = null;
        for(CombatCardModel cardModel: combatStateUpdateEventArgumentCaptor.getValue().getCombatStateModel().getPlayerHand()){
            if(cardModel.getCardName().equals(characterCard1.getCardName())){
                zach1 = cardModel;
            } else if(cardModel.getCardName().equals(characterCard2.getCardName())){
                zach2 = cardModel;
            }
        }

        testedService.cardDroppedOnLine(zach1,CombatLine.PLAYER_BACK_LINE);
        testedService.cardDroppedOnLine(zach2,CombatLine.PLAYER_BACK_LINE);
        if(testedService.endSetup()){
            testedService.endPlayerTurn();
        }
        verify(mockMainActivity,atLeastOnce()).publishEvent(combatStateUpdateEventArgumentCaptor.capture());
        assertNotNull(combatStateUpdateEventArgumentCaptor.getValue());
        assertEquals("Expected Zach1 to be targeted by all 3 1 hp attacks", 17, zach1.getCurrentHealth());
        assertEquals("Expected Zach2 to not be targeted.", 25, zach2.getCurrentHealth());

        CombatCardModel healItem = null;
        for(CombatCardModel cardModel: combatStateUpdateEventArgumentCaptor.getValue().getCombatStateModel().getPlayerHand()){
            if(cardModel.getCardName().equals("healItem")){
                healItem = cardModel;
            }
        }
        testedService.cardDroppedOnLine(healItem,CombatLine.PLAYER_BACK_LINE);
        verify(mockMainActivity,atLeastOnce()).publishEvent(combatStateUpdateEventArgumentCaptor.capture());
        assertNotNull(combatStateUpdateEventArgumentCaptor.getValue());
        assertEquals("Expected Zach1 to be healed by heal amount", 19, zach1.getCurrentHealth());
        assertEquals("Expected Zach2 to not be affected", 25, zach2.getCurrentHealth());
    }

    @Test
    public void rowItemUsedAllAllyHealTest(){
        String mockUserId ="testUser1";
        MainActivity mockMainActivity = Mockito.mock(MainActivity.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        when(mockMainActivity.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.getUid()).thenReturn(mockUserId);
        DeckService mockDeckService = Mockito.mock(DeckService.class);
        when(mockMainActivity.getDeckService()).thenReturn(mockDeckService);

        CombatService testedService = new CombatService(mockMainActivity);
        testedService.initializeCombat();

        Deck testUserDeck = new Deck(mockUserId,"userDeck");
        List<Card> userCards = new ArrayList<>();

        for(int i=0; i<4; i++) {
            ItemCard testItem1 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "healItem", "test", ItemType.CONSUMABLE, new HealAbility("test", "test", AbilityTarget.ALL_ALLY, 2));
            userCards.add(testItem1);
        }
        CharacterCard characterCard1 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Zach","Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.ICE, AttackType.MELEE,1),20);
        userCards.add(characterCard1);
        CharacterCard characterCard2 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Zach2","Zach is a software engineer",new HealAbility("AllyHeal","slap", AbilityTarget.SINGLE_ALLY,2),25);
        userCards.add(characterCard2);
        testUserDeck.setCards(userCards);

        List<Card> encounterCards = new ArrayList<>();

        CharacterCard encounterCharacterCard1 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Dark Zach","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.MELEE,1),5);
        encounterCards.add(encounterCharacterCard1);
        CharacterCard encounterCharacterCard2 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Dark Zach 2","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.MELEE,1),5);
        encounterCards.add(encounterCharacterCard2);
        CharacterCard encounterCharacterCard3 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Dark Ranged Zach","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.RANGED,1),5);
        encounterCards.add(encounterCharacterCard3);
        ItemCard testItem1 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
        encounterCards.add(testItem1);
        ItemCard testItem2 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
        encounterCards.add(testItem2);
        ItemCard testItem3 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
        encounterCards.add(testItem3);

        Encounter testEncounter = new Encounter("testEncounter", EncounterDifficultyLevel.EASY, encounterCards);

        testedService.deckFetchReturned(testUserDeck);
        testedService.encounterFetchReturned(testEncounter);
        ArgumentCaptor<CombatStateUpdateEvent> combatStateUpdateEventArgumentCaptor = ArgumentCaptor.forClass(CombatStateUpdateEvent.class);
        verify(mockMainActivity,atLeastOnce()).publishEvent(combatStateUpdateEventArgumentCaptor.capture());
        assertNotNull(combatStateUpdateEventArgumentCaptor.getValue());
        CombatCardModel zach1 = null;
        CombatCardModel zach2 = null;
        for(CombatCardModel cardModel: combatStateUpdateEventArgumentCaptor.getValue().getCombatStateModel().getPlayerHand()){
            if(cardModel.getCardName().equals(characterCard1.getCardName())){
                zach1 = cardModel;
            } else if(cardModel.getCardName().equals(characterCard2.getCardName())){
                zach2 = cardModel;
            }
        }

        testedService.cardDroppedOnLine(zach1,CombatLine.PLAYER_BACK_LINE);
        testedService.cardDroppedOnLine(zach2,CombatLine.PLAYER_BACK_LINE);
        if(testedService.isPlayerTurn()){
            testedService.endPlayerTurn();
        }
        verify(mockMainActivity,atLeastOnce()).publishEvent(combatStateUpdateEventArgumentCaptor.capture());
        assertNotNull(combatStateUpdateEventArgumentCaptor.getValue());
        assertEquals("Expected Zach1 to be targeted by all 3 1 hp attacks", 17, zach1.getCurrentHealth());
        assertEquals("Expected Zach2 to not be targeted.", 25, zach2.getCurrentHealth());

        CombatCardModel healItem = null;
        for(CombatCardModel cardModel: combatStateUpdateEventArgumentCaptor.getValue().getCombatStateModel().getPlayerHand()){
            if(cardModel.getCardName().equals("healItem")){
                healItem = cardModel;
            }
        }
        testedService.cardDroppedOnLine(healItem,CombatLine.PLAYER_BACK_LINE);
        verify(mockMainActivity,atLeastOnce()).publishEvent(combatStateUpdateEventArgumentCaptor.capture());
        assertNotNull(combatStateUpdateEventArgumentCaptor.getValue());
        assertEquals("Expected Zach1 to be healed by heal amount", 19, zach1.getCurrentHealth());
        assertEquals("Expected Zach2 to not be affected", 25, zach2.getCurrentHealth());
    }

    @Test
    public void buffItemUsedTest(){
        String mockUserId ="testUser1";
        MainActivity mockMainActivity = Mockito.mock(MainActivity.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        when(mockMainActivity.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.getUid()).thenReturn(mockUserId);
        DeckService mockDeckService = Mockito.mock(DeckService.class);
        when(mockMainActivity.getDeckService()).thenReturn(mockDeckService);

        CombatService testedService = new CombatService(mockMainActivity);
        testedService.initializeCombat();

        Deck testUserDeck = new Deck(mockUserId,"userDeck");
        List<Card> userCards = new ArrayList<>();

        for(int i=0; i<4; i++) {
            ItemCard testItem1 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "buffItem", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 2));
            userCards.add(testItem1);
        }
        CharacterCard characterCard1 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Zach","Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.ICE, AttackType.MELEE,1),20);
        userCards.add(characterCard1);
        CharacterCard characterCard2 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Zach2","Zach is a software engineer",new HealAbility("AllyHeal","slap", AbilityTarget.SINGLE_ALLY,2),25);
        userCards.add(characterCard2);
        testUserDeck.setCards(userCards);

        List<Card> encounterCards = new ArrayList<>();

        CharacterCard encounterCharacterCard1 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Dark Zach","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.MELEE,1),5);
        encounterCards.add(encounterCharacterCard1);
        CharacterCard encounterCharacterCard2 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Dark Zach 2","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.MELEE,1),5);
        encounterCards.add(encounterCharacterCard2);
        CharacterCard encounterCharacterCard3 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Dark Ranged Zach","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.RANGED,1),5);
        encounterCards.add(encounterCharacterCard3);
        ItemCard testItem1 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
        encounterCards.add(testItem1);
        ItemCard testItem2 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
        encounterCards.add(testItem2);
        ItemCard testItem3 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
        encounterCards.add(testItem3);

        Encounter testEncounter = new Encounter("testEncounter", EncounterDifficultyLevel.EASY, encounterCards);

        testedService.encounterFetchReturned(testEncounter);
        testedService.deckFetchReturned(testUserDeck);

        ArgumentCaptor<CombatStateUpdateEvent> combatStateUpdateEventArgumentCaptor = ArgumentCaptor.forClass(CombatStateUpdateEvent.class);
        verify(mockMainActivity,atLeastOnce()).publishEvent(combatStateUpdateEventArgumentCaptor.capture());
        assertNotNull(combatStateUpdateEventArgumentCaptor.getValue());
        CombatCardModel zach1 = null;
        CombatCardModel zach2 = null;
        for(CombatCardModel cardModel: combatStateUpdateEventArgumentCaptor.getValue().getCombatStateModel().getPlayerHand()){
            if(cardModel.getCardName().equals(characterCard1.getCardName())){
                zach1 = cardModel;
            } else if(cardModel.getCardName().equals(characterCard2.getCardName())){
                zach2 = cardModel;
            }
        }

        testedService.cardDroppedOnLine(zach1,CombatLine.PLAYER_BACK_LINE);
        testedService.cardDroppedOnLine(zach2,CombatLine.PLAYER_BACK_LINE);
        if(testedService.isPlayerTurn()){
            testedService.endPlayerTurn();
        }
        verify(mockMainActivity,atLeastOnce()).publishEvent(combatStateUpdateEventArgumentCaptor.capture());
        assertNotNull(combatStateUpdateEventArgumentCaptor.getValue());
        assertEquals("Expected Zach1 to be targeted by all 3 1 hp attacks", 17, zach1.getCurrentHealth());
        assertEquals("Expected Zach2 to not be targeted.", 25, zach2.getCurrentHealth());

        CombatCardModel buffItem = null;
        for(CombatCardModel cardModel: combatStateUpdateEventArgumentCaptor.getValue().getCombatStateModel().getPlayerHand()){
            if(cardModel.getCardName().equals("buffItem")){
                buffItem = cardModel;
            }
        }
        testedService.cardDroppedOnCard(buffItem,zach1);
        verify(mockMainActivity,atLeastOnce()).publishEvent(combatStateUpdateEventArgumentCaptor.capture());
        assertNotNull(combatStateUpdateEventArgumentCaptor.getValue());
        assertEquals("Expected Zach1 to be healed by buff amount", 19, zach1.getCurrentHealth());
        assertEquals("Expected Zach1 to have max hp buffed by buff amount", 22, zach1.getMaxHealth());
        assertEquals("Expected Zach2 to not be affected", 25, zach2.getCurrentHealth());
    }

    @Test
    public void enemyBuffItemUsedTest() {
        String mockUserId ="testUser1";
        MainActivity mockMainActivity = Mockito.mock(MainActivity.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        when(mockMainActivity.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.getUid()).thenReturn(mockUserId);
        DeckService mockDeckService = Mockito.mock(DeckService.class);
        when(mockMainActivity.getDeckService()).thenReturn(mockDeckService);

        CombatService testedService = new CombatService(mockMainActivity);
        testedService.initializeCombat();

        Deck testUserDeck = new Deck(mockUserId,"userDeck");
        List<Card> userCards = new ArrayList<>();

        for(int i=0; i<4; i++) {
            ItemCard testItem1 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "buffItem", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 2));
            userCards.add(testItem1);
        }
        CharacterCard characterCard1 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Zach","Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.ICE, AttackType.MELEE,1),20);
        userCards.add(characterCard1);
        CharacterCard characterCard2 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Zach2","Zach is a software engineer",new HealAbility("AllyHeal","slap", AbilityTarget.SINGLE_ALLY,2),25);
        userCards.add(characterCard2);
        testUserDeck.setCards(userCards);

        List<Card> encounterCards = new ArrayList<>();

        CharacterCard encounterCharacterCard1 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Dark Zach","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.MELEE,1),5);
        encounterCards.add(encounterCharacterCard1);
        CharacterCard encounterCharacterCard2 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Dark Zach 2","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.MELEE,2),6);
        encounterCards.add(encounterCharacterCard2);
        CharacterCard encounterCharacterCard3 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Dark Ranged Zach","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.RANGED,2),7);
        encounterCards.add(encounterCharacterCard3);
        ItemCard testItem1 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.ATTACK, 3));
        encounterCards.add(testItem1);
        ItemCard testItem2 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
        encounterCards.add(testItem2);
        ItemCard testItem3 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
        encounterCards.add(testItem3);

        Encounter testEncounter = new Encounter("testEncounter", EncounterDifficultyLevel.EASY, encounterCards);

        testedService.encounterFetchReturned(testEncounter);
        testedService.deckFetchReturned(testUserDeck);

        ArgumentCaptor<CombatStateUpdateEvent> combatStateUpdateEventArgumentCaptor = ArgumentCaptor.forClass(CombatStateUpdateEvent.class);
        verify(mockMainActivity,atLeastOnce()).publishEvent(combatStateUpdateEventArgumentCaptor.capture());
        assertNotNull(combatStateUpdateEventArgumentCaptor.getValue());
        CombatCardModel zach1 = null;
        CombatCardModel zach2 = null;
        for(CombatCardModel cardModel: combatStateUpdateEventArgumentCaptor.getValue().getCombatStateModel().getPlayerHand()){
            if(cardModel.getCardName().equals(characterCard1.getCardName())){
                zach1 = cardModel;
            } else if(cardModel.getCardName().equals(characterCard2.getCardName())){
                zach2 = cardModel;
            }
        }

        testedService.cardDroppedOnLine(zach1,CombatLine.PLAYER_BACK_LINE);
        testedService.cardDroppedOnLine(zach2,CombatLine.PLAYER_BACK_LINE);
        if(testedService.isPlayerTurn()){
            testedService.endPlayerTurn();
        }
        verify(mockMainActivity,atLeastOnce()).publishEvent(combatStateUpdateEventArgumentCaptor.capture());
        assertNotNull(combatStateUpdateEventArgumentCaptor.getValue());
        assertEquals("Expected Zach1 to be targeted by all 2 2 hp attacks and 1 4 hp attack", 12, zach1.getCurrentHealth());
        assertEquals("Expected Zach2 to not be targeted.", 25, zach2.getCurrentHealth());

    }

    @Test
    public void enemyRangedItemUsedTest() {
        String mockUserId ="testUser1";
        MainActivity mockMainActivity = Mockito.mock(MainActivity.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        when(mockMainActivity.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.getUid()).thenReturn(mockUserId);
        DeckService mockDeckService = Mockito.mock(DeckService.class);
        when(mockMainActivity.getDeckService()).thenReturn(mockDeckService);

        CombatService testedService = new CombatService(mockMainActivity);
        testedService.initializeCombat();

        Deck testUserDeck = new Deck(mockUserId,"userDeck");
        List<Card> userCards = new ArrayList<>();

        for(int i=0; i<4; i++) {
            ItemCard testItem1 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "buffItem", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 2));
            userCards.add(testItem1);
        }
        CharacterCard characterCard1 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Zach","Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.ICE, AttackType.MELEE,1),20);
        userCards.add(characterCard1);
        CharacterCard characterCard2 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Zach2","Zach is a software engineer",new HealAbility("AllyHeal","slap", AbilityTarget.SINGLE_ALLY,2),25);
        userCards.add(characterCard2);
        testUserDeck.setCards(userCards);

        List<Card> encounterCards = new ArrayList<>();

        CharacterCard encounterCharacterCard1 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Dark Zach","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.MELEE,1),5);
        encounterCards.add(encounterCharacterCard1);
        CharacterCard encounterCharacterCard2 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Dark Zach 2","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.MELEE,2),6);
        encounterCards.add(encounterCharacterCard2);
        CharacterCard encounterCharacterCard3 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Dark Ranged Zach","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.RANGED,2),7);
        encounterCards.add(encounterCharacterCard3);
        ItemCard testItem1 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.ATTACK, 3));
        encounterCards.add(testItem1);
        ItemCard testItem2 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
        encounterCards.add(testItem2);
        ItemCard testItem3 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
        encounterCards.add(testItem3);

        Encounter testEncounter = new Encounter("testEncounter", EncounterDifficultyLevel.EASY, encounterCards);

        testedService.encounterFetchReturned(testEncounter);
        testedService.deckFetchReturned(testUserDeck);

        ArgumentCaptor<CombatStateUpdateEvent> combatStateUpdateEventArgumentCaptor = ArgumentCaptor.forClass(CombatStateUpdateEvent.class);
        verify(mockMainActivity,atLeastOnce()).publishEvent(combatStateUpdateEventArgumentCaptor.capture());
        assertNotNull(combatStateUpdateEventArgumentCaptor.getValue());
        CombatCardModel zach1 = null;
        CombatCardModel zach2 = null;
        for(CombatCardModel cardModel: combatStateUpdateEventArgumentCaptor.getValue().getCombatStateModel().getPlayerHand()){
            if(cardModel.getCardName().equals(characterCard1.getCardName())){
                zach1 = cardModel;
            } else if(cardModel.getCardName().equals(characterCard2.getCardName())){
                zach2 = cardModel;
            }
        }

        testedService.cardDroppedOnLine(zach1,CombatLine.PLAYER_BACK_LINE);
        testedService.cardDroppedOnLine(zach2,CombatLine.PLAYER_BACK_LINE);

        CombatCardModel testRangedItem = new CombatCardModel(UUID.randomUUID().toString(), "rangedItem", CardType.ITEM, false,false);
        testRangedItem.setAbility(new DamageAbility("test", "test", AbilityTarget.SINGLE_ENEMY, DamageType.NORMAL, AttackType.RANGED, 5));
        combatStateUpdateEventArgumentCaptor.getValue().getCombatStateModel().getEnemyHand().add(testRangedItem);

        CombatCardModel darkZach = null;
        CombatCardModel darkZach2 = null;
        CombatCardModel darkRangedZach = null;
        List<CombatCardModel> allEnemyCards = new ArrayList<>();
        allEnemyCards.addAll(combatStateUpdateEventArgumentCaptor.getValue().getCombatStateModel().getEnemyFrontLine());
        allEnemyCards.addAll(combatStateUpdateEventArgumentCaptor.getValue().getCombatStateModel().getEnemyBackLine());
        for(CombatCardModel cardModel: allEnemyCards){
            if(cardModel.getCardName().equals("Dark Zach")){
                darkZach = cardModel;
            } else if(cardModel.getCardName().equals("Dark Zach 2")){
                darkZach2 = cardModel;
            }else if(cardModel.getCardName().equals("Dark Ranged Zach")){
                darkRangedZach = cardModel;
            }

        }

        if(testedService.isPlayerTurn()){
            testedService.endPlayerTurn();
        }

        boolean gaveDarkZachRangedItem = false;
        for(Ability ability: darkRangedZach.getAbilities()){
            if(ability.getAbilityName().equals(testRangedItem.getAbility().getAbilityName())){
                gaveDarkZachRangedItem = true;
            }
        }

        assert(gaveDarkZachRangedItem);
    }


    @Test
    public void enemyMeleeItemUsedTest() {
        String mockUserId ="testUser1";
        MainActivity mockMainActivity = Mockito.mock(MainActivity.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        when(mockMainActivity.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.getUid()).thenReturn(mockUserId);
        DeckService mockDeckService = Mockito.mock(DeckService.class);
        when(mockMainActivity.getDeckService()).thenReturn(mockDeckService);

        CombatService testedService = new CombatService(mockMainActivity);
        testedService.initializeCombat();

        Deck testUserDeck = new Deck(mockUserId,"userDeck");
        List<Card> userCards = new ArrayList<>();

        for(int i=0; i<4; i++) {
            ItemCard testItem1 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "buffItem", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 2));
            userCards.add(testItem1);
        }
        CharacterCard characterCard1 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Zach","Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.ICE, AttackType.MELEE,1),20);
        userCards.add(characterCard1);
        CharacterCard characterCard2 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Zach2","Zach is a software engineer",new HealAbility("AllyHeal","slap", AbilityTarget.SINGLE_ALLY,2),25);
        userCards.add(characterCard2);
        testUserDeck.setCards(userCards);

        List<Card> encounterCards = new ArrayList<>();

        CharacterCard encounterCharacterCard1 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Dark Zach","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.MELEE,1),5);
        encounterCards.add(encounterCharacterCard1);
        CharacterCard encounterCharacterCard2 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Dark Zach 2","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.MELEE,2),6);
        encounterCards.add(encounterCharacterCard2);
        CharacterCard encounterCharacterCard3 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Dark Ranged Zach","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.RANGED,2),7);
        encounterCards.add(encounterCharacterCard3);
        ItemCard testItem1 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.ATTACK, 3));
        encounterCards.add(testItem1);
        ItemCard testItem2 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
        encounterCards.add(testItem2);
        ItemCard testItem3 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
        encounterCards.add(testItem3);

        Encounter testEncounter = new Encounter("testEncounter", EncounterDifficultyLevel.EASY, encounterCards);

        testedService.encounterFetchReturned(testEncounter);
        testedService.deckFetchReturned(testUserDeck);

        ArgumentCaptor<CombatStateUpdateEvent> combatStateUpdateEventArgumentCaptor = ArgumentCaptor.forClass(CombatStateUpdateEvent.class);
        verify(mockMainActivity,atLeastOnce()).publishEvent(combatStateUpdateEventArgumentCaptor.capture());
        assertNotNull(combatStateUpdateEventArgumentCaptor.getValue());
        CombatCardModel zach1 = null;
        CombatCardModel zach2 = null;
        for(CombatCardModel cardModel: combatStateUpdateEventArgumentCaptor.getValue().getCombatStateModel().getPlayerHand()){
            if(cardModel.getCardName().equals(characterCard1.getCardName())){
                zach1 = cardModel;
            } else if(cardModel.getCardName().equals(characterCard2.getCardName())){
                zach2 = cardModel;
            }
        }

        testedService.cardDroppedOnLine(zach1,CombatLine.PLAYER_BACK_LINE);
        testedService.cardDroppedOnLine(zach2,CombatLine.PLAYER_BACK_LINE);

        CombatCardModel testMeleeItem = new CombatCardModel(UUID.randomUUID().toString(), "meleeItem", CardType.ITEM, false,false);
        testMeleeItem.setAbility(new DamageAbility("test", "test", AbilityTarget.SINGLE_ENEMY, DamageType.NORMAL, AttackType.MELEE, 5));
        combatStateUpdateEventArgumentCaptor.getValue().getCombatStateModel().getEnemyHand().add(testMeleeItem);

        CombatCardModel darkZach = null;
        CombatCardModel darkZach2 = null;
        CombatCardModel darkRangedZach = null;
        List<CombatCardModel> allEnemyCards = new ArrayList<>();
        allEnemyCards.addAll(combatStateUpdateEventArgumentCaptor.getValue().getCombatStateModel().getEnemyFrontLine());
        allEnemyCards.addAll(combatStateUpdateEventArgumentCaptor.getValue().getCombatStateModel().getEnemyBackLine());
        for(CombatCardModel cardModel: allEnemyCards){
            if(cardModel.getCardName().equals("Dark Zach")){
                darkZach = cardModel;
            } else if(cardModel.getCardName().equals("Dark Zach 2")){
                darkZach2 = cardModel;
            }else if(cardModel.getCardName().equals("Dark Ranged Zach")){
                darkRangedZach = cardModel;
            }

        }

        if(testedService.isPlayerTurn()){
            testedService.endPlayerTurn();
        }

        boolean gaveDarkZachMeleeItem = false;
        for(Ability ability: darkZach.getAbilities()){
            if(ability.getAbilityName().equals(testMeleeItem.getAbility().getAbilityName())){
                gaveDarkZachMeleeItem = true;
            }
        }

        assert(gaveDarkZachMeleeItem);
    }

    @Test
    public void enemyHealItemUsedTest() {
        String mockUserId ="testUser1";
        MainActivity mockMainActivity = Mockito.mock(MainActivity.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        when(mockMainActivity.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.getUid()).thenReturn(mockUserId);
        DeckService mockDeckService = Mockito.mock(DeckService.class);
        when(mockMainActivity.getDeckService()).thenReturn(mockDeckService);

        CombatService testedService = new CombatService(mockMainActivity);
        testedService.initializeCombat();

        Deck testUserDeck = new Deck(mockUserId,"userDeck");
        List<Card> userCards = new ArrayList<>();

        for(int i=0; i<4; i++) {
            ItemCard testItem1 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "buffItem", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 2));
            userCards.add(testItem1);
        }
        CharacterCard characterCard1 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Zach","Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.ICE, AttackType.MELEE,1),20);
        userCards.add(characterCard1);
        CharacterCard characterCard2 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Zach2","Zach is a software engineer",new HealAbility("AllyHeal","slap", AbilityTarget.SINGLE_ALLY,2),25);
        userCards.add(characterCard2);
        testUserDeck.setCards(userCards);

        List<Card> encounterCards = new ArrayList<>();

        CharacterCard encounterCharacterCard1 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Dark Zach","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.MELEE,1),5);
        encounterCards.add(encounterCharacterCard1);
        CharacterCard encounterCharacterCard2 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Dark Zach 2","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.MELEE,2),6);
        encounterCards.add(encounterCharacterCard2);
        CharacterCard encounterCharacterCard3 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Dark Ranged Zach","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.RANGED,2),7);
        encounterCards.add(encounterCharacterCard3);
        ItemCard testItem1 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.ATTACK, 3));
        encounterCards.add(testItem1);
        ItemCard testItem2 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
        encounterCards.add(testItem2);
        ItemCard testItem3 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
        encounterCards.add(testItem3);

        Encounter testEncounter = new Encounter("testEncounter", EncounterDifficultyLevel.EASY, encounterCards);

        testedService.encounterFetchReturned(testEncounter);
        testedService.deckFetchReturned(testUserDeck);

        ArgumentCaptor<CombatStateUpdateEvent> combatStateUpdateEventArgumentCaptor = ArgumentCaptor.forClass(CombatStateUpdateEvent.class);
        verify(mockMainActivity,atLeastOnce()).publishEvent(combatStateUpdateEventArgumentCaptor.capture());
        assertNotNull(combatStateUpdateEventArgumentCaptor.getValue());
        CombatCardModel zach1 = null;
        CombatCardModel zach2 = null;
        for(CombatCardModel cardModel: combatStateUpdateEventArgumentCaptor.getValue().getCombatStateModel().getPlayerHand()){
            if(cardModel.getCardName().equals(characterCard1.getCardName())){
                zach1 = cardModel;
            } else if(cardModel.getCardName().equals(characterCard2.getCardName())){
                zach2 = cardModel;
            }
        }

        testedService.cardDroppedOnLine(zach1,CombatLine.PLAYER_BACK_LINE);
        testedService.cardDroppedOnLine(zach2,CombatLine.PLAYER_BACK_LINE);

        CombatCardModel testHealItem = new CombatCardModel(UUID.randomUUID().toString(), "meleeItem", CardType.ITEM, false,false);
        testHealItem.setAbility(new HealAbility("test", "test", AbilityTarget.SINGLE_ALLY,  5));
        combatStateUpdateEventArgumentCaptor.getValue().getCombatStateModel().getEnemyHand().add(testHealItem);

        CombatCardModel darkZach = null;
        CombatCardModel darkZach2 = null;
        CombatCardModel darkRangedZach = null;
        List<CombatCardModel> allEnemyCards = new ArrayList<>();
        allEnemyCards.addAll(combatStateUpdateEventArgumentCaptor.getValue().getCombatStateModel().getEnemyFrontLine());
        allEnemyCards.addAll(combatStateUpdateEventArgumentCaptor.getValue().getCombatStateModel().getEnemyBackLine());
        for(CombatCardModel cardModel: allEnemyCards){
            if(cardModel.getCardName().equals("Dark Zach")){
                darkZach = cardModel;
            } else if(cardModel.getCardName().equals("Dark Zach 2")){
                darkZach2 = cardModel;
            }else if(cardModel.getCardName().equals("Dark Ranged Zach")){
                darkRangedZach = cardModel;
            }

        }

        darkZach.setCurrentHealth(5);

        if(testedService.isPlayerTurn()){
            testedService.endPlayerTurn();
        }

        assertEquals("Dark Zach Should be healed with item", 8, darkZach.getCurrentHealth());

    }


    @Test
    public void enemyUsedHealAbilityTest() {
        String mockUserId ="testUser1";
        MainActivity mockMainActivity = Mockito.mock(MainActivity.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        when(mockMainActivity.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.getUid()).thenReturn(mockUserId);
        DeckService mockDeckService = Mockito.mock(DeckService.class);
        when(mockMainActivity.getDeckService()).thenReturn(mockDeckService);

        CombatService testedService = new CombatService(mockMainActivity);
        testedService.initializeCombat();

        Deck testUserDeck = new Deck(mockUserId,"userDeck");
        List<Card> userCards = new ArrayList<>();

        for(int i=0; i<4; i++) {
            ItemCard testItem1 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "buffItem", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 2));
            userCards.add(testItem1);
        }
        CharacterCard characterCard1 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Zach","Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.ICE, AttackType.MELEE,1),20);
        userCards.add(characterCard1);
        CharacterCard characterCard2 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Zach2","Zach is a software engineer",new HealAbility("AllyHeal","slap", AbilityTarget.SINGLE_ALLY,2),25);
        userCards.add(characterCard2);
        testUserDeck.setCards(userCards);

        List<Card> encounterCards = new ArrayList<>();

        CharacterCard encounterCharacterCard1 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Dark Zach","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.MELEE,1),5);
        encounterCards.add(encounterCharacterCard1);
        CharacterCard encounterCharacterCard2 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Dark Zach 2","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.MELEE,2),6);
        encounterCards.add(encounterCharacterCard2);
        CharacterCard encounterCharacterCard3 = new CharacterCard(mockUserId, UUID.randomUUID().toString(),"Dark Ranged Zach","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.RANGED,2),7);
        encounterCharacterCard3.addAbility(new HealAbility("test", "test", AbilityTarget.SINGLE_ALLY,  5));
        encounterCards.add(encounterCharacterCard3);
        ItemCard testItem1 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
        encounterCards.add(testItem1);
        ItemCard testItem2 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
        encounterCards.add(testItem2);
        ItemCard testItem3 = new ItemCard(mockUserId, UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 3));
        encounterCards.add(testItem3);

        Encounter testEncounter = new Encounter("testEncounter", EncounterDifficultyLevel.EASY, encounterCards);

        testedService.encounterFetchReturned(testEncounter);
        testedService.deckFetchReturned(testUserDeck);

        ArgumentCaptor<CombatStateUpdateEvent> combatStateUpdateEventArgumentCaptor = ArgumentCaptor.forClass(CombatStateUpdateEvent.class);
        verify(mockMainActivity,atLeastOnce()).publishEvent(combatStateUpdateEventArgumentCaptor.capture());
        assertNotNull(combatStateUpdateEventArgumentCaptor.getValue());
        CombatCardModel zach1 = null;
        CombatCardModel zach2 = null;
        for(CombatCardModel cardModel: combatStateUpdateEventArgumentCaptor.getValue().getCombatStateModel().getPlayerHand()){
            if(cardModel.getCardName().equals(characterCard1.getCardName())){
                zach1 = cardModel;
            } else if(cardModel.getCardName().equals(characterCard2.getCardName())){
                zach2 = cardModel;
            }
        }

        testedService.cardDroppedOnLine(zach1,CombatLine.PLAYER_BACK_LINE);
        testedService.cardDroppedOnLine(zach2,CombatLine.PLAYER_BACK_LINE);


        CombatCardModel darkZach = null;
        CombatCardModel darkZach2 = null;
        CombatCardModel darkRangedZach = null;
        List<CombatCardModel> allEnemyCards = new ArrayList<>();
        allEnemyCards.addAll(combatStateUpdateEventArgumentCaptor.getValue().getCombatStateModel().getEnemyFrontLine());
        allEnemyCards.addAll(combatStateUpdateEventArgumentCaptor.getValue().getCombatStateModel().getEnemyBackLine());
        for(CombatCardModel cardModel: allEnemyCards){
            if(cardModel.getCardName().equals("Dark Zach")){
                darkZach = cardModel;
            } else if(cardModel.getCardName().equals("Dark Zach 2")){
                darkZach2 = cardModel;
            }else if(cardModel.getCardName().equals("Dark Ranged Zach")){
                darkRangedZach = cardModel;
            }

        }

        darkZach.setCurrentHealth(1);

        if(testedService.isPlayerTurn()){
            testedService.endPlayerTurn();
        }

        assertEquals("Dark Zach Should be healed with ranged zach'a ability", 6, darkZach.getCurrentHealth());
        assertEquals("Zach should only be targeted by 2 attacks", 17, zach1.getCurrentHealth());
    }

    private boolean lineContains(List<CombatCardModel> cardsInLine, Card cardToCheck){
        for(CombatCardModel combatCardModel: cardsInLine){
            if(combatCardModel.getCardName().equals(cardToCheck.getCardName())){
                return true;
            }
        }

        return false;
    }
}
