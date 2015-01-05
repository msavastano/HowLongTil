package com.mikesavastano.howlongtil;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.SimpleCursorAdapter;
import android.os.Bundle;
import java.util.Calendar;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class SavedEvents extends ActionBarActivity {

    final String TAG = "com.mikesavastano.howlongtil.SavedEvents";

    MyDBHandler dbHelper;
    Toast toast;

    /**
     * Call database and create list of saved events
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_events);

        //Get name from events for display
        dbHelper = new MyDBHandler(this, null, null, 1);
        Cursor ed = dbHelper.getAllEventDates();
        String[] from = {"name"};
        int[] to = {R.id.customListItem};

        ListAdapter savedListAdapter=new SimpleCursorAdapter(this,R.layout.custom_simple_item, ed, from, to);
        ListView savedList = (ListView) findViewById(R.id.listViewSavedEvents);
        savedList.setAdapter(savedListAdapter);

        //On click go to event detail page
        savedList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cur = dbHelper.getDateByID(id);
                Cursor curName = dbHelper.getNameByID(id);
                cur.moveToFirst();
                curName.moveToFirst();
                String evdate = "";
                String evname = "";
                while (!cur.isAfterLast()) {
                    evdate = cur.getString(0);
                    cur.moveToNext();
                }
                while (!curName.isAfterLast()) {
                    evname = curName.getString(0);
                    curName.moveToNext();
                }

                Calendar t = Calendar.getInstance();

                Long unixtime = Long.parseLong(evdate);
                Calendar unix2Cal = Calendar.getInstance();
                unix2Cal.setTimeInMillis(unixtime);

                //if event has passed or is 'today'. do not go to event detail page
                if (HowLongDetail.isEventToday(t, unix2Cal)) {
                    toast = toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.thats_today_text), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                } else if (HowLongDetail.isEventBeforeToday(t, unix2Cal)) {
                    toast = toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.thats_before_today_text), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                } else {

                    Intent detailsView = new Intent(getApplicationContext(), HowLongDetail.class);
                    detailsView.putExtra("event", unix2Cal);
                    detailsView.putExtra("name", evname);
                    startActivity(detailsView);
                }
            }
        });

        savedList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                dbHelper.deleteByID(id);
                Intent i = new Intent(getApplicationContext(), SavedEvents.class);
                startActivity(i);
                finish();
                toast = toast.makeText(getApplicationContext(), R.string.deleted_text, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
                return true;
            }
        });
    }

    //Make sure list is current on resume (backpress)
    @Override
    protected void onResume() {
        super.onResume();
        dbHelper = new MyDBHandler(this, null, null, 1);
        Cursor ed = dbHelper.getAllEventDates();
        String[] from = {"name"};
        int[] to = {R.id.customListItem};

        ListAdapter savedListAdapter=new SimpleCursorAdapter(this,R.layout.custom_simple_item, ed, from, to);
        ListView savedList = (ListView) findViewById(R.id.listViewSavedEvents);
        savedList.setAdapter(savedListAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_saved_events, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        int id = item.getItemId();
        switch (id) {
            // Starts a saved list activity
            case R.id.holiday_list:
                i = new Intent(this, HolidayList.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
