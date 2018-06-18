package com.track.app;

/**
 * Created by ${Mobpair} on 12/3/18.
 */

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseIIDService";
    private Util util;

    @Override
    public void onTokenRefresh() {
        util = new Util(this);
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken + " ::: " + util.getFCMToken());
        util.setFCMToken(refreshedToken);
    }
}