package com.example.peter.sugar;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Funkyaron on 04.04.2017.
 */

public class EnableProfileReceiver extends BroadcastReceiver {

    private static final int ID = 42;

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(MainActivity.LOG_TAG, "EnableProfileReceiver: onReceive()");

        XMLProfileParser parser = new XMLProfileParser();

        String[] categories = (String[]) intent.getCategories().toArray();
        String profileName = categories[0];
        //XMLProfileParser.Profile prof = parser.readProfile(...);

        //ProfileUpdateUtil.enable(prof);
        //ProfileUpdateUtil.setNextEnable(context, prof);
    }
}
