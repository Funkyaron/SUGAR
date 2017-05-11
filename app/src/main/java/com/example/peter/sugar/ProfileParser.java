package com.example.peter.sugar;

import android.content.Context;
import android.util.Log;
import android.util.Xml;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

class ProfileParser {

    private static final String ns = null;
    /**
     * This function processes the given XML file and returns a profile which can be later used
     * to activate the profile which is associated to the file.
     * @param in represents the input stream which reads the contents of the file
     * @return a profile which will be later used by "TimeManager","EnableProfileReceiver"
     * @throws XmlPullParserException is thrown if the file has formatting issues
     * @throws IOException is thrown if the file does not exist
     */
    public Profile parse (InputStream in,Context context) throws IOException,XmlPullParserException
    {
        Profile result = null;
        try {
            Log.d(MainActivity.LOG_TAG, "ProfileParser: parse()");
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            result = readProfile(parser,context);
            if (result == null)
                Log.d(MainActivity.LOG_TAG, "Parsing result is null");
        } catch ( Exception e ) {
            e.printStackTrace();
        } finally {
            in.close();
        }
        return result;
    }

    /**
     * Implements the interface function "parse()" to read the data from the XML file and brings
     * the content of the tags into a logical order.
     * @param parser parses the XML file further
     * @return profile for later usage
     * @throws XmlPullParserException
     * @throws IOException
     */
    private Profile readProfile(XmlPullParser parser,Context context) throws XmlPullParserException,IOException
    {
        String profileName = null;
        boolean profileDays[] = new boolean[7];
        int startTime[] = new int[2];
        int endTime[] = new int[2];
        ArrayList<String> numbers = new ArrayList<String>(0);

        parser.require(XmlPullParser.START_TAG,ns,"profile");
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
        Log.d(MainActivity.LOG_TAG, "Parsing results:\n" +
            profileName + "\n" + profileDays + "\n" + startTime + "\n" + endTime + "\n" +
            numbers);
        return new Profile(profileName, profileDays, startTime, endTime,numbers,context);
    }

    private String readProfileName(XmlPullParser parser) throws XmlPullParserException,IOException
    {
        parser.require(XmlPullParser.START_TAG,ns,"name");
        String name = readText(parser);
        parser.require(XmlPullParser.END_TAG,ns,"name");
        return name;
    }


    private boolean[] readActivatedDays(XmlPullParser parser) throws XmlPullParserException,IOException
    {
        boolean[] daysActivated = new boolean[7];
        parser.require(XmlPullParser.START_TAG,ns,"days");
        char[] days = readText(parser).toCharArray();
        parser.require(XmlPullParser.END_TAG,ns,"days");

        for( int currentDay = 0; currentDay < days.length; currentDay++ )
        {
            if( days[currentDay] == '1' )
            {
                daysActivated[currentDay] = true;
            }
            else if ( days[currentDay] == '0')
            {
                daysActivated[currentDay] = false;
            }
        }
        return daysActivated;
    }

    private int[] readStartTime(XmlPullParser parser) throws XmlPullParserException,IOException
    {
        parser.require(XmlPullParser.START_TAG,ns,"startTime");
        String startTime = readText(parser);
        parser.require(XmlPullParser.END_TAG,ns,"startTime");
        String[] time = startTime.split(":");
        int hours = Integer.parseInt(time[0]);
        int minutes = Integer.parseInt(time[1]);
        int result[] = { hours,minutes };
        return result;
    }

    private int[] readEndTime(XmlPullParser parser) throws XmlPullParserException,IOException
    {
        parser.require(XmlPullParser.START_TAG,ns,"endTime");
        String endTime = readText(parser);
        parser.require(XmlPullParser.END_TAG,ns,"endTime");
        String[] time = endTime.split(":");
        int hours = Integer.parseInt(time[0]);
        int minutes = Integer.parseInt(time[1]);
        int result[] = { hours,minutes };
        return result;
    }

    private ArrayList<String> readPhoneNumbers(XmlPullParser parser) throws XmlPullParserException,IOException
    {
        ArrayList<String> resultList = new ArrayList<String>(0);
        parser.require(XmlPullParser.START_TAG,ns,"numbers");
        String[] phoneNumbers = readText(parser).split(",");
        parser.require(XmlPullParser.END_TAG,ns,"numbers");
        for( int currentPhoneNumber = 0; currentPhoneNumber < phoneNumbers.length; currentPhoneNumber++ )
        {
            resultList.add(phoneNumbers[currentPhoneNumber]);
        }
        return resultList;
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
