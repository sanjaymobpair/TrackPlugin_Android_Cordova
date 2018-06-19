package com.track.app;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.webkit.WebView;

/**
 * Created by ${Mobpair} on 21/3/18.
 */

public class TrackLib {
    private String TAG = TrackLib.class.getName();
    @SuppressLint("StaticFieldLeak")
    private static TrackLib instance = new TrackLib();
    private Util util;
    private String refferer_chk;
    private String userAgent;
    private Context mContext;

    public static TrackLib getInstance() {
        return instance;
    }

    void onReceive(Context context, Intent intent) {
        String REFFERER_VALUE = "referrer";
        String referrer = intent.getStringExtra(REFFERER_VALUE);

        if (referrer != null) {
            util = new Util(context);
            util.setRefferer(referrer);
            Log.d(TAG, "refferer : " + referrer);
        } else {
            Log.d(TAG, "refferer : Else");
        }
    }

    public void init(Context context) {
        mContext = context;
        util = new Util(context);

        if (util.getUserAgent().equals("null")) {
            userAgent = new WebView(context).getSettings().getUserAgentString();
            util.setUserAgent(userAgent);
        }

        if (util.getRefferer() != null) {
            refferer_chk = util.getRefferer();
        }
    }

    public void updateFCMToken(Context mContext, String fcmToken) {
        util = new Util(mContext);
        util.setIsFirstTime(false);

        String serverKey = util.getServerKey();
        String apiKey = util.getApiKey();
        refferer_chk = util.getRefferer();
        userAgent = util.getUserAgent();
        String domainEndPoint = util.getDomainEndPoint();

        Log.d(TAG, "FcmToken : " + fcmToken);
        Log.d(TAG, "ServerKey : " + serverKey);
        Log.d(TAG, "ApiKey : " + apiKey);
        Log.d(TAG, "Refferer_Check : " + refferer_chk);
        Log.d(TAG, "UserAgent : " + userAgent);
        Log.d(TAG, "DomainEndPoint : " + domainEndPoint);

        String eventId = "INSTALL";
        Boolean res = util.getBoolean();
        Log.d(TAG, "Boolean" + res);

        if (serverKey.equals("null") || apiKey.equals("null") || domainEndPoint.equals("null")) {
            Log.d(TAG, " Please Reopen Your App..not getting some data");
        } else {
            Log.d(TAG, ":: IsOnline");
            if (res) {
                Log.d(TAG, ":: IF");
            } else {
                Log.d(TAG, ":: Else");
                new Util.callapi(fcmToken, apiKey, serverKey, userAgent, refferer_chk, eventId, domainEndPoint).execute();
            }
        }
    }
}