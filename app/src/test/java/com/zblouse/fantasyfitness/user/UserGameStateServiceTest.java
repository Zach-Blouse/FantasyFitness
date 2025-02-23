package com.zblouse.fantasyfitness.user;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.core.DomainService;
import com.zblouse.fantasyfitness.world.GameLocationService;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

public class UserGameStateServiceTest {

    @Test
    public void fetchUserGameStateTest(){
        String userId = "testUserId1";
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        UserGameStateRepository mockUserGameStateRepository = Mockito.mock(UserGameStateRepository.class);
        UserGameStateService testedService = new UserGameStateService(mockActivity, mockUserGameStateRepository);
        testedService.fetchUserGameState(userId, new HashMap<>());
        ArgumentCaptor<Map> mapArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        verify(mockUserGameStateRepository).getUserGameState(eq(userId),mapArgumentCaptor.capture());
        assert(mapArgumentCaptor.getValue().containsKey(UserGameStateService.CALLING_FUNCTION_KEY));
        assertEquals("fetchGameState", mapArgumentCaptor.getValue().get(UserGameStateService.CALLING_FUNCTION_KEY));
    }

    @Test
    public void addUserGameDistanceTest(){
        String userId = "testUserId1";
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        UserGameStateRepository mockUserGameStateRepository = Mockito.mock(UserGameStateRepository.class);
        UserGameStateService testedService = new UserGameStateService(mockActivity, mockUserGameStateRepository);
        testedService.addUserGameDistance(userId, 2.0,new HashMap<>());
        ArgumentCaptor<Map> mapArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        verify(mockUserGameStateRepository).addUserDistance(eq(userId),eq(2.0),mapArgumentCaptor.capture());
        assert(mapArgumentCaptor.getValue().containsKey(UserGameStateService.CALLING_FUNCTION_KEY));
        assertEquals("addUserGameDistance", mapArgumentCaptor.getValue().get(UserGameStateService.CALLING_FUNCTION_KEY));
    }

    @Test
    public void initializeUserGameStateTest(){
        String userId = "testUserId1";
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        UserGameStateRepository mockUserGameStateRepository = Mockito.mock(UserGameStateRepository.class);
        UserGameStateService testedService = new UserGameStateService(mockActivity, mockUserGameStateRepository);
        testedService.initializeUserGameState(userId, GameLocationService.THANADEL_VILLAGE, new HashMap<>());
        ArgumentCaptor<Map> mapArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        verify(mockUserGameStateRepository).writeUserGameState(eq(userId),eq(GameLocationService.THANADEL_VILLAGE),eq(0.0),mapArgumentCaptor.capture());
        assert(mapArgumentCaptor.getValue().containsKey(UserGameStateService.CALLING_FUNCTION_KEY));
        assertEquals("initializeGameState", mapArgumentCaptor.getValue().get(UserGameStateService.CALLING_FUNCTION_KEY));
    }

    @Test
    public void updateUserGameLocationTest(){
        String userId = "testUserId1";
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        UserGameStateRepository mockUserGameStateRepository = Mockito.mock(UserGameStateRepository.class);
        UserGameStateService testedService = new UserGameStateService(mockActivity, mockUserGameStateRepository);
        testedService.updateUserGameLocation(userId, GameLocationService.ARDUWYN,new HashMap<>());
        ArgumentCaptor<Map> mapArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        verify(mockUserGameStateRepository).updateUserGameLocation(eq(userId),eq(GameLocationService.ARDUWYN),mapArgumentCaptor.capture());
        assert(mapArgumentCaptor.getValue().containsKey(UserGameStateService.CALLING_FUNCTION_KEY));
        assertEquals("updateUserGameLocation", mapArgumentCaptor.getValue().get(UserGameStateService.CALLING_FUNCTION_KEY));
        assert(mapArgumentCaptor.getValue().containsKey(UserGameStateService.NEW_LOCATION));
        assertEquals(GameLocationService.ARDUWYN, mapArgumentCaptor.getValue().get(UserGameStateService.NEW_LOCATION));
    }

