package com.mikesavastano.howlongtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHandler extends SQLiteOpenHelper {

    final String TAG = "com.mikesavastano.howlongdetail.MyDBHandler";

    //Db constants
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "eventsDB.db";
    private static final String TABLE_EVENTS = "events";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DATE = "date";

    /**
     * DB constructor
     * @param context
     * @param name
     * @param factory
     * @param version
     */
    public MyDBHandler(Context context, String name,  SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_EVENTS_TABLE = "CREATE TABLE " + TABLE_EVENTS + "( " + COLUMN_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_NAME + " TEXT," + COLUMN_DATE + " DATE" + ")";

        db.execSQL(CREATE_EVENTS_TABLE);
    }

    /**
     * For future DB upgrades to data model - no use as of 1.0.0-beta
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        onCreate(db);
    }

    /**
     * Adds event
     * @param event
     */
    public void addEvent(EventDate event) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, event.getEventName());
        values.put(COLUMN_DATE, event.getDate().getTime());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_EVENTS, null, values);
        db.close();
    }

    /**
     *
     * @return - a Cursor object will all events in DB
     */
    public Cursor getAllEventDates() {
        return getReadableDatabase().query(TABLE_EVENTS,
                new String[] { "_id", "name", "date"},
                null, null, null, null, null);
    }

    /**
     *
     * @param id - long (_id from DB)
     * @return - date of event based on ID
     */
    public Cursor getDateByID(long id){
        String i = String.valueOf(id);
        return getReadableDatabase().query(TABLE_EVENTS, new String[]{"date"},
                "_id = "+i,null,null,null,null);
    }

    /**
     *
     * @param id long (_id from DB)
     * @return - name of event based on ID
     */
    public Cursor getNameByID(long id){
        String i = String.valueOf(id);
        return getReadableDatabase().query(TABLE_EVENTS, new String[]{"name"},
                "_id = "+i,null,null,null,null);
    }

    /**
     * Deletes event
     * @param id long (_id from DB)
     */
    public void deleteByID(long id){
        String i = String.valueOf(id);
        getWritableDatabase().delete(TABLE_EVENTS, "_id = "+i, null);
    }
}
