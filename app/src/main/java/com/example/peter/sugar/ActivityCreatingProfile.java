package com.example.peter.sugar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import java.util.ArrayList;

/**
 *
 */

public class ActivityCreatingProfile extends AppCompatActivity
{
    private Profile underlyingProfile;
    private String name;
    private boolean[] days;
    private TimeObject[] startTimes;
    private TimeObject[] endTimes;
    private boolean isActive;
    private boolean isAllowed;
    private int mode;
    private ArrayList<String> phoneNumbers;
    private ArrayList<String> contactsName;

    @Override
    protected void onCreate(Bundle savedInstances)
    {
        super.onCreate(savedInstances);
        name = "";
        days = new boolean[7];

        /* Set everyday to 'false' inside the 'days' array */
        for(int currDay = 0; currDay < 7; currDay++ )
        {
            days[currDay] = false;
        }

        startTimes = new TimeObject[7];

        /* Fill every entry in 'startTimes' with a default timeobject */
        for(int currDay = 0; currDay < 7; currDay++ )
        {
            startTimes[currDay] = new TimeObject(0,0);
        }

        endTimes = new TimeObject[7];

        /* Fill every entry in 'endTimes' with a default TimeObject */
        for(int currDay = 0;currDay < 7; currDay++ )
        {
            endTimes[currDay] = new TimeObject(23,59);
        }

        isActive = false;
        isAllowed = true;
        mode = Profile.MODE_BLOCK_NOT_SELECTED;
        phoneNumbers = new ArrayList<String>(0);
        contactsName = new ArrayList<String>(0);
        underlyingProfile = new Profile(name,days,startTimes,endTimes,isActive,isAllowed,mode,phoneNumbers,contactsName);
    }

    public Profile getProfile()
    {
        return underlyingProfile;
    }

    public void setProfile(Profile updatedProfile)
    {
        this.underlyingProfile = updatedProfile;
    }
}
