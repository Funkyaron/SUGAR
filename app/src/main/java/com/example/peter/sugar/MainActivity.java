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

public class MainActivity extends AppCompatActivity
    implements ActivityCompat.OnRequestPermissionsResultCallback {

    public static final String LOG_TAG = "SUGAR";

    private final int REQUEST_VIBRATE = 1;
    private final int REQUEST_WRITE_SETTINGS = 2;

    private final String[] PERMISSION_VIBRATE = {Manifest.permission.VIBRATE};
    private final String[] PERMISSION_WRITE_SETTINGS = {Manifest.permission.WRITE_SETTINGS};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(LOG_TAG, "getFilesDir(): " + getFilesDir());



        Button contactsButton = (Button) findViewById(R.id.contacts_button_id);
        contactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ContactsActivity.class);
                startActivity(i);
            }
        });

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults)
    {
        if(requestCode == REQUEST_VIBRATE)
        {
            // Do something?
        }
        else
        {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}
