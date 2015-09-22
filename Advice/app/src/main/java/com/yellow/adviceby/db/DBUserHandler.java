package com.yellow.adviceby.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yellow.adviceby.model.User;

/**
 * Created by SheykinAV on 21.09.2015.
 */
public class DBUserHandler extends DBHelper implements IUserDAO {

    public DBUserHandler(Context context) {
        super(context);
    }

    public boolean hasRecord() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.getMaximumSize() > 0;
    }

    /**
     * Add user
     * @param user user object
     */
    @Override
    public void create(User user) {

        ContentValues values = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();

        values.put(UserTable.ID, user.getId());
        values.put(UserTable.IS_CONNECTED, user.getIsConnected());
        values.put(UserTable.CONNECTION_SOURCE, user.getConnectionSource());

        // Insert the new raw, returning the primary key value of the new raw
        db.insert(
                UserTable.TABLE_NAME,
                null,
                values);

        db.close();
    }

    /**
     * Read user data
     *
     * @return user
     */
    @Override
    public User read() {
        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;

        String[] projection = {
                UserTable.ID,
                UserTable.IS_CONNECTED,
                UserTable.CONNECTION_SOURCE
        };

        Cursor cursor = db.rawQuery(UserTable.SQL_SELECT_ALL, null);
    /*    Cursor cursor = db.query(
                UserTable.TABLE_NAME,
                new String[]{UserTable.ID + "=?"},
                String.valueOf(id), null, null, null, null
        ); */

        if(cursor.moveToFirst()) {
            user = new User(
                    Integer.parseInt(cursor.getString(0)),
                    Boolean.parseBoolean(cursor.getString(1)),
                    cursor.getString(2));
        }

        db.close();
        return user;
    }

    /**
     * Update user
     * @param user user
     */
    @Override
    public void update(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UserTable.ID, user.getId());
        values.put(UserTable.IS_CONNECTED, user.getIsConnected());
        values.put(UserTable.CONNECTION_SOURCE, user.getConnectionSource());

        // updating row
        db.update(
                UserTable.TABLE_NAME, values,
                UserTable.ID + " = ?",
                new String[]{String.valueOf(user.getId())});

        db.close();
    }

    @Override
    public void delete(User user) {

    }
}
