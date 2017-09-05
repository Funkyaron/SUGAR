package com.example.peter.sugar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class EditProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        LinearLayout ll = (LinearLayout) findViewById(R.id.edit_layout);
        TextView profileName = (TextView) findViewById(R.id.profile_name);
        profileName.setText(getIntent().getExtras().getString("profileName"));
        TimeTable testTable = new TimeTable(this);
        try {
            testTable.convertProfileToTimeTable(Profile.readProfileFromXmlFile("Kurz",this));
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        ll.addView(testTable);
    }
}
