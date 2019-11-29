package com.example.myapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.example.myapplication.DoctorInfo;
import com.example.myapplication.R;
import com.example.myapplication.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class UsersettingActivity extends BasicActivity {

    private String name;
    ViewFlipper v_fllipper;
    UserInfo userInfo;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: {
                finish();
                return true;
            }


        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usersetting);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setToolbarTitle("");
        userInfo = new UserInfo();
        User_info();
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.logout:
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(getApplicationContext(),SignUpActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        break;
                    case R.id.board:
                        Intent intent2 = new Intent(getApplicationContext(),MainActivity.class);
                        intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent2);
                        break;
                    case R.id.home_imgbtn:
                        Intent intent3 = new Intent(getApplicationContext(),UserInfoActivity.class);
                        intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent3);
                        break;
                    case R.id.streaming_btn:
                        Intent intent4 = new Intent(getApplicationContext(),StreamingActivity.class);
                        intent4.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent4);
                        break;
                    case R.id.location:

                        Intent intent5 = new Intent(getApplicationContext(),GPSActivity.class);
                        intent5.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent5);
                        break;
                    case R.id.lost:

                        Intent intent6 = new Intent(getApplicationContext(),ParseActivity.class);
                        intent6.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent6);
                        break;
                    case R.id.dust:

                        Intent intent7 = new Intent(getApplicationContext(),DustMainActivity.class);
                        intent7.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent7);
                        break;

                }
            }
        };

      findViewById(R.id.board).setOnClickListener(onClickListener);
        findViewById(R.id.logout).setOnClickListener(onClickListener);
        findViewById(R.id.home_imgbtn).setOnClickListener(onClickListener);
        findViewById(R.id.location).setOnClickListener(onClickListener);
        findViewById(R.id.dust).setOnClickListener(onClickListener);
        findViewById(R.id.lost).setOnClickListener(onClickListener);
        findViewById(R.id.streaming_btn).setOnClickListener(onClickListener);


        int images[] = {
                R.drawable.mmain,
                R.drawable.mfind,
                R.drawable.mshare
        };
        v_fllipper = findViewById(R.id.image_slide);

        for(int image : images) {
            fllipperImages(image);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user,menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void myStartActivity(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }
    private void fllipperImages(int image) {
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(image);

        v_fllipper.addView(imageView);      // 이미지 추가
        v_fllipper.setFlipInterval(2000);       // 자동 이미지 슬라이드 딜레이시간(1000 당 1초)
        v_fllipper.setAutoStart(true);          // 자동 시작 유무 설정

        // animation
        v_fllipper.setInAnimation(this,android.R.anim.slide_in_left);
        v_fllipper.setOutAnimation(this,android.R.anim.slide_out_right);
    }
    public void User_info(){
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
                            userInfo.setName(document.getData().get("name").toString());
                            setToolbarTitle(userInfo.getName()+"님");
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
