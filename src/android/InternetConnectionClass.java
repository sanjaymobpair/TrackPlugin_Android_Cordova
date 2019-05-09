package com.track.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by ${Mobpair} on 3/4/18.
 * it will check Internet Connectivity
 */
public class InternetConnectionClass {

    @SuppressLint("StaticFieldLeak")
    private static Context context;
    @SuppressLint("StaticFieldLeak")
    private static InternetConnectionClass instance = new InternetConnectionClass();
    private boolean connected = false;

    public static InternetConnectionClass getInstance(Context ctx) {
        context = ctx.getApplicationContext();
        return instance;
    }

    /**
     * it will check is device is Online Or Not
     * @return true or false
     */
    public boolean isOnline() {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            assert connectivityManager != null;
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.d("connectivity", e.toString());
        }
        return connected;
    }
}
