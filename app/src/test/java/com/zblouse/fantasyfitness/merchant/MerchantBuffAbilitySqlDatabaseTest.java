package com.zblouse.fantasyfitness.merchant;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zblouse.fantasyfitness.combat.cards.AbilityTarget;
import com.zblouse.fantasyfitness.combat.cards.BuffAbility;
import com.zblouse.fantasyfitness.combat.cards.BuffType;
import com.zblouse.fantasyfitness.world.GameLocation;
import com.zblouse.fantasyfitness.world.GameLocationSqlDatabase;

import org.junit.Test;
import org.mockito.Mockito;

public class MerchantBuffAbilitySqlDatabaseTest {

    @Test
    public void readAbilityByNameTest(){
        Integer testAbilityId = 1;
        String testAbilityName = "testAbility";
        String testAbilityDescription = "testAbilityDescription";
        String testAbilityTarget = AbilityTarget.ALL_ENEMY.toString();
        String buffType = BuffType.HEALTH.toString();
        Integer buffAmount = 7;
        Context mockContext = Mockito.mock(Context.class);
        SQLiteDatabase mockDatabase = Mockito.mock(SQLiteDatabase.class);
        Cursor mockCursor = Mockito.mock(Cursor.class);
        when(mockCursor.moveToFirst()).thenReturn(true);
        when(mockCursor.getInt(eq(0))).thenReturn(testAbilityId);
        when(mockCursor.getString(eq(1))).thenReturn(testAbilityName);
        when(mockCursor.getString(eq(2))).thenReturn(testAbilityDescription);
        when(mockCursor.getString(eq(3))).thenReturn(testAbilityTarget);
        when(mockCursor.getString(eq(4))).thenReturn(buffType);
        when(mockCursor.getInt(eq(5))).thenReturn(buffAmount);

        MerchantBuffAbilitySqlDatabase testedDatabase = Mockito.spy(new MerchantBuffAbilitySqlDatabase(mockContext));
        when(testedDatabase.getReadableDatabase()).thenReturn(mockDatabase);
        when(mockDatabase.rawQuery(eq("SELECT * FROM merchantBuffAbility WHERE abilityName='" + testAbilityName +"'"), eq(null))).thenReturn(mockCursor);
        BuffAbility returnedAbility = testedDatabase.readByAbilityName(testAbilityName);

        assertEquals(testAbilityName,returnedAbility.getAbilityName());
        assertEquals(testAbilityDescription, returnedAbility.getAbilityDescription());
        assertEquals(testAbilityTarget, returnedAbility.getAbilityTarget().toString());
        assertEquals(buffType, returnedAbility.getBuffType().toString());
        assertEquals(buffAmount, (Integer)returnedAbility.getBuffAmount());
    }
}
