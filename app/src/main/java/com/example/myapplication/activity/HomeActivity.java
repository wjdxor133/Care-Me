package com.example.myapplication.activity;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.example.myapplication.BackPressCloseHandler;
import com.example.myapplication.R;
import com.example.myapplication.fragment.BulletinFragment;
import com.example.myapplication.fragment.CommunicationFragment;
import com.example.myapplication.fragment.FoundFragment;
import com.example.myapplication.fragment.HomeFragment;
import com.example.myapplication.fragment.OwnlistFragment;
import com.example.myapplication.fragment.UserInfoFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.annotation.Nullable;


public class HomeActivity extends BasicActivity {

    private BackPressCloseHandler backPressCloseHandler;
    private static final String TAG = "HomeActivity";

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: {
               myStartActivity(UsersettingActivity.class);
                return true;
            }
            case R.id.setting:{
                myStartActivity(BluetoothActivity.class);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        backPressCloseHandler = new BackPressCloseHandler(this);
        setToolbarTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);


        init();




    }

    @Override
    protected void onResume() {
        super.onResume();
       // postUpdate(false);
        //만약에 현재 확인된 유저가 없으면
    }
     @Override
     protected void onPause(){
        super.onPause();

     }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                init();
                break;

        }
    }
private void init(){
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    if (firebaseUser == null) {

        myStartActivity(LoadingActivity.class);

    } else {

        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Users").document(firebaseUser.getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        } else {
                            Log.d(TAG, "No such document");
                            myStartActivity(MemberInitActivity.class);
                        }
                    }

                }else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        HomeFragment homeFragment = new HomeFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, homeFragment)
                .commit();



        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.home:
                        HomeFragment homeFragment = new HomeFragment();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, homeFragment)
                                .commit();
                        return  true;
                    case R.id.found:
                        FoundFragment foundFragment = new FoundFragment();

                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, foundFragment)
                                .commit();








                 /*       UserListFragment userListFragment = new UserListFragment();

                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, userListFragment)
                                .commit();
                                */
                        return  true;
                    case R.id.communication:
                        CommunicationFragment communicationFragment = new CommunicationFragment();

                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, communicationFragment)
                                .commit();
                        return  true;

                }
                return false;
            }
        });
    }
}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user,menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void myStartActivity(Class c) {
        Intent intent = new Intent(this, c);
        startActivityForResult(intent, 1);
    }
}
