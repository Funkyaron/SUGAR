package com.example.peter.sugar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by shk on 13.10.17.
 *
 * This Activity template can be used to pass a Profile to another class that needs to
 * modify it, for example from EditProfileActivity to TimePickerFragment.
 *
 * It automatically handles Profile parsingwhen entering the Activity.
 * That means you must always pass the EXTRA_PROFILE_NAME when starting an Activity
 * subclassing this one.
 */

public class ActivityContainingProfile extends AppCompatActivity {
    private Profile prof;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String profileName = getIntent().getStringExtra(MainActivity.EXTRA_PROFILE_NAME);

        try {
            prof = Profile.readProfileFromXmlFile(profileName, this);
        } catch(Exception e) {
            Log.e(MainActivity.LOG_TAG, e.toString());
            Toast.makeText(this, R.string.error, Toast.LENGTH_LONG).show();
            finish();
        }

        Log.d(MainActivity.LOG_TAG, "ActivityContainigProfile: onCreate()");
        Log.d(MainActivity.LOG_TAG, "Profile: " + prof.toString());
    }

    public Profile getProfile() {
        return prof;
    }
}
