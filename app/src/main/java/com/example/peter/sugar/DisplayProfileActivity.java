package com.example.peter.sugar;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Peter on 09.07.2017.
 */

public class DisplayProfileActivity extends AppCompatActivity implements ContactsDialogFragment.ContactsSelectedListener ,TimePickerDialog.OnTimeSetListener
{
    private Profile currentProfile = null;
    private ListView phoneNumberList;
    private ArrayAdapter<String> numberListContent;
    private int selectedWeekDay = -1;
    //private TextView numbersView;

    protected void onCreate(Bundle savedInstances)
    {
        super.onCreate(savedInstances);
        setContentView(R.layout.ias);

        try {
            currentProfile = Profile.readProfileFromXmlFile(getIntent().getStringExtra("profileName"), getApplicationContext());
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        /*
        GridView gridContent = (GridView) findViewById(R.id.gridView);
        TimeObject[] startTimes = currentProfile.getStart();
        TimeObject[] endTImes = currentProfile.getEnd();
        String startTimesAsString[] = new String[startTimes.length];
        for( int currentTime = 0; currentTime < startTimes.length; currentTime++ )
        {
            startTimesAsString[currentTime] = startTimes[currentTime].toString();
        }

        gridContent.setAdapter(new ArrayAdapter(this,android.R.layout.simple_list_item_1,startTimesAsString));
        gridContent.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent,View v,int position,long id)
            {
                switch(position)
                {
                    case 0 :
                    {
                        selectedWeekDay = 0;
                        Toast.makeText(getApplicationContext(),"You have clicked on Monday!",Toast.LENGTH_SHORT).show();
                        TimePickerFragment test = new TimePickerFragment();
                        test.show(getFragmentManager(),"datePicker");
                        break;
                    }
                    case 1 :
                    {
                        selectedWeekDay = 1;
                        Toast.makeText(getApplicationContext(),"You have clicked on Tuesday!",Toast.LENGTH_SHORT).show();
                        TimePickerFragment test = new TimePickerFragment();
                        test.show(getFragmentManager(),"datePicker");
                        break;
                    }
                    case 2 :
                    {
                        selectedWeekDay = 2;
                        Toast.makeText(getApplicationContext(),"You have clicked on Wednesday!",Toast.LENGTH_SHORT).show();
                        TimePickerFragment test = new TimePickerFragment();
                        test.show(getFragmentManager(),"datePicker");
                        break;
                    }
                    case 3 :
                    {
                        selectedWeekDay = 3;
                        Toast.makeText(getApplicationContext(),"You have clicked on Thursday!",Toast.LENGTH_SHORT).show();
                        TimePickerFragment test = new TimePickerFragment();
                        test.show(getFragmentManager(),"datePicker");
                        break;
                    }
                    case 4 :
                    {
                        selectedWeekDay = 4;
                        Toast.makeText(getApplicationContext(),"You have clicked on Friday!",Toast.LENGTH_SHORT).show();
                        TimePickerFragment test = new TimePickerFragment();
                        test.show(getFragmentManager(),"datePicker");
                        break;
                    }
                    case 5 :
                    {
                        selectedWeekDay = 5;
                        Toast.makeText(getApplicationContext(),"You have clicked on Saturday!",Toast.LENGTH_SHORT).show();
                        TimePickerFragment test = new TimePickerFragment();
                        test.show(getFragmentManager(),"datePicker");
                        break;
                    }
                    case 6 :
                    {
                        selectedWeekDay = 6;
                        Toast.makeText(getApplicationContext(),"You have clicked on Sunday!",Toast.LENGTH_SHORT).show();
                        TimePickerFragment test = new TimePickerFragment();
                        test.show(getFragmentManager(),"datePicker");
                        break;
                    }
                }
            }
        }); */

        TextView profileNameDisplayerTextView = (TextView) findViewById(R.id.profileName);
        profileNameDisplayerTextView.setText(currentProfile.getName());
        profileNameDisplayerTextView.setTextSize(24.0f);
        profileNameDisplayerTextView.setGravity(Gravity.CENTER);
        phoneNumberList = (ListView) findViewById(R.id.list);
        numberListContent = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,currentProfile.getContactNames());
        phoneNumberList.setAdapter(numberListContent);
        if( currentProfile.isAllowed() )
            findViewById(R.id.allowedDisplay).setBackgroundColor(Color.GREEN);
        else
            findViewById(R.id.allowedDisplay).setBackgroundColor(Color.RED);

    }

    protected void displayContactsChooser(View w)
    {
        try {
            ContactsDialogFragment testFragment = new ContactsDialogFragment();
            Bundle profileBundle = new Bundle();
            profileBundle.putString(MainActivity.KEY_PROFILE_NAME,currentProfile.getName());
            testFragment.setArguments(profileBundle);
            testFragment.show(getFragmentManager(),"dialog");
        } catch ( Exception e ) {
            Log.d(MainActivity.LOG_TAG,e.getMessage());
        }
    }

    @Override
    public void onContactsSelected(ArrayList<String> numbers,ArrayList<String> names)
    {
        currentProfile.setPhoneNumbers(numbers);
        currentProfile.setContactNames(names);
        try {
            currentProfile.saveProfile(this);
        } catch (Exception e) {
            Log.e(MainActivity.LOG_TAG, e.toString());
        }
        numberListContent = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,names);
        phoneNumberList = (ListView) findViewById(R.id.list);
        phoneNumberList.setAdapter(numberListContent);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute)
    {
        TimeObject updatedTimeObjectList[] = new TimeObject[currentProfile.getDays().length];
    }

    /* This function shall */
    public void onTableColumnClick()
    {

    }
}
