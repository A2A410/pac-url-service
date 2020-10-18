package com.headuck.httpserver;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;

public class Utils {
    public static boolean isServiceRunning(final Context context, final String className) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> info = activityManager.getRunningServices(Integer.MAX_VALUE);
        if (info == null || info.size() == 0) return false;
        for (ActivityManager.RunningServiceInfo aInfo : info) {
            if (className.equals(aInfo.service.getClassName())) return true;
        }
        return false;
    }

    public static void savePreference(Context context, boolean started, int port) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("PacPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("started", started);
        editor.putInt("port", port);
        editor.commit();
    }

    public static class PacPref {
        int port;
        boolean started;
    }

    public static PacPref getPreference(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("PacPref", Context.MODE_PRIVATE);
        PacPref pref = new PacPref();
        pref.port = sharedPreferences.getInt("port", 8080);
        pref.started = sharedPreferences.getBoolean("started", false);
        return pref;
    }
}
