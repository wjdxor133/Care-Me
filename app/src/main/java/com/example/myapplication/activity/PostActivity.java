package com.example.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.example.myapplication.FirebaseHelper;
import com.example.myapplication.PostInfo;
import com.example.myapplication.R;
import com.example.myapplication.listener.OnPostListener;
import com.example.myapplication.view.ReadContentsView;

import javax.annotation.Nullable;

public class PostActivity extends BasicActivity {


    private PostInfo postInfo;
   private FirebaseHelper firebaseHelper;
   private ReadContentsView readContentsView;
   private  LinearLayout contentsLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        postInfo = (PostInfo) getIntent().getSerializableExtra("postInfo");


        contentsLayout = findViewById(R.id.contentsLayout);
        readContentsView = findViewById(R.id.readContentsView);



        firebaseHelper = new FirebaseHelper(this);
        firebaseHelper.setOnPostListener(onPostListener);

        uiUpdate();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (resultCode == Activity.RESULT_OK) {
                 postInfo = (PostInfo)data.getSerializableExtra("postInfo");

                    contentsLayout.removeAllViews();
                    uiUpdate();

                }
                break;

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.post,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete :
                firebaseHelper.storageDelete(postInfo);
                finish();
                return true;


            case R.id.modity :
            myStartActivity(WritePostActivity.class, postInfo);
                return true;

            case android.R.id.home :
                finish();
                return true;

                default:
                    return super.onOptionsItemSelected(item);

        }

    }

    OnPostListener onPostListener = new OnPostListener() {
        @Override
        public void onDelete(PostInfo postInfo) {
            Log.e("로그","삭제성공");

        }

        @Override
        public void onModity() {
            Log.e("로그","수정성공");
        }
    };

    private void uiUpdate(){
        setToolbarTitle(postInfo.getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        readContentsView.setPostInfo(postInfo);
    }
    private void myStartActivity(Class c, PostInfo postInfo) {
        Intent intent = new Intent(this, c);
        intent.putExtra("postInfo", postInfo);
        startActivityForResult(intent,0);
    }

}
