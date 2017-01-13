package com.mendelu.vui_nim.db;

import static com.mendelu.vui_nim.db.Archive.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Jakub on 06.06.2016.
 *
 * A helper class to manage database creation and version management.
 * It is used for create, upgrade, delete and select function.
 */

public class ArchiveOpenHelper  extends SQLiteOpenHelper {

    private static final String TAG = "ArchiveOpenHelper";
    private static final String DATABASE_NAME = "directoryPlayers.db";
    private static final int DATABASE_VERSION = 1;

    public ArchiveOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Method create new table in db.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + "(" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                NAME + " TEXT UNIQUE NOT NULL," +
                RESULT + " TEXT," +
                NUMBER + " TEXT," +
                DATE + " TEXT)";
        Log.d(TAG, String.format("Create table: %s", sql));
        db.execSQL(sql);
    }

    /**
     * Method update whole table in the db.
     * Delete previously table and add new one.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        Log.d(TAG, String.format("drop table: %s", sql));
        db.execSQL(sql);
        onCreate(db);
    }

    /**
     * Method add one row into the db with new played game.
     */
    public void insert(Archive archive) {
        ContentValues values = new ContentValues();
        values.put(NAME, archive.getName());
        values.put(RESULT, archive.getResult());
        values.put(NUMBER, archive.getNumber());
        values.put(DATE, archive.getDate());
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            db.insert(TABLE_NAME, null, values);
        } catch (Exception e) {
            Log.e(TAG, "Insert archive failed.", e);
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * Method delete one row (a played game) in the db.
     */
    public void delete(String id){
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            db.delete(TABLE_NAME, "_ID = ?", new String[]{id});
        } catch (Exception e) {
            Log.e(TAG, "Delete archive failed.", e);
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    /**
     * Method select list of rows (a played games) from the db.
     * Random read-write access variable c select table by which is
     * possible to select rows step by step.
     */
    public List<Archive> findAllAdresar() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(Archive.TABLE_NAME,
                Archive.COLUMNS, null, null, null, null, null);

        List<Archive> archiveList = new LinkedList<>();
        while (c.moveToNext()) {
            Archive s = new Archive();
            s.setId(c.getLong(0));
            s.setName(c.getString(1));
            s.setResult(c.getString(2));
            s.setNumber(c.getString(3));
            s.setDate(c.getString(4));
            archiveList.add(s);
        }
        c.close();
        db.close();
        return archiveList;
    }
}
