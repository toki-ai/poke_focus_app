package com.example.pokedexapp.data.repository;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.pokedexapp.data.helper.DatabaseHelper;

public class CoinRepository {
    private DatabaseHelper coinDBHelpler;
    public static int DEFAULT_INCREASE_COIN = 10;

    public CoinRepository(Context contex){
        this.coinDBHelpler = new DatabaseHelper(contex);
    }

    public void saveCoin(int coin){
        SQLiteDatabase db = coinDBHelpler.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COLUMN_TOTAL, coin);
        int rowUpdate = db.update(DatabaseHelper.TABLE_COIN, contentValues, null, null);
        if (rowUpdate == 0){
            db.insert(DatabaseHelper.TABLE_COIN, null, contentValues);
        }
        db.close();
    }

    @SuppressLint("Range")
    public int getCoin(){
        SQLiteDatabase db = coinDBHelpler.getWritableDatabase();
        String query = "SELECT " + DatabaseHelper.COLUMN_TOTAL + " FROM " + DatabaseHelper.TABLE_COIN;
        Cursor cursor = db.rawQuery( query,null);
        int coin = 0;
        if(cursor.moveToFirst()){
             coin = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_TOTAL));
        }
        cursor.close();
        db.close();
        return coin;
    }
}
