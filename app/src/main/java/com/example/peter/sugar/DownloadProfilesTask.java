package com.example.peter.sugar;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.net.ftp.*;
import org.xmlpull.v1.XmlPullParserException;

import java.util.*;
import java.io.*;

/**
 * Created by Peter on 30.05.2017.
 */

class DownloadProfilesTask extends AsyncTask<String,Void,Boolean>
{

    Context context;
    FTPClient androidClient = new FTPClient();

    public DownloadProfilesTask(Context conContext)
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
                if( serverFiles[currentFile].getName().equals(".."))
                {
                    continue;
                } else {
                    String currentFileName = serverFiles[currentFile].getName();
                    FileOutputStream fos = context.openFileOutput(currentFileName, Context.MODE_PRIVATE);
                    isSuccessfull = androidClient.retrieveFile(serverFiles[currentFile].getName(), fos);
                    fos.close();
                }
            }
            return isSuccessfull;
        } catch ( IOException exception ) {
            Log.d("IO ERROR : ",exception.getMessage());
            return isSuccessfull;
        } finally {
            try {
                androidClient.logout();
            } catch ( IOException exception ) {
                Log.d("LOGOUT ERROR : ",exception.getMessage());
            }
        }
    }

    protected void onPostExecute()
    {
    }
}
