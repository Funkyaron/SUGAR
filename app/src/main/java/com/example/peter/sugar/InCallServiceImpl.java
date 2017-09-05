package com.example.peter.sugar;

import android.content.Context;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Vibrator;
import android.provider.Settings;
import android.telecom.Call;
import android.telecom.InCallService;
import android.telecom.TelecomManager;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Funkyaron on 11.04.2017.
 */

public class InCallServiceImpl extends InCallService {

    static boolean shouldBlockAbsolutely = false;

    private int currentRingerMode;
    private AudioManager mAudioManager;

    @Override
    public void onCallAdded(Call call) {

        String number = getNumber(call);
        Log.d(MainActivity.LOG_TAG, "Number: " + number);
        Call.Details det = call.getDetails();
        Log.d(MainActivity.LOG_TAG, "CallerDisplayName: " + det.getCallerDisplayName());

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        currentRingerMode = mAudioManager.getRingerMode();
        mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);

        if(shouldBlock(number))
            call.reject(false, "Zur zeit leider nicht erreichbar");
        else {
            mAudioManager.setRingerMode(currentRingerMode);
            super.onCallAdded(call);
        }
    }

    @Override
    public void onCallRemoved(Call call) {
        mAudioManager.setRingerMode(currentRingerMode);
        super.onCallRemoved(call);
    }

    // Copy-pasted from built-in phone app
    String getNumber(Call call) {
        if (call == null) {
            return null;
        }
        if (call.getDetails().getGatewayInfo() != null) {
            return call.getDetails().getGatewayInfo()
                    .getOriginalAddress().getSchemeSpecificPart();
        }
        Uri handle = getHandle(call);
        return handle == null ? null : handle.getSchemeSpecificPart();
    }

    Uri getHandle(Call call) {
        return call == null ? null : call.getDetails().getHandle();
    }



    // Self-made ;)
    private boolean shouldBlock(String number) {
        if(shouldBlockAbsolutely)
            return true;

        Profile[] allProfiles = Profile.readAllProfiles(this);
        for(Profile prof : allProfiles) {
            if(!(prof.isAllowed())) {
                if(prof.getMode() == Profile.MODE_BLOCK_ALL)
                    return true;
                else if(prof.getMode() == Profile.MODE_BLOCK_SELECTED && prof.getPhoneNumbers().contains(number))
                    return true;
                else if(prof.getMode() == Profile.MODE_BLOCK_NOT_SELECTED && !(prof.getPhoneNumbers().contains(number)))
                    return true;
            }
        }
        return false;
    }

    /* private boolean shouldBlock(String number) {
        return true;
    } */
}
