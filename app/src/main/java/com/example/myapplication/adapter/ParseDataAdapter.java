package com.example.myapplication.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.example.myapplication.ParseInfo;
import com.example.myapplication.R;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;


public class ParseDataAdapter extends RecyclerView.Adapter<ParseDataAdapter.MainViewHolder> {
    private ArrayList<ParseInfo> mDataset;
    private LayoutInflater mInflate;
    private Context mContext;

    static class MainViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;

        MainViewHolder(CardView v) {
            super(v);
            cardView = v;

        }
    }


    public ParseDataAdapter(Context context, ArrayList<ParseInfo> myDataset) {
        this.mDataset = myDataset;
        this.mContext = context;

    }





    @Override
    public int getItemViewType(int position) {
        return position;

    }

    @NonNull
    @Override
    public ParseDataAdapter.MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                              int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_parse, parent, false);

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
        TextView ageTextView = cardView.findViewById(R.id.ageTextView);
        TextView addressTextView = cardView.findViewById(R.id.addressTextView);
        TextView sexTextView = cardView.findViewById(R.id.sexTextView);
        TextView weigthTextView = cardView.findViewById(R.id.weigthTextView);
        TextView kindCdTextView = cardView.findViewById(R.id.kindCdTextView);
        TextView placeTextView = cardView.findViewById(R.id.placeTextView);
        TextView specialTextView = cardView.findViewById(R.id.specialTextView);

        Button call_btn = cardView.findViewById(R.id.call_btn);


        final ParseInfo parseInfo   = mDataset.get(position);
        if(mDataset.get(position).getFilename() != null){
            Glide.with(mContext).load(mDataset.get(position).getFilename()).centerCrop().override(500).into(photo);
        }

        nameTextView.setText(parseInfo.getCareNm());
        ageTextView.setText("출생 : "+parseInfo.getAge());
        addressTextView.setText("위치 : "+parseInfo.getCareAddr());
        sexTextView.setText("성별 : "+parseInfo.getSexCd());
        weigthTextView.setText("몸무게 : "+parseInfo.getWeight());
        kindCdTextView.setText(parseInfo.getKindCd());
        placeTextView.setText("발견 장소 : "+parseInfo.getHappenPlace());
        specialTextView.setText("특이사항 : "+parseInfo.getSpecialMark());
        call_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+parseInfo.getCareTel()));

                mContext.startActivity(intent.addFlags(FLAG_ACTIVITY_NEW_TASK));
            }
        });


    }


    @Override
    public int getItemCount() {
        return mDataset.size();
    }







}
