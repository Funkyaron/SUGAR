package com.example.peter.sugar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

/**
 * Created by Peter on 09.07.2017.
 */

class DisplayProfileActivity extends AppCompatActivity
{
    protected void onCreate(Bundle savedInstances)
    {
        super.onCreate(savedInstances);
        setContentView(R.layout.displayprofileactivity);

        TextView tv = (TextView) findViewById(R.id.profileName);
        Intent currentIntent = getIntent();
        String name = currentIntent.getStringExtra("name");
        tv.setBackgroundColor(Color.GREEN);
        tv.setText(name);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(25.0f);
    }

    protected void displayContactsChooser()
    {
        android.app.FragmentTransaction testTransaction = getFragmentManager().beginTransaction();
        ContactsDialogFragment testFragment = new ContactsDialogFragment();
    }
}
