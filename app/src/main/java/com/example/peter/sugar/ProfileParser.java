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
    private final String[] weekDays = { "monday","tuesday","wednesday","thursday","friday","saturday","sunday"};
    /**
     * This function processes the given XML file and returns a profile which can be later used
     * to activate the profile which is associated to the file.
     * @param in represents the input stream which reads the contents of the file
     * @return a profile which will be later used by "TimeManager","EnableProfileReceiver"
     * @throws XmlPullParserException is thrown if the file has formatting issues
     * @throws IOException is thrown if the file does not exist
     */
    public Profile parse (InputStream in,Context context)
            throws IOException,XmlPullParserException
    {
        Log.d(MainActivity.LOG_TAG, "ProfileParser: parse()");
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
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
    private Profile readProfile(XmlPullParser parser)
            throws XmlPullParserException,IOException
    {
        String profileName = null;
        boolean[] profileDays = new boolean[7];
        TimeObject[] startTime = new TimeObject[7];
        TimeObject[] endTime = new TimeObject[7];
        ArrayList<String> numbers = new ArrayList<String>(0);

        parser.require(XmlPullParser.START_TAG,ns,"profile");
        while( parser.next() != XmlPullParser.END_TAG )
        {
            if( parser.getEventType() != XmlPullParser.START_TAG )
            {
                continue;
            }
            String name = parser.getName();
            switch (name) {
                case "name":
                    profileName = readProfileName(parser);
                    break;
                case "days":
                    profileDays = readActivatedDays(parser);
                    break;
                case "startTime":
                    startTime = readStartTimes(parser);
                    break;
                case "endTime":
                    endTime = readEndTimes(parser);
                    break;
                case "numbers":
                    numbers = readPhoneNumbers(parser);
            }
        }
        return new Profile(profileName, profileDays, startTime, endTime,numbers);
    }

    private String readProfileName(XmlPullParser parser)
            throws XmlPullParserException,IOException
    {
        parser.require(XmlPullParser.START_TAG,ns,"name");
        String name = readText(parser);
        parser.require(XmlPullParser.END_TAG,ns,"name");
        return name;
    }


    private boolean[] readActivatedDays(XmlPullParser parser)
            throws XmlPullParserException,IOException
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

    private TimeObject[] readStartTimes(XmlPullParser parser)
            throws XmlPullParserException,IOException
    {
        TimeObject[] result = new TimeObject[7];
        parser.require(XmlPullParser.START_TAG, ns, "startTime");
        String[] resultText = readText(parser).split(",");
        parser.require(XmlPullParser.END_TAG, ns, "startTime");

        for(int currentDay = 0; currentDay < resultText.length; currentDay++ )
        {
            result[currentDay] = new TimeObject(resultText[currentDay]);
        }
        return result;
    }

    private TimeObject[] readEndTimes(XmlPullParser parser)
            throws XmlPullParserException,IOException
    {
        TimeObject[] result = new TimeObject[7];
        parser.require(XmlPullParser.START_TAG,ns,"endTime");
        String[] resultText = readText(parser).split(",");
        parser.require(XmlPullParser.END_TAG,ns,"endTime");

        for(int currentDay = 0; currentDay < resultText.length; currentDay++ )
        {
            result[currentDay] = new TimeObject(resultText[currentDay]);
        }
        return result;
    }

    private ArrayList<String> readPhoneNumbers(XmlPullParser parser)
            throws XmlPullParserException,IOException
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
