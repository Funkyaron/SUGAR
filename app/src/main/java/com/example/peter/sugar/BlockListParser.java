package com.example.peter.sugar;

import android.content.Context;
import android.util.Log;
import android.util.Xml;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class BlockListParser {

    private static final String ns = null;

    public ArrayList<String> parse (InputStream in) throws IOException,XmlPullParserException
    {
        Log.d(MainActivity.LOG_TAG, "BlockListParser : Parsing blocklist ... ");

        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readBlockList(parser);
        } finally {
            in.close();
        }
    }

    private ArrayList<String> readBlockList(XmlPullParser parser) throws XmlPullParserException,IOException
    {
        parser.require(XmlPullParser.START_TAG, ns, "numbers");
        String[] allNumbers = readText(parser).split(",");
        parser.require(XmlPullParser.END_TAG, ns, "numbers");

        ArrayList<String> blockedNumbers = new ArrayList<String>(0);
        for(String number : allNumbers) {
            blockedNumbers.add(number);
        }
        return blockedNumbers;
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
