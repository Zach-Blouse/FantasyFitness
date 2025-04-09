package com.zblouse.fantasyfitness.combat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.zblouse.fantasyfitness.R;
import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.combat.cards.AbilityTarget;
import com.zblouse.fantasyfitness.combat.cards.AttackType;
import com.zblouse.fantasyfitness.combat.cards.BuffAbility;
import com.zblouse.fantasyfitness.combat.cards.BuffType;
import com.zblouse.fantasyfitness.combat.cards.CardType;
import com.zblouse.fantasyfitness.combat.cards.DamageAbility;
import com.zblouse.fantasyfitness.combat.cards.DamageType;
import com.zblouse.fantasyfitness.combat.cards.HealAbility;
import com.zblouse.fantasyfitness.user.UserService;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.Arrays;

@RunWith(RobolectricTestRunner.class)
public class AbilityViewAdapterTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);

    @Test
    public void onCreateViewHolderTest(){
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

        CombatCardModel testModel = new CombatCardModel("TestCard","Card", CardType.CHARACTER,true,true);
        DamageAbility damageAbility = new DamageAbility("test", "test", AbilityTarget.SINGLE_ENEMY, DamageType.ELECTRIC, AttackType.MELEE,1);
        testModel.setAbilities(Arrays.asList(damageAbility));
        testModel.setMaxHealth(10);
        testModel.setCurrentHealth(10);

        AbilityViewAdapter abilityViewAdapter = new AbilityViewAdapter(testModel,testedFragment,false);
        AbilityViewAdapter.ViewHolder viewHolder = abilityViewAdapter.onCreateViewHolder(returnedView.findViewById(R.id.playerFrontLine),1);
        assertNotNull(viewHolder.abilityCard);
        assertNotNull(viewHolder.abilityImpactTextView);
        assertNotNull(viewHolder.abilityNameTextView);

    }

    @Test
    public void onCreateViewHolderMultipleAbilitiesTest(){
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

        CombatCardModel testModel = new CombatCardModel("TestCard","Card", CardType.CHARACTER,true,true);
        DamageAbility damageAbility = new DamageAbility("test", "test", AbilityTarget.SINGLE_ENEMY, DamageType.ELECTRIC, AttackType.MELEE,1);
        HealAbility healAbility = new HealAbility("test", "test", AbilityTarget.ALL_ALLY,1);
        testModel.setAbilities(Arrays.asList(damageAbility, healAbility));
        testModel.setMaxHealth(10);
        testModel.setCurrentHealth(10);

        AbilityViewAdapter abilityViewAdapter = new AbilityViewAdapter(testModel,testedFragment,false);

        assertEquals(2, abilityViewAdapter.getItemCount());

    }

    @Test
    public void onBindViewHolderTest(){
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

        CombatCardModel testModel = new CombatCardModel("TestCard","Card", CardType.CHARACTER,true,true);
        DamageAbility damageAbility = new DamageAbility("test", "test", AbilityTarget.SINGLE_ENEMY, DamageType.ELECTRIC, AttackType.MELEE,1);
        testModel.setAbilities(Arrays.asList(damageAbility));
        testModel.setMaxHealth(10);
        testModel.setCurrentHealth(10);

        AbilityViewAdapter abilityViewAdapter = new AbilityViewAdapter(testModel,testedFragment,false);
        AbilityViewAdapter.ViewHolder viewHolder = abilityViewAdapter.onCreateViewHolder(returnedView.findViewById(R.id.playerFrontLine),1);

        abilityViewAdapter.onBindViewHolder(viewHolder,0);

        assertEquals(View.VISIBLE, viewHolder.abilityCard.getVisibility());
        assertEquals(damageAbility.getAbilityName(),viewHolder.abilityNameTextView.getText());

    }

    @Test
    public void onBindViewHolderRowEnemyTest(){
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

        CombatCardModel testModel = new CombatCardModel("TestCard","Card", CardType.CHARACTER,true,true);
        DamageAbility damageAbility = new DamageAbility("test", "test", AbilityTarget.ROW_ENEMY, DamageType.ELECTRIC, AttackType.MELEE,1);
        testModel.setAbilities(Arrays.asList(damageAbility));
        testModel.setMaxHealth(10);
        testModel.setCurrentHealth(10);

        AbilityViewAdapter abilityViewAdapter = new AbilityViewAdapter(testModel,testedFragment,false);
        AbilityViewAdapter.ViewHolder viewHolder = abilityViewAdapter.onCreateViewHolder(returnedView.findViewById(R.id.playerFrontLine),1);

        abilityViewAdapter.onBindViewHolder(viewHolder,0);

        assertEquals(View.VISIBLE, viewHolder.abilityCard.getVisibility());
        assertEquals(damageAbility.getAbilityName(),viewHolder.abilityNameTextView.getText());
        assertEquals("Does 1 electric damage  to a full line of your opponent's cards",viewHolder.abilityImpactTextView.getText());

    }

    @Test
    public void onBindViewHolderRowEnemyPlayedDetailViewTest(){
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

        CombatCardModel testModel = new CombatCardModel("TestCard","Card", CardType.CHARACTER,true,true);
        DamageAbility damageAbility = new DamageAbility("test", "test", AbilityTarget.ROW_ENEMY, DamageType.ELECTRIC, AttackType.MELEE,1);
        testModel.setAbilities(Arrays.asList(damageAbility));
        testModel.setMaxHealth(10);
        testModel.setCurrentHealth(10);

        AbilityViewAdapter abilityViewAdapter = new AbilityViewAdapter(testModel,testedFragment,true);
        AbilityViewAdapter.ViewHolder viewHolder = abilityViewAdapter.onCreateViewHolder(returnedView.findViewById(R.id.playerFrontLine),1);

        abilityViewAdapter.onBindViewHolder(viewHolder,0);

        assertEquals(View.VISIBLE, viewHolder.abilityCard.getVisibility());
        assertEquals(damageAbility.getAbilityName(),viewHolder.abilityNameTextView.getText());
        assertEquals("Does 1 electric damage  to a full line of your opponent's cards",viewHolder.abilityImpactTextView.getText());

        viewHolder.abilityCard.performClick();
        verify(mockCombatService).abilityUsed(eq(testModel),eq(damageAbility));
    }

    @Test
    public void onBindViewHolderAllEnemyTest(){
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

        CombatCardModel testModel = new CombatCardModel("TestCard","Card", CardType.CHARACTER,true,true);
        DamageAbility damageAbility = new DamageAbility("test", "test", AbilityTarget.ALL_ENEMY, DamageType.ELECTRIC, AttackType.MELEE,1);
        testModel.setAbilities(Arrays.asList(damageAbility));
        testModel.setMaxHealth(10);
        testModel.setCurrentHealth(10);

        AbilityViewAdapter abilityViewAdapter = new AbilityViewAdapter(testModel,testedFragment,false);
        AbilityViewAdapter.ViewHolder viewHolder = abilityViewAdapter.onCreateViewHolder(returnedView.findViewById(R.id.playerFrontLine),1);

        abilityViewAdapter.onBindViewHolder(viewHolder,0);

        assertEquals(View.VISIBLE, viewHolder.abilityCard.getVisibility());
        assertEquals(damageAbility.getAbilityName(),viewHolder.abilityNameTextView.getText());
        assertEquals("Does 1 electric damage  to all of your opponent's cards",viewHolder.abilityImpactTextView.getText());

    }

    @Test
    public void onBindViewHolderSingleAllyTest(){
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

        CombatCardModel testModel = new CombatCardModel("TestCard","Card", CardType.CHARACTER,true,true);
        HealAbility healAbility = new HealAbility("test", "test", AbilityTarget.SINGLE_ALLY,1);
        testModel.setAbilities(Arrays.asList(healAbility));
        testModel.setMaxHealth(10);
        testModel.setCurrentHealth(10);

        AbilityViewAdapter abilityViewAdapter = new AbilityViewAdapter(testModel,testedFragment,false);
        AbilityViewAdapter.ViewHolder viewHolder = abilityViewAdapter.onCreateViewHolder(returnedView.findViewById(R.id.playerFrontLine),1);

        abilityViewAdapter.onBindViewHolder(viewHolder,0);

        assertEquals(View.VISIBLE, viewHolder.abilityCard.getVisibility());
        assertEquals(healAbility.getAbilityName(),viewHolder.abilityNameTextView.getText());
        assertEquals("Heals 1 HP to a single one of your cards",viewHolder.abilityImpactTextView.getText());
    }

    @Test
    public void onBindViewHolderRowAllyTest(){
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

        CombatCardModel testModel = new CombatCardModel("TestCard","Card", CardType.CHARACTER,true,true);
        HealAbility healAbility = new HealAbility("test", "test", AbilityTarget.ROW_ALLY,1);
        testModel.setAbilities(Arrays.asList(healAbility));
        testModel.setMaxHealth(10);
        testModel.setCurrentHealth(10);

        AbilityViewAdapter abilityViewAdapter = new AbilityViewAdapter(testModel,testedFragment,false);
        AbilityViewAdapter.ViewHolder viewHolder = abilityViewAdapter.onCreateViewHolder(returnedView.findViewById(R.id.playerFrontLine),1);

        abilityViewAdapter.onBindViewHolder(viewHolder,0);

        assertEquals(View.VISIBLE, viewHolder.abilityCard.getVisibility());
        assertEquals(healAbility.getAbilityName(),viewHolder.abilityNameTextView.getText());
        assertEquals("Heals 1 HP to a full line of your cards",viewHolder.abilityImpactTextView.getText());
    }

    @Test
    public void onBindViewHolderAllAllyTest(){
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

        CombatCardModel testModel = new CombatCardModel("TestCard","Card", CardType.CHARACTER,true,true);
        HealAbility healAbility = new HealAbility("test", "test", AbilityTarget.ALL_ALLY,1);
        testModel.setAbilities(Arrays.asList(healAbility));
        testModel.setMaxHealth(10);
        testModel.setCurrentHealth(10);

        AbilityViewAdapter abilityViewAdapter = new AbilityViewAdapter(testModel,testedFragment,false);
        AbilityViewAdapter.ViewHolder viewHolder = abilityViewAdapter.onCreateViewHolder(returnedView.findViewById(R.id.playerFrontLine),1);

        abilityViewAdapter.onBindViewHolder(viewHolder,0);

        assertEquals(View.VISIBLE, viewHolder.abilityCard.getVisibility());
        assertEquals(healAbility.getAbilityName(),viewHolder.abilityNameTextView.getText());
        assertEquals("Heals 1 HP to all of your cards",viewHolder.abilityImpactTextView.getText());
    }

    @Test
    public void onBindViewHolderAllAllyNotPlayedTest(){
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

        CombatCardModel testModel = new CombatCardModel("TestCard","Card", CardType.CHARACTER,true,false);
        HealAbility healAbility = new HealAbility("test", "test", AbilityTarget.ALL_ALLY,1);
        testModel.setAbilities(Arrays.asList(healAbility));
        testModel.setMaxHealth(10);
        testModel.setCurrentHealth(10);

        AbilityViewAdapter abilityViewAdapter = new AbilityViewAdapter(testModel,testedFragment,true);
        AbilityViewAdapter.ViewHolder viewHolder = abilityViewAdapter.onCreateViewHolder(returnedView.findViewById(R.id.playerFrontLine),1);

        abilityViewAdapter.onBindViewHolder(viewHolder,0);

        assertEquals(View.VISIBLE, viewHolder.abilityCard.getVisibility());
        assertEquals(healAbility.getAbilityName(),viewHolder.abilityNameTextView.getText());
        assertEquals("Heals 1 HP to all of your cards",viewHolder.abilityImpactTextView.getText());
    }

    @Test
    public void onBindViewHolderSelfNotPlayedTest(){
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

        CombatCardModel testModel = new CombatCardModel("TestCard","Card", CardType.CHARACTER,true,false);
        HealAbility healAbility = new HealAbility("test", "test", AbilityTarget.SELF,1);
        testModel.setAbilities(Arrays.asList(healAbility));
        testModel.setMaxHealth(10);
        testModel.setCurrentHealth(10);

        AbilityViewAdapter abilityViewAdapter = new AbilityViewAdapter(testModel,testedFragment,true);
        AbilityViewAdapter.ViewHolder viewHolder = abilityViewAdapter.onCreateViewHolder(returnedView.findViewById(R.id.playerFrontLine),1);

        abilityViewAdapter.onBindViewHolder(viewHolder,0);

        assertEquals(View.VISIBLE, viewHolder.abilityCard.getVisibility());
        assertEquals(healAbility.getAbilityName(),viewHolder.abilityNameTextView.getText());
        assertEquals("Heals 1 HP to this card",viewHolder.abilityImpactTextView.getText());
    }

    @Test
    public void onBindViewHolderAllEnemyNotPlayedTest(){
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

        CombatCardModel testModel = new CombatCardModel("TestCard","Card", CardType.CHARACTER,true,true);
        DamageAbility damageAbility = new DamageAbility("test", "test", AbilityTarget.ALL_ENEMY, DamageType.ELECTRIC, AttackType.MELEE,1);
        testModel.setAbilities(Arrays.asList(damageAbility));
        testModel.setMaxHealth(10);
        testModel.setCurrentHealth(10);

        AbilityViewAdapter abilityViewAdapter = new AbilityViewAdapter(testModel,testedFragment,false);
        AbilityViewAdapter.ViewHolder viewHolder = abilityViewAdapter.onCreateViewHolder(returnedView.findViewById(R.id.playerFrontLine),1);

        abilityViewAdapter.onBindViewHolder(viewHolder,0);

        assertEquals(View.VISIBLE, viewHolder.abilityCard.getVisibility());
        assertEquals(damageAbility.getAbilityName(),viewHolder.abilityNameTextView.getText());
        assertEquals("Does 1 electric damage  to all of your opponent's cards",viewHolder.abilityImpactTextView.getText());

    }

    @Test
    public void onBindViewHolderDamageItemAllEnemyTest(){
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

        CombatCardModel testModel = new CombatCardModel("TestCard","Card", CardType.ITEM,true,false);
        DamageAbility damageAbility = new DamageAbility("test", "test", AbilityTarget.ALL_ENEMY, DamageType.ELECTRIC, AttackType.MELEE,1);
        testModel.setAbility(damageAbility);

        AbilityViewAdapter abilityViewAdapter = new AbilityViewAdapter(testModel,testedFragment,false);
        AbilityViewAdapter.ViewHolder viewHolder = abilityViewAdapter.onCreateViewHolder(returnedView.findViewById(R.id.playerFrontLine),1);

        abilityViewAdapter.onBindViewHolder(viewHolder,0);

        assertEquals(View.VISIBLE, viewHolder.abilityCard.getVisibility());
        assertEquals("Gives the card this is attached to the ability to make a melee attack for 1 electric damage to all enemies",viewHolder.abilityImpactTextView.getText());

    }

    @Test
    public void onBindViewHolderDamageItemRowEnemyTest(){
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

        CombatCardModel testModel = new CombatCardModel("TestCard","Card", CardType.ITEM,true,false);
        DamageAbility damageAbility = new DamageAbility("test", "test", AbilityTarget.ROW_ENEMY, DamageType.ELECTRIC, AttackType.MELEE,1);
        testModel.setAbility(damageAbility);

        AbilityViewAdapter abilityViewAdapter = new AbilityViewAdapter(testModel,testedFragment,false);
        AbilityViewAdapter.ViewHolder viewHolder = abilityViewAdapter.onCreateViewHolder(returnedView.findViewById(R.id.playerFrontLine),1);

        abilityViewAdapter.onBindViewHolder(viewHolder,0);

        assertEquals(View.VISIBLE, viewHolder.abilityCard.getVisibility());
        assertEquals("Gives the card this is attached to the ability to make a melee attack for 1 electric damage to a line of enemies",viewHolder.abilityImpactTextView.getText());

    }

    @Test
    public void onBindViewHolderDamageItemSingleEnemyTest(){
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

        CombatCardModel testModel = new CombatCardModel("TestCard","Card", CardType.ITEM,true,false);
        DamageAbility damageAbility = new DamageAbility("test", "test", AbilityTarget.SINGLE_ENEMY, DamageType.ELECTRIC, AttackType.MELEE,1);
        testModel.setAbility(damageAbility);

        AbilityViewAdapter abilityViewAdapter = new AbilityViewAdapter(testModel,testedFragment,false);
        AbilityViewAdapter.ViewHolder viewHolder = abilityViewAdapter.onCreateViewHolder(returnedView.findViewById(R.id.playerFrontLine),1);

        abilityViewAdapter.onBindViewHolder(viewHolder,0);

        assertEquals(View.VISIBLE, viewHolder.abilityCard.getVisibility());
        assertEquals("Gives the card this is attached to the ability to make a melee attack for 1 electric damage to one enemy",viewHolder.abilityImpactTextView.getText());

    }

    @Test
    public void onBindViewHolderHealItemSingleAllyTest(){
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

        CombatCardModel testModel = new CombatCardModel("TestCard","Card", CardType.ITEM,true,false);
        HealAbility healAbility = new HealAbility("test", "test", AbilityTarget.SINGLE_ALLY,1);
        testModel.setAbility(healAbility);

        AbilityViewAdapter abilityViewAdapter = new AbilityViewAdapter(testModel,testedFragment,false);
        AbilityViewAdapter.ViewHolder viewHolder = abilityViewAdapter.onCreateViewHolder(returnedView.findViewById(R.id.playerFrontLine),1);

        abilityViewAdapter.onBindViewHolder(viewHolder,0);

        assertEquals(View.VISIBLE, viewHolder.abilityCard.getVisibility());
        assertEquals("Heals 1 HP to a single one of your cards",viewHolder.abilityImpactTextView.getText());

    }

    @Test
    public void onBindViewHolderBuffItemSingleAllyTest(){
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

        CombatCardModel testModel = new CombatCardModel("TestCard","Card", CardType.ITEM,true,false);
        BuffAbility buffAbility = new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.ATTACK,1);
        testModel.setAbility(buffAbility);

        AbilityViewAdapter abilityViewAdapter = new AbilityViewAdapter(testModel,testedFragment,false);
        AbilityViewAdapter.ViewHolder viewHolder = abilityViewAdapter.onCreateViewHolder(returnedView.findViewById(R.id.playerFrontLine),1);

        abilityViewAdapter.onBindViewHolder(viewHolder,0);

        assertEquals(View.VISIBLE, viewHolder.abilityCard.getVisibility());
        assertEquals("Gives the card this is attached to 1 extra damage on each attack",viewHolder.abilityImpactTextView.getText());

    }

    @Test
    public void onBindViewHolderBuffItemHealthSingleAllyTest(){
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

        CombatCardModel testModel = new CombatCardModel("TestCard","Card", CardType.ITEM,true,false);
        BuffAbility buffAbility = new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH,1);
        testModel.setAbility(buffAbility);

        AbilityViewAdapter abilityViewAdapter = new AbilityViewAdapter(testModel,testedFragment,false);
        AbilityViewAdapter.ViewHolder viewHolder = abilityViewAdapter.onCreateViewHolder(returnedView.findViewById(R.id.playerFrontLine),1);

        abilityViewAdapter.onBindViewHolder(viewHolder,0);

        assertEquals(View.VISIBLE, viewHolder.abilityCard.getVisibility());
        assertEquals("Gives the card this is attached to 1 extra max HP",viewHolder.abilityImpactTextView.getText());

    }

    @Test
    public void onBindViewHolderBuffItemHealthSingleAllyDetailedTest(){
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

        CombatCardModel testModel = new CombatCardModel("TestCard","Card", CardType.ITEM,true,false);
        BuffAbility buffAbility = new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH,1);
        testModel.setAbility(buffAbility);

        AbilityViewAdapter abilityViewAdapter = new AbilityViewAdapter(testModel,testedFragment,true);
        AbilityViewAdapter.ViewHolder viewHolder = abilityViewAdapter.onCreateViewHolder(returnedView.findViewById(R.id.playerFrontLine),1);

        abilityViewAdapter.onBindViewHolder(viewHolder,0);

        assertEquals(View.GONE, viewHolder.abilityCard.getVisibility());

    }
}
