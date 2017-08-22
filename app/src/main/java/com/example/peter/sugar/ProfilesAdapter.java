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

    private String name;
    private boolean active;

    private TextView profileNameView;
    private ImageView onOffView;
    private ImageView editView;

    public ProfilesAdapter(Context context, Profile[] profiles) {
        super(context, 0, profiles);
    }

    @Override @NonNull
    public View getView(int position, View convertView, ViewGroup parent) {
        View result = convertView;

        if(result == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            result = inflater.inflate(R.layout.profile_list_item, null);
        }

        final Profile prof = getItem(position);

        name = prof.getName();
        active = prof.isActive();

        profileNameView = (TextView) result.findViewById(R.id.profile_name_view);
        onOffView = (ImageView) result.findViewById(R.id.on_off_view);
        editView = (ImageView) result.findViewById(R.id.edit_view);

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
