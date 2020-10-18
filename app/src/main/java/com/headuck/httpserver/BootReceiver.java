package com.headuck.httpserver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.content.ContextCompat;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Utils.PacPref pref = Utils.getPreference(context);
        if (pref.started) {
            Intent serviceIntent = new Intent(context.getApplicationContext(), HttpService.class);
            serviceIntent.putExtra("port", pref.port);
            ContextCompat.startForegroundService(context.getApplicationContext(), serviceIntent);
        }
    }
}
