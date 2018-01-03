package com.jxb.myrfid.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.jxb.myrfid.activity.MainActivity;

/**
 * Created by jxb on 2017-12-27.
 */

public class ServiceBroadcastReceiver extends BroadcastReceiver {
    private static String START_ACTION = "NotifyServiceStart";
    private static String STOP_ACTION = "NotifyServiceStop";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(USBService.TAG, Thread.currentThread().getName() + "---->"
                + "ServiceBroadcastReceiver onReceive");

        String action = intent.getAction();
        if (START_ACTION.equalsIgnoreCase(action)) {

            Intent actIntent = new Intent(context.getApplicationContext(), MainActivity.class);
            actIntent.setAction("android.intent.action.MAIN");
            actIntent.addCategory("android.intent.category.LAUNCHER");
            actIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(actIntent);


            context.startService(new Intent(context, USBService.class));
            Log.d(USBService.TAG, Thread.currentThread().getName() + "---->"
                    + "ServiceBroadcastReceiver onReceive start end");

        } else if (STOP_ACTION.equalsIgnoreCase(action)) {

            context.stopService(new Intent(context, USBService.class));
            Log.d(USBService.TAG, Thread.currentThread().getName() + "---->"
                    + "ServiceBroadcastReceiver onReceive stop end");
        }
    }
}
