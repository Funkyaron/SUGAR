package com.example.peter.sugar;

import java.util.Calendar;

/**
 * Created by Peter on 04.04.2017.
 */

class Profile {

    /* Profile attributes */
    private int profileId;
    private String profileName;
    private Calendar profileStartPoint;
    private Calendar profileEndPoint;
    private String[] profileContacts;
    private boolean profileIsActive;

    /* Constructor which gets it's data from the SQL database */
    public Profile(int c_id,String c_name,Calendar c_sdate,Calendar c_edate,
                   String c_contacts, boolean c_active)
    {
        this.profileId = c_id;
        this.profileName = c_name;
        this.profileStartPoint = c_sdate;
        this.profileEndPoint = c_edate;
        this.profileContacts = c_contacts.split(",");
        this.profileIsActive = c_active;
    }

    /* Returns the ID of the profile */
    int getProfileId()
    {
        return profileId;
    }

    /* Returns the name of the profile */
    String getProfileName()
    {
        return profileName;
    }

    /* Returns the starting point of the profile */
    Calendar getProfileStartPoint()
    {
        return profileStartPoint;
    }

    /* Returns the end point of the profile */
    Calendar getProfileEndPoint()
    {
        return profileEndPoint;
    }

    /* Returns an array with all contacts which are contained inside of the profile */
    String[] getContacts()
    {
        return profileContacts;
    }

    /* Returns wether the profile is active or not */
    boolean getProfileActive()
    {
        return profileIsActive;
    }

    /* Translate to SQL values */
    String translateToSQL()
    {
        return "INSERT INTO PROFILE VALUES " + "(" + getProfileName() + ",";
    }
}
