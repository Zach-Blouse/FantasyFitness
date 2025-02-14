package com.zblouse.fantasyfitness.core;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public abstract class SqlDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME="fantasy_fitness_database";
    private static final int DATABASE_VERSION=1;

    public SqlDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}
