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
    private Context context;
    private int selectedWeekDay;
    private Profile passedProfile;
    private EditText profileNameInput;
    private TextView[] weekdayViews;
    private CheckBox[] weekdayCheckboxes;
    private Button editStartTimeButton;
    private Button editEndTimeButton;
    private Button finishButton;

    private String name;
    private boolean days[];
    private TimeObject startTimes[];
    private TimeObject endTimes[];
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

        name = "";
        days = new boolean[7];
        startTimes = new TimeObject[7];
        endTimes = new TimeObject[7];
        active = false;
        allowed = true;
        mode = 2;
        phoneNumbers = new ArrayList<String>(0);
        contactNames = new ArrayList<String>(0);
        phoneNumbers.add("000");
        contactNames.add("BBB");
        for( int i = 0; i < startTimes.length; i++ )
        {
            startTimes[i] = new TimeObject(0,0);
            endTimes[i] = new TimeObject(0,0);
        }

        context = this;
        passedProfile = getProfile();
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
                            editStartTimeButton.setText("Ab : \n " + getProfile().getStart()[selectedWeekDay].toString());
                            editEndTimeButton.setText("End : \n" + getProfile().getEnd()[selectedWeekDay].toString());
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
                        getProfile().setDayActiveForWeekDay(selectDay);
                        if( selectDay == selectedWeekDay )
                        {
                            editStartTimeButton.setVisibility(View.VISIBLE);
                            editEndTimeButton.setVisibility(View.VISIBLE);
                            editStartTimeButton.setText("Ab : \n" + getProfile().getStart()[selectDay].toString());
                            editEndTimeButton.setText("Bis : \n" + getProfile().getEnd()[selectDay].toString());
                        }
                    } else if(!isChecked) {
                        getProfile().setDayInactiveForWeekDay(selectDay);
                        editStartTimeButton.setVisibility(View.INVISIBLE);
                        editEndTimeButton.setVisibility(View.INVISIBLE);
                    } else if ( isChecked && selectDay == selectedWeekDay ) {
                        getProfile().setDayActiveForWeekDay(selectDay);
                        editStartTimeButton.setText("Ab : \n " + getProfile().getStart()[selectedWeekDay].toString());
                        editEndTimeButton.setText("Bis : \n" + getProfile().getEnd()[selectedWeekDay].toString());
                        editStartTimeButton.setVisibility(View.VISIBLE);
                        editEndTimeButton.setVisibility(View.VISIBLE);
                    }
                }
            });
        }

        editStartTimeButton = (Button) findViewById(R.id.start_time_button);
        editStartTimeButton.setVisibility(View.GONE);
        editStartTimeButton.setOnClickListener(new View.OnClickListener() {
           public void onClick(View v)
           {
               pickTime(selectedWeekDay,true);
           }
        });
        editEndTimeButton = (Button) findViewById(R.id.end_time_button);
        editEndTimeButton.setVisibility(View.GONE);
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
                Log.d(MainActivity.LOG_TAG,"Current name : " + name);
                if( name.isEmpty() )
                    name = "NoName";
                Profile work = new Profile(name,getProfile().getDays(),getProfile().getStart(),getProfile().getEnd(),false,true,2,phoneNumbers,contactNames);
                try {
                    work.saveProfile(getApplicationContext());
                } catch ( Exception e ) {
                    e.printStackTrace();
                }
                Log.d(MainActivity.LOG_TAG,"File exists" + new File(context.getFilesDir()+"/"+name+".xml").exists());
                finish();
            }
        });
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
}
