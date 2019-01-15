package com.track.app;

/**
 * Created by ${Mobpair} on 12/3/18.
 */

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;

public class MyFirebaseInstanceIDService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseIIDService";
    private Util util;
    public static Boolean isFcmTokenGet = false;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        util = new Util(this);
        // Get updated InstanceID token.
        Log.d(TAG, "Refreshed token: " + s);
        util.setFCMToken(s);
//        TrackLib.getInstance().getFcmToken(this, s);
        isFcmTokenGet = true;
        TrackLib.getInstance().init(this);
    }
}