package com.ndurance.mobileapp.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TokenDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "UserAuth.db";
    private static final int DATABASE_VERSION = 1;

    // Table Name and Columns
    public static final String TABLE_NAME = "Auth";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_JWT_TOKEN = "jwt_token";

    // SQL Query to Create Table
    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY, " + // Ensure single row by managing id
                    COLUMN_USER_ID + " TEXT, " +
                    COLUMN_JWT_TOKEN + " TEXT)";

    public TokenDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}