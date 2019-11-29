package com.example.myapplication.data;

import android.util.Log;


import com.example.myapplication.model.FineDust;
import com.example.myapplication.util.FineDustUtil;

import retrofit2.Callback;

public class LocationFindDustRepository implements FineDustRepository {
    private FineDustUtil mFineDustUtil;

    private double mLatitude;

    private double mLongitude;

    public LocationFindDustRepository(double lat, double lng) {
        mFineDustUtil = new FineDustUtil();
        this.mLatitude = lat;
        this.mLongitude = lng;
    }

    @Override
    public boolean isAvailable() {
        if (mLatitude != 0.0 && mLongitude != 0.0) {
            return true;

        }
        return false;
    }

    @Override
    public void getFindDustData(Callback<FineDust> callback) {

            mFineDustUtil.getApi().getFineDust(mLatitude, mLongitude)
                    .enqueue(callback);




    }
}

