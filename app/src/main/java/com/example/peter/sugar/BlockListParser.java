package com.example.peter.sugar;

import android.content.Context;
import android.util.Log;
import android.util.Xml;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class BlockListParser {

    private static final String ns = null;

    public ArrayList<String> parse (InputStream in,Context context) throws IOException,XmlPullParserException
    {
        ArrayList<String> result = null;
        try {
            Log.d(MainActivity.LOG_TAG, "BlockListParser : Parsing blocklist ... ");
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            result = readBlockList(parser,context);
            if (result == null)
                Log.d(MainActivity.LOG_TAG, "Parsing result is null");
        } catch ( Exception e ) {
            e.printStackTrace();
        } finally {
            in.close();
        }
        return result;
    }

    public ArrayList<String> readBlockList(XmlPullParser parser,Context context) throws XmlPullParserException,IOException
    {
        ArrayList<String> blockedNumbers = new ArrayList<String>(0);
        parser.require(XmlPullParser.START_TAG,ns,"blacklist");
        while( parser.next() != XmlPullParser.END_TAG )
        {
            String name = parser.getName();
            if( parser.getEventType() != XmlPullParser.START_TAG )
                continue;
            else if ( name.equals("numbers")) {
                blockedNumbers = readPhoneNumbers(parser);
            }
        }
        return new ArrayList<String>(0);
    }

    private ArrayList<String> readPhoneNumbers(XmlPullParser parser) throws XmlPullParserException,IOException
    {
        ArrayList<String> result = new ArrayList<String>(0);
        parser.require(XmlPullParser.START_TAG,ns,"numbers");
        String allNumbers[] = readText(parser).split(",");
        parser.require(XmlPullParser.END_TAG,ns,"numbers");
        for( int currentPhoneNumber = 0; currentPhoneNumber < allNumbers.length; currentPhoneNumber++ )
        {
            result.add(allNumbers[currentPhoneNumber]);
        }
        return result;
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
