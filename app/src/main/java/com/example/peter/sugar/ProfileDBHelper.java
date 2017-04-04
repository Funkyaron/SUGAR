package com.example.peter.sugar;

import android.content.Context;
import android.database.sqlite.*;
import com.example.peter.sugar.ProfileContractor.ProfileEntry;
/**
 * Created by Peter on 04.04.2017.
 */

public class ProfileDBHelper extends SQLiteOpenHelper  {

    /* Database version , name and create statement */
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Profiles.db";
    public static final String CREATE_PROFILE_TABLE = "CREATE TABLE " + ProfileEntry.TABLE_NAME + " ( " +
                                                      ProfileEntry.COLUMN_PROFILE_NAME + " VARCHAR PRIMARY KEY, \n" +
                                                      ProfileEntry.COLUMN_PROFILE_START + "DATE, \n" +
                                                      ProfileEntry.COLUMN_PROFILE_END + "DATE, \n" +
                                                      ProfileEntry.COLUMN_PROFILE_CONTACTS + " VARCHAR, \n" +
                                                      ProfileEntry.COLUMN_PROFILE_ACTIVE + " DECIMAL(1)); \n";

    public ProfileDBHelper(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase database)
    {
        database.execSQL(CREATE_PROFILE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
    {
        database.execSQL("DROP TABLE IF EXISTS " + ProfileEntry.TABLE_NAME);
        onCreate(database);
    }

    public void onDowngrade(SQLiteDatabase database, int oldVersion, int newVersion)
    {
        onUpgrade(database,oldVersion,newVersion);
    }
}
