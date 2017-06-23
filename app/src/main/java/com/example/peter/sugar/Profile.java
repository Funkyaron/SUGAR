package com.example.peter.sugar;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.util.*;
import java.io.*;

/**
 * @author Peter
 */
class Profile implements Serializable
{
    private String name;
    private boolean[] days;
    private TimeObject[] startTime;
    private TimeObject[] endTime;
    private ArrayList<String> numbers;
    private final String[] weekDays = { "Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};

    Profile(String name, boolean[] days, TimeObject[] startTime, TimeObject[] endTime, ArrayList<String> numbers)
    {
        this.name = name;
        this.days = days;
        this.startTime = startTime;
        this.endTime = endTime;
        this.numbers = numbers;
    }

    Profile()
    {
        name = "GenericProfile";
        days = new boolean[7];
        for(int i = 0; i < 7; i++)
        {
            days[i] = false;
        }
        startTime = new TimeObject[7];
        for( int i = 0; i < 7; i++ )
        {
            startTime[i] = new TimeObject(0,0);
        }
        endTime = new TimeObject[7];
        for( int i = 0; i < 7; i++ )
        {
            endTime[i] = new TimeObject(0,0);
        }
        numbers = new ArrayList<String>(0);
        numbers.add("Pseudonumber");
    }

    /**
     *
     * @param
     * @return null if no profile is found
     */
    public static Profile readProfileFromXmlFile(String name, Context context)
            throws IOException,XmlPullParserException
    {
        Log.d(MainActivity.LOG_TAG, "Profile: readProfileFromXmlFile()");
        FileInputStream fileInput = context.openFileInput(name + ".xml");

        try {
            ProfileParser parser = new ProfileParser();
            return parser.parse(fileInput, context);
        } finally {
            fileInput.close();
        }
    }

    public static Profile readProfileFromXmlFile(File file, Context context)
            throws IOException, XmlPullParserException
    {
        FileInputStream fileInput = new FileInputStream(file);

        try {
            ProfileParser parser = new ProfileParser();
            return parser.parse(fileInput, context);
        } finally {
            fileInput.close();
        }
    }

    /**
     * Saves the given Profile to the default filepath of Android
     * @throws IOException if problems occured during the function execution
     */
    public void saveProfile(Context context) throws IOException {
        Log.d(MainActivity.LOG_TAG, "Profile: saveProfile()");

        File file = new File(context.getFilesDir() + "/" + name + ".xml");
        if(file.exists())
            file.delete();

        FileOutputStream fileOutput = context.openFileOutput(name + ".xml",Context.MODE_PRIVATE);
        XmlSerializer xmlWriter = Xml.newSerializer();
        try {
            xmlWriter.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output",true);
            xmlWriter.setOutput(fileOutput, "UTF-8");
            xmlWriter.startDocument(null, true);

            xmlWriter.startTag(null,"profile");

            xmlWriter.startTag(null, "name");
            xmlWriter.text(name);
            xmlWriter.endTag(null, "name");

            xmlWriter.startTag(null, "days");
            StringBuilder builder = new StringBuilder();
            for ( int currentDay = 0; currentDay < days.length; currentDay++ )
            {
                if( days[currentDay] == true )
                    builder.append("1");
                else
                    builder.append("0");
            }
            xmlWriter.text(builder.toString());
            xmlWriter.endTag(null,"days");

            xmlWriter.startTag(null,"startTime");
            for(int currentDay = 0; currentDay < startTime.length; currentDay++ )
            {
                if( currentDay == startTime.length - 1)
                    xmlWriter.text(startTime[currentDay].getHour() + ":" + startTime[currentDay].getMinute());
                else
                    xmlWriter.text(startTime[currentDay].getHour() + ":" + startTime[currentDay].getMinute() + "," );
            }
            xmlWriter.endTag(null,"startTime");

            xmlWriter.startTag(null,"endTime");
            for(int currentDay = 0; currentDay < endTime.length; currentDay++ )
            {
                if( currentDay == endTime.length - 1)
                    xmlWriter.text(endTime[currentDay].getHour() + ":" + endTime[currentDay].getMinute());
                else
                    xmlWriter.text(endTime[currentDay].getHour() + ":" + endTime[currentDay].getMinute() + ",");
            }
            xmlWriter.endTag(null,"endTime");

            xmlWriter.startTag(null,"numbers");
            ListIterator<String> iterator = numbers.listIterator();
            builder.delete(0, builder.length());
            while (iterator.hasNext())
            {
                if(iterator.hasPrevious())
                {
                    builder.append(",");
                }
                builder.append(iterator.next());
            }
            xmlWriter.text(builder.toString());
            xmlWriter.endTag(null,"numbers");

            xmlWriter.endTag(null,"profile");

            xmlWriter.endDocument();
            xmlWriter.flush();
        } finally {
            fileOutput.close();
        }
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("Name : ").append(getName()).append("\n");
        builder.append("Days : ");

        for( int currentDay = 0; currentDay < days.length; currentDay++ )
        {
            if( days[currentDay] == true )
                builder.append("{True}");
            else
                builder.append("{False}");
        }
        builder.append("\n");
        builder.append("StartTime :\n");
        for( int currentDay = 0; currentDay < weekDays.length; currentDay++ )
        {
            builder.append(weekDays[currentDay])
                   .append(":")
                   .append(startTime[currentDay].getHour())
                   .append(":")
                   .append(startTime[currentDay].getMinute())
                   .append("\n");
        }
        builder.append("EndTime :\n");
        for( int currentDay = 0; currentDay < weekDays.length; currentDay++ )
        {
            builder.append(weekDays[currentDay])
                   .append(":")
                   .append(endTime[currentDay].getHour())
                   .append(":")
                   .append(endTime[currentDay].getMinute())
                   .append("\n");
        }
        builder.append("Numbers :\n");
        for(String number : numbers)
        {
            builder.append(number).append("\n");
        }
        return builder.toString();
    }

    public String getName()
    {
        return name;
    }

    boolean[] getDays()
    {
        return days;
    }

    TimeObject[] getStart()
    {
        return startTime;
    }

    TimeObject[] getEnd()
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

    void setStart( TimeObject[] updatedStart )
    {
        startTime = updatedStart;
    }

    void setEnd( TimeObject[] updatedEnd )
    {
        endTime = updatedEnd;
    }

    void setPhoneNumbers(ArrayList<String> numbers) {
        this.numbers = numbers;
    }

}