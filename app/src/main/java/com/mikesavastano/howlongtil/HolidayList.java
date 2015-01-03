package com.mikesavastano.howlongtil;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HolidayList extends ActionBarActivity {

    ArrayAdapter<String> holListNames;
    TextView year;
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holiday_list);

        HolidayCalendar obs = new HolidayCalendar();
        ArrayList<List<String>> hols = new ArrayList<>();
        for(List<String> holiday : obs.holidays){
            hols.add(holiday);
        }

        ArrayList<String> names = new ArrayList<>();
        for(List<String> nm : hols){
            names.add(nm.get(4));
        }

        holListNames = new ArrayAdapter(this, R.layout.custom_simple_item, names);
        final ArrayAdapter<List<String>> hoListObs = new ArrayAdapter(this, R.layout.custom_simple_item, hols);
        ListView holList = (ListView) findViewById(R.id.listHolidayEvents);
        holList.setAdapter(holListNames);

        holList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getApplicationContext(), String.valueOf(id), Toast.LENGTH_LONG).show();
                List<String> d = hoListObs.getItem(position);
                //Toast.makeText(getApplicationContext(), d.toString(), Toast.LENGTH_LONG).show();
                HolidayCalendar hc = new HolidayCalendar();
                Calendar t = Calendar.getInstance();
                year = (TextView) findViewById(R.id.yearEditText);


                //check for older than today dates, empty
                if (year.getText().toString().isEmpty()) {
                    toast = toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.empty_string), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }else {

                    int yearText = Integer.parseInt(year.getText().toString());
                    Calendar xx = hc.calcHoliday(hc.mapMaker(d), yearText);
                    if (HowLongDetail.isEventToday(t, xx)) {
                        toast = toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.thats_today_text), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                    } else if (HowLongDetail.isEventBeforeToday(t, xx)) {
                        toast = toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.thats_before_today_text), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }else {
                        Intent detailsView = new Intent(getApplicationContext(), HowLongDetail.class);
                        detailsView.putExtra("event", xx);
                        detailsView.putExtra("name", d.get(4));
                        startActivity(detailsView);
                    }
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_holiday_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        int id = item.getItemId();
        switch (id) {
            // Starts a saved list activity
            case R.id.saved_list:
                i = new Intent(this, SavedEvents.class);
                startActivity(i);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
