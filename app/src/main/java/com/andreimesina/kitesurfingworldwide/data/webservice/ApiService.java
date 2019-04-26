package com.andreimesina.kitesurfingworldwide.data.webservice;

import com.andreimesina.kitesurfingworldwide.data.webservice.response.ProfileResponse;
import com.andreimesina.kitesurfingworldwide.data.model.Spot;
import com.andreimesina.kitesurfingworldwide.data.model.SpotFilter;
import com.andreimesina.kitesurfingworldwide.data.webservice.response.SpotDetailsResponse;
import com.andreimesina.kitesurfingworldwide.data.webservice.response.SpotsResponse;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("/api-user-get")
    Call<ProfileResponse> createProfile(@Body Map<String, String> email);

    @Headers("Content-Type: application/json")
    @POST("/api-spot-get-all")
    Call<SpotsResponse> getAllSpots(@Header("token") String token, @Body SpotFilter spotFilter);

    @Headers("Content-Type: application/json")
    @POST("/api-spot-get-details")
    Call<SpotDetailsResponse> getSpotDetails(@Header("token") String token, @Body String spotId);

    @Headers("Content-Type: application/json")
    @POST("/api-spot-get-countries")
    Call<List<String>> getAllSpotCountries(@Header("token") String token);

    @Headers("Content-Type: application/json")
    @POST("/api-spot-favorites-add")
    Call<Void> addSpotToFavorites(@Header("token") String token, @Body String spotId);

    @Headers("Content-Type: application/json")
    @POST("/api-spot-favorites-remove")
    Call<Void> removeSpotFromFavorites(@Header("token") String token, @Body String spotId);
}