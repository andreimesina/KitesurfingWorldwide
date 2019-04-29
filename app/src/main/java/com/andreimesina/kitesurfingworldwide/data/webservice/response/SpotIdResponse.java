package com.andreimesina.kitesurfingworldwide.data.webservice.response;

import com.google.gson.annotations.SerializedName;

public class SpotIdResponse {

    @SerializedName("spotId")
    private String spotId;

    public String getSpotId() {
        return spotId;
    }

    public void setSpotId(String spotId) {
        this.spotId = spotId;
    }
}