    @Test
    public void repositoryResponseFetchGameStateTest(){
        UserGameState responseGameState = new UserGameState("testId1", GameLocationService.ARDUWYN,1500.0);
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        UserGameStateRepository mockUserGameStateRepository = Mockito.mock(UserGameStateRepository.class);
        Map<String, Object> metadata = new HashMap<>();
        metadata.put(UserGameStateService.CALLING_FUNCTION_KEY,"fetchGameState");

        UserGameStateService testedService = new UserGameStateService(mockActivity, mockUserGameStateRepository);
        testedService.repositoryResponse(responseGameState,metadata);

        ArgumentCaptor<UserGameStateFetchResponseEvent> userGameStateFetchResponseEventArgumentCaptor = ArgumentCaptor.forClass(UserGameStateFetchResponseEvent.class);
        verify(mockActivity).publishEvent((UserGameStateFetchResponseEvent)userGameStateFetchResponseEventArgumentCaptor.capture());
        assertEquals(responseGameState,userGameStateFetchResponseEventArgumentCaptor.getValue().getUserGameState());
    }

    @Test
    public void repositoryResponseFetchGameStateInterDomainTest(){
        UserGameState responseGameState = new UserGameState("testId1", GameLocationService.ARDUWYN,1500.0);
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        UserGameStateRepository mockUserGameStateRepository = Mockito.mock(UserGameStateRepository.class);
        Map<String, Object> metadata = new HashMap<>();
        DomainService mockDomainService = Mockito.mock(DomainService.class);
        metadata.put(UserGameStateService.CALLING_FUNCTION_KEY,"fetchGameState");
        metadata.put(DomainService.INTER_DOMAIN_SERVICE_ORIGIN_KEY, mockDomainService);

        UserGameStateService testedService = new UserGameStateService(mockActivity, mockUserGameStateRepository);
        testedService.repositoryResponse(responseGameState,metadata);
        ArgumentCaptor<Map> mapArgumentCaptor = ArgumentCaptor.forClass(Map.class);

        verify(mockDomainService).interDomainServiceResponse(eq(responseGameState),mapArgumentCaptor.capture());
        assert(mapArgumentCaptor.getValue().containsKey(DomainService.INTER_DOMAIN_SERVICE_RESPONSE_CLASS_KEY));
        assertEquals(UserGameState.class, mapArgumentCaptor.getValue().get(DomainService.INTER_DOMAIN_SERVICE_RESPONSE_CLASS_KEY));
    }

    @Test
    public void repositoryResponseUpdateGameStateLocationTest(){
        UserGameState responseGameState = new UserGameState("testId1", GameLocationService.ARDUWYN,1500.0);
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        UserGameStateRepository mockUserGameStateRepository = Mockito.mock(UserGameStateRepository.class);
        Map<String, Object> metadata = new HashMap<>();
        metadata.put(UserGameStateService.CALLING_FUNCTION_KEY,"updateUserGameLocation");

        UserGameStateService testedService = new UserGameStateService(mockActivity, mockUserGameStateRepository);
        testedService.repositoryResponse(null,metadata);

        ArgumentCaptor<UserGameStateUpdateEvent> userGameStateUpdateEventArgumentCaptor = ArgumentCaptor.forClass(UserGameStateUpdateEvent.class);
        verify(mockActivity).publishEvent((UserGameStateUpdateEvent)userGameStateUpdateEventArgumentCaptor.capture());
        assert(userGameStateUpdateEventArgumentCaptor.getValue().getMetadata().containsKey(UserGameStateService.STATE_FIELD_UPDATED));
        assertEquals(UserGameStateService.LOCATION_FIELD,userGameStateUpdateEventArgumentCaptor.getValue().getMetadata().get(UserGameStateService.STATE_FIELD_UPDATED));
    }

    @Test
    public void repositoryResponseUpdateGameStateDistanceTest(){
        UserGameState responseGameState = new UserGameState("testId1", GameLocationService.ARDUWYN,1500.0);
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        UserGameStateRepository mockUserGameStateRepository = Mockito.mock(UserGameStateRepository.class);
        Map<String, Object> metadata = new HashMap<>();
        metadata.put(UserGameStateService.CALLING_FUNCTION_KEY,"addUserGameDistance");

        UserGameStateService testedService = new UserGameStateService(mockActivity, mockUserGameStateRepository);
        testedService.repositoryResponse(null,metadata);

        ArgumentCaptor<UserGameStateUpdateEvent> userGameStateUpdateEventArgumentCaptor = ArgumentCaptor.forClass(UserGameStateUpdateEvent.class);
        verify(mockActivity).publishEvent((UserGameStateUpdateEvent)userGameStateUpdateEventArgumentCaptor.capture());
        assert(userGameStateUpdateEventArgumentCaptor.getValue().getMetadata().containsKey(UserGameStateService.STATE_FIELD_UPDATED));
        assertEquals(UserGameStateService.USER_DISTANCE_FIELD,userGameStateUpdateEventArgumentCaptor.getValue().getMetadata().get(UserGameStateService.STATE_FIELD_UPDATED));
    }
}
