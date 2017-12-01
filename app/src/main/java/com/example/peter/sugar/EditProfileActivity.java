package com.example.peter.sugar;

import android.app.DialogFragment;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class EditProfileActivity extends ActivityContainingProfile {

    private TextView profileNameView;

    private TextView[] dayViews;
    private CheckBox[] dayCheckboxes;

    private Button startTimeButton;
    private Button endTimeButton;

    private Button chooseContactsButton;
    private Button finishButton;

    private Profile prof;
    private String profileName;
    private boolean[] days;
    private TimeObject[] startTimes;
    private TimeObject[] endTimes;

    /**
     * Used to detect which day of week is selected
     */
    private int dayIndex;

    @Override @NonNull
    protected Profile createProfile() {
        String profileName = getIntent().getStringExtra(MainActivity.EXTRA_PROFILE_NAME);

        try {
            return Profile.readProfileFromXmlFile(profileName, this);
        } catch(Exception e) {
            Log.e(MainActivity.LOG_TAG, e.toString());
            Toast.makeText(this, R.string.error, Toast.LENGTH_LONG).show();
            finish();
        }
        return new Profile();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        //Initialize Views
        profileNameView = (TextView) findViewById(R.id.profile_name_view);

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

        chooseContactsButton = (Button) findViewById(R.id.choose_contacts_button);
        finishButton = (Button) findViewById(R.id.finish_button);


        //Extract information from the passed profile and set up the Views.
        prof = getProfile();
        profileName = prof.getName();
        days = prof.getDays();
        startTimes = prof.getStart();
        endTimes = prof.getEnd();

        profileNameView.setText(profileName);
        dayIndex = 0;
        dayViews[dayIndex].setBackground(getResources().getDrawable(R.drawable.weekday_activated, null));
        for(int i = 0; i < 7; i++) {
            dayCheckboxes[i].setChecked(days[i]);
        }

        startTimeButton.setText(getString(R.string.from_plus_time, startTimes[dayIndex].toString()));
        endTimeButton.setText(getString(R.string.to_plus_time, endTimes[dayIndex].toString()));
        if(!days[dayIndex]) {
            startTimeButton.setVisibility(View.INVISIBLE);
            endTimeButton.setVisibility(View.INVISIBLE);
        }


        //Implement functionality
        for(int i = 0; i < 7; i++) {
            final int ind = i;
            dayViews[ind].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dayIndex = ind;
                    for(int j = 0; j < 7; j++) {
                        if(j == ind) {
                            dayViews[j].setBackground(getResources().getDrawable(R.drawable.weekday_activated, null));
                        } else {
                            dayViews[j].setBackground(getResources().getDrawable(R.drawable.weekday_deactivated, null));
                        }
                    }
                    startTimeButton.setText(getString(R.string.from_plus_time, startTimes[ind].toString()));
                    endTimeButton.setText(getString(R.string.to_plus_time, endTimes[ind].toString()));
                    if(days[dayIndex]) {
                        startTimeButton.setVisibility(View.VISIBLE);
                        endTimeButton.setVisibility(View.VISIBLE);
                    } else {
                        startTimeButton.setVisibility(View.INVISIBLE);
                        endTimeButton.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }

        for(int i = 0; i < 7; i++) {
            final int ind = i;
            dayCheckboxes[ind].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    days[ind] = isChecked;
                    if(ind == dayIndex) {
                        if (isChecked) {
                            startTimeButton.setVisibility(View.VISIBLE);
                            endTimeButton.setVisibility(View.VISIBLE);
                        } else {
                            startTimeButton.setVisibility(View.INVISIBLE);
                            endTimeButton.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            });
        }

        startTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickTime(profileName, dayIndex, true);
            }
        });

        endTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickTime(profileName, dayIndex, false);
            }
        });

        chooseContactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ContactsDialogFragment().show(getFragmentManager(), "cont");
            }
        });

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    prof.saveProfile(EditProfileActivity.this);
                    Toast.makeText(EditProfileActivity.this, R.string.profile_saved, Toast.LENGTH_LONG).show();
                    if(prof.isActive()) {
                        TimeManager mgr = new TimeManager(EditProfileActivity.this);
                        mgr.initProfile(prof);
                    }
                } catch(Exception e) {
                    Log.e(MainActivity.LOG_TAG, e.toString());
                    Toast.makeText(EditProfileActivity.this, R.string.profile_not_saved, Toast.LENGTH_LONG).show();
                }
                finish();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("dayIndex", dayIndex);
        for(int i = 0; i < days.length; i++) {
            outState.putBoolean("day" + i, days[i]);
        }
        for(int i = 0; i < startTimes.length; i++) {
            outState.putString("startTime" + i, startTimes[i].toString());
        }
        for(int i = 0; i < endTimes.length; i++) {
            outState.putString("endTime" + i, endTimes[i].toString());
        }
        outState.putStringArrayList("phoneNumbers", getProfile().getPhoneNumbers());
        outState.putStringArrayList("contactNames", getProfile().getContactNames());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        dayIndex = savedInstanceState.getInt("dayIndex");
        for(int i = 0; i < days.length; i++) {
            days[i] = savedInstanceState.getBoolean("day" + i);
        }
        for(int i = 0; i < startTimes.length; i++) {
            startTimes[i] = new TimeObject(savedInstanceState.getString("startTime" + i));
        }
        for(int i = 0; i < endTimes.length; i++) {
            endTimes[i] = new TimeObject(savedInstanceState.getString("endTime" + i));
        }
        getProfile().setPhoneNumbers(savedInstanceState.getStringArrayList("phoneNumbers"));
        getProfile().setContactNames(savedInstanceState.getStringArrayList("contactNames"));

        for(int i = 0; i < days.length; i++) {
            dayCheckboxes[i].setChecked(days[i]);
            if(i == dayIndex) {
                dayViews[i].setBackground(getResources().getDrawable(R.drawable.weekday_activated, null));
            } else {
                dayViews[i].setBackground(getResources().getDrawable(R.drawable.weekday_deactivated, null));
            }
        }
        startTimeButton.setText(getString(R.string.from_plus_time, startTimes[dayIndex].toString()));
        endTimeButton.setText(getString(R.string.to_plus_time, endTimes[dayIndex].toString()));
    }
    


    private void pickTime(String passedName, int index, boolean isStart) {
        Bundle args = new Bundle();
        args.putString(MainActivity.EXTRA_PROFILE_NAME, passedName);
        args.putInt(MainActivity.EXTRA_INDEX, index);
        args.putBoolean(MainActivity.EXTRA_IS_START, isStart);
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.setArguments(args);
        newFragment.show(getFragmentManager(), "end" + index);
    }
}
