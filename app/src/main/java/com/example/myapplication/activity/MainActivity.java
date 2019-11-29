package com.example.myapplication.activity;


import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.BackPressCloseHandler;
import com.example.myapplication.R;
import com.example.myapplication.fragment.BulletinFragment;
import com.example.myapplication.fragment.Camera2BasicFragment;
import com.example.myapplication.fragment.DoctorListFragment;
import com.example.myapplication.fragment.OwnlistFragment;
import com.example.myapplication.fragment.UserInfoFragment;
import com.example.myapplication.fragment.UserListFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import javax.annotation.Nullable;



public class MainActivity extends BasicActivity {




    private static final String TAG = "MainActivity";

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
        setContentView(R.layout.activity_main);


        setToolbarTitle("게시판"); // 툴바 이름설정
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


      /* androidx.appcompat.widget.Toolbar myToolbar = findViewById(R.id.showtoolbar);
        myToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.e("로그","실행 중  : ");
                switch (item.getItemId()){
                    case android.R.id.home:{
                        Log.e("로그","성공 : ");
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                    }


                }
                Log.e("로그","실패 : ");
                return false;
            }
        });

       */
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

        myStartActivity(SignUpActivity.class);
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

                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        BulletinFragment bulletinFragment = new BulletinFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, bulletinFragment)
                .commit();



        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.bulletin:
                        BulletinFragment bulletinFragment = new BulletinFragment();

                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, bulletinFragment)
                                .commit();
                        return  true;
                    case R.id.ownlist:
                        OwnlistFragment ownlistFragment = new OwnlistFragment();

                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, ownlistFragment)
                                .commit();








                 /*       UserListFragment userListFragment = new UserListFragment();

                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, userListFragment)
                                .commit();
                                */
                        return  true;
                    case R.id.counseling:
                        DoctorListFragment doctorListFragment = new DoctorListFragment();

                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, doctorListFragment)
                                .commit();
                        return  true;

                }
                return false;
            }
        });
    }
}
    private void myStartActivity(Class c) {
        Intent intent = new Intent(this, c);
        startActivityForResult(intent, 1);
    }
}
