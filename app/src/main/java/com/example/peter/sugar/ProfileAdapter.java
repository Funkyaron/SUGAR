package com.example.peter.sugar;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.*;

/**
 * Created by Peter on 14.07.2017.
 */

public class ProfileAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Profile> adapterContent = new ArrayList<Profile>(0);

    public ProfileAdapter(Context currentContext)
    {
        this.context = currentContext;
    }

    public int getCount()
    {
        return adapterContent.size()-1;
    }

    public Object getItem(int itemId )
    {
        return null;
    }

    public long getItemId(int position)
    {
        return 0;
    }

    public View getView(int position,View convertView,ViewGroup parent)
    {
        TextView profileView;
        if( convertView == null )
        {
            // if it's not recycled initalize some attributes
            profileView = new TextView(context);
            profileView.setLayoutParams(new GridView.LayoutParams(85,85));
        }
    }
}
