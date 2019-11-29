package com.example.myapplication.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import com.example.myapplication.R;
import com.example.myapplication.common.AddLocationDialogFragment;
import com.example.myapplication.db.LocationRealmObject;
import com.example.myapplication.finddust.FineDustContract;
import com.example.myapplication.finddust.FineDustFragment;
import com.example.myapplication.util.GeoUtil;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.util.Pair;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;


import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.view.Menu;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class DustMainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    public static final int REQUEST_CODE_FINE_COARSE_PERMISSION = 1000;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private List<Pair<Fragment, String>> mFragmentList;
    private FusedLocationProviderClient mFusedLocationClient;
    private Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dustmain);
        mRealm.init(getApplicationContext());
        mRealm =Realm.getDefaultInstance();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setToolbarTitle("미세먼지 정보");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddLocationDialogFragment.newInstance(new AddLocationDialogFragment.OnClickListener() {
                    @Override
                    public void onOkClicked(final String city) {

                        GeoUtil.getLocationFromName(DustMainActivity.this, city, new GeoUtil.GeoUtilListener() {
                            @Override
                            public void onSuccess(double lat, double lng) {
                                saveNewCity(lat,lng,city);
                                addNewFragment(lat,lng,city);
                            }

                            @Override
                            public void onError(String message) {
                                Toast.makeText(DustMainActivity.this,message,Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).show(getSupportFragmentManager(),"dialog");
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        setUpViewPager();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }
    public void saveNewCity(double lat, double lng, String city){
        mRealm.beginTransaction();
        LocationRealmObject newLocationRealmObject=
                mRealm.createObject(LocationRealmObject.class);
        newLocationRealmObject.setName(city);
        newLocationRealmObject.setLat(lat);
        newLocationRealmObject.setLng(lng);


        mRealm.commitTransaction();

    }

    private void addNewFragment(double lat, double lng, String city){
        mFragmentList.add(new Pair<Fragment, String>(
                FineDustFragment.newInstance(lat,lng),city
        ));
        mViewPager.getAdapter().notifyDataSetChanged();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case android.R.id.home: {
                finish();
                return true;
            }
            case  R.id.action_all_delete: {
                mRealm.beginTransaction();
                mRealm.where(LocationRealmObject.class).findAll().deleteAllFromRealm();
                mRealm.commitTransaction();
                setUpViewPager();
                return true;
            }
            case R.id.action_delete:{
                if(mTabLayout.getSelectedTabPosition() ==0){
                    Toast.makeText(this, "현재 위치 탭은 삭제할 수 없습니다", Toast.LENGTH_SHORT).show();

                }
                mRealm.beginTransaction();
                mRealm.where(LocationRealmObject.class).findAll()
                        .get(mTabLayout.getSelectedTabPosition()-1).deleteFromRealm();
                mRealm.commitTransaction();

                setUpViewPager();
                return true;
            }

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void setUpViewPager(){
        mTabLayout = findViewById(R.id.tab_layout);
        mViewPager =findViewById(R.id.view_pager);
        loadDbData();
    MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager(), mFragmentList);
    mViewPager.setAdapter(adapter);
    mTabLayout.setupWithViewPager(mViewPager);
    }

    private void loadDbData() {
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new Pair<Fragment, String>(
                new FineDustFragment(),"현재위치"
        ));

        RealmResults<LocationRealmObject> realmResults =
                mRealm.where(LocationRealmObject.class).findAll();
        for(LocationRealmObject realmObject: realmResults){
            mFragmentList.add(new Pair<Fragment, String>(
                    FineDustFragment.newInstance(realmObject.getLat(),
                            realmObject.getLng()),realmObject.getName()
            ));


        }
    }

    public void getLastKnownLocation(){
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 권한 요청
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_CODE_FINE_COARSE_PERMISSION);
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                    if(location != null){
                        FineDustContract.View view =(FineDustContract.View)mFragmentList.get(0).first;
                        view.reload(location.getLatitude(),location.getLongitude());
                    }
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE_FINE_COARSE_PERMISSION){
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED

            ) {

                getLastKnownLocation();
            }

        }
    }

    private static class MyPagerAdapter extends FragmentStatePagerAdapter{

        private final List<Pair<Fragment,String>> mFargmentList;
        public MyPagerAdapter(@NonNull FragmentManager fm,List<Pair<Fragment,String>> fragmentList) {
            super(fm);
            mFargmentList = fragmentList;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFargmentList.get(position).first;
        }

        @Override
        public int getCount() {
            return mFargmentList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mFargmentList.get(position).second;
        }
    }
    public void setToolbarTitle(String title){
        ActionBar actionbar = getSupportActionBar();
        if(actionbar != null){
            actionbar.setTitle(title);
        }
    }

    //status bar의 높이 계산
    public int getStatusBarHeight()
    {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
            result = getResources().getDimensionPixelSize(resourceId);

        return result;
    }

}
