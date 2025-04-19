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
import com.zblouse.fantasyfitness.combat.cards.HealAbility;

import org.junit.Test;
import org.mockito.Mockito;

public class MerchantHealAbilitySqlDatabaseTest {

    @Test
    public void readAbilityByNameTest(){
        Integer testAbilityId = 1;
        String testAbilityName = "testAbility";
        String testAbilityDescription = "testAbilityDescription";
        String testAbilityTarget = AbilityTarget.ALL_ENEMY.toString();
        Integer healAmount = 7;
        Context mockContext = Mockito.mock(Context.class);
        SQLiteDatabase mockDatabase = Mockito.mock(SQLiteDatabase.class);
        Cursor mockCursor = Mockito.mock(Cursor.class);
        when(mockCursor.moveToFirst()).thenReturn(true);
        when(mockCursor.getInt(eq(0))).thenReturn(testAbilityId);
        when(mockCursor.getString(eq(1))).thenReturn(testAbilityName);
        when(mockCursor.getString(eq(2))).thenReturn(testAbilityDescription);
        when(mockCursor.getString(eq(3))).thenReturn(testAbilityTarget);
        when(mockCursor.getInt(eq(4))).thenReturn(healAmount);

        MerchantHealAbilitySqlDatabase testedDatabase = Mockito.spy(new MerchantHealAbilitySqlDatabase(mockContext));
        when(testedDatabase.getReadableDatabase()).thenReturn(mockDatabase);
        when(mockDatabase.rawQuery(eq("SELECT * FROM merchantHealAbility WHERE abilityName='" + testAbilityName +"'"), eq(null))).thenReturn(mockCursor);
        HealAbility returnedAbility = testedDatabase.readByAbilityName(testAbilityName);

        assertEquals(testAbilityName,returnedAbility.getAbilityName());
        assertEquals(testAbilityDescription, returnedAbility.getAbilityDescription());
        assertEquals(testAbilityTarget, returnedAbility.getAbilityTarget().toString());
        assertEquals(healAmount, (Integer)returnedAbility.getHealAmount());
    }
}
