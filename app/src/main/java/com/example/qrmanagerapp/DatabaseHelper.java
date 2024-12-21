package com.example.qrmanagerapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "qr_manager.db";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_NAME = "qr_codes";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_CONTENT = "content";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CONTENT + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertQRCode(String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CONTENT, content);
        db.insert(TABLE_NAME, null, values);
    }

    public ArrayList<String> getAllQRCodes() {
        ArrayList<String> qrCodes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_CONTENT}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                qrCodes.add(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return qrCodes;
    }

    public void deleteAllQRCodes() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
    }
}