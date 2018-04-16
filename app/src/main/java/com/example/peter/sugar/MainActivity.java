package com.example.peter.sugar;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback {

    public static final String LOG_TAG = "SUGAR";

    public static final String EXTRA_PROFILE_NAME = "profile name";
    public static final String EXTRA_INDEX = "index";
    public static final String EXTRA_IS_START = "isStart";
    public static final String EXTRA_HOUR_OF_DAY = "hour";
    public static final String EXTRA_MINUTE = "minute";

    /**
     * Request code to identify the request for contacts permissions.
     */
    private final int REQUEST_CONTACTS = 1;

    /**
     * Permissions we need to read and write contacts.
     */
    private final String[] PERMISSION_CONTACTS = {Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Concerning runtime permission
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED)
        {
            Log.d(MainActivity.LOG_TAG, "ConAct: Permissions not granted, sending request.");
            //ActivityCompat.requestPermissions(this, PERMISSION_CONTACTS, REQUEST_CONTACTS);
        }else {
            Log.d(LOG_TAG, "Permissions granted");
        }

        // Prompt the user to change the default dialer package to SUGAR. This is necessary to
        // block phone calls.
        Intent changeDialer = new Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER);
        changeDialer.putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, getPackageName());
        startActivity(changeDialer);

        // Here we initialize the monitor for contacts database changes (see ContactsMonitorService).
        // TODO: Add an option to enable or disable this feature.
        /*final int JOB_ID = 1;
        JobScheduler scheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);

        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID,
                new ComponentName(getPackageName(), ContactsMonitorService.class.getName()));
        builder.setPeriodic(1000 * 60 * 15)
                .setPersisted(true);
        JobInfo info = builder.build();

        if(scheduler != null) {
            scheduler.schedule(info);
            Log.d(MainActivity.LOG_TAG, "All pending jobs: " + scheduler.getAllPendingJobs().toString());
        }*/
    }

    // Handling runtime permissions
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults)
    {
        Log.d(MainActivity.LOG_TAG, "ConAct: onRequestPermissionsResult()");
        if (requestCode == REQUEST_CONTACTS) {
            if(verifyPermissions(grantResults)) {int a = 1;}
            else {int b = 2;}
        }
        else
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private boolean verifyPermissions(int[] grantResults) {
        if (grantResults.length < 1)
            return false;

        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED)
                return false;
        }
        return true;
    }

    public void openClosingTimeActivity(View v)
    {
        Intent moveToClosingTimeActivity = new Intent(this,ClosingTimeDisplayActivity.class);
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
        startActivity(moveToListProfilesActivity);
    }
}
