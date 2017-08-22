package com.example.peter.sugar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ListProfilesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_profiles);

        Profile[] profiles = new Profile[0];

        try {
            profiles = Profile.readAllProfiles(this);
        } catch(Exception e) {
            Log.e(MainActivity.LOG_TAG, e.toString());
        }

        ProfilesAdapter adapter = new ProfilesAdapter(this, profiles);
        ListView listView = (ListView) findViewById(R.id.profiles_list);
        listView.setAdapter(adapter);
    }
}
