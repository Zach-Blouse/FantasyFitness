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
import com.zblouse.fantasyfitness.dialog.Dialog;
import com.zblouse.fantasyfitness.dialog.DialogService;
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

import java.util.HashMap;
import java.util.Map;

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

        UserGameState testUserGameState = new UserGameState("testuser", GameLocationService.THANADEL_VILLAGE,51);
        UserGameStateFetchResponseEvent userGameStateFetchResponseEvent = new UserGameStateFetchResponseEvent(testUserGameState,new HashMap<>());
        testedFragment.publishEvent(userGameStateFetchResponseEvent);

        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.inn_button).getVisibility());
        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.general_store_button).getVisibility());
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

        UserGameState testUserGameState = new UserGameState("testuser", GameLocationService.WOODLANDS,51);
        UserGameStateFetchResponseEvent userGameStateFetchResponseEvent = new UserGameStateFetchResponseEvent(testUserGameState,new HashMap<>());
        testedFragment.publishEvent(userGameStateFetchResponseEvent);

        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.dark_forest_button).getVisibility());
        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.cave_button).getVisibility());
        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.marsh_button).getVisibility());
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

        UserGameState testUserGameState = new UserGameState("testuser", GameLocationService.WOODLANDS,51);
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

        UserGameState testUserGameState = new UserGameState("testuser", GameLocationService.WOODLANDS,51);
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

        UserGameState testUserGameState = new UserGameState("testuser", GameLocationService.WOODLANDS,51);
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

        UserGameState testUserGameState = new UserGameState("testuser", GameLocationService.THANADEL_VILLAGE,51);
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

        UserGameState testUserGameState = new UserGameState("testuser", GameLocationService.THANADEL_VILLAGE,51);
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

        assertNotNull(returnedView);
        Dialog baseDialog = new Dialog("base", "base dialog flavor text","no option text");
        Dialog dialog1 = new Dialog("dialog1", "dialog 1 flavor text", "option 1 text");
        baseDialog.setDialogOption1(dialog1.getReferenceId());
        Dialog dialog2 = new Dialog("dialog2", "dialog 2 flavor text", "option 2 text");
        baseDialog.setDialogOption2(dialog2.getReferenceId());
        Dialog dialog3 = new Dialog("dialog3", "dialog 3 flavor text", "option 3 text");
        baseDialog.setDialogOption3(dialog3.getReferenceId());
        Dialog dialog4 = new Dialog("dialog4", "dialog 4 flavor text", "option 4 text");
        baseDialog.setDialogOption4(dialog4.getReferenceId());

        when(mockDialogService.fetchDialogOption(dialog1.getReferenceId())).thenReturn(dialog1);
        when(mockDialogService.fetchDialogOption(dialog2.getReferenceId())).thenReturn(dialog2);
        when(mockDialogService.fetchDialogOption(dialog3.getReferenceId())).thenReturn(dialog3);
        when(mockDialogService.fetchDialogOption(dialog4.getReferenceId())).thenReturn(dialog4);

        DialogActionResult dialogActionResult = new DialogActionResult(baseDialog);
        ExploreActionEvent exploreActionEvent = new ExploreActionEvent(dialogActionResult, new HashMap<>());
        testedFragment.publishEvent(exploreActionEvent);

        assertEquals(View.VISIBLE, returnedView.findViewById(R.id.dialog_card_view).getVisibility());
        assertEquals(baseDialog.getFlavorText(), ((TextView)returnedView.findViewById(R.id.dialog_flavor_text)).getText());
        assertEquals(dialog1.getOptionText(), ((Button)returnedView.findViewById(R.id.dialog_option_1_button)).getText());
        assertEquals(dialog2.getOptionText(), ((Button)returnedView.findViewById(R.id.dialog_option_2_button)).getText());
        assertEquals(dialog3.getOptionText(), ((Button)returnedView.findViewById(R.id.dialog_option_3_button)).getText());
        assertEquals(dialog4.getOptionText(), ((Button)returnedView.findViewById(R.id.dialog_option_4_button)).getText());

        ((Button)returnedView.findViewById(R.id.dialog_option_1_button)).callOnClick();
        verify(mockDialogService).selectDialogOption(eq(dialog1.getReferenceId()), anyMap());
        ((Button)returnedView.findViewById(R.id.dialog_option_2_button)).callOnClick();
        verify(mockDialogService).selectDialogOption(eq(dialog2.getReferenceId()), anyMap());
        ((Button)returnedView.findViewById(R.id.dialog_option_3_button)).callOnClick();
        verify(mockDialogService).selectDialogOption(eq(dialog3.getReferenceId()), anyMap());
        ((Button)returnedView.findViewById(R.id.dialog_option_4_button)).callOnClick();
        verify(mockDialogService).selectDialogOption(eq(dialog4.getReferenceId()), anyMap());

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

        UserGameState testUserGameState = new UserGameState("testuser", GameLocationService.THANADEL_VILLAGE,51);
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
}
