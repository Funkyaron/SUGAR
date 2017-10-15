package com.example.peter.sugar;

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
    TextView mondayView;
    TextView tuesdayView;
    TextView wednesdayView;
    TextView thursdayView;
    TextView fridayView;
    TextView saturdayView;
    TextView sundayView;
    private CheckBox startQuestion;
    private CheckBox endQuestion;
    private NumberPicker chooseHour;
    private NumberPicker chooseMinute;
    private String selectedDay = "Monday";

    @Override
    public void onCreate(Bundle savedInstances)
    {
        super.onCreate(savedInstances);
        setContentView(R.layout.edit_profile_modern);
        startQuestion = (CheckBox) findViewById(R.id.checkBoxStart);
        endQuestion = (CheckBox) findViewById(R.id.checkBoxEnd);
        mondayView = (TextView) findViewById(R.id.mondayRectangle);
        tuesdayView = (TextView) findViewById(R.id.tuesdayRectangle);
        wednesdayView = (TextView) findViewById(R.id.wednesdayRectangle);
        thursdayView = (TextView) findViewById(R.id.thursdayRectangle);
        fridayView = (TextView) findViewById(R.id.fridayRectangle);
        saturdayView = (TextView) findViewById(R.id.saturdayRectangle);
        sundayView = (TextView) findViewById(R.id.sundayRectangle);
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
        mondayView.setBackgroundResource(R.drawable.weekday_activated);
    }

    public void registerWeekDayListener()
    {
        mondayView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                selectedDay="Monday";
                v.setBackgroundResource(R.drawable.weekday_activated);
                tuesdayView.setBackgroundResource(R.drawable.weekday_deactivated);
                wednesdayView.setBackgroundResource(R.drawable.weekday_deactivated);
                thursdayView.setBackgroundResource(R.drawable.weekday_deactivated);
                fridayView.setBackgroundResource(R.drawable.weekday_deactivated);
                saturdayView.setBackgroundResource(R.drawable.weekday_deactivated);
                sundayView.setBackgroundResource(R.drawable.weekday_deactivated);
                if( startQuestion.isChecked() && !endQuestion.isChecked() )
                {
                    chooseHour.setValue(chosenProfile.getStart()[0].getHour());
                    chooseMinute.setValue(chosenProfile.getStart()[0].getMinute());
                } else if ( !startQuestion.isChecked() && endQuestion.isChecked() ) {
                    chooseHour.setValue(chosenProfile.getEnd()[0].getHour());
                    chooseMinute.setValue(chosenProfile.getEnd()[0].getMinute());
                }
            }
        });
        tuesdayView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                selectedDay="Tuesday";
                v.setBackgroundResource(R.drawable.weekday_activated);
                mondayView.setBackgroundResource(R.drawable.weekday_deactivated);
                wednesdayView.setBackgroundResource(R.drawable.weekday_deactivated);
                thursdayView.setBackgroundResource(R.drawable.weekday_deactivated);
                fridayView.setBackgroundResource(R.drawable.weekday_deactivated);
                mondayView.setBackgroundResource(R.drawable.weekday_deactivated);
                sundayView.setBackgroundResource(R.drawable.weekday_deactivated);
                if( startQuestion.isChecked() && !endQuestion.isChecked() )
                {
                    chooseHour.setValue(chosenProfile.getStart()[1].getHour());
                    chooseMinute.setValue(chosenProfile.getStart()[1].getMinute());
                } else if ( !startQuestion.isChecked() && endQuestion.isChecked() ) {
                    chooseHour.setValue(chosenProfile.getEnd()[1].getHour());
                    chooseMinute.setValue(chosenProfile.getEnd()[1].getMinute());
                }
            }
        });
        wednesdayView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                selectedDay="Wednesday";
                v.setBackgroundResource(R.drawable.weekday_activated);
                tuesdayView.setBackgroundResource(R.drawable.weekday_deactivated);
                mondayView.setBackgroundResource(R.drawable.weekday_deactivated);
                thursdayView.setBackgroundResource(R.drawable.weekday_deactivated);
                fridayView.setBackgroundResource(R.drawable.weekday_deactivated);
                mondayView.setBackgroundResource(R.drawable.weekday_deactivated);
                sundayView.setBackgroundResource(R.drawable.weekday_deactivated);
                if( startQuestion.isChecked() && !endQuestion.isChecked() )
                {
                    chooseHour.setValue(chosenProfile.getStart()[2].getHour());
                    chooseMinute.setValue(chosenProfile.getStart()[2].getMinute());
                } else if ( !startQuestion.isChecked() && endQuestion.isChecked() ) {
                    chooseHour.setValue(chosenProfile.getEnd()[2].getHour());
                    chooseMinute.setValue(chosenProfile.getEnd()[2].getMinute());
                }
            }
        });
        thursdayView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                selectedDay="Thursday";
                v.setBackgroundResource(R.drawable.weekday_activated);
                tuesdayView.setBackgroundResource(R.drawable.weekday_deactivated);
                wednesdayView.setBackgroundResource(R.drawable.weekday_deactivated);
                mondayView.setBackgroundResource(R.drawable.weekday_deactivated);
                fridayView.setBackgroundResource(R.drawable.weekday_deactivated);
                mondayView.setBackgroundResource(R.drawable.weekday_deactivated);
                sundayView.setBackgroundResource(R.drawable.weekday_deactivated);
                if( startQuestion.isChecked() && !endQuestion.isChecked() )
                {
                    chooseHour.setValue(chosenProfile.getStart()[3].getHour());
                    chooseMinute.setValue(chosenProfile.getStart()[3].getMinute());
                } else if ( !startQuestion.isChecked() && endQuestion.isChecked() ) {
                    chooseHour.setValue(chosenProfile.getEnd()[3].getHour());
                    chooseMinute.setValue(chosenProfile.getEnd()[3].getMinute());
                }
            }
        });
        fridayView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                selectedDay="Friday";
                v.setBackgroundResource(R.drawable.weekday_activated);
                tuesdayView.setBackgroundResource(R.drawable.weekday_deactivated);
                wednesdayView.setBackgroundResource(R.drawable.weekday_deactivated);
                thursdayView.setBackgroundResource(R.drawable.weekday_deactivated);
                mondayView.setBackgroundResource(R.drawable.weekday_deactivated);
                mondayView.setBackgroundResource(R.drawable.weekday_deactivated);
                sundayView.setBackgroundResource(R.drawable.weekday_deactivated);
                if( startQuestion.isChecked() && !endQuestion.isChecked() )
                {
                    chooseHour.setValue(chosenProfile.getStart()[4].getHour());
                    chooseMinute.setValue(chosenProfile.getStart()[4].getMinute());
                } else if ( !startQuestion.isChecked() && endQuestion.isChecked() ) {
                    chooseHour.setValue(chosenProfile.getEnd()[4].getHour());
                    chooseMinute.setValue(chosenProfile.getEnd()[4].getMinute());
                }
            }
        });
        saturdayView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                selectedDay="Saturday";
                v.setBackgroundResource(R.drawable.weekday_activated);
                tuesdayView.setBackgroundResource(R.drawable.weekday_deactivated);
                wednesdayView.setBackgroundResource(R.drawable.weekday_deactivated);
                thursdayView.setBackgroundResource(R.drawable.weekday_deactivated);
                fridayView.setBackgroundResource(R.drawable.weekday_deactivated);
                mondayView.setBackgroundResource(R.drawable.weekday_deactivated);
                sundayView.setBackgroundResource(R.drawable.weekday_deactivated);
                if( startQuestion.isChecked() && !endQuestion.isChecked() )
                {
                    chooseHour.setValue(chosenProfile.getStart()[5].getHour());
                    chooseMinute.setValue(chosenProfile.getStart()[5].getMinute());
                } else if ( !startQuestion.isChecked() && endQuestion.isChecked() ) {
                    chooseHour.setValue(chosenProfile.getEnd()[5].getHour());
                    chooseMinute.setValue(chosenProfile.getEnd()[5].getMinute());
                }
            }
        });
        sundayView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                selectedDay="Sunday";
                v.setBackgroundResource(R.drawable.weekday_activated);
                tuesdayView.setBackgroundResource(R.drawable.weekday_deactivated);
                wednesdayView.setBackgroundResource(R.drawable.weekday_deactivated);
                thursdayView.setBackgroundResource(R.drawable.weekday_deactivated);
                fridayView.setBackgroundResource(R.drawable.weekday_deactivated);
                saturdayView.setBackgroundResource(R.drawable.weekday_deactivated);
                mondayView.setBackgroundResource(R.drawable.weekday_deactivated);
                if( startQuestion.isChecked() && !endQuestion.isChecked() )
                {
                    chooseHour.setValue(chosenProfile.getStart()[6].getHour());
                    chooseMinute.setValue(chosenProfile.getStart()[6].getMinute());
                } else if ( !startQuestion.isChecked() && endQuestion.isChecked() ) {
                    chooseHour.setValue(chosenProfile.getEnd()[6].getHour());
                    chooseMinute.setValue(chosenProfile.getEnd()[6].getMinute());
                }
            }
        });
    }

}
