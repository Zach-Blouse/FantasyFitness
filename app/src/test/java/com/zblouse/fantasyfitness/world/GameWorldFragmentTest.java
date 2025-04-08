package com.zblouse.fantasyfitness.world;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.zblouse.fantasyfitness.R;
import com.zblouse.fantasyfitness.activity.LocationEvent;
import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.user.UserGameState;
import com.zblouse.fantasyfitness.user.UserGameStateFetchResponseEvent;
import com.zblouse.fantasyfitness.user.UserGameStateService;
import com.zblouse.fantasyfitness.user.UserGameStateUpdateEvent;
import com.zblouse.fantasyfitness.user.UserService;
import com.zblouse.fantasyfitness.workout.WorkoutFragment;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.HashMap;
import java.util.Map;

@RunWith(RobolectricTestRunner.class)
public class GameWorldFragmentTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);

    @Test
    public void onCreateTest() {
        UserService mockUserService = Mockito.mock(UserService.class);
        UserGameStateService mockuserGameStateService = Mockito.mock(UserGameStateService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserGameStateService(mockuserGameStateService);
        mainActivity.setUserService(mockUserService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        GameWorldFragment testedFragment = new GameWorldFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        assertEquals(View.INVISIBLE,returnedView.findViewById(R.id.location_info_view).getVisibility());
        assertNotNull(returnedView.findViewById(R.id.world_map_vertical));
        assertNotNull(returnedView.findViewById(R.id.world_map_horizontal));
        assertNotNull(returnedView.findViewById(R.id.valley_of_monsters));
        assertNotNull(returnedView.findViewById(R.id.tower));
        assertNotNull(returnedView.findViewById(R.id.arduwyn));
        assertNotNull(returnedView.findViewById(R.id.monastary));
        assertNotNull(returnedView.findViewById(R.id.north_road));
        assertNotNull(returnedView.findViewById(R.id.faolyn));
        assertNotNull(returnedView.findViewById(R.id.riverlands));
        assertNotNull(returnedView.findViewById(R.id.bridgeton));
        assertNotNull(returnedView.findViewById(R.id.mountain_pass));
        assertNotNull(returnedView.findViewById(R.id.woodlands));
        assertNotNull(returnedView.findViewById(R.id.thanadel_village));
        assertNotNull(returnedView.findViewById(R.id.farmlands));
        assertNotNull(returnedView.findViewById(R.id.hills));
    }

    @Test
    public void clickLocationsTest() {
        UserService mockUserService = Mockito.mock(UserService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        UserGameStateService mockuserGameStateService = Mockito.mock(UserGameStateService.class);
        GameLocationService mockLocationService = Mockito.mock(GameLocationService.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        mainActivity.setUserGameStateService(mockuserGameStateService);
        mainActivity.setGameLocationService(mockLocationService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        GameWorldFragment testedFragment = new GameWorldFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);
        returnedView.findViewById(R.id.valley_of_monsters).performClick();
        verify(mockLocationService).fetchLocation(eq(GameLocationService.VALLEY_OF_MONSTERS), any());
        returnedView.findViewById(R.id.tower).performClick();
        verify(mockLocationService).fetchLocation(eq(GameLocationService.LAST_TOWER), any());
        returnedView.findViewById(R.id.arduwyn).performClick();
        verify(mockLocationService).fetchLocation(eq(GameLocationService.ARDUWYN), any());
        returnedView.findViewById(R.id.monastary).performClick();
        verify(mockLocationService).fetchLocation(eq(GameLocationService.MONASTARY), any());
        returnedView.findViewById(R.id.north_road).performClick();
        verify(mockLocationService).fetchLocation(eq(GameLocationService.NORTH_ROAD), any());
        returnedView.findViewById(R.id.faolyn).performClick();
        verify(mockLocationService).fetchLocation(eq(GameLocationService.FAOLYN), any());
        returnedView.findViewById(R.id.riverlands).performClick();
        verify(mockLocationService).fetchLocation(eq(GameLocationService.RIVERLANDS), any());
        returnedView.findViewById(R.id.bridgeton).performClick();
        verify(mockLocationService).fetchLocation(eq(GameLocationService.BRIDGETON), any());
        returnedView.findViewById(R.id.mountain_pass).performClick();
        verify(mockLocationService).fetchLocation(eq(GameLocationService.MOUNTAIN_PASS), any());
        returnedView.findViewById(R.id.woodlands).performClick();
        verify(mockLocationService).fetchLocation(eq(GameLocationService.WOODLANDS), any());
        returnedView.findViewById(R.id.thanadel_village).performClick();
        verify(mockLocationService).fetchLocation(eq(GameLocationService.THANADEL_VILLAGE), any());
        returnedView.findViewById(R.id.farmlands).performClick();
        verify(mockLocationService).fetchLocation(eq(GameLocationService.FARMLANDS), any());
        returnedView.findViewById(R.id.hills).performClick();
        verify(mockLocationService).fetchLocation(eq(GameLocationService.HILLS), any());
    }

    @Test
    public void publishEventTest() {
        UserService mockUserService = Mockito.mock(UserService.class);
        UserGameStateService mockuserGameStateService = Mockito.mock(UserGameStateService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        mainActivity.setUserGameStateService(mockuserGameStateService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        GameWorldFragment testedFragment = new GameWorldFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);
        String testLocationName = "testLocation1";
        String testLocationDescription = "testlocation1description";
        Map<GameLocation, Double> connectedLocations = new HashMap<>();
        String testLocation2Name = "testLocation2";
        String testLocation3Name = "testLocation3";
        GameLocation gameLocation2 = new GameLocation(2, testLocation2Name,"");
        GameLocation gameLocation3 = new GameLocation(3, testLocation3Name, "");
        connectedLocations.put(gameLocation2, 5.0);
        connectedLocations.put(gameLocation3, 7.4);
        GameLocation testGameLocation = new GameLocation(1,testLocationName, testLocationDescription,connectedLocations);
        testedFragment.publishEvent(new GameLocationFetchEvent(testGameLocation, new HashMap<>()));
        assertEquals(View.VISIBLE,returnedView.findViewById(R.id.location_info_view).getVisibility());
        assertEquals(testLocationName,((TextView)returnedView.findViewById(R.id.location_info_name)).getText());
        assertEquals(testLocationDescription,((TextView)returnedView.findViewById(R.id.location_info_description)).getText());
        assert(((TextView)returnedView.findViewById(R.id.location_info_connected_locations)).getText().toString().contains("testLocation3 7.4"));
        assert(((TextView)returnedView.findViewById(R.id.location_info_connected_locations)).getText().toString().contains("testLocation2 5.0"));
    }

    @Test
    public void publishUserGameStateUpdateEventTest() {
        UserService mockUserService = Mockito.mock(UserService.class);
        UserGameStateService mockuserGameStateService = Mockito.mock(UserGameStateService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        String testUserId = "testUser1";
        String testUserId2 = "testUserId2";
        when(mockUser.getUid()).thenReturn(testUserId,testUserId2);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        mainActivity.setUserGameStateService(mockuserGameStateService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        GameWorldFragment testedFragment = new GameWorldFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);
        verify(mockuserGameStateService, times(1)).fetchUserGameState(eq(testUserId), anyMap());

        Map<String, Object> userGameStateUpdateMetadata = new HashMap<>();
        userGameStateUpdateMetadata.put(UserGameStateService.NEW_LOCATION,UserGameStateService.LOCATION_FIELD);
        UserGameStateUpdateEvent userGameStateUpdateEvent = new UserGameStateUpdateEvent(userGameStateUpdateMetadata);
        testedFragment.publishEvent(userGameStateUpdateEvent);

        verify(mockuserGameStateService, times(1)).fetchUserGameState(eq(testUserId2), anyMap());
    }

    @Test
    public void publishUserGameStateFetchResponseEventTest() {
        UserService mockUserService = Mockito.mock(UserService.class);
        UserGameStateService mockuserGameStateService = Mockito.mock(UserGameStateService.class);
        GameLocationService mockGameLocationService = Mockito.mock(GameLocationService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        String testUserId = "testUser1";
        when(mockUser.getUid()).thenReturn(testUserId);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setGameLocationService(mockGameLocationService);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        mainActivity.setUserGameStateService(mockuserGameStateService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        GameWorldFragment testedFragment = new GameWorldFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        UserGameState userGameState = new UserGameState(testUserId,GameLocationService.MONASTARY,150);

        UserGameStateFetchResponseEvent userGameStateFetchResponseEvent = new UserGameStateFetchResponseEvent(userGameState, new HashMap<>());

        testedFragment.publishEvent(userGameStateFetchResponseEvent);
        verify(mockGameLocationService).generatePaths(eq(GameLocationService.MONASTARY));
    }
}
