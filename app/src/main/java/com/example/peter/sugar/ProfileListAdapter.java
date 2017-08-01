package com.example.peter.sugar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by petersafontschik on 01.08.17.
 */

public class ProfileListAdapter extends ArrayAdapter<String>
{
    private Activity adapterContext;
    private String[] profileNames;

    public ProfileListAdapter(Activity context,String[] names)
    {
        super(context,R.layout.profile_listview_item,names);
        adapterContext = context;
        profileNames = names;
    }

    public View getView(int position,View view,ViewGroup parent)
    {
        final int currPosition = position;
        LayoutInflater adapterInflater = adapterContext.getLayoutInflater();
        View profileView = adapterInflater.inflate(R.layout.profile_listview_item,null);
        final TextView nameDisplay = (TextView) profileView.findViewById(R.id.profile_name);
        nameDisplay.setText(profileNames[position]);
        nameDisplay.setTextColor(Color.WHITE);
        Button activateButton = (Button) profileView.findViewById(R.id.activation_button);
        activateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                if( nameDisplay.getCurrentTextColor() == Color.RED )
                    nameDisplay.setTextColor(Color.WHITE);
                else if ( nameDisplay.getCurrentTextColor() == Color.WHITE )
                    nameDisplay.setTextColor(Color.RED);
            }
        });
        Button editButton = (Button) profileView.findViewById(R.id.edit_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent goToDisplayProfileActivity = new Intent(adapterContext.getApplicationContext(),DisplayProfileActivity.class);
                goToDisplayProfileActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                goToDisplayProfileActivity.putExtra("profileName",profileNames[currPosition]);
                adapterContext.getApplicationContext().startActivity(goToDisplayProfileActivity);
            }
        });
        return profileView;
    }
}
