package com.example.peter.sugar;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import org.xmlpull.v1.XmlPullParserException;

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = "SUGAR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(LOG_TAG, "getFilesDir(): " + getFilesDir());

        String name = "Test";
        boolean[] days = {true, true, true, true, true, false, false};
        int[] startTime = {14, 30};
        int[] endTime = {18, 15};
        ArrayList<String> numbers = new ArrayList<String>();
        numbers.add("+4917635183695");
        numbers.add("+49177222222");
        Profile prof = new Profile(
                name,
                days,
                startTime,
                endTime,
                numbers,
                this
        );

        try {
            prof.saveProfile();
            Log.d(LOG_TAG, "Profile saved");
        } catch (IOException e) {
            e.printStackTrace();
        }

        File testProFile = new File(this.getFilesDir() + "/" + name + ".xml");
        if (testProFile.exists()) {
            Log.d(LOG_TAG, testProFile.getAbsolutePath() + " exists");
        }

        Profile prof2 = Profile.readProfileFromXmlFile(prof.getName(), this);


        try {
            ProfileUpdateUtil.setNextEnable(this, prof2);
            ProfileUpdateUtil.setNextDisable(this, prof2);
            ProfileUpdateUtil.updateProfileStatus(this, prof2);
        } catch (NullPointerException e) {
            Log.e(LOG_TAG, e.toString());
        }

        Button contactsButton = (Button) findViewById(R.id.contacts_button_id);
        contactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ContactsActivity.class);
                startActivity(i);
            }
        });

    }
}
