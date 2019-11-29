package com.example.myapplication.adapter;


import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.DoctorInfo;
import com.example.myapplication.R;
import com.example.myapplication.UserInfo;

import java.util.ArrayList;


public class DoctorListAdapter extends RecyclerView.Adapter<DoctorListAdapter.MainViewHolder> {
    private ArrayList<DoctorInfo> mDataset;
    private Activity activity;

    static class MainViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;

        MainViewHolder(CardView v) {
            super(v);
            cardView = v;

        }
    }


    public DoctorListAdapter(Activity activity, ArrayList<DoctorInfo> myDataset) {
        this.mDataset = myDataset;
        this.activity = activity;

    }





    @Override
    public int getItemViewType(int position) {
        return position;

    }

    @NonNull
    @Override
    public DoctorListAdapter.MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                               int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_doctor, parent, false);

        final MainViewHolder mainViewHolder = new MainViewHolder(cardView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
            }
        });

        return mainViewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull final MainViewHolder holder, int position) {
        CardView cardView = holder.cardView;
        ImageView photo = cardView.findViewById(R.id.photo);
        TextView nameTextView = cardView.findViewById(R.id.nameTextView);
        TextView hospitalTextView = cardView.findViewById(R.id.hospitalTextView);
        TextView addressTextView = cardView.findViewById(R.id.addressTextView);
        Button email_btn = cardView.findViewById(R.id.email_btn);


        final DoctorInfo doctorInfo   = mDataset.get(position);
        if(mDataset.get(position).getPhotoUrl() != null){
            Glide.with(activity).load(mDataset.get(position).getPhotoUrl()).centerCrop().override(500).into(photo);
        }

        nameTextView.setText(doctorInfo.getName());
        hospitalTextView.setText(doctorInfo.getHospital());
        addressTextView.setText(doctorInfo.getLocation());
        email_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent email = new Intent(Intent.ACTION_SEND);
                email.setType("plain/text");
                // email setting 배열로 해놔서 복수 발송 가능
                String[] address = {doctorInfo.getEmail()};
                email.putExtra(Intent.EXTRA_EMAIL, address);
                activity.startActivity(email);

            }
        });


    }


    @Override
    public int getItemCount() {
        return mDataset.size();
    }







}
