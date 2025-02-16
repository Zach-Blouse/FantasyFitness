package com.zblouse.fantasyfitness.world;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class GameLocationSqlDatabase extends SQLiteOpenHelper {

    public static final String GAME_LOCATION_DATABASE_NAME = "location";
    public static final String TABLE_NAME = "location";
    public static final String ID_KEY = "id";
    public static final String NAME_KEY = "name";
    public static final String DESCRIPTION_KEY = "description";

    public GameLocationSqlDatabase(Context context){
        super(context, GAME_LOCATION_DATABASE_NAME,null,1);

    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                ID_KEY + " INTEGER PRIMARY KEY," +
                NAME_KEY + " TEXT," +
                DESCRIPTION_KEY + " TEXT" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void addLocationToDatabase(GameLocation location){
        SQLiteDatabase database = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME_KEY, location.getLocationName());
        contentValues.put(DESCRIPTION_KEY, location.getLocationDescription());
        database.insert(TABLE_NAME, null, contentValues);
        database.close();
    }

    public GameLocation getLocationByName(String name){
        SQLiteDatabase database = getReadableDatabase();
        Log.e("GAMELOCATIONDATABASE","QUERY: " + "SELECT * FROM " + TABLE_NAME + " WHERE " + NAME_KEY + "='" + name +"'");
        Cursor locationCursor = database.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + NAME_KEY + "='" + name +"'", null);
        GameLocation foundLocation = null;
        if(locationCursor.moveToFirst()) {
            do {
                foundLocation = new GameLocation(locationCursor.getInt(0),locationCursor.getString(1),
                        locationCursor.getString(2));
            } while(locationCursor.moveToNext());
        } else {
            Log.e("GAMELOCATIONDATABASE","none found that match name total count=" + getAllLocations().size());
        }
        locationCursor.close();
        return foundLocation;
    }

    public GameLocation getLocationById(Integer id){
        SQLiteDatabase database = getReadableDatabase();

        Cursor locationCursor = database.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + ID_KEY + "=" + id, null);
        GameLocation foundLocation = null;
        if(locationCursor.moveToFirst()) {
            do {
                foundLocation = new GameLocation(locationCursor.getInt(0),locationCursor.getString(1),
                        locationCursor.getString(2));
            } while(locationCursor.moveToNext());
        }
        locationCursor.close();
        return foundLocation;
    }

    public List<GameLocation> getAllLocations(){
        SQLiteDatabase database = getReadableDatabase();

        Cursor locationCursor = database.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        ArrayList<GameLocation> locationList = new ArrayList<>();
        if(locationCursor.moveToFirst()) {
            do {
                GameLocation location = new GameLocation(locationCursor.getInt(0),locationCursor.getString(1),
                        locationCursor.getString(2));
               locationList.add(location);
            } while(locationCursor.moveToNext());
        }
        locationCursor.close();
        return locationList;
    }
}
