package com.checkpoint.andela.mytracker.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.checkpoint.andela.mytracker.model.TrackerModel;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * Created by suadahaji.
 */
public class TrackerDbHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "noteinfo.db";
    private static final int DATABASE_VERSION = 1;

    public TrackerDbHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
    }

    static {
        /**
         * Registering the Note model
         */
        cupboard().register(TrackerModel.class);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /**
         * This ensures that all tables are created
         */
        cupboard().withDatabase(db).createTables();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /**
         * This will upgrade tables, adding columns and new tables.
         */
        cupboard().withDatabase(db).upgradeTables();

    }
}
