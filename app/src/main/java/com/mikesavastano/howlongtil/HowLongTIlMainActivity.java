package com.mikesavastano.howlongtil;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;
import java.util.Calendar;

public class HowLongTIlMainActivity extends ActionBarActivity {

    DatePicker datePicker;
    Calendar today;
    Calendar eventDate;
    Intent detailsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_long_til_main);
     }

    /**
     * Pass data to event detail activity
     * @param v View
     */
    public void howLongTil(View v){
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        today = Calendar.getInstance();

        //Gets values from datepicker view
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        //Sets event to midnight
        eventDate = Calendar.getInstance();
        eventDate.set(year, month, day);
        eventDate.set(Calendar.HOUR_OF_DAY, 0);
        eventDate.set(Calendar.MINUTE, 0);
        eventDate.set(Calendar.SECOND, 0);
        eventDate.set(Calendar.MILLISECOND, 0);

        //Starts activity, but not if date n picker is today or earlier
        if(HowLongDetail.isEventToday(today, eventDate)){
            Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.thats_today_text),Toast.LENGTH_LONG).show();
        }else if(HowLongDetail.isEventBeforeToday(today,eventDate)){
            Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.thats_before_today_text),Toast.LENGTH_LONG).show();
        }else{
            detailsView = new Intent(this, HowLongDetail.class);
            detailsView.putExtra("event", eventDate);
            detailsView.putExtra("name", "");
            startActivity(detailsView);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_how_long_til_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            // Starts a saved list activity
            case R.id.saved_list:
                Intent i = new Intent(this, SavedEvents.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
