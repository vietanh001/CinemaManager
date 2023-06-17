package com.example.image.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.image.MainActivity;
import com.example.image.R;
import com.example.image.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class FragmentAccount extends Fragment implements View.OnClickListener{

    private EditText etHo, etTen, etSDT, etDiaChi;
    private TextView tvTaiKhoan;
    private Button btThayDoi;
    private DatabaseReference root_user = FirebaseDatabase.getInstance().getReference("user");
    private FirebaseUser auth = FirebaseAuth.getInstance().getCurrentUser();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);

        ShowInfor();

        btThayDoi.setOnClickListener(this);
    }

    private void initView(View view) {
        etHo = view.findViewById(R.id.etHo);
        etTen = view.findViewById(R.id.etTen);
        etSDT = view.findViewById(R.id.etSDT);
        etDiaChi = view.findViewById(R.id.etDiaChi);
        tvTaiKhoan = view.findViewById(R.id.tvTaiKhoan);
        btThayDoi = view.findViewById(R.id.btThayDoi);
    }

    private void ShowInfor(){
        String email = auth.getEmail().toString();
        String email1 = email.substring(0, email.length() - 4) + "-com";
        root_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    if(user.getUsername().equals(email)){
                        etHo.setText(user.getHo());
                        etTen.setText(user.getTen());
                        etDiaChi.setText(user.getDiaChi());
                        etSDT.setText(user.getsDT());
                        tvTaiKhoan.setText(user.getUsername());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view == btThayDoi){
            String email = auth.getEmail().toString();
            String email1 = email.substring(0, email.length() - 4) + "-com";
            String ho = etHo.getText().toString();
            String ten = etTen.getText().toString();
            String sdt = etSDT.getText().toString();
            String diaChi = etDiaChi.getText().toString();
            if(ho.isEmpty() || ten.isEmpty() || sdt.isEmpty() || diaChi.isEmpty()){
                Toast.makeText(getContext(), "Không để trông các trường thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!sdt.matches("[0-9]{10}")){
                Toast.makeText(getContext(), "Sai định dạng số điện thoại!", Toast.LENGTH_SHORT).show();
            }
            Map<String, Object> updates = new HashMap<>();
            updates.put("ho", ho);
            updates.put("diaChi", diaChi);
            updates.put("ten", ten);
            updates.put("sDT", sdt);
            root_user.child(email1).updateChildren(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(getContext(), "Cập nhập thành công!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Cập nhập thất bại!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}