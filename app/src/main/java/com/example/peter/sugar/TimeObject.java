package com.example.peter.sugar;

/**
 * Created by Peter on 16.05.2017.
 */

public class TimeObject {

    private int hour;
    private int minute;

    public TimeObject(int conHour,int conMinute)
    {
        setHour(conHour);
        setMinute(conMinute);
    }

    public int getHour()
    {
        return hour;
    }

    public int getMinute()
    {
        return minute;
    }

    public void setHour( int updatedHour )
    {
        hour = updatedHour;
    }

    public void setMinute( int updatedMinute )
    {
        minute = updatedMinute;
    }


}
