package com.andreimesina.kitesurfingworldwide.data.model;

public class Profile {

    private static Profile instance;

    private String token;
    private String email;

    private Profile() {}

    public static synchronized Profile getInstance() {
        if(instance == null) {
            instance = new Profile();
        }

        return instance;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
