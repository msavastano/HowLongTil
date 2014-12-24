package com.mikesavastano.howlongtil;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MyDBHandler extends SQLiteOpenHelper {
    final String TAG = "com.mikesavastano.howlongdetail.MyDBHandler";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "eventsDB.db";
    private static final String TABLE_EVENTS = "events";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DATE = "date";



    public MyDBHandler(Context context, String name,  SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_EVENTS_TABLE = "CREATE TABLE " + TABLE_EVENTS + "( " + COLUMN_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_NAME + " TEXT," + COLUMN_DATE + " DATE" + ")";

        db.execSQL(CREATE_EVENTS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        onCreate(db);
    }

    public void addEvent(EventDate event) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, event.getEventName());
        values.put(COLUMN_DATE, event.getDate().getTime());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_EVENTS, null, values);
        db.close();
    }

    public Cursor getAllEventDates() {

        return getReadableDatabase().query(TABLE_EVENTS,
                new String[] { "_id", "name", "date"},
                null, null, null, null, null);
    }

    public Cursor getDateByID(long id){
        String i = String.valueOf(id);
        return getReadableDatabase().query(TABLE_EVENTS, new String[]{"date"},
                "_id = "+i,null,null,null,null);
    }

    public void deleteByID(long id){
        String i = String.valueOf(id);
        getWritableDatabase().delete(TABLE_EVENTS, "_id = "+i, null);
    }

    /*private EventDate cursorToEvent(Cursor cursor) {
        EventDate event = new EventDate();
        event.setID(cursor.getInt(0));
        event.setDate(cursor.get1));
        return event;
    }*/
}
