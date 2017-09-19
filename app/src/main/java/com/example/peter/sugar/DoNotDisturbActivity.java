package com.example.peter.sugar;

import java.text.DecimalFormat;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DoNotDisturbActivity extends AppCompatActivity {

    private class CustomCountDownTimer extends CountDownTimer {
        private TimeObject time;


        CustomCountDownTimer(TimeObject time) {
            super(time.getTimeInMillis(), 5000);
            millis = time.getTimeInMillis();
            Log.d(MainActivity.LOG_TAG, "CustomCountDownTimer(): millis = " + millis);
            this.time = time;
        }

        CustomCountDownTimer(long cMillis) {
            super(cMillis, 5000);
            millis = cMillis;
            TimeObject newTime = new TimeObject(0,0);
            newTime.setTimeInMillis(cMillis);
            time = newTime;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            Log.d(MainActivity.LOG_TAG, "CustomTimer: onTick() " + hashCode());
            time.setTimeInMillis(millisUntilFinished);
            millis = millisUntilFinished;
            Log.d(MainActivity.LOG_TAG, "millis = " + millis);
            countDownView.setText(time.toString());
        }

        @Override
        public void onFinish() {
            Log.d(MainActivity.LOG_TAG, "CustomTimer: onFinish()");
            InCallServiceImpl.shouldBlockAbsolutely = false;

            setContent(false);
            isRunning = false;
        }


    }

    private TextView countDownView;
    private TextView doNotDisturbDisplay;
    private Button startCountDownButton;
    private Button stopCountDownButton;
    private RelativeLayout timeAmountView;
    private NumberPicker hourPicker;
    private NumberPicker minutePicker;

    private static CustomCountDownTimer timer;

    private static boolean isRunning;
    private static long millis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_not_disturb);
        prepareViews();

        startCountDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InCallServiceImpl.shouldBlockAbsolutely = true;
                TimeObject actualTime = new TimeObject(hourPicker.getValue(), minutePicker.getValue());

                countDownView.setText(actualTime.toString());
                setContent(true);

                timer = new CustomCountDownTimer(actualTime);
                timer.start();
                isRunning = true;
            }
        });

        stopCountDownButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                timer.cancel();
                timer.onFinish();
            }
        });

        if(isRunning) {
            timer.cancel();
            setContent(true);
            TimeObject time = new TimeObject(0,0);
            time.setTimeInMillis(millis);
            countDownView.setText(time.toString());
            timer = new CustomCountDownTimer(millis);
            timer.start();
        }
    }



    private void setContent(boolean timerIsRunning) {
        if(timerIsRunning) {
            Log.d(MainActivity.LOG_TAG, "setContent(): timer is running");
            doNotDisturbDisplay.setText(getString(R.string.time_remaining));
            timeAmountView.setVisibility(View.GONE);
            countDownView.setVisibility(View.VISIBLE);
            startCountDownButton.setVisibility(View.GONE);
            stopCountDownButton.setVisibility(View.VISIBLE);
        } else {
            Log.d(MainActivity.LOG_TAG, "setContent(): timer is not running");
            doNotDisturbDisplay.setText(getString(R.string.prompt_do_not_disturb));
            countDownView.setVisibility(View.GONE);
            timeAmountView.setVisibility(View.VISIBLE);
            stopCountDownButton.setVisibility(View.GONE);
            startCountDownButton.setVisibility(View.VISIBLE);
        }
    }

    private void prepareViews() {
        countDownView = (TextView) findViewById(R.id.count_down_view);
        doNotDisturbDisplay = (TextView) findViewById(R.id.do_not_disturb_display);
        startCountDownButton = (Button) findViewById(R.id.start_count_down_button);
        stopCountDownButton = (Button) findViewById(R.id.stop_count_down_button);
        timeAmountView = (RelativeLayout) findViewById(R.id.time_amount_view);
        hourPicker = (NumberPicker) findViewById(R.id.hour_picker);
        minutePicker = (NumberPicker) findViewById(R.id.minute_picker);

        hourPicker.setMinValue(0);
        hourPicker.setMaxValue(23);
        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(59);
        minutePicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                DecimalFormat form = new DecimalFormat("00");
                return form.format(value);
            }
        });
    }
}