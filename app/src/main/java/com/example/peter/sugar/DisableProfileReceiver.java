package com.example.peter.sugar;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class DisableProfileReceiver extends BroadcastReceiver {

    private static final int ID = 42;

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(MainActivity.LOG_TAG, "DisableProfileReceiver: onReceive()");

        /* Temporarily coded to test the alarm functionality */

        String name = "";

        Object[] categories = intent.getCategories().toArray();
        name = (String) categories[0];

        Profile prof = null;
        TestXmlWriter tester = new TestXmlWriter();

        try {
            prof = tester.readTestProfile(context);
            TimeManager.setNextDisable(context, prof);
        } catch (Exception e) {
            Log.e(MainActivity.LOG_TAG, e.toString());
        }

        Notification.Builder builder = new Notification.Builder(context);

        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(name)
                .setContentText("Disabled")
                .setWhen(System.currentTimeMillis());

        Notification noti = builder.build();

        NotificationManager notiMgr = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        notiMgr.notify(ID, noti);

    }
}
