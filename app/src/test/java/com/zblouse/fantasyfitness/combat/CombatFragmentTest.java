package com.zblouse.fantasyfitness.combat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.zblouse.fantasyfitness.R;
import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.combat.cards.Ability;
import com.zblouse.fantasyfitness.combat.cards.AbilityTarget;
import com.zblouse.fantasyfitness.combat.cards.AttackType;
import com.zblouse.fantasyfitness.combat.cards.BuffAbility;
import com.zblouse.fantasyfitness.combat.cards.BuffType;
import com.zblouse.fantasyfitness.combat.cards.CardType;
import com.zblouse.fantasyfitness.combat.cards.DamageAbility;
import com.zblouse.fantasyfitness.combat.cards.DamageType;
import com.zblouse.fantasyfitness.combat.cards.Deck;
import com.zblouse.fantasyfitness.combat.cards.DeckFetchEvent;
import com.zblouse.fantasyfitness.combat.cards.HealAbility;
import com.zblouse.fantasyfitness.combat.encounter.Encounter;
import com.zblouse.fantasyfitness.combat.encounter.EncounterDifficultyLevel;
import com.zblouse.fantasyfitness.combat.encounter.EncounterFetchEvent;
import com.zblouse.fantasyfitness.user.UserGameStateService;
import com.zblouse.fantasyfitness.user.UserService;
import com.zblouse.fantasyfitness.world.GameWorldFragment;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@RunWith(RobolectricTestRunner.class)
public class CombatFragmentTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);

    @Test
    public void onCreateTest() {
        UserService mockUserService = Mockito.mock(UserService.class);
        CombatService mockCombatService = Mockito.mock(CombatService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setCombatService(mockCombatService);
        mainActivity.setUserService(mockUserService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        CombatFragment testedFragment = new CombatFragment(mainActivity, "testEncounter");
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        assertEquals(View.GONE,returnedView.findViewById(R.id.victory_screen).getVisibility());
        assertEquals(View.GONE,returnedView.findViewById(R.id.detailed_card).getVisibility());
        assertNotNull(returnedView.findViewById(R.id.enemyHand));
        assertNotNull(returnedView.findViewById(R.id.enemyBackLine));
        assertNotNull(returnedView.findViewById(R.id.enemyFrontLine));
        assertNotNull(returnedView.findViewById(R.id.playerFrontLine));
        assertNotNull(returnedView.findViewById(R.id.playerBackLine));
        assertNotNull(returnedView.findViewById(R.id.playerHand));
        assertNotNull(returnedView.findViewById(R.id.end_turn_button));
    }

    @Test
    public void initialCombatStateTest() {
        UserService mockUserService = Mockito.mock(UserService.class);
        CombatService mockCombatService = Mockito.mock(CombatService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setCombatService(mockCombatService);
        mainActivity.setUserService(mockUserService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        CombatFragment testedFragment = new CombatFragment(mainActivity, "testEncounter");
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        assertEquals(View.GONE,returnedView.findViewById(R.id.victory_screen).getVisibility());
        assertEquals(View.GONE,returnedView.findViewById(R.id.detailed_card).getVisibility());
        assertNotNull(returnedView.findViewById(R.id.enemyHand));
        assertNotNull(returnedView.findViewById(R.id.enemyBackLine));
        assertNotNull(returnedView.findViewById(R.id.enemyFrontLine));
        assertNotNull(returnedView.findViewById(R.id.playerFrontLine));
        assertNotNull(returnedView.findViewById(R.id.playerBackLine));
        assertNotNull(returnedView.findViewById(R.id.playerHand));
        assertNotNull(returnedView.findViewById(R.id.end_turn_button));

        CombatStateModel combatStateModel = new CombatStateModel();

        CombatCardModel zachCardModel = new CombatCardModel("zach","zach", CardType.CHARACTER,true,false);
        zachCardModel.setMaxHealth(10);
        zachCardModel.setCurrentHealth(10);
        List<Ability> zachAbilities = new ArrayList<>();
        Ability damageAbility = new DamageAbility("testDamage", "test", AbilityTarget.SINGLE_ENEMY, DamageType.ICE, AttackType.MELEE,1);
        Ability healAbility = new HealAbility("testHeal", "test", AbilityTarget.SINGLE_ALLY, 1);
        zachAbilities.add(healAbility);
        zachAbilities.add(damageAbility);
        zachCardModel.setAbilities(zachAbilities);
        CombatCardModel healItemCardModel = new CombatCardModel("heal","zach", CardType.ITEM,true,false);
        Ability itemHealAbility = new HealAbility("testHeal", "test", AbilityTarget.ROW_ALLY, 1);
        healItemCardModel.setAbility(itemHealAbility);
        combatStateModel.initialPlayerHand(Arrays.asList(zachCardModel,healItemCardModel));

        CombatCardModel darkZachCardModel = new CombatCardModel("dark zach","dark zach", CardType.CHARACTER,false,false);
        darkZachCardModel.setMaxHealth(10);
        darkZachCardModel.setCurrentHealth(10);
        List<Ability> darkZachAbilities = new ArrayList<>();
        Ability darkDamageAbility = new DamageAbility("testDamage", "test", AbilityTarget.ROW_ENEMY, DamageType.NORMAL, AttackType.RANGED,1);
        Ability darkZealAbility = new HealAbility("testHeal", "test", AbilityTarget.ALL_ALLY, 1);
        darkZachAbilities.add(darkDamageAbility);
        darkZachAbilities.add(darkZealAbility);
        darkZachCardModel.setAbilities(darkZachAbilities);
        CombatCardModel buffItemCardModel = new CombatCardModel("heal","zach", CardType.ITEM,true,false);
        Ability darkBuffHealAbility = new BuffAbility("testBuff", "test", AbilityTarget.ROW_ALLY, BuffType.HEALTH, 2);
        buffItemCardModel.setAbility(darkBuffHealAbility);
        combatStateModel.initialEnemyHand(Arrays.asList(darkZachCardModel,buffItemCardModel));

        CombatStateUpdateEvent initialCombatStateEvent = new CombatStateUpdateEvent(combatStateModel, new HashMap<>());

        testedFragment.publishEvent(initialCombatStateEvent);

        RecyclerView playerHand = (RecyclerView)returnedView.findViewById(R.id.playerHand);
        assertEquals(2, playerHand.getAdapter().getItemCount());
    }

    @Test
    public void attemptCardAbilityTargetTest() {
        UserService mockUserService = Mockito.mock(UserService.class);
        CombatService mockCombatService = Mockito.mock(CombatService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setCombatService(mockCombatService);
        mainActivity.setUserService(mockUserService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        CombatFragment testedFragment = new CombatFragment(mainActivity, "testEncounter");
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        CombatCardModel testCardModel = new CombatCardModel("testCard", "testDescription", CardType.CHARACTER, false, true);
        testedFragment.attemptCardAbilityTarget(testCardModel);

        verify(mockCombatService).attemptCardAbilityTarget(eq(testCardModel));
    }

    @Test
    public void setupCompleteTest() {
        UserService mockUserService = Mockito.mock(UserService.class);
        CombatService mockCombatService = Mockito.mock(CombatService.class);
        when(mockCombatService.endSetup()).thenReturn(true);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setCombatService(mockCombatService);
        mainActivity.setUserService(mockUserService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        CombatFragment testedFragment = new CombatFragment(mainActivity, "testEncounter");
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        returnedView.findViewById(R.id.end_turn_button).performClick();
        verify(mockCombatService).endSetup();

        assertEquals("End Turn",((Button)returnedView.findViewById(R.id.end_turn_button)).getText());
    }

    @Test
    public void reportLineDropTest() {
        UserService mockUserService = Mockito.mock(UserService.class);
        CombatService mockCombatService = Mockito.mock(CombatService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setCombatService(mockCombatService);
        mainActivity.setUserService(mockUserService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        CombatFragment testedFragment = new CombatFragment(mainActivity, "testEncounter");
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);
        CombatCardModel combatCardModel1 = new CombatCardModel("test1","test",CardType.CHARACTER,true,false);
        testedFragment.reportLineDrop(combatCardModel1, CombatLine.PLAYER_BACK_LINE);
        verify(mockCombatService).cardDroppedOnLine(eq(combatCardModel1), eq(CombatLine.PLAYER_BACK_LINE));

    }

    @Test
    public void reportCardDropTest() {
        UserService mockUserService = Mockito.mock(UserService.class);
        CombatService mockCombatService = Mockito.mock(CombatService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setCombatService(mockCombatService);
        mainActivity.setUserService(mockUserService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        CombatFragment testedFragment = new CombatFragment(mainActivity, "testEncounter");
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);
        CombatCardModel combatCardModel1 = new CombatCardModel("test1","test",CardType.CHARACTER,true,false);
        CombatCardModel combatCardModel2 = new CombatCardModel("test2","test",CardType.CHARACTER,true,true);
        testedFragment.reportCardDrop(combatCardModel1, combatCardModel2);
        verify(mockCombatService).cardDroppedOnCard(eq(combatCardModel1), eq(combatCardModel2));

    }

    @Test
    public void isWaitingForAbilityTargetingTest() {
        UserService mockUserService = Mockito.mock(UserService.class);
        CombatService mockCombatService = Mockito.mock(CombatService.class);
        when(mockCombatService.isWaitingForAbilityTargeting()).thenReturn(true);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setCombatService(mockCombatService);
        mainActivity.setUserService(mockUserService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        CombatFragment testedFragment = new CombatFragment(mainActivity, "testEncounter");
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        boolean result = testedFragment.isWaitingForAbilityTargeting();
        assertTrue(result);

    }

    @Test
    public void endPlayerTurnTest() {
        UserService mockUserService = Mockito.mock(UserService.class);
        CombatService mockCombatService = Mockito.mock(CombatService.class);
        when(mockCombatService.endSetup()).thenReturn(true);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setCombatService(mockCombatService);
        mainActivity.setUserService(mockUserService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        CombatFragment testedFragment = new CombatFragment(mainActivity, "testEncounter");
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        returnedView.findViewById(R.id.end_turn_button).performClick();
        verify(mockCombatService).endSetup();

        returnedView.findViewById(R.id.end_turn_button).performClick();
        verify(mockCombatService).endPlayerTurn();

    }

    @Test
    public void deckFetchEventTest() {
        UserService mockUserService = Mockito.mock(UserService.class);
        CombatService mockCombatService = Mockito.mock(CombatService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setCombatService(mockCombatService);
        mainActivity.setUserService(mockUserService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        CombatFragment testedFragment = new CombatFragment(mainActivity, "testEncounter");
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        Deck deck = new Deck("userId", "testDeck");

        DeckFetchEvent deckFetchEvent = new DeckFetchEvent(deck, new HashMap<>());

        testedFragment.publishEvent(deckFetchEvent);
        verify(mockCombatService).deckFetchReturned(eq(deck));

    }

    @Test
    public void encounterFetchEventTest() {
        UserService mockUserService = Mockito.mock(UserService.class);
        CombatService mockCombatService = Mockito.mock(CombatService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setCombatService(mockCombatService);
        mainActivity.setUserService(mockUserService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        CombatFragment testedFragment = new CombatFragment(mainActivity, "testEncounter");
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        Encounter encounter = new Encounter("testEncounter", EncounterDifficultyLevel.EASY, new ArrayList<>());

        EncounterFetchEvent encounterFetchEvent = new EncounterFetchEvent(encounter, new HashMap<>());

        testedFragment.publishEvent(encounterFetchEvent);
        verify(mockCombatService).encounterFetchReturned(eq(encounter));

    }

    @Test
    public void startPlayerTurnEventTest() {
        UserService mockUserService = Mockito.mock(UserService.class);
        CombatService mockCombatService = Mockito.mock(CombatService.class);
        when(mockCombatService.endSetup()).thenReturn(false);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setCombatService(mockCombatService);
        mainActivity.setUserService(mockUserService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        CombatFragment testedFragment = new CombatFragment(mainActivity, "testEncounter");
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        returnedView.findViewById(R.id.end_turn_button).performClick();
        verify(mockCombatService).endSetup();
        assertFalse(((Button)returnedView.findViewById(R.id.end_turn_button)).isClickable());

        EnemyTurnCompleteEvent enemyTurnCompleteEvent = new EnemyTurnCompleteEvent();

        testedFragment.publishEvent(enemyTurnCompleteEvent);
        assertTrue(((Button)returnedView.findViewById(R.id.end_turn_button)).isClickable());
    }

    @Test
    public void playerVictoryEventTest() {
        UserService mockUserService = Mockito.mock(UserService.class);
        CombatService mockCombatService = Mockito.mock(CombatService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setCombatService(mockCombatService);
        mainActivity.setUserService(mockUserService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        CombatFragment testedFragment = new CombatFragment(mainActivity, "testEncounter");
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        PlayerVictoryEvent playerVictoryEvent = new PlayerVictoryEvent();

        testedFragment.publishEvent(playerVictoryEvent);

        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.victory_screen).getVisibility());
        assertEquals("VICTORY", ((TextView)returnedView.findViewById(R.id.victory_screen_text)).getText());

    }

    @Test
    public void enemyVictoryEventTest() {
        UserService mockUserService = Mockito.mock(UserService.class);
        CombatService mockCombatService = Mockito.mock(CombatService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setCombatService(mockCombatService);
        mainActivity.setUserService(mockUserService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        CombatFragment testedFragment = new CombatFragment(mainActivity, "testEncounter");
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        EnemyVictoryEvent enemyVictoryEvent = new EnemyVictoryEvent();

        testedFragment.publishEvent(enemyVictoryEvent);

        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.victory_screen).getVisibility());
        assertEquals("DEFEAT", ((TextView)returnedView.findViewById(R.id.victory_screen_text)).getText());

    }
}
