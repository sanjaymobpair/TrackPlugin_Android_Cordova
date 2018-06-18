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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by ${Mobpair} on 6/3/18.
 */

/**
 * {@link Util class is used to store sharedpreference }
 */
class Util {

    @SuppressLint("StaticFieldLeak")
    private static Context mContext;
    private static String CURRENT_DATE = "currentdate";
    private static String REFFERER = "refferer", CLICKID = "clickid", FCMTOKEN = "fcmtoken";
    private static String APIKEY = "apikey", SERVERKEY = "serverkey", USERAGENT = "useragent", BOOLEAN = "boolean", ISFIRSTTIME = "isFirst", STORELIST = "storelist";
    private static String ISERROR = "iserror", DOMAINENDPOINT = "domainpoint";
    private final SharedPreferences mPrefs;
    private static final String PREFERENCES = "settings";
    private String TAG = Util.class.getName();

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
     * @param name
     * @param value
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

    public void pushStringList(List<String> list, String uniqueListName) {

        mPrefs.edit().putInt(uniqueListName + "_size", list.size());

        for (int i = 0; i < list.size(); i++) {
            //mPrefs.edit().remove(uniqueListName + i);
            mPrefs.edit().putString(uniqueListName + i, list.get(i));
        }
        mPrefs.edit().apply();
    }

    public List<String> pullStringList(String uniqueListName) {

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

    boolean getIsFirstTime() {
        return mPrefs.getBoolean(ISFIRSTTIME, true);
    }

    void setErrorResponse(Boolean isError) {
        putBoolean(ISERROR, isError);
    }

    boolean getErrorResponse() {
        return mPrefs.getBoolean(ISERROR, false);
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
    void setClickId(String clickId) {
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

    void setOfflineStage(String list) {
        Set<String> lists = new HashSet<>();
        lists.add(list);
        putList(STORELIST, lists);
    }

    Set<String> getofflineStage() {
        return mPrefs.getStringSet(STORELIST, null);
    }


    @SuppressLint({"ObsoleteSdkInt", "HardwareIds"})
    public static String DeviceId(Context context) {
        @SuppressLint("HardwareIds")
        String ANDROID_ID = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            ANDROID_ID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return ANDROID_ID;
    }

    public static String getResponseofPost(String URL, HashMap<String, String> postDataParams) {
        java.net.URL url;
        String response = "";
        try {
            url = new URL(URL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(50000);
            conn.setConnectTimeout(50000);
            conn.setReadTimeout(50000);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));

            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            int responseCode = conn.getResponseCode();
            Log.d("Util", "Response Code" + URL + " - " + responseCode);
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                Log.d("Util", "If" + responseCode);
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else if (responseCode == HttpURLConnection.HTTP_CLIENT_TIMEOUT) {
                Log.d("Util", "TimeOut" + responseCode);
                response = "";
            }
            Log.d("jai", "response :" + response);

            if (response == null || response == "" || response.equals("")) {
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
                    util.setClickId(data);
                    util.setBoolean(response);
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