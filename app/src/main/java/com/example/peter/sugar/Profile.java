package com.example.peter.sugar;

import java.util.Date;

/**
 * Created by Peter on 04.04.2017.
 */

class Profile {

    /* Profile attributes */
    private int profileId;
    private String profileName;
    private Date profileStartPoint;
    private Date profileEndPoint;
    private String[] profileContacts;
    private boolean profileIsActive;

    /* Constructor which gets it's data from the SQL database */
    public Profile(int c_id,String c_name,Date c_sdate,Date c_edate,String c_contacts, boolean c_active)
    {
        this.profileId = c_id;
        this.profileName = c_name;
        this.profileStartPoint = c_sdate;
        this.profileEndPoint = c_edate;
        this.profileContacts = c_contacts.split(",");
        this.profileIsActive = c_active;
    }

    /* Returns the ID of the profile */
    public int getProfileId()
    {
        return profileId;
    }

    /* Returns the name of the profile */
    public String getProfileName()
    {
        return profileName;
    }

    /* Returns the starting point of the profile */
    public Date getProfileStartPoint()
    {
        return profileStartPoint;
    }

    /* Returns the end point of the profile */
    public Date getProfileEndPoint()
    {
        return profileEndPoint;
    }

    /* Returns an array with all contacts which are contained inside of the profile */
    public String[] getContacts()
    {
        return profileContacts;
    }

    /* Returns wether the profile is active or not */
    public boolean getProfileActive()
    {
        return profileIsActive;
    }
}
