package com.andreimesina.kitesurfingworldwide.data.webservice;

import com.andreimesina.kitesurfingworldwide.core.ServiceProvider;
import com.andreimesina.kitesurfingworldwide.data.model.Profile;
import com.andreimesina.kitesurfingworldwide.data.model.Spot;
import com.andreimesina.kitesurfingworldwide.data.model.SpotFilter;

import java.util.List;
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

    public Call<Profile> createProfile() {
        return api.createProfile(
                ServiceProvider.getInstance().getAuthManager().getProfile().getEmail());
    }

    public Call<List<Spot>> getAllSpots(SpotFilter spotFilter) {
        return api.getAllSpots(
                ServiceProvider.getInstance().getAuthManager().getProfile().getToken(),
                spotFilter);
    }

    public Call<Spot> getSpotDetails(String spotId) {
        return api.getSpotDetails(
                ServiceProvider.getInstance().getAuthManager().getProfile().getToken(),
                spotId);
    }

    public Call<List<String>> getAllSpotCountries() {
        return api.getAllSpotCountries(
                ServiceProvider.getInstance().getAuthManager().getProfile().getToken());
    }

    public Call<Void> addSpotToFavorites(String spotId) {
        return api.addSpotToFavorites(
                ServiceProvider.getInstance().getAuthManager().getProfile().getToken(),
                spotId);
    }

    public Call<Void> removeSpotFromFavorites(String spotId) {
        return api.removeSpotFromFavorites(
                ServiceProvider.getInstance().getAuthManager().getProfile().getToken(),
                spotId);
    }
}
