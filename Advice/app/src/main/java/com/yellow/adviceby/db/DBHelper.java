package com.yellow.adviceby.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by SheykinAV on 21.09.2015.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "adviceby";

    public static abstract class UserTable {
        public static final String TABLE_NAME = "user_connectivity";
        public static final String ID = "id";
        public static final String IS_CONNECTED = "is_connected";
        public static final String CONNECTION_SOURCE = "connection_source";
        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " ( " +
                        ID + " INTEGER PRIMARY KEY, " +
                        IS_CONNECTED + " BOOLEAN, " +
                        CONNECTION_SOURCE + " TEXT " +
                        " )";
        public static final String SQL_DELETE_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
        public static final String SQL_SELECT_ALL =
                "SELECT * FROM " + TABLE_NAME;
    }

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("La", "DB.onCreate");
        db.execSQL(UserTable.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(UserTable.SQL_DELETE_TABLE);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}