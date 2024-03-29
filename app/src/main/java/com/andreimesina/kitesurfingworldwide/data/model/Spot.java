package com.andreimesina.kitesurfingworldwide.data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "Spot")
public class Spot {

    @SerializedName("id")
    @NonNull
    @ColumnInfo(name = "id", index = true)
    @PrimaryKey(autoGenerate = false)
    private String id;

    @SerializedName("name")
    @ColumnInfo(name = "name")
    private String name;

    @SerializedName("country")
    @ColumnInfo(name = "country")
    private String country;

    @SerializedName("whenToGo")
    @ColumnInfo(name = "whenToGo")
    private String whenToGo;

    @SerializedName("isFavorite")
    @ColumnInfo(name = "isFavorite")
    private boolean isFavorite;

    @ColumnInfo(name = "toDisplay")
    private boolean toDisplay;

    public Spot(String id, String name, String country, String whenToGo, boolean isFavorite) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.whenToGo = whenToGo;
        this.isFavorite = isFavorite;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public boolean isToDisplay() {
        return toDisplay;
    }

    public void setToDisplay(boolean toDisplay) {
        this.toDisplay = toDisplay;
    }
}