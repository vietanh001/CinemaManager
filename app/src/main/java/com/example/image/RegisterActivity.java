package com.example.image;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.image.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText etHo, etTen, etSDT, etDiaChi, etTaiKhoan, etMatKhau, etLaiMatKhau;
    private Button btDangKy, btTroLai;
    private TextView tvDangNhap;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private String username = "", password = "", repassword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        btDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

        tvDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btTroLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initView() {
        etHo = findViewById(R.id.etHo);
        etTen = findViewById(R.id.etTen);
        etSDT = findViewById(R.id.etSDT);
        etDiaChi = findViewById(R.id.etDiaChi);
        etTaiKhoan = findViewById(R.id.etTaiKhoan);
        etMatKhau = findViewById(R.id.etMatKhau);
        etLaiMatKhau = findViewById(R.id.etLaiMatKhau);
        btDangKy = findViewById(R.id.btDangKy);
        btTroLai = findViewById(R.id.btTroLai);
        tvDangNhap = findViewById(R.id.tvDangNhap);
    }

    private void register(){
        String ho = etHo.getText().toString();
        String ten = etTen.getText().toString();
        String sdt = etSDT.getText().toString();
        String diaChi = etDiaChi.getText().toString();
        username = etTaiKhoan.getText().toString();
        password = etMatKhau.getText().toString();
        repassword = etLaiMatKhau.getText().toString();
        if(ho.isEmpty() || ten.isEmpty() || sdt.isEmpty() || diaChi.isEmpty() ||
                username.isEmpty() || password.isEmpty() || repassword.isEmpty()){
            Toast.makeText(this, "Không để trông các trường thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!sdt.matches("[0-9]{10}")){
            Toast.makeText(this, "Sai định dạng số điện thoại!", Toast.LENGTH_SHORT).show();
        }
        if(password.length() < 6){
            Toast.makeText(this, "Password phải ít nhất 6 ký tự!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!password.equals(repassword)){
            Toast.makeText(this, "Nhập 2 trường password phải giống nhau!", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.createUserWithEmailAndPassword(etTaiKhoan.getText().toString(), etMatKhau.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            DatabaseReference root_user = FirebaseDatabase.getInstance().getReference("user");
            @Override
            public void onSuccess(AuthResult authResult) {
//                String userId = auth.getCurrentUser().getUid();
                String usernameChild = username.substring(0, username.length() - 4) + "-com";
                root_user.child(usernameChild).setValue(new User(ho, ten, sdt, diaChi, username)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(RegisterActivity.this, "Tạo tài khoản thành công!", Toast.LENGTH_SHORT).show();
                        final Intent data = new Intent();
                        data.putExtra("email", username);
                        data.putExtra("password", password);
                        setResult(RESULT_OK, data);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, "Tạo tài khoản không thành công!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, "Tạo tài khoản không thành công!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}