package com.andreimesina.kitesurfingworldwide.core;

import android.content.Context;

import com.andreimesina.kitesurfingworldwide.data.model.Profile;
import com.andreimesina.kitesurfingworldwide.utils.Utils;

/**
 * Used in order to hold a consistent Profile throughout the whole application
 * and keep {@link Profile} class a simple POJO to make serialization easier.
 */
public class AuthenticationManager {

    private Context mContext;

    private static AuthenticationManager instance;
    private Profile profile;

    private boolean isAuthenticated;
    private boolean isOfflineAuth;

    private AuthenticationManager(Context context) {
        mContext = context;
        // Hotfix solution until we have a registration layout
        // so we can get the email address from user input
        String email = Utils.getString(mContext, "email");
        isOfflineAuth = Utils.getBoolean(mContext, "authenticated");

        profile = new Profile();
        if(isOfflineAuth && email.length() > 0) {
            profile.setEmail(email);
        } else {
            profile.setEmail("test@test.com");
        }
    }

    public static AuthenticationManager initialize(Context context) {
        if(instance == null) {
            instance = new AuthenticationManager(context);
        }

        return instance;
    }

    public static synchronized AuthenticationManager getInstance() {
        return instance;
    }

    public synchronized Profile getProfile() {
        return profile;
    }

    public synchronized void setProfile(Profile profile) {
        isAuthenticated = true;
        isOfflineAuth = false;
        this.profile = profile;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        isAuthenticated = authenticated;
    }

    public boolean isOfflineAuth() {
        return isOfflineAuth;
    }

    public void setOfflineAuth(boolean offlineAuth) {
        isOfflineAuth = offlineAuth;
    }
}
