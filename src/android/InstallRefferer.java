package com.track.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * It will call when any one download from playstore for get refferer of the apk
 */
public class InstallRefferer extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        /**
         * send context or intent to the @TrackLib class
         */
        TrackLib.getInstance().onReceive(context, intent);
        Log.d("InstallRefferer ::","onReceiver");
    }
}
