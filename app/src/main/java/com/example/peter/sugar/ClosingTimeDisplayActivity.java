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

    final String[] WEEKDAYS = {
            "Monday", "Tuesday", "Wednesday", "Thursday",
            "Friday", "Staurday", "Sunday"
    };

    private TimeObject[] closingTimes;
    private TextView[] timeViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_closing_time_display);

        timeViews = new TextView[7];
        TableLayout rootLayout = (TableLayout) findViewById(R.id.closing_times_view);

        for(int i = 0; i < timeViews.length; i++) {
            TableRow row = (TableRow) rootLayout.getChildAt(i);
            timeViews[i] = (TextView) row.getChildAt(1);
        }

        Log.d(MainActivity.LOG_TAG, "Before opening SharedPreferences");
        closingTimes = new TimeObject[7];
        SharedPreferences savedTimes = getPreferences(Context.MODE_PRIVATE);
        for(int i = 0; i < WEEKDAYS.length; i++) {
            String str = savedTimes.getString(WEEKDAYS[i], "-");
            if(str.equals("-")) {
                closingTimes[i] = null;
            } else {
                closingTimes[i] = new TimeObject(str);
            }
        }


        for(int i = 0; i < timeViews.length; i++) {
            timeViews[i].setText(closingTimes[i] == null ? "" : closingTimes[i].toString());
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

        TimeManager mgr = new TimeManager(this);
        for(int i = 0; i < closingTimes.length; i++) {
            if(closingTimes[i] != null) {
                mgr.setNextClosingTime(i, closingTimes[i]);
            }
        }
    }

    public void setClosingTime(int index, TimeObject time) {
        closingTimes[index] = time;
    }

    public TextView[] getWeekDayViews() { return timeViews; }
    public TimeObject[] getClosingTimes() { return closingTimes; }

}
