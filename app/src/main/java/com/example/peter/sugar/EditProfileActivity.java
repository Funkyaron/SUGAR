package com.example.peter.sugar;

import android.app.DialogFragment;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class EditProfileActivity extends AppCompatActivity {

    static Profile prof = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        final String passedName = getIntent().getExtras().getString("profileName");
        try {
            prof = Profile.readProfileFromXmlFile(passedName, this);
        } catch(Exception e) {
            Log.e(MainActivity.LOG_TAG, e.toString());
            finish();
            return;
        }

        final boolean[] days = prof.getDays();
        final TimeObject[] startTimes = prof.getStart();
        final TimeObject[] endTimes = prof.getEnd();

        TextView profileNameView = (TextView) findViewById(R.id.profile_name);

        final TableRow daysRow = (TableRow) findViewById(R.id.days_row);
        final TableRow startTimeRow = (TableRow) findViewById(R.id.start_time_row);
        final TableRow endTimeRow = (TableRow) findViewById(R.id.end_time_row);

        profileNameView.setText(passedName);

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
    }

    @Override
    public void onDestroy() {
        try {
            prof.saveProfile(this);
        } catch(Exception e) {
            Log.e(MainActivity.LOG_TAG, e.toString());
        }
        if(prof.isActive()) {
            TimeManager mgr = new TimeManager(this);
            mgr.initProfile(prof);
        }
        super.onDestroy();
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
