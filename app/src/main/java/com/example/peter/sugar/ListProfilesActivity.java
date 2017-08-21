package com.example.peter.sugar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class ListProfilesActivity extends AppCompatActivity {

    private ImageView onOffView;
    private ImageView editView;
    private TextView profileNameView;

    private String name;
    private boolean active;

    private Profile prof;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_profiles);

        try {
            prof = Profile.readAllProfiles(this)[0];
        } catch(Exception e) {
            Log.e(MainActivity.LOG_TAG, e.toString());
        }

        name = prof.getName();
        active = prof.isActive();

        onOffView = (ImageView) findViewById(R.id.on_off_view);
        editView = (ImageView) findViewById(R.id.edit_view);
        profileNameView = (TextView) findViewById(R.id.profile_name_view);

        profileNameView.setText(name);
        if(active) {
            onOffView.setImageResource(R.mipmap.on1);
        } else {
            onOffView.setImageResource(R.mipmap.off1);
        }
        onOffView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(active) {
                    prof.setActive(false);
                    onOffView.setImageResource(R.mipmap.off1);
                } else {
                    prof.setActive(true);
                    onOffView.setImageResource(R.mipmap.on1);
                }
                try {
                    prof.saveProfile(ListProfilesActivity.this);
                } catch (Exception e) {
                    Log.e(MainActivity.LOG_TAG, e.toString());
                }
            }
        });
        editView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: open edit screen
            }
        });
    }
}
