package com.example.peter.sugar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.Date;

/**
 * Created by Funkyaron on 04.04.2017.
 */

class AlarmInitializer {

    protected static final String ACTIVE_KEY = "is active";
    protected static final String PROFILE_NAME_KEY = "profile name";

    private AlarmManager mAlarmManager;
    private Intent alarmIntent;
    private PendingIntent startPendingIntent;
    private PendingIntent endPendingIntent;

    /**
     * Cancels every existing alarm associated with this app and sets new alarms for
     * every profile. This method should be called initially when the app starts the first
     * time and then every time a profile is added, removed or modified.
     *
     * @param cont The context in which the alarms should be set.
     * @param profs An array that contains every profile the app is dealing with.
     */
    protected void updateAlarms(Context cont, Profile[] profs)
    {
        if (mAlarmManager != null) {
            mAlarmManager.cancel(startPendingIntent);
            mAlarmManager.cancel(endPendingIntent);
        } else {
            mAlarmManager = (AlarmManager) cont.getSystemService(Context.ALARM_SERVICE);
        }

        for (Profile prof : profs)
        {
            alarmIntent = new Intent(cont, UpdateProfileReceiver.class);
            Bundle mBundle = new Bundle();
            mBundle.putBoolean(ACTIVE_KEY, true);
            mBundle.putString(PROFILE_NAME_KEY, prof.getProfileName());
            alarmIntent.putExtras(mBundle);

            startPendingIntent = PendingIntent.getBroadcast(cont, 0,
                    alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            mAlarmManager.setExact(AlarmManager.RTC_WAKEUP,
                    prof.getProfileStartPoint().getTime(), startPendingIntent);

            mBundle.putBoolean(ACTIVE_KEY, false);
            alarmIntent.putExtras(mBundle);

            endPendingIntent = PendingIntent.getBroadcast(cont, 0,
                    alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            mAlarmManager.setExact(AlarmManager.RTC_WAKEUP,
                    prof.getProfileEndPoint().getTime(), endPendingIntent);
        }
    }
}
