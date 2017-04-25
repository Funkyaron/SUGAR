package com.example.peter.sugar;

/**
 * Created by Peter on 25.04.2017.
 */
public class Profile
{
    private String profileName;
    private boolean days[];
    private int startTime[];
    private int endTime[];


    public Profile(String conProfileName, boolean con_days[], int startTime[], int endTime[])
    {
        this.profileName = conProfileName;
        this.days = con_days;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}