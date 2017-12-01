package com.example.peter.sugar;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TimePicker;

/**
 * Created by SHK on 14.11.17.
 */
public class CreateActivityTimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener
{
    private Profile underlyingProfile;
    private int selectedWeekDay;
    private boolean isStart;

    @Override
    public Dialog onCreateDialog(Bundle savedInstances)
    {
        Bundle arguments = getArguments();
        selectedWeekDay = arguments.getInt("selectedWeekDay");
        Log.d(MainActivity.LOG_TAG,"Your currently selected weekday is : " + selectedWeekDay);
        isStart = arguments.getBoolean("isStart");
        Log.d(MainActivity.LOG_TAG,"Your segment is start : " + isStart);

        TimeObject currentlySelectedTime = new TimeObject(0,0);
        CreateProfileActivity underlyingActivity = (CreateProfileActivity) getActivity();
        underlyingProfile = underlyingActivity.getActivityProfile();

        if(isStart)
        {
            currentlySelectedTime = underlyingProfile.getStart()[selectedWeekDay];
            Log.d(MainActivity.LOG_TAG,"Old start time is : " + currentlySelectedTime.toString());
        } else if (!isStart) {
            currentlySelectedTime = underlyingProfile.getEnd()[selectedWeekDay];
            Log.d(MainActivity.LOG_TAG,"Old end time is : " + currentlySelectedTime.toString());
        }
        final TimePickerDialog toResult = new TimePickerDialog(getActivity(),this,currentlySelectedTime.getHour(),currentlySelectedTime.getMinute(),true);
        toResult.setOnShowListener(new DialogInterface.OnShowListener() {
            public void onShow(DialogInterface inter)
            {
                toResult.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                toResult.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
            }
        });
        return toResult;
    }

    @Override
    public void onTimeSet(TimePicker view,int hourOfDay,int minuteOfDay)
    {
        ActivityCreatingProfile underlyingActivity = (CreateProfileActivity) getActivity();
        underlyingProfile = underlyingActivity.getProfile();
        TimeObject startTime = underlyingProfile.getStart()[selectedWeekDay];
        TimeObject endTime = underlyingProfile.getEnd()[selectedWeekDay];
        TimeObject modifiedTime = new TimeObject(hourOfDay,minuteOfDay);

        if(isStart)
        {
            startTime = modifiedTime;
            endTime = underlyingProfile.getEnd()[selectedWeekDay];
        } else if (!isStart) {
            startTime = underlyingProfile.getStart()[selectedWeekDay];
            endTime = modifiedTime;
        }

        if(isStart)
        {
            underlyingProfile.setStartForDay(selectedWeekDay,startTime);
            Log.d(MainActivity.LOG_TAG,"You have updated the start time of " + selectedWeekDay + " to " + underlyingProfile.getStart()[selectedWeekDay].toString());
        } else if(!isStart) {
            underlyingProfile.setEndForDay(selectedWeekDay,endTime);
            Log.d(MainActivity.LOG_TAG,"You have updated the end time of " + selectedWeekDay + " to " + underlyingProfile.getEnd()[selectedWeekDay].toString());
        }

        if(isStart)
        {
            Button editStartTimeButton = (Button) getActivity().findViewById(R.id.start_time_button);
            editStartTimeButton.setText("Ab: \n" + startTime.toString());
        } else if(!isStart) {
            Button editEndTimeButton = (Button) getActivity().findViewById(R.id.end_time_button);
            editEndTimeButton.setText("Bis: \n" + endTime.toString());
        }
    }
}
