package com.zblouse.fantasyfitness.merchant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.zblouse.fantasyfitness.combat.cards.Ability;
import com.zblouse.fantasyfitness.combat.cards.AbilityType;
import com.zblouse.fantasyfitness.combat.cards.Card;
import com.zblouse.fantasyfitness.combat.cards.HealAbility;
import com.zblouse.fantasyfitness.combat.cards.ItemCard;
import com.zblouse.fantasyfitness.combat.cards.ItemType;
import com.zblouse.fantasyfitness.world.GameLocationDistance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MerchantCardSqlDatabase extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "merchantCard";
    private static String TABLE_NAME = "merchantCard";
    private static String ID_KEY = "id";
    private static String TAG_KEY = "tag";
    private static String CARD_NAME_KEY = "cardName";
    private static String CARD_DESCRIPTION_KEY = "cardDescription";
    private static String ITEM_TYPE_KEY = "itemType";
    private static String ITEM_ABILITY_NAME = "itemAbilityName";
    private static String ITEM_ABILITY_TYPE = "itemAbilityType";
    private static String PRICE_KEY = "price";

    public MerchantCardSqlDatabase(Context context){
        super(context, DATABASE_NAME,null,1);

    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                ID_KEY + " INTEGER PRIMARY KEY," +
                TAG_KEY + " TEXT," +
                CARD_NAME_KEY + " TEXT," +
                CARD_DESCRIPTION_KEY + " TEXT," +
                ITEM_TYPE_KEY + " TEXT," +
                ITEM_ABILITY_NAME + " TEXT," +
                ITEM_ABILITY_TYPE + " TEXT," +
                PRICE_KEY + " INTEGER" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void write(Merchant merchant){
        SQLiteDatabase database = getWritableDatabase();
        for(Card card: merchant.getCardPriceMap().keySet()) {
            Log.e("MerchantCardSqlDatabase", "Writing card: " + card.getCardName() + " ability name: " + ((ItemCard)card).getAbility().getAbilityName());
            ContentValues contentValues = new ContentValues();
            contentValues.put(TAG_KEY, merchant.getMerchantTag());
            contentValues.put(CARD_NAME_KEY, card.getCardName());
            contentValues.put(CARD_DESCRIPTION_KEY, card.getCardDescription());
            contentValues.put(ITEM_TYPE_KEY, ((ItemCard)card).getItemType().toString());
            contentValues.put(ITEM_ABILITY_NAME, ((ItemCard)card).getAbility().getAbilityName());
            contentValues.put(ITEM_ABILITY_TYPE, ((ItemCard)card).getAbility().getAbilityType().toString());
            contentValues.put(PRICE_KEY, merchant.getCardPriceMap().get(card));
            database.insert(TABLE_NAME, null, contentValues);
        }
        database.close();
    }
    public Merchant getMerchantByTag(String tag, MerchantAbilityRepository merchantAbilityRepository){
        SQLiteDatabase database = getReadableDatabase();
        Map<Card,Integer> merchantCardPriceMap = new HashMap<>();
        Cursor merchantCursor = database.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + TAG_KEY + "='" + tag +"'", null);
        if(merchantCursor.moveToFirst()) {
            do {
                Ability cardAbility = merchantAbilityRepository.getMerchantAbilityByName(merchantCursor.getString(5), AbilityType.valueOf(merchantCursor.getString(6)));
                Card card = new ItemCard("merchant","merchantCard",merchantCursor.getString(2),merchantCursor.getString(3), ItemType.valueOf(merchantCursor.getString(4)),cardAbility);
                merchantCardPriceMap.put(card,merchantCursor.getInt(7));
            } while(merchantCursor.moveToNext());
        }
        merchantCursor.close();
        database.close();
        return new Merchant( tag, merchantCardPriceMap);
    }
}
