package com.example.peter.sugar;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DoNotDisturbActivity extends AppCompatActivity {

    private TextView countDownView;
    private Button startCountDownButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_not_disturb);

        countDownView = (TextView) findViewById(R.id.count_down_view);
        startCountDownButton = (Button) findViewById(R.id.start_count_down_button);

        final CountDownTimer timer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                countDownView.setText("" + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                countDownView.setText(R.string.finish);
            }
        };


        startCountDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.start();
            }
        });
    }
}
