package com.example.peter.sugar;

import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ClosingTimeDisplayActivity extends AppCompatActivity {

    final String[] weekdays = {
            "Monday", "Tuesday", "Wednesday", "Thursday",
            "Friday", "Staurday", "Sunday"
    };

    private TimeObject[] closingTimes;

    private TableLayout table;
    TextView[] timeViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_closing_time_display);

        Log.d(MainActivity.LOG_TAG, "Before opening SharedPreferences");
        closingTimes = new TimeObject[7];
        SharedPreferences savedTimes = getPreferences(Context.MODE_PRIVATE);
        for(int i = 0; i < weekdays.length; i++) {
            String str = savedTimes.getString(weekdays[i], "-");
            if(str.equals("-")) {
                closingTimes[i] = null;
            } else {
                closingTimes[i] = new TimeObject(str);
            }
        }

        Log.d(MainActivity.LOG_TAG, "Before initializing Views");
        try {
            table = (TableLayout) findViewById(R.id.closing_time_table);
            timeViews = new TextView[table.getChildCount()];
            for (int i = 0; i < table.getChildCount(); i++) {
                TableRow row = (TableRow) table.getChildAt(i);
                timeViews[i] = (TextView) row.getChildAt(1);
                if (closingTimes[i] != null) {
                    timeViews[i].setText(closingTimes[i].toString());
                } else {
                    timeViews[i].setText("-");
                }
            }
        } catch(Exception e) {
            Log.e(MainActivity.LOG_TAG, e.toString());
        }

        for(int i = 0; i < timeViews.length; i++) {
            final int index = i;
            timeViews[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle args = new Bundle();
                    if(closingTimes[index] != null) {
                        args.putInt(MainActivity.EXTRA_HOUR_OF_DAY, closingTimes[index].getHour());
                        args.putInt(MainActivity.EXTRA_MINUTE, closingTimes[index].getMinute());
                    }
                    args.putInt(MainActivity.EXTRA_INDEX, index);

                    DialogFragment newFragment = new ClosingTimePickerFragment();
                    newFragment.setArguments(args);
                    newFragment.show(getFragmentManager(), "closing" + index);
                }
            });
        }
    }

    public void setClosingTime(int index, TimeObject time) {
        closingTimes[index] = time;
    }

    public TableLayout getTable() {
        return table;
    }

    /* mondayButton = find...
     * ..
     * sundayButton = find...
     *
     * Button[] allDays = ...
     *
     * for(int i = 0; i < 7; i++) {
     *  siehe oben (69-83)
     * }
     */

    /* <button
     *  android:text="@string/mondayPlusTime"
     *  />
     *
     *  strings.xml:
     *  <string name="mondayPlusTime">Montag\n%s</string>
     *
     *  Activity:
     *  getString(R.string.mondayPlusTime, timeObject.toString());
     */

}
