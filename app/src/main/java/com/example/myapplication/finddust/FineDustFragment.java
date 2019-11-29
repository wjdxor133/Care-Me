package com.example.myapplication.finddust;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.myapplication.R;
import com.example.myapplication.activity.DustMainActivity;
import com.example.myapplication.data.FineDustRepository;
import com.example.myapplication.data.LocationFindDustRepository;
import com.example.myapplication.model.FineDust;


public class FineDustFragment extends Fragment implements FineDustContract.View{
    private TextView mLocationTextView;
    private TextView mTimeTextView;
    private TextView mDustTextView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FineDustRepository mRepository;
    private FineDustPresenter mPresenter;
    private ImageView mdust;

    public static FineDustFragment newInstance(double lat, double lng){
        Bundle args = new Bundle();
        args.putDouble("lat",lat);
        args.putDouble("lng",lng);
        FineDustFragment fragment = new FineDustFragment();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getArguments() != null){
            double lat = getArguments().getDouble("lat");
            double lng = getArguments().getDouble("lng");
            mRepository = new LocationFindDustRepository(lat,lng);
        }else{
            mRepository = new LocationFindDustRepository(0,0);
            ((DustMainActivity)getActivity()).getLastKnownLocation();
        }
        mPresenter = new FineDustPresenter(mRepository, this);
        mPresenter.loadFineDustData();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_fine_dust,container,false);
        mLocationTextView =view.findViewById(R.id.result_location_text);
        mTimeTextView =view.findViewById(R.id.result_time_text);
        mDustTextView =view.findViewById(R.id.result_dust_text);
        mdust=view.findViewById(R.id.dustdust);
        if(savedInstanceState != null){
            //복원
            mLocationTextView.setText(savedInstanceState.getString("location"));
            mTimeTextView.setText(savedInstanceState.getString("time"));
            mDustTextView.setText(savedInstanceState.getString("dust"));

        }
        mSwipeRefreshLayout=view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.YELLOW,Color.GREEN,Color.BLUE);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadFineDustData();
            }
        });

        return view;


}

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("location",mLocationTextView.getText().toString());
        outState.putString("time",mTimeTextView.getText().toString());
        outState.putString("dust",mDustTextView.getText().toString());

    }

    @Override
    public void showFineDustResult(FineDust fineDust) {
        float i = Float.parseFloat(fineDust.getWeather().getDust().get(0).getPm10().getValue());

        String state = null;

        if (i >25 && i<=45){
            mdust.setImageResource(R.drawable.good);
            state = "보통";
        }else if(i >=0 && i<=25){
            mdust.setImageResource(R.drawable.great);
            state = "좋음";
        }else if(i>45 && i<=65){
            mdust.setImageResource(R.drawable.soso);
            state = "나쁨";
        }else if(i>65 && i<=150){
            mdust.setImageResource(R.drawable.bad);
            state = "매우 나쁨";
        }
        mLocationTextView.setText(fineDust.getWeather().getDust().get(0).getStation().getName());
        mTimeTextView.setText(fineDust.getWeather().getDust().get(0).getTimeObservation());
        mDustTextView.setText(fineDust.getWeather().getDust().get(0)
        .getPm10().getValue() + "㎍/㎥"+ state);
        Log.e("로그", "미세먼지 농도 : "+ i);
    }

    @Override
    public void showLoadError(String message) {

        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loadingStart() {
        mSwipeRefreshLayout.setRefreshing(true);

    }

    @Override
    public void loadingEnd() {

        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void reload(double lat, double lng) {

        mRepository=new LocationFindDustRepository(lat,lng);
        mPresenter=new FineDustPresenter(mRepository, this);
        mPresenter.loadFineDustData();
    }
}
