package com.example.peter.sugar;

import android.content.Intent;
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
    private ProfilesAdapter adapter;
    private TextView addProfilePseudoButton;
    //private Intent toCreateProfileActivity;

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

        adapter = new ProfilesAdapter(this, profiles);
        profilesList = (ListView) findViewById(R.id.profiles_list);
        profilesList.setAdapter(adapter);

        /*
        addProfilePseudoButton = (TextView) findViewById(R.id.add_profile_button);
        addProfilePseudoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                toCreateProfileActivity = new Intent(getApplicationContext(),CreateProfileActivity.class);
                startActivity(toCreateProfileActivity);
            }
        }); */
    }
}
