package com.example.myapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.CameraInfo;
import com.example.myapplication.GPS_Data;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class VideosingupActivity extends BasicActivity {
    private EditText videoEdit;
    private Button signup_btn;
    private FirebaseUser firebaseUser;
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
        setContentView(R.layout.activity_videosingup);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setToolbarTitle("회원정보");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        signup_btn =findViewById(R.id.sing_btn);
        setToolbarTitle("스트리밍 주소 등록");
        Intent intent = getIntent();
        String s_address = intent.getStringExtra("stream");
        ((EditText)findViewById(R.id.editText)).setText(s_address);
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = ((EditText) findViewById(R.id.editText)).getText().toString();
                if(address.length()<7){
                    Toast.makeText(getApplicationContext(),"다시 입력해주세요",Toast.LENGTH_SHORT).show();
                    return;
                }
                CameraInfo cameraInfo = new CameraInfo(address);
                signup(cameraInfo);
                finish();
            }
        });

    }
    private void signup(CameraInfo cameraInfo){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Stream").document(firebaseUser.getUid()).set(cameraInfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void avoid) {


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "실패!.", Toast.LENGTH_SHORT).show();

                    }
                });
    }
}
