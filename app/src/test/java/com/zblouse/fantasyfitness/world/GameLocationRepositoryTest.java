package com.zblouse.fantasyfitness.world;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameLocationRepositoryTest {

    @Test
    public void databaseInitializedTest(){
        GameLocationSqlDatabase mockGameLocationSqlDatabase = Mockito.mock(GameLocationSqlDatabase.class);
        GameLocationDistanceSqlDatabase mockGameLocationDistanceSqlDatabase = Mockito.mock(GameLocationDistanceSqlDatabase.class);
        List<GameLocation> gameLocationList = new ArrayList<>();
        GameLocation location1 = new GameLocation("TestLocation1","Description 1");
        gameLocationList.add(location1);
        when(mockGameLocationSqlDatabase.getAllLocations()).thenReturn(gameLocationList);
        GameLocationRepository testedRepository = new GameLocationRepository(mockGameLocationSqlDatabase, mockGameLocationDistanceSqlDatabase);
        boolean isInitialized = testedRepository.databaseInitialized();
        assert(isInitialized);
    }

    @Test
    public void databaseNotInitializedTest(){
        GameLocationSqlDatabase mockGameLocationSqlDatabase = Mockito.mock(GameLocationSqlDatabase.class);
        GameLocationDistanceSqlDatabase mockGameLocationDistanceSqlDatabase = Mockito.mock(GameLocationDistanceSqlDatabase.class);
        List<GameLocation> gameLocationList = new ArrayList<>();
        when(mockGameLocationSqlDatabase.getAllLocations()).thenReturn(gameLocationList);
        GameLocationRepository testedRepository = new GameLocationRepository(mockGameLocationSqlDatabase, mockGameLocationDistanceSqlDatabase);
        boolean isInitialized = testedRepository.databaseInitialized();
        assert(!isInitialized);
    }

    @Test
    public void getLocationByNameFoundTest(){
        String testLocation1Name = "TestLocation1";
        String testLocation2Name = "TestLocation2";
        String testLocation3Name = "TestLocation3";
        GameLocationSqlDatabase mockGameLocationSqlDatabase = Mockito.mock(GameLocationSqlDatabase.class);
        GameLocationDistanceSqlDatabase mockGameLocationDistanceSqlDatabase = Mockito.mock(GameLocationDistanceSqlDatabase.class);
        GameLocation location1 = new GameLocation(1,testLocation1Name,"Description 1");
        when(mockGameLocationSqlDatabase.getLocationByName(eq(testLocation1Name))).thenReturn(location1);
        when(mockGameLocationSqlDatabase.getLocationById(1)).thenReturn(location1);
        GameLocation location2 = new GameLocation(2, testLocation2Name,"Description 2");
        when(mockGameLocationSqlDatabase.getLocationByName(eq(testLocation2Name))).thenReturn(location2);
        when(mockGameLocationSqlDatabase.getLocationById(2)).thenReturn(location2);
        GameLocation location3 = new GameLocation(3, testLocation3Name,"Description 3");
        when(mockGameLocationSqlDatabase.getLocationByName(eq(testLocation3Name))).thenReturn(location3);
        when(mockGameLocationSqlDatabase.getLocationById(3)).thenReturn(location3);
        GameLocationDistance testDistance1 = new GameLocationDistance(1,2,5);
        GameLocationDistance testDistance2 = new GameLocationDistance(3,1,4);
        List<GameLocationDistance> distances = new ArrayList<>();
        distances.add(testDistance1);
        distances.add(testDistance2);
        when(mockGameLocationDistanceSqlDatabase.getAllLocationDistancesForLocation(eq(location1))).thenReturn(distances);
        GameLocationRepository testedRepository = new GameLocationRepository(mockGameLocationSqlDatabase, mockGameLocationDistanceSqlDatabase);
        GameLocationService mockGameLocationService = Mockito.mock(GameLocationService.class);

        testedRepository.getLocationByName(testLocation1Name,mockGameLocationService,new HashMap<>());

        ArgumentCaptor<GameLocation> gameLocationArgumentCaptor = ArgumentCaptor.forClass(GameLocation.class);
        verify(mockGameLocationService).repositoryResponse(gameLocationArgumentCaptor.capture(),anyMap());
        assertEquals(testLocation1Name,gameLocationArgumentCaptor.getValue().getLocationName());
        assertEquals(location1.getLocationDescription(),gameLocationArgumentCaptor.getValue().getLocationDescription());
        assertEquals(2,gameLocationArgumentCaptor.getValue().getConnectedLocations().size());
        assert(gameLocationArgumentCaptor.getValue().getConnectedLocations().containsKey(location2));
        assertEquals(testDistance1.getDistanceMiles(),(double)gameLocationArgumentCaptor.getValue().getConnectedLocations().get(location2),0);
        assert(gameLocationArgumentCaptor.getValue().getConnectedLocations().containsKey(location3));
        assertEquals(testDistance2.getDistanceMiles(),(double)gameLocationArgumentCaptor.getValue().getConnectedLocations().get(location3),0);
    }

    @Test
    public void getLocationByNameNotFoundTest(){
        String testLocation1Name = "TestLocation1";
        GameLocationSqlDatabase mockGameLocationSqlDatabase = Mockito.mock(GameLocationSqlDatabase.class);
        GameLocationDistanceSqlDatabase mockGameLocationDistanceSqlDatabase = Mockito.mock(GameLocationDistanceSqlDatabase.class);
        when(mockGameLocationSqlDatabase.getLocationByName(eq(testLocation1Name))).thenReturn(null);
        GameLocationService mockGameLocationService = Mockito.mock(GameLocationService.class);
        GameLocationRepository testedRepository = new GameLocationRepository(mockGameLocationSqlDatabase, mockGameLocationDistanceSqlDatabase);

        testedRepository.getLocationByName(testLocation1Name,mockGameLocationService,new HashMap<>());
        ArgumentCaptor<GameLocation> gameLocationArgumentCaptor = ArgumentCaptor.forClass(GameLocation.class);
        verify(mockGameLocationService).repositoryResponse(gameLocationArgumentCaptor.capture(),anyMap());
        assertNull(gameLocationArgumentCaptor.getValue());
    }

    @Test
    public void addLocationTest(){
        String testLocation1Name = "TestLocation1";
        GameLocationSqlDatabase mockGameLocationSqlDatabase = Mockito.mock(GameLocationSqlDatabase.class);
        GameLocationDistanceSqlDatabase mockGameLocationDistanceSqlDatabase = Mockito.mock(GameLocationDistanceSqlDatabase.class);
        when(mockGameLocationSqlDatabase.getLocationByName(eq(testLocation1Name))).thenReturn(null);
        GameLocationRepository testedRepository = new GameLocationRepository(mockGameLocationSqlDatabase, mockGameLocationDistanceSqlDatabase);
        GameLocation location1 = new GameLocation(1,testLocation1Name,"Description 1");

        testedRepository.createGameLocation(location1);
        verify(mockGameLocationSqlDatabase).addLocationToDatabase(location1);
    }

    @Test
    public void addGameLocationDistanceTest(){
        String testLocation1Name = "TestLocation1";
        String testLocation2Name = "TestLocation2";
        GameLocationSqlDatabase mockGameLocationSqlDatabase = Mockito.mock(GameLocationSqlDatabase.class);
        GameLocationDistanceSqlDatabase mockGameLocationDistanceSqlDatabase = Mockito.mock(GameLocationDistanceSqlDatabase.class);
        GameLocation location1 = new GameLocation(1,testLocation1Name,"Description 1");
        when(mockGameLocationSqlDatabase.getLocationByName(eq(testLocation1Name))).thenReturn(location1);
        when(mockGameLocationSqlDatabase.getLocationById(1)).thenReturn(location1);
        GameLocation location2 = new GameLocation(2, testLocation2Name,"Description 2");
        when(mockGameLocationSqlDatabase.getLocationByName(eq(testLocation2Name))).thenReturn(location2);
        when(mockGameLocationSqlDatabase.getLocationById(2)).thenReturn(location2);

        GameLocation noIdLocation1 = new GameLocation(testLocation1Name, "Description 1");
        GameLocation noIdLocation2 = new GameLocation(testLocation2Name, "Description 2");

        GameLocationRepository testedRepository = new GameLocationRepository(mockGameLocationSqlDatabase, mockGameLocationDistanceSqlDatabase);

        testedRepository.addGameLocationConnection(noIdLocation1, noIdLocation2, 5);
        ArgumentCaptor<GameLocationDistance> gameLocationDistanceArgumentCaptor = ArgumentCaptor.forClass(GameLocationDistance.class);
        verify(mockGameLocationDistanceSqlDatabase).addLocationDistanceToDatabase(gameLocationDistanceArgumentCaptor.capture());

        assertEquals(1, (long)gameLocationDistanceArgumentCaptor.getValue().getLocation1Id());
        assertEquals(2, (long)gameLocationDistanceArgumentCaptor.getValue().getLocation2Id());
        assertEquals(5, gameLocationDistanceArgumentCaptor.getValue().getDistanceMiles(),0);
    }
}
