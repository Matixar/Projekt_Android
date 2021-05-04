package com.example.projektandroid;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HelperDB extends SQLiteOpenHelper {    //pomocnik do bazy danych, odwołania do nazwy tabeli, kolumn i poleceń SQL
    public final static int DB_VERSION = 1;
    public final static String ID = "_id";
    public final static String DB_NAME = "cars.db";
    public final static String TABLE_NAME = "cars";
    public final static String COLUMN1 = "brand";
    public final static String COLUMN2 = "model";
    public final static String COLUMN3 = "fuel_type";
    public final static String COLUMN4 = "www";
    public final static String DB_CREATE = "CREATE TABLE " + TABLE_NAME + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN1 + " TEXT NOT NULL," + COLUMN2 + " TEXT," + COLUMN3 + " TEXT," + COLUMN4 + " TEXT);";
    private static final String DB_DELETE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public HelperDB(Context context) {
        super(context,DB_NAME,null,DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DB_DELETE);
        onCreate(db);
    }
}
