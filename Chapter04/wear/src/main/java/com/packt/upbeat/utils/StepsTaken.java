package com.packt.upbeat.utils;


import android.util.Log;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by ashok.kumar on 20/05/17.
 */


public class StepsTaken implements Serializable {

    private static int steps = 0;
    private static long lastUpdateTime = 0L;
    private static final String TAG = "StepsTaken";

    public static void updateSteps(int stepsTaken) {
        steps += stepsTaken;

        // today
        Calendar tomorrow = new GregorianCalendar();
        tomorrow.setTimeInMillis(lastUpdateTime);
        // reset hour, minutes, seconds and millis
        tomorrow.set(Calendar.HOUR_OF_DAY, 0);
        tomorrow.set(Calendar.MINUTE, 0);
        tomorrow.set(Calendar.SECOND, 0);
        tomorrow.set(Calendar.MILLISECOND, 0);

        // next day
        tomorrow.add(Calendar.DAY_OF_MONTH, 1);

        Calendar now = Calendar.getInstance();

        if (now.after(tomorrow)) {
            Log.d(TAG, "I think it's tomorrow, resetting");
            steps = stepsTaken;
        }

        lastUpdateTime = System.currentTimeMillis();
    }

    public static int getSteps() {
        return steps;
    }
}