package com.example.peter.sugar;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Funkyaron on 06.06.2017.
 *
 * Not needed anymore.
 */

public class ContactsAdapter extends SimpleCursorAdapter {

    private Activity context;
    ArrayList<Model> list;

    public ContactsAdapter(Activity context, Cursor cursor, String[] from, int[] to) {
        super(context, R.layout.list_item_contacts, cursor, from, to, 0);
        this.context = context;
        list = new ArrayList<Model>(0);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(
                        ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY));
                long id = cursor.getLong(cursor.getColumnIndex(
                        ContactsContract.RawContacts._ID));
                list.add(new Model(name, id));
            }
        }
    }

    static class ViewHolder {
        protected TextView text;
        protected CheckBox checkBox;
    }

    @Override
    public Cursor swapCursor(Cursor cursor) {
        super.swapCursor(cursor);
        Log.d(MainActivity.LOG_TAG, "ContactsAdapter: swapCursor()");
        list = new ArrayList<Model>(0);
        if(cursor != null) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(
                        ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY));
                long id = cursor.getLong(cursor.getColumnIndex(
                        ContactsContract.RawContacts._ID));
                list.add(new Model(name, id));
            }
        } else {
        }
        return cursor;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View result = null;

        if(convertView == null) {

            LayoutInflater inflater = context.getLayoutInflater();
            result = inflater.inflate(R.layout.list_item_contacts, null);

            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.text = (TextView) result.findViewById(R.id.name_view);
            viewHolder.checkBox = (CheckBox) result.findViewById(R.id.contacts_checkbox);
            viewHolder.checkBox.setOnCheckedChangeListener(
                    new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            Model element = (Model) viewHolder.checkBox.getTag();
                            element.setSelected(isChecked);
                            if(isChecked)
                                Log.d(MainActivity.LOG_TAG, element.getName() + " selected");
                            else
                                Log.d(MainActivity.LOG_TAG, element.getName() + " deselected");
                        }
                    }
            );

            result.setTag(viewHolder);
            viewHolder.checkBox.setTag(list.get(position));
        } else {
            result = convertView;
            ((ViewHolder) result.getTag()).checkBox.setTag(list.get(position));
        }

        ViewHolder holder = (ViewHolder) result.getTag();
        holder.text.setText(list.get(position).getName());
        holder.checkBox.setChecked(list.get(position).isSelected());
        return result;
    }

    public ArrayList<String> getSelectedContacts() {
        ArrayList<String> result = new ArrayList<String>(0);
        for (Model element : list) {
            if(element.isSelected()) {
                result.add(element.getName());
            }
        }
        return result;
    }

    public ArrayList<Long> getSelectedContactIds() {
        ArrayList<Long> result = new ArrayList<Long>(0);
        for(Model element: list) {
            if(element.isSelected()) {
                result.add(element.getId());
            }
        }
        return result;
    }
}
