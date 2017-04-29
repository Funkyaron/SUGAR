package com.example.peter.sugar;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.*;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = "SUGAR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(LOG_TAG, "getFilesDir(): " + getFilesDir());

        TestXmlWriter tester = new TestXmlWriter();

        try {
            tester.writeTestProfile(this);
        } catch (IOException e) {
            Log.e(LOG_TAG, e.toString());
        }

        Profile prof = null;
        try {
            prof = tester.readTestProfile(this);
        } catch (IOException e) {
            Log.e(LOG_TAG, e.toString());
        } catch (XmlPullParserException e) {
            Log.e(LOG_TAG, e.toString());
        }

        try {
            ProfileUpdateUtil.setNextEnable(this, prof);
            ProfileUpdateUtil.setNextDisable(this, prof);
            ProfileUpdateUtil.updateProfileStatus(this, prof);
        } catch (NullPointerException e) {
            Log.e(LOG_TAG, e.toString());
        }

    }
}
