package com.example.peter.sugar;

import java.text.DecimalFormat;

/**
 * Created by Peter on 16.05.2017.
 */

public class TimeObject {

    private int hour;
    private int minute;

    public TimeObject(int hour,int minute)
    {
        this.hour = hour;
        this.minute = minute;
    }

    // The following constructor requires a String in the format 18:30
    public TimeObject(String time) {
        String[] timeObjectDescription = time.split(":");
        hour = Integer.parseInt(timeObjectDescription[0]);
        minute = Integer.parseInt(timeObjectDescription[1]);
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

    public String toString() {
        DecimalFormat form = new DecimalFormat("00");
        return "" + hour + ":" + form.format(minute) + " Uhr";
    }


}
