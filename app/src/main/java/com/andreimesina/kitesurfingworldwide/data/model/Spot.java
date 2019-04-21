package com.andreimesina.kitesurfingworldwide.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Spot {

    @PrimaryKey
    private int id;

    private String name;

    private float longitude;

    private float latitude;

    private int windProbability;

    private String country;

    private String whenToGo;

    private boolean isFavorite;

    public Spot(int id, String name, float longitude, float latitude, int windProbability,
                String country, String whenToGo, boolean isFavorite) {
        this.id = id;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.windProbability = windProbability;
        this.country = country;
        this.whenToGo = whenToGo;
        this.isFavorite = isFavorite;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getLongitude() {
        return longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public int getWindProbability() {
        return windProbability;
    }

    public String getCountry() {
        return country;
    }

    public String getWhenToGo() {
        return whenToGo;
    }

    public boolean isFavorite() {
        return isFavorite;
    }
}