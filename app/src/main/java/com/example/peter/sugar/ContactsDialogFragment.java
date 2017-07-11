package com.example.peter.sugar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Funkyaron on 09.06.2017.
 *
 * The aim of this dialog is to select the contacts / phone numbers the user wants to
 * associate with a profile.
 *
 * The activity that opens this dialog has to do 2 things:
 * 1. Implement the ContactsDialogFragment.ContactsSelectedListener interface.
 *    Otherwise it will throw an exception.
 *    The onContactsSelected()-method is invoked when the user clicks the positive
 *    button and receives the phone numbers of the selected contacts as parameter.
 * 2. Pass the profile name via the setArguments()-method. The dialog will read the
 *    numbers that are already associated with this profile and marks the correspondent
 *    contacts as checked when opening the dialog.
 *
 * The parent activity can use the onContactsSelected()-method to do whatever it wants
 * to do with the numbers.
 */

public class ContactsDialogFragment extends DialogFragment {

    public interface ContactsSelectedListener {
        void onContactsSelected(ArrayList<String> numbers);
    }

    private ArrayList<String> mNumbers;
    private ArrayList<Long> mRawContactIds;
    private ContactsSelectedListener mListener;
    private Cursor mDataCursor;
    private Cursor mRawCursor;

    @Override
    public void onAttach(Context context) {
        Log.d(MainActivity.LOG_TAG, "CDF: onAttach()");
        super.onAttach(context);

        // Instanciate the parent activity as listener.
        try {
            mListener = (ContactsSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    " must implement ContactsDialogFragment.ContactsSelectedListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(MainActivity.LOG_TAG, "CDF: onCreateDialog()");

        // Read the numbers already  associated with the profile. If an error occurs,
        // it will be an empty list.
        try {
            Bundle args = getArguments();
            String profileName = (String) args.get(MainActivity.KEY_PROFILE_NAME);
            Profile profile = Profile.readProfileFromXmlFile(profileName, getActivity());
            mNumbers = profile.getPhoneNumbers();
        } catch (Exception e) {
            Toast.makeText(getActivity(), R.string.read_contacts_failed, Toast.LENGTH_LONG).show();
            mNumbers = new ArrayList<>(0);
        }

        // For information about the cursors see below.
        // The RawContacts query is the one that is displayed to the user.
        mDataCursor = getDataCursor();
        mRawCursor = getRawCursor();

        // Now we have to reverse-engine the checked items, so we extract the RawContact IDs
        // from the data table using the list of numbers. Then we go through the RawContacts
        // query to identify the checked items using the IDs.
        mRawContactIds = getIdsByNumbers(mNumbers, mDataCursor);
        boolean[] checkedItems = getCheckedItemsByIds(mRawContactIds, mRawCursor);

        // Here we get the contact names which are displayed in the list out of the
        // RawContacts table.
        CharSequence[] names = getNames(mRawCursor);

        // The contacts appear twice in the Data table but once in the RawContacts table,
        // so we need to fetch the selected IDs again, this time from the RawContacts table.
        mRawContactIds = getIdsByCheckedItems(checkedItems, mRawCursor);

        // Finally, we can actually build the dialog.
        // Every time a contact is checked or unchecked we modify the list of RawContact IDs.
        // When the user is finished, we look up the phone numbers in the Data table and pass
        // them back to the parent activity by invoking the onContactsSelected()-method.
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.prompt_select_contacts)
               .setMultiChoiceItems(names,
                       checkedItems,
                       new DialogInterface.OnMultiChoiceClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which,
                                               boolean isChecked) {
                               mRawCursor.moveToPosition(which);
                               if(isChecked) {
                                   Long addedId = mRawCursor.getLong(
                                           mRawCursor.getColumnIndex(ContactsContract.RawContacts._ID));
                                   mRawContactIds.add(addedId);
                               } else {
                                   Long removedId = mRawCursor.getLong(
                                           mRawCursor.getColumnIndex(ContactsContract.RawContacts._ID));
                                   mRawContactIds.remove(removedId);
                               }
                           }
                       })
               .setPositiveButton(R.string.finish, new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                       mNumbers = getNumbersByIds(mRawContactIds, mDataCursor);
                       mListener.onContactsSelected(mNumbers);
                   }
               })
               .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                       getDialog().cancel();
                   }
               });
        return builder.create();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.d(MainActivity.LOG_TAG, "CDF: onDismiss()");
        mDataCursor.close();
        mRawCursor.close();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Log.d(MainActivity.LOG_TAG, "CDF: onCancel()");
        Toast.makeText(getActivity(), R.string.on_cancel, Toast.LENGTH_LONG).show();
    }






    /* Read the Android contacts database developer guide.
     *
     * We need to query two tables: the RawContacts table (see getRawCursor())
     * and the Data table (see getDataCursor()).
     *
     * It may be that multiple rows for the same contact appear even in the RawContacts table.
     * That is because the entries are created by different accounts. This information exists
     * only in this table, so we need the RawContacts table to differ these multiple
     * appearances of the same contact by ContactsContract.RawContacts.ACCOUNT_NAME. We
     * query for SIM1, SIM2 and Phone to get off everything that's created by WhatsApp et al.
     * Additionally, when a contact is deleted, it is NOT removed from the RawContacts table,
     * Android only sets the DELETED-Flag. So we also filter for all contacts that are not
     * marked for deletion.
     * The RawContacts query result has to be sorted by name because it is presented to the user.
     *
     * Unfortunately the phone numbers are not saved in the RawContacts table, so we need to
     * do a 2nd query for the Data table. We can use the RawContact-IDs as a bridge between
     * these two tables - they are saved in both of them.
     * We look for both the NUMBER and the NORMALIZED_NUMBER to make the blocklist more
     * secure when reading the phone number from an incoming call.
     * We look for only those rows where actually a phone number is saved (MIMETYPE).
     */

    private Cursor getDataCursor() {
        Log.d(MainActivity.LOG_TAG, "CDF: getDataCursor()");

        String[] dataProjection = new String[] {
                ContactsContract.Data.RAW_CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER,
                ContactsContract.Data.MIMETYPE};
        String dataSelection =
                "(" + ContactsContract.Data.MIMETYPE + " =?)";
        String[] dataSelectionArgs = new String[] {
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE};

        return getActivity().getContentResolver().query(
                ContactsContract.Data.CONTENT_URI,
                dataProjection,
                dataSelection,
                dataSelectionArgs,
                null);
    }

    private Cursor getRawCursor() {
        Log.d(MainActivity.LOG_TAG, "CDF: getRawCursor()");

        String[] rawProjection = {
                ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY,
                ContactsContract.RawContacts._ID,
                ContactsContract.RawContacts.ACCOUNT_NAME,
                ContactsContract.RawContacts.DELETED};
        String rawSelection = //"(((" +
                //ContactsContract.RawContacts.ACCOUNT_NAME + " =?) OR (" +
                //ContactsContract.RawContacts.ACCOUNT_NAME + " =?) OR (" +
                //ContactsContract.RawContacts.ACCOUNT_NAME + " =?)) AND (" +
                "(" + ContactsContract.RawContacts.DELETED + " =?) AND (" +
                ContactsContract.RawContacts.ACCOUNT_NAME + " !=?)" /* + "))" */ ;
        String[] rawSelectionArgs = {
                /*"SIM1", "SIM2", "Phone", */ "0", "WhatsApp"};
        String rawSortOrder =
                ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY;

        return getActivity().getContentResolver().query(
                ContactsContract.RawContacts.CONTENT_URI,
                rawProjection,
                rawSelection,
                rawSelectionArgs,
                rawSortOrder);
    }







    private ArrayList<Long> getIdsByNumbers(ArrayList<String> numbers, Cursor dataCursor) {
        Log.d(MainActivity.LOG_TAG, "CDF: getIdsByNumbers()");

        ArrayList<Long> rawContactIds = new ArrayList<>(0);

        dataCursor.moveToPosition(-1);
        while(dataCursor.moveToNext()) {
            for(String currentNumber : numbers) {
                if(currentNumber.equals(
                        dataCursor.getString(dataCursor.getColumnIndex(
                        ContactsContract.CommonDataKinds.Phone.NUMBER))))
                {
                    rawContactIds.add(dataCursor.getLong(dataCursor.getColumnIndex(
                            ContactsContract.Data.RAW_CONTACT_ID)));
                    break;
                }
            }
        }
        return rawContactIds;
    }

    private ArrayList<String> getNumbersByIds(ArrayList<Long> ids, Cursor dataCursor) {
        Log.d(MainActivity.LOG_TAG, "CDF: getNumbersByIds()");

        ArrayList<String> numbers = new ArrayList<>(0);

        dataCursor.moveToPosition(-1);
        while(dataCursor.moveToNext()) {
            Long cursorId = dataCursor.getLong(dataCursor.getColumnIndex(
                    ContactsContract.Data.RAW_CONTACT_ID));
            for(Long listId : ids) {
                if(listId.equals(cursorId)) {
                    String number = dataCursor.getString(dataCursor.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER));
                    String normNumber = dataCursor.getString(dataCursor.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER));
                    if(number != null)
                        numbers.add(number);
                    if(normNumber != null)
                        numbers.add(normNumber);
                    break;
                }
            }
        }
        return numbers;
    }

    private boolean[] getCheckedItemsByIds(ArrayList<Long> ids, Cursor rawCursor) {
        Log.d(MainActivity.LOG_TAG, "CDF: getCheckedItemsByIds()");

        boolean[] checkedItems = new boolean[rawCursor.getCount()];
        for(int i = 0; i < checkedItems.length; i++) {
            checkedItems[i] = false;
        }

        rawCursor.moveToPosition(-1);
        while(rawCursor.moveToNext()) {
            for(Long id : ids) {
                if(id.equals(rawCursor.getLong(rawCursor.getColumnIndex(
                        ContactsContract.RawContacts._ID))))
                {
                    checkedItems[rawCursor.getPosition()] = true;
                    break;
                }
            }
        }
        return checkedItems;
    }

    private CharSequence[] getNames(Cursor rawCursor) {
        Log.d(MainActivity.LOG_TAG, "CDF: getNames()");

        String[] names = new String[rawCursor.getCount()];

        rawCursor.moveToPosition(-1);
        while(rawCursor.moveToNext()) {
            names[rawCursor.getPosition()] = rawCursor.getString(rawCursor.getColumnIndex(
                    ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY));
                    // ContactsContract.RawContacts.ACCOUNT_NAME));
        }
        return names;
    }

    private ArrayList<Long> getIdsByCheckedItems(boolean[] checkedItems, Cursor rawCursor) {
        ArrayList<Long> ids = new ArrayList<>(0);
        for(int i = 0; i < checkedItems.length; i++) {
            if(checkedItems[i]) {
                rawCursor.moveToPosition(i);
                ids.add(rawCursor.getLong(rawCursor.getColumnIndex(ContactsContract.RawContacts._ID)));
            }
        }
        return ids;
    }

    @Deprecated
    private void logIds(ArrayList<Long> ids) {
        Log.d(MainActivity.LOG_TAG, "Selected Ids:");
        for(Long id : ids) {
            Log.d(MainActivity.LOG_TAG, id.toString());
        }
    }
}
