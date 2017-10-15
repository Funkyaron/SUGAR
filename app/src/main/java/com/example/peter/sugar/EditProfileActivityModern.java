package com.example.peter.sugar;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

/**
 * Created by SHK on 14.10.17.
 */

public class EditProfileActivityModern extends AppCompatActivity
{

    private NumberPicker editTimeSpinner;
    private Profile chosenProfile;
    private char beginOrEnd;
    TextView mondayView;
    TextView tuesdayView;
    TextView wednesdayView;
    TextView thursdayView;
    TextView fridayView;
    TextView saturdayView;
    TextView sundayView;
    TextView weekDayRow[];
    private CheckBox startQuestion;
    private CheckBox endQuestion;
    private Button confirmUpdatedTime;
    private NumberPicker chooseHour;
    private NumberPicker chooseMinute;
    private int selectedDay = 1;
    private Context activityContext;

    @Override
    public void onCreate(Bundle savedInstances)
    {
        super.onCreate(savedInstances);
        setContentView(R.layout.edit_profile_modern);
        activityContext = this;
        confirmUpdatedTime = (Button) findViewById(R.id.adjust_time);
        startQuestion = (CheckBox) findViewById(R.id.checkBoxStart);
        endQuestion = (CheckBox) findViewById(R.id.checkBoxEnd);
        mondayView = (TextView) findViewById(R.id.mondayRectangle);
        tuesdayView = (TextView) findViewById(R.id.tuesdayRectangle);
        wednesdayView = (TextView) findViewById(R.id.wednesdayRectangle);
        thursdayView = (TextView) findViewById(R.id.thursdayRectangle);
        fridayView = (TextView) findViewById(R.id.fridayRectangle);
        saturdayView = (TextView) findViewById(R.id.saturdayRectangle);
        sundayView = (TextView) findViewById(R.id.sundayRectangle);
        weekDayRow = new TextView[]{mondayView,tuesdayView,wednesdayView,thursdayView,fridayView,saturdayView,sundayView};
        chooseHour = (NumberPicker) findViewById(R.id.hourPicker);
        chooseMinute = (NumberPicker) findViewById(R.id.minutePicker);
        chooseHour.setMinValue(0);
        chooseHour.setMaxValue(23);
        chooseMinute.setMinValue(0);
        chooseMinute.setMaxValue(59);
        String passedProfileName = getIntent().getExtras().getString("profileName");
        chosenProfile = null;
        try {
            chosenProfile = Profile.readProfileFromXmlFile(passedProfileName, this);
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        registerWeekDayListener();
        registerCheckBoxListener();
        registerButtonListener();
        mondayView.setBackgroundResource(R.drawable.weekday_activated);
    }

    public void registerCheckBoxListener() {
        startQuestion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked)
            {
                if( isChecked && !endQuestion.isChecked() )
                {
                    beginOrEnd = 's';
                } else if ( isChecked && endQuestion.isChecked() ) {
                    beginOrEnd = 'n';
                }
            }
        });
        endQuestion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compundButton,boolean isChecked)
            {
                if( isChecked && !startQuestion.isChecked() )
                {
                    beginOrEnd = 'e';
                } else if ( isChecked && startQuestion.isChecked() ) {
                    beginOrEnd = 'n';
                }
            }
        });
    }

    public void registerButtonListener()
    {
        confirmUpdatedTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                int selectedHour = chooseHour.getValue();
                int selectedMinute = chooseMinute.getValue();
                TimeObject updatedTime = new TimeObject(selectedHour,selectedMinute);

                    if( beginOrEnd == 's' )
                    {
                        chosenProfile.setStartForDay(selectedDay,updatedTime);
                        Log.d(MainActivity.LOG_TAG,"You have changed the time to " + chosenProfile.getStart()[selectedDay].toString());
                        try {
                            chosenProfile.saveProfile(activityContext);
                        } catch ( Exception e ) {
                            e.printStackTrace();
                        }
                    } else if ( beginOrEnd == 'e' ) {
                        chosenProfile.setEndForDay(selectedDay, updatedTime);
                        try {
                            chosenProfile.saveProfile(activityContext);
                        } catch ( Exception e ) {
                            e.printStackTrace();
                        }
                    }

            }
        });
    }

    public void registerWeekDayListener()
    {
        for(int currentWeekDay = 0; currentWeekDay < weekDayRow.length; currentWeekDay++ )
        {
            final int currDay = currentWeekDay;
            weekDayRow[currentWeekDay].setOnClickListener(new View.OnClickListener() {
               public void onClick(View weekDayView)
               {
                   selectedDay = currDay;
                   Log.d(MainActivity.LOG_TAG,"You have selected the day " + selectedDay);
                   weekDayView.setBackgroundResource(R.drawable.weekday_activated);
                   for( int anyOtherWeekDay = 0; anyOtherWeekDay < weekDayRow.length; anyOtherWeekDay++ )
                   {
                       if( anyOtherWeekDay != currDay )
                       {
                           weekDayRow[anyOtherWeekDay].setBackgroundResource(R.drawable.weekday_deactivated);
                       }
                   }
               }
            });
        }
    }

}
