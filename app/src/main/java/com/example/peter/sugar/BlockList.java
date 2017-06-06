package com.example.peter.sugar;

/**
 * Created by Peter on 19.05.2017.
 */

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import java.io.*;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;
import java.util.*;

public class BlockList {

    private ArrayList<String> blockedNumbers;

    public BlockList(Context context)
    {
        try {
            blockedNumbers = readBlockList(context);
        } catch (Exception e) {
            blockedNumbers = new ArrayList<String>(0);
            Log.e(MainActivity.LOG_TAG, "Blocklist(): " + e.toString());
        }
    }

    public ArrayList<String> getBlockedNumbers() { return blockedNumbers; }

    public void addProfile(Profile profile, Context context) throws Exception
    {
        ArrayList<String> numbersToAdd = profile.getPhoneNumbers();
        for(String number : numbersToAdd)
        {
            blockedNumbers.add(number);
        }
        saveBlockList(context);
    }

    public void removeProfile(Profile profile, Context context) throws Exception
    {
        ArrayList<String> numbersToRemove = profile.getPhoneNumbers();
        for(String number : numbersToRemove)
        {
            blockedNumbers.remove(number);
        }
        saveBlockList(context);
    }



    private ArrayList<String> readBlockList(Context context) throws IOException,XmlPullParserException
    {
        BlockListParser parser = new BlockListParser();
        return parser.parse(context.openFileInput("blocklist.xml"));
    }

    private void saveBlockList(Context context) throws Exception
    {
        File currentBlacklistFile = new File(context.getFilesDir() + "/" + "blocklist.xml");
        if( currentBlacklistFile.exists() )
            currentBlacklistFile.delete();

        FileOutputStream fileOutput = context.openFileOutput("blocklist.xml",Context.MODE_PRIVATE);
        XmlSerializer xmlWriter = Xml.newSerializer();
        xmlWriter.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output",true);
        xmlWriter.setOutput(fileOutput,"UTF-8");

        xmlWriter.startDocument("UTF-8",true);
        xmlWriter.startTag(null,"numbers");

        if(blockedNumbers.size() != 0) {
            for (String number : blockedNumbers) {
                xmlWriter.text(number + ",");
            }
        } else {
            xmlWriter.text("");
        }

        xmlWriter.endTag(null,"numbers");
        xmlWriter.endDocument();
        xmlWriter.flush();
    }


}
