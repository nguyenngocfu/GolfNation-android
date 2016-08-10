package com.golf.golfnation.common;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by NgocNQ on 7/13/2015.
 */
public class PreferenceManager {
    public static boolean saveString(Context ctx, String key, String value) {
        SharedPreferences pref = android.preference.PreferenceManager.getDefaultSharedPreferences(ctx);
        return pref.edit().putString(key, value).commit();
    }

    public static String getString(Context ctx, String key) {
        SharedPreferences pref = android.preference.PreferenceManager.getDefaultSharedPreferences(ctx);
        return pref.getString(key, "");
    }

    public static boolean saveBoolean(Context ctx, String key, boolean value) {
        SharedPreferences pref = android.preference.PreferenceManager.getDefaultSharedPreferences(ctx);
        return pref.edit().putBoolean(key, value).commit();
    }

    public static boolean getBoolean(Context ctx, String key) {
        SharedPreferences pref = android.preference.PreferenceManager.getDefaultSharedPreferences(ctx);
        return pref.getBoolean(key,false);
    }

}
