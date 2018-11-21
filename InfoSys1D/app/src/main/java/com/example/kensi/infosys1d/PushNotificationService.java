package com.example.kensi.infosys1d;

import android.content.SharedPreferences;

import com.google.firebase.messaging.FirebaseMessagingService;

public class PushNotificationService extends FirebaseMessagingService {
    private static final String TAG = "access_token";
    public PushNotificationService() {
    }

    @Override
    public void onNewToken(String token) {
        /*
            Save the Instance ID into the SharedPreference.
            This is used later in the LoginMain.java onCreate() to save the Instance ID to the database.
            SharedPreference ID: FCM_ID
            key: ID (Instance ID)
            key: uploaded (Flag for uploading the Instance ID to database)
         */
        SharedPreferences FCM = getSharedPreferences("FCM_ID", MODE_PRIVATE);
        SharedPreferences.Editor editor = FCM.edit();
        editor.putString("ID", token);
        editor.putInt("uploaded", 0);
        editor.apply();
    }

}