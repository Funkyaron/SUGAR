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

/**
 * Created by Peter on 18.04.2017.
 * @param : ns Defines the namespace of the XML document. In this case we don't use any namespace at all
 */

class XMLProfileParser {

    /* Our profiles do not have any xml-namespace */
    private static final String ns = null;

    public static class Profile
    {
        /**
         * @param : profile_name Defines the name of the profile
         * @param : days Defines an array of seven boolean values which indicate wether the profile is active(TRUE) or not(FALSE)
         * @param : start Defines the beginning of the profile
         * @param : end Defines the end of the profile
         */
        public String profile_name;
        public boolean days[];
        public Date[] start;
        public Date[] termination;

        public Profile(String con_profile_name, boolean con_days[], Date[] con_start, Date[] con_termination)
        {
            this.profile_name = con_profile_name;
            this.days = con_days;
            this.start = con_start;
            this.termination = con_termination;
        }
    }

    public Profile readProfile(XmlPullParser parser) throws XmlPullParserException,IOException {
        parser.require(XmlPullParser.START_TAG,ns,"profile");
        String profile_name = null;
        boolean days[] = new boolean[7];
        Date[] start = null;
        Date[] termination = null;
        while( parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("name")) {
                profile_name = readName(parser);
            } else if (name.equals("days")) {
                days = readDays(parser);
            } else if (name.equals("start")) {
                start = readStartDate(parser);
            } else if (name.equals("end ")) {
                termination = readTerminationDate(parser);
            }
        }
            return new Profile(profile_name,days,start,termination);
    }

    /* Starting point of the PARSE-Process */
    public List parse(InputStream xml_input) throws XmlPullParserException,IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES,false);
            parser.setInput(xml_input,null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            xml_input.close();
        }
    }

    /* Method reads an XML file */
    public List readFeed(XmlPullParser parser) throws XmlPullParserException,IOException {
        List profiles = new ArrayList();
        parser.require(XmlPullParser.START_TAG,ns,"profile");
        while(parser.next() != XmlPullParser.END_TAG) {
            if( parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if( name.equals(name))
            {
                profiles.add(readProfile(parser));
            } else {
                skip(parser);
            }
        }
        return profiles;
    }

    /* Method returns the name of the profile */
    private String readName(XmlPullParser parser) throws XmlPullParserException,IOException {
        parser.require(XmlPullParser.START_TAG,ns,"name");
        String name = extractValue(parser);
        parser.require(XmlPullParser.END_TAG,ns,"name");
        return name;
    }

    /* Method returns the days of the week and if they are affected by the profile */
    private boolean[] readDays (XmlPullParser parser) throws XmlPullParserException,IOException {
        boolean[] result = new boolean[7];
        parser.require(XmlPullParser.START_TAG,ns,"days");
        char[] days = extractValue(parser).toCharArray();
        for(int curr_char = 0; curr_char < days.length; curr_char++ )
        {
            if( days[curr_char] == '0')
                result[curr_char] = false;
            else if ( days[curr_char] == '1')
                result[curr_char] = true;

        }
        return result;
    }

    /* Returns the starting point of the profile */
    private Date[] readStartDate(XmlPullParser parser) throws XmlPullParserException,IOException {
        String[] input = null;
        Date[] result = new Date[7];
        SimpleDateFormat date_formatter = new SimpleDateFormat("E yyy.MM.dd");
        parser.require(XmlPullParser.START_TAG,ns,"start");
        input = extractValue(parser).split("]");
        for(int curr_date = 0; curr_date < input.length; curr_date++ )
        {
            //date_formatter.parse(input[curr_date]);
        }
        return null;
    }

    /* Returns the end point of the profile */
    private Date[] readTerminationDate(XmlPullParser parser) throws XmlPullParserException,IOException {
        /* TODO: Implement method */
        return null;
    }

    /* Returns the value of a specified XML tag */
    private String extractValue(XmlPullParser parser) throws XmlPullParserException,IOException {
        String result = "";
        if( parser.next() == XmlPullParser.TEXT )
        {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException,IOException {
        if( parser.getEventType() != XmlPullParser.START_TAG ) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while ( depth != 0 )
        {
            switch(parser.next())
            {
                case XmlPullParser.END_TAG :
                {
                    depth--;
                    break;
                }

                case XmlPullParser.START_TAG :
                {
                    depth++;
                    break;
                }
            }
        }
    }

}
