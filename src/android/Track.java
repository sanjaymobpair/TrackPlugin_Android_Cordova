package com.track.app;

import android.content.Context;
import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * This class echoes a string called from JavaScript.
 */
public class Track extends CordovaPlugin {

    private String TAG = Track.class.getName();
    private Context context;
    private Util util;
    static Boolean isApiKeyDataGet=false;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        context = this.cordova.getActivity().getApplicationContext();
        util = new Util(context);

        Log.d(TAG, "TOKEN GET ");
        if (action.equals("startTrack")) {
            String serverKey = args.getString(0);
            String apiKey = args.getString(1);
            String domainendPoint = args.getString(2);

            if (util.getServerKey().equals("null") || util.getApiKey().equals("null") || util.getDomainEndPoint().equals("null")) {
                util.setServerKey(serverKey);
                util.setApiKey(apiKey);
                util.setDomainEndPoint(domainendPoint);

                Log.d(TAG, "Init : ServerKey" + serverKey + "ApiKey :" + apiKey + "FcmToken" + domainendPoint + "::" + util.getFCMToken());
            } else {
                Log.d(TAG, "::: Else");
            }
            isApiKeyDataGet = true;
            cordova.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TrackLib.getInstance().init(context);
                }
            });
            return true;
        } else if (action.equals("stage")) {
            String eventId = args.getString(0);
            if (InternetConnectionClass.getInstance(context).isOnline()) {
                Log.d(TAG, "dailyTrack Called");
                List<String> list = util.pullStringList("unique");
                Log.d(TAG, "List : " + list);
                startDailyTrack(eventId);
            } else {
                List list = new ArrayList();
                list.add(eventId);
                util.pushStringList(list, "unique");
                Log.d(TAG, "Lists : " + eventId);
            }
        }
        return false;
    }


    private void startDailyTrack(String eventId) {
        String fcmtoken, serverkey, apikey, useragent, clickId, domainendpoint;
        fcmtoken = util.getFCMToken();
        serverkey = util.getServerKey();
        apikey = util.getApiKey();
        useragent = util.getUserAgent();
        clickId = util.getClickID();
        domainendpoint = util.getDomainEndPoint();
        new Util.callapi(fcmtoken, apikey, serverkey, useragent, clickId, eventId, domainendpoint).execute();
    }
}