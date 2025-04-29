package com.example.kidsplay;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit = null;

    // Base URL for your API
    private static final String BASE_URL = "http://192.168.166.216/project/home/";


    // Get Retrofit client instance
    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()) // for JSON to POJO conversion
                    .build();
        }
        return retrofit;
    }
}
