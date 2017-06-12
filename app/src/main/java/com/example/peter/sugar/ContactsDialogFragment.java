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
 * Test
 */

public class ContactsDialogFragment extends DialogFragment {

    public interface ContactsSelectedListener {
        void onContactsSelected(ArrayList<String> numbers);
    }

    private Profile mProfile;
    private String mProfileName;
    private ArrayList<String> mNumbers;
    private ArrayList<Long> mRawContactIds;
    private ContactsSelectedListener mListener;
    private Cursor mDataCursor;
    private Cursor mRawCursor;

    @Override
    public void onAttach(Context context) {
        Log.d(MainActivity.LOG_TAG, "CDF: onAttach()");
        super.onAttach(context);
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
        try {
            Bundle args = getArguments();
            mProfileName = (String) args.get(MainActivity.KEY_PROFILE_NAME);
            mProfile = Profile.readProfileFromXmlFile(mProfileName, getActivity());
            mNumbers = mProfile.getPhoneNumbers();
        } catch (Exception e) {
            Log.d(MainActivity.LOG_TAG, "No Profile name delivered");
            mProfile = new Profile(getActivity());
            mNumbers = new ArrayList<>(0);
        }

        mDataCursor = getDataCursor();
        mRawCursor = getRawCursor();

        mRawContactIds = getIdsByNumbers(mNumbers, mDataCursor);

        boolean[] checkedItems = getCheckedItemsByIds(mRawContactIds, mRawCursor);
        CharSequence[] names = getNames(mRawCursor);

        Log.d(MainActivity.LOG_TAG, "Initialization finished, building dialog");

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
                                   mRawContactIds.add(mRawCursor.getLong(
                                           mRawCursor.getColumnIndex(ContactsContract.RawContacts._ID)));
                               } else {
                                   mRawContactIds.remove(mRawCursor.getLong(
                                           mRawCursor.getColumnIndex(ContactsContract.RawContacts._ID)));
                               }
                           }
                       })
               .setPositiveButton(R.string.finish, new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                       mNumbers = getNumbersByIds(mRawContactIds, mDataCursor);
                       mDataCursor.close();
                       mRawCursor.close();
                       mListener.onContactsSelected(mNumbers);
                   }
               })
               .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                       mDataCursor.close();
                       mRawCursor.close();
                       getDialog().cancel();
                   }
               });
        Log.d(MainActivity.LOG_TAG, "Dialog configuration done");
        return builder.create();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Toast.makeText(getActivity(), R.string.on_cancel, Toast.LENGTH_LONG).show();
    }





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
                ContactsContract.RawContacts.ACCOUNT_NAME};
        String rawSelection = "((" +
                ContactsContract.RawContacts.ACCOUNT_NAME + " =?) OR (" +
                ContactsContract.RawContacts.ACCOUNT_NAME + " =?) OR (" +
                ContactsContract.RawContacts.ACCOUNT_NAME + " =?))";
        String[] rawSelectionArgs = {
                "SIM1", "SIM2", "Phone"};
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
        }
        return names;
    }
}
