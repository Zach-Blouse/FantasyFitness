package com.zblouse.fantasyfitness.combat;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

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
        CombatFragment testedFragment = new CombatFragment(mainActivity, "testEncounter");
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        CombatCardModel testModel = new CombatCardModel("TestCard","Card", CardType.CHARACTER,true,true);
        DamageAbility damageAbility = new DamageAbility("test", "test", AbilityTarget.SINGLE_ENEMY, DamageType.ELECTRIC, AttackType.MELEE,1);
        testModel.setAbilities(Arrays.asList(damageAbility));
        testModel.setMaxHealth(10);
        testModel.setCurrentHealth(10);

        CombatCardStateViewAdapter combatCardStateViewAdapter = new CombatCardStateViewAdapter(Arrays.asList(testModel),true, testedFragment);
        CombatCardStateViewAdapter.ViewHolder viewHolder = combatCardStateViewAdapter.onCreateViewHolder(returnedView.findViewById(R.id.playerFrontLine),1);
        assertNotNull(viewHolder.abilitiesRecyclerView);
        assertNotNull(viewHolder.card);
        assertNotNull(viewHolder.cardHealthTextView);
        assertNotNull(viewHolder.cardNameTextView);
        assertNotNull(viewHolder.cardDescriptionTextView);
    }
}
