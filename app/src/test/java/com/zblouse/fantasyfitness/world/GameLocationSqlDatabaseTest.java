package com.zblouse.fantasyfitness.world;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

public class GameLocationSqlDatabaseTest {

    @Mock
    SQLiteOpenHelper sqLiteOpenHelper;

    @Test
    public void onCreateTest(){
        Context mockContext = Mockito.mock(Context.class);
        SQLiteDatabase mockDatabase = Mockito.mock(SQLiteDatabase.class);
        GameLocationSqlDatabase testedDatabase = new GameLocationSqlDatabase(mockContext);
        testedDatabase.onCreate(mockDatabase);
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(mockDatabase).execSQL((String)stringArgumentCaptor.capture());
        assertEquals("CREATE TABLE IF NOT EXISTS location(id INTEGER PRIMARY KEY,name TEXT,description TEXT)", stringArgumentCaptor.getValue());
    }

    @Test
    public void getLocationByIdTest(){
        Integer testLocationId = 1;
        String testLocationName = "testLocation";
        String testLocationDescription = "testLocationDescription";
        Context mockContext = Mockito.mock(Context.class);
        SQLiteDatabase mockDatabase = Mockito.mock(SQLiteDatabase.class);
        Cursor mockCursor = Mockito.mock(Cursor.class);
        when(mockCursor.moveToFirst()).thenReturn(true);
        when(mockCursor.getInt(eq(0))).thenReturn(testLocationId);
        when(mockCursor.getString(eq(1))).thenReturn(testLocationName);
        when(mockCursor.getString(eq(2))).thenReturn(testLocationDescription);

        GameLocationSqlDatabase testedDatabase = Mockito.spy(new GameLocationSqlDatabase(mockContext));
        when(testedDatabase.getReadableDatabase()).thenReturn(mockDatabase);
        when(mockDatabase.rawQuery(eq("SELECT * FROM location WHERE id=" + testLocationId), eq(null))).thenReturn(mockCursor);


        GameLocation returnedLocation = testedDatabase.getLocationById(testLocationId);

        assertEquals(testLocationId,returnedLocation.getId());
        assertEquals(testLocationName, returnedLocation.getLocationName());
        assertEquals(testLocationDescription, returnedLocation.getLocationDescription());

    }

    @Test
    public void getLocationByIdNotFoundTest(){
        Integer testLocationId = 1;
        String testLocationName = "testLocation";
        String testLocationDescription = "testLocationDescription";
        Context mockContext = Mockito.mock(Context.class);
        SQLiteDatabase mockDatabase = Mockito.mock(SQLiteDatabase.class);
        Cursor mockCursor = Mockito.mock(Cursor.class);
        when(mockCursor.moveToFirst()).thenReturn(false);
        when(mockCursor.getInt(eq(0))).thenReturn(testLocationId);
        when(mockCursor.getString(eq(1))).thenReturn(testLocationName);
        when(mockCursor.getString(eq(2))).thenReturn(testLocationDescription);

        GameLocationSqlDatabase testedDatabase = Mockito.spy(new GameLocationSqlDatabase(mockContext));
        when(testedDatabase.getReadableDatabase()).thenReturn(mockDatabase);
        when(mockDatabase.rawQuery(eq("SELECT * FROM location WHERE id=" + testLocationId), eq(null))).thenReturn(mockCursor);


        GameLocation returnedLocation = testedDatabase.getLocationById(testLocationId);

        assertNull(returnedLocation);
    }

    @Test
    public void getAllLocationsTest(){
        Integer testLocationId = 1;
        String testLocationName = "testLocation";
        String testLocationDescription = "testLocationDescription";
        Integer testLocationId2 = 2;
        String testLocationName2 = "testLocation2";
        String testLocationDescription2 = "testLocationDescription2";
        Context mockContext = Mockito.mock(Context.class);
        SQLiteDatabase mockDatabase = Mockito.mock(SQLiteDatabase.class);
        Cursor mockCursor = Mockito.mock(Cursor.class);
        when(mockCursor.moveToFirst()).thenReturn(true);
        when(mockCursor.moveToNext()).thenReturn(true, false);
        when(mockCursor.getInt(eq(0))).thenReturn(testLocationId, testLocationId2);
        when(mockCursor.getString(eq(1))).thenReturn(testLocationName, testLocationName2);
        when(mockCursor.getString(eq(2))).thenReturn(testLocationDescription, testLocationDescription2);

        GameLocationSqlDatabase testedDatabase = Mockito.spy(new GameLocationSqlDatabase(mockContext));
        when(testedDatabase.getReadableDatabase()).thenReturn(mockDatabase);
        when(mockDatabase.rawQuery(eq("SELECT * FROM location"), eq(null))).thenReturn(mockCursor);


        List<GameLocation> returnedLocations = testedDatabase.getAllLocations();

        assertEquals(2, returnedLocations.size());

        assertEquals(testLocationId,returnedLocations.get(0).getId());
        assertEquals(testLocationName, returnedLocations.get(0).getLocationName());
        assertEquals(testLocationDescription, returnedLocations.get(0).getLocationDescription());

        assertEquals(testLocationId2,returnedLocations.get(1).getId());
        assertEquals(testLocationName2, returnedLocations.get(1).getLocationName());
        assertEquals(testLocationDescription2, returnedLocations.get(1).getLocationDescription());

    }
}
