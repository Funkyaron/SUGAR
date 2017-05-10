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
    private static Context context;
    private String name;
    private boolean days[];
    private int startTime[];
    private int endTime[];
    private ArrayList<String> numbers;

    Profile(Context context )
    {
        this.context = context;
        name = "";
        days = new boolean[7];
        startTime = new int[2];
        endTime = new int[2];
        numbers = new ArrayList<String>(0);

    }

    Profile(String name, boolean days[], int startTime[], int endTime[], ArrayList<String> numbers,Context context)
    {
        this.context = context;
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

    public Profile readProfileFromData(Profile referenceObject) throws Exception
    {
        return new ProfileParser().parse(new FileInputStream(context.getFilesDir() + "/" + referenceObject.getName() + ".xml"),context);
    }

    private void saveProfile() throws IOException
    {
        FileOutputStream fileOutput = null;
        try {
            XmlSerializer serializer = Xml.newSerializer();
            fileOutput = new FileOutputStream(context.getFilesDir() + "/" + getName() + ".xml");
            serializer.setOutput(fileOutput,"UTF-8");
            serializer.startDocument(null,true);
            serializer.startTag(null,"profile");
                serializer.startTag(null,"name");
                serializer.text(getName());
                serializer.endTag(null,"name");
                serializer.startTag(null,"startTime");
                serializer.text(getStart()[0] + ":" + getStart()[1]);
                serializer.endTag(null,"startTime");
                serializer.startTag(null,"endTime");
                serializer.text(getEnd()[0] + ":" + getEnd()[1]);
                serializer.endTag(null,"endTime");
                serializer.startTag(null,"numbers");
                for(ListIterator<String> iterator = numbers.listIterator();iterator.hasNext();)
                {
                    if( iterator.hasNext() == false )
                        serializer.text(numbers.get(numbers.size()));
                    else
                        serializer.text(iterator.next());
                }
                serializer.endTag(null,"numbers");
            serializer.endTag(null,"profile");
            fileOutput.close();
        } catch ( IOException exception ) {
            throw new IOException();
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