package com.andreimesina.kitesurfingworldwide.core;

import android.app.Application;

import com.andreimesina.kitesurfingworldwide.data.repository.Repository;

public class ServiceProvider {

    private static ServiceProvider instance;

    private final Application app;

    private final AuthenticationManager authManager;
    private final Repository repository;

    private ServiceProvider(Application app) {
        this.app = app;

        authManager = AuthenticationManager.getInstance();
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

    public AuthenticationManager getAuthManager() {
        return authManager;
    }

    public Repository getRepository() {
        return repository;
    }
}
