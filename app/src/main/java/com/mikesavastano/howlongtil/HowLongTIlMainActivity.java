package com.mikesavastano.howlongtil;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class HowLongTIlMainActivity extends ActionBarActivity {

    DatePicker datePicker;
    Calendar today;
    Calendar eventDate;
    Button howLongButton;

    Intent detailsView;
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_long_til_main);
     }

    public void howLongTil(View v){
        datePicker = (DatePicker) findViewById(R.id.datePicker);

        today = Calendar.getInstance();
        //System.out.println(dateFormat.format(today.getTime())); //2014/08/06 16:00:22

        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        eventDate = Calendar.getInstance();
        eventDate.set(year, month, day);
        eventDate.set(Calendar.HOUR_OF_DAY, 0);
        eventDate.set(Calendar.MINUTE, 0);
        eventDate.set(Calendar.SECOND, 0);
        eventDate.set(Calendar.MILLISECOND, 0);
        if(HowLongDetail.isEventToday(today, eventDate)){
            Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.thats_today_text),Toast.LENGTH_LONG).show();
        }else if(HowLongDetail.isEventBeforeToday(today,eventDate)){
            Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.thats_before_today_text),Toast.LENGTH_LONG).show();
        }else{

            detailsView = new Intent(this, HowLongDetail.class);
            detailsView.putExtra("event", eventDate);
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            //case R.id.action_settings:
              //  return  true;
            case R.id.saved_list:
                Intent i = new Intent(this, SavedEvents.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
