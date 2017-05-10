package com.example.peter.sugar;

import android.content.Context;
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
    private int startTime[];
    private int endTime[];
    private ArrayList<String> numbers;

    Profile(String name, boolean days[], int startTime[], int endTime[], ArrayList<String> numbers,Context context)
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
        startTime = new int[2];
        for( int i = 0; i < 2; i++ )
        {
            startTime[i] = 0;
        }
        endTime = new int[2];
        for( int i = 0; i < 2; i++ )
        {
            endTime[i] = 0;
        }
        numbers = new ArrayList<String>(0);
        numbers.add("Pseudonumber");
    }

    /**
     *
     * @param
     * @return null if no profile is found
     */
    public Profile readProfileFromXmlFile(Profile a )
    {
        try {
            FileInputStream fileInput = a.getContext().openFileInput(a.getName() + ".xml");
            ProfileParser parser = new ProfileParser();
            Profile result = parser.parse(fileInput, context);
            return result;
        } catch ( IOException e ) {
            e.printStackTrace();
        } catch ( XmlPullParserException e ) {
            e.printStackTrace();
        } finally {
            return null;
        }
    }
    /**
     * Saves the given Profile to the default filepath of Android
     * @return boolean whether the file was saved properly or not
     * @throws IOException if problems occured during the function execution
     */
    public boolean saveProfile() throws IOException {
        FileOutputStream fileOutput = null;
        XmlSerializer xmlWriter;
        try {
            fileOutput = context.openFileOutput(getName() + ".xml",Context.MODE_PRIVATE);
            xmlWriter = Xml.newSerializer();
            xmlWriter.setOutput(fileOutput, "UTF-8");
            xmlWriter.startDocument(null, true);
            xmlWriter.startTag("","profile");
            xmlWriter.startTag("", "name");
            xmlWriter.text(getName());
            xmlWriter.endTag("", "name");
            xmlWriter.startTag("", "days");
            for ( int currentDay = 0; currentDay < days.length; currentDay++ )
            {
                if( days[currentDay] == true )
                    xmlWriter.text("1");
                else
                    xmlWriter.text("0");
            }
            xmlWriter.endTag("","days");
            xmlWriter.startTag("","startTime");
            xmlWriter.text(startTime[0] + ":" + startTime[1]);
            xmlWriter.endTag("","startTime");
            xmlWriter.startTag("","endTime");
            xmlWriter.text(endTime[0] + ":" + endTime[1]);
            xmlWriter.endTag("","endTime");
            xmlWriter.startTag("","numbers");
            for(ListIterator<String> iterator = numbers.listIterator();iterator.hasNext();)
            {
                if( iterator.hasNext() == false )
                    xmlWriter.text(numbers.get(numbers.size()));
                else
                    xmlWriter.text(iterator.next());
            }
            xmlWriter.endTag("","numbers");
            xmlWriter.endTag("","profile");
            xmlWriter.endDocument();
            xmlWriter.flush();
            return true;
        } catch ( IOException exception ) {
          return false;
        } finally {
            fileOutput.close();
        }
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