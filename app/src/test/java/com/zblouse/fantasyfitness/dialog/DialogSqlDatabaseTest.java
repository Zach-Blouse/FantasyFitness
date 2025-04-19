package com.zblouse.fantasyfitness.dialog;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;

public class DialogSqlDatabaseTest {

    @Mock
    SQLiteOpenHelper sqLiteOpenHelper;

    @Test
    public void onCreateTest(){
        Context mockContext = Mockito.mock(Context.class);
        SQLiteDatabase mockDatabase = Mockito.mock(SQLiteDatabase.class);
        DialogSqlDatabase testedDatabase = new DialogSqlDatabase(mockContext);
        testedDatabase.onCreate(mockDatabase);
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(mockDatabase).execSQL((String)stringArgumentCaptor.capture());
        assertEquals("CREATE TABLE IF NOT EXISTS dialog(id INTEGER PRIMARY KEY,reference_id TEXT,option_text TEXT,flavor_text TEXT,dialog1 TEXT,dialog2 TEXT,dialog3 TEXT,dialog4 TEXT,dialogAffectType TEXT,questUUIDKey TEXT,questObjectiveUUIDKey TEXT,shopTagKey TEXT)", stringArgumentCaptor.getValue());
    }

    @Test
    public void getDialogByReferenceIdTest(){
        int testDialogId = 1;
        String testReferenceId = "reference1";
        String testOptionText = "optionText1";
        String testFlavorText = "flavorText1";
        String testDialog1 = "dialog1";
        String testDialog2 = "dialog2";
        String testDialog3 = "dialog3";
        String testDialog4 = "dialog4";
        String testDialogAffect = DialogAffectType.NONE.toString();

        Context mockContext = Mockito.mock(Context.class);
        SQLiteDatabase mockDatabase = Mockito.mock(SQLiteDatabase.class);
        DialogSqlDatabase testedDatabase = Mockito.spy(new DialogSqlDatabase(mockContext));

        Cursor mockCursor = Mockito.mock(Cursor.class);
        when(mockCursor.moveToFirst()).thenReturn(true);
        when(mockCursor.getInt(eq(0))).thenReturn(testDialogId);
        when(mockCursor.getString(eq(1))).thenReturn(testReferenceId);
        when(mockCursor.getString(eq(2))).thenReturn(testOptionText);
        when(mockCursor.getString(eq(3))).thenReturn(testFlavorText);
        when(mockCursor.getString(eq(4))).thenReturn(testDialog1);
        when(mockCursor.getString(eq(5))).thenReturn(testDialog2);
        when(mockCursor.getString(eq(6))).thenReturn(testDialog3);
        when(mockCursor.getString(eq(7))).thenReturn(testDialog4);
        when(mockCursor.getString(eq(8))).thenReturn(testDialogAffect);

        when(testedDatabase.getReadableDatabase()).thenReturn(mockDatabase);
        when(mockDatabase.rawQuery(eq("SELECT * FROM dialog WHERE reference_id='" + testReferenceId+"'"), eq(null))).thenReturn(mockCursor);

        Dialog responseDialog = testedDatabase.getDialogByReferenceId(testReferenceId);

        assertEquals(testReferenceId, responseDialog.getReferenceId());
        assertEquals(testOptionText, responseDialog.getOptionText());
        assertEquals(testFlavorText, responseDialog.getFlavorText());
        assertEquals(testDialog1, responseDialog.getDialogOption1());
        assertEquals(testDialog2, responseDialog.getDialogOption2());
        assertEquals(testDialog3, responseDialog.getDialogOption3());
        assertEquals(testDialog4, responseDialog.getDialogOption4());
        assertEquals(testDialogAffect, responseDialog.getDialogAffect().getDialogAffectType().toString());
    }
}
