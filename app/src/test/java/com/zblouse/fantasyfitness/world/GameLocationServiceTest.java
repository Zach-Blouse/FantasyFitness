package com.zblouse.fantasyfitness.world;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.firebase.auth.FirebaseUser;
import com.zblouse.fantasyfitness.activity.DeviceServiceType;
import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.activity.ToastDeviceService;
import com.zblouse.fantasyfitness.combat.encounter.EncounterDifficultyLevel;
import com.zblouse.fantasyfitness.core.DomainService;
import com.zblouse.fantasyfitness.user.UserGameState;
import com.zblouse.fantasyfitness.user.UserGameStateService;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameLocationServiceTest {

    @Test
    public void fetchLocationTest(){
        String location1Name = "location1";
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        GameLocationRepository mockGameLocationRepository = Mockito.mock(GameLocationRepository.class);
        GameLocationService testedService = new GameLocationService(mockActivity, mockGameLocationRepository);
        testedService.fetchLocation(location1Name, new HashMap<>());
        verify(mockGameLocationRepository).getLocationByName(eq(location1Name),eq(testedService), anyMap());

    }

    @Test
    public void repositoryResponseTest(){
        String location1Name = "location1";
        GameLocation location = new GameLocation(1,location1Name,"test description 1");
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        GameLocationRepository mockGameLocationRepository = Mockito.mock(GameLocationRepository.class);
        GameLocationService testedService = new GameLocationService(mockActivity, mockGameLocationRepository);
        ArgumentCaptor<GameLocationFetchEvent> gameLocationFetchEventArgumentCaptor = ArgumentCaptor.forClass(GameLocationFetchEvent.class);

        testedService.repositoryResponse(location,new HashMap<>());
        verify(mockActivity).publishEvent(gameLocationFetchEventArgumentCaptor.capture());
        assertEquals(location1Name, gameLocationFetchEventArgumentCaptor.getValue().getLocation().getLocationName());
    }

    @Test
    public void initializeRepositoryAlreadyInitializedTest(){
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        GameLocationRepository mockGameLocationRepository = Mockito.mock(GameLocationRepository.class);
        when(mockGameLocationRepository.databaseInitialized()).thenReturn(true);
        GameLocationService testedService = new GameLocationService(mockActivity, mockGameLocationRepository);
        testedService.initializeLocationDatabase();
        verify(mockGameLocationRepository,times(0)).createGameLocation(any());
        verify(mockGameLocationRepository,times(0)).addGameLocationConnection(any(),any(),anyDouble());
    }

    @Test
    public void initializeRepositoryNotAlreadyInitializedTest(){
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        GameLocationRepository mockGameLocationRepository = Mockito.mock(GameLocationRepository.class);
        when(mockGameLocationRepository.databaseInitialized()).thenReturn(false);
        GameLocationService testedService = new GameLocationService(mockActivity, mockGameLocationRepository);
        testedService.initializeLocationDatabase();
        ArgumentCaptor<GameLocation> gameLocationArgumentCaptor = ArgumentCaptor.forClass(GameLocation.class);
        verify(mockGameLocationRepository,times(13)).createGameLocation(any());
        verify(mockGameLocationRepository,times(16)).addGameLocationConnection(any(),any(),anyDouble());

    }

    @Test
    public void generatePathsTest(){
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        GameLocationRepository mockGameLocationRepository = Mockito.mock(GameLocationRepository.class);
        when(mockGameLocationRepository.databaseInitialized()).thenReturn(true);
        Map<Integer, GameLocation> locationMap = getTestAllLocations();
        when(mockGameLocationRepository.getAllGameLocations()).thenReturn(new ArrayList<>(getTestAllLocations().values()));
        when(mockGameLocationRepository.getLocationById(1)).thenReturn(locationMap.get(1));
        when(mockGameLocationRepository.getLocationById(2)).thenReturn(locationMap.get(2));
        when(mockGameLocationRepository.getLocationById(3)).thenReturn(locationMap.get(3));
        when(mockGameLocationRepository.getLocationById(4)).thenReturn(locationMap.get(4));
        when(mockGameLocationRepository.getLocationById(5)).thenReturn(locationMap.get(5));
        when(mockGameLocationRepository.getLocationById(6)).thenReturn(locationMap.get(6));
        GameLocationService testedService = new GameLocationService(mockActivity, mockGameLocationRepository);

        GameLocationPaths responsePaths = testedService.generatePaths(GameLocationService.ARDUWYN);

        assertEquals(GameLocationService.ARDUWYN, responsePaths.getStartLocation().getLocationName());
        assertEquals(5.0,responsePaths.getPath(GameLocationService.NORTH_ROAD).getDistanceKm(),0.0);
        assertEquals(Arrays.asList(locationMap.get(2)), responsePaths.getPath(GameLocationService.NORTH_ROAD).getLocationPath());
        assertEquals(3.0,responsePaths.getPath(GameLocationService.MONASTARY).getDistanceKm(),0.0);
        assertEquals(Arrays.asList(locationMap.get(3)), responsePaths.getPath(GameLocationService.MONASTARY).getLocationPath());
        assertEquals(4.0,responsePaths.getPath(GameLocationService.FAOLYN).getDistanceKm(),0.0);
        assertEquals(Arrays.asList(locationMap.get(3), locationMap.get(4)), responsePaths.getPath(GameLocationService.FAOLYN).getLocationPath());
        assertEquals(8.0,responsePaths.getPath(GameLocationService.HILLS).getDistanceKm(),0.0);
        assertEquals(Arrays.asList(locationMap.get(3), locationMap.get(5)), responsePaths.getPath(GameLocationService.HILLS).getLocationPath());
        assertEquals(7.0,responsePaths.getPath(GameLocationService.RIVERLANDS).getDistanceKm(),0.0);
        assertEquals(Arrays.asList(locationMap.get(3), locationMap.get(4), locationMap.get(6)), responsePaths.getPath(GameLocationService.RIVERLANDS).getLocationPath());
    }

    @Test
    public void generatePathsNullTest(){
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        GameLocationRepository mockGameLocationRepository = Mockito.mock(GameLocationRepository.class);
        when(mockGameLocationRepository.databaseInitialized()).thenReturn(true);
        when(mockGameLocationRepository.getAllGameLocations()).thenReturn(new ArrayList<>(getTestAllLocations().values()));
        GameLocationService testedService = new GameLocationService(mockActivity, mockGameLocationRepository);

        GameLocationPaths responsePaths = testedService.generatePaths(null);
        assertNull(responsePaths);
    }

    @Test
    public void travelTest(){
        String testUserId = "testUser1";
        GameLocation connectionlessArduwyn = new GameLocation(1,GameLocationService.ARDUWYN,"testDescription");
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        when(mockUser.getUid()).thenReturn(testUserId);
        when(mockActivity.getCurrentUser()).thenReturn(mockUser);
        UserGameStateService mockUserGameStateService = Mockito.mock(UserGameStateService.class);
        when(mockActivity.getUserGameStateService()).thenReturn(mockUserGameStateService);
        GameLocationRepository mockGameLocationRepository = Mockito.mock(GameLocationRepository.class);
        when(mockGameLocationRepository.databaseInitialized()).thenReturn(true);
        GameLocationService testedService = new GameLocationService(mockActivity, mockGameLocationRepository);
        testedService.travel(connectionlessArduwyn);
        ArgumentCaptor<Map> mapArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        verify(mockUserGameStateService).fetchUserGameState(eq(testUserId), mapArgumentCaptor.capture());
        assert(mapArgumentCaptor.getValue().containsKey(DomainService.INTER_DOMAIN_SERVICE_ORIGIN_KEY));
        assertEquals(testedService, mapArgumentCaptor.getValue().get(DomainService.INTER_DOMAIN_SERVICE_ORIGIN_KEY));
        assert(mapArgumentCaptor.getValue().containsKey(DomainService.ORIGIN_FUNCTION));
        assertEquals("travel", mapArgumentCaptor.getValue().get(DomainService.ORIGIN_FUNCTION));
        assert(mapArgumentCaptor.getValue().containsKey("travelDestination"));
        assertEquals(connectionlessArduwyn, mapArgumentCaptor.getValue().get("travelDestination"));
    }

    @Test
    public void interdomainServiceResponseTravelEnoughDistanceTest(){
        String testUserId = "testUser1";
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        when(mockUser.getUid()).thenReturn(testUserId);
        when(mockActivity.getCurrentUser()).thenReturn(mockUser);
        GameLocationRepository mockGameLocationRepository = Mockito.mock(GameLocationRepository.class);
        when(mockGameLocationRepository.databaseInitialized()).thenReturn(true);
        UserGameStateService mockUserGameStateService = Mockito.mock(UserGameStateService.class);
        when(mockActivity.getUserGameStateService()).thenReturn(mockUserGameStateService);
        UserGameState testUserGameState = new UserGameState(testUserId,GameLocationService.ARDUWYN,5500,98);
        GameLocation northRoadLocation = new GameLocation(2,GameLocationService.NORTH_ROAD,"testDescription");
        Map<Integer, GameLocation> locationMap = getTestAllLocations();
        when(mockGameLocationRepository.getAllGameLocations()).thenReturn(new ArrayList<>(getTestAllLocations().values()));
        when(mockGameLocationRepository.getLocationById(1)).thenReturn(locationMap.get(1));
        when(mockGameLocationRepository.getLocationById(2)).thenReturn(locationMap.get(2));
        when(mockGameLocationRepository.getLocationById(3)).thenReturn(locationMap.get(3));
        when(mockGameLocationRepository.getLocationById(4)).thenReturn(locationMap.get(4));
        when(mockGameLocationRepository.getLocationById(5)).thenReturn(locationMap.get(5));
        when(mockGameLocationRepository.getLocationById(6)).thenReturn(locationMap.get(6));
        GameLocationService testedService = new GameLocationService(mockActivity, mockGameLocationRepository);

        Map<String, Object> metadata = new HashMap<>();
        metadata.put(DomainService.INTER_DOMAIN_SERVICE_RESPONSE_CLASS_KEY,UserGameState.class);
        metadata.put(DomainService.ORIGIN_FUNCTION, "travel");
        metadata.put("travelDestination", northRoadLocation);
        testedService.interDomainServiceResponse(testUserGameState,metadata);

        verify(mockUserGameStateService).addUserGameDistance(eq(testUserId),eq(-5000.0),anyMap());
        verify(mockUserGameStateService).updateUserGameLocation(eq(testUserId),eq(northRoadLocation.getLocationName()),anyMap());
    }

    @Test
    public void interdomainServiceResponseTravelNotEnoughDistanceTest(){
        String testUserId = "testUser1";
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        when(mockUser.getUid()).thenReturn(testUserId);
        when(mockActivity.getCurrentUser()).thenReturn(mockUser);
        ToastDeviceService mockToastDeviceService = Mockito.mock(ToastDeviceService.class);
        when(mockActivity.getDeviceService(eq(DeviceServiceType.TOAST))).thenReturn(mockToastDeviceService);
        GameLocationRepository mockGameLocationRepository = Mockito.mock(GameLocationRepository.class);
        when(mockGameLocationRepository.databaseInitialized()).thenReturn(true);
        UserGameStateService mockUserGameStateService = Mockito.mock(UserGameStateService.class);
        when(mockActivity.getUserGameStateService()).thenReturn(mockUserGameStateService);
        UserGameState testUserGameState = new UserGameState(testUserId,GameLocationService.ARDUWYN,2,98);
        GameLocation northRoadLocation = new GameLocation(2,GameLocationService.NORTH_ROAD,"testDescription");
        Map<Integer, GameLocation> locationMap = getTestAllLocations();
        when(mockGameLocationRepository.getAllGameLocations()).thenReturn(new ArrayList<>(getTestAllLocations().values()));
        when(mockGameLocationRepository.getLocationById(1)).thenReturn(locationMap.get(1));
        when(mockGameLocationRepository.getLocationById(2)).thenReturn(locationMap.get(2));
        when(mockGameLocationRepository.getLocationById(3)).thenReturn(locationMap.get(3));
        when(mockGameLocationRepository.getLocationById(4)).thenReturn(locationMap.get(4));
        when(mockGameLocationRepository.getLocationById(5)).thenReturn(locationMap.get(5));
        when(mockGameLocationRepository.getLocationById(6)).thenReturn(locationMap.get(6));
        GameLocationService testedService = new GameLocationService(mockActivity, mockGameLocationRepository);

        Map<String, Object> metadata = new HashMap<>();
        metadata.put(DomainService.INTER_DOMAIN_SERVICE_RESPONSE_CLASS_KEY,UserGameState.class);
        metadata.put(DomainService.ORIGIN_FUNCTION, "travel");
        metadata.put("travelDestination", northRoadLocation);
        testedService.interDomainServiceResponse(testUserGameState,metadata);

        verify(mockUserGameStateService, times(0)).addUserGameDistance(any(),anyDouble(),anyMap());
        verify(mockUserGameStateService, times(0)).updateUserGameLocation(any(),any(),anyMap());
        verify(mockToastDeviceService).sendToast(eq("Unable to travel, you do not have enough saved distance. Go on a run or walk to get more distance."));
    }

    private Map<Integer, GameLocation> getTestAllLocations(){
        Map<Integer, GameLocation> getAllLocationsResponse = new HashMap<>();
        GameLocation connectionlessArduwyn = new GameLocation(1,GameLocationService.ARDUWYN,"testDescription");
        GameLocation connectionlessNorthRoad = new GameLocation(2,GameLocationService.NORTH_ROAD,"testDescription");
        GameLocation connectionlessMonastary = new GameLocation(3, GameLocationService.MONASTARY, "testDescription");
        GameLocation connectionlessFaolyn = new GameLocation(4, GameLocationService.FAOLYN,"testDescription");
        GameLocation connectionlessHills = new GameLocation(5, GameLocationService.HILLS,"testDescription");
        GameLocation connectionlessRiverlands = new GameLocation(6, GameLocationService.RIVERLANDS,"testDescription");
        Map<GameLocation, Double> arduwynConnections = new HashMap<>();
        arduwynConnections.put(connectionlessNorthRoad,5.0);
        arduwynConnections.put(connectionlessMonastary,3.0);
        GameLocation arduwynLocation = new GameLocation(1, GameLocationService.ARDUWYN,"TestDescription",arduwynConnections);
        getAllLocationsResponse.put(1,arduwynLocation);
        Map<GameLocation, Double> northRoadConnections = new HashMap<>();
        northRoadConnections.put(connectionlessArduwyn,5.0);
        northRoadConnections.put(connectionlessFaolyn,5.0);
        GameLocation northRoadLocation = new GameLocation(2,GameLocationService.NORTH_ROAD,"testDescription",northRoadConnections);
        getAllLocationsResponse.put(2, northRoadLocation);
        Map<GameLocation, Double> monastaryConnections = new HashMap<>();
        monastaryConnections.put(connectionlessArduwyn,3.0);
        monastaryConnections.put(connectionlessFaolyn,1.0);
        monastaryConnections.put(connectionlessHills,5.0);
        GameLocation monastaryLocation = new GameLocation(3,GameLocationService.MONASTARY,"testDescription", monastaryConnections);
        getAllLocationsResponse.put(3, monastaryLocation);
        Map<GameLocation, Double> faolynConnections = new HashMap<>();
        faolynConnections.put(connectionlessNorthRoad,5.0);
        faolynConnections.put(connectionlessMonastary,1.0);
        faolynConnections.put(connectionlessRiverlands, 3.0);
        GameLocation faolynLocation = new GameLocation(4, GameLocationService.FAOLYN,"testDescription", faolynConnections);
        getAllLocationsResponse.put(4, faolynLocation);
        Map<GameLocation, Double> hillsConnections = new HashMap<>();
        hillsConnections.put(connectionlessMonastary,5.0);
        hillsConnections.put(connectionlessRiverlands,5.0);
        GameLocation hillsLocation = new GameLocation(5, GameLocationService.HILLS,"testDescription", hillsConnections);
        getAllLocationsResponse.put(5, hillsLocation);
        Map<GameLocation, Double> riverlandsConnections = new HashMap<>();
        riverlandsConnections.put(connectionlessHills, 5.0);
        riverlandsConnections.put(connectionlessFaolyn,3.0);
        GameLocation riverlandsLocation = new GameLocation(6, GameLocationService.RIVERLANDS,"testDescription", riverlandsConnections);
        getAllLocationsResponse.put(6, riverlandsLocation);
        return getAllLocationsResponse;
    }

    @Test
    public void getCombatDifficultyWoodlandsTest(){
        EncounterDifficultyLevel encounterDifficultyLevel = GameLocationService.getLocationDifficulty(GameLocationService.WOODLANDS);
        assertEquals(EncounterDifficultyLevel.EASY, encounterDifficultyLevel);
    }

    @Test
    public void getCombatDifficultyValleyOfMonstersTest(){
        EncounterDifficultyLevel encounterDifficultyLevel = GameLocationService.getLocationDifficulty(GameLocationService.VALLEY_OF_MONSTERS);
        assertEquals(EncounterDifficultyLevel.MEDIUM, encounterDifficultyLevel);
    }

    @Test
    public void getCombatDifficultyNorthRoadTest(){
        EncounterDifficultyLevel encounterDifficultyLevel = GameLocationService.getLocationDifficulty(GameLocationService.NORTH_ROAD);
        assertEquals(EncounterDifficultyLevel.MEDIUM, encounterDifficultyLevel);
    }

    @Test
    public void getCombatDifficultyRiverlandsTest(){
        EncounterDifficultyLevel encounterDifficultyLevel = GameLocationService.getLocationDifficulty(GameLocationService.RIVERLANDS);
        assertEquals(EncounterDifficultyLevel.MEDIUM, encounterDifficultyLevel);
    }

    @Test
    public void getCombatDifficultyHillsTest(){
        EncounterDifficultyLevel encounterDifficultyLevel = GameLocationService.getLocationDifficulty(GameLocationService.HILLS);
        assertEquals(EncounterDifficultyLevel.MEDIUM, encounterDifficultyLevel);
    }
}
