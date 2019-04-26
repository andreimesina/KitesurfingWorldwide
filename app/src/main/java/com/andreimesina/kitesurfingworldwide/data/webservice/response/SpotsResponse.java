package com.andreimesina.kitesurfingworldwide.data.webservice.response;

import com.andreimesina.kitesurfingworldwide.data.model.Spot;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SpotsResponse {

    @SerializedName("result")
    private List<Spot> spots;

    public List<Spot> getSpots() {
        return spots;
    }

    public void setSpots(List<Spot> spots) {
        this.spots = spots;
    }
}
