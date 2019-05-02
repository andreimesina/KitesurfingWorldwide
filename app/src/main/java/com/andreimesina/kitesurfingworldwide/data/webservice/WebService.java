package com.andreimesina.kitesurfingworldwide.data.webservice;

import com.andreimesina.kitesurfingworldwide.core.AuthenticationManager;
import com.andreimesina.kitesurfingworldwide.data.webservice.response.CountriesResponse;
import com.andreimesina.kitesurfingworldwide.data.webservice.response.SpotIdResponse;
import com.andreimesina.kitesurfingworldwide.data.webservice.response.ProfileResponse;
import com.andreimesina.kitesurfingworldwide.data.model.SpotFilter;
import com.andreimesina.kitesurfingworldwide.data.webservice.response.SpotDetailsResponse;
import com.andreimesina.kitesurfingworldwide.data.webservice.response.SpotsResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WebService {

    private static final String API_BASE_URL = "https://internship-2019.herokuapp.com";

    private ApiService api;

    public WebService() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        api = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder()
                        .callTimeout(10, TimeUnit.SECONDS)
                        .addInterceptor(httpLoggingInterceptor)
                        .build())
                .build()
                .create(ApiService.class);
    }

    public Call<ProfileResponse> createProfile() {
        Map<String, String> emailMap = new HashMap<>();
        emailMap.put("email",
                AuthenticationManager.getInstance().getProfile().getEmail());

        return api.createProfile(emailMap);
    }

    public Call<SpotsResponse> getAllSpots() {
        return api.getAllSpots(
                AuthenticationManager.getInstance().getProfile().getToken(),
                SpotFilter.getInstance());
    }

    public Call<SpotDetailsResponse> getSpotDetails(String spotId) {
        Map<String, String> idMap = new HashMap<>();
        idMap.put("spotId", spotId);

        return api.getSpotDetails(
                AuthenticationManager.getInstance().getProfile().getToken(),
                idMap);
    }

    public Call<CountriesResponse> getAllSpotCountries() {
        return api.getAllSpotCountries(
                AuthenticationManager.getInstance().getProfile().getToken());
    }

    public Call<SpotIdResponse> addSpotToFavorites(String spotId) {
        Map<String, String> idMap = new HashMap<>();
        idMap.put("spotId", spotId);

        return api.addSpotToFavorites(
                AuthenticationManager.getInstance().getProfile().getToken(),
                idMap);
    }

    public Call<SpotIdResponse> removeSpotFromFavorites(String spotId) {
        Map<String, String> idMap = new HashMap<>();
        idMap.put("spotId", spotId);

        return api.removeSpotFromFavorites(
                AuthenticationManager.getInstance().getProfile().getToken(),
                idMap);
    }
}
