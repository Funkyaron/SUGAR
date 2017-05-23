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

public class TimeManager {

    /**
     * Determines when the given profile should be enabled the next time and sets the
     * correspondent "alarm".
     *
     * @param context Needed for Intent, AlarmManager etc.
     * @param profile The profile for which the action should be performed
     */
    public static void setNextEnable(Context context, Profile profile) {

        Log.d(MainActivity.LOG_TAG, "TimeManager: setNextEnable()");

        String name = profile.getName();
        boolean[] days = profile.getDays();
        TimeObject[] start = profile.getStart();



        // First check if any day of week should apply
        boolean shouldApply = false;
        for (boolean day : days)
        {
            if (day)
                shouldApply = true;
        }
        if (!shouldApply)
            return;



        // Prepare the alarm
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, EnableProfileReceiver.class);
        intent.addCategory(name);

        PendingIntent pending = PendingIntent.getBroadcast(context, 0,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);



        // Figure out when to execute the alarm

        // First we get the current time
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        long currentTime = cal.getTimeInMillis();
        int currentDay = cal.get(Calendar.DAY_OF_WEEK);

        // Just in case, we check the start time from "today".
        cal.set(Calendar.HOUR_OF_DAY, start[toIndex(currentDay)].getHour());
        cal.set(Calendar.MINUTE, start[toIndex(currentDay)].getMinute());
        long targetTime = cal.getTimeInMillis();
        if (days[toIndex(currentDay)] && (currentTime < targetTime))
        {
            // Don't change target time.
        }
        else
        {

            // Now we have to match the next day to apply with the corresponding start time,
            // so we have to find out the appropriate Array index.
            // First we find out how many days we have to add to the current day.

            int daysToAdd = 0;
            int i = toIndex(currentDay);
            int j = 0;
            while (j <= 6) {
                daysToAdd++;
                i = (i + 1) % 7;
                j++;
                if (days[i])
                    break;
            }

            // Now we determine the target time by adding the days to the Calendar instance
            // and extracting the right start time from the TimeObject Array.

            cal.add(Calendar.DAY_OF_MONTH, daysToAdd);

            int targetIndex = (toIndex(currentDay) + daysToAdd) % 7;
            cal.set(Calendar.HOUR_OF_DAY, start[targetIndex].getHour());
            cal.set(Calendar.MINUTE, start[targetIndex].getMinute());

            targetTime = cal.getTimeInMillis();
        }

        // Finally we actually set the alarm.
        alarmMgr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, targetTime, pending);
    }


    /**
     * Determines when the given profile sould be disabled the next time and sets the
     * correspondent "alarm".
     *
     * @param context Needed for Intent, AlarmManager etc.
     * @param profile The profile for which the action should be performed
     */
    public static void setNextDisable(Context context, Profile profile) {

        Log.d(MainActivity.LOG_TAG, "TimeManager: setNextDisable()");

        // Thins method is very similar to setNextEnable(). For comments see there.

        String name = profile.getName();
        boolean[] days = profile.getDays();
        TimeObject[] end = profile.getEnd();



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


        cal.set(Calendar.HOUR_OF_DAY, end[toIndex(currentDay)].getHour());
        cal.set(Calendar.MINUTE, end[toIndex(currentDay)].getMinute());
        long targetTime = cal.getTimeInMillis();
        if (profile.getDays()[toIndex(currentDay)] && (currentTime < targetTime))
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

            int targetIndex = (toIndex(currentDay) + daysToAdd) % 7;
            cal.set(Calendar.HOUR_OF_DAY, end[targetIndex].getHour());
            cal.set(Calendar.MINUTE, end[targetIndex].getMinute());

            targetTime = cal.getTimeInMillis();
        }

        alarmMgr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, targetTime, pending);
    }



    /**
     * Checks if the given profile should currently be enabled or not and updates its status.
     *
     * @param context Needed for Intent
     * @param profile The profile which should be initialized
     */
    public static void updateProfileStatus(Context context, Profile profile) {

        Log.d(MainActivity.LOG_TAG, "TimeManager: UpdateProfileStatus");

        String name = profile.getName();
        boolean[] days = profile.getDays();
        TimeObject[] startTimes = profile.getStart();
        TimeObject[] endTimes = profile.getEnd();



        // At first, get the current time.
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        long currentTime = cal.getTimeInMillis();
        int currentDay = cal.get(Calendar.DAY_OF_WEEK);



        // Now set the start and end time for the current day.
        // Later on we will check if we need it at all.
        cal.set(Calendar.HOUR_OF_DAY, startTimes[toIndex(currentDay)].getHour());
        cal.set(Calendar.MINUTE, startTimes[toIndex(currentDay)].getMinute());
        long startTimeInMillis = cal.getTimeInMillis();

        cal.set(Calendar.HOUR_OF_DAY, endTimes[toIndex(currentDay)].getHour());
        cal.set(Calendar.MINUTE, endTimes[toIndex(currentDay)].getMinute());
        long endTimeInMillis = cal.getTimeInMillis();



        // Now we check for the current day and time.
        if (days[toIndex(currentDay)] == false
                || currentTime < startTimeInMillis
                || currentTime > endTimeInMillis)
        {
            Intent intent = new Intent(context, DisableProfileReceiver.class);
            intent.addCategory(name);
            context.sendBroadcast(intent);
        }
        else
        {
            Intent intent = new Intent(context, EnableProfileReceiver.class);
            intent.addCategory(name);
            context.sendBroadcast(intent);
        }
    }



    private long getTargetTime(boolean[] days, TimeObject[] time)
    {
        //TODO: Fill in this method.
        return 0;
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
