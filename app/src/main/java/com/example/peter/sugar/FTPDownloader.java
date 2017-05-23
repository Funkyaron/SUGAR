package com.example.peter.sugar;

/**
 * Created by Peter on 17.05.2017.
 */

import android.content.Context;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.commons.net.ftp.*;
import org.xmlpull.v1.XmlPullParserException;

public class FTPDownloader {
    private FTPClient androidPhone = new FTPClient();
    private ArrayList<Profile> downloadedProfiles = new ArrayList<Profile>(0);
    private Context context;

    /**
     *
     * @param customServer represents the name of the server you want to connect to
     * @param customUser represents the user which is accessing the server
     * @param customPassword represents the password of the user
     * @param customPort represents the port which has to be used
     * @param conContext represents the current context
     * @return
     * @throws IOException
     * @throws XmlPullParserException
     */
    public ArrayList<Profile> retrieveProfileFromServer(String customServer,String customUser,String customPassword,int customPort,Context conContext) throws IOException,XmlPullParserException
    {
        context = conContext;
        try {
            androidPhone.connect(customServer, customPort);
            androidPhone.login(customUser,customPassword);
            androidPhone.enterLocalPassiveMode();
            int replyCode = androidPhone.getReplyCode();
            if(!FTPReply.isPositiveCompletion(replyCode))
            {
                return new ArrayList<Profile>(0);
            }
            FTPFile ftpFiles[] = androidPhone.listFiles();
            String fileArray[] = new String[ftpFiles.length];
            for( int currentFTPFile = 0; currentFTPFile < fileArray.length; currentFTPFile++ )
            {
                fileArray[currentFTPFile] = ftpFiles[currentFTPFile].getName();
            }
            File downloadDirectories[] = new File[fileArray.length];
            for( int currentFile = 0; currentFile < downloadDirectories.length; currentFile++ )
            {
                downloadDirectories[currentFile] = new File(fileArray[currentFile]+".xml");
            }
            boolean successfullyDownloaded = true;
            for( int currentDownloadProcess = 0; currentDownloadProcess < downloadDirectories.length; currentDownloadProcess++ )
            {
                FileOutputStream downloadResult = null;
                downloadResult = new FileOutputStream(fileArray[currentDownloadProcess]);
                successfullyDownloaded = androidPhone.retrieveFile(fileArray[currentDownloadProcess], downloadResult);
                ProfileParser resultParser = new ProfileParser();
                FileInputStream profileInput = context.openFileInput(fileArray[currentDownloadProcess]);
                Profile result = resultParser.parse(profileInput,context);
                downloadedProfiles.add(result);
                downloadResult.close();
                profileInput.close();
                if(!successfullyDownloaded)
                    break;
            }
            if( successfullyDownloaded )
            {
                return downloadedProfiles;
            } else if (!successfullyDownloaded  ) {
                return new ArrayList<Profile>(0);
            }
            return new ArrayList<Profile>(0);
        } catch ( IOException e ) {
            e.printStackTrace();
            Log.d("FTP ERROR : ","Unfortunately the requested files couldn't be found anywhere on the server : " + e.getMessage());
            return new ArrayList<Profile>(0);
        } catch ( XmlPullParserException e ) {
            Log.d("XML ERROR : ","Unfortunately the xml files have a bad format : " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<Profile>(0);
        }
    }
}
