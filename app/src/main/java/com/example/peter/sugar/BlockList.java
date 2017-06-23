package com.example.peter.sugar;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.Xml;

import java.io.*;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;
import java.util.*;

/**
 * Created by Peter on 19.05.2017.
 *
 * The name BlockList is maybe rather confusing. The basic principle is:
 * The numbers that are listed in this list are allowed concerning incoming
 * calls. Therefore the default state is that the numbers of all contacts
 * exist once in this list.
 * This principle allows for handling "concurrent profiles". That means is one
 * contact is associated with more than one profile. When one contact is in the allow-
 * time of one profile, the number appears twice in the list. Then, when one of the
 * two profiles is disabled, the number is deleted only once - calls are still allowed.
 */

public class BlockList {

    private ArrayList<String> blockedNumbers;

    public BlockList(Context context)
    {
        try {
            blockedNumbers = readBlockList(context);
        } catch (Exception e) {
            blockedNumbers = new ArrayList<>(0);
            Log.e(MainActivity.LOG_TAG, "BlockList(): " + e.toString());
            Log.d(MainActivity.LOG_TAG, "Empty BlockList created");
        }
    }

    public ArrayList<String> getBlockedNumbers() { return blockedNumbers; }

    public void setNumbersAndSave(Context context, ArrayList<String> numbers) throws Exception {
        blockedNumbers = numbers;
        saveBlockList(context);
    }

    public void addProfile(Context context, Profile profile) throws Exception
    {
        ArrayList<String> numbersToAdd = profile.getPhoneNumbers();
        for(String number : numbersToAdd)
        {
            blockedNumbers.add(number);
        }
        saveBlockList(context);
    }

    public void removeProfile(Context context, Profile profile) throws Exception
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
        File currentBlockListFile = new File(context.getFilesDir() + "/blocklist.xml");
        if( currentBlockListFile.exists() )
            currentBlockListFile.delete();

        FileOutputStream fileOutput = context.openFileOutput("blocklist.xml",Context.MODE_PRIVATE);

        try {
            XmlSerializer xmlWriter = Xml.newSerializer();
            xmlWriter.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            xmlWriter.setOutput(fileOutput, "UTF-8");

            xmlWriter.startDocument("UTF-8", true);
            xmlWriter.startTag(null, "numbers");

            if (blockedNumbers.size() != 0) {
                for (String number : blockedNumbers) {
                    xmlWriter.text(number + ",");
                }
            } else {
                xmlWriter.text("");
            }

            xmlWriter.endTag(null, "numbers");
            xmlWriter.endDocument();
            xmlWriter.flush();
        } finally {
            fileOutput.close();
        }
    }



    public void resetBlockList(Context context) throws Exception {
        Log.d(MainActivity.LOG_TAG, "Reset BlockList");

        ArrayList<String> allNumbers = new ArrayList<>();

        Cursor dataCursor = getDataCursor(context);
        Cursor rawCursor = getRawCursor(context);

        ArrayList<Long> allIds = new ArrayList<>();

        rawCursor.moveToPosition(-1);
        while(rawCursor.moveToNext()) {
            allIds.add(rawCursor.getLong(
                    rawCursor.getColumnIndex(ContactsContract.RawContacts._ID)));
        }

        dataCursor.moveToPosition(-1);
        while(dataCursor.moveToNext()) {
            Long currentId = dataCursor.getLong(
                    dataCursor.getColumnIndex(ContactsContract.Data.RAW_CONTACT_ID));
            for(Long id : allIds) {
                if(id.equals(currentId)) {
                    String number = dataCursor.getString(
                            dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    if(number != null)
                        allNumbers.add(number);
                    String normNumber = dataCursor.getString(
                            dataCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER));
                    if(normNumber != null)
                        allNumbers.add(normNumber);
                }
            }
        }

        blockedNumbers = allNumbers;
        MainActivity.logBlockList(this);
        saveBlockList(context);
    }


    // The same as in ContactsDialogFragment

    private Cursor getDataCursor(Context context) {

        String[] dataProjection = new String[] {
                ContactsContract.Data.RAW_CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER,
                ContactsContract.Data.MIMETYPE};
        String dataSelection =
                "(" + ContactsContract.Data.MIMETYPE + " =?)";
        String[] dataSelectionArgs = new String[] {
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE};

        return context.getContentResolver().query(
                ContactsContract.Data.CONTENT_URI,
                dataProjection,
                dataSelection,
                dataSelectionArgs,
                null);
    }

    private Cursor getRawCursor(Context context) {

        String[] rawProjection = {
                ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY,
                ContactsContract.RawContacts._ID,
                ContactsContract.RawContacts.ACCOUNT_NAME,
                ContactsContract.RawContacts.DELETED};
        String rawSelection = "(((" +
                ContactsContract.RawContacts.ACCOUNT_NAME + " =?) OR (" +
                ContactsContract.RawContacts.ACCOUNT_NAME + " =?) OR (" +
                ContactsContract.RawContacts.ACCOUNT_NAME + " =?)) AND (" +
                ContactsContract.RawContacts.DELETED + " =?))";
        String[] rawSelectionArgs = {
                "SIM1", "SIM2", "Phone", "0"};
        String rawSortOrder =
                ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY;

        return context.getContentResolver().query(
                ContactsContract.RawContacts.CONTENT_URI,
                rawProjection,
                rawSelection,
                rawSelectionArgs,
                rawSortOrder);
    }

}
