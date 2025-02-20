package com.example.pokedexapp.data.repository;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.pokedexapp.data.helper.DatabaseHelper;
import com.example.pokedexapp.data.model.TimeEntry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlockTimingRepository {
    private DatabaseHelper helper;

    public BlockTimingRepository(Context contex){
        this.helper = new DatabaseHelper(contex);
    }

    public void save(TimeEntry entry) {
        SQLiteDatabase db = helper.getWritableDatabase();

        String query = "SELECT " + DatabaseHelper.BLOCK_COLUMN_ID +
                " FROM " + DatabaseHelper.TABLE_BLOCK_TIMING +
                " WHERE " + DatabaseHelper.BLOCK_COLUMN_DATE + " = ? " +
                " AND " + DatabaseHelper.BLOCK_COLUMN_MONTH + " = ? " +
                " AND " + DatabaseHelper.BLOCK_COLUMN_YEAR + " = ? ";

        String[] selectionArgs = {
                String.valueOf(entry.getDay()),
                String.valueOf(entry.getMonth()),
                String.valueOf(entry.getYear())
        };

        Cursor cursor = db.rawQuery(query, selectionArgs);

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.BLOCK_COLUMN_TOTAL, entry.getTotalBlock());
        values.put(DatabaseHelper.BLOCK_COLUMN_DATE, entry.getDay());
        values.put(DatabaseHelper.BLOCK_COLUMN_MONTH, entry.getMonth());
        values.put(DatabaseHelper.BLOCK_COLUMN_YEAR, entry.getYear());

        if (cursor.moveToFirst()) {
            int existingId = cursor.getInt(0);
            db.update(DatabaseHelper.TABLE_BLOCK_TIMING, values,
                    DatabaseHelper.BLOCK_COLUMN_ID + " = ?",
                    new String[]{String.valueOf(existingId)});
        } else {
            db.insert(DatabaseHelper.TABLE_BLOCK_TIMING, null, values);
        }

        cursor.close();
        db.close();
    }


    public Map<Integer, Integer> getMonthBlockEntry(int month, int year) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Map<Integer, Integer> timeEntries = new HashMap<>();

        String query = "SELECT " + DatabaseHelper.BLOCK_COLUMN_ID + ", " +
                DatabaseHelper.BLOCK_COLUMN_TOTAL + ", " +
                DatabaseHelper.BLOCK_COLUMN_DATE + ", " +
                DatabaseHelper.BLOCK_COLUMN_MONTH + ", " +
                DatabaseHelper.BLOCK_COLUMN_YEAR +
                " FROM " + DatabaseHelper.TABLE_BLOCK_TIMING +
                " WHERE " + DatabaseHelper.BLOCK_COLUMN_MONTH + " = ? " +
                " AND " + DatabaseHelper.BLOCK_COLUMN_YEAR + " = ? " +
                " ORDER BY " + DatabaseHelper.BLOCK_COLUMN_DATE + " ASC";

        String[] selectionArgs = {String.valueOf(month), String.valueOf(year)};
        Cursor cursor = db.rawQuery(query, selectionArgs);

        if (cursor.moveToFirst()) {
            do {
                int totalBlock = cursor.getInt(1);
                int day = cursor.getInt(2);

                timeEntries.put(day, totalBlock);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return timeEntries;
    }
}
