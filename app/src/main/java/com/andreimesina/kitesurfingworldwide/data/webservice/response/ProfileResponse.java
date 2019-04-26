package com.andreimesina.kitesurfingworldwide.data.webservice.response;

import com.andreimesina.kitesurfingworldwide.data.model.Profile;
import com.google.gson.annotations.SerializedName;

public class ProfileResponse {

    @SerializedName("result")
    private Profile profile;

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
