package com.mikesavastano.howlongtil;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyDBHandler extends SQLiteOpenHelper {
    final String TAG = "com.mikesavastano.howlongdetail.MyDBHandler";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "eventsDB.db";
    private static final String TABLE_EVENTS = "events";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DATE = "date";


    public MyDBHandler(Context context, String name,  SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_EVENTS_TABLE = "CREATE TABLE " + TABLE_EVENTS + "(" + COLUMN_ID +
                " INTEGER PRIMARY KEY," + COLUMN_NAME + " TEXT," + COLUMN_DATE + " DATE" + ")";

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

    public List<String> getAllEventDates() {
        String query = "Select * FROM " + TABLE_EVENTS;

        List<String> events = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Cursor cursor_query = getReadableDatabase().query(TABLE_EVENTS,
                new String[] { "id", "name", "date"},
                null, null, null, null, null);

        EventDate eventDate = new EventDate();

        cursor_query.moveToFirst();
        while (!cursor_query.isAfterLast()) {
            //EventDate eventDate = cursorToComment(eventDate);

            String name = cursor_query.getString(1);
            events.add(name);
            Integer ID = cursor_query.getInt(0);
            long edate = cursor_query.getLong(3);
            cursor_query.moveToNext();
        }
        // make sure to close the cursor
        cursor_query.close();
        Log.i(TAG, events.toString());
        return events;
    }

    /*private EventDate cursorToEvent(Cursor cursor) {
        EventDate event = new EventDate();
        event.setID(cursor.getInt(0));
        event.setDate(cursor.get1));
        return event;
    }*/
}
