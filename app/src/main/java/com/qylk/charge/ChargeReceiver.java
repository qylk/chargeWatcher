package com.qylk.charge;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ChargeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("ChargeReceiver", intent.getAction());
        if (Intent.ACTION_POWER_CONNECTED.equals(intent.getAction())) {
            Intent intentService = new Intent(context, ChargeListenerService.class);
            intentService.putExtra(ChargeListenerService.ACT, ChargeListenerService.ACT_PLUGIN);
            context.startService(intentService);
        } else if (Intent.ACTION_POWER_DISCONNECTED.equals(intent.getAction())) {
            Intent intentService = new Intent(context, ChargeListenerService.class);
            intentService.putExtra(ChargeListenerService.ACT, ChargeListenerService.ACT_UNPLUGIN);
            context.startService(intentService);
        }
    }
}
