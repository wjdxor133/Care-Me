package com.example.myapplication.activity;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.example.myapplication.util.Util.showToast;

public class SignUpActivity extends BasicActivity {


    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setToolbarTitle("");
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.signUpButton).setOnClickListener(onClickListener);
        findViewById(R.id.gotoLoginButton).setOnClickListener(onClickListener);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    } // 로그아웃 후 회원가입 창으로 간후, 뒤로가기 누르면 종료

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.signUpButton:
                    singUp();
                    break;
                case R.id.gotoLoginButton:
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
            }
        }
    };

    private void singUp() {
        EditText email = findViewById(R.id.emailEditText);
        String sEmail = email.getText().toString();

        EditText password = findViewById(R.id.passwordEditText);
        EditText passwordCheck = findViewById(R.id.passwordCheckEditText);
        String sPWD = password.getText().toString();
        String sPWDC = passwordCheck.getText().toString();


        if (email.length() > 0 && password.length() > 0.8 && passwordCheck.length() > 0) {
            if (sPWD.equals(sPWDC)) {
                final RelativeLayout loaderLayout = findViewById(R.id.loaderLayout);
                loaderLayout.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(sEmail, sPWD)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                loaderLayout.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    showToast(SignUpActivity.this,"가입을 환영합니다.");
                                    Intent it = new Intent(SignUpActivity.this, SlideActivity.class);
                                    startActivity(it);
                                    //성공 했을 때
                                } else {
                                    if (task.getException() != null) {
                                        //Toast.makeText(getApplicationContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                                        showToast(SignUpActivity.this,task.getException().toString());
                                        //실패 했을 때
                                    }
                                }

                                // ...
                            }
                        });

            } else {

                showToast(SignUpActivity.this,"비밀번호를 다시 확인해주세요.");
            }
        } else {
            showToast(SignUpActivity.this,"이메일 또는 비밀번호를 입력해주세요.");
        }
    }

}
