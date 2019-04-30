package com.andreimesina.kitesurfingworldwide.data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "SpotDetails")
public class SpotDetails {

    @SerializedName("id")
    @NonNull
    @ColumnInfo(name = "spotId", index = true)
    @PrimaryKey(autoGenerate = false)
    private String spotId;

    @SerializedName("name")
    @ColumnInfo(name = "name")
    private String name;

    @SerializedName("longitude")
    @ColumnInfo(name = "longitude")
    private float longitude;

    @SerializedName("latitude")
    @ColumnInfo(name = "latitude")
    private float latitude;

    @SerializedName("windProbability")
    @ColumnInfo(name = "windProbability")
    private int windProbability;

    @SerializedName("country")
    @ColumnInfo(name = "country")
    private String country;

    @SerializedName("whenToGo")
    @ColumnInfo(name = "whenToGo")
    private String whenToGo;

    @SerializedName("isFavorite")
    @ColumnInfo(name = "isFavorite")
    private boolean isFavorite;

    public SpotDetails(String spotId, String name, float longitude, float latitude, int windProbability,
                String country, String whenToGo, boolean isFavorite) {
        this.spotId = spotId;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.windProbability = windProbability;
        this.country = country;
        this.whenToGo = whenToGo;
        this.isFavorite = isFavorite;
    }

    @NonNull
    public String getSpotId() {
        return spotId;
    }

    public void setSpotId(@NonNull String spotId) {
        this.spotId = spotId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public int getWindProbability() {
        return windProbability;
    }

    public void setWindProbability(int windProbability) {
        this.windProbability = windProbability;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getWhenToGo() {
        return whenToGo;
    }

    public void setWhenToGo(String whenToGo) {
        this.whenToGo = whenToGo;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

}
