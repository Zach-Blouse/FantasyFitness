package com.zblouse.fantasyfitness.user;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.zblouse.fantasyfitness.world.GameLocationService;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

public class UserGameStateRepositoryTest {

    @Test
    public void addUserDistanceTest(){
        String testUserId = "testUser1";
        UserGameStateFirestoreDatabase mockUserGameStateFirestoreDatabase = Mockito.mock(UserGameStateFirestoreDatabase.class);
        UserGameStateService mockUserGameStateService = Mockito.mock(UserGameStateService.class);

        UserGameStateRepository testedRepository = new UserGameStateRepository(mockUserGameStateFirestoreDatabase,mockUserGameStateService);

        testedRepository.addUserDistance(testUserId,8, new HashMap<>());
        ArgumentCaptor<Map> mapArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        verify(mockUserGameStateFirestoreDatabase).read(eq(testUserId), eq(testedRepository), mapArgumentCaptor.capture());
        assert(mapArgumentCaptor.getValue().containsKey("multiStageUpdate"));
        assertEquals("userDistance", mapArgumentCaptor.getValue().get("multiStageUpdate"));
        assert(mapArgumentCaptor.getValue().containsKey("modifyValue"));
        assertEquals(8.0, mapArgumentCaptor.getValue().get("modifyValue"));
    }

    @Test
    public void getUserGameStateTest(){
        String testUserId = "testUser1";
        UserGameStateFirestoreDatabase mockUserGameStateFirestoreDatabase = Mockito.mock(UserGameStateFirestoreDatabase.class);
        UserGameStateService mockUserGameStateService = Mockito.mock(UserGameStateService.class);

        UserGameStateRepository testedRepository = new UserGameStateRepository(mockUserGameStateFirestoreDatabase,mockUserGameStateService);
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("userGameStateTest", "test");
        testedRepository.getUserGameState(testUserId, metadata);
        ArgumentCaptor<Map> mapArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        verify(mockUserGameStateFirestoreDatabase).read(eq(testUserId), eq(testedRepository), mapArgumentCaptor.capture());
        assert(mapArgumentCaptor.getValue().containsKey("userGameStateTest"));
        assertEquals("test", mapArgumentCaptor.getValue().get("userGameStateTest"));
    }

    @Test
    public void writeUserGameStateTest(){
        String testUserId = "testUser1";
        String testUserLocation = GameLocationService.ARDUWYN;
        Double testUserDistance = 5.5;
        int testUserCurrency = 6;
        UserGameStateFirestoreDatabase mockUserGameStateFirestoreDatabase = Mockito.mock(UserGameStateFirestoreDatabase.class);
        UserGameStateService mockUserGameStateService = Mockito.mock(UserGameStateService.class);

        UserGameStateRepository testedRepository = new UserGameStateRepository(mockUserGameStateFirestoreDatabase,mockUserGameStateService);

        testedRepository.writeUserGameState(testUserId,testUserLocation,testUserDistance,testUserCurrency, new HashMap<>());
        ArgumentCaptor<Map> mapArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        ArgumentCaptor<UserGameState> userGameStateArgumentCaptor = ArgumentCaptor.forClass(UserGameState.class);
        verify(mockUserGameStateFirestoreDatabase).write(userGameStateArgumentCaptor.capture(),eq(testedRepository),mapArgumentCaptor.capture());

        assertEquals(testUserId, userGameStateArgumentCaptor.getValue().getUserId());
        assertEquals(testUserLocation, userGameStateArgumentCaptor.getValue().getCurrentGameLocationName());
        assertEquals(testUserDistance,userGameStateArgumentCaptor.getValue().getSavedWorkoutDistanceMeters(),0.0);
        assertEquals(testUserCurrency, userGameStateArgumentCaptor.getValue().getUserGameCurrency());
    }

    @Test
    public void updateUserGameLocationTest(){
        String testUserId = "testUser1";
        String testUserLocation = GameLocationService.ARDUWYN;
        UserGameStateFirestoreDatabase mockUserGameStateFirestoreDatabase = Mockito.mock(UserGameStateFirestoreDatabase.class);
        UserGameStateService mockUserGameStateService = Mockito.mock(UserGameStateService.class);

        UserGameStateRepository testedRepository = new UserGameStateRepository(mockUserGameStateFirestoreDatabase,mockUserGameStateService);

        testedRepository.updateUserGameLocation(testUserId, testUserLocation, new HashMap<>());

        verify(mockUserGameStateFirestoreDatabase).updateField(eq(testUserId),eq(UserGameStateFirestoreDatabase.USER_LOCATION_FIELD),eq(testUserLocation),eq(testedRepository), anyMap());
    }

