package com.example.peter.sugar;

import android.app.DialogFragment;
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

public class CreateProfileActivity extends ActivityCreatingProfile
{
    private int selectedWeekDay;
    private Profile passedProfile;
    private TextView[] weekdayViews;
    private CheckBox[] weekdayCheckboxes;
    private Button editStartTimeButton;
    private Button editEndTimeButton;
    private Button finishButton;

    @Override
    protected void onCreate(Bundle savedInstances)
    {
        super.onCreate(savedInstances);
        setContentView(R.layout.activity_create_profile);

        passedProfile = getProfile();
        weekdayViews = new TextView[7];

        /* Assign a TextView to each entry of 'weekdayViews' */
        for(int currDay = 0; currDay < 7; currDay++ )
        {
            final int selectedDay = currDay;
            weekdayViews[currDay] = new TextView(this);
            weekdayViews[currDay].setOnClickListener(new View.OnClickListener() {
               public void onClick(View v)
               {
                   selectedWeekDay = selectedDay;
                   weekdayViews[selectedDay].setBackgroundResource(R.drawable.weekday_activated);
                   for( int currDay = 0; currDay < 7; currDay++ )
                   {
                       if( currDay != selectedDay )
                           weekdayViews[selectedDay].setBackgroundResource(R.drawable.weekday_deactivated);
                   }
                   if( weekdayCheckboxes[selectedDay].isChecked() )
                   {
                       editStartTimeButton.setText("Ab : \n " + passedProfile.getStart()[selectedDay].toString());
                       editEndTimeButton.setText("Bis : \n " + passedProfile.getEnd()[selectedDay].toString());
                       editStartTimeButton.setVisibility(View.VISIBLE);
                       editEndTimeButton.setVisibility(View.VISIBLE);
                   } else if ( !weekdayCheckboxes[selectedDay].isChecked() ) {
                       editStartTimeButton.setText("");
                       editEndTimeButton.setText("");
                       editStartTimeButton.setVisibility(View.GONE);
                       editEndTimeButton.setVisibility(View.GONE);
                   }
               }
            });
        }

        weekdayCheckboxes = new CheckBox[7];

        /* Assign a Checkbox to each entry of 'weekdayCheckboxes' */
        for(int currDay = 0; currDay < 7; currDay++ )
        {
            final int selectedCheckBox = currDay;
            weekdayCheckboxes[currDay] = new CheckBox(this);
            weekdayCheckboxes[currDay].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked)
                {
                    if(isChecked && selectedCheckBox == selectedWeekDay )
                    {
                        editStartTimeButton.setVisibility(View.VISIBLE);
                        editEndTimeButton.setVisibility(View.VISIBLE);
                    } else if (!isChecked ) {
                        editStartTimeButton.setVisibility(View.GONE);
                        editEndTimeButton.setVisibility(View.GONE);
                    }
                }
            });
        }

        editStartTimeButton = (Button) findViewById(R.id.start_time_button);
        editStartTimeButton.setOnClickListener(new View.OnClickListener() {
           public void onClick(View v)
           {
           }
        });
        editEndTimeButton = (Button) findViewById(R.id.end_time_button);
        editEndTimeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
            }
        });
        finishButton = (Button) findViewById(R.id.finish_button);
    }
}
