package com.headuck.httpserver;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private EditText editTextInput;
    private TextView tv;
    private Switch mainSwitch;
    int port = 8080;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.server_info);
        editTextInput = findViewById(R.id.edit_text_input);
        mainSwitch = findViewById(R.id.main_switch);
        Utils.PacPref pref = Utils.getPreference(this.getApplicationContext());
        port = pref.port;

        setText();
        boolean isRunning = setSwitch();

        if (isRunning != pref.started) {
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mainSwitch.toggle();
                    toogleService(mainSwitch);
                }
            }, 500);

        }
    }

    public void toogleService(View v) {
        Log.d(TAG, "Toggle service");
        if (mainSwitch.isChecked()) {
            Log.d(TAG, "Switch checked");
            startService(v);
        } else {
            Log.d(TAG, "Switch unchecked");
            stopService(v);
        }
    }

    public void startService(View v) {
        Log.d(TAG, "Start service");
        String input = editTextInput.getText().toString();
        if (!"".equals(input.trim())) {
            try {
                port = Integer.valueOf(input);
            } catch (NumberFormatException e) {
            }
        }
        Intent serviceIntent = new Intent(this, HttpService.class);
        serviceIntent.putExtra("port", port);
        setText();
        ContextCompat.startForegroundService(this, serviceIntent);
    }

    public void stopService(View v) {
        Log.d(TAG, "Stop service");
        Intent serviceIntent = new Intent(this, HttpService.class);
        stopService(serviceIntent);
        Utils.savePreference(this.getApplicationContext(), false, port);
    }

    public void setClipboard(View v) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("url", "http://localhost:"+port+"/direct.pac");
        clipboard.setPrimaryClip(clip);
    }

    private void setText() {
        StringBuilder serverInfo = new StringBuilder().append("PAC URL: http://").
                append("localhost").append(":").append(port).append("/direct.pac").append("\n");
        tv.setText(serverInfo);
    }

    private boolean setSwitch() {
        boolean isRunning = Utils.isServiceRunning(this, HttpService.class.getName());
        Log.d(TAG, "Check service running: " + isRunning);
        mainSwitch.setChecked(isRunning);
        return isRunning;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    /* public String getWifiIpAddress() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        return android.text.format.Formatter.formatIpAddress(info.getIpAddress());
    } */
}
