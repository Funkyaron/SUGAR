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
import java.io.InputStream;

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
            testProFile.delete();
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
        serializer.text("0000111");
        serializer.endTag(null, "days");

        serializer.startTag(null, "startTime");
        serializer.text("14:30");
        serializer.endTag(null, "startTime");

        serializer.startTag(null, "endTime");
        serializer.text("18:30");
        serializer.endTag(null, "endTime");

        serializer.startTag(null, "numbers");
        serializer.text("+4917635183695,1234");
        serializer.endTag(null, "numbers");

        serializer.endDocument();
        serializer.flush();

        fos.close();
    }

    public Profile readTestProfile(Context context) throws IOException, XmlPullParserException {

        Log.d(MainActivity.LOG_TAG, "TestXmlWriter: readTestProfile()");

        InputStream fis = context.openFileInput(FILENAME);
        ProfileParser pParser = new ProfileParser();
        return pParser.parse(fis, context);

    }
}
