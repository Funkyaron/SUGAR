package com.example.peter.sugar;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * Created by petersafontschik on 08.08.17.
 */

public class CustomTableLayout extends TableLayout
{
    private Context tableContext;
    private AttributeSet tableAttributes;
    private TableRow[] currentRows = new TableRow[7];
    private String[] currentContents = new String[7];
    private Profile selectedProfile;
    private final String[] weekDays = { "Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday" };

    public CustomTableLayout(Context context)
    {
        super(context);
        this.tableContext = context;
    }

    public CustomTableLayout(Context context,AttributeSet attributes)
    {
        super(context,attributes);
        this.tableContext = context;
    }

    @Override
    public void addView(View child, int index)
    {
        // Is called if you want to insert a row at a invalid position ( in our case any index >= 7 )
        if( index >= 7 )
        {
            Log.d(MainActivity.LOG_TAG,"Error : You can't assign an eight-th value to array of rows! Reason : You only have seven days, therefore you only have seven rows.");
            return;
        } else {
            Log.d(MainActivity.LOG_TAG,"We are now adding a child to the current table!");
            currentRows[index] = (TableRow) child;
            TextView weekDayView = new TextView(tableContext);
            weekDayView.setText(weekDays[index]);
            TextView weekDayActivatedView = new TextView(tableContext);
            weekDayActivatedView.setOnClickListener(new OnClickListener() {
                public void onClick(View v)
                {
                    // Call timePickDialog to modify the time
                }
            });
            if( selectedProfile.getDays()[index] )
                weekDayActivatedView.setText( selectedProfile.getStart()[index] + " bis " + selectedProfile.getEnd()[index]);
            else if ( !selectedProfile.getDays()[index] )
                weekDayActivatedView.setText("Das Profil ist für diesen Tag nicht aktiviert!");
            currentRows[index].addView(weekDayView);
            currentRows[index].addView(weekDayActivatedView);
        }
    }

}
