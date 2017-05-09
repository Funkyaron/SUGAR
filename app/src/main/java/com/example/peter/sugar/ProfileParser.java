package com.example.peter.sugar;

import android.util.Log;
import android.util.Xml;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.io.InputStream;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.net.*;

class ProfileParser {

    /** Defines the namespace of our xml files ( which is non-existent ) */
    private static final String ns = null;
    /** Serializes the profiles into a readable xml file */
    private XStream serializer;

    /**
     * This function processes the given XML file and returns a profile which can be later used
     * to activate the profile which is associated to the file.
     * @param in represents the input stream which reads the contents of the file
     * @return a profile which will be later used by "ProfileUpdateUtil","EnableProfileReceiver"
     * @throws XmlPullParserException is thrown if the file has formatting issues
     * @throws IOException is thrown if the file does not exist
     */
    public Profile parse (InputStream in) throws XmlPullParserException,IOException
    {
        Log.d(MainActivity.LOG_TAG, "ProfileParser: parse()");
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES,false);
            parser.setInput(in,null);
            parser.nextTag();
            return readProfile(parser);
        } finally {
            in.close();
        }
    }

    /**
     * Implements the interface function "parse()" to read the data from the XML file and brings
     * the content of the tags into a logical order.
     * @param parser parses the XML file further
     * @return profile for later usage
     * @throws XmlPullParserException
     * @throws IOException
     */
    private Profile readProfile(XmlPullParser parser) throws XmlPullParserException,IOException
    {
        Log.d(MainActivity.LOG_TAG, "ProfileParser: readProfile()");
        parser.require(XmlPullParser.START_TAG,ns,"profile");
        String profileName = null;
        boolean profileDays[] = new boolean[7];
        int startTime[] = new int[2];
        int endTime[] = new int[2];
        String[] numbers = null;
        while( parser.next() != XmlPullParser.END_TAG )
        {
            if( parser.getEventType() != XmlPullParser.START_TAG )
            {
                continue;
            }
            String name = parser.getName();
            if( name.equals("name"))
            {
                profileName = readProfileName(parser);
            }
            else if ( name.equals("days"))
            {
                profileDays = readActivatedDays(parser);
            }
            else if ( name.equals("startTime"))
            {
                startTime = readStartTime(parser);
            }
            else if ( name.equals("endTime"))
            {
                endTime = readEndTime(parser);
            }
            else if ( name.equals("numbers"))
            {
                numbers = readPhoneNumbers(parser);
            }
        }
        return new Profile(profileName, profileDays, startTime, endTime,numbers);
    }

    private String readProfileName(XmlPullParser parser) throws XmlPullParserException,IOException
    {
        Log.d(MainActivity.LOG_TAG, "ProfileParser: readProfileName()");
        parser.require(XmlPullParser.START_TAG,ns,"name");
        String name = readText(parser);
        parser.require(XmlPullParser.END_TAG,ns,"name");
        return name;
    }


    private boolean[] readActivatedDays(XmlPullParser parser) throws XmlPullParserException,IOException
    {
        Log.d(MainActivity.LOG_TAG, "ProfileParser: readDays()");
        boolean[] daysActivated = new boolean[7];
        parser.require(XmlPullParser.START_TAG,ns,"days");
        char[] days = readText(parser).toCharArray();
        parser.require(XmlPullParser.END_TAG,ns,"days");

        Log.d(MainActivity.LOG_TAG, "Next Line: for(int currentDay...");
        for( int currentDay = 0; currentDay < days.length; currentDay++ )
        {
            Log.d(MainActivity.LOG_TAG, "current Day = " + currentDay);
            if( days[currentDay] == '1' )
            {
                Log.d(MainActivity.LOG_TAG, "day active");
                daysActivated[currentDay] = true;
            }
            else if ( days[currentDay] == '0')
            {
                Log.d(MainActivity.LOG_TAG, "day inactive");
                daysActivated[currentDay] = false;
            }
        }
        return daysActivated;
    }

    private int[] readStartTime(XmlPullParser parser) throws XmlPullParserException,IOException
    {
        Log.d(MainActivity.LOG_TAG, "ProfileParser: readStartTime()");
        parser.require(XmlPullParser.START_TAG,ns,"startTime");
        String startTime = readText(parser);
        parser.require(XmlPullParser.END_TAG,ns,"startTime");
        int hours = Integer.parseInt(startTime.substring(0,2));
        int minutes = Integer.parseInt(startTime.substring(2,4));
        int result[] = { hours,minutes };
        return result;
    }

    private int[] readEndTime(XmlPullParser parser) throws XmlPullParserException,IOException
    {
        Log.d(MainActivity.LOG_TAG, "ProfileParser: readEndTime()");
        parser.require(XmlPullParser.START_TAG,ns,"endTime");
        String startTime = readText(parser);
        parser.require(XmlPullParser.END_TAG,ns,"endTime");
        int hours = Integer.parseInt(startTime.substring(0,2));
        int minutes = Integer.parseInt(startTime.substring(2,4));
        int result[] = { hours,minutes };
        return result;
    }

    private String[] readPhoneNumbers(XmlPullParser parser) throws XmlPullParserException,IOException
    {
        Log.d(MainActivity.LOG_TAG," ProfileParser: readPhoneNumbers() ");
        parser.require(XmlPullParser.START_TAG,ns,"numbers");
        String[] phoneNumbers = readText(parser).toString().split(",");
        parser.require(XmlPullParser.END_TAG,ns,"numbers");
        return phoneNumbers;
    }

    private String readText(XmlPullParser parser) throws XmlPullParserException,IOException
    {
        String result = "";
        if( parser.next() == XmlPullParser.TEXT )
        {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

}
