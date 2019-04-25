package com.andreimesina.kitesurfingworldwide.core;

import com.andreimesina.kitesurfingworldwide.data.model.Profile;

/**
 * Used in order to hold a consistent Profile throughout the whole application
 * and keep {@link Profile} class a simple POJO to make serialization easier.
 */
public class AuthenticationManager {

    private static AuthenticationManager instance;
    private Profile profile;

    private AuthenticationManager() { }

    public static synchronized AuthenticationManager getInstance() {
        if(instance == null) {
            instance = new AuthenticationManager();
        }

        return instance;
    }

    public synchronized Profile getProfile() {
        return profile;
    }

    public synchronized void setProfile(Profile profile) {
        this.profile = profile;
    }
}
