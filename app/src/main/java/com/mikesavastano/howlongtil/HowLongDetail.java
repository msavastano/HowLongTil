package com.mikesavastano.howlongtil;


import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
    DateFormat usDateFormat = new SimpleDateFormat("MM/dd/yyyy");
    Thread thdA;
    Button saveButton;

    Handler handler = new Handler() {
        /**
         * Author MSavastano
         * Date 12-24-14
         *
         * Handles the message sent from thread
         * that updates screen 1x per second
         *
         * @param msg
         */
        public void handleMessage(Message msg) {

            Bundle bundle = msg.getData();
            Calendar today = (Calendar) bundle.getSerializable("curr_date");
            Calendar eventDate = (Calendar) bundle.getSerializable("event_date");
            String eventNameFromBundle = (String) bundle.getSerializable("name");

            if(!eventNameFromBundle.isEmpty()) {
                name.setText(eventNameFromBundle);
                saveButton.setVisibility(View.INVISIBLE);
            }
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

        name = (TextView) findViewById(R.id.nameEditText);
        event = (TextView) findViewById(R.id.eventDate);
        month = (TextView) findViewById(R.id.textViewMonth);
        day = (TextView) findViewById(R.id.textViewDay);
        hour = (TextView) findViewById(R.id.textViewHour);
        minute = (TextView) findViewById(R.id.textViewMinute);
        second = (TextView) findViewById(R.id.textViewSecond);
        saveButton = (Button) findViewById(R.id.saveButton);

        Runnable r = new Runnable(){
            @Override
            public void run() {
                try {
                    while (true) {
                        Calendar today = Calendar.getInstance();
                        Calendar eventDate = (Calendar) getIntent().getSerializableExtra("event");
                        String eventName = (String) getIntent().getSerializableExtra("name");

                        Message msg = handler.obtainMessage();
                        Bundle bndl = new Bundle();
                        bndl.putSerializable("curr_date", today);
                        bndl.putSerializable("event_date", eventDate);
                        bndl.putSerializable("name", eventName);
                        msg.setData(bndl);
                        handler.sendMessage(msg);
                        Thread.sleep(1000l);
                    }
                }catch(InterruptedException e){
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
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }


    public void saveEvent (View view) {
        EditText EditNAME = (EditText) findViewById(R.id.nameEditText);
        String ename = EditNAME.getText().toString();
        if(ename.isEmpty()){
            Toast.makeText(getApplicationContext(), R.string.fill_name_text, Toast.LENGTH_LONG).show();
        }else {

            MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
            Date d = new Date();

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
            Toast.makeText(getApplicationContext(), R.string.saved_text, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Adds 's' or empty string depending on number
     * @param num int
     * @return String 's' or ''
     */
    public static String addEss(int num){
        if (num == 1)
            return "";
        else
            return "s";
    }

    /**
     * Date comparison
     * @param today Calendar
     * @param event Calendar
     * @return boolean
     */
    public static boolean isEventBeforeToday(Calendar today, Calendar event){
        return daysBetween(today, event) < 0;
    }

    /**
     * Date comparison
     * @param today Calendar
     * @param event Calendar
     * @return boolean
     */
    public static boolean isEventToday(Calendar today, Calendar event){
        return hoursBetween(today, event) < 0 && daysBetween(today, event) == 0;
    }

    public static long millisBetween(Calendar today, Calendar event){
        return event.getTimeInMillis() - today.getTimeInMillis();
    }

    //Below functions compare dates and return dates in milliseconds

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

    //Below functions compare dates and return dates in time values

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
