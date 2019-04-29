package com.andreimesina.kitesurfingworldwide.data.model;

public class SpotFilter {

    private static SpotFilter instance;

    private String country;
    private String windProbability;

    private SpotFilter() { }

    public static synchronized SpotFilter getInstance() {
        if(instance == null) {
            instance = new SpotFilter();
        }

        return instance;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getWindProbability() {
        return windProbability;
    }

    public void setWindProbability(String windProbability) {
        this.windProbability = windProbability;
    }
}