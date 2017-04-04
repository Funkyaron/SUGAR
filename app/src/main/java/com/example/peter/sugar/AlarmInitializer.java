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
    private PendingIntent alarmPendingIntent;

    protected void initAlarms(Context cont, Profile[] profs)
    {
        for (Profile prof : profs) {
            mAlarmManager = (AlarmManager) cont.getSystemService(Context.ALARM_SERVICE);
            alarmIntent = new Intent(cont, UpdateProfileReceiver.class);
            Bundle startBundle = new Bundle();
            startBundle.putBoolean(ACTIVE_KEY, true);
            startBundle.putString(PROFILE_NAME_KEY, prof.getProfileName());
            alarmIntent.putExtras(startBundle);

            alarmPendingIntent = PendingIntent.getBroadcast(cont, 0,
                    alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            mAlarmManager.setExact(AlarmManager.RTC_WAKEUP,
                    prof.getProfileStartPoint().getTime(), alarmPendingIntent);
        }

    }

    protected void updateAlarm(Context cont, Profile prof) {

    }

    protected void addAlarm(Context cont, Profile prof) {

    }

    protected void removeAlarm(Context cont, Profile prof) {

    }
}
