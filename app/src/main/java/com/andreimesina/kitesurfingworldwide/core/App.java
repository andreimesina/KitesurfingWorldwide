package com.andreimesina.kitesurfingworldwide.core;

import android.app.Application;

import com.andreimesina.kitesurfingworldwide.BuildConfig;
import com.jakewharton.threetenabp.AndroidThreeTen;

import timber.log.Timber;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if(BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        AndroidThreeTen.init(this);

        ServiceProvider.initialize(this);

    }
}
