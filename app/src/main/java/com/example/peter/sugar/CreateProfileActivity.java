package com.example.peter.sugar;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class CreateProfileActivity extends ActivityCreatingProfile
{
    private int selectedWeekDay;
    private EditText profileNameInput;
    private TextView[] weekdayViews;
    private CheckBox[] weekdayCheckboxes;
    private Button editStartTimeButton;
    private Button editEndTimeButton;
    private Button finishButton;

    private Profile prof;
    private String name;
    private boolean[] days;
    private TimeObject[] startTimes;
    private TimeObject[] endTimes;
    private boolean active;
    private boolean allowed;
    private int mode;
    private ArrayList<String> phoneNumbers;
    private ArrayList<String> contactNames;

    @Override
    protected void onCreate(Bundle savedInstances)
    {
        super.onCreate(savedInstances);
        setContentView(R.layout.activity_create_profile);

        selectedWeekDay = 0;
        prof = getProfile();
        name = prof.getName();
        days = prof.getDays();
        startTimes = prof.getStart();
        endTimes = prof.getEnd();
        active = prof.isActive();
        allowed = prof.isAllowed();
        mode = prof.getMode();
        phoneNumbers = prof.getPhoneNumbers();
        contactNames = prof.getContactNames();

        profileNameInput = (EditText) findViewById(R.id.edit_profile_name);
        weekdayViews = new TextView[7];
        weekdayCheckboxes = new CheckBox[7];
        TableRow viewRow = (TableRow) findViewById(R.id.daysRow);
        TableRow checkBoxRow = (TableRow) findViewById(R.id.daysCheckboxes);

        for( int currentWeekDay = 0; currentWeekDay < weekdayViews.length; currentWeekDay++ )
        {
            Log.d(MainActivity.LOG_TAG,"Parsing 'weekdayViews' ...");
            weekdayViews[currentWeekDay] = (TextView) viewRow.getChildAt(currentWeekDay);
            Log.d(MainActivity.LOG_TAG,"Text of current weekdayView : " + weekdayViews[currentWeekDay].getText().toString());
            weekdayCheckboxes[currentWeekDay] = (CheckBox) checkBoxRow.getChildAt(currentWeekDay);
            Log.d(MainActivity.LOG_TAG,"Is the current checkbox checked : " + weekdayCheckboxes[currentWeekDay].isChecked());
        }

        /* Set OnClickListener for each TextView inside the array 'weekdayViews' */
        for( int currentWeekDay = 0; currentWeekDay < weekdayViews.length; currentWeekDay++ )
        {
            final int selectDay = currentWeekDay;
            weekdayViews[currentWeekDay].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    weekdayViews[selectedWeekDay].setBackgroundResource(R.drawable.weekday_deactivated);
                    selectedWeekDay = selectDay;
                    for( int currentWeekDay = 0; currentWeekDay < weekdayViews.length; currentWeekDay++ )
                    {
                        if( currentWeekDay == selectedWeekDay && !(weekdayCheckboxes[selectedWeekDay].isChecked()))
                        {
                            weekdayViews[selectedWeekDay].setBackgroundResource(R.drawable.weekday_activated);
                            editStartTimeButton.setVisibility(View.GONE);
                            editEndTimeButton.setVisibility(View.GONE);
                        } else if( ( selectedWeekDay == selectDay ) && ( weekdayCheckboxes[currentWeekDay].isChecked() ) ) {
                            weekdayViews[selectedWeekDay].setBackgroundResource(R.drawable.weekday_activated);
                            editStartTimeButton.setText(CreateProfileActivity.this.getString(R.string.from_plus_time,startTimes[selectDay]));
                            editEndTimeButton.setText(CreateProfileActivity.this.getString(R.string.to_plus_time,endTimes[selectDay]));
                            editStartTimeButton.setVisibility(View.VISIBLE);
                            editEndTimeButton.setVisibility(View.VISIBLE);
                        } else if ( currentWeekDay != selectDay ) {
                            weekdayViews[currentWeekDay].setBackgroundResource(R.drawable.weekday_deactivated);
                        }
                    }
                }
            });
        }

        for( int currentWeekDay = 0; currentWeekDay < weekdayCheckboxes.length; currentWeekDay++ )
        {
            final int selectDay = currentWeekDay;
            weekdayCheckboxes[currentWeekDay].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton selectedCheckbox,boolean isChecked)
                {
                    if(isChecked)
                    {
                        prof.setDayActiveForWeekDay(selectDay);
                        if( selectDay == selectedWeekDay )
                        {
                            editStartTimeButton.setVisibility(View.VISIBLE);
                            editEndTimeButton.setVisibility(View.VISIBLE);
                            editStartTimeButton.setText(CreateProfileActivity.this.getString(R.string.from_plus_time,startTimes[selectDay]));
                            editEndTimeButton.setText(CreateProfileActivity.this.getString(R.string.to_plus_time,endTimes[selectDay]));
                        }
                    } else{
                        prof.setDayInactiveForWeekDay(selectDay);
                        if( selectDay == selectedWeekDay ) {
                            editStartTimeButton.setVisibility(View.INVISIBLE);
                            editEndTimeButton.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            });
        }

        editStartTimeButton = (Button) findViewById(R.id.start_time_button);
        editStartTimeButton.setVisibility(View.INVISIBLE);
        editStartTimeButton.setOnClickListener(new View.OnClickListener() {
           public void onClick(View v)
           {
               pickTime(selectedWeekDay,true);
           }
        });
        editEndTimeButton = (Button) findViewById(R.id.end_time_button);
        editEndTimeButton.setVisibility(View.INVISIBLE);
        editEndTimeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                pickTime(selectedWeekDay,false);
            }
        });
        finishButton = (Button) findViewById(R.id.finish_button);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                name = profileNameInput.getText().toString();
                prof.setName(name);
                Log.d(MainActivity.LOG_TAG,"Current name : " + name);
                if( name.isEmpty() )
                    name = "NoName";
                try {
                    prof.saveProfile(getApplicationContext());
                } catch ( Exception e ) {
                    e.printStackTrace();
                }
                Log.d(MainActivity.LOG_TAG,"File exists" + new File(getApplicationContext().getFilesDir()+"/"+name+".xml").exists());
                finish();
            }
        });
        weekdayViews[selectedWeekDay].setBackgroundResource(R.drawable.weekday_activated);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstances)
    {
        super.onSaveInstanceState(savedInstances);
        savedInstances.putInt("selectedWeekDay",selectedWeekDay);
        savedInstances.putString("name",profileNameInput.getText().toString());
        for( int currDay = 0; currDay < days.length; currDay++ )
        {
            savedInstances.putBoolean("day"+currDay,days[currDay]);
        }
        for( int currDay = 0; currDay < startTimes.length; currDay++ )
        {
            savedInstances.putString("start"+currDay,startTimes[currDay].toString());
        }
        for( int currDay = 0; currDay < startTimes.length; currDay++ )
        {
            savedInstances.putString("end"+currDay,endTimes[currDay].toString());
        }
        savedInstances.putBoolean("isActive",active);
        savedInstances.putBoolean("isAllowed",allowed);
        savedInstances.putInt("mode",mode);
        savedInstances.putStringArrayList("phoneNumbers",phoneNumbers);
        savedInstances.putStringArrayList("contactNames",contactNames);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstances)
    {
        super.onRestoreInstanceState(savedInstances);
        selectedWeekDay = savedInstances.getInt("selectedWeekDay");
        name = savedInstances.getString("name");
        Log.d(MainActivity.LOG_TAG,"Content of 'name' :"+name);
        for( int currDay = 0; currDay < days.length; currDay++ )
        {
            days[currDay] = savedInstances.getBoolean("day"+currDay);
        }
        for( int currDay = 0; currDay < startTimes.length; currDay++ )
        {
            startTimes[currDay] = new TimeObject(savedInstances.getString("start"+currDay));
        }
        for( int currDay = 0; currDay < endTimes.length; currDay++ ) {
            endTimes[currDay] = new TimeObject(savedInstances.getString("end" + currDay));
        }
        for( int currDay = 0; currDay < weekdayViews.length; currDay++ )
        {
            weekdayCheckboxes[currDay].setChecked(days[currDay]);
            if( currDay == selectedWeekDay )
            {
                weekdayViews[currDay].setBackgroundResource(R.drawable.weekday_activated);
            } else if ( currDay != selectedWeekDay ) {
                weekdayViews[currDay].setBackgroundResource(R.drawable.weekday_deactivated);
            }
        }
        active = savedInstances.getBoolean("active");
        allowed = savedInstances.getBoolean("allowed");
        mode = savedInstances.getInt("mode");
        phoneNumbers = savedInstances.getStringArrayList("phoneNumbers");
        contactNames = savedInstances.getStringArrayList("contactNames");
    }

    private void pickTime(int index,boolean isStart)
    {
        DialogFragment timePickerFragment = new CreateActivityTimePickerFragment();
        Bundle timePickerFragmentBundle = new Bundle();
        timePickerFragmentBundle.putInt("selectedWeekDay",selectedWeekDay);
        timePickerFragmentBundle.putBoolean("isStart",isStart);
        timePickerFragment.setArguments(timePickerFragmentBundle);
        timePickerFragment.show(getFragmentManager(),"pickTime");
    }

    public Profile getActivityProfile()
    {
        return prof;
    }
}
