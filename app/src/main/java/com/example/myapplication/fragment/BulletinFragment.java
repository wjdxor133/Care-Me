package com.example.myapplication.fragment;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.PostInfo;
import com.example.myapplication.R;
import com.example.myapplication.activity.WritePostActivity;
import com.example.myapplication.adapter.BulletinAdapter;
import com.example.myapplication.listener.OnPostListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;


public class BulletinFragment extends Fragment {
    private static final String TAG = "BulletinFragment";

    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;
    private BulletinAdapter bulletinAdapter;
    private ArrayList<PostInfo> postList;
    private boolean updating;
    private boolean topScrolled;
    private boolean Load;
    private  SwipeRefreshLayout swipeRefreshLayout;
    public BulletinFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bulletin, container, false);


        //만약에 현재 확인된 유저가 없으면
        firebaseFirestore = FirebaseFirestore.getInstance();


        postList = new ArrayList<>();
        bulletinAdapter = new BulletinAdapter(getActivity(), postList);
        bulletinAdapter.setOnPostListener(onPostListener);
        //findViewById(R.id.logout).setOnClickListener(onClickListener);


        final RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        view.findViewById(R.id.floatingActionButton).setOnClickListener(onClickListener);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(bulletinAdapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {  //스크롤 상태가 변했을  (시작과 끝 탐지)

                        super.onScrollStateChanged(recyclerView, newState);

                        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

                        int firstVisibleitemPosition = ((LinearLayoutManager)layoutManager).findFirstVisibleItemPosition();  //화면에 보이는 첫번째

                        if(newState == 1 && firstVisibleitemPosition == 0){  //스크롤이 맨 위일
                            topScrolled = true;
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        if(newState == 0 && topScrolled){  // 손을 땠을



                      postUpdate(true);
                      topScrolled = false;



                        }

                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView1, int dx, int dy){  //스크롤이 되는 내내 감지
                        super.onScrolled(recyclerView,dx,dy);


                        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                        int visibleitmeCount = layoutManager.getChildCount();
                        int totalitemCount  = layoutManager.getItemCount();
                        int firstVisibleitemPosition = ((LinearLayoutManager)layoutManager).findFirstVisibleItemPosition();  //화면에 보이는 첫번째
                        int lastVisibleitemPosition = ((LinearLayoutManager)layoutManager).findLastVisibleItemPosition();  //화면에 보이는 마지

                        if(totalitemCount -3 <= lastVisibleitemPosition && updating){
                            postUpdate(false);
                            swipeRefreshLayout.setRefreshing(false);
                        }

                        if(0<firstVisibleitemPosition){

                            topScrolled = false;

                        }




                    }


                });


                swipeRefreshLayout.setRefreshing(false);
            }




        });




        postUpdate(false);


        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }
    @Override
    public void onPause(){
        super.onPause();
        bulletinAdapter.playerStop();
    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                /*
                case R.id.logout:
                    FirebaseAuth.getI stance().signOut();
                    Intent intent = new Intent(getApplicationContext(),SignUpActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    */

                case R.id.floatingActionButton:
                    myStartActivity(WritePostActivity.class);
                    break;
            }
        }
    };
    OnPostListener onPostListener = new OnPostListener() {
        @Override
        public void onDelete(PostInfo postInfo) {
            postList.remove(postInfo);
            bulletinAdapter.notifyDataSetChanged();

            Log.e("로그", "삭제 성공");
        }

        @Override
        public void onModity() {
            Log.e("로그", "수정 성공");
        }
    };

    private void postUpdate(final boolean clear) {
        updating = true;
        Date date = postList.size() == 0 || clear ? new Date() : postList.get(postList.size()-1).getCreatedAt();
        CollectionReference collectionReference = firebaseFirestore.collection("Posts");
        collectionReference.orderBy("createdAt", Query.Direction.DESCENDING).whereLessThan("createdAt", date).limit(10).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(clear){
                                postList.clear();
                            }

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                postList.add(new PostInfo(

                                        document.getData().get("title").toString(),
                                        (ArrayList<String>) document.getData().get("contents"),
                                        (ArrayList<String>) document.getData().get("formats"),
                                        document.getData().get("publisher").toString(),
                                        new Date(document.getDate("createdAt").getTime()),
                                        document.getId()));
                            }

                            bulletinAdapter.notifyDataSetChanged();
                            swipeRefreshLayout.setRefreshing(false);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                        updating=false;
                    }
                });
    }



    private void myStartActivity(Class c) {
        Intent intent = new Intent(getActivity(), c);
        startActivityForResult(intent, 0);
    }
}
