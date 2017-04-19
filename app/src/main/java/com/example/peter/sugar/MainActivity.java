package com.example.peter.sugar;

import java.util.Calendar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = "SUGAR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Testing the alarm initialization */

        Log.d(LOG_TAG, "MainActivity: preparing alarm init.");

        Calendar startCal = Calendar.getInstance();
        startCal.setTimeInMillis(System.currentTimeMillis());
        startCal.set(Calendar.HOUR_OF_DAY, 12);
        startCal.set(Calendar.MINUTE, 25);

        Calendar endCal = Calendar.getInstance();
        endCal.setTimeInMillis(System.currentTimeMillis());
        endCal.set(Calendar.HOUR_OF_DAY, 12);
        endCal.set(Calendar.MINUTE, 30);

        /*
        Profile testProf = new Profile(42, "Arbeit", startCal, endCal, "Moritz Müller", false);
        Profile[] testProfs = new Profile[] {testProf};

        AlarmInitializer ai = new AlarmInitializer();
        ai.updateAlarms(this, testProfs); */

    }
}
