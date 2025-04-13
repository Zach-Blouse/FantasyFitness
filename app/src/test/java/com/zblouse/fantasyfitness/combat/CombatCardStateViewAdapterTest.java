package com.zblouse.fantasyfitness.combat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.zblouse.fantasyfitness.R;
import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.combat.cards.AbilityTarget;
import com.zblouse.fantasyfitness.combat.cards.AttackType;
import com.zblouse.fantasyfitness.combat.cards.CardType;
import com.zblouse.fantasyfitness.combat.cards.DamageAbility;
import com.zblouse.fantasyfitness.combat.cards.DamageType;
import com.zblouse.fantasyfitness.user.UserService;
import com.zblouse.fantasyfitness.world.GameLocationService;

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
public class CombatCardStateViewAdapterTest {

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
        CombatFragment testedFragment = new CombatFragment(mainActivity, "testEncounter", GameLocationService.WOODLANDS,R.id.cave_button);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        CombatCardModel testModel = new CombatCardModel("TestCard","Card", CardType.CHARACTER,true,true);
        DamageAbility damageAbility = new DamageAbility("test", "test", AbilityTarget.SINGLE_ENEMY, DamageType.ELECTRIC, AttackType.MELEE,1);
        testModel.setAbilities(Arrays.asList(damageAbility));
        testModel.setMaxHealth(10);
        testModel.setCurrentHealth(10);

