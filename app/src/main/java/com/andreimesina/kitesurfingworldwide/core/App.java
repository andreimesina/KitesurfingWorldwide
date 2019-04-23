package com.andreimesina.kitesurfingworldwide.core;

import android.app.Application;

import com.andreimesina.kitesurfingworldwide.BuildConfig;
import com.andreimesina.kitesurfingworldwide.data.model.Profile;

import timber.log.Timber;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if(BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        Profile.getInstance().setEmail("test@test.com");
        ServiceProvider.initialize(this);

    }
}
