package com.example.peter.sugar;

import android.content.Context;
import android.media.AudioManager;
import android.net.Uri;
import android.telecom.Call;
import android.telecom.InCallService;
import android.telecom.TelecomManager;
import android.util.Log;

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

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        currentRingerMode = mAudioManager.getRingerMode();
        mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);

        /* Set ringer mode silent only does'nt work when the ringer is currently
         * in vibration mode.
         * Suggestion: Use Vibrator class via getSystemService(Context.VIBRATION_SERVICE);
         * Needs android.permission.VIBRATE -> runtime permission -.-
         */

        if (number.equals("+4917635183695") || number.equals("017635183695")) {
            call.disconnect();
            mAudioManager.setRingerMode(currentRingerMode);
        } else {
            mAudioManager.setRingerMode(currentRingerMode);
            super.onCallAdded(call);
        }
    }


    // From built-in phone app
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
}
