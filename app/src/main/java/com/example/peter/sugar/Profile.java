package com.example.peter.sugar;

import android.content.Context;
import java.util.*;
import java.io.*;

/**
 * @author Peter
 */
class Profile implements Comparator<Profile>,Serializable
{
    private Context context;
    /** Writes xml files to the device */
    private static FileWriter xmlWriter = null;
    /** Name of the profile */
    private String name;
    /** Boolean array which stores whether a weekday */
    private boolean days[];
    /** Integer array which stores the beginning hour and beginning minute of the profile*/
    private int startTime[];
    /** Integer array which stores the ending hour and  ending minute of the profile*/
    private int endTime[];
    /** String array which stores each number of the profile */
    private String[] numbers;

    /**
     * Constructor of the Profile
     * @param name represents the name of the profile
     * @param days represents the days of the profile
     * @param startTime represents the start time of the profile
     * @param endTime represents the end time of the profile
     * @param numbers represents the numbers which are associated to the profile
     */
    Profile(String name, boolean days[], int startTime[], int endTime[], String[] numbers)
    {
        this.name = name;
        this.days = days;
        this.startTime = startTime;
        this.endTime = endTime;
        this.numbers = numbers;
        try {
            saveProfile();
        } catch( IOException e ) {
            e.printStackTrace();
        }
    }

    /**
     * Compares both profiles by starting date and sorts an collection of profiles by the specified
     * criteria. In general you use profile "a" as your comparision base.
     * @param a first profile
     * @param b second profile
     * @return FLAG which indicates whether a or b is greater,less or equal ( the FLAG is an integer
     */
    public int compare( Profile a, Profile b )
    {
        // Get starting hour and starting minute of profile "A"
        int profileHoursA = a.getStart()[0];
        int profileMinutesA = a.getStart()[1];
        // Get starting hour and starting minute of profile "B"
        int profileHoursB = b.getStart()[0];
        int profileMinutesB = b.getStart()[1];
        // Define possible cases for the relation of profile "A" and "B"
        boolean aIsGreaterThanb = (( profileHoursA > profileMinutesB ) || ( ( profileHoursA == profileHoursB ) && ( profileMinutesA < profileMinutesB )));
        boolean aIsEqualTob = ( ( profileHoursA == profileHoursB ) && ( profileMinutesA == profileMinutesB ));
        boolean aIsInferiorTob = ( ( profileHoursA < profileHoursB ) || ( ( profileHoursA == profileHoursB ) && ( profileMinutesA < profileMinutesB )));
        // Checks wether the startDate from a is greater than the start date from b
        if(aIsGreaterThanb)
        {
            return 1;
        }
        // Checks whether the startDate form a is equal to the start date of b
        else if (aIsEqualTob)
        {
            return 0;
        }
        // Check wether the startDate from a is smaller to the start date from b
        else if (aIsInferiorTob)
        {
            return -1;
        }
        return 0;
    }

    /**
     * Saves the current profile to a local storage point
     * @return whether file was saved successfully ( true ) or not ( false )
     */
    private boolean saveProfile() throws IOException
    {
        try {
            FileOutputStream fos = new FileOutputStream(context.getFilesDir() + "/" + getName() + ".profile");
        } catch  ( IOException exceptionIo) {
            throw exceptionIo;
        } finally {
            xmlWriter.close();
        }
        return true;
    }

    public String getName()
    {
        return name;
    }

    boolean[] getDays()
    {
        return days;
    }

    int[] getStart()
    {
        return startTime;
    }

    int[] getEnd()
    {
        return endTime;
    }

    String[] getPhoneNumbers()
    {
        return numbers;
    }

    void setName(String updatedName)
    {
        name = updatedName;
    }

    void setDays(boolean[] updatedDays)
    {
        days = updatedDays;
    }

    void setStart( int[] updatedStart )
    {
        startTime = updatedStart;
    }

    void setEnd( int[] updatedEnd )
    {
        endTime = updatedEnd;
    }

}