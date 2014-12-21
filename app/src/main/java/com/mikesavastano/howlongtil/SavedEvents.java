package com.mikesavastano.howlongtil;

import android.app.ListActivity;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;


public class SavedEvents extends ListActivity {
    final String TAG = "com.mikesavastano.howlongtil.SavedEvents";
    private MyDBHandler dbHelper;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, dbHelper.getAllEventDates().toString());
        //setContentView(R.layout.activity_saved_events);
        //String query = "Select * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_PRODUCTNAME + " =  \"" + productname + "\"";
        dbHelper = new MyDBHandler(this, null, null, 1);
        //database = dbHelper.getWritableDatabase();
        Cursor ed = dbHelper.getAllEventDates();
        //new String[] { "_id", "name", "date"},
        String[] from = {"name"};
        int[] to = {android.R.id.text1};
        ListView savedList = (ListView) findViewById(R.id.listViewSavedEvents);

        ListAdapter savedListAdapter=new SimpleCursorAdapter(this,android.R.layout.simple_list_item_1,
                ed, from, to);

        savedList.setAdapter(savedListAdapter);

        /*List<Integer> id = new ArrayList<>();
        for(EventDate i : ed){
            id.add(i.getID());
        }*/

        //setListAdapter(new ArrayAdapter<>(this,
                //android.R.layout.simple_list_item_checked, ed));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_saved_events, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
