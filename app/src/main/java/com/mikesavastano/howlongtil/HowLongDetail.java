package com.mikesavastano.howlongtil;

import android.content.Context;
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

    //TextView until;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_how_long_detail);
        View rootView = getWindow().getDecorView().getRootView();

        rootView.setBackgroundColor(Color.LTGRAY);

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
        //curr.setText(dateFormat.format(today.getTime()));

        ShareButton = (Button) findViewById(R.id.shareButton);

        ShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                captureScreen();

                Toast.makeText(getApplicationContext(), "Shared", Toast.LENGTH_LONG).show();

            }
        });
    }

    private void captureScreen() {
        String fd; // = "";
        String fn; // = "";
        View v = getWindow().getDecorView().getRootView();  //getRootView();
        v.setDrawingCacheEnabled(true);
        Bitmap bmp = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false);
        if(isExternalStorageWritable()) {
            File p = new File(Environment.getExternalStorageDirectory() + "/Pictures/HowLongTil");
            if(!p.exists()){
                p.mkdir();
            }

            Log.i(TAG, "External drive writable");
            Log.i(TAG, Environment.getExternalStorageDirectory().toString());
            fd = Environment.getExternalStorageDirectory().toString() + "/Pictures/HowLongTil";
            fn = "SCREEN" + System.currentTimeMillis() + ".png";
        }else{
            Log.i(TAG, "External drive NOT writable");
            fd = new File(getApplicationContext().getFilesDir() + "/Pictures/HowLongTil/", "SCREEN" + System.currentTimeMillis() + ".png").toString();
            fn = "";
        }
        //String intfd = Environment.get

        try {
            FileOutputStream fos = new FileOutputStream(new File(fd, fn));
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        openScreenshot(fd, fn);
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    private void openScreenshot(String fd, String fn){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Log.i(TAG, "file://" + fd + "/" + fn);
        intent.setDataAndType(Uri.parse("file://" + fd + "/" + fn), "image/*");
        startActivity(intent);
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
        MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
        Date d = new Date();
        Log.i(TAG, event.getText().toString());
        String event_date = event.getText().toString();
        try {
            d = usDateFormat.parse(event_date);
        }catch (ParseException e){
            e.printStackTrace();
        }
        String eventName;
        if (name.getText() == null){
            eventName = d.toString();
        }else{
            eventName = name.getText().toString();
        }
        EventDate event = new EventDate(eventName, d);
        dbHandler.addEvent(event);
        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
    }

    /*public Bitmap screenShot(View view) {
        //File mkdir = new File();
        //mkdir.mkdirs();

        File mPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES+"/HowLongTil");
        View bigView = findViewById(R.id.relLayoutEventScreen);
        Bitmap bitmap = Bitmap.createBitmap(bigView.getWidth(), bigView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        bigView.draw(canvas);

        OutputStream fout;
        File imageFile = new File(mPath, "pic.jpg");

        try {
            fout = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fout);
            fout.flush();
            fout.close();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Toast.makeText(getApplicationContext(), "Shared", Toast.LENGTH_LONG).show();
        Log.i(TAG, imageFile.toString());
        return bitmap;
    }*/
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_how_long_detail, menu);
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
*/
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
