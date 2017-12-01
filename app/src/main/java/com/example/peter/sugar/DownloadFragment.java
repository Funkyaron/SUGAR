package com.example.peter.sugar;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by SHK on 23.11.17.
 */

public class DownloadFragment extends DialogFragment
{
    public interface DownloadFragmentListener
    {
        public void onDialogPositiveClick(DialogFragment dialog);
    }

    private String hostAdress;
    private String userName;
    private String userPassword;
    DownloadFragmentListener listenForDownloadedFiles;

    private EditText serverAdressInput;
    private EditText userNameInput;
    private EditText userPasswordInput;
    private Button confirmDownload;
    private Button cancelDownload;

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try {
            listenForDownloadedFiles = (DownloadFragmentListener) activity;
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanced)
    {
        setRetainInstance(true);
        hostAdress = "";
        userName = "";
        userPassword = "";
        AlertDialog.Builder downloadDialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.download_profiles_dialog,null);
        downloadDialogBuilder.setView(rootView);
        serverAdressInput = (EditText) rootView.findViewById(R.id.serverName);
        userNameInput = (EditText) rootView.findViewById(R.id.serverUser);
        userPasswordInput = (EditText) rootView.findViewById(R.id.serverPassword);
        confirmDownload = (Button) rootView.findViewById(R.id.downloadProfileButton);
        cancelDownload = (Button) rootView.findViewById(R.id.cancelDownloadButton);
         confirmDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                hostAdress = serverAdressInput.getText().toString();
                userName = userNameInput.getText().toString();
                userPassword = userPasswordInput.getText().toString();
                String port = String.valueOf(21);
                Log.d(MainActivity.LOG_TAG,"Login data : " + "[" + hostAdress + "," + port + userName + "," + userPassword + "]");
                new DownloadProfilesTaskAlt(getActivity()).execute(hostAdress,port,userName,userPassword);
                dismiss();
            }
        });
        cancelDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dismiss();
            }
        });
        return downloadDialogBuilder.create();
    }

    @Override
    public void onDestroyView()
    {
        Dialog downloadDialogFragment = getDialog();

        if( downloadDialogFragment != null && getRetainInstance() )
        {
            downloadDialogFragment.setDismissMessage(null);
        }
        super.onDestroyView();
    }
}
