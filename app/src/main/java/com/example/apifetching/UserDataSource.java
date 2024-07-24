package com.example.apifetching;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class UserDataSource {

    private SQLiteDatabase database;
    private UserDbHelper dbHelper;
    private static final String TAG = "UserDataSource";

    public UserDataSource(Context context) {
        dbHelper = new UserDbHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public boolean isUserLoggedIn() {
        Cursor cursor = database.query(UserDbHelper.TABLE_USERS, null, null, null, null, null, null);
        boolean loggedIn = cursor.moveToFirst();
        cursor.close();
        return loggedIn;
    }

    public String getUserResponse() {
        Cursor cursor = database.query(UserDbHelper.TABLE_USERS, new String[]{UserDbHelper.COLUMN_RESPONSE}, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String response = cursor.getString(cursor.getColumnIndex(UserDbHelper.COLUMN_RESPONSE));
            cursor.close();
            return response;
        }
        return null;
    }

    public void insertUser(String response) {
        ContentValues values = new ContentValues();
        values.put(UserDbHelper.COLUMN_RESPONSE, response);

        long result = database.insert(UserDbHelper.TABLE_USERS, null, values);
        if (result == -1) {
            Log.e(TAG, "Failed to insert user");
        } else {
            Log.d(TAG, "User inserted successfully");
        }
    }

    public void deleteUser() {
        database.delete(UserDbHelper.TABLE_USERS, null, null);
    }

    private static class UserDbHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "user.db";
        private static final int DATABASE_VERSION = 1;

        private static final String TABLE_USERS = "users";
        private static final String COLUMN_ID = "_id";
        private static final String COLUMN_RESPONSE = "response";

        private static final String TABLE_CREATE =
                "CREATE TABLE " + TABLE_USERS + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_RESPONSE + " TEXT);";

        public UserDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(TABLE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            onCreate(db);
        }
    }
}
