package com.packt.smartchat;

import android.app.Application;

import com.firebase.client.Firebase;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Created by ashok.kumar on 02/06/17.
 */

public class PacktApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
        FirebaseMessaging.getInstance().subscribeToTopic("pushNotifications");
    }
}
