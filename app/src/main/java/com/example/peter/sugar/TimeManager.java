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
 * Helper class that provides methods to handle time-dependent enabling
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

    private AlarmManager mAlarmManager;
    private Context context;

    public TimeManager(Context context) {
        mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.context = context;
    }

    /**
     * Determines when the given profile should be enabled the next time and sets the
     * correspondent alarm. When a profile is enabled, calls from the associated
     * contacts are allowed.
     *
     * @param profile The profile for which the action should be performed
     */
    public void setNextEnable(Profile profile) {

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
        Intent intent = new Intent(context, EnableProfileReceiver.class);
        intent.addCategory(name);

        PendingIntent pending = PendingIntent.getBroadcast(context, 0,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);

        long targetTime = getTargetTime(days, start);

        mAlarmManager.setExact(AlarmManager.RTC_WAKEUP, targetTime, pending);
    }


    /**
     * Determines when the given profile sould be disabled the next time and sets the
     * correspondent alarm. When a profile is disabled, calls from the associated
     * contacts are blocked.
     *
     * @param profile The profile for which the action should be performed
     */
    public void setNextDisable(Profile profile) {

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

        Intent intent = new Intent(context, DisableProfileReceiver.class);
        intent.addCategory(name);

        PendingIntent pending = PendingIntent.getBroadcast(context, 0,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);

        long targetTime = getTargetTime(days, end);

        mAlarmManager.setExact(AlarmManager.RTC_WAKEUP, targetTime, pending);
    }



    /**
     * Checks if the given profile should currently be enabled or not and updates its status.
     * It also sets the next enabling and disabling alarms properly.
     *
     */
    public void initProfile(Profile prof) {
        Log.d(MainActivity.LOG_TAG, "TimeManager: initProfile");

        // At first, get the current time.
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        long currentTime = cal.getTimeInMillis();
        int currentDay = cal.get(Calendar.DAY_OF_WEEK);

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
                || endTimeInMillis < currentTime)
        {
            prof.setAllowed(false);
            try {
                prof.saveProfile(context);
            } catch(Exception e) {
                Log.e(MainActivity.LOG_TAG, e.toString());
            }

            setNextDisable(prof);
            setNextEnable(prof);

            // Inform the user about what happened.
            Notification.Builder builder = new Notification.Builder(context);
            builder.setSmallIcon(R.mipmap.sugar)
                    .setContentTitle(prof.getName())
                    .setContentText(context.getString(R.string.calls_forbidden))
                    .setWhen(System.currentTimeMillis());

            Notification noti = builder.build();

            NotificationManager notiMgr = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);
            notiMgr.notify(23, noti);
        } else {
            prof.setAllowed(true);
            try {
                prof.saveProfile(context);
            } catch(Exception e) {
                Log.e(MainActivity.LOG_TAG, e.toString());
            }

            setNextEnable(prof);
            setNextDisable(prof);

            Notification.Builder builder = new Notification.Builder(context);
            builder.setSmallIcon(R.mipmap.sugar)
                    .setContentTitle(prof.getName())
                    .setContentText(context.getString(R.string.calls_allowed))
                    .setWhen(System.currentTimeMillis());

            Notification noti = builder.build();

            NotificationManager notiMgr = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);
            notiMgr.notify(24, noti);
        }
    }

    public void initProfiles() {

        Log.d(MainActivity.LOG_TAG, "TimeManager: initProfiles");

        Profile[] allProfiles = Profile.readAllProfiles(context);

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
                    || endTimeInMillis < currentTime)
            {
                disabledProfiles.add(prof);
            } else {
                enabledProfiles.add(prof);
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

            setNextDisable(disProf);
            setNextEnable(disProf);

            // Inform the user about what happened.
            Notification.Builder builder = new Notification.Builder(context);
            builder.setSmallIcon(R.mipmap.sugar)
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

            setNextEnable(enProf);
            setNextDisable(enProf);

            Notification.Builder builder = new Notification.Builder(context);
            builder.setSmallIcon(R.mipmap.sugar)
                    .setContentTitle(enProf.getName())
                    .setContentText(context.getString(R.string.calls_allowed))
                    .setWhen(System.currentTimeMillis());

            Notification noti = builder.build();

            NotificationManager notiMgr = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);
            notiMgr.notify(id++, noti);
        }
    }



    private long getTargetTime(boolean[] days, TimeObject[] times)
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
     * beginning from monday.
     *
     * @param calendarDay Constant field value from java.util.Calendar
     * @return Index that can be used for an array, beginning from monday
     */
    private int toIndex(int calendarDay) {
        switch(calendarDay) {
            case Calendar.MONDAY:
                return 0;
            case Calendar.TUESDAY:
                return 1;
            case Calendar.WEDNESDAY:
                return 2;
            case Calendar.THURSDAY:
                return 3;
            case Calendar.FRIDAY:
                return 4;
            case Calendar.SATURDAY:
                return 5;
            case Calendar.SUNDAY:
                return 6;
            default:
                return 0; // Never used.
        }
    }
}
