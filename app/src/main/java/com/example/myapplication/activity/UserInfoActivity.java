package com.example.myapplication.activity;

import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserInfoActivity extends BasicActivity {

    private static final String TAG = "UserInfoFragment";

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: {
                finish();
                return true;
            }
            case R.id.setting: {
                Intent intent = new Intent(getApplicationContext(), Member_UpdateActivity.class);
                startActivity(intent);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setToolbarTitle("회원정보");

        final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);

        final ImageView profileimageView = findViewById(R.id.profileimageView);
        final TextView nameTextView = findViewById(R.id.nameTextView);
        final TextView phoneNumberTextView = findViewById(R.id.phoneNumberTextView);
        final TextView petTextView = findViewById(R.id.petTextView);
        final TextView birthDayTextView =findViewById(R.id.birthDayTextView);
        final TextView addressTextView =findViewById(R.id.addressTextView);

        //만약에 현재 확인된 유저가 없으면
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Users").document( FirebaseAuth.getInstance().getCurrentUser().getUid());
                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null) {
                                if (document.exists()) {
                                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                    String str = document.getData().get("photoUrl") == null? null : document.getData().get("photoUrl").toString();
                                    if(str != null){
                                        Glide.with(getApplicationContext()).load(str).centerCrop().override(500).into(profileimageView);
                                    }

                                    swipeRefreshLayout.setRefreshing(false);

                                    nameTextView.setText(document.getData().get("name").toString()+"님");
                                    phoneNumberTextView.setText(document.getData().get("phoneNumber").toString());
                                    petTextView.setText(document.getData().get("petName").toString());
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
            }
        });

        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Users").document( FirebaseAuth.getInstance().getCurrentUser().getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                           String str = document.getData().get("photoUrl") == null? null : document.getData().get("photoUrl").toString();
                            if(str != null){
                                Glide.with(getApplicationContext()).load(str).centerCrop().override(500).into(profileimageView);
                            }

                            nameTextView.setText(document.getData().get("name").toString()+"님");
                            phoneNumberTextView.setText(document.getData().get("phoneNumber").toString());
                            petTextView.setText(document.getData().get("petName").toString());
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


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user,menu);
        return super.onCreateOptionsMenu(menu);
    }




}