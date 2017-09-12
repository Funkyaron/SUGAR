package com.example.peter.sugar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by arons on 22.08.2017.
 *
 * Used to display Profile names with additional icons.
 */

public class ProfilesAdapter extends ArrayAdapter<Profile> {

    private Context context;

    public ProfilesAdapter(Context context, Profile[] profiles) {
        super(context, 0, profiles);
        this.context = context;
    }

    @Override @NonNull
    public View getView(int position, View convertView, ViewGroup parent) {
        View result = convertView;


        LayoutInflater inflater = LayoutInflater.from(getContext());
        result = inflater.inflate(R.layout.profile_list_item, null);


        final Profile prof = getItem(position);

        final String name = prof.getName();

        final TextView profileNameView = (TextView) result.findViewById(R.id.profile_name_text);
        final ImageButton onOffView = (ImageButton) result.findViewById(R.id.power_icon);
        final ImageButton editView = (ImageButton) result.findViewById(R.id.edit_icon);

        profileNameView.setText(name);
        if(prof.isActive()) {
            onOffView.setImageResource(R.mipmap.power_on);
        } else {
            onOffView.setImageResource(R.mipmap.power_off);
        }
        onOffView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d(MainActivity.LOG_TAG, "Concerning profile: " + name);
                if(prof.isActive()) {
                    prof.setActive(false);
                    prof.setAllowed(true);
                    onOffView.setImageResource(R.mipmap.power_off);
                } else {
                    prof.setActive(true);
                    new TimeManager(context).initProfile(prof);
                    onOffView.setImageResource(R.mipmap.power_on);
                }
                try {
                    prof.saveProfile(getContext());
                } catch (Exception e) {
                    Log.e(MainActivity.LOG_TAG, e.toString());
                }
            }
        });
        editView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent openEditProfileActivity = new Intent(context,EditProfileActivity.class);
                Bundle openEditProfileActivityBundle = new Bundle();
                openEditProfileActivityBundle.putString("profileName",name);
                openEditProfileActivity.putExtras(openEditProfileActivityBundle);
                context.startActivity(openEditProfileActivity);
            }
        });

        return result;
    }
}
