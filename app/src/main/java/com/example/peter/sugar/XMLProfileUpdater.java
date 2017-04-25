package com.example.peter.sugar;

 import org.apache.commons.net.ftp.*;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.FileInputStream;

/**
 * Created by Peter on 19.04.2017.
 */

public class XMLProfileUpdater{

    public static String[] display_files ( String host, int port, String usr, String pwd ) throws IOException
    {
        FTPClient client = new FTPClient();
        String files[];
        try {
            client.connect(host,port);
            client.login(usr,pwd);
            files = client.listNames();
            client.logout();
        } finally {
            client.disconnect();
        }

        return files;
    }

    public static boolean download_file ( String output_path, String remote_path, boolean message) throws IOException
    {
        FTPClient client = new FTPClient();
        FileOutputStream downloaded_file = null;
        boolean error = false;

        try {
            client.connect("RandomClient",22);

        } finally {

        }
        return true;
    }
}

