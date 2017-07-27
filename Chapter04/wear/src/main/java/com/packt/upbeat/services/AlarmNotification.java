package com.packt.upbeat.services;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.packt.upbeat.services.WearStepService;

/**
 * Created by ashok.kumar on 20/05/17.
 */

public class AlarmNotification extends BroadcastReceiver {

    private static final String TAG = "AlarmNotification";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "alarm fired");
        context.startService(new Intent(context, WearStepService.class));
    }
}