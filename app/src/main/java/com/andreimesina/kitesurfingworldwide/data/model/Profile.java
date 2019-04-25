package com.andreimesina.kitesurfingworldwide.data.model;

import com.google.gson.annotations.SerializedName;

public class Profile {

    @SerializedName("token")
    private String token;

    @SerializedName("email")
    private String email;

    public Profile(String email, String token) {
        this.email = email;
        this.token = token;
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
