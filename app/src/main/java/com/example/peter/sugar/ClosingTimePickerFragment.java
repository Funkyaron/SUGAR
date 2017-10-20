package com.example.peter.sugar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;

/**
 * Created by shk on 27.09.17.
 */

public class ClosingTimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    private int index;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        int hourOfDay = args.getInt(MainActivity.EXTRA_HOUR_OF_DAY, 0);
        int minute = args.getInt(MainActivity.EXTRA_MINUTE, 0);
        index = args.getInt(MainActivity.EXTRA_INDEX);

        AlertDialog dialog = new TimePickerDialog(getActivity(), this, hourOfDay,
                minute, true);

        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.remove),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ClosingTimeDisplayActivity parentActivity = (ClosingTimeDisplayActivity) getActivity();
                        parentActivity.setClosingTime(index, null);

                        TextView timeView = parentActivity.getWeekDayViews()[index];
                        timeView.setText(parentActivity.toDayString(index, ""));

                        SharedPreferences savedTimes = parentActivity.getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = savedTimes.edit();
                        editor.putString(parentActivity.WEEKDAYS[index], "-");
                        editor.apply();

                        TimeManager mgr = new TimeManager(getContext());
                        mgr.unsetClosingTime(index);
                    }
                });

        return dialog;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TimeObject time = new TimeObject(hourOfDay, minute);

        ClosingTimeDisplayActivity parentActivity = (ClosingTimeDisplayActivity) getActivity();
        parentActivity.setClosingTime(index, time);

        TextView timeView = parentActivity.getWeekDayViews()[index];
        timeView.setText(parentActivity.toDayString(index, time.toString()));

        SharedPreferences savedTimes = parentActivity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = savedTimes.edit();
        editor.putString(parentActivity.WEEKDAYS[index], time.toString());
        editor.apply();

        TimeManager mgr = new TimeManager(getContext());
        mgr.setNextClosingTime(index, time);
    }


}
