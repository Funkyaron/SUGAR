package com.example.peter.sugar;

import android.util.Xml;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class ProfileParser {

    private static final String ns = null;

    /**
     * This function processes the given XML file and returns a profile which can be later used
     * to activate the profile which is associated to the file.
     * @param in represents the input stream which reads the contents of the file
     * @return a profile which will be later used by "ProfileUpdateUtil","EnableProfileReceiver"
     * @throws XmlPullParserException is thrown if the xml file is invalid
     * @throws IOException is thrown if the xml file isn't found at all
     */
    public Profile parse (InputStream in) throws XmlPullParserException,IOException
    {
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
        parser.require(XmlPullParser.START_TAG,ns,"profile");
        String profileName = null;
        boolean profileDays[] = new boolean[7];
        int startTime[] = new int[2];
        int endTime[] = new int[2];
        while( parser.next() != XmlPullParser.END_TAG )
        {
            if( parser.getEventType() != XmlPullParser.END_TAG )
            {
                continue;
            }
            String name = parser.getName();
            if( name.equals("title"))
            {
                name = readProfileName(parser);
            }
        }
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