    @Test
    public void readCallbackNotMultiTest(){
        String testUserId = "testUser1";
        String testUserLocation = GameLocationService.ARDUWYN;
        Double testUserDistance = 5.5;
        int testUserCurrency = 6;
        UserGameState testUserGameState = new UserGameState(testUserId,testUserLocation,testUserDistance, testUserCurrency);
        UserGameStateFirestoreDatabase mockUserGameStateFirestoreDatabase = Mockito.mock(UserGameStateFirestoreDatabase.class);
        UserGameStateService mockUserGameStateService = Mockito.mock(UserGameStateService.class);
        Map<String, Object> metadata = new HashMap<>();

        UserGameStateRepository testedRepository = new UserGameStateRepository(mockUserGameStateFirestoreDatabase,mockUserGameStateService);

        testedRepository.readCallback(testUserGameState, metadata);
        ArgumentCaptor<UserGameState> userGameStateArgumentCaptor = ArgumentCaptor.forClass(UserGameState.class);
        verify(mockUserGameStateService).repositoryResponse(userGameStateArgumentCaptor.capture(), anyMap());
        assertEquals(testUserId, userGameStateArgumentCaptor.getValue().getUserId());
        assertEquals(testUserLocation, userGameStateArgumentCaptor.getValue().getCurrentGameLocationName());
        assertEquals(testUserDistance,userGameStateArgumentCaptor.getValue().getSavedWorkoutDistanceMeters(),0.0);
        assertEquals(testUserCurrency, userGameStateArgumentCaptor.getValue().getUserGameCurrency());
    }

    @Test
    public void readCallbackMultiTest(){
        String testUserId = "testUser1";
        String testUserLocation = GameLocationService.ARDUWYN;
        Double testUserDistance = 5.5;
        int testUserCurrency = 6;
        UserGameState testUserGameState = new UserGameState(testUserId,testUserLocation,testUserDistance, testUserCurrency);
        UserGameStateFirestoreDatabase mockUserGameStateFirestoreDatabase = Mockito.mock(UserGameStateFirestoreDatabase.class);
        UserGameStateService mockUserGameStateService = Mockito.mock(UserGameStateService.class);
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("multiStageUpdate", "userDistance");
        metadata.put("modifyValue",1.1);

        UserGameStateRepository testedRepository = new UserGameStateRepository(mockUserGameStateFirestoreDatabase,mockUserGameStateService);

        testedRepository.readCallback(testUserGameState, metadata);

        verify(mockUserGameStateFirestoreDatabase).updateField(eq(testUserId),eq(UserGameStateFirestoreDatabase.USER_SAVED_DISTANCE),eq(6.6),eq(testedRepository),anyMap());
    }

    @Test
    public void writeCallbackTest(){
        String testUserId = "testUser1";
        String testUserLocation = GameLocationService.ARDUWYN;
        Double testUserDistance = 5.5;
        int testUserCurrency = 6;
        UserGameState testUserGameState = new UserGameState(testUserId,testUserLocation,testUserDistance, testUserCurrency);
        UserGameStateFirestoreDatabase mockUserGameStateFirestoreDatabase = Mockito.mock(UserGameStateFirestoreDatabase.class);
        UserGameStateService mockUserGameStateService = Mockito.mock(UserGameStateService.class);
        Map<String, Object> metadata = new HashMap<>();

        UserGameStateRepository testedRepository = new UserGameStateRepository(mockUserGameStateFirestoreDatabase,mockUserGameStateService);

        testedRepository.writeCallback(testUserGameState, metadata);
        ArgumentCaptor<UserGameState> userGameStateArgumentCaptor = ArgumentCaptor.forClass(UserGameState.class);
        verify(mockUserGameStateService).repositoryResponse(userGameStateArgumentCaptor.capture(), anyMap());
        assertEquals(testUserId, userGameStateArgumentCaptor.getValue().getUserId());
        assertEquals(testUserLocation, userGameStateArgumentCaptor.getValue().getCurrentGameLocationName());
        assertEquals(testUserDistance,userGameStateArgumentCaptor.getValue().getSavedWorkoutDistanceMeters(),0.0);
        assertEquals(testUserCurrency, userGameStateArgumentCaptor.getValue().getUserGameCurrency());
    }

    @Test
    public void updateCallbackSuccessTest(){
        UserGameStateFirestoreDatabase mockUserGameStateFirestoreDatabase = Mockito.mock(UserGameStateFirestoreDatabase.class);
        UserGameStateService mockUserGameStateService = Mockito.mock(UserGameStateService.class);
        Map<String, Object> metadata = new HashMap<>();
        UserGameStateRepository testedRepository = new UserGameStateRepository(mockUserGameStateFirestoreDatabase,mockUserGameStateService);

        testedRepository.updateCallback(true, metadata);
        verify(mockUserGameStateService).repositoryResponse(eq(null), anyMap());
    }

    @Test
    public void updateCallbackFailTest(){
        UserGameStateFirestoreDatabase mockUserGameStateFirestoreDatabase = Mockito.mock(UserGameStateFirestoreDatabase.class);
        UserGameStateService mockUserGameStateService = Mockito.mock(UserGameStateService.class);
        Map<String, Object> metadata = new HashMap<>();
        UserGameStateRepository testedRepository = new UserGameStateRepository(mockUserGameStateFirestoreDatabase,mockUserGameStateService);

        testedRepository.updateCallback(false, metadata);
        verify(mockUserGameStateService, times(0)).repositoryResponse(any(), anyMap());
    }

}
