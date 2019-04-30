package com.andreimesina.kitesurfingworldwide.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;

public class Utils {

    private static final String APP_KEY = "unbreakable";

    /**
     * Check if device is connected to a network.
     * @param ctx Current context.
     * @return Boolean value indicating the connectivity to a network.
     */
    public static boolean isNetworkConnected(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    // get a String value by key
    public static String getString(Context ctx, String key) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(Utils.APP_KEY,
                Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    // save a String value by key
    public static void setString(Context ctx, String key, String value) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(Utils.APP_KEY,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    // get a float value by key
    public static float getFloat(Context ctx, String key) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(Utils.APP_KEY,
                Context.MODE_PRIVATE);
        return sharedPreferences.getFloat(key, 0.0f);
    }

    // save a float value by key
    public static void setFloat(Context ctx, String key, float value) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(Utils.APP_KEY,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    // save a boolean value by key
    public static void setBoolean(Context ctx, String key, boolean value) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(Utils.APP_KEY,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    // get a boolean value by key
    public static boolean getBoolean(Context ctx, String key) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(Utils.APP_KEY,
                Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, false);
    }
}
