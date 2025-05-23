package com.zblouse.fantasyfitness.home;

import static com.zblouse.fantasyfitness.actions.ExploreActionService.EXPLORE_ACTION_LOCATION_KEY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
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
import com.zblouse.fantasyfitness.actions.DialogActionResult;
import com.zblouse.fantasyfitness.actions.ExploreActionEvent;
import com.zblouse.fantasyfitness.actions.ExploreActionService;
import com.zblouse.fantasyfitness.actions.NothingFoundActionResult;
import com.zblouse.fantasyfitness.activity.DeviceServiceType;
import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.activity.ToastDeviceService;
import com.zblouse.fantasyfitness.combat.cards.AbilityTarget;
import com.zblouse.fantasyfitness.combat.cards.Card;
import com.zblouse.fantasyfitness.combat.cards.HealAbility;
import com.zblouse.fantasyfitness.combat.cards.ItemCard;
import com.zblouse.fantasyfitness.combat.cards.ItemType;
import com.zblouse.fantasyfitness.dialog.BaseDialogFetchEvent;
import com.zblouse.fantasyfitness.dialog.Dialog;
import com.zblouse.fantasyfitness.dialog.DialogAffect;
import com.zblouse.fantasyfitness.dialog.DialogAffectType;
import com.zblouse.fantasyfitness.dialog.DialogFetchEvent;
import com.zblouse.fantasyfitness.dialog.DialogService;
import com.zblouse.fantasyfitness.merchant.Merchant;
import com.zblouse.fantasyfitness.merchant.MerchantDisplayEvent;
import com.zblouse.fantasyfitness.quest.Quest;
import com.zblouse.fantasyfitness.quest.QuestFetchResponseEvent;
import com.zblouse.fantasyfitness.quest.QuestObjective;
import com.zblouse.fantasyfitness.quest.QuestObjectiveType;
import com.zblouse.fantasyfitness.quest.QuestService;
import com.zblouse.fantasyfitness.user.UserExistEvent;
import com.zblouse.fantasyfitness.user.UserGameState;
import com.zblouse.fantasyfitness.user.UserGameStateFetchResponseEvent;
import com.zblouse.fantasyfitness.user.UserGameStateService;
import com.zblouse.fantasyfitness.user.UserService;
import com.zblouse.fantasyfitness.world.GameLocationService;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RunWith(RobolectricTestRunner.class)
public class UserHomeFragmentTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);

    @Test
    public void onCreateTest() {
        UserService mockUserService = Mockito.mock(UserService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        UserGameStateService mockuserGameStateService = Mockito.mock(UserGameStateService.class);
        mainActivity.setUserGameStateService(mockuserGameStateService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        UserHomeFragment testedFragment = new UserHomeFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        assertNotNull(returnedView);
    }

    @Test
    public void publishUserExistEventNotExistsTest(){
        MainActivity mockMainActivity = Mockito.mock(MainActivity.class);
        ToastDeviceService mockToastDeviceService = Mockito.mock(ToastDeviceService.class);
        when(mockMainActivity.getDeviceService(eq(DeviceServiceType.TOAST))).thenReturn(mockToastDeviceService);
        UserHomeFragment testedFragment = new UserHomeFragment(mockMainActivity);
        testedFragment.publishEvent(new UserExistEvent(false,new HashMap<>()));
        verify(mockMainActivity).logout();
        verify(mockToastDeviceService).sendToast(eq("Account does not exist"));
    }

    @Test
    public void publishMerchantDisplayEventTest() {
        UserService mockUserService = Mockito.mock(UserService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        UserGameStateService mockuserGameStateService = Mockito.mock(UserGameStateService.class);
        mainActivity.setUserGameStateService(mockuserGameStateService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        UserHomeFragment testedFragment = new UserHomeFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        assertNotNull(returnedView);

        Map<Card, Integer> cardMap = new HashMap<>();
        ItemCard itemCard = new ItemCard("testUesr", "testuuid", "TestCardName","cardDescription", ItemType.EQUIPPABLE, new HealAbility("heal","heal", AbilityTarget.SINGLE_ALLY,3));
        cardMap.put(itemCard,50);

        Merchant merchant = new Merchant("merchantTag", cardMap);
        MerchantDisplayEvent merchantDisplayEvent = new MerchantDisplayEvent(merchant);

        testedFragment.publishEvent(merchantDisplayEvent);

        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.merchant_card_view).getVisibility());
        returnedView.findViewById(R.id.merchant_dismiss_button).performClick();
        assertEquals(View.GONE, returnedView.findViewById(R.id.merchant_card_view).getVisibility());
    }

    @Test
    public void publishUserGameStateResponseThanadelEventTest() {
        UserService mockUserService = Mockito.mock(UserService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        UserGameStateService mockuserGameStateService = Mockito.mock(UserGameStateService.class);
        mainActivity.setUserGameStateService(mockuserGameStateService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        UserHomeFragment testedFragment = new UserHomeFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        assertNotNull(returnedView);

        UserGameState testUserGameState = new UserGameState("testuser", GameLocationService.THANADEL_VILLAGE,51, 6);
        UserGameStateFetchResponseEvent userGameStateFetchResponseEvent = new UserGameStateFetchResponseEvent(testUserGameState,new HashMap<>());
        testedFragment.publishEvent(userGameStateFetchResponseEvent);

        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.inn_button).getVisibility());
        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.general_store_button).getVisibility());
    }

    @Test
    public void publishUserGameStateResponseFaolynEventTest() {
        UserService mockUserService = Mockito.mock(UserService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        UserGameStateService mockuserGameStateService = Mockito.mock(UserGameStateService.class);
        mainActivity.setUserGameStateService(mockuserGameStateService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        UserHomeFragment testedFragment = new UserHomeFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        assertNotNull(returnedView);

        UserGameState testUserGameState = new UserGameState("testuser", GameLocationService.FAOLYN,51, 6);
        UserGameStateFetchResponseEvent userGameStateFetchResponseEvent = new UserGameStateFetchResponseEvent(testUserGameState,new HashMap<>());
        testedFragment.publishEvent(userGameStateFetchResponseEvent);

        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.inn_button).getVisibility());
        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.general_store_button).getVisibility());
        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.blacksmith_button).getVisibility());
    }

    @Test
    public void publishUserGameStateResponseBridgetonEventTest() {
        UserService mockUserService = Mockito.mock(UserService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        UserGameStateService mockuserGameStateService = Mockito.mock(UserGameStateService.class);
        mainActivity.setUserGameStateService(mockuserGameStateService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        UserHomeFragment testedFragment = new UserHomeFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        assertNotNull(returnedView);

        UserGameState testUserGameState = new UserGameState("testuser", GameLocationService.BRIDGETON,51, 6);
        UserGameStateFetchResponseEvent userGameStateFetchResponseEvent = new UserGameStateFetchResponseEvent(testUserGameState,new HashMap<>());
        testedFragment.publishEvent(userGameStateFetchResponseEvent);

        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.inn_button).getVisibility());
        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.general_store_button).getVisibility());
        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.blacksmith_button).getVisibility());
    }

    @Test
    public void publishUserGameStateResponseRiverlandsEventTest() {
        UserService mockUserService = Mockito.mock(UserService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        UserGameStateService mockuserGameStateService = Mockito.mock(UserGameStateService.class);
        mainActivity.setUserGameStateService(mockuserGameStateService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        UserHomeFragment testedFragment = new UserHomeFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        assertNotNull(returnedView);

        UserGameState testUserGameState = new UserGameState("testuser", GameLocationService.RIVERLANDS,51, 6);
        UserGameStateFetchResponseEvent userGameStateFetchResponseEvent = new UserGameStateFetchResponseEvent(testUserGameState,new HashMap<>());
        testedFragment.publishEvent(userGameStateFetchResponseEvent);

        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.river_button).getVisibility());
        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.dark_forest_button).getVisibility());
    }

    @Test
    public void publishUserGameStateResponseMountainPassEventTest() {
        UserService mockUserService = Mockito.mock(UserService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        UserGameStateService mockuserGameStateService = Mockito.mock(UserGameStateService.class);
        mainActivity.setUserGameStateService(mockuserGameStateService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        UserHomeFragment testedFragment = new UserHomeFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        assertNotNull(returnedView);

        UserGameState testUserGameState = new UserGameState("testuser", GameLocationService.MOUNTAIN_PASS,51, 6);
        UserGameStateFetchResponseEvent userGameStateFetchResponseEvent = new UserGameStateFetchResponseEvent(testUserGameState,new HashMap<>());
        testedFragment.publishEvent(userGameStateFetchResponseEvent);

        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.tower_button).getVisibility());
        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.dwarven_tents_button).getVisibility());
    }

    @Test
    public void publishUserGameStateResponseHillsEventTest() {
        UserService mockUserService = Mockito.mock(UserService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        UserGameStateService mockuserGameStateService = Mockito.mock(UserGameStateService.class);
        mainActivity.setUserGameStateService(mockuserGameStateService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        UserHomeFragment testedFragment = new UserHomeFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        assertNotNull(returnedView);

        UserGameState testUserGameState = new UserGameState("testuser", GameLocationService.HILLS,51, 6);
        UserGameStateFetchResponseEvent userGameStateFetchResponseEvent = new UserGameStateFetchResponseEvent(testUserGameState,new HashMap<>());
        testedFragment.publishEvent(userGameStateFetchResponseEvent);

        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.cave_button).getVisibility());
        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.copse_of_trees_button).getVisibility());
    }

    @Test
    public void publishUserGameStateResponseWoodlandsEventTest() {
        UserService mockUserService = Mockito.mock(UserService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        UserGameStateService mockuserGameStateService = Mockito.mock(UserGameStateService.class);
        mainActivity.setUserGameStateService(mockuserGameStateService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        UserHomeFragment testedFragment = new UserHomeFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        assertNotNull(returnedView);

        UserGameState testUserGameState = new UserGameState("testuser", GameLocationService.WOODLANDS,51, 6);
        UserGameStateFetchResponseEvent userGameStateFetchResponseEvent = new UserGameStateFetchResponseEvent(testUserGameState,new HashMap<>());
        testedFragment.publishEvent(userGameStateFetchResponseEvent);

        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.dark_forest_button).getVisibility());
        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.cave_button).getVisibility());
        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.marsh_button).getVisibility());
    }

    @Test
    public void publishUserGameStateResponseMonasteryEventTest() {
        UserService mockUserService = Mockito.mock(UserService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        UserGameStateService mockuserGameStateService = Mockito.mock(UserGameStateService.class);
        mainActivity.setUserGameStateService(mockuserGameStateService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        UserHomeFragment testedFragment = new UserHomeFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        assertNotNull(returnedView);

        UserGameState testUserGameState = new UserGameState("testuser", GameLocationService.MONASTARY,51, 6);
        UserGameStateFetchResponseEvent userGameStateFetchResponseEvent = new UserGameStateFetchResponseEvent(testUserGameState,new HashMap<>());
        testedFragment.publishEvent(userGameStateFetchResponseEvent);

        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.monastery_button).getVisibility());
    }

    @Test
    public void publishUserGameStateResponseNorthRoadEventTest() {
        UserService mockUserService = Mockito.mock(UserService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        UserGameStateService mockuserGameStateService = Mockito.mock(UserGameStateService.class);
        mainActivity.setUserGameStateService(mockuserGameStateService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        UserHomeFragment testedFragment = new UserHomeFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        assertNotNull(returnedView);

        UserGameState testUserGameState = new UserGameState("testuser", GameLocationService.NORTH_ROAD,51, 6);
        UserGameStateFetchResponseEvent userGameStateFetchResponseEvent = new UserGameStateFetchResponseEvent(testUserGameState,new HashMap<>());
        testedFragment.publishEvent(userGameStateFetchResponseEvent);

        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.beach_button).getVisibility());
        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.bandit_camp_button).getVisibility());
    }

    @Test
    public void publishUserGameStateResponseArduwynEventTest() {
        UserService mockUserService = Mockito.mock(UserService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        UserGameStateService mockuserGameStateService = Mockito.mock(UserGameStateService.class);
        mainActivity.setUserGameStateService(mockuserGameStateService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        UserHomeFragment testedFragment = new UserHomeFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        assertNotNull(returnedView);

        UserGameState testUserGameState = new UserGameState("testuser", GameLocationService.ARDUWYN,51, 6);
        UserGameStateFetchResponseEvent userGameStateFetchResponseEvent = new UserGameStateFetchResponseEvent(testUserGameState,new HashMap<>());
        testedFragment.publishEvent(userGameStateFetchResponseEvent);

        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.inn_button).getVisibility());
        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.blacksmith_button).getVisibility());
        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.general_store_button).getVisibility());
    }

    @Test
    public void publishUserGameStateResponseLastTowerEventTest() {
        UserService mockUserService = Mockito.mock(UserService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        UserGameStateService mockuserGameStateService = Mockito.mock(UserGameStateService.class);
        mainActivity.setUserGameStateService(mockuserGameStateService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        UserHomeFragment testedFragment = new UserHomeFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        assertNotNull(returnedView);

        UserGameState testUserGameState = new UserGameState("testuser", GameLocationService.LAST_TOWER,51, 6);
        UserGameStateFetchResponseEvent userGameStateFetchResponseEvent = new UserGameStateFetchResponseEvent(testUserGameState,new HashMap<>());
        testedFragment.publishEvent(userGameStateFetchResponseEvent);

        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.tower_button).getVisibility());
    }

    @Test
    public void publishUserGameStateResponseValleyOfMonstersEventTest() {
        UserService mockUserService = Mockito.mock(UserService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        UserGameStateService mockuserGameStateService = Mockito.mock(UserGameStateService.class);
        mainActivity.setUserGameStateService(mockuserGameStateService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        UserHomeFragment testedFragment = new UserHomeFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        assertNotNull(returnedView);

        UserGameState testUserGameState = new UserGameState("testuser", GameLocationService.VALLEY_OF_MONSTERS,51, 6);
        UserGameStateFetchResponseEvent userGameStateFetchResponseEvent = new UserGameStateFetchResponseEvent(testUserGameState,new HashMap<>());
        testedFragment.publishEvent(userGameStateFetchResponseEvent);

        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.marsh_button).getVisibility());
        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.dark_forest_button).getVisibility());
        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.cave_button).getVisibility());
    }

    @Test
    public void publishUserGameStateResponseFarmlandsEventTest() {
        UserService mockUserService = Mockito.mock(UserService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        UserGameStateService mockuserGameStateService = Mockito.mock(UserGameStateService.class);
        mainActivity.setUserGameStateService(mockuserGameStateService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        UserHomeFragment testedFragment = new UserHomeFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        assertNotNull(returnedView);

        UserGameState testUserGameState = new UserGameState("testuser", GameLocationService.FARMLANDS,51, 6);
        UserGameStateFetchResponseEvent userGameStateFetchResponseEvent = new UserGameStateFetchResponseEvent(testUserGameState,new HashMap<>());
        testedFragment.publishEvent(userGameStateFetchResponseEvent);

        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.inn_button).getVisibility());
    }

    @Test
    public void woodlandsDarkForestClickTest() {
        UserService mockUserService = Mockito.mock(UserService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        UserGameStateService mockuserGameStateService = Mockito.mock(UserGameStateService.class);
        ExploreActionService exploreActionService = Mockito.mock(ExploreActionService.class);
        mainActivity.setExploreActionService(exploreActionService);
        mainActivity.setUserGameStateService(mockuserGameStateService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        UserHomeFragment testedFragment = new UserHomeFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        assertNotNull(returnedView);

        UserGameState testUserGameState = new UserGameState("testuser", GameLocationService.WOODLANDS,51, 6);
        UserGameStateFetchResponseEvent userGameStateFetchResponseEvent = new UserGameStateFetchResponseEvent(testUserGameState,new HashMap<>());
        testedFragment.publishEvent(userGameStateFetchResponseEvent);

        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.dark_forest_button).getVisibility());

        returnedView.findViewById(R.id.dark_forest_button).performClick();

        ArgumentCaptor<Map> metadataCaptor = ArgumentCaptor.forClass(Map.class);
        verify(exploreActionService).exploreAction(metadataCaptor.capture());

        assertNotNull(metadataCaptor.getValue());
        assert(metadataCaptor.getValue().containsKey(EXPLORE_ACTION_LOCATION_KEY));
        assertEquals(GameLocationService.WOODLANDS, metadataCaptor.getValue().get(EXPLORE_ACTION_LOCATION_KEY));
        assert(metadataCaptor.getValue().containsKey(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED));
        assertEquals(R.id.dark_forest_button, metadataCaptor.getValue().get(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED));
    }


    @Test
    public void woodlandsCaveClickTest() {
        UserService mockUserService = Mockito.mock(UserService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        UserGameStateService mockuserGameStateService = Mockito.mock(UserGameStateService.class);
        ExploreActionService exploreActionService = Mockito.mock(ExploreActionService.class);
        mainActivity.setExploreActionService(exploreActionService);
        mainActivity.setUserGameStateService(mockuserGameStateService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        UserHomeFragment testedFragment = new UserHomeFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        assertNotNull(returnedView);

        UserGameState testUserGameState = new UserGameState("testuser", GameLocationService.WOODLANDS,51, 6);
        UserGameStateFetchResponseEvent userGameStateFetchResponseEvent = new UserGameStateFetchResponseEvent(testUserGameState,new HashMap<>());
        testedFragment.publishEvent(userGameStateFetchResponseEvent);

        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.cave_button).getVisibility());

        returnedView.findViewById(R.id.cave_button).performClick();

        ArgumentCaptor<Map> metadataCaptor = ArgumentCaptor.forClass(Map.class);
        verify(exploreActionService).exploreAction(metadataCaptor.capture());

        assertNotNull(metadataCaptor.getValue());
        assert(metadataCaptor.getValue().containsKey(EXPLORE_ACTION_LOCATION_KEY));
        assertEquals(GameLocationService.WOODLANDS, metadataCaptor.getValue().get(EXPLORE_ACTION_LOCATION_KEY));
        assert(metadataCaptor.getValue().containsKey(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED));
        assertEquals(R.id.cave_button, metadataCaptor.getValue().get(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED));
    }

    @Test
    public void woodlandsMarshlandsClickTest() {
        UserService mockUserService = Mockito.mock(UserService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        UserGameStateService mockuserGameStateService = Mockito.mock(UserGameStateService.class);
        ExploreActionService exploreActionService = Mockito.mock(ExploreActionService.class);
        mainActivity.setExploreActionService(exploreActionService);
        mainActivity.setUserGameStateService(mockuserGameStateService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        UserHomeFragment testedFragment = new UserHomeFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        assertNotNull(returnedView);

        UserGameState testUserGameState = new UserGameState("testuser", GameLocationService.WOODLANDS,51, 6);
        UserGameStateFetchResponseEvent userGameStateFetchResponseEvent = new UserGameStateFetchResponseEvent(testUserGameState,new HashMap<>());
        testedFragment.publishEvent(userGameStateFetchResponseEvent);

        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.marsh_button).getVisibility());

        returnedView.findViewById(R.id.marsh_button).performClick();

        ArgumentCaptor<Map> metadataCaptor = ArgumentCaptor.forClass(Map.class);
        verify(exploreActionService).exploreAction(metadataCaptor.capture());

        assertNotNull(metadataCaptor.getValue());
        assert(metadataCaptor.getValue().containsKey(EXPLORE_ACTION_LOCATION_KEY));
        assertEquals(GameLocationService.WOODLANDS, metadataCaptor.getValue().get(EXPLORE_ACTION_LOCATION_KEY));
        assert(metadataCaptor.getValue().containsKey(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED));
        assertEquals(R.id.marsh_button, metadataCaptor.getValue().get(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED));
    }

    @Test
    public void thanadelInnClickTest() {
        UserService mockUserService = Mockito.mock(UserService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        UserGameStateService mockuserGameStateService = Mockito.mock(UserGameStateService.class);
        ExploreActionService exploreActionService = Mockito.mock(ExploreActionService.class);
        mainActivity.setExploreActionService(exploreActionService);
        mainActivity.setUserGameStateService(mockuserGameStateService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        UserHomeFragment testedFragment = new UserHomeFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        assertNotNull(returnedView);

        UserGameState testUserGameState = new UserGameState("testuser", GameLocationService.THANADEL_VILLAGE,51, 6);
        UserGameStateFetchResponseEvent userGameStateFetchResponseEvent = new UserGameStateFetchResponseEvent(testUserGameState,new HashMap<>());
        testedFragment.publishEvent(userGameStateFetchResponseEvent);

        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.inn_button).getVisibility());

        returnedView.findViewById(R.id.inn_button).performClick();

        ArgumentCaptor<Map> metadataCaptor = ArgumentCaptor.forClass(Map.class);
        verify(exploreActionService).exploreAction(metadataCaptor.capture());

        assertNotNull(metadataCaptor.getValue());
        assert(metadataCaptor.getValue().containsKey(EXPLORE_ACTION_LOCATION_KEY));
        assertEquals(GameLocationService.THANADEL_VILLAGE, metadataCaptor.getValue().get(EXPLORE_ACTION_LOCATION_KEY));
        assert(metadataCaptor.getValue().containsKey(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED));
        assertEquals(R.id.inn_button, metadataCaptor.getValue().get(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED));
    }


    @Test
    public void thanadelStoreClickTest() {
        UserService mockUserService = Mockito.mock(UserService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        UserGameStateService mockuserGameStateService = Mockito.mock(UserGameStateService.class);
        ExploreActionService exploreActionService = Mockito.mock(ExploreActionService.class);
        mainActivity.setExploreActionService(exploreActionService);
        mainActivity.setUserGameStateService(mockuserGameStateService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        UserHomeFragment testedFragment = new UserHomeFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        assertNotNull(returnedView);

        UserGameState testUserGameState = new UserGameState("testuser", GameLocationService.THANADEL_VILLAGE,51, 6);
        UserGameStateFetchResponseEvent userGameStateFetchResponseEvent = new UserGameStateFetchResponseEvent(testUserGameState,new HashMap<>());
        testedFragment.publishEvent(userGameStateFetchResponseEvent);

        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.general_store_button).getVisibility());

        returnedView.findViewById(R.id.general_store_button).performClick();

        ArgumentCaptor<Map> metadataCaptor = ArgumentCaptor.forClass(Map.class);
        verify(exploreActionService).exploreAction(metadataCaptor.capture());

        assertNotNull(metadataCaptor.getValue());
        assert(metadataCaptor.getValue().containsKey(EXPLORE_ACTION_LOCATION_KEY));
        assertEquals(GameLocationService.THANADEL_VILLAGE, metadataCaptor.getValue().get(EXPLORE_ACTION_LOCATION_KEY));
        assert(metadataCaptor.getValue().containsKey(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED));
        assertEquals(R.id.general_store_button, metadataCaptor.getValue().get(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED));
    }

    @Test
    public void faolynInnClickTest() {
        UserService mockUserService = Mockito.mock(UserService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        UserGameStateService mockuserGameStateService = Mockito.mock(UserGameStateService.class);
        ExploreActionService exploreActionService = Mockito.mock(ExploreActionService.class);
        mainActivity.setExploreActionService(exploreActionService);
        mainActivity.setUserGameStateService(mockuserGameStateService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        UserHomeFragment testedFragment = new UserHomeFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        assertNotNull(returnedView);

        UserGameState testUserGameState = new UserGameState("testuser", GameLocationService.FAOLYN,51, 6);
        UserGameStateFetchResponseEvent userGameStateFetchResponseEvent = new UserGameStateFetchResponseEvent(testUserGameState,new HashMap<>());
        testedFragment.publishEvent(userGameStateFetchResponseEvent);

        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.inn_button).getVisibility());

        returnedView.findViewById(R.id.inn_button).performClick();

        ArgumentCaptor<Map> metadataCaptor = ArgumentCaptor.forClass(Map.class);
        verify(exploreActionService).exploreAction(metadataCaptor.capture());

        assertNotNull(metadataCaptor.getValue());
        assert(metadataCaptor.getValue().containsKey(EXPLORE_ACTION_LOCATION_KEY));
        assertEquals(GameLocationService.FAOLYN, metadataCaptor.getValue().get(EXPLORE_ACTION_LOCATION_KEY));
        assert(metadataCaptor.getValue().containsKey(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED));
        assertEquals(R.id.inn_button, metadataCaptor.getValue().get(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED));
    }

    @Test
    public void faolynBlacksmithClickTest() {
        UserService mockUserService = Mockito.mock(UserService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        UserGameStateService mockuserGameStateService = Mockito.mock(UserGameStateService.class);
        ExploreActionService exploreActionService = Mockito.mock(ExploreActionService.class);
        mainActivity.setExploreActionService(exploreActionService);
        mainActivity.setUserGameStateService(mockuserGameStateService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        UserHomeFragment testedFragment = new UserHomeFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        assertNotNull(returnedView);

        UserGameState testUserGameState = new UserGameState("testuser", GameLocationService.FAOLYN,51, 6);
        UserGameStateFetchResponseEvent userGameStateFetchResponseEvent = new UserGameStateFetchResponseEvent(testUserGameState,new HashMap<>());
        testedFragment.publishEvent(userGameStateFetchResponseEvent);

        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.blacksmith_button).getVisibility());

        returnedView.findViewById(R.id.blacksmith_button).performClick();

        ArgumentCaptor<Map> metadataCaptor = ArgumentCaptor.forClass(Map.class);
        verify(exploreActionService).exploreAction(metadataCaptor.capture());

        assertNotNull(metadataCaptor.getValue());
        assert(metadataCaptor.getValue().containsKey(EXPLORE_ACTION_LOCATION_KEY));
        assertEquals(GameLocationService.FAOLYN, metadataCaptor.getValue().get(EXPLORE_ACTION_LOCATION_KEY));
        assert(metadataCaptor.getValue().containsKey(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED));
        assertEquals(R.id.blacksmith_button, metadataCaptor.getValue().get(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED));
    }

    @Test
    public void faolynGeneralStoreClickTest() {
        UserService mockUserService = Mockito.mock(UserService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        UserGameStateService mockuserGameStateService = Mockito.mock(UserGameStateService.class);
        ExploreActionService exploreActionService = Mockito.mock(ExploreActionService.class);
        mainActivity.setExploreActionService(exploreActionService);
        mainActivity.setUserGameStateService(mockuserGameStateService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        UserHomeFragment testedFragment = new UserHomeFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        assertNotNull(returnedView);

        UserGameState testUserGameState = new UserGameState("testuser", GameLocationService.FAOLYN,51, 6);
        UserGameStateFetchResponseEvent userGameStateFetchResponseEvent = new UserGameStateFetchResponseEvent(testUserGameState,new HashMap<>());
        testedFragment.publishEvent(userGameStateFetchResponseEvent);

        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.general_store_button).getVisibility());

        returnedView.findViewById(R.id.general_store_button).performClick();

        ArgumentCaptor<Map> metadataCaptor = ArgumentCaptor.forClass(Map.class);
        verify(exploreActionService).exploreAction(metadataCaptor.capture());

        assertNotNull(metadataCaptor.getValue());
        assert(metadataCaptor.getValue().containsKey(EXPLORE_ACTION_LOCATION_KEY));
        assertEquals(GameLocationService.FAOLYN, metadataCaptor.getValue().get(EXPLORE_ACTION_LOCATION_KEY));
        assert(metadataCaptor.getValue().containsKey(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED));
        assertEquals(R.id.general_store_button, metadataCaptor.getValue().get(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED));
    }

    @Test
    public void bridgetonInnClickTest() {
        UserService mockUserService = Mockito.mock(UserService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        UserGameStateService mockuserGameStateService = Mockito.mock(UserGameStateService.class);
        ExploreActionService exploreActionService = Mockito.mock(ExploreActionService.class);
        mainActivity.setExploreActionService(exploreActionService);
        mainActivity.setUserGameStateService(mockuserGameStateService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        UserHomeFragment testedFragment = new UserHomeFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        assertNotNull(returnedView);

        UserGameState testUserGameState = new UserGameState("testuser", GameLocationService.BRIDGETON,51, 6);
        UserGameStateFetchResponseEvent userGameStateFetchResponseEvent = new UserGameStateFetchResponseEvent(testUserGameState,new HashMap<>());
        testedFragment.publishEvent(userGameStateFetchResponseEvent);

        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.inn_button).getVisibility());

        returnedView.findViewById(R.id.inn_button).performClick();

        ArgumentCaptor<Map> metadataCaptor = ArgumentCaptor.forClass(Map.class);
        verify(exploreActionService).exploreAction(metadataCaptor.capture());

        assertNotNull(metadataCaptor.getValue());
        assert(metadataCaptor.getValue().containsKey(EXPLORE_ACTION_LOCATION_KEY));
        assertEquals(GameLocationService.BRIDGETON, metadataCaptor.getValue().get(EXPLORE_ACTION_LOCATION_KEY));
        assert(metadataCaptor.getValue().containsKey(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED));
        assertEquals(R.id.inn_button, metadataCaptor.getValue().get(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED));
    }

    @Test
    public void bridgetonBlacksmithClickTest() {
        UserService mockUserService = Mockito.mock(UserService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        UserGameStateService mockuserGameStateService = Mockito.mock(UserGameStateService.class);
        ExploreActionService exploreActionService = Mockito.mock(ExploreActionService.class);
        mainActivity.setExploreActionService(exploreActionService);
        mainActivity.setUserGameStateService(mockuserGameStateService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        UserHomeFragment testedFragment = new UserHomeFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        assertNotNull(returnedView);

        UserGameState testUserGameState = new UserGameState("testuser", GameLocationService.BRIDGETON,51, 6);
        UserGameStateFetchResponseEvent userGameStateFetchResponseEvent = new UserGameStateFetchResponseEvent(testUserGameState,new HashMap<>());
        testedFragment.publishEvent(userGameStateFetchResponseEvent);

        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.blacksmith_button).getVisibility());

        returnedView.findViewById(R.id.blacksmith_button).performClick();

        ArgumentCaptor<Map> metadataCaptor = ArgumentCaptor.forClass(Map.class);
        verify(exploreActionService).exploreAction(metadataCaptor.capture());

        assertNotNull(metadataCaptor.getValue());
        assert(metadataCaptor.getValue().containsKey(EXPLORE_ACTION_LOCATION_KEY));
        assertEquals(GameLocationService.BRIDGETON, metadataCaptor.getValue().get(EXPLORE_ACTION_LOCATION_KEY));
        assert(metadataCaptor.getValue().containsKey(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED));
        assertEquals(R.id.blacksmith_button, metadataCaptor.getValue().get(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED));
    }

    @Test
    public void bridgetonGeneralStoreClickTest() {
        UserService mockUserService = Mockito.mock(UserService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        UserGameStateService mockuserGameStateService = Mockito.mock(UserGameStateService.class);
        ExploreActionService exploreActionService = Mockito.mock(ExploreActionService.class);
        mainActivity.setExploreActionService(exploreActionService);
        mainActivity.setUserGameStateService(mockuserGameStateService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        UserHomeFragment testedFragment = new UserHomeFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        assertNotNull(returnedView);

        UserGameState testUserGameState = new UserGameState("testuser", GameLocationService.BRIDGETON,51, 6);
        UserGameStateFetchResponseEvent userGameStateFetchResponseEvent = new UserGameStateFetchResponseEvent(testUserGameState,new HashMap<>());
        testedFragment.publishEvent(userGameStateFetchResponseEvent);

        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.general_store_button).getVisibility());

        returnedView.findViewById(R.id.general_store_button).performClick();

        ArgumentCaptor<Map> metadataCaptor = ArgumentCaptor.forClass(Map.class);
        verify(exploreActionService).exploreAction(metadataCaptor.capture());

        assertNotNull(metadataCaptor.getValue());
        assert(metadataCaptor.getValue().containsKey(EXPLORE_ACTION_LOCATION_KEY));
        assertEquals(GameLocationService.BRIDGETON, metadataCaptor.getValue().get(EXPLORE_ACTION_LOCATION_KEY));
        assert(metadataCaptor.getValue().containsKey(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED));
        assertEquals(R.id.general_store_button, metadataCaptor.getValue().get(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED));
    }


    @Test
    public void arduwynInnClickTest() {
        UserService mockUserService = Mockito.mock(UserService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        UserGameStateService mockuserGameStateService = Mockito.mock(UserGameStateService.class);
        ExploreActionService exploreActionService = Mockito.mock(ExploreActionService.class);
        mainActivity.setExploreActionService(exploreActionService);
        mainActivity.setUserGameStateService(mockuserGameStateService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        UserHomeFragment testedFragment = new UserHomeFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        assertNotNull(returnedView);

        UserGameState testUserGameState = new UserGameState("testuser", GameLocationService.ARDUWYN,51, 6);
        UserGameStateFetchResponseEvent userGameStateFetchResponseEvent = new UserGameStateFetchResponseEvent(testUserGameState,new HashMap<>());
        testedFragment.publishEvent(userGameStateFetchResponseEvent);

        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.inn_button).getVisibility());

        returnedView.findViewById(R.id.inn_button).performClick();

        ArgumentCaptor<Map> metadataCaptor = ArgumentCaptor.forClass(Map.class);
        verify(exploreActionService).exploreAction(metadataCaptor.capture());

        assertNotNull(metadataCaptor.getValue());
        assert(metadataCaptor.getValue().containsKey(EXPLORE_ACTION_LOCATION_KEY));
        assertEquals(GameLocationService.ARDUWYN, metadataCaptor.getValue().get(EXPLORE_ACTION_LOCATION_KEY));
        assert(metadataCaptor.getValue().containsKey(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED));
        assertEquals(R.id.inn_button, metadataCaptor.getValue().get(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED));
    }

    @Test
    public void arduwynBlacksmithClickTest() {
        UserService mockUserService = Mockito.mock(UserService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        UserGameStateService mockuserGameStateService = Mockito.mock(UserGameStateService.class);
        ExploreActionService exploreActionService = Mockito.mock(ExploreActionService.class);
        mainActivity.setExploreActionService(exploreActionService);
        mainActivity.setUserGameStateService(mockuserGameStateService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        UserHomeFragment testedFragment = new UserHomeFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        assertNotNull(returnedView);

        UserGameState testUserGameState = new UserGameState("testuser", GameLocationService.ARDUWYN,51, 6);
        UserGameStateFetchResponseEvent userGameStateFetchResponseEvent = new UserGameStateFetchResponseEvent(testUserGameState,new HashMap<>());
        testedFragment.publishEvent(userGameStateFetchResponseEvent);

        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.blacksmith_button).getVisibility());

        returnedView.findViewById(R.id.blacksmith_button).performClick();

        ArgumentCaptor<Map> metadataCaptor = ArgumentCaptor.forClass(Map.class);
        verify(exploreActionService).exploreAction(metadataCaptor.capture());

        assertNotNull(metadataCaptor.getValue());
        assert(metadataCaptor.getValue().containsKey(EXPLORE_ACTION_LOCATION_KEY));
        assertEquals(GameLocationService.ARDUWYN, metadataCaptor.getValue().get(EXPLORE_ACTION_LOCATION_KEY));
        assert(metadataCaptor.getValue().containsKey(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED));
        assertEquals(R.id.blacksmith_button, metadataCaptor.getValue().get(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED));
    }

    @Test
    public void arduwynGeneralStoreClickTest() {
        UserService mockUserService = Mockito.mock(UserService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        UserGameStateService mockuserGameStateService = Mockito.mock(UserGameStateService.class);
        ExploreActionService exploreActionService = Mockito.mock(ExploreActionService.class);
        mainActivity.setExploreActionService(exploreActionService);
        mainActivity.setUserGameStateService(mockuserGameStateService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        UserHomeFragment testedFragment = new UserHomeFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        assertNotNull(returnedView);

        UserGameState testUserGameState = new UserGameState("testuser", GameLocationService.ARDUWYN,51, 6);
        UserGameStateFetchResponseEvent userGameStateFetchResponseEvent = new UserGameStateFetchResponseEvent(testUserGameState,new HashMap<>());
        testedFragment.publishEvent(userGameStateFetchResponseEvent);

        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.general_store_button).getVisibility());

        returnedView.findViewById(R.id.general_store_button).performClick();

        ArgumentCaptor<Map> metadataCaptor = ArgumentCaptor.forClass(Map.class);
        verify(exploreActionService).exploreAction(metadataCaptor.capture());

        assertNotNull(metadataCaptor.getValue());
        assert(metadataCaptor.getValue().containsKey(EXPLORE_ACTION_LOCATION_KEY));
        assertEquals(GameLocationService.ARDUWYN, metadataCaptor.getValue().get(EXPLORE_ACTION_LOCATION_KEY));
        assert(metadataCaptor.getValue().containsKey(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED));
        assertEquals(R.id.general_store_button, metadataCaptor.getValue().get(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED));
    }

    @Test
    public void monasteryClickTest() {
        UserService mockUserService = Mockito.mock(UserService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        UserGameStateService mockuserGameStateService = Mockito.mock(UserGameStateService.class);
        ExploreActionService exploreActionService = Mockito.mock(ExploreActionService.class);
        mainActivity.setExploreActionService(exploreActionService);
        mainActivity.setUserGameStateService(mockuserGameStateService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        UserHomeFragment testedFragment = new UserHomeFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        assertNotNull(returnedView);

        UserGameState testUserGameState = new UserGameState("testuser", GameLocationService.MONASTARY,51, 6);
        UserGameStateFetchResponseEvent userGameStateFetchResponseEvent = new UserGameStateFetchResponseEvent(testUserGameState,new HashMap<>());
        testedFragment.publishEvent(userGameStateFetchResponseEvent);

        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.monastery_button).getVisibility());

        returnedView.findViewById(R.id.monastery_button).performClick();

        ArgumentCaptor<Map> metadataCaptor = ArgumentCaptor.forClass(Map.class);
        verify(exploreActionService).exploreAction(metadataCaptor.capture());

        assertNotNull(metadataCaptor.getValue());
        assert(metadataCaptor.getValue().containsKey(EXPLORE_ACTION_LOCATION_KEY));
        assertEquals(GameLocationService.MONASTARY, metadataCaptor.getValue().get(EXPLORE_ACTION_LOCATION_KEY));
        assert(metadataCaptor.getValue().containsKey(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED));
        assertEquals(R.id.monastery_button, metadataCaptor.getValue().get(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED));
    }

    @Test
    public void mountainPassTowerClickTest() {
        UserService mockUserService = Mockito.mock(UserService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        UserGameStateService mockuserGameStateService = Mockito.mock(UserGameStateService.class);
        ExploreActionService exploreActionService = Mockito.mock(ExploreActionService.class);
        mainActivity.setExploreActionService(exploreActionService);
        mainActivity.setUserGameStateService(mockuserGameStateService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        UserHomeFragment testedFragment = new UserHomeFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        assertNotNull(returnedView);

        UserGameState testUserGameState = new UserGameState("testuser", GameLocationService.MOUNTAIN_PASS,51, 6);
        UserGameStateFetchResponseEvent userGameStateFetchResponseEvent = new UserGameStateFetchResponseEvent(testUserGameState,new HashMap<>());
        testedFragment.publishEvent(userGameStateFetchResponseEvent);

        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.tower_button).getVisibility());

        returnedView.findViewById(R.id.tower_button).performClick();

        ArgumentCaptor<Map> metadataCaptor = ArgumentCaptor.forClass(Map.class);
        verify(exploreActionService).exploreAction(metadataCaptor.capture());

        assertNotNull(metadataCaptor.getValue());
        assert(metadataCaptor.getValue().containsKey(EXPLORE_ACTION_LOCATION_KEY));
        assertEquals(GameLocationService.MOUNTAIN_PASS, metadataCaptor.getValue().get(EXPLORE_ACTION_LOCATION_KEY));
        assert(metadataCaptor.getValue().containsKey(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED));
        assertEquals(R.id.tower_button, metadataCaptor.getValue().get(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED));
    }

    @Test
    public void mountainDwarvenTentsClickTest() {
        UserService mockUserService = Mockito.mock(UserService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        UserGameStateService mockuserGameStateService = Mockito.mock(UserGameStateService.class);
        ExploreActionService exploreActionService = Mockito.mock(ExploreActionService.class);
        mainActivity.setExploreActionService(exploreActionService);
        mainActivity.setUserGameStateService(mockuserGameStateService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        UserHomeFragment testedFragment = new UserHomeFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        assertNotNull(returnedView);

        UserGameState testUserGameState = new UserGameState("testuser", GameLocationService.MOUNTAIN_PASS,51, 6);
        UserGameStateFetchResponseEvent userGameStateFetchResponseEvent = new UserGameStateFetchResponseEvent(testUserGameState,new HashMap<>());
        testedFragment.publishEvent(userGameStateFetchResponseEvent);

        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.dwarven_tents_button).getVisibility());

        returnedView.findViewById(R.id.dwarven_tents_button).performClick();

        ArgumentCaptor<Map> metadataCaptor = ArgumentCaptor.forClass(Map.class);
        verify(exploreActionService).exploreAction(metadataCaptor.capture());

        assertNotNull(metadataCaptor.getValue());
        assert(metadataCaptor.getValue().containsKey(EXPLORE_ACTION_LOCATION_KEY));
        assertEquals(GameLocationService.MOUNTAIN_PASS, metadataCaptor.getValue().get(EXPLORE_ACTION_LOCATION_KEY));
        assert(metadataCaptor.getValue().containsKey(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED));
        assertEquals(R.id.dwarven_tents_button, metadataCaptor.getValue().get(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED));
    }

    @Test
    public void lastTowerTowerClickTest() {
        UserService mockUserService = Mockito.mock(UserService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        UserGameStateService mockuserGameStateService = Mockito.mock(UserGameStateService.class);
        ExploreActionService exploreActionService = Mockito.mock(ExploreActionService.class);
        mainActivity.setExploreActionService(exploreActionService);
        mainActivity.setUserGameStateService(mockuserGameStateService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        UserHomeFragment testedFragment = new UserHomeFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        assertNotNull(returnedView);

        UserGameState testUserGameState = new UserGameState("testuser", GameLocationService.LAST_TOWER,51, 6);
        UserGameStateFetchResponseEvent userGameStateFetchResponseEvent = new UserGameStateFetchResponseEvent(testUserGameState,new HashMap<>());
        testedFragment.publishEvent(userGameStateFetchResponseEvent);

        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.tower_button).getVisibility());

        returnedView.findViewById(R.id.tower_button).performClick();

        ArgumentCaptor<Map> metadataCaptor = ArgumentCaptor.forClass(Map.class);
        verify(exploreActionService).exploreAction(metadataCaptor.capture());

        assertNotNull(metadataCaptor.getValue());
        assert(metadataCaptor.getValue().containsKey(EXPLORE_ACTION_LOCATION_KEY));
        assertEquals(GameLocationService.LAST_TOWER, metadataCaptor.getValue().get(EXPLORE_ACTION_LOCATION_KEY));
        assert(metadataCaptor.getValue().containsKey(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED));
        assertEquals(R.id.tower_button, metadataCaptor.getValue().get(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED));
    }

    @Test
    public void riverlandsDarkForestClickTest() {
        UserService mockUserService = Mockito.mock(UserService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        UserGameStateService mockuserGameStateService = Mockito.mock(UserGameStateService.class);
        ExploreActionService exploreActionService = Mockito.mock(ExploreActionService.class);
        mainActivity.setExploreActionService(exploreActionService);
        mainActivity.setUserGameStateService(mockuserGameStateService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        UserHomeFragment testedFragment = new UserHomeFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        assertNotNull(returnedView);

        UserGameState testUserGameState = new UserGameState("testuser", GameLocationService.RIVERLANDS,51, 6);
        UserGameStateFetchResponseEvent userGameStateFetchResponseEvent = new UserGameStateFetchResponseEvent(testUserGameState,new HashMap<>());
        testedFragment.publishEvent(userGameStateFetchResponseEvent);

        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.dark_forest_button).getVisibility());

        returnedView.findViewById(R.id.dark_forest_button).performClick();

        ArgumentCaptor<Map> metadataCaptor = ArgumentCaptor.forClass(Map.class);
        verify(exploreActionService).exploreAction(metadataCaptor.capture());

        assertNotNull(metadataCaptor.getValue());
        assert(metadataCaptor.getValue().containsKey(EXPLORE_ACTION_LOCATION_KEY));
        assertEquals(GameLocationService.RIVERLANDS, metadataCaptor.getValue().get(EXPLORE_ACTION_LOCATION_KEY));
        assert(metadataCaptor.getValue().containsKey(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED));
        assertEquals(R.id.dark_forest_button, metadataCaptor.getValue().get(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED));
    }

    @Test
    public void riverlandsRiversideClickTest() {
        UserService mockUserService = Mockito.mock(UserService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        UserGameStateService mockuserGameStateService = Mockito.mock(UserGameStateService.class);
        ExploreActionService exploreActionService = Mockito.mock(ExploreActionService.class);
        mainActivity.setExploreActionService(exploreActionService);
        mainActivity.setUserGameStateService(mockuserGameStateService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        UserHomeFragment testedFragment = new UserHomeFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        assertNotNull(returnedView);

        UserGameState testUserGameState = new UserGameState("testuser", GameLocationService.RIVERLANDS,51, 6);
        UserGameStateFetchResponseEvent userGameStateFetchResponseEvent = new UserGameStateFetchResponseEvent(testUserGameState,new HashMap<>());
        testedFragment.publishEvent(userGameStateFetchResponseEvent);

        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.river_button).getVisibility());

        returnedView.findViewById(R.id.river_button).performClick();

        ArgumentCaptor<Map> metadataCaptor = ArgumentCaptor.forClass(Map.class);
        verify(exploreActionService).exploreAction(metadataCaptor.capture());

        assertNotNull(metadataCaptor.getValue());
        assert(metadataCaptor.getValue().containsKey(EXPLORE_ACTION_LOCATION_KEY));
        assertEquals(GameLocationService.RIVERLANDS, metadataCaptor.getValue().get(EXPLORE_ACTION_LOCATION_KEY));
        assert(metadataCaptor.getValue().containsKey(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED));
        assertEquals(R.id.river_button, metadataCaptor.getValue().get(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED));
    }

    @Test
    public void hillsCaveClickTest() {
        UserService mockUserService = Mockito.mock(UserService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        UserGameStateService mockuserGameStateService = Mockito.mock(UserGameStateService.class);
        ExploreActionService exploreActionService = Mockito.mock(ExploreActionService.class);
        mainActivity.setExploreActionService(exploreActionService);
        mainActivity.setUserGameStateService(mockuserGameStateService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        UserHomeFragment testedFragment = new UserHomeFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        assertNotNull(returnedView);

        UserGameState testUserGameState = new UserGameState("testuser", GameLocationService.HILLS,51, 6);
        UserGameStateFetchResponseEvent userGameStateFetchResponseEvent = new UserGameStateFetchResponseEvent(testUserGameState,new HashMap<>());
        testedFragment.publishEvent(userGameStateFetchResponseEvent);

        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.cave_button).getVisibility());

        returnedView.findViewById(R.id.cave_button).performClick();

        ArgumentCaptor<Map> metadataCaptor = ArgumentCaptor.forClass(Map.class);
        verify(exploreActionService).exploreAction(metadataCaptor.capture());

        assertNotNull(metadataCaptor.getValue());
        assert(metadataCaptor.getValue().containsKey(EXPLORE_ACTION_LOCATION_KEY));
        assertEquals(GameLocationService.HILLS, metadataCaptor.getValue().get(EXPLORE_ACTION_LOCATION_KEY));
        assert(metadataCaptor.getValue().containsKey(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED));
        assertEquals(R.id.cave_button, metadataCaptor.getValue().get(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED));
    }

    @Test
    public void hillsTreesClickTest() {
        UserService mockUserService = Mockito.mock(UserService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        UserGameStateService mockuserGameStateService = Mockito.mock(UserGameStateService.class);
        ExploreActionService exploreActionService = Mockito.mock(ExploreActionService.class);
        mainActivity.setExploreActionService(exploreActionService);
        mainActivity.setUserGameStateService(mockuserGameStateService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        UserHomeFragment testedFragment = new UserHomeFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        assertNotNull(returnedView);

        UserGameState testUserGameState = new UserGameState("testuser", GameLocationService.HILLS,51, 6);
        UserGameStateFetchResponseEvent userGameStateFetchResponseEvent = new UserGameStateFetchResponseEvent(testUserGameState,new HashMap<>());
        testedFragment.publishEvent(userGameStateFetchResponseEvent);

        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.copse_of_trees_button).getVisibility());

        returnedView.findViewById(R.id.copse_of_trees_button).performClick();

        ArgumentCaptor<Map> metadataCaptor = ArgumentCaptor.forClass(Map.class);
        verify(exploreActionService).exploreAction(metadataCaptor.capture());

        assertNotNull(metadataCaptor.getValue());
        assert(metadataCaptor.getValue().containsKey(EXPLORE_ACTION_LOCATION_KEY));
        assertEquals(GameLocationService.HILLS, metadataCaptor.getValue().get(EXPLORE_ACTION_LOCATION_KEY));
        assert(metadataCaptor.getValue().containsKey(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED));
        assertEquals(R.id.copse_of_trees_button, metadataCaptor.getValue().get(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED));
    }

    @Test
    public void northRoadBeachClickTest() {
        UserService mockUserService = Mockito.mock(UserService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        UserGameStateService mockuserGameStateService = Mockito.mock(UserGameStateService.class);
        ExploreActionService exploreActionService = Mockito.mock(ExploreActionService.class);
        mainActivity.setExploreActionService(exploreActionService);
        mainActivity.setUserGameStateService(mockuserGameStateService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        UserHomeFragment testedFragment = new UserHomeFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        assertNotNull(returnedView);

        UserGameState testUserGameState = new UserGameState("testuser", GameLocationService.NORTH_ROAD,51, 6);
        UserGameStateFetchResponseEvent userGameStateFetchResponseEvent = new UserGameStateFetchResponseEvent(testUserGameState,new HashMap<>());
        testedFragment.publishEvent(userGameStateFetchResponseEvent);

        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.beach_button).getVisibility());

        returnedView.findViewById(R.id.beach_button).performClick();

        ArgumentCaptor<Map> metadataCaptor = ArgumentCaptor.forClass(Map.class);
        verify(exploreActionService).exploreAction(metadataCaptor.capture());

        assertNotNull(metadataCaptor.getValue());
        assert(metadataCaptor.getValue().containsKey(EXPLORE_ACTION_LOCATION_KEY));
        assertEquals(GameLocationService.NORTH_ROAD, metadataCaptor.getValue().get(EXPLORE_ACTION_LOCATION_KEY));
        assert(metadataCaptor.getValue().containsKey(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED));
        assertEquals(R.id.beach_button, metadataCaptor.getValue().get(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED));
    }

    @Test
    public void northRoadBanditCampClickTest() {
        UserService mockUserService = Mockito.mock(UserService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        UserGameStateService mockuserGameStateService = Mockito.mock(UserGameStateService.class);
        ExploreActionService exploreActionService = Mockito.mock(ExploreActionService.class);
        mainActivity.setExploreActionService(exploreActionService);
        mainActivity.setUserGameStateService(mockuserGameStateService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        UserHomeFragment testedFragment = new UserHomeFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        assertNotNull(returnedView);

        UserGameState testUserGameState = new UserGameState("testuser", GameLocationService.NORTH_ROAD,51, 6);
        UserGameStateFetchResponseEvent userGameStateFetchResponseEvent = new UserGameStateFetchResponseEvent(testUserGameState,new HashMap<>());
        testedFragment.publishEvent(userGameStateFetchResponseEvent);

        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.bandit_camp_button).getVisibility());

        returnedView.findViewById(R.id.bandit_camp_button).performClick();

        ArgumentCaptor<Map> metadataCaptor = ArgumentCaptor.forClass(Map.class);
        verify(exploreActionService).exploreAction(metadataCaptor.capture());

        assertNotNull(metadataCaptor.getValue());
        assert(metadataCaptor.getValue().containsKey(EXPLORE_ACTION_LOCATION_KEY));
        assertEquals(GameLocationService.NORTH_ROAD, metadataCaptor.getValue().get(EXPLORE_ACTION_LOCATION_KEY));
        assert(metadataCaptor.getValue().containsKey(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED));
        assertEquals(R.id.bandit_camp_button, metadataCaptor.getValue().get(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED));
    }

    @Test
    public void publishExploreActionEventNothingTest() {
        UserService mockUserService = Mockito.mock(UserService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        UserGameStateService mockuserGameStateService = Mockito.mock(UserGameStateService.class);
        mainActivity.setUserGameStateService(mockuserGameStateService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        UserHomeFragment testedFragment = new UserHomeFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        assertNotNull(returnedView);
        String nothingFoundFlavorText = "Nothing found flavor text";
        NothingFoundActionResult nothingFoundActionResult = new NothingFoundActionResult(nothingFoundFlavorText);
        ExploreActionEvent exploreActionEvent = new ExploreActionEvent(nothingFoundActionResult, new HashMap<>());
        testedFragment.publishEvent(exploreActionEvent);

        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.nothing_found_card_view).getVisibility());
        assertEquals(nothingFoundFlavorText, ((TextView)returnedView.findViewById(R.id.nothing_found_flavor_text)).getText());
    }

    @Test
    public void publishExploreActionEventDialogTest() {
        UserService mockUserService = Mockito.mock(UserService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        UserGameStateService mockuserGameStateService = Mockito.mock(UserGameStateService.class);
        mainActivity.setUserGameStateService(mockuserGameStateService);
        DialogService mockDialogService = Mockito.mock(DialogService.class);

        mainActivity.setDialogService(mockDialogService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        UserHomeFragment testedFragment = new UserHomeFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        UserGameState testUserGameState = new UserGameState("testId",GameLocationService.BRIDGETON,1000,2);
        UserGameStateFetchResponseEvent userGameStateFetchResponseEvent = new UserGameStateFetchResponseEvent(testUserGameState, new HashMap<>());

        testedFragment.publishEvent(userGameStateFetchResponseEvent);

        assertNotNull(returnedView);
        Dialog baseDialog = new Dialog("base", "base dialog flavor text","no option text", new DialogAffect(DialogAffectType.NONE), false);
        Dialog dialog1 = new Dialog("dialog1", "dialog 1 flavor text", "option 1 text", new DialogAffect(DialogAffectType.NONE), false);
        baseDialog.setDialogOption1(dialog1.getReferenceId());
        Dialog dialog2 = new Dialog("dialog2", "dialog 2 flavor text", "option 2 text", new DialogAffect(DialogAffectType.NONE), false);
        baseDialog.setDialogOption2(dialog2.getReferenceId());
        Dialog dialog3 = new Dialog("dialog3", "dialog 3 flavor text", "option 3 text", new DialogAffect(DialogAffectType.NONE), false);
        baseDialog.setDialogOption3(dialog3.getReferenceId());
        Dialog dialog4 = new Dialog("dialog4", "dialog 4 flavor text", "option 4 text", new DialogAffect(DialogAffectType.NONE), false);
        baseDialog.setDialogOption4(dialog4.getReferenceId());


        DialogActionResult dialogActionResult = new DialogActionResult(baseDialog.getReferenceId(),false);
        ExploreActionEvent exploreActionEvent = new ExploreActionEvent(dialogActionResult, new HashMap<>());
        testedFragment.publishEvent(exploreActionEvent);

        verify(mockDialogService).fetchBaseDialog(eq(baseDialog.getReferenceId()),eq(false), anyMap());


        BaseDialogFetchEvent baseDialogFetchEvent = new BaseDialogFetchEvent(baseDialog, new HashMap<>());
        testedFragment.publishEvent(baseDialogFetchEvent);

        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.dialog_card_view).getVisibility());
        assertEquals(baseDialog.getFlavorText(), ((TextView)returnedView.findViewById(R.id.dialog_flavor_text)).getText());

        verify(mockDialogService).fetchDialogOptions(eq(baseDialog),anyMap());

        DialogFetchEvent dialogFetchEvent = new DialogFetchEvent(dialog1, dialog2, dialog3, dialog4, new HashMap<>());
        testedFragment.publishEvent(dialogFetchEvent);

        ((Button)returnedView.findViewById(R.id.dialog_option_1_button)).callOnClick();
        verify(mockDialogService).selectDialogOption(eq(dialog1), eq(GameLocationService.BRIDGETON), anyMap());
        ((Button)returnedView.findViewById(R.id.dialog_option_2_button)).callOnClick();
        verify(mockDialogService).selectDialogOption(eq(dialog2), eq(GameLocationService.BRIDGETON), anyMap());
        ((Button)returnedView.findViewById(R.id.dialog_option_3_button)).callOnClick();
        verify(mockDialogService).selectDialogOption(eq(dialog3), eq(GameLocationService.BRIDGETON), anyMap());
        ((Button)returnedView.findViewById(R.id.dialog_option_4_button)).callOnClick();
        verify(mockDialogService).selectDialogOption(eq(dialog4), eq(GameLocationService.BRIDGETON), anyMap());

    }

    @Test
    public void verifyCantStartNewExploreActionWhenOneIsActiveTest() {
        UserService mockUserService = Mockito.mock(UserService.class);
        ExploreActionService mockExploreActionService = Mockito.mock(ExploreActionService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setExploreActionService(mockExploreActionService);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        UserGameStateService mockuserGameStateService = Mockito.mock(UserGameStateService.class);
        mainActivity.setUserGameStateService(mockuserGameStateService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        UserHomeFragment testedFragment = new UserHomeFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        UserGameState testUserGameState = new UserGameState("testuser", GameLocationService.THANADEL_VILLAGE,51,2);
        UserGameStateFetchResponseEvent userGameStateFetchResponseEvent = new UserGameStateFetchResponseEvent(testUserGameState,new HashMap<>());
        testedFragment.publishEvent(userGameStateFetchResponseEvent);

        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.general_store_button).getVisibility());

        returnedView.findViewById(R.id.general_store_button).performClick();
        String nothingFoundFlavorText = "Nothing found flavor text";
        NothingFoundActionResult nothingFoundActionResult = new NothingFoundActionResult(nothingFoundFlavorText);
        ExploreActionEvent exploreActionEvent = new ExploreActionEvent(nothingFoundActionResult, new HashMap<>());
        testedFragment.publishEvent(exploreActionEvent);
        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.nothing_found_card_view).getVisibility());

        returnedView.findViewById(R.id.general_store_button).performClick();
        returnedView.findViewById(R.id.general_store_button).performClick();
        returnedView.findViewById(R.id.general_store_button).performClick();
        returnedView.findViewById(R.id.general_store_button).performClick();
        returnedView.findViewById(R.id.general_store_button).performClick();
        verify(mockExploreActionService, times(1)).exploreAction(anyMap());

    }

    @Test
    public void questButtonClickedTest() {
        UserService mockUserService = Mockito.mock(UserService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        UserGameStateService mockuserGameStateService = Mockito.mock(UserGameStateService.class);
        mainActivity.setUserGameStateService(mockuserGameStateService);
        QuestService questService = Mockito.mock(QuestService.class);
        mainActivity.setQuestService(questService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        UserHomeFragment testedFragment = new UserHomeFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        assertNotNull(returnedView);
        returnedView.findViewById(R.id.quests_button).performClick();

        verify(questService).fetchQuests(anyMap());
        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.quests_view).getVisibility());
    }

    @Test
    public void questFetchResponseEventTest() {
        UserService mockUserService = Mockito.mock(UserService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        UserGameStateService mockuserGameStateService = Mockito.mock(UserGameStateService.class);
        mainActivity.setUserGameStateService(mockuserGameStateService);
        QuestService questService = Mockito.mock(QuestService.class);
        mainActivity.setQuestService(questService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        UserHomeFragment testedFragment = new UserHomeFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        String testUuid = UUID.randomUUID().toString();
        QuestObjective objective1 = new QuestObjective(QuestObjectiveType.FIGHT,"1", GameLocationService.WOODLANDS,1,false);
        QuestObjective objective2 = new QuestObjective(QuestObjectiveType.VISIT,"2", GameLocationService.THANADEL_VILLAGE,1,false);

        Quest quest1 = new Quest("testQuest", testUuid, 8, Arrays.asList(objective1, objective2));
        String testUuid2 = UUID.randomUUID().toString();
        QuestObjective objective3 = new QuestObjective(QuestObjectiveType.FIGHT,"3", GameLocationService.WOODLANDS,1,false);
        QuestObjective objective4 = new QuestObjective(QuestObjectiveType.VISIT,"4", GameLocationService.THANADEL_VILLAGE,1,false);

        Quest quest2 = new Quest("testQuest", testUuid2, 8, Arrays.asList(objective3, objective4));

        returnedView.findViewById(R.id.quests_button).performClick();
        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.quests_view).getVisibility());
        QuestFetchResponseEvent questFetchResponseEvent = new QuestFetchResponseEvent(Arrays.asList(quest1, quest2),new HashMap<>());

        testedFragment.publishEvent(questFetchResponseEvent);

        RecyclerView questRecyclerView = (RecyclerView)returnedView.findViewById(R.id.quest_recyclerView);
        assertEquals(2, questRecyclerView.getAdapter().getItemCount());
    }
}
