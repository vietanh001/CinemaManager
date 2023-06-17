package com.example.image;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private final static int REQUEST_CODE_REGISTER = 10000;
    private TextView etTaiKhoan, etMatKhau;
    private Button btDangNhap;
    private TextView tvQuenMatKhau, tvTaoTaiKhoan;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        auth = FirebaseAuth.getInstance();

        btDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        tvQuenMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, "Reset lại mật khẩu!", Toast.LENGTH_SHORT).show();
            }
        });

        tvTaoTaiKhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
    }

    private void initView() {
        etTaiKhoan = findViewById(R.id.etTaiKhoan);
        etMatKhau = findViewById(R.id.etMatKhau);
        btDangNhap = findViewById(R.id.btDangNhap);
        tvQuenMatKhau = findViewById(R.id.tvQuenMatKhau);
        tvTaoTaiKhoan = findViewById(R.id.tvTaoTaiKhoan);
    }

    private void login(){
        String email = etTaiKhoan.getText().toString();
        String password = etMatKhau.getText().toString();
        if(email.isEmpty()){
            Toast.makeText(this, "Không để trống email!", Toast.LENGTH_SHORT).show();
        }
        if(password.isEmpty()){
            Toast.makeText(this, "Hãy nhập password!", Toast.LENGTH_SHORT).show();
        }
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                if(email.equals("admin@gmail.com")){
                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công với tài khoản Admin!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, AdminMainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, "Không đúng tài khoản, mật khẩu!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void register(){
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivityForResult(intent, REQUEST_CODE_REGISTER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_REGISTER){
            if(resultCode == RESULT_OK){
                final String email = data.getStringExtra("email");
                final String password = data.getStringExtra("password");
                etTaiKhoan.setText(email);
                etMatKhau.setText(password);
            }else{

            }
        }
    }
}