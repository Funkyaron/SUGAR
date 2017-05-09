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
    implements ActivityCompat.OnRequestPermissionsResultCallback,
        LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * Request code to identify the request for contacts permissions.
     */
    private final int REQUEST_CONTACTS = 1;

    /**
     * Permissions we need to read and write contacts.
     */
    private final String[] PERMISSION_CONTACTS = {Manifest.permission.READ_CONTACTS,
        Manifest.permission.WRITE_CONTACTS};

    /**
     * These are the contacts database columns that we will retrieve.
     */
    private final String[] PROJECTION = {ContactsContract.Data._ID,
        ContactsContract.Data.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER};

    /**
     * We will order the query result by display name.
     */
    private final String SORT_ORDER = ContactsContract.Data.DISPLAY_NAME;

    /**
     * We query for every contact that contains a NORMALIZED_NUMBER
     */
    private final String SELECTION = "((" +
            ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER + " NOTNULL) AND (" +
            ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER + " != '' ))";

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

        // Concerning runtime permissions
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED)
        {
            Log.d(MainActivity.LOG_TAG, "ConAct: Permissions not granted, sending request.");
            ActivityCompat.requestPermissions(this, PERMISSION_CONTACTS, REQUEST_CONTACTS);
        } else {

            Log.d(MainActivity.LOG_TAG, "ConAct: Proceeding with database query");

            // Concerning contacts database
            String[] fromColumns = {ContactsContract.Data.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER};
            int[] toViews = {R.id.name_view, R.id.number_view};

            mAdapter = new SimpleCursorAdapter(this, R.layout.list_item_contacts,
                    null, fromColumns, toViews, 0);
            contactsView.setAdapter(mAdapter);

            getLoaderManager().initLoader(0, null, this);
        }
    }



    // Handling runtime permissions
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults)
    {
        Log.d(MainActivity.LOG_TAG, "ConAct: onRequestPermissionsResult()");
        if (requestCode == REQUEST_CONTACTS)
        {
            if(verifyPermissions(grantResults))
                Toast.makeText(this, "Berechtigungen genehmigt", Toast.LENGTH_LONG).show();
            else
            {
                Toast.makeText(this, "Berechtigungen nicht genehmigt", Toast.LENGTH_LONG).show();
            }
        }
        else
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private boolean verifyPermissions(int[] grantResults)
    {
        if (grantResults.length < 1)
            return false;

        for (int result : grantResults)
        {
            if (result != PackageManager.PERMISSION_GRANTED)
                return false;
        }

        return true;
    }



    // Implementing LoaderCallbacks. See https://developer.android.com/guide/topics/ui/layout/listview.html
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(MainActivity.LOG_TAG, "ConAct: onCreateLoader()");
        return new CursorLoader(this, ContactsContract.Data.CONTENT_URI,
                PROJECTION, SELECTION, null, SORT_ORDER);
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
