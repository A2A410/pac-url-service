package com.headuck.httpserver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import java.io.IOException;

import static com.headuck.httpserver.App.CHANNEL_ID;


public class HttpService extends Service {
    private static final String TAG = "HttpService";

    private HttpServer mHttpd;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Start command");
        int port = intent.getIntExtra("port", 8080);
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setOngoing(true)
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setContentTitle("PAC Url Service")
                .setContentText("Url: http://localhost:" + port+"/direct.pac")
                .setSmallIcon(R.drawable.ic_notification_duck)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);
        mHttpd = new HttpServer(port);

        try {
            mHttpd.start();
            Utils.savePreference(this.getApplicationContext(), true, port);
            return START_STICKY;
        } catch (IOException e) {
            e.printStackTrace();
            stopSelf();
            return START_NOT_STICKY;
        }

    }

    @Override
    public void onDestroy() {
        if (mHttpd != null && mHttpd.wasStarted()) {
            mHttpd.stop();
        }
        super.onDestroy();
    }



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
