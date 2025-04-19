package com.zblouse.fantasyfitness.merchant;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.zblouse.fantasyfitness.R;
import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.combat.CombatService;
import com.zblouse.fantasyfitness.combat.cards.AbilityTarget;
import com.zblouse.fantasyfitness.combat.cards.BuffAbility;
import com.zblouse.fantasyfitness.combat.cards.BuffType;
import com.zblouse.fantasyfitness.combat.cards.Card;
import com.zblouse.fantasyfitness.combat.cards.ItemCard;
import com.zblouse.fantasyfitness.combat.cards.ItemType;
import com.zblouse.fantasyfitness.home.UserHomeFragment;
import com.zblouse.fantasyfitness.quest.Quest;
import com.zblouse.fantasyfitness.quest.QuestObjective;
import com.zblouse.fantasyfitness.quest.QuestObjectiveType;
import com.zblouse.fantasyfitness.quest.QuestViewAdapter;
import com.zblouse.fantasyfitness.user.UserGameStateService;
import com.zblouse.fantasyfitness.user.UserService;
import com.zblouse.fantasyfitness.world.GameLocationService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RunWith(RobolectricTestRunner.class)
public class MerchantViewAdapterTest {

    @Test
    public void onCreateViewHolderTest(){
        UserService mockUserService = Mockito.mock(UserService.class);
        MerchantService mockMerchantService = Mockito.mock(MerchantService.class);
        UserGameStateService mockUserGameStateService = Mockito.mock(UserGameStateService.class);
        CombatService mockCombatService = Mockito.mock(CombatService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setCombatService(mockCombatService);
        mainActivity.setUserService(mockUserService);
        mainActivity.setUserGameStateService(mockUserGameStateService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        UserHomeFragment testedFragment = new UserHomeFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        Card testCard = new ItemCard("testId","testUuid","testCard","testDescription", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.ATTACK,2));

        MerchantViewAdaper merchantViewAdapter = new MerchantViewAdaper(Arrays.asList(testCard), mockMerchantService,testedFragment);
        MerchantViewAdaper.ViewHolder viewHolder = merchantViewAdapter.onCreateViewHolder(returnedView.findViewById(R.id.merchant_recycler_view),1);

        assertNotNull(viewHolder.itemCard);
        assertNotNull(viewHolder.cardName);
        assertNotNull(viewHolder.cardAbility);
        assertNotNull(viewHolder.cardDescription);
        assertNotNull(viewHolder.cardPrice);
    }

    @Test
    public void onBindViewHolderTest(){
        UserService mockUserService = Mockito.mock(UserService.class);
        MerchantService mockMerchantService = Mockito.mock(MerchantService.class);
        UserGameStateService mockUserGameStateService = Mockito.mock(UserGameStateService.class);
        CombatService mockCombatService = Mockito.mock(CombatService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setCombatService(mockCombatService);
        mainActivity.setUserService(mockUserService);
        mainActivity.setUserGameStateService(mockUserGameStateService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        UserHomeFragment testedFragment = new UserHomeFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        Card testCard = new ItemCard("testId","testUuid","testCard","testDescription", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ENEMY, BuffType.ATTACK,2));
        Map<Card, Integer> merchantMap = new HashMap<>();
        merchantMap.put(testCard, 15);
        MerchantViewAdaper merchantViewAdapter = new MerchantViewAdaper(Arrays.asList(testCard), mockMerchantService,testedFragment);
        merchantViewAdapter.setMerchant(new Merchant("testTag", merchantMap));
        MerchantViewAdaper.ViewHolder viewHolder = merchantViewAdapter.onCreateViewHolder(returnedView.findViewById(R.id.merchant_recycler_view),1);

        merchantViewAdapter.onBindViewHolder(viewHolder,0);

        assertEquals(View.VISIBLE, viewHolder.itemCard.getVisibility());
        assertEquals(testCard.getCardName(),viewHolder.cardName.getText());
        assertEquals(testCard.getCardDescription(),viewHolder.cardDescription.getText());
        assertEquals("Price: " + 15 + " gold", viewHolder.cardPrice.getText());
        assertEquals(" to a single opponent card", viewHolder.cardAbility.getText());
    }
}
