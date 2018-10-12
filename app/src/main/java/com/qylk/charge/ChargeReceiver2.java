package com.qylk.charge;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.util.Log;

public class ChargeReceiver2 extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("ChargeReceiver2", intent.getAction());
        if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, BatteryManager.BATTERY_STATUS_UNKNOWN);
            if (status == BatteryManager.BATTERY_STATUS_FULL) {
                Intent intentService = new Intent(context, ChargeListenerService.class);
                intentService.putExtra(ChargeListenerService.ACT, ChargeListenerService.ACT_FULL);
                context.startService(intentService);
            }
        }
    }
}
