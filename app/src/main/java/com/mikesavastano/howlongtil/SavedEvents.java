package com.mikesavastano.howlongtil;

import android.app.Activity;
import android.app.ListActivity;
import android.content.ClipData;
import android.content.Intent;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;


public class SavedEvents extends Activity {
    final String TAG = "com.mikesavastano.howlongtil.SavedEvents";
    private MyDBHandler dbHelper;

    //private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.i(TAG, "list " + dbHelper.getAllEventDates().toString());
        //System.out.println(dbHelper.getAllEventDates().toString());
        setContentView(R.layout.activity_saved_events);
        //String query = "Select * FROM " + TABLE_PRODUCTS + " WHERE " + COLUMN_PRODUCTNAME + " =  \"" + productname + "\"";
        dbHelper = new MyDBHandler(this, null, null, 1);
        //database = dbHelper.getWritableDatabase();
        Cursor ed = dbHelper.getAllEventDates();

        String[] from = {"name"};
        int[] to = {R.id.customListItem};

        ListAdapter savedListAdapter=new SimpleCursorAdapter(this,R.layout.custom_simple_item,
                ed, from, to);
        ListView savedList = (ListView) findViewById(R.id.listViewSavedEvents);

        savedList.setAdapter(savedListAdapter);

        savedList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cur = dbHelper.getDateByID(id);
                cur.moveToFirst();
                String evdate = "";
                while(!cur.isAfterLast()){
                  evdate = cur.getString(0);
                  cur.moveToNext();
                }
                Long unixtime = Long.parseLong(evdate);
                Calendar unix2Cal = Calendar.getInstance();
                unix2Cal.setTimeInMillis(unixtime);

                Intent detailsView = new Intent(getApplicationContext(), HowLongDetail.class);
                detailsView.putExtra("event", unix2Cal);
                startActivity(detailsView);
            }
        });

        savedList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                dbHelper.deleteByID(id);
                Intent i = new Intent(getApplicationContext(), SavedEvents.class);
                startActivity(i);
                Toast.makeText(getApplicationContext(), R.string.deleted_text, Toast.LENGTH_LONG).show();
                return true;
            }
        });


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
