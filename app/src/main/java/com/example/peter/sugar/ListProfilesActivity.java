package com.example.peter.sugar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ListProfilesActivity extends AppCompatActivity
{

    private Profile profiles[];
    private ListView profilesList;

    @Override
    public void onResume()
    {
        super.onResume();
        profiles = Profile.readAllProfiles(this);
        ProfilesAdapter adapter = new ProfilesAdapter(this,profiles);
        profilesList = (ListView) findViewById(R.id.profiles_list);
        profilesList.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_profiles);

        profiles = Profile.readAllProfiles(this);

        ProfilesAdapter adapter = new ProfilesAdapter(this, profiles);
        profilesList = (ListView) findViewById(R.id.profiles_list);
        profilesList.setAdapter(adapter);
    }
}
