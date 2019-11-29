package com.example.myapplication.finddust;


import com.example.myapplication.model.FineDust;

public class FineDustContract {
    public interface View{
        void showFineDustResult(FineDust fineDust);
        void showLoadError(String message);
        void loadingStart();
        void loadingEnd();
        void reload(double lat, double lng);

    }
    public interface UserActionsListener{
        void loadFineDustData();
    }
}