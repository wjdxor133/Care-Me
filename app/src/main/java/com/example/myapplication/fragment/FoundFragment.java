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
import com.example.myapplication.activity.BluetoothActivity;
import com.example.myapplication.activity.GPSActivity;
import com.example.myapplication.activity.MainActivity;
import com.example.myapplication.activity.StreamingActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class FoundFragment extends Fragment {
    private static final String TAG = "FoundFragment";
   Button camera_btn,GPS_btn;
    public FoundFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_found, container, false);
        LottieAnimationView animationView = (LottieAnimationView)view.findViewById(R.id.animation_view);
        animationView.setAnimation("7420-clouds-opening.json");
        animationView.loop(true);
        animationView.playAnimation();

        camera_btn = (Button)view.findViewById(R.id.camerabtn);
        GPS_btn = (Button)view.findViewById(R.id.GPSbtn);
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.button1);


        camera_btn.startAnimation(animation);
        GPS_btn.startAnimation(animation);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){

                    case R.id.camerabtn:
                        Intent intent = new Intent(getActivity(), StreamingActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        break;

                    case R.id.GPSbtn:
                        Intent intent2 = new Intent(getActivity(),  GPSActivity.class);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent2);
                        break;


                }
            }
        };
        camera_btn.setOnClickListener(onClickListener);
        GPS_btn.setOnClickListener(onClickListener);
        /*
        final ImageView profileimageView = view.findViewById(R.id.profileimageView);
        final TextView nameTextView = view.findViewById(R.id.nameTextView);
        final TextView phoneNumberTextView = view.findViewById(R.id.phoneNumberTextView);
        final TextView birthDayTextView = view.findViewById(R.id.birthDayTextView);
        final TextView addressTextView = view.findViewById(R.id.addressTextView);

        //만약에 현재 확인된 유저가 없으면


        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Users").document( FirebaseAuth.getInstance().getCurrentUser().getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            if(document.getData().get("photoUrl").toString() != null){
                                Glide.with(getActivity()).load(document.getData().get("photoUrl").toString()).centerCrop().override(500).into(profileimageView);
                            }

                            nameTextView.setText(document.getData().get("name").toString());
                            phoneNumberTextView.setText(document.getData().get("phoneNumber").toString());
                            birthDayTextView.setText(document.getData().get("birthDay").toString());
                            addressTextView.setText(document.getData().get("address").toString());
                        } else {
                            Log.d(TAG, "No such document");

                        }
                    }

                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


        //findViewById(R.id.logout).setOnClickListener(onClickListener);
*/
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



}
