package com.andreimesina.kitesurfingworldwide.data.model;

public class SpotFilter {

    private String country;
    private int windProbability;

    public SpotFilter(String country, int windProbability) {
        this.country = country;
        this.windProbability = windProbability;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getWindProbability() {
        return windProbability;
    }

    public void setWindProbability(int windProbability) {
        this.windProbability = windProbability;
    }
}