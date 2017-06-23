package com.example.peter.sugar;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.apache.commons.net.ftp.*;
import org.xmlpull.v1.XmlPullParserException;

import java.util.*;
import java.io.*;

/**
 * Created by Peter on 30.05.2017.
 */

class DownloadProfilesTask extends AsyncTask<String,Void,Boolean>
{

    Activity context;
    FTPClient androidClient = new FTPClient();

    public DownloadProfilesTask(Activity conContext)
    {
        context = conContext;
    }

    protected Boolean doInBackground(String... params)
    {
        boolean isSuccessfull = false;
        ArrayList<Profile> result = new ArrayList<Profile>(0);
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
            FTPFile serverFiles[] = androidClient.listFiles();
            for( int currentFile = 0; currentFile < serverFiles.length; currentFile++ )
            {
                if( ! serverFiles[currentFile].getName().equals("..")) {
                    String currentFileName = serverFiles[currentFile].getName();
                    FileOutputStream fos = context.openFileOutput(currentFileName, Context.MODE_PRIVATE);
                    isSuccessfull = androidClient.retrieveFile(serverFiles[currentFile].getName(), fos);
                    fos.close();
                }
            }
            return isSuccessfull;
        } catch ( IOException exception ) {
            Log.e(MainActivity.LOG_TAG,exception.toString());
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
    }
}
