package com.example.peter.sugar;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback {

    public static final String LOG_TAG = "SUGAR";
    public static final String EXTRA_PROFILE_NAME = "profile name";
    public static final String EXTRA_INDEX = "index";
    public static final String EXTRA_IS_START = "isStart";
    String[] closingTimes = new String[7];
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
    private RelativeLayout rootLayout;
    private Profile mProfile;

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu items for the use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.simplemenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // numbersView = (TextView) findViewById(R.id.numbers_view)
        // Concerning runtime permission
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED)
        {
            Log.d(MainActivity.LOG_TAG, "ConAct: Permissions not granted, sending request.");
            ActivityCompat.requestPermissions(this, PERMISSION_CONTACTS, REQUEST_CONTACTS);
        }else {
            Log.d(LOG_TAG, "Permissions granted");
        }

        rootLayout = (RelativeLayout) findViewById(R.id.root_layout);
        rootLayout.setOnTouchListener(new View.OnTouchListener() {
           public boolean onTouch(View v,MotionEvent event)
           {
               Toast onTouchMessage;
               if( event.getActionMasked() == MotionEvent.ACTION_DOWN )
               {
                   Random colorGenerator = new Random();
                   rootLayout.setBackgroundColor(Color.argb(255,colorGenerator.nextInt(256),colorGenerator.nextInt(256),colorGenerator.nextInt(256)));
                   onTouchMessage = Toast.makeText(getApplicationContext(),"Boop :)",Toast.LENGTH_LONG);
                   onTouchMessage.show();
                   return true;
               } else if ( event.getActionMasked() == MotionEvent.ACTION_UP ) {
                   onTouchMessage = Toast.makeText(getApplicationContext(),"Boop :(",Toast.LENGTH_LONG);
                   onTouchMessage.show();
                   return true;
               }
               return false;
           }
        });
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

    public void openClosingTimeActivity(View v)
    {
        Intent moveToClosingTimeActivity = new Intent(this,ClosingTimeDisplayActivity.class);
        if( new File(this.getFilesDir()+"closingTimes.xml").exists())
        {
            Log.d(MainActivity.LOG_TAG,"Now reading all closing times ... \n");
        }
        startActivity(moveToClosingTimeActivity);
    }

    public void openDontDisturbActivity(View v)
    {
        Intent moveToDontDisturbeActivity = new Intent(this,DoNotDisturbActivity.class);
        startActivity(moveToDontDisturbeActivity);
    }

    public void openListProfilesActivity(View v)
    {
        Intent moveToListProfilesActivity = new Intent(this,ListProfilesActivity.class);
        Log.d(MainActivity.LOG_TAG,"Opening ListProfilesActivity ...");
        startActivity(moveToListProfilesActivity);
    }
}
