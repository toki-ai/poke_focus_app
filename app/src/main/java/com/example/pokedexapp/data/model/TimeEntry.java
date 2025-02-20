package com.example.pokedexapp.data.model;

import androidx.annotation.ColorRes;

import com.example.pokedexapp.R;

public class TimeEntry {
    private int totalBlock;
    private int day;
    private int month;
    private int year;

    public TimeEntry(int year, int month, int day, int totalBlock) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.totalBlock = totalBlock;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getTotalBlock() {
        return totalBlock;
    }

    public void setTotalBlock(int totalBlock) {
        this.totalBlock = totalBlock;
    }

}
