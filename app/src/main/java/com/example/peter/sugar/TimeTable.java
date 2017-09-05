package com.example.peter.sugar;

/**
 * Created by SHK on 22.08.17.
 */

import java.util.*;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class TimeTable extends TableLayout
{
    private Context context;
    private ArrayList<TableRow> timeTableRows;
    private TableRow toBeModifiedTableRow;
    private TableRow toBeAddedTableRow;

    public TimeTable(Context context)
    {
        super(context);
        this.context = context;
        timeTableRows = new ArrayList<TableRow>(7);
        for(int tableRowCounter = 0; tableRowCounter < 7; tableRowCounter++ )
        {
            Log.d(MainActivity.LOG_TAG,"Added a new tableRow to the TableLayout!");
            TableRow test = new TableRow(context);
            timeTableRows.add(test);
        }
        toBeModifiedTableRow = new TableRow(context);
        toBeAddedTableRow = new TableRow(context);
    }

    public void convertProfileToTimeTable(Profile passedProfile) throws Exception
    {
        int timeIndex = 0;
        TimeObject[] startTimes = passedProfile.getStart();
        TimeObject[] endTimes = passedProfile.getEnd();
        boolean[] activatedDays = passedProfile.getDays();

        // Setup the ArrayList of TableRow
        for(ListIterator<TableRow> rowIterator = timeTableRows.listIterator();rowIterator.hasNext();)
        {
            Log.d(MainActivity.LOG_TAG,"Weekday ");
            // Setup TableRow's properties
            TableRow currRow = rowIterator.next();
            currRow.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
            currRow.setWeightSum(6.0f);

            // Setup TextView's properties
            TextView currWeekDay = new TextView(context);
            currWeekDay.setLayoutParams(new TableRow.LayoutParams(0,TableRow.LayoutParams.WRAP_CONTENT,3.0f));
            currWeekDay.setBackgroundColor(Color.RED);
            TextView currWeekDayTime = new TextView(context);
            currWeekDayTime.setLayoutParams(new TableRow.LayoutParams(0,TableRow.LayoutParams.WRAP_CONTENT,3.0f));
            currWeekDayTime.setBackgroundColor(Color.GREEN);
            currWeekDayTime.setText(startTimes[timeIndex].toString()+" bis " + endTimes[timeIndex].toString());
            currWeekDayTime.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                }
            });

            // Add TextView to the current TableRow
            currRow.addView(currWeekDay);
            currRow.addView(currWeekDayTime);
            addView(currRow);
            timeIndex++;
        }
    }
}
