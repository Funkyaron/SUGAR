package com.example.peter.sugar;

import android.content.Context;
import android.media.AudioManager;
import android.net.Uri;
import android.telecom.Call;
import android.telecom.InCallService;
import android.util.Log;

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
            call.reject(true, getString(R.string.reject_message));
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


    /**
     * Checks the DoNotDisturb-condition and all profiles and determines
     * if the given number should currently be blocked.
     * @param number The phone number to be checked.
     * @return True if the number should be blocked, false if not.
     */
    private boolean shouldBlock(String number) {
        Log.d(MainActivity.LOG_TAG, "InCallServiceImpl: shouldBlock()");
        if(shouldBlockAbsolutely) {
            Log.d(MainActivity.LOG_TAG, "DoNotDisturb applies");
            return true;
        }

        Profile[] allProfiles = Profile.readAllProfiles(this);
        // This foreach-loop goes through all existing profiles. If the number matches any blocking condition,
        // the loop will be canceled and true will be returned.
        for(Profile prof : allProfiles) {
            Log.d(MainActivity.LOG_TAG, prof.toString());
            // Skip this profile, if it allows calls.
            if(!(prof.isAllowed())) {
                Log.d(MainActivity.LOG_TAG, "Profile does not allow. Checking for numbers");
                Log.d(MainActivity.LOG_TAG, "Mode: " + prof.getMode());
                // Go through any possible mode.
                if(prof.getMode() == Profile.MODE_BLOCK_ALL) {
                    // Always block.
                    Log.d(MainActivity.LOG_TAG, "Profile should block + MODE_BLOCK_ALL");
                    return true;
                }
                else if(prof.getMode() == Profile.MODE_BLOCK_SELECTED) {
                    // Block only if the given number is selected in the profile.
                    if(prof.getPhoneNumbers().contains(number)) {
                        Log.d(MainActivity.LOG_TAG, "Profile should block + MODE_BLOCK_SELECTED");
                        return true;
                    }
                }
                else if(prof.getMode() == Profile.MODE_BLOCK_NOT_SELECTED) {
                    // Block only if the given number is not selected in the profile.
                    if(!(prof.getPhoneNumbers().contains(number))) {
                        Log.d(MainActivity.LOG_TAG, "Profile should block + MODE_BLOCK_NOT_SELECTED");
                        return true;
                    }
                }
            }
        }
        // If we made it until here, there is no profile that wants to block the given phone number.
        Log.d(MainActivity.LOG_TAG, "Everything's fine -> Call allowed");
        return false;
    }
}
