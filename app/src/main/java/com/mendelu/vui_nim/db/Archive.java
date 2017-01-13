package com.mendelu.vui_nim.db;

import android.provider.BaseColumns;

/**
 * Created by Jakub on 06.06.2016.
 *
 * Class represents one played game in database.
 * Consists of id, name, result, number and date.
 * Provides all needed setters and getters.
 */
public class Archive {

    public static final String TABLE_NAME = "archive";
    public static final String ID = BaseColumns._ID;
    public static final String NAME = "name";
    public static final String RESULT = "result";
    public static final String NUMBER = "number";
    public static final String DATE = "date";
    static final String[] COLUMNS = {ID, NAME, RESULT, NUMBER, DATE};


    private long id;
    private String name;
    private String result;
    private String number;
    private String date;

    public Archive(String name, String result, String number, String date) {
        this.name = name;
        this.result = result;
        this.number = number;
        this.date = date;
    }

    Archive() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

