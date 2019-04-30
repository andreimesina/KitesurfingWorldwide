package com.andreimesina.kitesurfingworldwide.data.webservice.response;

import com.andreimesina.kitesurfingworldwide.data.model.SpotDetails;
import com.google.gson.annotations.SerializedName;

public class SpotDetailsResponse {

    @SerializedName("result")
    private SpotDetails spotDetails;

    public SpotDetails getSpotDetails() {
        return spotDetails;
    }

    public void setSpotDetails(SpotDetails spotDetails) {
        this.spotDetails = spotDetails;
    }
}
