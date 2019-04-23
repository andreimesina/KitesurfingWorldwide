package com.andreimesina.kitesurfingworldwide.data.webservice;

import com.andreimesina.kitesurfingworldwide.data.model.Profile;
import com.andreimesina.kitesurfingworldwide.data.model.Spot;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface Api {

    @POST("/api-user-get")
    @Headers("Content-Type: application/json")
    Call<Profile> createProfile(@Body String email);

    @POST("/api-spot-get-all")
    @Headers("Content-Type: application/json")
    Call<List<Spot>> getAllSpots(@Header("token") String token, @Body Spot s);

    @POST("/api-spot-get-details")
    @Headers("Content-Type: application/json")
    Call<Spot> getSpotDetails(@Header("token") String token, @Body String spotId);

    @POST("/api-spot-get-countries")
    @Headers("Content-Type: application/json")
    Call<List<String>> getAllSpotCountries(@Header("token") String token);

    @POST("/api-spot-favorites-add")
    @Headers("Content-Type: application/json")
    Call<Void> addSpotToFavorites(@Header("token") String token, @Body String spotId);

    @POST("/api-spot-favorites-remove")
    @Headers("Content-Type: application/json")
    Call<Void> removeSpotFromFavorites(@Header("token") String token, @Body String spotId);
}