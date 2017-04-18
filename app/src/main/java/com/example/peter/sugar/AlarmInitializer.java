package com.example.peter.sugar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Funkyaron on 04.04.2017.
 */

public class AlarmInitializer {

    protected static final String EXTRA_ACTIVE = "is active";
    protected static final String EXTRA_PROFILE_NAME = "profile name";

    private AlarmManager mAlarmManager;
    private Intent startIntent;
    private Intent endIntent;
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
        /*
        Log.d(MainActivity.LOG_TAG, "AlarmInitializer: updateAlarms()");

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
                    prof.getProfileStartPoint().getTimeInMillis(), startPendingIntent);

            mBundle.putBoolean(ACTIVE_KEY, false);
            alarmIntent.putExtras(mBundle);

            endPendingIntent = PendingIntent.getBroadcast(cont, 0,
                    alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            mAlarmManager.setExact(AlarmManager.RTC_WAKEUP,
                    prof.getProfileEndPoint().getTimeInMillis(), endPendingIntent);
        }
        */
    }

    /**
     * Prototype method to test Alarm functionality.
     *
     * @param context
     */
    protected void updateAlarms(Context context) {

        if(mAlarmManager != null) {
            mAlarmManager.cancel(startPendingIntent);
            mAlarmManager.cancel(endPendingIntent);
        } else {
            mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        }

        Calendar startCal = Calendar.getInstance();
        startCal.setTimeInMillis(System.currentTimeMillis());
        startCal.set(Calendar.HOUR_OF_DAY, 11);
        startCal.set(Calendar.MINUTE, 51);

        startIntent = new Intent(context, UpdateProfileReceiver.class);
        Bundle mBundle = new Bundle();
        mBundle.putBoolean(EXTRA_ACTIVE, true);
        mBundle.putString(EXTRA_PROFILE_NAME, "Arbeit");
        startIntent.putExtras(mBundle);
        //Adding different categories, otherwise the two Intents are considered equal
        //and will not both be executed.
        startIntent.addCategory("start");

        startPendingIntent = PendingIntent.getBroadcast(context, 0,
                startIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        mAlarmManager.setExact(AlarmManager.RTC_WAKEUP,
                startCal.getTimeInMillis(), startPendingIntent);

        Calendar endCal = Calendar.getInstance();
        endCal.setTimeInMillis(System.currentTimeMillis());
        endCal.set(Calendar.HOUR_OF_DAY, 11);
        endCal.set(Calendar.MINUTE, 53);

        endIntent = new Intent(context, UpdateProfileReceiver.class);
        mBundle.putBoolean(EXTRA_ACTIVE, false);
        endIntent.putExtras(mBundle);
        endIntent.addCategory("end");

        endPendingIntent = PendingIntent.getBroadcast(context, 0,
                endIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        mAlarmManager.setExact(AlarmManager.RTC_WAKEUP,
                endCal.getTimeInMillis(), endPendingIntent);
    }
}
