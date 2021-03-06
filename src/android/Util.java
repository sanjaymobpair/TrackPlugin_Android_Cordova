package com.track.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

import static com.track.app.MyFirebaseInstanceIDService.isFcmTokenGet;
import static com.track.app.Track.isApiKeyDataGet;

/**
 * Created by ${Mobpair} on 6/3/18.
 */

/**
 * {@link Util class is used to store sharedpreference }
 */
class Util {

    @SuppressLint("StaticFieldLeak")
    private static Context mContext;
    private static String TAG = Util.class.getName(), CURRENT_DATE = "currentdate";
    private static String REFFERER = "refferer", CLICKID = "clickid", FCMTOKEN = "fcmtoken";
    private static String APIKEY = "apikey", SERVERKEY = "serverkey", USERAGENT = "useragent", BOOLEAN = "boolean", ISFIRSTTIME = "isFirst", STORELIST = "storelist";
    private static String ISERROR = "iserror", DOMAINENDPOINT = "domainpoint", ISDATAGET = "DATAGET";
    private final SharedPreferences mPrefs;
    private static final String PREFERENCES = "settings";

    /**
     * @param context context of used class
     */

    Util(final Context context) {
        mPrefs = context.getSharedPreferences(PREFERENCES, 0);
        mContext = context;
    }

    /**
     * putString method is used for store string value preference
     *
     * @param name  KEY NAME
     * @param value VALUE OF THAT KEY
     */

    private void putString(String name, String value) {
        mPrefs.edit().putString(name, value).apply();
    }

    private void putBoolean(String name, Boolean value) {
        mPrefs.edit().putBoolean(name, value).apply();
    }

    private void putList(String name, Set<String> value) {
        mPrefs.edit().putStringSet(name, value).apply();
    }

    @SuppressLint("CommitPrefEdits")
    void pushStringList(List<String> list, String uniqueListName) {

        mPrefs.edit().putInt(uniqueListName + "_size", list.size());

        for (int i = 0; i < list.size(); i++) {
            //mPrefs.edit().remove(uniqueListName + i);
            mPrefs.edit().putString(uniqueListName + i, list.get(i));
        }
        mPrefs.edit().apply();
    }

    List<String> pullStringList(String uniqueListName) {

        List<String> result = new ArrayList<>();
        int size = mPrefs.getInt(uniqueListName + "_size", 0);

        for (int i = 0; i < size; i++) {
            result.add(mPrefs.getString(uniqueListName + i, null));
        }
        return result;
    }

    /**
     * set Current Date in preference
     *
     * @param date date you want to store
     */

    void setCurrentDate(String date) {
        putString(CURRENT_DATE, date);
    }

    /**
     * Get Date Stored in preference
     *
     * @return return current date stored in preference
     */

    String getCurrentDate() {
        return mPrefs.getString(CURRENT_DATE, "null");
    }

    void setBoolean(Boolean value) {
        putBoolean(BOOLEAN, value);
    }

    boolean getBoolean() {
        return mPrefs.getBoolean(BOOLEAN, false);
    }

    void setDomainEndPoint(String domainEndPoint) {
        putString(DOMAINENDPOINT, domainEndPoint);
    }

    String getDomainEndPoint() {
        return mPrefs.getString(DOMAINENDPOINT, "null");
    }

    void setIsFirstTime(Boolean isFirstTime) {
        putBoolean(ISFIRSTTIME, isFirstTime);
    }

    private void setErrorResponse(Boolean isError) {
        putBoolean(ISERROR, isError);
    }


    /**
     * for store refferer in preference
     *
     * @param refferer value of refferer
     */

    void setRefferer(String refferer) {
        putString(REFFERER, refferer);
    }


    String getRefferer() {
        return mPrefs.getString(REFFERER, "null");
    }

    /**
     * a
     * setclick id get from api dat
     *
     * @param clickId store clickid
     */
    private void setClickId(String clickId) {
        putString(CLICKID, clickId);
    }

    String getClickID() {
        return mPrefs.getString(CLICKID, "null");
    }

    // TODO: 16/4/18 set fcm token
    void setFCMToken(String fcmtoken) {
        putString(FCMTOKEN, fcmtoken);
    }

    // TODO: 16/4/18 get fcm token
    String getFCMToken() {
        return mPrefs.getString(FCMTOKEN, "null");
    }

    // TODO: 16/4/18 set api key
    void setApiKey(String apikey) {
        putString(APIKEY, apikey);
    }

    // TODO: 16/4/18 get api key
    String getApiKey() {
        return mPrefs.getString(APIKEY, "null");
    }

    // TODO: 16/4/18 set server key
    void setServerKey(String serverkey) {
        putString(SERVERKEY, serverkey);
    }

