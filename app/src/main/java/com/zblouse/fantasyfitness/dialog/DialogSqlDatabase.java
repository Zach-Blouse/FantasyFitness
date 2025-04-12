package com.zblouse.fantasyfitness.dialog;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.zblouse.fantasyfitness.world.GameLocation;

public class DialogSqlDatabase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "dialog";
    private static final String TABLE_NAME = "dialog";
    private static final String ID_KEY = "id";
    private static final String REFERENCE_ID_KEY = "reference_id";
    private static final String OPTION_TEXT_KEY = "option_text";
    private static final String FLAVOR_TEXT_KEY = "flavor_text";
    private static final String DIALOG_1_KEY = "dialog1";
    private static final String DIALOG_2_KEY = "dialog2";
    private static final String DIALOG_3_KEY = "dialog3";
    private static final String DIALOG_4_KEY = "dialog4";
    private static final String DIALOG_AFFECT_TYPE_KEY = "dialogAffectType";
    private static final String QUEST_UUID_KEY = "questUUIDKey";
    private static final String QUEST_OBJECTIVE_KEY = "questObjectiveUUIDKey";

    public DialogSqlDatabase(Context context){
        super(context, DATABASE_NAME,null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                ID_KEY + " INTEGER PRIMARY KEY," +
                REFERENCE_ID_KEY + " TEXT," +
                OPTION_TEXT_KEY + " TEXT," +
                FLAVOR_TEXT_KEY + " TEXT," +
                DIALOG_1_KEY + " TEXT," +
                DIALOG_2_KEY + " TEXT," +
                DIALOG_3_KEY + " TEXT," +
                DIALOG_4_KEY + " TEXT," +
                DIALOG_AFFECT_TYPE_KEY + " TEXT," +
                QUEST_UUID_KEY + " TEXT," +
                QUEST_OBJECTIVE_KEY + " TEXT"
                +")");
    }

    public void addDialogToDatabase(Dialog dialog){
        SQLiteDatabase database = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(REFERENCE_ID_KEY, dialog.getReferenceId());
        contentValues.put(OPTION_TEXT_KEY, dialog.getOptionText());
        contentValues.put(FLAVOR_TEXT_KEY, dialog.getFlavorText());
        contentValues.put(DIALOG_1_KEY, dialog.getDialogOption1());
        contentValues.put(DIALOG_2_KEY, dialog.getDialogOption2());
        contentValues.put(DIALOG_3_KEY, dialog.getDialogOption3());
        contentValues.put(DIALOG_4_KEY, dialog.getDialogOption4());
        contentValues.put(DIALOG_AFFECT_TYPE_KEY, dialog.getDialogAffect().getDialogAffectType().toString());
        contentValues.put(QUEST_UUID_KEY, dialog.getDialogAffect().getQuestUuid());
        contentValues.put(QUEST_OBJECTIVE_KEY, dialog.getDialogAffect().getQuestObjectiveUuid());

        database.insert(TABLE_NAME, null, contentValues);
        database.close();
    }

    public Dialog getDialogByReferenceId(String referenceId){
        SQLiteDatabase database = getReadableDatabase();
        Cursor dialogCursor = database.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + REFERENCE_ID_KEY + "='" + referenceId +"'", null);
        Dialog dialog = null;
        if(dialogCursor.moveToFirst()) {
            do {
                DialogAffect dialogAffect = new DialogAffect(DialogAffectType.valueOf(dialogCursor.getString(8)));
                dialogAffect.setQuestUuid(dialogCursor.getString(9));
                dialogAffect.setQuestObjectiveUuid(dialogCursor.getString(10));
                dialog = new Dialog(dialogCursor.getInt(0),dialogCursor.getString(1),
                        dialogCursor.getString(2), dialogCursor.getString(3), dialogCursor.getString(4),
                        dialogCursor.getString(5),dialogCursor.getString(6), dialogCursor.getString(7), dialogAffect);
            } while(dialogCursor.moveToNext());
        }
        dialogCursor.close();
        return dialog;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
