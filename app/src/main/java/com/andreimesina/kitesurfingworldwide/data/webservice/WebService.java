package com.andreimesina.kitesurfingworldwide.data.webservice;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WebService {

    private static final String API_BASE_URL = "https://internship-2019.herokuapp.com";

    private static WebService instance;

    private Api api;

    public WebService() {
        api = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Api.class);
    }

    public Api getApi() {
        return api;
    }
}