    // TODO: 16/4/18 get server key
    String getServerKey() {
        return mPrefs.getString(SERVERKEY, "null");
    }

    // TODO: 16/4/18 set useragent
    void setUserAgent(String userAgent) {
        putString(USERAGENT, userAgent);
    }

    // TODO: 16/4/18 get useragent
    String getUserAgent() {
        return mPrefs.getString(USERAGENT, "null");
    }

    @SuppressLint({"ObsoleteSdkInt", "HardwareIds"})
    private static String DeviceId(Context context) {
        @SuppressLint("HardwareIds")
        String ANDROID_ID = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            ANDROID_ID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return ANDROID_ID;
    }

    /**
     * Handle Url Call
     *
     * @param URL            url get through response
     * @param postDataParams post Data To Pass
     * @return
     */
    private static String getResponseofPost(String URL, HashMap<String, String> postDataParams) {
        java.net.URL url;
        String response = "";
        try {
            // TODO: 2019-05-09 set Url 
            url = new URL(URL);

            // TODO: 2019-05-09 OpenUrlConnection Using HttpURLConnection Class 
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // TODO: 2019-05-09 Read Timeout 50 sec. 
            conn.setReadTimeout(50000);
            // TODO: 2019-05-09 Connection TImeout 50 sec. 
            conn.setConnectTimeout(50000);
            // TODO: 2019-05-09 Read TimeOut 50sec. 
            conn.setReadTimeout(50000);
            // TODO: 2019-05-09 Connection Input/Ouput set true 
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // TODO: 2019-05-09 Store OutPut Stream to get connection OutPut Stream 
            OutputStream os = conn.getOutputStream();

            // TODO: 2019-05-09 store OutPut Stream into the BufferedWriter Class 
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));

            // TODO: 2019-05-09 Write params
            writer.write(getPostDataString(postDataParams));

            // TODO: 2019-05-09 writer flush and close 
            writer.flush();
            writer.close();

            // TODO: 2019-05-09 Close OuPut Stream 
            os.close();

            // TODO: 2019-05-09 Get REsponse COde 
            int responseCode = conn.getResponseCode();
            Log.d("Util", "Response Code" + URL + " - " + responseCode);
            
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                Log.d("Util", "If" + responseCode);
                String line;
                // TODO: 2019-05-09 BufferedReader read Stream
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                // TODO: 2019-05-09 store line in response
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else if (responseCode == HttpURLConnection.HTTP_CLIENT_TIMEOUT) {
                Log.d("Util", "TimeOut" + responseCode);
                response = "";
            }
            Log.d(TAG, "response :" + response);

            if (response.equals("")) {
                Log.d("jai", "response : null" + response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    private static String getPostDataString(HashMap<String, String> params) throws
            UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }

    /**
     * Calling Tracking API
     */
    public static class callapi extends AsyncTask<String, String, String> {
        String token, apikey, serverkey, usergent, refferer, event_id, domainEndPoint;

        callapi(String token, String apikey, String serverkey, String userAgent, String refferer, String eventid, String domainendpoint) {
            this.token = token;
            this.apikey = apikey;
            this.serverkey = serverkey;
            this.usergent = userAgent;
            this.refferer = refferer;
            this.event_id = eventid;
            this.domainEndPoint = domainendpoint;
        }

        @Override
        protected void onPreExecute() {
            Log.d("Util", "" + usergent);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            Util util = new Util(mContext);
            if (s == null || s.equals("")) {
                util.setErrorResponse(true);
                Log.d("Util", "Response : " + s);
            } else {
                util.setErrorResponse(false);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String message = jsonObject.getString("message");
                    Boolean response = jsonObject.getBoolean("response");
                    String data = jsonObject.getString("data");

                    // TODO: 2019-05-09 set Response 
                    util.setClickId(data);
                    util.setBoolean(response);

                    if (response) {
                        isApiKeyDataGet = false;
                        isFcmTokenGet = false;
                    }
                    Log.d("Util", "Response ParaMeter : " + message + ":" + response + ":" + data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("mtoken", token);
            hashMap.put("eventid", event_id);
            hashMap.put("deviceid", Util.DeviceId(mContext));
            hashMap.put("apikey", apikey);
            hashMap.put("legacy", serverkey);
            hashMap.put("useragent", usergent);
            hashMap.put("refferer", refferer);
            Log.d("Util", "HashMap " + hashMap.toString());
            Log.d("Util", "DomainEndPoint :  " + domainEndPoint);
            String url = Util.getResponseofPost("http://" + domainEndPoint + "/site/track.html?", hashMap);
            //String url = Util.getResponseofPost("http://technology.makeaff.com:8081/frontend/web/site/track?", hashMap);
            Log.d("Util", "Url " + url);
            return url;
        }
    }

}