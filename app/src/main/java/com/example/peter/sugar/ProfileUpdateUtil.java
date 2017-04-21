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
 * Created by Funkyaron on 04.04.2017. <p/>
 * Helper class that provides static methods to handle time-dependent enabling
 * or disabling of SUGAR-Profiles.
 */

public class ProfileUpdateUtil {

    protected static final String EXTRA_ACTIVE = "is active";

    /**
     * String extra that specifies the affected profile
     */
    protected static final String EXTRA_PROFILE_NAME = "profile name";

    private AlarmManager mAlarmManager;
    private Intent startIntent;
    private Intent endIntent;
    private PendingIntent startPendingIntent;
    private PendingIntent endPendingIntent;


    /**
     * Prototype method to test alarm functionality.
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

    /**
     * Checks if the profile should currently be enabled or not and updates its status.
     *
     * @param prof The profile which should be initialized
     */
    public static void applyCurrentProfileStatus(XMLProfileParser.Profile prof) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        long currentTime = cal.getTimeInMillis();
        int currentDay = cal.get(Calendar.DAY_OF_WEEK);

        // cal.set(Calendar.HOUR_OF_DAY, prof...);
        // cal.set(Calendar.MINUTE, prof...);
        long startTime = cal.getTimeInMillis();

        // cal.set(Calendar.HOUR_OF_DAY, prof...);
        // cal.set(Calendar.MINUTE, prof...);
        long endTime = cal.getTimeInMillis();

        boolean[] days = prof.days;

        if (days[toIndex(currentDay)] == false) {
            disable(prof);
        } else if (startTime <= currentTime && endTime >= currentTime) {
            enable(prof);
        } else {
            disable(prof);
        }
    }

    /**
     * Enables the specified profile. If enabled, the profile's blocklist applies on
     * incoming calls.
     *
     * @param prof The profile which should be enabled
     */
    public static void enable(XMLProfileParser.Profile prof) {
        //TODO: enable profile
    }

    /**
     * Disables the specified profile. If disabled, the profile's blocklist doesn't
     * affect incoming calls.
     *
     * @param prof The profile which should be disabled
     */
    public static void disable(XMLProfileParser.Profile prof) {
        //TODO: Disable profile
    }

    /**
     * Converts constant field values from java.util.Calendar to array-index,
     * beginning from monday. <p/><p/>
     * Calendar.MONDAY = 2 -> 0 <p/>
     * Calendar.TUESDAY = 3 -> 1 <p/>
     * ... <p/>
     * Calendar.SATURDAY = 7 -> 5 <p/>
     * Calendar.SUNDAY = 1 -> 6 <p/>
     * @param calendarDay Constant field value from java.util.Calendar
     * @return Index that can be used for an array, beginning from monday
     */
    public static int toIndex(int calendarDay) {
        return (calendarDay + 5) % 7;
    }
}
