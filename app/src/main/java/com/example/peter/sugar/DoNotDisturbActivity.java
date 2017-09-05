package com.example.peter.sugar;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DoNotDisturbActivity extends AppCompatActivity {

    private TextView countDownView;
    private Button startCountDownButton;
    private Button stopCountDownButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_not_disturb);

        countDownView = (TextView) findViewById(R.id.count_down_view);
        startCountDownButton = (Button) findViewById(R.id.start_count_down_button);
        stopCountDownButton = (Button) findViewById(R.id.stop_count_down_button);

        final TimeObject time = new TimeObject(0, 5);
        countDownView.setText(time.toString());

        final CountDownTimer timer = new CountDownTimer(time.getTimeInMillis(), 5000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d(MainActivity.LOG_TAG, "timer: onTick()");
                time.setTimeInMillis(millisUntilFinished);
                countDownView.setText(time.toString());
            }

            @Override
            public void onFinish() {
                Log.d(MainActivity.LOG_TAG, "timer: onFinish()");
                InCallServiceImpl.shouldBlockAbsolutely = false;
                time.setTime(0, 5);
                countDownView.setText(time.toString());
                startCountDownButton.setVisibility(View.VISIBLE);
                stopCountDownButton.setVisibility(View.GONE);
            }
        };


        startCountDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InCallServiceImpl.shouldBlockAbsolutely = true;
                timer.start();
                startCountDownButton.setVisibility(View.GONE);
                stopCountDownButton.setVisibility(View.VISIBLE);
            }
        });

        stopCountDownButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                timer.cancel();
                timer.onFinish();
            }
        });
    }
}
