package com.zblouse.fantasyfitness.world;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

public class GameLocationDistanceSqlDatabaseTest {

    @Test
    public void getAllLocationDistancesForLocationTest(){
        Integer testLocationId = 1;
        String testLocationName = "testLocation";
        String testLocationDescription = "testLocationDescription";
        GameLocation testLocation1 = new GameLocation(testLocationId, testLocationName, testLocationDescription);
        Integer testLocationId2 = 2;
        String testLocationName2 = "testLocation2";
        String testLocationDescription2 = "testLocationDescription2";
        double testDistance = 12.2;
        Context mockContext = Mockito.mock(Context.class);
        SQLiteDatabase mockDatabase = Mockito.mock(SQLiteDatabase.class);
        Cursor mockCursor = Mockito.mock(Cursor.class);
        when(mockCursor.moveToFirst()).thenReturn(true);
        when(mockCursor.moveToNext()).thenReturn(false);
        when(mockCursor.getInt(eq(0))).thenReturn(testLocationId);
        when(mockCursor.getInt(eq(1))).thenReturn(testLocationId);
        when(mockCursor.getInt(eq(2))).thenReturn(testLocationId2);
        when(mockCursor.getDouble(eq(3))).thenReturn(testDistance);

        Cursor mockCursor2 = Mockito.mock(Cursor.class);
        when(mockCursor2.moveToFirst()).thenReturn(true);
        when(mockCursor2.moveToNext()).thenReturn(false);
        when(mockCursor2.getInt(eq(0))).thenReturn(testLocationId2);
        when(mockCursor2.getInt(eq(1))).thenReturn(testLocationId2);
        when(mockCursor2.getInt(eq(2))).thenReturn(testLocationId);
        when(mockCursor2.getDouble(eq(3))).thenReturn(testDistance);



        GameLocationDistanceSqlDatabase testedDatabase = Mockito.spy(new GameLocationDistanceSqlDatabase(mockContext));
        when(testedDatabase.getReadableDatabase()).thenReturn(mockDatabase);
        when(mockDatabase.rawQuery(eq("SELECT * FROM location_distance WHERE location1=" + testLocationId), eq(null))).thenReturn(mockCursor);
        when(mockDatabase.rawQuery(eq("SELECT * FROM location_distance WHERE location2=" + testLocationId), eq(null))).thenReturn(mockCursor2);


        List<GameLocationDistance> returnedLocationDistances = testedDatabase.getAllLocationDistancesForLocation(testLocation1);

        assertEquals(2, returnedLocationDistances.size());

        assertEquals(testLocationId,returnedLocationDistances.get(0).getId());
        assertEquals(testLocationId, returnedLocationDistances.get(0).getLocation1Id());
        assertEquals(testLocationId2, returnedLocationDistances.get(0).getLocation2Id());
        assertEquals(testDistance, returnedLocationDistances.get(0).getDistanceMiles(),0);

        assertEquals(testLocationId2,returnedLocationDistances.get(1).getId());
        assertEquals(testLocationId2, returnedLocationDistances.get(1).getLocation1Id());
        assertEquals(testLocationId, returnedLocationDistances.get(1).getLocation2Id());
        assertEquals(testDistance, returnedLocationDistances.get(1).getDistanceMiles(),0);

    }
}
