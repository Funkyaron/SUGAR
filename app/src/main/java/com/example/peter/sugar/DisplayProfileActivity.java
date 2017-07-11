package com.example.peter.sugar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peter on 09.07.2017.
 */

class DisplayProfileActivity extends AppCompatActivity implements ContactsDialogFragment.ContactsSelectedListener
{
    private Profile currentProfile = null;
    private TextView numbersView;

    protected void onCreate(Bundle savedInstances)
    {
        super.onCreate(savedInstances);
        setContentView(R.layout.displayprofileactivity);

        TableLayout rootTable = (TableLayout) findViewById(R.id.root_table);
        try {
            currentProfile = Profile.readProfileFromXmlFile(getIntent().getStringExtra("profileName"), getApplicationContext());
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        TextView profileNameDisplayerTextView = (TextView) findViewById(R.id.profileName);
        profileNameDisplayerTextView.setText(currentProfile.getName());
        profileNameDisplayerTextView.setBackgroundColor(Color.GREEN);
        profileNameDisplayerTextView.setTextSize(24.0f);
        profileNameDisplayerTextView.setGravity(Gravity.CENTER);
        for( int currentWeekDay = 0; currentWeekDay < rootTable.getChildCount(); currentWeekDay++ )
        {
            TableRow currentRow = (TableRow) rootTable.getChildAt(currentWeekDay);
            TextView modifiedColumn = (TextView) currentRow.getChildAt(1);
            int startHours = currentProfile.getStart()[currentWeekDay].getHour();
            int startMinutes = currentProfile.getStart()[currentWeekDay].getMinute();
            int endHours = currentProfile.getEnd()[currentWeekDay].getHour();
            int endMinutes = currentProfile.getEnd()[currentWeekDay].getMinute();
            modifiedColumn.setText(startHours+":"+startMinutes+" - "+endHours+":"+endMinutes);
        }
        ListView phoneNumberList = (ListView) findViewById(R.id.list);
        ArrayAdapter<String> numberListContent = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,currentProfile.getPhoneNumbers());
        phoneNumberList.setAdapter(numberListContent);
        if( currentProfile.isAllowed() )
            findViewById(R.id.isActiveDisplay).setBackgroundColor(Color.GREEN);
        else
            findViewById(R.id.isActiveDisplay).setBackgroundColor(Color.RED);

    }

    protected void displayContactsChooser(View w)
    {
        try {
            android.app.FragmentTransaction testTransaction = getFragmentManager().beginTransaction();
            ContactsDialogFragment testFragment = new ContactsDialogFragment();
            Bundle profileBundle = new Bundle();
            profileBundle.putString(MainActivity.KEY_PROFILE_NAME,currentProfile.getName());
            testFragment.show(getFragmentManager(),"dialog");
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    @Override
    public void onContactsSelected(ArrayList<String> numbers) {
        currentProfile.setPhoneNumbers(numbers);
        try {
            currentProfile.saveProfile(this);
        } catch (Exception e) {
            Log.e("DisplayProfileActivity:", e.toString());
        }
        numbersView.setText(currentProfile.toString());
    }
}
