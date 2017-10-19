package com.example.peter.sugar;

import android.app.DialogFragment;
import android.graphics.Color;
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

public class EditProfileActivity extends ActivityContainingProfile {

    private TextView profileNameView;

    private TextView[] dayViews;
    private CheckBox[] dayCheckboxes;

    private Button startTimeButton;
    private Button endTimeButton;

    private Button chooseContactsButton;
    private Button finishButton;

    //Used to detect which day of week is selected
    private int index;

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
        final Profile prof = getProfile();
        final String profileName = prof.getName();
        final boolean[] days = prof.getDays();
        final TimeObject[] startTimes = prof.getStart();
        final TimeObject[] endTimes = prof.getEnd();

        profileNameView.setText(profileName);
        index = 0;
        dayViews[index].setBackground(getResources().getDrawable(R.drawable.weekday_activated, null));
        for(int i = 0; i < 7; i++) {
            dayCheckboxes[i].setChecked(days[i]);
        }

        startTimeButton.setText(getString(R.string.from_plus_time, startTimes[index].toString()));
        endTimeButton.setText(getString(R.string.to_plus_time, endTimes[index].toString()));
        if(!days[index]) {
            startTimeButton.setVisibility(View.INVISIBLE);
            endTimeButton.setVisibility(View.INVISIBLE);
        }


        //Implement functionality
        for(int i = 0; i < 7; i++) {
            final int ind = i;
            dayViews[ind].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    index = ind;
                    for(int j = 0; j < 7; j++) {
                        if(j == ind) {
                            dayViews[j].setBackground(getResources().getDrawable(R.drawable.weekday_activated, null));
                        } else {
                            dayViews[j].setBackground(getResources().getDrawable(R.drawable.weekday_deactivated, null));
                        }
                    }
                    startTimeButton.setText(getString(R.string.from_plus_time, startTimes[ind].toString()));
                    endTimeButton.setText(getString(R.string.to_plus_time, endTimes[ind].toString()));
                    if(days[index]) {
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
                    if(ind == index) {
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
                pickTime(profileName, index, true);
            }
        });

        endTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickTime(profileName, index, false);
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
                if(prof.isActive()) {
                    TimeManager mgr = new TimeManager(EditProfileActivity.this);
                    mgr.initProfile(prof);
                }
                finish();
            }
        });

        /*
        final String passedName = getIntent().getExtras().getString("profileName");


        final boolean[] days = prof.getDays();


        final TableRow daysRow = (TableRow) findViewById(R.id.days_row);
        final TableRow startTimeRow = (TableRow) findViewById(R.id.start_time_row);
        final TableRow endTimeRow = (TableRow) findViewById(R.id.end_time_row);

        for(int i = 0; i < daysRow.getChildCount(); i++) {
            TextView v = (TextView) daysRow.getChildAt(i);
            if(days[i])
                v.setBackgroundColor(Color.GREEN);
            else
                v.setBackgroundColor(Color.RED);
            final Integer index = i;
            v.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    TextView dayView = (TextView) daysRow.getChildAt(index);
                    TextView startTimeView = (TextView) startTimeRow.getChildAt(index);
                    TextView endTimeView = (TextView) endTimeRow.getChildAt(index);

                    days[index] = !days[index];

                    if(days[index]) {
                        dayView.setBackgroundColor(Color.GREEN);
                        startTimeView.setBackgroundColor(Color.GREEN);
                        startTimeView.setText(startTimes[index].toString());
                        startTimeView.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                pickTime(passedName, index, true);
                            }
                        });
                        endTimeView.setBackgroundColor(Color.GREEN);
                        endTimeView.setText(endTimes[index].toString());
                        endTimeView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                pickTime(passedName, index, false);
                            }
                        });
                    } else {
                        dayView.setBackgroundColor(Color.RED);
                        startTimeView.setBackgroundColor(Color.RED);
                        startTimeView.setText("");
                        startTimeView.setOnClickListener(null);
                        endTimeView.setBackgroundColor(Color.RED);
                        endTimeView.setText("");
                        endTimeView.setOnClickListener(null);
                    }
                }
            });
        }

        Log.d(MainActivity.LOG_TAG, "Children of startTimeRow: " + startTimeRow.getChildCount());

        for(int i = 0; i < startTimeRow.getChildCount(); i++) {
            TextView v = (TextView) startTimeRow.getChildAt(i);
            if(days[i]) {
                v.setText(startTimes[i].toString());
                v.setBackgroundColor(Color.GREEN);
                final Integer index = i;
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pickTime(passedName, index, true);
                    }
                });
            } else {
                v.setBackgroundColor(Color.RED);
                v.setOnClickListener(null);
            }

        }

        for(int i = 0; i < endTimeRow.getChildCount(); i++) {
            TextView v = (TextView) endTimeRow.getChildAt(i);
            if(days[i]) {
                v.setText(endTimes[i].toString());
                v.setBackgroundColor(Color.GREEN);
                final Integer index = i;
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pickTime(passedName, index, false);
                    }
                });
            } else {
                v.setBackgroundColor(Color.RED);
                v.setOnClickListener(null);
            }

        }

        Button chooseContactsButton = (Button) findViewById(R.id.choose_contacts_button);
        chooseContactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ContactsDialogFragment().show(getFragmentManager(), "cont");
            }
        });
        */
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
