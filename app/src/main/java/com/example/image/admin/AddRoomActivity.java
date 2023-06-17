package com.example.image.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.image.AdminMainActivity;
import com.example.image.R;
import com.example.image.adapter.admin.AddRoomAdapter;
import com.example.image.model.Room;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddRoomActivity extends AppCompatActivity implements View.OnClickListener, AddRoomAdapter.ItemListener {

    private Button btSaveSlot, btHienThi;
    private EditText etTenPhong;
    private Spinner spSoGhe, spSoHang;
    private View vBack;
    private RecyclerView rvChoNgoi;
    private TextView tvTenP;
    private String list;
    private AddRoomAdapter slotAdapter;
    private ArrayList<String> listSlot;
    private String tenP;
    private String slot;
    private int soGhe, soHang;
    private DatabaseReference root_room = FirebaseDatabase.getInstance().getReference("room");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);
        initView();

        vBack.setOnClickListener(this);
        btSaveSlot.setOnClickListener(this);
        btHienThi.setOnClickListener(this);

        spSoGhe.setAdapter(new ArrayAdapter<>(this, R.layout.item_spinner,
                getResources().getStringArray(R.array.spSoGhe)));

        spSoHang.setAdapter(new ArrayAdapter<>(this, R.layout.item_spinner,
                getResources().getStringArray(R.array.spSoHang)));
    }

    private void initView() {
        etTenPhong = findViewById(R.id.etTenPhong);
        spSoGhe = findViewById(R.id.spSoGhe);
        spSoHang = findViewById(R.id.spSoHang);
        tvTenP = findViewById(R.id.tvTenP);
        rvChoNgoi = findViewById(R.id.rvChoNgoi);
        btSaveSlot = findViewById(R.id.btSaveSlot);
        btHienThi = findViewById(R.id.btHienThi);
        vBack = findViewById(R.id.vBack);
    }

    public void check(){
        tenP = etTenPhong.getText().toString();
        if(tenP.isEmpty()){
            Toast.makeText(this, "Không để trống tên phòng!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(spSoGhe.getSelectedItem().toString().equals("Chọn số ghế trên một hàng")){
            Toast.makeText(this, "Hãy chọn số ghế!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(spSoHang.getSelectedItem().toString().equals("Chọn số hàng trong phòng")){
            Toast.makeText(this, "Hãy chọn số hàng!", Toast.LENGTH_SHORT).show();
            return;
        }
    }


    @Override
    public void onClick(View view) {
        if(view == vBack){
            Intent intent = new Intent(AddRoomActivity.this, AdminMainActivity.class);
            startActivity(intent);
            finish();
        }

        if(view == btHienThi){
            tenP = etTenPhong.getText().toString();
            if(tenP.isEmpty()){
                Toast.makeText(this, "Không để trống tên phòng!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(spSoGhe.getSelectedItem().toString().equals("Chọn số ghế trên một hàng")){
                Toast.makeText(this, "Hãy chọn số ghế!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(spSoHang.getSelectedItem().toString().equals("Chọn số hàng trong phòng")){
                Toast.makeText(this, "Hãy chọn số hàng!", Toast.LENGTH_SHORT).show();
                return;
            }
            tvTenP.setText("Màn hình phòng " + tenP);
            list = "";
            soGhe = Integer.valueOf(spSoGhe.getSelectedItem().toString());
            soHang = Integer.valueOf(spSoHang.getSelectedItem().toString());

            int c = 65;
            for(int i = 0; i < soGhe * soHang; i++){
                if(i%soGhe == 0){
                    list += String.valueOf((char) c) + ", ";
                    c++;
                }
                list += "0, ";
            }

            GridLayoutManager manager = new GridLayoutManager(this, soGhe + 1);
            rvChoNgoi.setLayoutManager(manager);
            listSlot = new ArrayList<>();
            slotAdapter = new AddRoomAdapter(this, listSlot);
            rvChoNgoi.setAdapter(slotAdapter);

            String[] cho = list.split(", ");
            for(String i : cho){
                listSlot.add(i);
            }
            slotAdapter.notifyDataSetChanged();
            slotAdapter.setItemListener(this);

        }

        if(view == btSaveSlot){
            root_room.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Room room = dataSnapshot.getValue(Room.class);
                        if(room.getTenPhong().equals(tenP)){
                            Toast.makeText(AddRoomActivity.this, "Trùng số phòng trong Firebase!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    int count = 0;
                    List<List<String>> list = new ArrayList<>();
                    for (int i = 0; i < soHang; i++) {
                        List<String> row = new ArrayList<>();
                        for (int j = 0; j < soGhe + 1; j++) {
                            row.add(listSlot.get(count++));
                        }
                        list.add(row);
                    }
                    root_room.child(tenP).setValue(new Room(tenP, list)).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(AddRoomActivity.this, "Lưu số ghế trong rạp thành công!", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddRoomActivity.this, "Lưu không thành công!", Toast.LENGTH_SHORT).show();
                        }
                    });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    @Override
    public void onItemClick(View view, int position) {

    }
}