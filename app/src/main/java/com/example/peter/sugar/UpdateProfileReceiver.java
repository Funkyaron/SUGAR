package com.example.peter.sugar;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Funkyaron on 04.04.2017.
 */

class UpdateProfileReceiver extends BroadcastReceiver {

    private static final int ID = 42;

    @Override
    public void onReceive(Context context, Intent intent) {


        /* Temporarily coded to test the alarm functionality */

        Bundle extras = intent.getExtras();
        String name = extras.getString(AlarmInitializer.PROFILE_NAME_KEY);
        Boolean active = extras.getBoolean(AlarmInitializer.ACTIVE_KEY);

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
