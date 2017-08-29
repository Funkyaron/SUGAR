package com.example.peter.sugar;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

        final TextView profileNameView = (TextView) result.findViewById(R.id.profile_name_view);
        final ImageView onOffView = (ImageView) result.findViewById(R.id.on_off_view);
        final ImageView editView = (ImageView) result.findViewById(R.id.edit_view);

        profileNameView.setText(name);
        if(prof.isActive()) {
            onOffView.setImageResource(R.mipmap.on1);
        } else {
            onOffView.setImageResource(R.mipmap.off1);
        }
        onOffView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.d(MainActivity.LOG_TAG, "Concerning profile: " + name);
                if(prof.isActive()) {
                    prof.setActive(false);
                    prof.setAllowed(true);
                    onOffView.setImageResource(R.mipmap.off1);
                } else {
                    prof.setActive(true);
                    new TimeManager(context).initProfile(prof);
                    onOffView.setImageResource(R.mipmap.on1);
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
                //TODO: open edit screen
            }
        });

        return result;
    }
}
