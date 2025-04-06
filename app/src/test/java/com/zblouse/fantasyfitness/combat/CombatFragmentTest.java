package com.zblouse.fantasyfitness.combat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

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
import com.zblouse.fantasyfitness.combat.cards.HealAbility;
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
}
