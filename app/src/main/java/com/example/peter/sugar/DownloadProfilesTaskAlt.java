package com.example.peter.sugar;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.commons.net.ftp.*;
import org.xmlpull.v1.XmlPullParserException;

import java.util.*;
import java.io.*;

/**
 * Created by aron on 30.05.2017.
 */

class DownloadProfilesTaskAlt extends AsyncTask<String,Void,Boolean>
{

    Activity context;
    FTPClient androidClient = new FTPClient();

    public DownloadProfilesTaskAlt(Activity conContext)
    {
        context = conContext;
    }

    protected Boolean doInBackground(String... params)
    {
        Log.d(MainActivity.LOG_TAG, "DownloadProfilesTask: doInBackground()");
        boolean isSuccessfull = false;
        String serverName = params[0];
        int serverPort = Integer.parseInt(params[1]);
        String serverUser = params[2];
        String serverPassword = params[3];
        try {
            androidClient.connect(serverName,serverPort);
            androidClient.login(serverUser,serverPassword);
            androidClient.enterLocalPassiveMode();
            androidClient.setFileType(FTP.BINARY_FILE_TYPE);
            androidClient.changeWorkingDirectory("SUGAR");

            FTPFile[] serverFiles = androidClient.listFiles();
            File[] localFiles = context.getFilesDir().listFiles();

            Log.d(MainActivity.LOG_TAG, "Server files count: " + serverFiles.length);
            Log.d(MainActivity.LOG_TAG, "Local files count: " + localFiles.length);

            boolean localMatchesServer;
            for(int i = 0; i < localFiles.length; i++) {
                localMatchesServer = false;
                for(int j = 0; j < serverFiles.length; j++) {
                    if((localFiles[i].getName()).equals(serverFiles[j].getName())) {
                        localMatchesServer = true;
                    }
                }
                if(!localMatchesServer) {
                    localFiles[i].delete();
                }
            }

            for( int currentFile = 0; currentFile < serverFiles.length; currentFile++ )
            {
                String currentFileName = serverFiles[currentFile].getName();
                if( ! currentFileName.equals("..")) {
                    File localFile = new File(context.getFilesDir(), currentFileName);

                    if(!localFile.exists()) {
                        Log.d(MainActivity.LOG_TAG, "Retrieving file from server");
                        FileOutputStream fos = context.openFileOutput(currentFileName, Context.MODE_PRIVATE);
                        isSuccessfull = androidClient.retrieveFile(currentFileName, fos);
                        fos.close();
                    }
                }
            }
            return isSuccessfull;
        } catch ( IOException exception ) {
            Log.e(MainActivity.LOG_TAG, exception.toString());
            return isSuccessfull;
        } finally {
            try {
                androidClient.logout();
            } catch ( IOException exception ) {
                Log.e(MainActivity.LOG_TAG,exception.toString());
            }
            try {
                androidClient.disconnect();
            } catch(IOException e) {
                Log.e(MainActivity.LOG_TAG, e.toString());
            }
        }
    }


    protected void onPostExecute(Boolean isSuccessful) {
        Log.d(MainActivity.LOG_TAG, "DownloadProfilesBlah ist fertig" + isSuccessful);
    }
}