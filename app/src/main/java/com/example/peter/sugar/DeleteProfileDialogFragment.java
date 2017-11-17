package com.example.peter.sugar;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import java.io.File;

/**
 * Created by SHK on 15.11.17.
 */

public class DeleteProfileDialogFragment extends DialogFragment
{
    @Override
    public Dialog onCreateDialog(Bundle savedInstances)
    {
        final String chosenProfileName = getArguments().getString("toBeDeletedProfile");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Möchten Sie wirklich das Profil '" +  chosenProfileName + "' löschen?");
        builder.setPositiveButton("Ja",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface,int id)
            {
                File destinationDirectory = getActivity().getFilesDir();
                File toBeDeletedFile = new File(destinationDirectory,chosenProfileName+".xml");
                boolean isFileDeletedSuccesfully = toBeDeletedFile.delete();
            }
        });
        builder.setNegativeButton("Nein",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface,int id)
            {
                dismiss();
            }
        });
        return builder.create();
    }
}
