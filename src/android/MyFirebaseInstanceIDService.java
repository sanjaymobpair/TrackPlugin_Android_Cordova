package com.track.app;

/**
 * Created by ${Mobpair} on 12/3/18.
 */

import android.os.Handler;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseIIDService";
    private Util util;
    String refreshedToken;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        util = new Util(this);
        // Get updated InstanceID token.
        Log.d(TAG, "Refreshed token: " + s);
        util.setFCMToken(s);
        TrackLib.getInstance().updateFCMToken(this, s);
    }
}