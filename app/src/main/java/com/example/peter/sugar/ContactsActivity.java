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
import android.view.View;
import android.widget.Button;
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
        ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup.NORMALIZED_NUMBER};

    /**
     * We query for every contact that contains a NORMALIZED_NUMBER
     */
    private final String SELECTION = "((" +
            ContactsContract.PhoneLookup.NORMALIZED_NUMBER + " NOTNULL) AND (" +
            ContactsContract.PhoneLookup.NORMALIZED_NUMBER + " != '' ))";

    SimpleCursorAdapter mAdapter;
    private Button finishButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);


        // Concerning runtime permissions
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, PERMISSION_CONTACTS, REQUEST_CONTACTS);
            // Activity will be finished automatically if the permission will not be granted.
        }


        // Concerning contacts database
        String[] fromColumns = {ContactsContract.PhoneLookup.DISPLAY_NAME,
            ContactsContract.PhoneLookup.NORMALIZED_NUMBER};
        int[] toViews = {R.id.name_view, R.id.number_view};

        mAdapter = new SimpleCursorAdapter(this, R.layout.list_item_contacts,
                null, fromColumns, toViews, 0);

        getLoaderManager().initLoader(0, null, this);


        finishButton = (Button) findViewById(R.id.finish_button_id);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                finish();
            }
        });
    }



    // Handling runtime permissions
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults)
    {
        if (requestCode == REQUEST_CONTACTS)
        {
            if(verifyPermissions(grantResults))
                Toast.makeText(this, "Berechtigungen genehmigt", Toast.LENGTH_LONG).show();
            else
            {
                Toast.makeText(this, "Berechtigungen nicht genehmigt", Toast.LENGTH_LONG).show();
                finish();
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
        return new CursorLoader(this, ContactsContract.Data.CONTENT_URI,
                PROJECTION, SELECTION, null, null);
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
