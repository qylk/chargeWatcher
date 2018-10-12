package com.qylk.charge;

import android.accessibilityservice.AccessibilityService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

public class ChargeListenerService extends AccessibilityService {
    public static int ACT_FULL = 1;
    public static int ACT_PLUGIN = 2;
    public static int ACT_UNPLUGIN = 3;
    public static String ACT = "act";
    private static final String TAG = "ChargeListenerService";
    private BroadcastReceiver receiver;
    private BroadcastReceiver receiver2;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.d(TAG, "onAccessibilityEvent");
    }

    @Override
    public void onInterrupt() {
        Log.d(TAG, "onInterrupt");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int act = intent.getIntExtra(ACT, 0);
        Log.d(TAG, "onStartCommand:" + act);
        if (act == ACT_FULL) {
            notification(true);
        } else if (act == ACT_PLUGIN) {
            notification(false);
            if (receiver2 == null) {
                receiver2 = new ChargeReceiver2();
            }
            registerReceiver(receiver2, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        } else if (act == ACT_UNPLUGIN) {
            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            nm.cancelAll();
            if (receiver2 != null) {
                unregisterReceiver(receiver2);
                receiver2 = null;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        IntentFilter ift = new IntentFilter();
        ift.addAction(Intent.ACTION_POWER_CONNECTED);
        ift.addAction(Intent.ACTION_POWER_DISCONNECTED);
        receiver = new ChargeReceiver();
        registerReceiver(receiver, ift);
    }

    private void notification(boolean sound) {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setDefaults(0)
                .setAutoCancel(false)
                .setContentTitle(sound ? "充电已完成" : "正在充电中...")
                .setContentText(sound ? "请及时拔掉电源" : "充电完成时将响铃")
                .setOngoing(true)
                .setContentIntent(PendingIntent.getActivity(this, 1,
                        new Intent(), PendingIntent.FLAG_UPDATE_CURRENT));
        if (sound) {
            builder.setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.ripple));
        }
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(101, builder.build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
        if (receiver2 != null) {
            unregisterReceiver(receiver2);
        }
    }
}

