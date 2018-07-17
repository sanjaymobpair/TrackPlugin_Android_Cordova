package com.track.app;

/**
 * Created by ${Mobpair} on 12/3/18.
 */

import android.os.Handler;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseIIDService";
    private Util util;
    String refreshedToken;

    @Override
    public void onTokenRefresh() {
        util = new Util(this);
        // Get updated InstanceID token.
        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        util.setFCMToken(refreshedToken);
    }

}