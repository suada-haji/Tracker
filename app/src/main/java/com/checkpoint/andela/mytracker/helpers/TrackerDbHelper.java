package com.checkpoint.andela.mytracker.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by suadahaji.
 */
public class TrackerDbHelper extends SQLiteOpenHelper {

    private SQLiteDatabase writeDatabase;


    public TrackerDbHelper(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
        writeDatabase = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase liteDatabase) {
        String create_table = "CREATE TABLE IF NOT EXISTS " + Constants.TABLE_NAME + " (" +
                Constants.TABLE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Constants.TABLE_COLUMN_LOCATION + " TEXT, " +
                Constants.TABLE_COLUMN_DATE + " TEXT, " +
                Constants.TABLE_COLUMN_COORDINATES + " TEXT, " +
                Constants.TABLE_COLUMN_ACTIVITY + " TEXT, " +
                Constants.TABLE_COLUMN_DURATION + " TEXT );";
        liteDatabase.execSQL(create_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase liteDatabase, int oldVersion, int newVersion) {
        liteDatabase.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);
        onCreate(liteDatabase);
    }

    public SQLiteDatabase getDB() {
        return writeDatabase;
    }

    public void setWriteDatabase(SQLiteDatabase database) {
        this.writeDatabase = database;
    }
}
