package com.example.peter.sugar;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeoutException;

/**
 * Created by Funkyaron on 04.04.2017. <p/>
 * Helper class that provides static methods to handle time-dependent enabling
 * or disabling of SUGAR-Profiles.
 *
 * The general idea is to jump from one alarm to another because we can set only one alarm
 * at a time. Every time an alarm is performed, the next alarm is set.
 *
 * Note that every Intent associated with an alarm has to be unique, otherwise one alarm
 * will replace another. Extras are ignored! So we add the profile name as a category
 * and use different Receivers for enabling and disabling.
 */

public class TimeManager {

    private static AlarmManager mAlarmManager;

    private static AlarmManager getAlarmManager(Context context) {
        if(mAlarmManager == null) {
            mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        }
        return mAlarmManager;
    }

    /**
     * Determines when the given profile should be enabled the next time and sets the
     * correspondent alarm. When a profile is enabled, calls from the associated
     * contacts are allowed.
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
        AlarmManager alarmMgr = getAlarmManager(context);

        Intent intent = new Intent(context, EnableProfileReceiver.class);
        intent.addCategory(name);

        PendingIntent pending = PendingIntent.getBroadcast(context, 0,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);

        long targetTime = getTargetTime(days, start);

        alarmMgr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, targetTime, pending);
    }


    /**
     * Determines when the given profile sould be disabled the next time and sets the
     * correspondent alarm. When a profile is disabled, calls from the associated
     * contacts are blocked.
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



        AlarmManager alarmMgr = getAlarmManager(context);

        Intent intent = new Intent(context, DisableProfileReceiver.class);
        intent.addCategory(name);

        PendingIntent pending = PendingIntent.getBroadcast(context, 0,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);

        long targetTime = getTargetTime(days, end);

        alarmMgr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, targetTime, pending);
    }



    /**
     * Checks if the given profile should currently be enabled or not and updates its status.
     * It also sets the next enabling and disabling alarms properly.
     *
     * @param context Needed for Intent
     */
    public static void initProfiles(Context context) {

        Log.d(MainActivity.LOG_TAG, "TimeManager: initProfiles");

        /* Interesting thing: The BlockList contains every number that is allowed.
         * And a number that is currently associated with two or more enabled
         * profiles appears twice - or respectively more often - on the BlockList.
         * We want to make every number that is not affected by any profile appear
         * once.
         * So the plan is:
         *  1. Reset the BlockList so that every number appears once.
         *  2. Remove every number associated with a disabled profile.
         *     -> Some numbers may be "removed" more than once. That
         *        doesn't have any further effect.
         *  3. Handle enabled profiles:
         *   a) Create a new list and add every number associated with an
         *      enabled profile.
         *   b) Remove every number that's already on the BlockList.
         *   c) Add the result to the BlockList.
         * Then save the BlockList and set the next alarms for every profile.
         */

        File[] allFiles = context.getFilesDir().listFiles();
        Profile[] allProfiles = Profile.readAllProfiles(allFiles, context);

        // At first, get the current time.
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        long currentTime = cal.getTimeInMillis();
        int currentDay = cal.get(Calendar.DAY_OF_WEEK);

        // Now we collect every enabled and disabled profiles.
        ArrayList<Profile> enabledProfiles = new ArrayList<>(0);
        ArrayList<Profile> disabledProfiles = new ArrayList<>(0);

        for(Profile prof : allProfiles) {
            TimeObject[] startTimes = prof.getStart();
            TimeObject[] endTimes = prof.getEnd();
            boolean[] days = prof.getDays();

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
                    || endTimeInMillis < currentTime) {
                try {
                    disabledProfiles.add(prof);
                } catch(NullPointerException e) {
                    Log.e(MainActivity.LOG_TAG, e.toString());
                }
            } else {
                try {
                    enabledProfiles.add(prof);
                } catch(NullPointerException e) {
                    Log.e(MainActivity.LOG_TAG, e.toString());
                }
            }
        }



        int id = 1;

        for(Profile disProf : disabledProfiles) {
            disProf.setAllowed(false);
            try {
                disProf.saveProfile(context);
            } catch(Exception e) {
                Log.e(MainActivity.LOG_TAG, e.toString());
            }

            setNextDisable(context, disProf);
            setNextEnable(context, disProf);

            // Inform the user about what happened.
            Notification.Builder builder = new Notification.Builder(context);
            builder.setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(disProf.getName())
                    .setContentText(context.getString(R.string.calls_forbidden))
                    .setWhen(System.currentTimeMillis());

            Notification noti = builder.build();

            NotificationManager notiMgr = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);
            notiMgr.notify(id++, noti);
        }

        for(Profile enProf : enabledProfiles) {
            enProf.setAllowed(true);
            try {
                enProf.saveProfile(context);
            } catch(Exception e) {
                Log.e(MainActivity.LOG_TAG, e.toString());
            }

            setNextEnable(context, enProf);
            setNextDisable(context, enProf);

            Notification.Builder builder = new Notification.Builder(context);
            builder.setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(enProf.getName())
                    .setContentText(context.getString(R.string.calls_allowed))
                    .setWhen(System.currentTimeMillis());

            Notification noti = builder.build();

            NotificationManager notiMgr = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);
            notiMgr.notify(id++, noti);
        }
    }



    private static long getTargetTime(boolean[] days, TimeObject[] times)
    {
        // Figure out when to execute the alarm

        // First we get the current time
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        long currentTime = cal.getTimeInMillis();
        int currentDay = cal.get(Calendar.DAY_OF_WEEK);

        // Just in case, we check the start time from "today".
        cal.set(Calendar.HOUR_OF_DAY, times[toIndex(currentDay)].getHour());
        cal.set(Calendar.MINUTE, times[toIndex(currentDay)].getMinute());
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
            cal.set(Calendar.HOUR_OF_DAY, times[targetIndex].getHour());
            cal.set(Calendar.MINUTE, times[targetIndex].getMinute());

            targetTime = cal.getTimeInMillis();
        }

        return targetTime;
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
    private static int toIndex(int calendarDay) {
        return (calendarDay + 5) % 7;
    }
}
