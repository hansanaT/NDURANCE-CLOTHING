package com.ndurance.mobileapp.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TokenManager {
    private static final int SINGLE_ROW_ID = 1;
    private TokenDatabaseHelper dbHelper;

    public TokenManager(Context context) {
        dbHelper = new TokenDatabaseHelper(context);
    }
    public void saveTokenAndUserId(String userId, String jwtToken) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.query(TokenDatabaseHelper.TABLE_NAME, null,
                TokenDatabaseHelper.COLUMN_ID + "=?", new String[]{String.valueOf(SINGLE_ROW_ID)},
                null, null, null);

        ContentValues values = new ContentValues();
        values.put(TokenDatabaseHelper.COLUMN_USER_ID, userId);
        values.put(TokenDatabaseHelper.COLUMN_JWT_TOKEN, jwtToken);

        if (cursor != null && cursor.moveToFirst()) {
            db.update(TokenDatabaseHelper.TABLE_NAME, values, TokenDatabaseHelper.COLUMN_ID + "=?",
                    new String[]{String.valueOf(SINGLE_ROW_ID)});
        } else {
            values.put(TokenDatabaseHelper.COLUMN_ID, SINGLE_ROW_ID); // Set fixed ID
            db.insert(TokenDatabaseHelper.TABLE_NAME, null, values);
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();
    }

    public String getUserId() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TokenDatabaseHelper.TABLE_NAME,
                new String[]{TokenDatabaseHelper.COLUMN_USER_ID},
                TokenDatabaseHelper.COLUMN_ID + "=?",
                new String[]{String.valueOf(SINGLE_ROW_ID)},
                null, null, null);

        String userId = null;
        if (cursor != null && cursor.moveToFirst()) {
            int userIdIndex = cursor.getColumnIndex(TokenDatabaseHelper.COLUMN_USER_ID);
            if (userIdIndex >= 0) {
                userId = cursor.getString(userIdIndex);
            }
            cursor.close();
        }
        db.close();
        return userId;
    }

    public String getJwtToken() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TokenDatabaseHelper.TABLE_NAME,
                new String[]{TokenDatabaseHelper.COLUMN_JWT_TOKEN},
                TokenDatabaseHelper.COLUMN_ID + "=?",
                new String[]{String.valueOf(SINGLE_ROW_ID)},
                null, null, null);

        String jwtToken = null;
        if (cursor != null && cursor.moveToFirst()) {
            int tokenIndex = cursor.getColumnIndex(TokenDatabaseHelper.COLUMN_JWT_TOKEN);
            if (tokenIndex >= 0) {
                jwtToken = cursor.getString(tokenIndex);
            }
            cursor.close();
        }
        db.close();
        return jwtToken;
    }

    public void clearData() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TokenDatabaseHelper.TABLE_NAME,
                TokenDatabaseHelper.COLUMN_ID + "=?",
                new String[]{String.valueOf(SINGLE_ROW_ID)});
        db.close();
    }
}