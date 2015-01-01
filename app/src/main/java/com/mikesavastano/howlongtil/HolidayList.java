package com.mikesavastano.howlongtil;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HolidayList extends ActionBarActivity {

    ArrayAdapter<String> holListNames;

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
                Toast.makeText(getApplicationContext(), d.toString(), Toast.LENGTH_LONG).show();
                HolidayCalendar hc = new HolidayCalendar();
                Calendar xx = hc.calcHoliday(hc.mapMaker(d), 2015);

                Intent detailsView = new Intent(getApplicationContext(), HowLongDetail.class);
                detailsView.putExtra("event", xx);
                detailsView.putExtra("name", d.get(4));
                startActivity(detailsView);
            }
        });


    }
}
