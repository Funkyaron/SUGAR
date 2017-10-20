package com.example.peter.sugar;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
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

public class EditProfileActivityModern extends ActivityContainingProfile
{
    private Profile chosenProfile;
    TextView mondayView;
    TextView tuesdayView;
    TextView wednesdayView;
    TextView thursdayView;
    TextView fridayView;
    TextView saturdayView;
    TextView sundayView;
    TextView weekDayRow[];
    private Button selectContacts;
    private Button confirmUpdatedTime;
    private NumberPicker startChooseHour;
    private NumberPicker startChooseMinute;
    private NumberPicker endChooseHour;
    private NumberPicker endChooseMinute;
    private int selectedDay;
    private Context activityContext;

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
        selectedDay = -1;
        activityContext = this;
        selectContacts = (Button) findViewById(R.id.determineContacts);
        confirmUpdatedTime = (Button) findViewById(R.id.adjust_time);
        mondayView = (TextView) findViewById(R.id.mondayRectangle);
        tuesdayView = (TextView) findViewById(R.id.tuesdayRectangle);
        wednesdayView = (TextView) findViewById(R.id.wednesdayRectangle);
        thursdayView = (TextView) findViewById(R.id.thursdayRectangle);
        fridayView = (TextView) findViewById(R.id.fridayRectangle);
        saturdayView = (TextView) findViewById(R.id.saturdayRectangle);
        sundayView = (TextView) findViewById(R.id.sundayRectangle);
        weekDayRow = new TextView[]{mondayView,tuesdayView,wednesdayView,thursdayView,fridayView,saturdayView,sundayView};
        startChooseHour = (NumberPicker) findViewById(R.id.startHourPicker);
        startChooseMinute = (NumberPicker) findViewById(R.id.startMinutePicker);
        endChooseHour = (NumberPicker) findViewById(R.id.endHourPicker);
        endChooseMinute = (NumberPicker) findViewById(R.id.endMinutePicker);
        startChooseHour.setLayerType(View.LAYER_TYPE_HARDWARE,null);
        startChooseMinute.setLayerType(View.LAYER_TYPE_HARDWARE,null);
        endChooseHour.setLayerType(View.LAYER_TYPE_HARDWARE,null);
        endChooseMinute.setLayerType(View.LAYER_TYPE_HARDWARE,null);
        startChooseHour.setMinValue(0);
        startChooseHour.setMaxValue(23);
        startChooseMinute.setMinValue(0);
        startChooseMinute.setMaxValue(59);
        endChooseHour.setMinValue(0);
        endChooseHour.setMaxValue(23);
        endChooseMinute.setMinValue(0);
        endChooseMinute.setMaxValue(59);
        String passedProfileName = getIntent().getExtras().getString("profileName");
        chosenProfile = getProfile();
        try {
            chosenProfile = Profile.readProfileFromXmlFile(passedProfileName, this);
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        registerWeekDayListeners();
        registerButtonListeners();
    }


    /**
     * Will register all the essential "onClickListeners" for the Views which represent the days of the week
     * ( e.g. : "mondayView",...","sundayView"). If you click on one of these Views, it will turn green and
     * you are able to choose a explicit time for the start and the end of the profile on the corresponding day.
     */
    public void registerWeekDayListeners()
    {
        for(int currentWeekDay = 0; currentWeekDay < weekDayRow.length; currentWeekDay++ )
        {
            final int currentlyChosenDay = currentWeekDay;
            weekDayRow[currentWeekDay].setOnClickListener(new View.OnClickListener() {
                public void onClick(View v)
                {
                    /* Note : If no day was selected you have to say explicitly that your app shouldn't do anything
                       If this mechanism isn't implemented, you will have an ArraysOutOfBoundsException
                     */
                    if( selectedDay >= 0 ) {
                        weekDayRow[selectedDay].setBackgroundResource(R.drawable.weekday_deactivated);
                    }
                    selectedDay = currentlyChosenDay;
                    weekDayRow[currentlyChosenDay].setBackgroundResource(R.drawable.weekday_activated);
                    startChooseHour.setValue(chosenProfile.getStart()[currentlyChosenDay].getHour());
                    startChooseMinute.setValue(chosenProfile.getStart()[currentlyChosenDay].getMinute());
                    endChooseHour.setValue(chosenProfile.getEnd()[currentlyChosenDay].getHour());
                    endChooseMinute.setValue(chosenProfile.getEnd()[currentlyChosenDay].getMinute());
                }
            });
        }
    }

    /**
     * Will register the OnClickListener for the buttons "SaveProfileButton" and "SelectContactsButton". While the "SaveProfileButton"
     * shouldn't do anything if no inital day was selected, the button will save the newly inserted time to the profile if you select a
     * day AND press the button. "SelectContactsButton" should display the ContactsDialogFragment.
     */
    public void registerButtonListeners()
    {
        confirmUpdatedTime.setOnClickListener(new View.OnClickListener() {
           public void onClick(View v)
           {
               if( selectedDay == -1 )
               {
                   return;
               } else if ( selectedDay >= 0 ){
                   TimeObject updatedStartTime = new TimeObject(startChooseHour.getValue(),startChooseMinute.getValue());
                   TimeObject updatedEndTime = new TimeObject(endChooseHour.getValue(),endChooseMinute.getValue());
                   chosenProfile.setStartForDay(selectedDay,updatedStartTime);
                   chosenProfile.setEndForDay(selectedDay,updatedEndTime);
                   try {
                       Log.d(MainActivity.LOG_TAG,"I am about to save the data!");
                       Log.d(MainActivity.LOG_TAG,"New time for start : " + chosenProfile.getStart()[selectedDay].toString());
                       Log.d(MainActivity.LOG_TAG,"New time for end : " + chosenProfile.getEnd()[selectedDay].toString());
                       chosenProfile.saveProfile(activityContext);
                   } catch ( Exception e ) {
                       e.printStackTrace();
                   }
               }
           }
        });

        selectContacts.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                new ContactsDialogFragment().show(getFragmentManager(), "cont");
            }
        });
    }
}
