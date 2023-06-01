package com.onthemove.commons.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.onthemove.responseClasses.LoginResponse;


import java.util.Map;
import java.util.Set;

public class AppPref {

    public static final String FCM_TOKEN = "FCM_TOKEN";
    public static final String LAT = "LAT";
    public static final String LNG = "LNG";
    public static final String VEHICLE_NO = "VEHICLE_NO";
    public static final String VEHICLE_REGISTER = "VEHICLE_REGISTER";
    public static final String TASK_ID_PART_DATA = "TASK_ID_PART_DATA";


    public static final String LAST_DIST = "LAST_DIST";

    private static AppPref sInstance;
    private static SharedPreferences sPref;
    private static SharedPreferences.Editor sEditor;

    private AppPref(Context context) {
        sPref = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        sEditor = sPref.edit();
    }

    public static AppPref getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new AppPref(context);
        }
        return sInstance;
    }

    //set methods
    public void set(String key, String value) {
        sEditor.putString(key, value).commit();
    }

    public void set(String key, boolean value) {
        sEditor.putBoolean(key, value).commit();
    }

    public void set(String key, float value) {
        sEditor.putFloat(key, value).commit();
    }

    public void set(String key, int value) {
        sEditor.putInt(key, value).commit();
    }

    public void set(String key, long value) {
        sEditor.putLong(key, value).commit();
    }

    public void set(String key, Set<String> value) {
        sEditor.putStringSet(key, value).commit();
    }

    // get methods
    public int getInt(String key, int defaultVal) {
        return sPref.getInt(key, defaultVal);
    }

    public int getInt(String key) {
        return sPref.getInt(key, 0);
    }

    public String getString(String key, String defaultVal) {
        return sPref.getString(key, defaultVal);
    }

    public String getString(String key) {
        return sPref.getString(key, "");
    }


    public boolean getBoolean(String key, boolean defaultVal) {
        return sPref.getBoolean(key, defaultVal);
    }

    public boolean getBoolean(String key) {
        return sPref.getBoolean(key, false);
    }


    public float getFloat(String key, float defaultVal) {
        return sPref.getFloat(key, defaultVal);
    }

    public float getFloat(String key) {
        return sPref.getFloat(key, 0);
    }

    public long getLong(String key, long defaultVal) {
        return sPref.getLong(key, defaultVal);
    }

    public long getLong(String key) {
        return sPref.getLong(key, 0);
    }

    public Set<String> getStringSet(String key) {
        return sPref.getStringSet(key, null);
    }

    public boolean contains(String key) {
        return sPref.contains(key);
    }

    public void remove(String key) {
        sEditor.remove(key);
    }

    public Map<String, ?> getAll() {
        return sPref.getAll();
    }

    public boolean isLogin() {
        return getBoolean(Constants.IS_LOGIN);
    }

    public void clearSession() {
        String token = getString(AppPref.FCM_TOKEN);

        sEditor.remove(Constants.USER_SESSION);
        sEditor.remove(Constants.USER_TOKEN);
        sEditor.remove(Constants.IS_LOGIN);
        sEditor.clear();

        set(AppPref.FCM_TOKEN, token);
    }

//    public void setRegistrationSession(RegistrationResponse.DataBean registrationData) {
//        sEditor.putString(Constants.USER_SESSION, new Gson().toJson(registrationData)).commit();
//    }
//
//    public RegistrationResponse.DataBean getRegistrationSession() {
//        return new Gson().fromJson(sPref.getString(Constants.USER_SESSION, "{}"), RegistrationResponse.DataBean.class);
//    }


    public void setLoginSession(LoginResponse.DataBean loginData) {
        sEditor.putString(Constants.USER_SESSION, new Gson().toJson(loginData)).commit();
    }

    public LoginResponse.DataBean getLoginSession() {
        return new Gson().fromJson(sPref.getString(Constants.USER_SESSION, "{}"), LoginResponse.DataBean.class);
    }


//    public void setSession(LoginRes.AppSession appSession) {
//        sEditor.putString(Constants.USER_SESSION,new Gson().toJson(appSession)).commit();
//    }

//    public LoginRes.AppSession getSession(){
//        return new Gson().fromJson(sPref.getString(Constants.USER_SESSION,"{}"), LoginRes.AppSession.class);
//    }

    public String getToken() {
        return sPref.getString(Constants.USER_TOKEN, "");
    }
}