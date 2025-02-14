package com.zblouse.fantasyfitness.world;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zblouse.fantasyfitness.core.SqlDatabase;

import java.util.ArrayList;
import java.util.List;

public class GameLocationDistanceSqlDatabase extends SqlDatabase {

    public static final String TABLE_NAME = "location_distance";
    public static final String ID_KEY = "id";
    public static final String LOCATION_1_KEY = "location1";
    public static final String LOCATION_2_KEY = "location2";
    public static final String DISTANCE_KEY = "distance";

    public GameLocationDistanceSqlDatabase(Context context){
        super(context);
    }

    /**
     * Creates the PRODUCTS_TABLE_NAME table
     * @param database
     */
    @Override
    public void onCreate(SQLiteDatabase database) {
        String createLocationDistanceTableQuery = "CREATE TABLE " + TABLE_NAME + "(" +
                ID_KEY + " INTEGER PRIMARY KEY," +
                LOCATION_1_KEY + " INTEGER," +
                LOCATION_2_KEY + " INTEGER," +
                DISTANCE_KEY + " REAL," +
                "FOREIGN KEY(" + LOCATION_1_KEY + ") REFERENCES " + GameLocationSqlDatabase.TABLE_NAME + "(" + GameLocationSqlDatabase.ID_KEY + ")," +
                "FOREIGN KEY(" + LOCATION_2_KEY + ") REFERENCES " + GameLocationSqlDatabase.TABLE_NAME + "(" + GameLocationSqlDatabase.ID_KEY + ")" +
                ")";
        database.execSQL(createLocationDistanceTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void addLocationDistanceToDatabase(GameLocationDistance locationDistance){
        SQLiteDatabase database = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(LOCATION_1_KEY, locationDistance.getLocation1Id());
        contentValues.put(LOCATION_2_KEY, locationDistance.getLocation2Id());
        contentValues.put(DISTANCE_KEY, locationDistance.getDistanceMiles());
        database.insert(TABLE_NAME, null, contentValues);
        database.close();
    }

    public List<GameLocationDistance> getAllLocationDistancesForLocation(GameLocation location){
        SQLiteDatabase database = getReadableDatabase();
        ArrayList<GameLocationDistance> locationDistanceList = new ArrayList<>();
        Cursor locationDistance1Cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + LOCATION_1_KEY + "=" + location.getId(), null);
        if(locationDistance1Cursor.moveToFirst()) {
            do {
                GameLocationDistance locationDistance = new GameLocationDistance(locationDistance1Cursor.getInt(0),locationDistance1Cursor.getInt(1),
                        locationDistance1Cursor.getInt(2), locationDistance1Cursor.getDouble(3));
                locationDistanceList.add(locationDistance);
            } while(locationDistance1Cursor.moveToNext());
        }
        locationDistance1Cursor.close();
        Cursor locationDistance2Cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + LOCATION_2_KEY + "=" + location.getId(), null);
        if(locationDistance2Cursor.moveToFirst()) {
            do {
                GameLocationDistance locationDistance = new GameLocationDistance(locationDistance2Cursor.getInt(0),locationDistance2Cursor.getInt(1),
                        locationDistance2Cursor.getInt(2), locationDistance2Cursor.getDouble(3));
                locationDistanceList.add(locationDistance);
            } while(locationDistance2Cursor.moveToNext());
        }
        locationDistance2Cursor.close();
        return locationDistanceList;
    }
}
