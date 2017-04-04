package com.example.peter.sugar;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.example.peter.sugar.ProfileContractor.ProfileEntry;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.*;
import com.example.peter.sugar.ProfileDBHelper.*;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* NOTE : This block only gives you WRITE-ACCESS to the database */
        ProfileDBHelper database_access = new ProfileDBHelper(getApplicationContext());
        SQLiteDatabase profile_database = database_access.getWritableDatabase();

        /* Setup data for the key/value-pair */
        String profile_name = "Arbeit";
        Date y = Calendar.getInstance().getTime();
        Date z = Calendar.getInstance().getTime();
        String contacts = "015757901173,01639018985,016377723";
        boolean isActive = false;

        /* Add a test tuple into the database */
        ContentValues example_row = new ContentValues();
        example_row.put(ProfileEntry.COLUMN_PROFILE_NAME,profile_name);
        example_row.put(ProfileEntry.COLUMN_PROFILE_START,y.getTime());
        example_row.put(ProfileEntry.COLUMN_PROFILE_END,z.getTime());
        example_row.put(ProfileEntry.COLUMN_PROFILE_CONTACTS,contacts);
        example_row.put(ProfileEntry.COLUMN_PROFILE_ACTIVE,isActive);
        long rowId = profile_database.insert(ProfileEntry.TABLE_NAME,null,example_row);

        /* Check wether the tuple is saved inside of the database */
        String buffer_tv = "";
        String[] projection = { ProfileEntry.COLUMN_PROFILE_NAME };
        String selection = ProfileEntry.COLUMN_PROFILE_NAME + " = ? ";
        String[] selection_args = { "Arbeit" };
        String sortOrder = ProfileEntry.COLUMN_PROFILE_NAME + " DESC";
        Cursor cursor = profile_database.rawQuery("SELECT * FROM PROFILE",null);
        while( cursor.moveToNext() )
        {
            buffer_tv = cursor.getString(cursor.getColumnIndexOrThrow(ProfileEntry.COLUMN_PROFILE_NAME));
        }
        cursor.close();
        profile_database.close();



    }
}
