package com.example.myapplication.util;

import android.util.Log;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FineDustUtil {
    private FineDustApi mGetApi;
    public FineDustUtil(){
        Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl(FineDustApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mGetApi=mRetrofit.create(FineDustApi.class);
    }

    public FineDustApi getApi() {
        return mGetApi;
    }
}
