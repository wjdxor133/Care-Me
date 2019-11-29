package com.example.myapplication.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.DoctorInfo;
import com.example.myapplication.PostInfo;
import com.example.myapplication.R;
import com.example.myapplication.UserInfo;
import com.example.myapplication.adapter.DoctorListAdapter;
import com.example.myapplication.adapter.UserListAdapter;
import com.example.myapplication.listener.OnPostListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class DoctorListFragment extends Fragment {
    private static final String TAG = "DoctorListFragment";
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;
    private DoctorListAdapter doctorListAdapter;
    private ArrayList<DoctorInfo> doctorList;
    private boolean updating;
    private boolean topScrolled;

    public DoctorListFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_doctor_list, container, false);


        //만약에 현재 확인된 유저가 없으면
        firebaseFirestore = FirebaseFirestore.getInstance();


        doctorList = new ArrayList<>();
        doctorListAdapter = new DoctorListAdapter(getActivity(), doctorList);

        //findViewById(R.id.logout).setOnClickListener(onClickListener);


        final RecyclerView recyclerView = view.findViewById(R.id.recyclerView);



        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(doctorListAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {  //스크롤 상태가 변했을  (시작과 끝 탐지)

                super.onScrollStateChanged(recyclerView, newState);

                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

                int firstVisibleitemPosition = ((LinearLayoutManager)layoutManager).findFirstVisibleItemPosition();  //화면에 보이는 첫번째

                if(newState == 1 && firstVisibleitemPosition == 0){  //스크롤이 맨 위일
                    topScrolled = true;
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
                }

                if(0<firstVisibleitemPosition){

                    topScrolled = false;

                }



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


                case R.id.floatingActionButton:
                    Intent intent1 = new Intent(getActivity(), WritePostActivity.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent1);
                    break;  */
            }
        }
    };
    OnPostListener onPostListener = new OnPostListener() {
        @Override
        public void onDelete(PostInfo postInfo) {
            doctorList.remove(postInfo);
            doctorListAdapter.notifyDataSetChanged();

            Log.e("로그", "삭제 성공");
        }

        @Override
        public void onModity() {
            Log.e("로그", "수정 성공");
        }
    };

    private void postUpdate(final boolean clear) {
        updating = true;
      //  Date date = userList.size() == 0 || clear ? new Date() : userList.get(userList.size()-1).getCreatedAt();
        CollectionReference collectionReference = firebaseFirestore.collection("Doctors");
        collectionReference.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(clear){
                                doctorList.clear();
                            }
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                doctorList.add(new DoctorInfo(

                                        document.getData().get("name").toString(),
                                        document.getData().get("hospital").toString(),
                                        document.getData().get("location").toString(),
                                        document.getData().get("email").toString(),
                                        document.getData().get("photoUrl") == null ? null : document.getData().get("photoUrl").toString()));
                            }
                                Log.e("로그","doctorList"+doctorList);
                            doctorListAdapter.notifyDataSetChanged();

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
