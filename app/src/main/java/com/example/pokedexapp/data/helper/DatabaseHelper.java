package com.example.pokedexapp.data.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "poke_db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_COIN = "coin";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TOTAL = "total";

    public static final String TABLE_BLOCK_TIMING = "block_timing";
    public static final String BLOCK_COLUMN_ID = "id";
    public static final String BLOCK_COLUMN_TOTAL = "total";
    public static final String BLOCK_COLUMN_YEAR = "year";
    public static final String BLOCK_COLUMN_DATE = "date";
    public static final String BLOCK_COLUMN_MONTH = "month";

    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_COIN + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TOTAL + " INTEGER DEFAULT 0);";

    private static final String CREATE_TABLE_BLOCK_TIMING =
            "CREATE TABLE " + TABLE_BLOCK_TIMING + " (" +
                    BLOCK_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    BLOCK_COLUMN_TOTAL + " INTEGER NOT NULL, " +
                    BLOCK_COLUMN_YEAR + " INTEGER NOT NULL, " +
                    BLOCK_COLUMN_MONTH + " INTEGER NOT NULL, " +
                    BLOCK_COLUMN_DATE + " INTEGER NOT NULL)";
    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
        sqLiteDatabase.execSQL(CREATE_TABLE_BLOCK_TIMING);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_COIN);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_BLOCK_TIMING);
        onCreate(sqLiteDatabase);
    }
}
