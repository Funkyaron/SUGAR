package com.example.peter.sugar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by petersafontschik on 08.08.17.
 */

public class TimeTableLayout extends TableLayout
{
    private final String weekDays[] = { "Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
    private Context tableContext;

    public TimeTableLayout(Context context)
    {
        super(context);
        this.tableContext = context;
    }

    public TimeTableLayout(Context context,AttributeSet set)
    {
        super(context, set);
        this.tableContext = context;
    }

    /**
     * With this function you can add a row for a given day by providing the start and end time as well as the week day
     * @param timeSpan represents the time span on the corresponding day
     * @param weekDayIndex represents the number of the day ( BEGINS WITH 0 NOT 1 ) xD
     */
    public void addTimeRow(TimeObject timeSpan[],int weekDayIndex)
    {
        TableRow toBeAddedRow = new TableRow(tableContext);
        toBeAddedRow.setLayoutParams(new TableRow.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
        toBeAddedRow.setWeightSum(3.0f);
        TextView weekDayDisplay = new TextView(tableContext);
        weekDayDisplay.setLayoutParams(new TableRow.LayoutParams(0,LayoutParams.WRAP_CONTENT,1.0f));
        weekDayDisplay.setText(weekDays[weekDayIndex]);
        weekDayDisplay.setTextSize(25f);
        weekDayDisplay.setBackgroundColor(Color.GREEN);
        weekDayDisplay.setTextColor(Color.BLACK);
        weekDayDisplay.setGravity(Gravity.CENTER);
        toBeAddedRow.addView(weekDayDisplay);
        TextView timeSpanDisplay = new TextView(tableContext);
        timeSpanDisplay.setLayoutParams(new TableRow.LayoutParams(0,LayoutParams.WRAP_CONTENT,2.0f));
        timeSpanDisplay.setText(timeSpan[0].toString() + "-" + timeSpan[1].toString());
        timeSpanDisplay.setTextSize(25f);
        timeSpanDisplay.setBackgroundColor(Color.BLUE);
        timeSpanDisplay.setTextColor(Color.BLACK);
        timeSpanDisplay.setGravity(Gravity.CENTER);
        timeSpanDisplay.setOnClickListener(new OnClickListener() {
            public void onClick(View v)
            {
                Toast.makeText(tableContext,"Listener is working!",Toast.LENGTH_LONG).show();
            }
        });
        toBeAddedRow.addView(timeSpanDisplay);
        addView(toBeAddedRow);
    }

}