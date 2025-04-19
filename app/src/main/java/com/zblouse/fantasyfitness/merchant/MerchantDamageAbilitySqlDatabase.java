package com.zblouse.fantasyfitness.merchant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.zblouse.fantasyfitness.combat.cards.AbilityTarget;
import com.zblouse.fantasyfitness.combat.cards.AttackType;
import com.zblouse.fantasyfitness.combat.cards.DamageAbility;
import com.zblouse.fantasyfitness.combat.cards.DamageType;
import com.zblouse.fantasyfitness.world.GameLocation;

public class MerchantDamageAbilitySqlDatabase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "merchantDamageAbility";
    private static final String TABLE_NAME = "merchantDamageAbility";
    private static final String ID_KEY = "id";
    private static final String ABILITY_NAME_KEY = "abilityName";
    private static final String ABILITY_DESCRIPTION_KEY = "abilityDescription";
    private static final String ABILITY_TARGET_KEY = "abilityTarget";
    private static final String DAMAGE_TYPE_KEY = "damageType";
    private static final String ATTACK_TYPE_KEY = "attackType";
    private static final String DAMAGE_AMOUNT_KEY = "damageAmount";

    public MerchantDamageAbilitySqlDatabase(Context context){
        super(context, DATABASE_NAME,null,1);

    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                ID_KEY + " INTEGER PRIMARY KEY," +
                ABILITY_NAME_KEY + " TEXT," +
                ABILITY_DESCRIPTION_KEY + " TEXT," +
                ABILITY_TARGET_KEY + " TEXT," +
                DAMAGE_TYPE_KEY + " TEXT," +
                ATTACK_TYPE_KEY + " TEXT," +
                DAMAGE_AMOUNT_KEY + " INTEGER" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void write(DamageAbility damageAbility){
        SQLiteDatabase database = getWritableDatabase();
        Log.e("MerchantDamageAbilitySqlDatabase","Writing Damage Ability: " + damageAbility.getAbilityName());
        ContentValues contentValues = new ContentValues();
        contentValues.put(ABILITY_NAME_KEY, damageAbility.getAbilityName());
        contentValues.put(ABILITY_DESCRIPTION_KEY, damageAbility.getAbilityDescription());
        contentValues.put(ABILITY_TARGET_KEY, damageAbility.getAbilityTarget().toString());
        contentValues.put(DAMAGE_TYPE_KEY, damageAbility.getDamageType().toString());
        contentValues.put(ATTACK_TYPE_KEY, damageAbility.getAttackType().toString());
        contentValues.put(DAMAGE_AMOUNT_KEY, damageAbility.getDamageAmount());
        database.insert(TABLE_NAME, null, contentValues);
        database.close();
    }

    public DamageAbility readByAbilityName(String abilityName){
        SQLiteDatabase database = getReadableDatabase();
        Cursor abilityCursor = database.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + ABILITY_NAME_KEY + "='" + abilityName +"'", null);
        DamageAbility foundAbility = null;
        if(abilityCursor.moveToFirst()) {
            do {
                Log.e("MerchantDamageAbilitySqlDatabase","Damage Ability Found: " + abilityName);
                foundAbility = new DamageAbility(abilityCursor.getString(1),
                        abilityCursor.getString(2), AbilityTarget.valueOf(abilityCursor.getString(3)), DamageType.valueOf(abilityCursor.getString(4)), AttackType.valueOf(abilityCursor.getString(5)), abilityCursor.getInt(6));
            } while(abilityCursor.moveToNext());
        }
        abilityCursor.close();
        database.close();
        return foundAbility;
    }
}
