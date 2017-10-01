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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_profiles);

        Profile[] profiles = Profile.readAllProfiles(this);

        ProfilesAdapter adapter = new ProfilesAdapter(this, profiles);
        ListView listView = (ListView) findViewById(R.id.profiles_list);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,R.layout.profile_list_item,R.id.profile_name,new String[]{"A","B","C"});
        listView.setAdapter(adapter);
    }
}
