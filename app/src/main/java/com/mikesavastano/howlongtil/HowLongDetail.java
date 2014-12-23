package com.mikesavastano.howlongtil;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import android.os.Handler;
import android.widget.Toast;


public class HowLongDetail extends ActionBarActivity {
    final String TAG = "com.mikesavastano.howlongdetail.saveevent";
    TextView name;
    TextView event;
    TextView month;
    TextView day;
    TextView hour;
    TextView minute;
    TextView second;
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    DateFormat usDateFormat = new SimpleDateFormat("MM/dd/yyyy");
    //Boolean whileOn;
    //Runnable r;
    Thread thdA;
    Button ShareButton;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {

            Bundle bundle = msg.getData();
            Calendar today = (Calendar) bundle.getSerializable("curr_date");
            Calendar eventDate = (Calendar) bundle.getSerializable("event_date");

            month.setText(monthsBetween(today, eventDate) + " Month" + addEss(monthsBetween(today, eventDate)));
            second.setText(remainderSeconds(today, eventDate) + " Second" + addEss(remainderSeconds(today, eventDate)));
            day.setText(remainderDays(today, eventDate) + " Day" + addEss(remainderDays(today, eventDate)));
            hour.setText(remainderHours(today, eventDate) + " Hour" + addEss(remainderHours(today, eventDate)));
            minute.setText(remainderMinutes(today, eventDate) + " Minute" + addEss(remainderMinutes(today, eventDate)));
            event.setText(usDateFormat.format(eventDate.getTime()));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_how_long_detail);
        View rootView = getWindow().getDecorView().getRootView();

        name = (TextView) findViewById(R.id.nameEditText);
        event = (TextView) findViewById(R.id.eventDate);
        month = (TextView) findViewById(R.id.textViewMonth);
        day = (TextView) findViewById(R.id.textViewDay);
        hour = (TextView) findViewById(R.id.textViewHour);
        minute = (TextView) findViewById(R.id.textViewMinute);
        second = (TextView) findViewById(R.id.textViewSecond);

        Runnable r = new Runnable(){
            @Override
            public void run() {
                //whileOn = true;
                try {
                    while (true) {
                        Calendar today = Calendar.getInstance();
                        Calendar eventDate = (Calendar) getIntent().getSerializableExtra("event");
                       // DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                        Message msg = handler.obtainMessage();
                        Bundle bndl = new Bundle();
                        bndl.putSerializable("curr_date", today);
                        bndl.putSerializable("event_date", eventDate);
                        msg.setData(bndl);
                        handler.sendMessage(msg);
                        Thread.sleep(1000l);
                    }
                }catch(InterruptedException e){
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };
        thdA = new Thread(r);
        thdA.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //thdA.interrupt();
        //whileOn = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //if(thdA.isInterrupted()) {
            //thdA.start();
        //}
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //whileOn = true;
        //thdA.start();
    }


    public void saveEvent (View view) {
        EditText EditNAME = (EditText) findViewById(R.id.nameEditText);
        String ename = EditNAME.getText().toString();
        if(ename.isEmpty()){
            Toast.makeText(getApplicationContext(), "Please fill out 'Name'", Toast.LENGTH_LONG).show();
        }else {

            MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
            Date d = new Date();
            Log.i(TAG, event.getText().toString());
            String event_date = event.getText().toString();
            try {
                d = usDateFormat.parse(event_date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String eventName;
            if (name.getText() == null) {
                eventName = d.toString();
            } else {
                eventName = name.getText().toString();
            }
            EventDate event = new EventDate(eventName, d);
            dbHandler.addEvent(event);
            Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
        }
    }

    public static String addEss(int num){
        if (num == 1)
            return "";
        else
            return "s";
    }

    public static boolean isEventBeforeToday(Calendar today, Calendar event){
        return daysBetween(today, event) < 0;
    }

    public static boolean isEventToday(Calendar today, Calendar event){
        return hoursBetween(today, event) < 0 && daysBetween(today, event) == 0;
    }

    public static long millisBetween(Calendar today, Calendar event){
        return event.getTimeInMillis() - today.getTimeInMillis();
    }

    public static long daysBetween(Calendar today , Calendar event) {
        long e = event.getTimeInMillis();
        long t = today.getTimeInMillis();
        return TimeUnit.MILLISECONDS.toDays(e - t);
    }

    public static long hoursBetween(Calendar today , Calendar event) {
        long e = event.getTimeInMillis();
        long t = today.getTimeInMillis();
        return TimeUnit.MILLISECONDS.toHours(e - t);
    }

    public static long secondsBetween(Calendar today , Calendar event) {
        long e = event.getTimeInMillis();
        long t = today.getTimeInMillis();
        return TimeUnit.MILLISECONDS.toSeconds(e - t);
    }

    public static long minutesBetween(Calendar today , Calendar event) {
        long e = event.getTimeInMillis();
        long t = today.getTimeInMillis();
        return TimeUnit.MILLISECONDS.toMinutes(e - t);
    }
    public static int monthsBetween(Calendar today , Calendar event){
        return (int) (daysBetween(today, event) / (365.0/12.0));
    }

    public static int remainderDays(Calendar today , Calendar event){
        return (int) (daysBetween(today, event) - (monthsBetween(today, event) * (365.0/12.0)));
    }

    public static int remainderHours(Calendar today , Calendar event){
        return (int) (hoursBetween(today, event) - daysBetween(today, event)*24);
    }

    public static int remainderMinutes(Calendar today , Calendar event){
        return (int) (minutesBetween(today, event) - hoursBetween(today, event)*60);
    }

    public static int remainderSeconds(Calendar today , Calendar event){
        return (int) (secondsBetween(today, event) - minutesBetween(today, event)*60);
    }

}
