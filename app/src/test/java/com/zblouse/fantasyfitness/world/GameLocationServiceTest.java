package com.zblouse.fantasyfitness.world;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.zblouse.fantasyfitness.activity.MainActivity;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.HashMap;

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
}
