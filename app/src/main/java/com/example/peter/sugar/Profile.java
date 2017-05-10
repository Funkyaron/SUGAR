package com.example.peter.sugar;

import android.content.Context;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.util.*;
import java.io.*;

/**
 * @author Peter
 */
class Profile implements Serializable
{
    private Context context;
    private String name;
    private boolean days[];
    private int startTime[];
    private int endTime[];
    private ArrayList<String> numbers;

    Profile(String name, boolean days[], int startTime[], int endTime[], ArrayList<String> numbers)
    {
        this.name = name;
        this.days = days;
        this.startTime = startTime;
        this.endTime = endTime;
        this.numbers = numbers;
    }

    public String toString()
    {
        String result = "";
        result = result + "Name : " + getName() + " \n";
        for( int currentDay = 0; currentDay < getDays().length; currentDay++ )
        {
            if( getDays()[currentDay] == true )
                result = result + "{True}";
            else if ( getDays()[currentDay] == false )
                result = result + "{False}";
        }
        result = result + " \n";
        result = result + "StartTime : " + getStart()[0] + ":" + getStart()[1] + " \n";
        result = result + "EndTime : " + getEnd()[0] + ":" + getEnd()[1] + " \n";
        result = result + "Numbers : ";
        for(ListIterator<String> iterator = getPhoneNumbers().listIterator();iterator.hasNext();)
        {
            result = result + iterator.next() + " \n";
        }
        return result;
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

    ArrayList<String> getPhoneNumbers()
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