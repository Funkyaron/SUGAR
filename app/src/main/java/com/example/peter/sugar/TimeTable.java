package com.example.peter.sugar;

/**
 * Created by SHK on 22.08.17.
 */

import java.util.*;
import android.content.Context;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class TimeTable extends TableLayout
{
    private Context context;
    private ArrayList<TableRow> timeTableRows;
    private TableRow toBeModifiedTableRow;
    private TableRow toBeAddedTableRow;
    private int weekDayCount = 0;

    public TimeTable(Context context)
    {
        super(context);
        timeTableRows = new ArrayList<TableRow>(7);
        toBeModifiedTableRow = new TableRow(context);
        toBeAddedTableRow = new TableRow(context);
    }

    public void profileToTimeTable(Profile profile)
    {
        TimeObject[] startTimes = profile.getStart();
        TimeObject[] endTimes = profile.getEnd();
    }
}
