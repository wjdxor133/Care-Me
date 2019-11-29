package com.example.myapplication.finddust;


import com.example.myapplication.data.FineDustRepository;
import com.example.myapplication.model.FineDust;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FineDustPresenter implements FineDustContract.UserActionsListener{
    private final FineDustRepository mRepository;
    private final FineDustContract.View mView;

    public FineDustPresenter(FineDustRepository repository,FineDustContract.View view) {
        this.mRepository = repository;
        this.mView=view;
    }

    @Override
    public void loadFineDustData(){

        if(mRepository.isAvailable()){
            mView.loadingStart();
            mRepository.getFindDustData(new Callback<FineDust>() {
                @Override
                public void onResponse(Call<FineDust> call, Response<FineDust> response) {
                    mView.showFineDustResult(response.body());
                    mView.loadingEnd();
                }

                @Override
                public void onFailure(Call<FineDust> call, Throwable t) {

                    mView.showLoadError(t.getLocalizedMessage());
                    mView.loadingEnd();
                }
            });
        }
    }

}
