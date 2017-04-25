package com.example.peter.sugar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class DisableProfileReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String[] categories = (String[]) intent.getCategories().toArray();
        String profileName = categories[0];
        //XMLProfileParser.Profile prof = readProfile(profileName);

        //ProfileUpdateUtil.disable(profileName);
        //ProfileUpdateUtil.setNextDisable(context, prof);
    }
}
