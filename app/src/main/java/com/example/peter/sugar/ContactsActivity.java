package com.example.peter.sugar;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ContactsActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {



    /**
     * These are the contacts database columns that we will retrieve.
     */
    private final String[] PROJECTION = {
            ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY,
            ContactsContract.RawContacts._ID,
            ContactsContract.RawContacts.ACCOUNT_NAME
    };

    /**
     * We will order the query result by display name.
     */
    private final String SORT_ORDER = ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY;

    /**
     * We don't want to display some contacts twice, so we get off all contact entries
     * created by WhatsApp or whatever. See SELECTION_ARGS
     */
    private final String SELECTION = "((" +
            ContactsContract.RawContacts.ACCOUNT_NAME + " =?) OR (" +
            ContactsContract.RawContacts.ACCOUNT_NAME + " =?) OR (" +
            ContactsContract.RawContacts.ACCOUNT_NAME + " =?))";

    /**
     *
     */
    private final String[] SELECTION_ARGS = {
            "SIM1", "SIM2", "Phone"
    };

    ContactsAdapter mAdapter;
    private Button finishButton;
    private ListView contactsView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        // Initializing Views
        finishButton = (Button) findViewById(R.id.finish_button_id);
        contactsView = (ListView) findViewById(R.id.contacts_view_id);
        Log.d(MainActivity.LOG_TAG, "Views initialized");



        Log.d(MainActivity.LOG_TAG, "ConAct: Proceeding with database query");

        // Concerning contacts database
        String[] fromColumns = {ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY};
        int[] toViews = {R.id.name_view};

        mAdapter = new ContactsAdapter(this, null, fromColumns, toViews);
        contactsView.setAdapter(mAdapter);

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(MainActivity.LOG_TAG, "ContactsAcitivty: onDestroy()");
    }







    // Implementing LoaderCallbacks. See https://developer.android.com/guide/topics/ui/layout/listview.html
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(MainActivity.LOG_TAG, "ConAct: onCreateLoader()");
        return new CursorLoader(this, ContactsContract.RawContacts.CONTENT_URI,
                PROJECTION, SELECTION, SELECTION_ARGS, SORT_ORDER);
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(MainActivity.LOG_TAG, "ConAct: onLoadFinished()");
        mAdapter.swapCursor(data);
    }

    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(MainActivity.LOG_TAG, "ConAct: onLoaderReset()");
        mAdapter.swapCursor(null);
    }


    /**
     * Called when the finishButton is clicked.
     * @param v
     */
    public void onFinishButtonClick (View v) {
        Log.d(MainActivity.LOG_TAG, "Finish Button is clicked");
        ArrayList<String> selectedContacts = mAdapter.getSelectedContacts();
        ArrayList<Long> selectedContactIds = mAdapter.getSelectedContactIds();
        ArrayList<String> selectedContactNumbers = getNumbersByIds(selectedContactIds);

        logStrings(selectedContacts);
        logLongs(selectedContactIds);
        logStrings(selectedContactNumbers);

        finish();
    }

    private void logStrings(ArrayList<String> list) {
        for(String element : list) {
            Log.d(MainActivity.LOG_TAG, element);
        }
    }

    private void logLongs(ArrayList<Long> list) {
        for(Long element : list) {
            Log.d(MainActivity.LOG_TAG, element.toString());
        }
    }




    private ArrayList<String> getNumbersByIds(ArrayList<Long> ids) {
        ArrayList<String> result = new ArrayList<String>(0);

        String[] projection = new String[] {
                ContactsContract.Data.RAW_CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER,
                ContactsContract.Data.MIMETYPE
        };

        String selection = "(" + ContactsContract.Data.MIMETYPE + " =?)";

        String[] selectionArgs = new String[] {ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE};

        Cursor cursor = getContentResolver().query(
                ContactsContract.Data.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null
        );

        while(cursor.moveToNext()) {
            Long cursorId = cursor.getLong(cursor.getColumnIndex(
                    ContactsContract.Data.RAW_CONTACT_ID));
            for(Long listId : ids) {
                if(listId.equals(cursorId)) {
                    String number = cursor.getString(cursor.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER));
                    String normNumber = cursor.getString(cursor.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER));
                    if(number != null)
                        result.add(number);
                    if(normNumber != null)
                        result.add(normNumber);
                    break;
                }
            }
        }

        cursor.close();

        return result;
    }
}
