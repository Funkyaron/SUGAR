package com.example.peter.sugar;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Funkyaron on 28.04.2017.
 */

public class TestXmlWriter {

    public final String FILENAME = "testprofile.xml";

    public void writeTestProfile (Context context) throws IOException {

        Log.d(MainActivity.LOG_TAG, "TestXmlWriter: writeTestProfile()");

        File testProFile = new File(context.getFilesDir() + "/" + FILENAME);
        if (testProFile.exists())
        {
            return;
        }

        FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);

        XmlSerializer serializer = Xml.newSerializer();
        serializer.setOutput(fos, "UTF-8");
        serializer.startDocument(null, true);

        serializer.startTag(null, "profile");

        serializer.startTag(null, "name");
        serializer.text("Arbeit");
        serializer.endTag(null, "name");

        serializer.startTag(null, "days");
        serializer.text("0,0,0,0,1,1,1");
        serializer.endTag(null, "days");

        serializer.startTag(null, "start_time");
            serializer.startTag(null, "hour");
            serializer.text("14");
            serializer.endTag(null, "hour");

            serializer.startTag(null, "minute");
            serializer.text("30");
            serializer.endTag(null, "minute");
        serializer.endTag(null, "start_time");

        serializer.startTag(null, "end_time");
            serializer.startTag(null, "hour");
            serializer.text("18");
            serializer.endTag(null, "hour");

            serializer.startTag(null, "minute");
            serializer.text("15");
            serializer.endTag(null, "minute");
        serializer.endTag(null, "end_time");

        serializer.startTag(null, "numbers");
        serializer.text("+4917635183695,1234");
        serializer.endTag(null, "numbers");

        serializer.endDocument();
        serializer.flush();

        fos.close();
    }

    public Profile readTestProfile(Context context) throws IOException, XmlPullParserException {

        Log.d(MainActivity.LOG_TAG, "TestXmlWriter: readTestProfile()");

        File testProFile = new File(context.getFilesDir() + "/" + FILENAME);
        if (testProFile.exists())
        {
            FileInputStream fis = context.openFileInput(FILENAME);
            ProfileParser pParser = new ProfileParser();
            return pParser.parse(fis);
        }
        else
        {
            return null;
        }

    }
}
