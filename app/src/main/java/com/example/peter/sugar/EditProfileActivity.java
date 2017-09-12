package com.example.peter.sugar;

import android.app.DialogFragment;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class EditProfileActivity extends AppCompatActivity {

    private Profile prof = null;

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

        boolean[] days = prof.getDays();
        TimeObject[] startTimes = prof.getStart();
        TimeObject[] endTimes = prof.getEnd();

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
                    TextView view1 = (TextView) daysRow.getChildAt(index);
                }
            });
        }

        Log.d(MainActivity.LOG_TAG, "Children of startTimeRow: " + startTimeRow.getChildCount());

        for(int i = 0; i < startTimeRow.getChildCount(); i++) {
            TextView v = (TextView) startTimeRow.getChildAt(i);
            if(days[i]) {
                v.setText(startTimes[i].toString());
                v.setBackgroundColor(Color.GREEN);
            } else {
                v.setBackgroundColor(Color.RED);
            }
            final Integer index = i;
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle args = new Bundle();
                    args.putString(MainActivity.EXTRA_PROFILE_NAME, passedName);
                    args.putInt(MainActivity.EXTRA_INDEX, index);
                    args.putBoolean(MainActivity.EXTRA_IS_START, true);
                    DialogFragment newFragment = new TimePickerFragment();
                    newFragment.setArguments(args);
                    newFragment.show(getFragmentManager(), "start" + index);
                }
            });
        }

        for(int i = 0; i < endTimeRow.getChildCount(); i++) {
            TextView v = (TextView) endTimeRow.getChildAt(i);
            if(days[i]) {
                v.setText(endTimes[i].toString());
                v.setBackgroundColor(Color.GREEN);
            } else {
                v.setBackgroundColor(Color.RED);
            }
            final Integer index = i;
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle args = new Bundle();
                    args.putString(MainActivity.EXTRA_PROFILE_NAME, passedName);
                    args.putInt(MainActivity.EXTRA_INDEX, index);
                    args.putBoolean(MainActivity.EXTRA_IS_START, false);
                    DialogFragment newFragment = new TimePickerFragment();
                    newFragment.setArguments(args);
                    newFragment.show(getFragmentManager(), "end" + index);
                }
            });
        }

        /*
        LinearLayout ll = (LinearLayout) findViewById(R.id.edit_layout);
        TextView profileName = (TextView) findViewById(R.id.profile_name);
        profileName.setText(getIntent().getExtras().getString("profileName"));
        TimeTable testTable = new TimeTable(this);
        try {
            testTable.convertProfileToTimeTable(Profile.readProfileFromXmlFile(passedName,this));
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        ll.addView(testTable);
        */
    }

    @Override
    public void onDestroy() {
        try {
            //prof.saveProfile(this);
        } catch(Exception e) {
            Log.e(MainActivity.LOG_TAG, e.toString());
        }
        super.onDestroy();
    }
}
