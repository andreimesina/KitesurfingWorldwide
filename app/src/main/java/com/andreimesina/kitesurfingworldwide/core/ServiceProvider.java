package com.andreimesina.kitesurfingworldwide.core;

import android.app.Application;

import com.andreimesina.kitesurfingworldwide.data.repository.Repository;

public class ServiceProvider {

    private static ServiceProvider instance;

    private final Application app;
    private final Repository repository;

    private ServiceProvider(Application app) {
        this.app = app;
        repository = new Repository(app);
    }

    public static void initialize(Application app) {
        if(instance != null) {
            throw new IllegalStateException("ServiceProvider is already initialized");
        }

        instance = new ServiceProvider(app);
    }

    public static ServiceProvider getInstance() {
        return instance;
    }

    public Repository getRepository() {
        return repository;
    }
}
