package com.example.peter.sugar;

/**
 * This class represents a parser which is able to parse the app-exclusive file "closingTimes" which saves the
 * last-known closing time of the employee XY permanently ( unless you delete the app ).
 * Created by Peter Safontschik
 */

import java.io.*;
import android.util.*;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class ClosingTimesParser
{
    private static final String ns = null;
    private final String[] weekDays = { "Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};

    /**
     * This function is the call you have to make if you want to load the current closing times into the app.
     * @param in InputStream which directs to the file "closingTimes.xml" inside of the files directory of the app
     * @return an array of TimeObject's where each position corresponds to the i+1-th day of the week
     * @throws IOException is thrown if the file couldn't be found
     * @throws XmlPullParserException is thrown if the xml file is badly formatted
     */
    public TimeObject[] parseClosingTime (InputStream in) throws IOException,XmlPullParserException
    {
        Log.d(MainActivity.LOG_TAG,"ClosingTimeParser : parse()");
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES,false);
            parser.setInput(in,null);
            parser.nextTag();
            return readClosingTimes(parser);
        } finally {
            in.close();
        }
    }

    /**
     * This function reads the xml data and converts the content between an START_TAG and END_TAG to a string which can
     * be later converted to a object of the type "TimeObject"
     * @param parser
     * @return an array of TimeObjects which contain the times
     * @throws XmlPullParserException
     * @throws IOException
     */
    private TimeObject[] readClosingTimes(XmlPullParser parser) throws XmlPullParserException,IOException {
        int currentlyReadWeekDay = 0;
        String[] closingTimesFromFile = new String[7];
        TimeObject[] result = new TimeObject[7];
        parser.require(XmlPullParser.START_TAG,null,"closingTimes");
        Log.d(MainActivity.LOG_TAG,"Starting parsing process ... ");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tagName = parser.getName();
            if (parser.next() == XmlPullParser.TEXT)
            {
                // TODO: Alternate XML layout so you can see whether the closing time is activated or not and write function for two subtags
                closingTimesFromFile[currentlyReadWeekDay] = parser.getText();
                result[currentlyReadWeekDay] = new TimeObject(closingTimesFromFile[currentlyReadWeekDay]);
                Log.d(MainActivity.LOG_TAG,weekDays[currentlyReadWeekDay] + " -> " + result[currentlyReadWeekDay].toString());
                currentlyReadWeekDay++;
                parser.nextTag();
            }
        }
        Log.d(MainActivity.LOG_TAG,"Finished parsing process!");
        return result;
    }

}
