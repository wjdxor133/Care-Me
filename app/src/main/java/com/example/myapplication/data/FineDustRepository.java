package com.example.myapplication.data;

import com.example.myapplication.model.FineDust;

import retrofit2.Callback;

public interface FineDustRepository {

        boolean isAvailable();
        void getFindDustData(Callback<FineDust> callback);

}
