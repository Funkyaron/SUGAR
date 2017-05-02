package com.example.peter.sugar;

import android.content.Context;
import android.util.Log;

import org.apache.commons.net.ftp.*;
import java.io.*;

/**
 * Created by Peter on 19.04.2017.
 */

class XMLProfileUpdater
{
    Context context;
    private FTPClient androidClient;
    private int reply;
    String gnuTest = "https://ftp.gnu.org";

    /**
     * Downloads the profile from the FTP-Server. Note that the app only downloads from the public
     * GNU server to test the Apache Commons Net library.
     * @throws IOException is thrown if the profile file is not located in the FRP
     * @throws FTPConnectionClosedException is thrown if the connection can't be established
     */
    public void downloadProfile() throws IOException, FTPConnectionClosedException
    {
        androidClient.connect(gnuTest);
        androidClient.enterLocalPassiveMode();
        reply = androidClient.getReplyCode();
        androidClient.setFileType(FTP.BINARY_FILE_TYPE);

        if( !FTPReply.isPositiveCompletion(reply))
        {
            androidClient.disconnect();
            Log.d(MainActivity.LOG_TAG,"XMLProfileUpdater : Bad reply code!");
        }
        else
        {
            String remoteFilePath = "/gnu/GNUinfo/README";
            String fileName = "README";
            InputStream source = androidClient.retrieveFileStream(remoteFilePath);
            FileOutputStream target = new FileOutputStream(context.getFilesDir() + "/" + fileName);
            byte buffer[] = new byte[4096];
            long count = 0L;
            int n = 0;
            while( -1 != (n = source.read(buffer)))
            {
                target.write(buffer,0,n);
                count = count + n;
            }
        }
    }
}

