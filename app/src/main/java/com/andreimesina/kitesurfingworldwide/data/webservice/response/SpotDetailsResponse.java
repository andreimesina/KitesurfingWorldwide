package com.andreimesina.kitesurfingworldwide.data.webservice.response;

import com.andreimesina.kitesurfingworldwide.data.model.Spot;
import com.google.gson.annotations.SerializedName;

public class SpotDetailsResponse {

    @SerializedName("result")
    private Spot spot;

    public Spot getSpot() {
        return spot;
    }

    public void setSpot(Spot spot) {
        this.spot = spot;
    }
}