        CombatCardStateViewAdapter combatCardStateViewAdapter = new CombatCardStateViewAdapter(Arrays.asList(testModel),true, testedFragment);
        CombatCardStateViewAdapter.ViewHolder viewHolder = combatCardStateViewAdapter.onCreateViewHolder(returnedView.findViewById(R.id.playerFrontLine),1);
        assertNotNull(viewHolder.card);
        assertNotNull(viewHolder.cardHealthTextView);
        assertNotNull(viewHolder.cardNameTextView);
        assertNotNull(viewHolder.cardDescriptionTextView);
    }

    @Test
    public void onBindViewHolderPlayerCharacterCardTest(){
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

        CombatFragment testedFragment = new CombatFragment(mainActivity, "testEncounter", GameLocationService.WOODLANDS,R.id.cave_button);

        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        CombatCardModel testModel = new CombatCardModel("TestCard","Card", CardType.CHARACTER,true,true);
        DamageAbility damageAbility = new DamageAbility("test", "test", AbilityTarget.SINGLE_ENEMY, DamageType.ELECTRIC, AttackType.MELEE,1);
        testModel.setAbilities(Arrays.asList(damageAbility));
        testModel.setMaxHealth(10);
        testModel.setCurrentHealth(10);

        CombatCardStateViewAdapter combatCardStateViewAdapter = new CombatCardStateViewAdapter(Arrays.asList(testModel),true, testedFragment);
        CombatCardStateViewAdapter.ViewHolder viewHolder = combatCardStateViewAdapter.onCreateViewHolder(returnedView.findViewById(R.id.playerFrontLine),1);

        combatCardStateViewAdapter.onBindViewHolder(viewHolder,0);
        assertEquals(testModel.getCardName(), viewHolder.cardNameTextView.getText());
        assertEquals(testModel.getCardDescription(), viewHolder.cardDescriptionTextView.getText());
        assertEquals(testModel.getCurrentHealth()+"/"+ testModel.getMaxHealth(), viewHolder.cardHealthTextView.getText());
    }

    @Test
    public void onBindViewHolderPlayerCharacterCardOnLongClickTest(){
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

        CombatFragment testedFragment = new CombatFragment(mainActivity, "testEncounter", GameLocationService.WOODLANDS,R.id.cave_button);

        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        CombatCardModel testModel = new CombatCardModel("TestCard","Card", CardType.CHARACTER,true,true);
        DamageAbility damageAbility = new DamageAbility("test", "test", AbilityTarget.SINGLE_ENEMY, DamageType.ELECTRIC, AttackType.MELEE,1);
        testModel.setAbilities(Arrays.asList(damageAbility));
        testModel.setMaxHealth(10);
        testModel.setCurrentHealth(10);

        CombatCardStateViewAdapter combatCardStateViewAdapter = new CombatCardStateViewAdapter(Arrays.asList(testModel),true, testedFragment);
        CombatCardStateViewAdapter.ViewHolder viewHolder = combatCardStateViewAdapter.onCreateViewHolder(returnedView.findViewById(R.id.playerFrontLine),1);

        combatCardStateViewAdapter.onBindViewHolder(viewHolder,0);
        viewHolder.card.performLongClick();

        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.detailed_card).getVisibility());
        assertEquals(testModel.getCardName(), ((TextView)returnedView.findViewById(R.id.detailed_card_name)).getText());
        assertEquals(testModel.getCardDescription(), ((TextView)returnedView.findViewById(R.id.detailed_card_description)).getText());
        assertEquals(testModel.getCurrentHealth() + "/" + testModel.getMaxHealth(), ((TextView)returnedView.findViewById(R.id.detailedcard_health)).getText());
    }

    @Test
    public void onBindViewHolderPlayerCharacterCardOnTouchPlayerTurnTest(){
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

        CombatFragment testedFragment = new CombatFragment(mainActivity, "testEncounter", GameLocationService.WOODLANDS,R.id.cave_button);

        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        CombatCardModel testModel = new CombatCardModel("TestCard","Card", CardType.CHARACTER,true,false);
        DamageAbility damageAbility = new DamageAbility("test", "test", AbilityTarget.SINGLE_ENEMY, DamageType.ELECTRIC, AttackType.MELEE,1);
        testModel.setAbilities(Arrays.asList(damageAbility));
        testModel.setMaxHealth(10);
        testModel.setCurrentHealth(10);

        CombatCardStateViewAdapter combatCardStateViewAdapter = new CombatCardStateViewAdapter(Arrays.asList(testModel),true, testedFragment);
        CombatCardStateViewAdapter.ViewHolder viewHolder = combatCardStateViewAdapter.onCreateViewHolder(returnedView.findViewById(R.id.playerFrontLine),1);

        combatCardStateViewAdapter.onBindViewHolder(viewHolder,0);

        MotionEvent touchMotionEvent = MotionEvent.obtain(1,1,MotionEvent.ACTION_DOWN,5,5,1,1,1,1,1,1,1);
        assertTrue(viewHolder.card.onTouchEvent(touchMotionEvent));
    }

    @Test
    public void onBindViewHolderPlayerCharacterCardOnTouchPlayerTurnNotInHandTest(){
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

        CombatFragment testedFragment = new CombatFragment(mainActivity, "testEncounter", GameLocationService.WOODLANDS,R.id.cave_button);

        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        CombatCardModel testModel = new CombatCardModel("TestCard","Card", CardType.CHARACTER,true,true);
        DamageAbility damageAbility = new DamageAbility("test", "test", AbilityTarget.SINGLE_ENEMY, DamageType.ELECTRIC, AttackType.MELEE,1);
        testModel.setAbilities(Arrays.asList(damageAbility));
        testModel.setMaxHealth(10);
        testModel.setCurrentHealth(10);

        CombatCardStateViewAdapter combatCardStateViewAdapter = new CombatCardStateViewAdapter(Arrays.asList(testModel),false, testedFragment);
        CombatCardStateViewAdapter.ViewHolder viewHolder = combatCardStateViewAdapter.onCreateViewHolder(returnedView.findViewById(R.id.playerFrontLine),1);

        combatCardStateViewAdapter.onBindViewHolder(viewHolder,0);

        MotionEvent touchMotionEvent = MotionEvent.obtain(1,1,MotionEvent.ACTION_DOWN,5,5,1,1,1,1,1,1,1);
        assertTrue(viewHolder.card.onTouchEvent(touchMotionEvent));
    }

    @Test
    public void onBindViewHolderPlayerCharacterCardOnTouchNotPlayerTurnTest(){
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

        CombatFragment testedFragment = new CombatFragment(mainActivity, "testEncounter", GameLocationService.WOODLANDS,R.id.cave_button);

        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        CombatCardModel testModel = new CombatCardModel("TestCard","Card", CardType.CHARACTER,true,false);
        DamageAbility damageAbility = new DamageAbility("test", "test", AbilityTarget.SINGLE_ENEMY, DamageType.ELECTRIC, AttackType.MELEE,1);
        testModel.setAbilities(Arrays.asList(damageAbility));
        testModel.setMaxHealth(10);
        testModel.setCurrentHealth(10);

        CombatCardStateViewAdapter combatCardStateViewAdapter = new CombatCardStateViewAdapter(Arrays.asList(testModel),true, testedFragment);
        CombatCardStateViewAdapter.ViewHolder viewHolder = combatCardStateViewAdapter.onCreateViewHolder(returnedView.findViewById(R.id.playerFrontLine),1);

        combatCardStateViewAdapter.onBindViewHolder(viewHolder,0);

        MotionEvent touchMotionEvent = MotionEvent.obtain(1,1,MotionEvent.ACTION_DOWN,5,5,1,1,1,1,1,1,1);
        assertTrue(viewHolder.card.onTouchEvent(touchMotionEvent));
    }

    @Test
    public void onBindViewHolderPlayerItemCardTest(){
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

        CombatFragment testedFragment = new CombatFragment(mainActivity, "testEncounter", GameLocationService.WOODLANDS,R.id.cave_button);

        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        CombatCardModel testModel = new CombatCardModel("TestCard","Card", CardType.ITEM,true,false);
        DamageAbility damageAbility = new DamageAbility("test", "test", AbilityTarget.SINGLE_ENEMY, DamageType.ELECTRIC, AttackType.MELEE,1);
        testModel.setAbility(damageAbility);
        testModel.setMaxHealth(10);
        testModel.setCurrentHealth(10);

        CombatCardStateViewAdapter combatCardStateViewAdapter = new CombatCardStateViewAdapter(Arrays.asList(testModel),true, testedFragment);
        CombatCardStateViewAdapter.ViewHolder viewHolder = combatCardStateViewAdapter.onCreateViewHolder(returnedView.findViewById(R.id.playerFrontLine),1);

        combatCardStateViewAdapter.onBindViewHolder(viewHolder,0);
        assertEquals(testModel.getCardName(), viewHolder.cardNameTextView.getText());
        assertEquals(testModel.getCardDescription(), viewHolder.cardDescriptionTextView.getText());
        assertEquals("", viewHolder.cardHealthTextView.getText());
    }

    @Test
    public void onBindViewHolderPlayerItemCardLongClickTest(){
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

        CombatFragment testedFragment = new CombatFragment(mainActivity, "testEncounter", GameLocationService.WOODLANDS,R.id.cave_button);

        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        CombatCardModel testModel = new CombatCardModel("TestCard","Card", CardType.ITEM,true,false);
        DamageAbility damageAbility = new DamageAbility("test", "test", AbilityTarget.SINGLE_ENEMY, DamageType.ELECTRIC, AttackType.MELEE,1);
        testModel.setAbility(damageAbility);
        testModel.setMaxHealth(10);
        testModel.setCurrentHealth(10);

        CombatCardStateViewAdapter combatCardStateViewAdapter = new CombatCardStateViewAdapter(Arrays.asList(testModel),true, testedFragment);
        CombatCardStateViewAdapter.ViewHolder viewHolder = combatCardStateViewAdapter.onCreateViewHolder(returnedView.findViewById(R.id.playerFrontLine),1);

        combatCardStateViewAdapter.onBindViewHolder(viewHolder,0);
        viewHolder.card.performLongClick();

        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.detailed_card).getVisibility());
        assertEquals(testModel.getCardName(), ((TextView)returnedView.findViewById(R.id.detailed_card_name)).getText());
        assertEquals(testModel.getCardDescription(), ((TextView)returnedView.findViewById(R.id.detailed_card_description)).getText());
        assertEquals("", ((TextView)returnedView.findViewById(R.id.detailedcard_health)).getText());

    }

    @Test
    public void onBindViewHolderEnemyItemCardNotPlayedTest(){
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

        CombatFragment testedFragment = new CombatFragment(mainActivity, "testEncounter", GameLocationService.WOODLANDS,R.id.cave_button);

        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        CombatCardModel testModel = new CombatCardModel("TestCard","Card", CardType.ITEM,false,false);
        DamageAbility damageAbility = new DamageAbility("test", "test", AbilityTarget.SINGLE_ENEMY, DamageType.ELECTRIC, AttackType.MELEE,1);
        testModel.setAbility(damageAbility);
        testModel.setMaxHealth(10);
        testModel.setCurrentHealth(10);

        CombatCardStateViewAdapter combatCardStateViewAdapter = new CombatCardStateViewAdapter(Arrays.asList(testModel),true, testedFragment);
        CombatCardStateViewAdapter.ViewHolder viewHolder = combatCardStateViewAdapter.onCreateViewHolder(returnedView.findViewById(R.id.playerFrontLine),1);

        combatCardStateViewAdapter.onBindViewHolder(viewHolder,0);
        assertEquals("", viewHolder.cardNameTextView.getText());
        assertEquals("", viewHolder.cardDescriptionTextView.getText());
        assertEquals("", viewHolder.cardHealthTextView.getText());
    }

    @Test
    public void onBindViewHolderEnemyItemCardPlayedNotInSetupTest(){
        UserService mockUserService = Mockito.mock(UserService.class);
        CombatService mockCombatService = Mockito.mock(CombatService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setCombatService(mockCombatService);
        mainActivity.setUserService(mockUserService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        when(mockCombatService.isInSetup()).thenReturn(false);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);

        CombatFragment testedFragment = new CombatFragment(mainActivity, "testEncounter", GameLocationService.WOODLANDS,R.id.cave_button);
        testedFragment.onAttach(mainActivity);

        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        CombatCardModel testModel = new CombatCardModel("TestCard","Card", CardType.ITEM,false,true);
        DamageAbility damageAbility = new DamageAbility("test", "test", AbilityTarget.SINGLE_ENEMY, DamageType.ELECTRIC, AttackType.MELEE,1);
        testModel.setAbility(damageAbility);
        testModel.setMaxHealth(10);
        testModel.setCurrentHealth(10);

        CombatCardStateViewAdapter combatCardStateViewAdapter = new CombatCardStateViewAdapter(Arrays.asList(testModel),true, testedFragment);
        CombatCardStateViewAdapter.ViewHolder viewHolder = combatCardStateViewAdapter.onCreateViewHolder(returnedView.findViewById(R.id.playerFrontLine),1);

        combatCardStateViewAdapter.onBindViewHolder(viewHolder,0);
        assertEquals(testModel.getCardName(), viewHolder.cardNameTextView.getText());
        assertEquals(testModel.getCardDescription(), viewHolder.cardDescriptionTextView.getText());
        assertEquals("", viewHolder.cardHealthTextView.getText());
    }


}
