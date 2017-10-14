package com.example.peter.sugar;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by SHK on 14.10.17.
 */

public class EditProfileFragment extends Fragment
{
    public static String selectedSubSection = "Time";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstances)
    {
        View rootView = inflater.inflate(R.layout.edit_profile_fragment_time,container);
        String passedArguments = getArguments().getString("selectedTab");
        if( passedArguments.equals("Time") )
        {

        } else if ( passedArguments.equals("Phone") ) {
        }
        return rootView;
    }
}
