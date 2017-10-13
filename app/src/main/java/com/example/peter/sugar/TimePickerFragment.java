package com.example.peter.sugar;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by Peter on 14.07.2017.
 */

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private int index;
    private boolean isStart;

    @Override
    public Dialog onCreateDialog(Bundle savedInstance)
    {
        Bundle args = getArguments();

        index = args.getInt(MainActivity.EXTRA_INDEX);
        Log.d(MainActivity.LOG_TAG, "index is: " + index);
        isStart = args.getBoolean(MainActivity.EXTRA_IS_START);
        Log.d(MainActivity.LOG_TAG, "isStart: " + isStart);

        TimeObject time;

        ActivityContainingProfile parentActitivty = (ActivityContainingProfile) getActivity();
        Profile prof = parentActitivty.getProfile();

        if(isStart) {
            time = prof.getStart()[index];
        } else {
            time = prof.getEnd()[index];
        }
        return new TimePickerDialog(getActivity(),this,
                time.getHour(),time.getMinute(), DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute)
    {
        ActivityContainingProfile parentActivity = (ActivityContainingProfile) getActivity();
        Profile prof = parentActivity.getProfile();

        boolean isValid = true;
        TimeObject startTime;
        TimeObject endTime;
        if(isStart) {
            startTime = new TimeObject(hourOfDay, minute);
            endTime = prof.getEnd()[index];
        } else {
            startTime = prof.getStart()[index];
            endTime = new TimeObject(hourOfDay, minute);
        }
        isValid = startTime.earlierThan(endTime);
        if(!isValid) {
            return;
        }

        TimeObject modifiedTime;
        if(isStart) {
            TimeObject[] modified = prof.getStart();
            modified[index] = new TimeObject(hourOfDay, minute);
            modifiedTime = modified[index];
            prof.setStart(modified);
        } else {
            TimeObject[] modified = prof.getEnd();
            modified[index] = new TimeObject(hourOfDay, minute);
            modifiedTime = modified[index];
            prof.setEnd(modified);
        }

        TableRow row;
        if(isStart)
            row = (TableRow) parentActivity.findViewById(R.id.start_time_row);
        else
            row = (TableRow) parentActivity.findViewById(R.id.end_time_row);

        TextView modifiedView = (TextView) row.getChildAt(index);
        modifiedView.setText(modifiedTime.toString());

        if(prof.isActive()) {
            TimeManager mgr = new TimeManager(parentActivity);
            mgr.initProfile(prof);
        }
    }
}
