package com.example.peter.sugar;

/**
 * Created by Peter on 25.04.2017.
 */
public class Profile
{
    private String name;
    private boolean days[];
    private int startTime[];
    private int endTime[];


    public Profile(String name, boolean days[], int startTime[], int endTime[])
    {
        this.name = name;
        this.days = days;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getName()
    {
        return name;
    }

    public boolean[] getDays()
    {
        return days;
    }

    public int[] getStart()
    {
        return startTime;
    }

    public int[] getEnd()
    {
        return endTime;
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