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

public class CreateProfileActivity extends Activity {

    private String inputName;
    private boolean inputDays[];
    private TimeObject inputStartTime[];
    private TimeObject inputEndTime[];

    private EditText nameInput;
    private TextView[] dayViews;
    private CheckBox[] dayCheckboxes;
    private Button startTimeButton;
    private Button endTimeButton;
    private Button finishButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        nameInput = (EditText) findViewById(R.id.edit_profile_name);
        TableRow daysRow = (TableRow) findViewById(R.id.days_row);
        dayViews = new TextView[7];
        for(int i = 0; i < 7; i++) {
            dayViews[i] = (TextView) daysRow.getChildAt(i);
        }
        TableRow checkboxesRow = (TableRow) findViewById(R.id.days_checkboxes);
        dayCheckboxes = new CheckBox[7];
        for(int i = 0; i < 7; i++) {
            dayCheckboxes[i] = (CheckBox) checkboxesRow.getChildAt(i);
        }
        startTimeButton = (Button) findViewById(R.id.start_time_button);
        endTimeButton = (Button) findViewById(R.id.end_time_button);
        finishButton = (Button) findViewById(R.id.finish_button);



        for( int currDay = 0; currDay < dayViews.length; currDay++ )
        {
            final int selectedDay = currDay;
            dayViews[currDay].setOnClickListener(new View.OnClickListener() {
               public void onClick(View v)
               {
                   dayViews[selectedDay].setBackgroundResource(R.color.green);
                   if( inputDays[selectedDay] )
                   {
                       startTimeButton.setText("Ab : \n 0:00");
                       endTimeButton.setText("Ab : \n 0:00");
                   } else {
                       // Do nothing
                   }
               }
            });
        }

        for( int currDay = 0; currDay < dayCheckboxes.length; currDay++ )
        {
            final int selectedDay = currDay;
            dayCheckboxes[currDay].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    inputDays[selectedDay] = isChecked;
                    if( isChecked )
                    {
                        startTimeButton.setVisibility(View.VISIBLE);
                        endTimeButton.setVisibility(View.VISIBLE);
                    } else if ( !isChecked ) {
                        startTimeButton.setVisibility(View.INVISIBLE);
                        endTimeButton.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }

        startTimeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {

            }
        });
    }



}
