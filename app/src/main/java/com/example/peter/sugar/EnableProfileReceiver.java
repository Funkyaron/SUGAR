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

        /* Temporarily coded to test the alarm functionality */

        Bundle extras = intent.getExtras();
        String name = extras.getString(ProfileUpdateUtil.EXTRA_PROFILE_NAME);
        Boolean active = extras.getBoolean(ProfileUpdateUtil.EXTRA_ACTIVE);

        Notification.Builder builder = new Notification.Builder(context);

        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(name)
                .setContentText(active.toString())
                .setWhen(System.currentTimeMillis());

        Notification noti = builder.build();

        NotificationManager notiMgr = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        notiMgr.notify(ID, noti);
    }
}
