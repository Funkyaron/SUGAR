package com.example.peter.sugar;

import android.Manifest;
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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = "SUGAR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(LOG_TAG, "getFilesDir(): " + getFilesDir());


        TestXmlWriter writer = new TestXmlWriter();
        try {
            writer.writeTestBlockList(this);
        } catch (Exception e) {
            Log.e(LOG_TAG, "MainActivity: " + e.toString());
        }

        BlockList testBlockList = new BlockList(this);
        Log.d(LOG_TAG, "Created BlockList.");
        logBlockList(testBlockList);

        String[] numbers = {"+4917635183695", "017635183695"};
        Profile testProfile = new Profile(this);
        testProfile.addNumbers(numbers);
        try {
            testBlockList.addProfile(testProfile, this);
            testBlockList.addProfile(testProfile, this);
        } catch (Exception e) {
            Log.e(LOG_TAG, "addProfile(): " + e.toString());
        }

        Log.d(LOG_TAG, "Added test profile to blocklist.");
        logBlockList(testBlockList);

        try {
            testBlockList.removeProfile(testProfile, this);
        } catch (Exception e) {
            Log.e(LOG_TAG, "removeProfile(): " + e.toString());
        }

        Log.d(LOG_TAG, "Removed test profile from blocklist");
        logBlockList(testBlockList);




        Button contactsButton = (Button) findViewById(R.id.contacts_button_id);
        contactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ContactsActivity.class);
                startActivity(i);
            }
        });

    }

    private void logBlockList(BlockList blockList) {
        ArrayList<String> blockedNumbers = blockList.getBlockedNumbers();
        Log.d(LOG_TAG, "That's the blocklist:");

        for (String number : blockedNumbers) {
            Log.d(LOG_TAG, number);
        }
    }

}
