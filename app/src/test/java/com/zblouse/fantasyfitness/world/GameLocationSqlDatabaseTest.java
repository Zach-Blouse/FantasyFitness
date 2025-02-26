package com.zblouse.fantasyfitness.world;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class GameLocationSqlDatabaseTest {

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
}
