package com.example.peter.sugar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by Funkyaron on 04.04.2017. <p/>
 * Helper class that provides static methods to handle time-dependent enabling
 * or disabling of SUGAR-Profiles.
 */

public class ProfileUpdateUtil {

    /**
     * Determines when the given profile should be enabled the next time and sets the
     * correspondent "alarm".
     *
     * @param context Needed for Intent, AlarmManager etc.
     * @param profile The profile for which the action should be performed
     */
    public static void setNextEnable(Context context, Profile profile) {

        Log.d(MainActivity.LOG_TAG, "PUU: setNextEnable()");

        String name = profile.getName();
        boolean[] days = profile.getDays();
        int[] start = profile.getStart();

        // First check if any day of week should apply
        Log.d(MainActivity.LOG_TAG, "Checking days");
        boolean shouldApply = false;
        for (boolean day : days)
        {
            if (day)
                shouldApply = true;
        }
        if (!shouldApply)
            return;

        Log.d(MainActivity.LOG_TAG, "Setting Alarm");

        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, EnableProfileReceiver.class);
        intent.addCategory(name);

        PendingIntent pending = PendingIntent.getBroadcast(context, 0,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        long currentTime = cal.getTimeInMillis();
        int currentDay = cal.get(Calendar.DAY_OF_WEEK);

        cal.set(Calendar.HOUR_OF_DAY, start[0]);
        cal.set(Calendar.MINUTE, start[1]);
        long targetTime = cal.getTimeInMillis();

        if (days[toIndex(currentDay)] && currentTime < targetTime)
        {
            // Don't change target time
        }
        else
        {
            int daysToAdd = 0;
            int i = toIndex(currentDay);
            int j = 0;
            while (j <= 6)
            {
                daysToAdd++;
                i = (i + 1) % 7;
                j++;
                if (days[i])
                    break;
            }

            cal.add(Calendar.DAY_OF_MONTH, daysToAdd);
            targetTime = cal.getTimeInMillis();
        }

        alarmMgr.setExact(AlarmManager.RTC_WAKEUP, targetTime, pending);
    }


    /**
     * Determines when the given profile sould be disabled the next time and sets the
     * correspondent "alarm".
     *
     * @param context Needed for Intent, AlarmManager etc.
     * @param profile The profile for which the action should be performed
     */
    public static void setNextDisable(Context context, Profile profile) {

        Log.d(MainActivity.LOG_TAG, "PUU: setNextDisable()");

        String name = profile.getName();
        boolean[] days = profile.getDays();
        int[] end = profile.getEnd();

        // First check if any day of week should apply
        boolean shouldApply = false;
        for (boolean day : days)
        {
            if (day)
                shouldApply = true;
        }
        if (!shouldApply)
            return;

        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, DisableProfileReceiver.class);
        intent.addCategory(name);

        PendingIntent pending = PendingIntent.getBroadcast(context, 0,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        long currentTime = cal.getTimeInMillis();
        int currentDay = cal.get(Calendar.DAY_OF_WEEK);

        cal.set(Calendar.HOUR_OF_DAY, end[0]);
        cal.set(Calendar.MINUTE, end[1]);
        long targetTime = cal.getTimeInMillis();

        if (profile.getDays()[toIndex(currentDay)] && currentTime < targetTime)
        {
            // Don't change target time
        }
        else
        {
            int daysToAdd = 0;
            int i = toIndex(currentDay);
            int j = 0;
            while (j <= 6)
            {
                daysToAdd++;
                i = (i + 1) % 7;
                j++;
                if (days[i])
                    break;
            }

            cal.add(Calendar.DAY_OF_MONTH, daysToAdd);
            targetTime = cal.getTimeInMillis();
        }

        alarmMgr.setExact(AlarmManager.RTC_WAKEUP, targetTime, pending);
    }



    /**
     * Checks if the given profile should currently be enabled or not and updates its status.
     *
     * @param context Needed for Intent
     * @param profile The profile which should be initialized
     */
    public static void updateProfileStatus(Context context, Profile profile) {

        Log.d(MainActivity.LOG_TAG, "PUU: UpdateProfileStatus");

        String name = profile.getName();
        boolean[] days = profile.getDays();
        int[] startTime = profile.getStart();
        int[] endTime = profile.getEnd();

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        long currentTime = cal.getTimeInMillis();
        int currentDay = cal.get(Calendar.DAY_OF_WEEK);

        cal.set(Calendar.HOUR_OF_DAY, startTime[0]);
        cal.set(Calendar.MINUTE, startTime[1]);
        long startTimeInMillis = cal.getTimeInMillis();

        cal.set(Calendar.HOUR_OF_DAY, endTime[0]);
        cal.set(Calendar.MINUTE, endTime[1]);
        long endTimeInMillis = cal.getTimeInMillis();

        Log.d(MainActivity.LOG_TAG, "currentDay Index = " + toIndex(currentDay)
            + ", " + days[toIndex(currentDay)]);
        Log.d(MainActivity.LOG_TAG, "startHour = " + startTime[0]);
        Log.d(MainActivity.LOG_TAG, "startMinute = " + startTime[1]);
        Log.d(MainActivity.LOG_TAG, "endHour = " + endTime[0]);
        Log.d(MainActivity.LOG_TAG, "endMinute = " + endTime[1]);

        if (days[toIndex(currentDay)] == false
                || startTimeInMillis >= currentTime
                || endTimeInMillis <= currentTime)
        {
            Log.d(MainActivity.LOG_TAG, "Profile should be disabled");
            Intent intent = new Intent(context, DisableProfileReceiver.class);
            Log.d(MainActivity.LOG_TAG, "Next line: intent.addCategory...");
            intent.addCategory(name);
            Log.d(MainActivity.LOG_TAG, "Next line: sendBroadcast...");
            context.sendBroadcast(intent);
        }
        else
        {
            Log.d(MainActivity.LOG_TAG, "Profile should be enabled");
            Intent intent = new Intent(context, EnableProfileReceiver.class);
            intent.addCategory(name);
            context.sendBroadcast(intent);
        }
    }



    /**
     * Converts constant field values from java.util.Calendar to array-index,
     * beginning from monday. <p/><p/>
     * Calendar.MONDAY = 2 -> 0 <p/>
     * Calendar.TUESDAY = 3 -> 1 <p/>
     * ... <p/>
     * Calendar.SATURDAY = 7 -> 5 <p/>
     * Calendar.SUNDAY = 1 -> 6 <p/>
     *
     * @param calendarDay Constant field value from java.util.Calendar
     * @return Index that can be used for an array, beginning from monday
     */
    public static int toIndex(int calendarDay) {
        return (calendarDay + 5) % 7;
    }
}
