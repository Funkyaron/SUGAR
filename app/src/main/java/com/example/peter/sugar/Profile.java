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
    private Context context;
    private String name;
    private boolean days[];
    private TimeObject startTime[];
    private TimeObject endTime[];
    private ArrayList<String> numbers;
    private static final String[] weekDays = { "monday","tuesday","wednesday","thursday","friday","saturday","sunday"};

    Profile(String name, boolean days[], TimeObject startTime[], TimeObject endTime[], ArrayList<String> numbers,Context context)
    {
        this.context = context;
        this.name = name;
        this.days = days;
        this.startTime = startTime;
        this.endTime = endTime;
        this.numbers = numbers;
    }

    Profile(Context context)
    {
        name = "GenericProfile";
        days = new boolean[7];
        for(int i = 0; i < 7; i++)
        {
            days[i] = false;
        }
        startTime = new TimeObject[7];
        for( int i = 0; i < 2; i++ )
        {
            startTime[i] = new TimeObject(0,0);
        }
        endTime = new TimeObject[7];
        for( int i = 0; i < 2; i++ )
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
    {
        Log.d(MainActivity.LOG_TAG, "Profile: readProfileFromXmlFile()");
        Profile result = null;
        try {
            FileInputStream fileInput = context.openFileInput(name + ".xml");
            ProfileParser parser = new ProfileParser();
            result = parser.parse(fileInput, context);
            if (result == null)
                Log.d(MainActivity.LOG_TAG, "Parsing result did not return");
            fileInput.close();
        } catch ( IOException e ) {
            e.printStackTrace();
        } catch ( XmlPullParserException e ) {
            e.printStackTrace();
        }
        return result;
    }
    /**
     * Saves the given Profile to the default filepath of Android
     * @return boolean whether the file was saved properly or not
     * @throws IOException if problems occured during the function execution
     */
    public boolean saveProfile() throws IOException {
        Log.d(MainActivity.LOG_TAG, "Profile: saveProfile()");
        FileOutputStream fileOutput = null;
        XmlSerializer xmlWriter;
        try {
            fileOutput = context.openFileOutput(name + ".xml",Context.MODE_PRIVATE);

            xmlWriter = Xml.newSerializer();
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
                xmlWriter.startTag(null,weekDays[currentDay]);
                xmlWriter.text(startTime[currentDay].getHour() + ":" + startTime[currentDay].getMinute() );
                xmlWriter.endTag(null,weekDays[currentDay]);
            }
            xmlWriter.endTag(null,"startTime");

            xmlWriter.startTag(null,"endTime");
            for(int currentDay = 0; currentDay < endTime.length; currentDay++ )
            {
                xmlWriter.startTag(null,weekDays[currentDay]);
                xmlWriter.text(endTime[currentDay].getHour() + ":" + endTime[currentDay].getMinute());
                xmlWriter.endTag(null,weekDays[currentDay]);
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
            return true;
        } finally {
            fileOutput.close();
        }
    }

    @Override
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

    public Context getContext()
    {
        return context;
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

}