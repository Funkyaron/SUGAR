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

import java.util.ArrayList;

/**
 * Created by Funkyaron on 11.04.2017.
 */

public class InCallServiceImpl extends InCallService {

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

        if(!shouldBlock(number)) {
            mAudioManager.setRingerMode(currentRingerMode);
        }
        super.onCallAdded(call);
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



    private boolean shouldBlock(String number) {
        boolean result = false;

        BlockList blockList = new BlockList(this);
        ArrayList<String> blockedNumbers = blockList.getBlockedNumbers();
        for(String blockedNumber : blockedNumbers) {
            if(number.equals(blockedNumber)) {
                result = true;
                break;
            }
        }

        if(result) {
            Log.d(MainActivity.LOG_TAG, "Call should be blocked");
        } else {
            Log.d(MainActivity.LOG_TAG, "Call is allowed");
        }

        return result;
    }
}
