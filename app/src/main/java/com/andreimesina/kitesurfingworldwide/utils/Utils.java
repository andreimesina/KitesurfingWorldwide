package com.andreimesina.kitesurfingworldwide.utils;

import android.content.Context;
import android.net.ConnectivityManager;

public class Utils {

    /**
     * Check if device is connected to a network.
     * @param ctx Current context.
     * @return Boolean value indicating the connectivity to a network.
     */
    public static boolean isNetworkConnected(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
}
