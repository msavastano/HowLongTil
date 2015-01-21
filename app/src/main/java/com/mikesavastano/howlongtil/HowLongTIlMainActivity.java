package com.mikesavastano.howlongtil;

import android.app.Fragment;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Calendar;

public class HowLongTIlMainActivity extends ActionBarActivity {

    DatePicker datePicker;
    Calendar today;
    Calendar eventDate;
    Intent detailsView;
    Toast toast;

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
            toast = Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.thats_today_text),Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }else if(HowLongDetail.isEventBeforeToday(today,eventDate)){
            toast = Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.thats_before_today_text),Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
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
        Intent i;
        int id = item.getItemId();
        switch (id) {
            // Starts a saved list activity
            case R.id.saved_list:
                i = new Intent(this, SavedEvents.class);
                startActivity(i);
                return true;
            case R.id.holiday_list:
                i = new Intent(this, HolidayList.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This class makes the ad request and loads the ad.
     */
    public static class AdFragment extends Fragment {

        private AdView mAdView;

        public AdFragment() {
        }

        @Override
        public void onActivityCreated(Bundle bundle) {
            super.onActivityCreated(bundle);

            // Gets the ad view defined in layout/ad_fragment.xml with ad unit ID set in
            // values/strings.xml.
            mAdView = (AdView) getView().findViewById(R.id.adView);

            // Create an ad request. Check logcat output for the hashed device ID to
            // get test ads on a physical device. e.g.
            // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .build();

            // Start loading the ad in the background.
            mAdView.loadAd(adRequest);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_ad, container, false);
        }

        /** Called when leaving the activity */
        @Override
        public void onPause() {
            if (mAdView != null) {
                mAdView.pause();
            }
            super.onPause();
        }

        /** Called when returning to the activity */
        @Override
        public void onResume() {
            super.onResume();
            if (mAdView != null) {
                mAdView.resume();
            }
        }

        /** Called before the activity is destroyed */
        @Override
        public void onDestroy() {
            if (mAdView != null) {
                mAdView.destroy();
            }
            super.onDestroy();
        }

    }
}
