package com.example.peter.sugar;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by shk on 13.10.17.
 *
 * This Activity subclass is intended to pass a Profile to another class that needs to
 * modify it, for example from EditProfileActivity to TimePickerFragment.
 */

public class ActivityContainingProfile extends AppCompatActivity {
    protected Profile prof;

    public void setProfile(Profile prof) {
        this.prof = prof;
    }

    public Profile getProfile() {
        return prof;
    }
}
