package com.example.peter.sugar;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Peter on 09.07.2017.
 */

class DisplayProfileActivity extends AppCompatActivity
{

    //Bundle passedBundle = getIntent().getExtras();
    protected void onCreate(Bundle savedInstances)
    {
        super.onCreate(savedInstances);
        setContentView(R.layout.displayprofileactivity);
    }

    protected void displayContactsChooser()
    {
        android.app.FragmentTransaction testTransaction = getFragmentManager().beginTransaction();
        ContactsDialogFragment testFragment = new ContactsDialogFragment();
    }
}
