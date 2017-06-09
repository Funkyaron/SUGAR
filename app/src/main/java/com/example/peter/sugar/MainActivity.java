package com.example.peter.sugar;

import android.Manifest;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback,
        ContactsDialogFragment.ContactsSelectedListener {

    public static final String LOG_TAG = "SUGAR";
    public static final String KEY_PROFILE_NAME = "profile name";

    /**
     * Request code to identify the request for contacts permissions.
     */
    private final int REQUEST_CONTACTS = 1;

    /**
     * Permissions we need to read and write contacts.
     */
    private final String[] PERMISSION_CONTACTS = {Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS};

    private TextView numbersView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        numbersView = (TextView) findViewById(R.id.numbers_view);

        // Concerning runtime permissions
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED)
        {
            Log.d(MainActivity.LOG_TAG, "ConAct: Permissions not granted, sending request.");
            ActivityCompat.requestPermissions(this, PERMISSION_CONTACTS, REQUEST_CONTACTS);
        } else {
            Log.d(LOG_TAG, "Permissions granted");
        }

        Log.d(LOG_TAG, "getFilesDir(): " + getFilesDir());







        Button contactsButton = (Button) findViewById(R.id.contacts_button_id);
        contactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new ContactsDialogFragment();
                try {
                    newFragment.show(getFragmentManager(), "contacts");
                } catch (Exception e) {
                    Log.e(LOG_TAG, "MainActivity: " + e.toString());
                }
            }
        });

    }

    @Override
    public void onContactsSelected(ArrayList<String> numbers) {
        StringBuilder builder = new StringBuilder();
        for(String number : numbers) {
            builder.append(number);
            builder.append("\n");
        }
        String allNumbers = builder.toString();
        numbersView.setText(allNumbers);
    }

    // Handling runtime permissions
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults)
    {
        Log.d(MainActivity.LOG_TAG, "ConAct: onRequestPermissionsResult()");
        if (requestCode == REQUEST_CONTACTS)
        {
            if(verifyPermissions(grantResults))
                Toast.makeText(this, "Berechtigungen genehmigt", Toast.LENGTH_LONG).show();
            else
            {
                Toast.makeText(this, "Berechtigungen nicht genehmigt", Toast.LENGTH_LONG).show();
            }
        }
        else
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private boolean verifyPermissions(int[] grantResults)
    {
        if (grantResults.length < 1)
            return false;

        for (int result : grantResults)
        {
            if (result != PackageManager.PERMISSION_GRANTED)
                return false;
        }

        return true;
    }

    private void logBlockList(BlockList blockList) {
        ArrayList<String> blockedNumbers = blockList.getBlockedNumbers();
        Log.d(LOG_TAG, "That's the blocklist:");

        for (String number : blockedNumbers) {
            Log.d(LOG_TAG, number);
        }
    }

}
