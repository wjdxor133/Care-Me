package com.example.myapplication.adapter;


import android.app.Activity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.FirebaseHelper;
import com.example.myapplication.PostInfo;
import com.example.myapplication.R;
import com.example.myapplication.activity.PostActivity;
import com.example.myapplication.activity.WritePostActivity;
import com.example.myapplication.listener.OnPostListener;
import com.example.myapplication.view.ReadContentsView;
import com.google.android.exoplayer2.SimpleExoPlayer;

import java.util.ArrayList;


public class BulletinAdapter extends RecyclerView.Adapter<BulletinAdapter.MainViewHolder> {
    private ArrayList<PostInfo> mDataset;
    private Activity activity;
    private ArrayList<ArrayList<SimpleExoPlayer>> playerArrayListArrayList = new ArrayList<>();
    private final int MORE_INDEX = 2;
    private FirebaseHelper firebaseHelper;
    static class MainViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;

        MainViewHolder(CardView v) {
            super(v);
            cardView = v;

        }
    }


    public BulletinAdapter(Activity activity, ArrayList<PostInfo> myDataset) {
        this.mDataset = myDataset;
        this.activity = activity;

        firebaseHelper = new FirebaseHelper(activity);

    }

    public void setOnPostListener(OnPostListener onPostListener){

        firebaseHelper.setOnPostListener(onPostListener);

    }



    @Override
    public int getItemViewType(int position) {
        return position;

    }

    @NonNull
    @Override
    public BulletinAdapter.MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                             int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        final MainViewHolder mainViewHolder = new MainViewHolder(cardView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, PostActivity.class);
                intent.putExtra("postInfo",mDataset.get(mainViewHolder.getAdapterPosition()));
                activity.startActivity(intent);
            }
        });
        cardView.findViewById(R.id.menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view, mainViewHolder.getAdapterPosition());
            }
        });
        return mainViewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull final MainViewHolder holder, int position) {
        CardView cardView = holder.cardView;
        TextView titleTextView = cardView.findViewById(R.id.titleTextView);

        PostInfo postInfo = mDataset.get(position);
        titleTextView.setText(postInfo.getTitle());

        ReadContentsView readContentsView = cardView.findViewById(R.id.readContentsView);

        LinearLayout contentsLayout = cardView.findViewById(R.id.contentsLayout);


        if (contentsLayout.getTag() == null || !contentsLayout.equals(postInfo)) {
            contentsLayout.setTag(postInfo);
            contentsLayout.removeAllViews();


            readContentsView.setMoreIndex(MORE_INDEX);
            readContentsView.setPostInfo(postInfo);

            ArrayList<SimpleExoPlayer> playerArrayList = readContentsView.getPlayerArrayList();
    if(playerArrayList != null) {
        playerArrayListArrayList.add(playerArrayList);
    }


        }
    }


    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    private void showPopup(View v, final int position) {
        PopupMenu popup = new PopupMenu(activity, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.modity:  //수정
                        myStartActivity(WritePostActivity.class, mDataset.get(position));

                        return true;

                    case R.id.delete:  // 삭제
                        firebaseHelper.storageDelete(mDataset.get(position));

                        return true;

                    default:
                        return false;
                }

            }
        });
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.post, popup.getMenu());
        popup.show();
    }




    private void myStartActivity(Class c, PostInfo postInfo) {
        Intent intent = new Intent(activity, c);
        intent.putExtra("postInfo", postInfo);
        activity.startActivity(intent);
    }
    public void playerStop(){
        for (int i = 0; i < playerArrayListArrayList.size(); i++){
            ArrayList<SimpleExoPlayer> playerArrayList = playerArrayListArrayList.get(i);
            for (int ii=0; ii<playerArrayList.size(); ii++){
                SimpleExoPlayer player = playerArrayList.get(ii);
                if(player.getPlayWhenReady()){
                    player.setPlayWhenReady(false);
                }

            }
        }
    }
}
