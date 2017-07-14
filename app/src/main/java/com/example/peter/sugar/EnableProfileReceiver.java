package com.example.peter.sugar;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Funkyaron on 04.04.2017.
 */

public class EnableProfileReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(MainActivity.LOG_TAG, "EnableProfileReceiver: onReceive()");

        Object[] categories = intent.getCategories().toArray();
        String name = (String) categories[0];

        Profile prof = null;
        try {
            prof = Profile.readProfileFromXmlFile(name, context);
        } catch (Exception e) {
            Log.e(MainActivity.LOG_TAG, "Error reading Profile: " + e.toString());
        }

        if(prof == null || !(prof.isActive()))
            return;

        prof.setAllowed(true);
        TimeManager mgr = new TimeManager(context);
        mgr.setNextEnable(prof);
        try {
            prof.saveProfile(context);
        } catch(Exception e) {
            Log.e(MainActivity.LOG_TAG, e.toString());
        }

        Notification.Builder builder = new Notification.Builder(context);

        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(name)
                .setContentText(context.getString(R.string.calls_allowed))
                .setWhen(System.currentTimeMillis());

        Notification noti = builder.build();

        NotificationManager notiMgr = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        notiMgr.notify(name.hashCode(), noti);
    }
}
