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
    private Button selectContacts;
    private CheckBox startQuestion;
    private CheckBox endQuestion;
    private Button confirmUpdatedTime;
    private NumberPicker startChooseHour;
    private NumberPicker startChooseMinute;
    private NumberPicker endChooseHour;
    private NumberPicker endChooseMinute;
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
        startChooseHour.setMinValue(0);
        startChooseHour.setMaxValue(23);
        startChooseMinute.setMinValue(0);
        startChooseMinute.setMaxValue(59);
        endChooseHour.setMinValue(0);
        endChooseHour.setMaxValue(23);
        endChooseMinute.setMinValue(0);
        endChooseMinute.setMaxValue(59);
        String passedProfileName = getIntent().getExtras().getString("profileName");
        chosenProfile = null;
        try {
            chosenProfile = Profile.readProfileFromXmlFile(passedProfileName, this);
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }


    /**
     * Will register all the essential "onClickListeners" for the Views which represent the days of the week.
     */
    public void registerWeekDayListeners()
    {
        for(int currentWeekDay = 0; currentWeekDay < weekDayRow.length; currentWeekDay++ )
        {
        }
    }
}
