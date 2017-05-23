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

public class BlackList {

    private XmlSerializer xmlWriter;
    private ArrayList<Profile> activatedProfiles;
    private ArrayList<String> blockedNumbers;

    public BlackList()
    {
        activatedProfiles = new ArrayList<Profile>(0);
        blockedNumbers = new ArrayList<String>(0);
    }

    public void addProfile(Profile addProfile,Context context) throws Exception
    {
        activatedProfiles.add(addProfile);
        saveBlockList(context);
    }

    public boolean removeProfile( Profile removeProfile , Context context ) throws Exception
    {
        /*
        int currentArrayListId = 0;
        for(Profile currentProfile: activatedProfiles )
        {
            if( currentProfile.getId() == removeProfile.getId() )
            {
                activatedProfiles.remove(currentArrayListId);
                saveBlockList(context);
                return true;
            }
        } */
        return false;
    }

    public boolean readBlockList(Context context) throws IOException,XmlPullParserException
    {
        try {
            BlockListParser parser = new BlockListParser();
            FileInputStream blockListInputStream = new FileInputStream(context.getApplicationContext() + "");
            ArrayList<String> result = parser.parse(context.openFileInput("blocklist.xml"), context);
            blockedNumbers = result;
            return true;
        } catch ( IOException e ) {
            Log.d("READ BLACKLIST : "," Unfortunately your blacklist couldn't be read : " + e.getMessage());
            return false;
        } catch ( XmlPullParserException e ) {
            Log.d("READ XML : ","Unfortunately your xml file containing the blacklist is badly formatted : " + e.getMessage());
            return false;
        }
    }

    public boolean saveBlockList(Context context) throws Exception
    {
        File currentBlockList = new File(context.getFilesDir() + "blacklist.xml");
        if( currentBlockList.exists() )
        {
            currentBlockList.delete();
        }
        try {
            xmlWriter = Xml.newSerializer();
            xmlWriter.setOutput(context.openFileOutput("blacklist.xml",Context.MODE_PRIVATE),"UTF-8");
            xmlWriter.startDocument("UTF-8",true);
            xmlWriter.startTag(null,"blacklist");
            xmlWriter.startTag(null,"numbers");
            for(ListIterator<Profile> iterator = activatedProfiles.listIterator();iterator.hasNext();)
            {
                String toAddNumbers[] = new String[iterator.next().getPhoneNumbers().size()];
                toAddNumbers = iterator.next().getPhoneNumbers().toArray(toAddNumbers);
                for( int currentPhoneNumber = 0; currentPhoneNumber < toAddNumbers.length; currentPhoneNumber++ )
                {
                    xmlWriter.text(toAddNumbers[currentPhoneNumber]);
                }
            }
            xmlWriter.endTag(null,"numbers");
            xmlWriter.endTag(null,"blacklist");
            xmlWriter.endDocument();
            xmlWriter.flush();
            return true;
        } catch ( Exception e ) {
            Log.d("WRITE BLACKLIST :","Unfortunately your blacklist could not be written : " + e.getMessage());
            return false;
        }
    }

    public ArrayList<String> getBlockedNumbers()
    {
        return blockedNumbers;
    }

    public ArrayList<Profile> getActivatedProfiles()
    {
        return activatedProfiles;
    }

}
