package com.example.peter.sugar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.Date;

/**
 * Created by Peter on 04.04.2017.
 */

class AlarmInitializer {

    protected static final String ACTIVE_KEY = "is active";
    protected static final String PROFILE_NAME_KEY = "profile name";

    private AlarmManager mAlarmManager;
    private Intent alarmIntent;
    private PendingIntent startPendingIntent;
    private PendingIntent endPendingIntent;

    protected void initAlarms(Context cont, Profile[] profs)
    {
        for (Profile prof : profs)
        {
            mAlarmManager = (AlarmManager) cont.getSystemService(Context.ALARM_SERVICE);
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

    protected void updateAlarm(Context cont, Profile prof) {

    }

    protected void addAlarm(Context cont, Profile prof) {

    }

    protected void removeAlarm(Context cont, Profile prof) {

    }
}
