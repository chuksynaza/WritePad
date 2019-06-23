package com.example.chuksy.playground;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by chuksy on 5/22/19.
 */

public class EntryTable {

    public static final String TABLE_NAME = "entry";
    public static final String KEY_ID = "ID";
    public static final String KEY_EMOTION = "emotion";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_IMAGE_URL = "image_url";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_DATE_CREATED = "date_created";
    public static final String KEY_DATE_UPDATED = "date_updated";

    public static final String STATEMENT_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME
    + " ("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_EMOTION + " INTEGER DEFAULT 0, "
            + KEY_TITLE + " STRING, "
            + KEY_ADDRESS + " STRING, "
            + KEY_IMAGE_URL + " STRING, "
            + KEY_MESSAGE + " TEXT, "
            + KEY_DATE_CREATED + " INTEGER, "
            + KEY_DATE_UPDATED + " INTEGER"
    + " )";

    public static final String STATEMENT_DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static long create(SQLiteDatabase database, Entry entry){

        ContentValues values = new ContentValues();

        values.put(KEY_EMOTION, entry.getEmotion());
        values.put(KEY_TITLE, entry.getTitle());
        values.put(KEY_MESSAGE, entry.getMessage());
        values.put(KEY_ADDRESS, entry.getAddress());
        values.put(KEY_IMAGE_URL, entry.getImageUrl());
        values.put(KEY_DATE_CREATED, entry.getDateCreated());
        values.put(KEY_DATE_UPDATED, System.currentTimeMillis());

        Log.d("PLAY", "Inserting the entry to the database");

        long entryID = database.insert(TABLE_NAME, null, values);

        return entryID;

    }

    public static void update(SQLiteDatabase database, Entry entry){

        ContentValues values = new ContentValues();

        values.put(KEY_EMOTION, entry.getEmotion());
        values.put(KEY_TITLE, entry.getTitle());
        values.put(KEY_MESSAGE, entry.getMessage());
        values.put(KEY_ADDRESS, entry.getAddress());
        values.put(KEY_IMAGE_URL, entry.getImageUrl());
        values.put(KEY_DATE_CREATED, entry.getDateCreated());
        values.put(KEY_DATE_UPDATED, System.currentTimeMillis());

        String[] whereClause = { "" + entry.getID() };

        Log.d("PLAY", "Updating the entry in the database");

        database.update(TABLE_NAME, values, KEY_ID + " = ?", whereClause);

    }


    public static Entry fromCursor(Cursor c){

        if(c == null || c.isAfterLast() || c.isBeforeFirst()){

            return null;

        }

        Entry entry = new Entry(
                c.getLong(c.getColumnIndex(KEY_ID)),
                c.getInt(c.getColumnIndex(KEY_EMOTION)),
                c.getString(c.getColumnIndex(KEY_TITLE)),
                c.getString(c.getColumnIndex(KEY_MESSAGE)),
                c.getString(c.getColumnIndex(KEY_ADDRESS)),
                c.getString(c.getColumnIndex(KEY_IMAGE_URL)),
                c.getLong(c.getColumnIndex(KEY_DATE_CREATED)),
                c.getLong(c.getColumnIndex(KEY_DATE_UPDATED))
        );

        return entry;

    }

    public static ArrayList<Entry> getAll(SQLiteDatabase database){

        ArrayList<Entry> entries = new ArrayList<Entry>();

        Cursor c = database.query(TABLE_NAME, null, null, null, null, null, KEY_DATE_CREATED + " DESC");

        if(c != null){

            c.moveToFirst();

            while(!c.isAfterLast()){

                Entry entry = fromCursor(c);

                entries.add(entry);

                c.moveToNext();

            }

        }

        return entries;

    }

    public static void deleteMany(SQLiteDatabase database, long[] IDs){

        int IDsLength = IDs.length;

        String[] whereClause = new String[IDsLength];

        for(int i = 0; i < IDsLength; i++){

            whereClause[i] = "" + IDs[i];

        }

        database.delete(TABLE_NAME, KEY_ID + " IN (" + TextUtils.join(",", Collections.nCopies(IDs.length, "?"))  + ")", whereClause);

    }

    public static ArrayList<Entry> getAll(SQLiteDatabase database, String searchString){

        ArrayList<Entry> entries = new ArrayList<Entry>();

        String[] whereClause = {
                "%" + searchString + "%",
                "%" + searchString + "%"
        };

        Cursor c = database.query(TABLE_NAME, null, KEY_TITLE + " LIKE ? OR " + KEY_MESSAGE + " LIKE ?", whereClause, null, null, KEY_DATE_CREATED + " DESC");

        if(c != null){

            c.moveToFirst();

            while(!c.isAfterLast()){

                Entry entry = fromCursor(c);

                entries.add(entry);

                c.moveToNext();

            }

        }

        return entries;

    }


    public static ArrayList<Entry> getAllWithEmotions(SQLiteDatabase database){

        ArrayList<Entry> entries = new ArrayList<Entry>();

        Cursor c = database.query(TABLE_NAME, null, KEY_EMOTION + " <> 0", null, null, null, KEY_DATE_CREATED + " DESC");

        if(c != null){

            c.moveToFirst();

            while(!c.isAfterLast()){

                Entry entry = fromCursor(c);

                entries.add(entry);

                c.moveToNext();

            }

        }

        return entries;

    }

    public static Entry getEntry(SQLiteDatabase database, long ID){

        Entry entry = null;

        String[] whereClause = { "" + ID };

        Cursor c = database.query(TABLE_NAME, null, KEY_ID + " = ? ", whereClause, null, null, KEY_DATE_CREATED + " DESC");

        if(c != null){

            c.moveToFirst();

            entry = fromCursor(c);

        }

        Log.d("PLAY", "Selected the entry with ID: " + entry.getID() + ", Title: " + entry.getTitle() + ", Message: " + entry.getMessage() + ", Feeling Level: " + entry.getEmotion());

        return entry;

    }





}
