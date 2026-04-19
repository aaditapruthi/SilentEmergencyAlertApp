package com.example.silentemergencyalertapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "EmergencyApp.db";
    private static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE Contacts (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, phone TEXT)");

        db.execSQL("CREATE TABLE AlertHistory (id INTEGER PRIMARY KEY AUTOINCREMENT, contact TEXT, date TEXT, time TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Contacts");
        db.execSQL("DROP TABLE IF EXISTS AlertHistory");
        onCreate(db);
    }

    public boolean insertContact(String name, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("phone", phone);
        long result = db.insert("Contacts", null, cv);
        return result != -1;
    }

    public boolean deleteContact(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("Contacts", "id=?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    public Cursor getAllContacts() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM Contacts", null);
    }

    public boolean insertHistory(String contact, String date, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("contact", contact);
        cv.put("date", date);
        cv.put("time", time);
        long result = db.insert("AlertHistory", null, cv);
        return result != -1;
    }

    public Cursor getAllHistory() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM AlertHistory ORDER BY id DESC", null);
    }

    public boolean deleteHistory(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("AlertHistory", "id=?", new String[]{String.valueOf(id)});
        return result > 0;
    }
}