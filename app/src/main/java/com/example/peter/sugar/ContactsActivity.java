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

public class ContactsActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {



    /**
     * These are the contacts database columns that we will retrieve.
     */
    private final String[] PROJECTION = {
            ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY,
            ContactsContract.RawContacts._ID,
            ContactsContract.RawContacts.CONTACT_ID,
            ContactsContract.RawContacts.ACCOUNT_NAME,
            ContactsContract.RawContacts.ACCOUNT_TYPE
    };

    /**
     * We will order the query result by display name.
     */
    private final String SORT_ORDER = ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY;

    /**
     * We query for every contact that contains a NORMALIZED_NUMBER
     */
    private final String SELECTION = "((" +
            ContactsContract.RawContacts.ACCOUNT_NAME + " =?) OR (" +
            ContactsContract.RawContacts.ACCOUNT_NAME + " =?) OR (" +
            ContactsContract.RawContacts.ACCOUNT_NAME + " =?))";

    private final String[] SELECTION_ARGS = {
            "SIM1", "SIM2", "Phone"
    };

    SimpleCursorAdapter mAdapter;
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
        String[] fromColumns = {ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY,
                ContactsContract.RawContacts.ACCOUNT_NAME};
        int[] toViews = {R.id.name_view, R.id.number_view};

        mAdapter = new SimpleCursorAdapter(this, R.layout.list_item_contacts,
                null, fromColumns, toViews, 0);
        contactsView.setAdapter(mAdapter);

        getLoaderManager().initLoader(0, null, this);



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
        finish();
    }
}
