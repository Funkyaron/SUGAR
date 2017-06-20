package com.example.peter.sugar;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;

public class DisableProfileReceiver extends BroadcastReceiver {

    private static final int ID = 43;

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(MainActivity.LOG_TAG, "DisableProfileReceiver: onReceive()");

        // Temporarily coded to test the alarm functionality

        Object[] categories = intent.getCategories().toArray();
        String name = (String) categories[0];

        Profile prof = null;
        BlockList blockList = null;
        try {
            prof = Profile.readProfileFromXmlFile(name, context);
        } catch (Exception e) {
            Log.e(MainActivity.LOG_TAG, "Error reading Profile: " + e.toString());
        }
        try {
            blockList = new BlockList(context);
        } catch(Exception e) {
            Log.e(MainActivity.LOG_TAG, "Error reading BlockList: " + e.toString());
        }

        try {
            TimeManager.setNextDisable(context, prof);
            blockList.removeProfile(context, prof);
        } catch(Exception e) {
            Log.e(MainActivity.LOG_TAG, e.toString());
        }

        Notification.Builder builder = new Notification.Builder(context);

        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(name)
                .setContentText(context.getString(R.string.calls_forbidden))
                .setWhen(System.currentTimeMillis());

        Notification noti = builder.build();

        NotificationManager notiMgr = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        notiMgr.notify(ID, noti);
    }
}
