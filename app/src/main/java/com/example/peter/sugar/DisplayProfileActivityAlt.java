package com.example.peter.sugar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DisplayProfileActivityAlt extends AppCompatActivity
        implements ContactsDialogFragment.ContactsSelectedListener {

    private Profile prof;

    private Switch activateProfileSwitch;
    private TableLayout timeTable;
    private LinearLayout blockAllCallsTextBar;
    private CheckBox blockAllCallsCheckbox;
    private TextView profileInactiveView;
    private Button backButton;
    private Button chooseContactsButton;

    String name;
    boolean[] days;
    TimeObject[] startTimes;
    TimeObject[] endTimes;
    boolean active;
    int mode;
    ArrayList<String> numbers;
    ArrayList<String> names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_profile_alt);

        try {
            prof = Profile.readProfileFromXmlFile(getIntent().getStringExtra(MainActivity.KEY_PROFILE_NAME), this);
        } catch(Exception e) {
            Log.e(MainActivity.LOG_TAG, e.toString());
            Toast.makeText(this, R.string.error, Toast.LENGTH_LONG).show();
            finish();
        }

        name = prof.getName();
        days = prof.getDays();
        startTimes = prof.getStart();
        endTimes = prof.getEnd();
        active = prof.isActive();
        mode = prof.getMode();
        numbers = prof.getPhoneNumbers();
        names = prof.getContactNames();

        ((TextView) findViewById(R.id.profile_name_view_id)).setText(name);

        activateProfileSwitch = (Switch) findViewById(R.id.activate_profile_switch_id);
        timeTable = (TableLayout) findViewById(R.id.times_displayer_id);
        blockAllCallsTextBar = (LinearLayout) findViewById(R.id.block_all_calls_text_bar_id);
        blockAllCallsCheckbox = (CheckBox) findViewById(R.id.block_all_calls_checkbox_id);
        profileInactiveView = (TextView) findViewById(R.id.profile_inactive_view_id);
        chooseContactsButton = (Button) findViewById(R.id.choose_contacts_button_id);
        backButton = (Button) findViewById(R.id.back_button_id);


        activateProfileSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(MainActivity.LOG_TAG, "activateProfileSwitch: onCheckedChanged()");
                prof.setActive(isChecked);
                try {
                    prof.saveProfile(DisplayProfileActivityAlt.this);
                    active = prof.isActive();
                } catch(Exception e) {
                    Log.e(MainActivity.LOG_TAG, e.toString());
                    prof.setActive(active);
                    Toast.makeText(DisplayProfileActivityAlt.this, R.string.error, Toast.LENGTH_LONG).show();
                    activateProfileSwitch.setChecked(active); // Caution!!
                }
                if(active) {
                    timeTable.setVisibility(View.VISIBLE);
                    blockAllCallsTextBar.setVisibility(View.VISIBLE);
                    profileInactiveView.setVisibility(View.INVISIBLE);
                    TimeManager mgr = new TimeManager(DisplayProfileActivityAlt.this);
                    mgr.initProfile(prof);
                } else {
                    timeTable.setVisibility(View.INVISIBLE);
                    blockAllCallsTextBar.setVisibility(View.INVISIBLE);
                    profileInactiveView.setVisibility(View.VISIBLE);
                }
            }
        });



        TableRow row;
        TextView view;
        for(int i = 0; i < 7; i++) {
            row = (TableRow) timeTable.getChildAt(i);
            view = (TextView) row.getChildAt(1);
            if(days[i]) {
                view.setText(getString(R.string.from_to, startTimes[i], endTimes[i]));
            } else {
                view.setText(getString(R.string.not_at_all));
            }
        }


        blockAllCallsCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    prof.setMode(Profile.MODE_BLOCK_ALL);
                } else {
                    prof.setMode(Profile.MODE_BLOCK_SELECTED);
                }
                try {
                    prof.saveProfile(DisplayProfileActivityAlt.this);
                    mode = prof.getMode();
                } catch(Exception e) {
                    Log.e(MainActivity.LOG_TAG, e.toString());
                    prof.setMode(mode);
                    blockAllCallsCheckbox.setChecked(mode == Profile.MODE_BLOCK_ALL);
                    Toast.makeText(DisplayProfileActivityAlt.this, R.string.error, Toast.LENGTH_LONG).show();
                }
                if(mode == Profile.MODE_BLOCK_ALL) {
                    chooseContactsButton.setVisibility(View.INVISIBLE);
                } else {
                    chooseContactsButton.setVisibility(View.VISIBLE);
                }
            }
        });



        chooseContactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle(1);
                args.putString(MainActivity.KEY_PROFILE_NAME, name);
                ContactsDialogFragment frag = new ContactsDialogFragment();
                frag.setArguments(args);
                frag.show(getFragmentManager(), "cont");
            }
        });


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        if(active) {
            timeTable.setVisibility(View.VISIBLE);
            blockAllCallsTextBar.setVisibility(View.VISIBLE);
            profileInactiveView.setVisibility(View.INVISIBLE);
            if(mode == Profile.MODE_BLOCK_ALL) {
                chooseContactsButton.setVisibility(View.INVISIBLE);
            } else {
                chooseContactsButton.setVisibility(View.VISIBLE);
            }
        } else {
            timeTable.setVisibility(View.INVISIBLE);
            blockAllCallsTextBar.setVisibility(View.INVISIBLE);
            profileInactiveView.setVisibility(View.VISIBLE);
        }

        blockAllCallsCheckbox.setChecked(mode == Profile.MODE_BLOCK_ALL);
        activateProfileSwitch.setChecked(active);
    }

    @Override
    public void onContactsSelected(ArrayList<String> newNumbers, ArrayList<String> newNames) {
        prof.setPhoneNumbers(newNumbers);
        prof.setContactNames(newNames);
        try {
            prof.saveProfile(this);
            numbers = newNumbers;
            names = newNames;
        } catch(Exception e) {
            Log.e(MainActivity.LOG_TAG, e.toString());
            prof.setPhoneNumbers(numbers);
            prof.setContactNames(names);
            Toast.makeText(this, R.string.error, Toast.LENGTH_LONG).show();
        }
    }
}
