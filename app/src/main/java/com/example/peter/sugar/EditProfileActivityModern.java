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
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.Arrays;

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
    private int selectedDay = -1;
    private Context activityContext;
    private int selectedWeekDayIndex = -1;

    /**
     * In this function every variable which is declared at the beginning of the file is given an inital value and crucial operations like
     * the registration of all listeners are done.
     * @param savedInstances
     */
    @Override
    public void onCreate(Bundle savedInstances)
    {
        super.onCreate(savedInstances);
        setContentView(R.layout.edit_profile_modern);
        beginOrEnd = 's';
        selectedWeekDayIndex = -1;
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
        for( int currWeekDay = 0; currWeekDay < weekDayRow.length; currWeekDay++ )
        {
            if( chosenProfile.getStart()[currWeekDay].getHour() == -1  )
            {
                weekDayRow[currWeekDay].setBackgroundResource(R.drawable.weekday_blocked);
            } else {
                weekDayRow[currWeekDay].setBackgroundResource(R.drawable.weekday_deactivated);
            }
        }
        registerWeekDayListener();
        registerWeekDayLongClickListener();
        registerCheckBoxListener();
        registerButtonListener();
    }

    public void registerCheckBoxListener() {
        startQuestion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked)
            {
                if( isChecked && !endQuestion.isChecked() )
                {
                    beginOrEnd = 's';
                    if( selectedWeekDayIndex >= 0) {
                        chooseHour.setValue(chosenProfile.getStart()[selectedWeekDayIndex].getHour());
                        chooseMinute.setValue(chosenProfile.getStart()[selectedWeekDayIndex].getMinute());
                    }
                } else if ( isChecked && endQuestion.isChecked() ) {
                    beginOrEnd = 'n';
                    chooseHour.setValue(0);
                    chooseMinute.setValue(0);
                } else if ( !isChecked && !endQuestion.isChecked())
                {
                    beginOrEnd = 'n';
                    chooseHour.setValue(0);
                    chooseMinute.setValue(0);
                } else if ( !isChecked && endQuestion.isChecked() ) {
                    beginOrEnd = 'e';
                    if( selectedWeekDayIndex >= 0 ) {
                        chooseHour.setValue(chosenProfile.getEnd()[selectedWeekDayIndex].getHour());
                        chooseMinute.setValue(chosenProfile.getEnd()[selectedWeekDayIndex].getMinute());
                    }
                }
            }
        });
        endQuestion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compundButton,boolean isChecked)
            {
                if( isChecked && !startQuestion.isChecked() )
                {
                    beginOrEnd = 'e';
                    if( selectedWeekDayIndex >= 0 ) {
                        chooseHour.setValue(chosenProfile.getEnd()[selectedWeekDayIndex].getHour());
                        chooseMinute.setValue(chosenProfile.getEnd()[selectedWeekDayIndex].getMinute());
                    }
                } else if ( isChecked && startQuestion.isChecked() ) {
                    beginOrEnd = 'n';
                    chooseHour.setValue(0);
                    chooseMinute.setValue(0);
                } else if ( !isChecked && !startQuestion.isChecked() )
                {
                    beginOrEnd = 'n';
                    chooseHour.setValue(0);
                    chooseMinute.setValue(0);
                } else if ( !isChecked && startQuestion.isChecked() ) {
                    beginOrEnd = 's';
                    if( selectedWeekDayIndex >= 0 ) {
                        chooseHour.setValue(chosenProfile.getStart()[selectedWeekDayIndex].getHour());
                        chooseMinute.setValue(chosenProfile.getStart()[selectedWeekDayIndex].getMinute());
                    }
                }
            }
        });
    }

    /**
     * Registers all button listeners in this activity. There are many possible cases which can occur and have to be worked on.
     * Case A : One wants to set the start time of a valid day => Update the old time with the new one
     * Case B : One wants to set the end time of a valid day => Update the old time with the new one
     * Case C : One wants to set the start time for an invalid day => Error Toast
     * Case D : One wants to set the end time for an invalid day => Error Toast
     */
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
                        if( selectedWeekDayIndex >= 0) {
                            if (chosenProfile.getStart()[selectedWeekDayIndex].getHour() >= 0) {
                                chosenProfile.setStartForDay(selectedDay, updatedTime);
                                Log.d(MainActivity.LOG_TAG, "You have changed the time to " + chosenProfile.getStart()[selectedDay].toString());
                                try {
                                    chosenProfile.saveProfile(activityContext);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else if( chosenProfile.getStart()[selectedWeekDayIndex].getHour() == -1 ){
                                Toast.makeText(activityContext, "Leider ist dieser Tag blockiert!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else if ( beginOrEnd == 'e' ) {
                        if( selectedWeekDayIndex >= 0) {
                            if (chosenProfile.getEnd()[selectedWeekDayIndex].getHour() >= 0) {
                                Log.d(MainActivity.LOG_TAG,"You have changed the time to " + chosenProfile.getEnd()[selectedWeekDayIndex].toString());
                                chosenProfile.setEndForDay(selectedDay, updatedTime);
                                try {
                                    chosenProfile.saveProfile(activityContext);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else if(chosenProfile.getEnd()[selectedWeekDayIndex].getHour() == -1){
                                Toast.makeText(activityContext, "Leider ist dieser Tag blockiert!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

            }
        });
    }

    /**
     * Register all listeners for the "weekViews". If a day is blocked by the profile, it will be displayed with a red background color.
     * If the opposite its the case, the color will be green. If the user chooses one valid day, the background color of the
     * corresponding TextView will be green until the user wants to choose another day.
     */
    public void registerWeekDayLongClickListener()
    {
        for(int currentWeekDay = 0; currentWeekDay < weekDayRow.length; currentWeekDay++ )
        {
            final int currDay = currentWeekDay;
            weekDayRow[currentWeekDay].setOnLongClickListener(new View.OnLongClickListener() {
                public boolean onLongClick(View weekDayView) {
                    for (int blockedWeekDay = 0; blockedWeekDay < 7; blockedWeekDay++) {
                        if (chosenProfile.getStart()[currDay].getHour() >= 0 && currDay >= 0) {
                            Log.d(MainActivity.LOG_TAG,"This weekDay should now be blocked!");
                            weekDayRow[currDay].setBackgroundResource(R.drawable.weekday_blocked);
                            TimeObject noAccessObject = new TimeObject(-1,-1);
                            chosenProfile.setStartForDay(currDay,noAccessObject);
                            chosenProfile.setEndForDay(currDay,noAccessObject);
                            Log.d(MainActivity.LOG_TAG,"New start time : " + chosenProfile.getStart()[currDay].toString());
                            Log.d(MainActivity.LOG_TAG,"New end time : " + chosenProfile.getEnd()[currDay].toString());
                            try {
                                chosenProfile.saveProfile(activityContext);
                            } catch ( Exception e ) {
                                e.printStackTrace();
                            }
                            return true;
                        } else if ( chosenProfile.getStart()[currDay].getHour() == -1 && currDay >= 0 ){
                            Log.d(MainActivity.LOG_TAG,"This weekDay is now unblocked!");
                            weekDayRow[currDay].setBackgroundResource(R.drawable.weekday_deactivated);
                            chosenProfile.setStartForDay(currDay,new TimeObject(23,59));
                            chosenProfile.setEndForDay(currDay,new TimeObject(23,59));
                            Log.d(MainActivity.LOG_TAG,"New start time : " + chosenProfile.getStart()[currDay].toString());
                            Log.d(MainActivity.LOG_TAG,"New end time : " + chosenProfile.getEnd()[currDay].toString());
                            try {
                                chosenProfile.saveProfile(activityContext);
                            } catch ( Exception e ) {
                                e.printStackTrace();
                            }
                            return true;
                        }
                    }
                    return false;
                }
            });
        }
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
                       Log.d(MainActivity.LOG_TAG, "You have selected the day " + selectedDay);
                        if( chosenProfile.getStart()[selectedDay].getHour() >= 0 ) {
                            weekDayView.setBackgroundResource(R.drawable.weekday_activated);
                        }
                        if( beginOrEnd == 's') {
                            chooseHour.setValue(chosenProfile.getStart()[currDay].getHour());
                            chooseMinute.setValue(chosenProfile.getStart()[currDay].getMinute());
                        } else if ( beginOrEnd == 'e') {
                            chooseHour.setValue(chosenProfile.getEnd()[currDay].getHour());
                            chooseMinute.setValue(chosenProfile.getEnd()[currDay].getMinute());
                        } else if ( beginOrEnd == 'n' ) {
                            chooseHour.setValue(0);
                            chooseMinute.setValue(0);
                        }
                       for (int anyOtherWeekDay = 0; anyOtherWeekDay < weekDayRow.length; anyOtherWeekDay++) {
                           if ( chosenProfile.getStart()[currDay].getHour() == -1 ) {
                               if(selectedWeekDayIndex >= 0) {
                                   if( chosenProfile.getStart()[selectedWeekDayIndex].getHour() >= 0 ) {
                                       weekDayRow[selectedWeekDayIndex].setBackgroundResource(R.drawable.weekday_deactivated);
                                   }
                                   weekDayRow[currDay].setBackgroundResource(R.drawable.weekday_blocked);
                                   selectedWeekDayIndex = currDay;
                               }
                               chooseHour.setValue(0);
                               chooseMinute.setValue(0);
                           } else if ( chosenProfile.getStart()[currDay].getHour() >= 0 ) {
                               if( selectedWeekDayIndex >= 0 && chosenProfile.getStart()[selectedWeekDayIndex].getHour() >= 0) {
                                   weekDayRow[selectedWeekDayIndex].setBackgroundResource(R.drawable.weekday_deactivated);
                               }
                               weekDayRow[currDay].setBackgroundResource(R.drawable.weekday_activated);
                               selectedWeekDayIndex = currDay;
                           }
                       }
               }
            });
        }
    }

}
