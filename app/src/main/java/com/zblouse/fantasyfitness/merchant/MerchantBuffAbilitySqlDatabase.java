package com.zblouse.fantasyfitness.merchant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zblouse.fantasyfitness.combat.cards.AbilityTarget;
import com.zblouse.fantasyfitness.combat.cards.BuffAbility;
import com.zblouse.fantasyfitness.combat.cards.BuffType;
import com.zblouse.fantasyfitness.combat.cards.HealAbility;

public class MerchantBuffAbilitySqlDatabase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "merchantBuffAbility";
    private static final String TABLE_NAME = "merchantBuffAbility";
    private static final String ID_KEY = "id";
    private static final String ABILITY_NAME_KEY = "abilityName";
    private static final String ABILITY_DESCRIPTION_KEY = "abilityDescription";
    private static final String ABILITY_TARGET_KEY = "abilityTarget";
    private static final String BUFF_TYPE_KEY = "buffType";
    private static final String BUFF_AMOUNT_KEY = "buffAmount";

    public MerchantBuffAbilitySqlDatabase(Context context){
        super(context, DATABASE_NAME,null,1);

    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                ID_KEY + " INTEGER PRIMARY KEY," +
                ABILITY_NAME_KEY + " TEXT," +
                ABILITY_DESCRIPTION_KEY + " TEXT," +
                ABILITY_TARGET_KEY + " TEXT," +
                BUFF_TYPE_KEY + " TEXT," +
                BUFF_AMOUNT_KEY + " INTEGER" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void write(BuffAbility ability){
        SQLiteDatabase database = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ABILITY_NAME_KEY, ability.getAbilityName());
        contentValues.put(ABILITY_DESCRIPTION_KEY, ability.getAbilityDescription());
        contentValues.put(ABILITY_TARGET_KEY, ability.getAbilityTarget().toString());
        contentValues.put(BUFF_TYPE_KEY, ability.getBuffType().toString());
        contentValues.put(BUFF_AMOUNT_KEY, ability.getBuffAmount());
        database.insert(TABLE_NAME, null, contentValues);
        database.close();
    }

    public BuffAbility readByAbilityName(String abilityName){
        SQLiteDatabase database = getReadableDatabase();
        Cursor abilityCursor = database.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + ABILITY_NAME_KEY + "='" + abilityName +"'", null);
        BuffAbility foundAbility = null;
        if(abilityCursor.moveToFirst()) {
            do {
                foundAbility = new BuffAbility(abilityCursor.getString(1),
                        abilityCursor.getString(2), AbilityTarget.valueOf(abilityCursor.getString(3)), BuffType.valueOf(abilityCursor.getString(4)), abilityCursor.getInt(5));
            } while(abilityCursor.moveToNext());
        }
        abilityCursor.close();
        database.close();
        return foundAbility;
    }
}
