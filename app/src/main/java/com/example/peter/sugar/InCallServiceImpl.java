package com.example.peter.sugar;

import android.net.Uri;
import android.telecom.Call;
import android.telecom.InCallService;
import android.telecom.TelecomManager;
import android.util.Log;

/**
 * Created by Funkyaron on 11.04.2017.
 */

public class InCallServiceImpl extends InCallService {

    @Override
    public void onCallAdded(Call call) {

        //Ton aus

        if (getNumber(call).equals("+4917635183695")) {
            call.disconnect();
            //Ton wieder an (für nächsten Anruf)
        } else {
            //Ton wieder an
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
