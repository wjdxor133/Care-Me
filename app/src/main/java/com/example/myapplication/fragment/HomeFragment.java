package com.example.myapplication.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.activity.DustMainActivity;
import com.example.myapplication.activity.MainActivity;
import com.example.myapplication.activity.UserInfoActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private FirebaseUser firebaseUser;
    TextView petNameText;
    Button home_btn, dust_btn;
    String petName;
    public HomeFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        LottieAnimationView animationView = (LottieAnimationView)view.findViewById(R.id.animation_view);
        animationView.setAnimation("4251-plant-office-desk.json");
        animationView.loop(true);
        animationView.playAnimation();
     home_btn = (Button)view.findViewById(R.id.homebtn);
        dust_btn = (Button)view.findViewById(R.id.dustbtn);
       Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.button1);


        home_btn.startAnimation(animation);
       dust_btn.startAnimation(animation);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.homebtn:
                        Intent intent = new Intent(getActivity(), UserInfoActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        break;

                    case R.id.dustbtn:
                        Intent intent2 = new Intent(getActivity(), DustMainActivity.class);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent2);
                        break;

                }
            }
        };
        home_btn.setOnClickListener(onClickListener);
        dust_btn.setOnClickListener(onClickListener);
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

    public void Pet_info(){
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Users").document(firebaseUser.getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                              petName = document.getData().get("petName").toString();
                        } else {
                            Log.d(TAG, "No such document");

                        }
                    }

                }else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

}
