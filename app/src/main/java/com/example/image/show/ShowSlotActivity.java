package com.example.image.show;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.image.AdminMainActivity;
import com.example.image.MainActivity;
import com.example.image.R;
import com.example.image.adapter.SlotShowAdapter;
import com.example.image.admin.SetUpActivity;
import com.example.image.listener.IOnSingleClickListener;
import com.example.image.model.Movie;
import com.example.image.model.Room;
import com.example.image.model.SetUp;
import com.example.image.model.Ticket;
import com.example.image.model.User;
import com.google.android.exoplayer2.C;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowSlotActivity extends AppCompatActivity implements SlotShowAdapter.ItemListener, View.OnClickListener{

    private SlotShowAdapter slotShowAdapter;
    private ArrayList<String> listSlot;
    private RecyclerView rvChoNgoi;
    private View vBack;
    private Button btDatVe;
    private TextView tvTenPhim, tvHTvaDT, tvSeat, tvTongTien, tvTenManHinh;
    private String maPhim, hinhThuc, gioChieu, ngayChieu, diaDiem, slot;
    private int row, col;
    String listSeatSelected = "Ghế đang chọn: ";
    private int countSeat = 0;
    private long total = 0;
    private Dialog dialog;

    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();;
    private ArrayList<Integer> selectedPositions = new ArrayList<>();
    private DatabaseReference root_user = FirebaseDatabase.getInstance().getReference("user");
    private DatabaseReference root_setUp = FirebaseDatabase.getInstance().getReference("setUp");
    private DatabaseReference root_movie = FirebaseDatabase.getInstance().getReference("movie");
    private DatabaseReference root_ticket = FirebaseDatabase.getInstance().getReference("ticket");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_slot);
        initView();
        Intent intent = getIntent();
        maPhim = intent.getStringExtra("maP");
        diaDiem = intent.getStringExtra("diaDiem_c");
        ngayChieu = intent.getStringExtra("ngayChieu_c");
        hinhThuc = intent.getStringExtra("hinhThuc_c");
        gioChieu = intent.getStringExtra("gioChieu_c");

        ShowSlot();
        ShowInfor();

        slotShowAdapter.setItemListener(this);
        vBack.setOnClickListener(this);
        btDatVe.setOnClickListener(this);
    }

    private void ShowInfor(){
        root_movie.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Movie movie = dataSnapshot.getValue(Movie.class);
                    if(movie.getMa().equals(maPhim)){
                        tvTenPhim.setText(movie.getTenP());
                        String[] kd = movie.getKiemDuyet().split("\\(|\\)");
                        tvHTvaDT.setText(hinhThuc + " - " + kd[kd.length - 1]);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initView() {
        rvChoNgoi = findViewById(R.id.rvChoNgoi);
        tvTenPhim = findViewById(R.id.tvTenPhim);
        tvTenManHinh = findViewById(R.id.tvTenManHinh);
        tvHTvaDT = findViewById(R.id.tvHTvaDT);
        tvSeat = findViewById(R.id.tvSeat);
        tvTongTien = findViewById(R.id.tvTongTien);
        btDatVe = findViewById(R.id.btDatVe);
        vBack = findViewById(R.id.vBack);
    }

    private void ShowSlot() {
        listSlot = new ArrayList<>();
        slotShowAdapter = new SlotShowAdapter(this, listSlot);
        rvChoNgoi.setAdapter(slotShowAdapter);
        root_setUp.child(diaDiem)
                .child(ngayChieu)
                .child(maPhim)
                .child(hinhThuc)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    SetUp setUp = dataSnapshot.getValue(SetUp.class);
                    if(setUp.getGioChieu().equals(gioChieu)){
                        tvTenManHinh.setText("Màn hình phòng " + setUp.getSoPhong());
                        List<List<String>> list = setUp.getList();
                        row = list.size();
                        col = list.get(0).size();
                        GridLayoutManager manager = new GridLayoutManager(ShowSlotActivity.this, col);
                        rvChoNgoi.setLayoutManager(manager);
                        for(int i = 0; i < row; i++){
                            for(int  j = 0; j < col; j++){
                                listSlot.add(list.get(i).get(j));
                            }
                        }
                    }
                }
                slotShowAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ShowSlotActivity.this, "Chưa SetUp lịch chiếu phim!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ShowDialogConfig(){

        if (countSeat == 0) {
            Toast.makeText(this, "Hãy chọn vị trí ghế để xem phim!", Toast.LENGTH_SHORT).show();
            return;
        }

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.item_config_booking);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        final TextView tvTenPhim = dialog.findViewById(R.id.tvTenPhim);
        final TextView tvDinhDang = dialog.findViewById(R.id.tvDinhDang);
        final TextView tvDiaDiem = dialog.findViewById(R.id.tvDiaDiem);
        final TextView tvNgayChieu = dialog.findViewById(R.id.tvNgayChieu);
        final TextView tvGioChieu = dialog.findViewById(R.id.tvGioChieu);
        final TextView tvPhongChieu = dialog.findViewById(R.id.tvPhongChieu);
        final TextView tvGhe = dialog.findViewById(R.id.tvGhe);
        final TextView tvSoVe = dialog.findViewById(R.id.tvSoVe);
        final TextView tvTien = dialog.findViewById(R.id.tvTien);

        final TextView btHuy = dialog.findViewById(R.id.btHuy);
        final TextView btDongY = dialog.findViewById(R.id.btDongY);

        root_movie.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Movie movie = dataSnapshot.getValue(Movie.class);
                    if(movie.getMa().equals(maPhim)){
                        tvTenPhim.setText(movie.getTenP());
                        tvDiaDiem.setText(diaDiem);
                        tvDinhDang.setText(hinhThuc);
                        tvNgayChieu.setText(ngayChieu.replace("-", "/"));
                        tvGioChieu.setText(gioChieu);

                        String[] s = tvTenManHinh.getText().toString().split(" ");
                        String phongChieu = s[s.length - 1];
                        tvPhongChieu.setText(phongChieu);
                        String ghe = tvSeat.getText().toString().substring(15);
                        tvGhe.setText(ghe);

                        String[] total = tvTongTien.getText().toString().split("-");
                        String t = total[total.length - 1];
                        String tien = t.substring(0, t.length() - 4).trim();
                        tvTien.setText(tien + " VNĐ");

                        String[] listghe = ghe.split(",");
                        tvSoVe.setText(listghe.length + "");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        btHuy.setOnClickListener(new IOnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                dialog.dismiss();
            }
        });

        btDongY.setOnClickListener(new IOnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                dialog.dismiss();
                BookingTicket();
            }
        });

        dialog.show();
    }


    private void BookingTicket(){

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        String currentDate = String.format("%02d", day) + "/" + String.format("%02d", month) + "/" + year;

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        String currentTime = String.format("%02d", hour) + ":" + String.format("%02d", minute) + ":" + String.format("%02d", second);

        String currentDateTime = String.format("%02d", day) + "-" + String.format("%02d", month) + "-" + year
                + " " + String.format("%02d", hour) + ":" + String.format("%02d", minute) + ":" + String.format("%02d", second);

        String[] total = tvTongTien.getText().toString().split("-");
        String t = total[total.length - 1];

        String[] s = tvTenManHinh.getText().toString().split(" ");
        String phongChieu = s[s.length - 1];

        root_movie.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Movie movie = dataSnapshot.getValue(Movie.class);
                    if (movie.getMa().equals(maPhim)) {
                        String email = currentUser.getEmail().toString();
                        String email1 = email.substring(0, email.length() - 4) + "-com";
                        String tenPhim = movie.getTenP();
                        String ghe = tvSeat.getText().toString().substring(15);
                        String tongTien = t.substring(0, t.length() - 4).trim();

                        root_setUp.child(diaDiem)
                                .child(ngayChieu)
                                .child(maPhim)
                                .child(hinhThuc)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            SetUp setUp = dataSnapshot.getValue(SetUp.class);
                                            if (setUp.getGioChieu().equals(gioChieu)) {
                                                List<List<String>> listUpdate = new ArrayList<>();
                                                listUpdate.addAll(setUp.getList());

                                                String[] seatArray = ghe.split(", ");
                                                for (String seat : seatArray) {
                                                    String hang = seat.substring(0, 1);
                                                    int cot = Integer.valueOf(seat.substring(1));

                                                    for (int i = 0; i < listUpdate.size(); i++) {
                                                        String currentHang = listUpdate.get(i).get(0);
                                                        if (currentHang.equals(hang)) {
                                                            int columnIndex = cot;
                                                            String oldValue = listUpdate.get(i).get(columnIndex);
                                                            String newValue = String.valueOf(Integer.valueOf(oldValue) + 4);
                                                            listUpdate.get(i).set(columnIndex, newValue);
                                                        }
                                                    }
                                                }

                                                Map<String, Object> update = new HashMap<>();
                                                update.put("list", listUpdate);
                                                dataSnapshot.getRef().updateChildren(update);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(ShowSlotActivity.this, "Lỗi cập nhật dữ liệu", Toast.LENGTH_SHORT).show();
                                    }
                                });

                        root_user.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                                    User user = dataSnapshot1.getValue(User.class);
                                    if(user.getUsername().equals(email)){
                                        String hoTen = user.getHo() + " " + user.getTen();


                                        root_ticket.child(email1).child(currentDateTime)
                                                .setValue(new Ticket(email, hoTen, tenPhim, hinhThuc, currentDate, currentTime, diaDiem, ngayChieu.replace("-", "/"), phongChieu, gioChieu, ghe, tongTien))
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {

                                                        Toast.makeText(ShowSlotActivity.this, "Đặt vé thành công!", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(ShowSlotActivity.this, MainActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                });
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


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
        if (view == vBack) {
            Intent intent = new Intent(ShowSlotActivity.this, ShowDateTimeActivity.class);
            intent.putExtra("maP", maPhim);
            startActivity(intent);
            finish();
        }

        if (view == btDatVe) {
            ShowDialogConfig();
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        slot = slotShowAdapter.getSlot(position);
        root_setUp.child(diaDiem)
                .child(ngayChieu)
                .child(maPhim)
                .child(hinhThuc).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    SetUp setUp = dataSnapshot.getValue(SetUp.class);
                    int count = 0;
                    if(setUp.getGioChieu().equals(gioChieu)){
                        List<List<String>> list = setUp.getList();
                        row = list.size();
                        col = list.get(0).size();
                        for(int i = 0; i < row; i++){
                            for(int  j = 0; j < col; j++){
                                count++;
                                if(count == position){
                                    if(selectedPositions.contains(position)){
                                        if(slotShowAdapter.getSlot(position).equals("0")){
                                            total -= 80000;
                                        }
                                        if(slotShowAdapter.getSlot(position).equals("1")){
                                            total -= 110000;
                                        }
                                        String s = list.get(i).get(0) + String.valueOf(j+1) + ", ";
                                        listSeatSelected = listSeatSelected.replace(s, "");
                                        selectedPositions.remove(Integer.valueOf(position));
                                        countSeat--;
                                    } else{
                                        if(slotShowAdapter.getSlot(position).equals("2")){
                                            total += 80000;
                                        }
                                        if(slotShowAdapter.getSlot(position).equals("3")){
                                            total += 110000;
                                        }
                                        listSeatSelected += list.get(i).get(0) + String.valueOf(j+1) + ", ";
                                        selectedPositions.add(position);
                                        countSeat++;
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
                DecimalFormat decimalFormat = new DecimalFormat("#,###");
                tvTongTien.setText(String.valueOf(countSeat) + " ghế - " + decimalFormat.format(total) + " VNĐ" );
                tvSeat.setText(listSeatSelected.substring(0, listSeatSelected.length() - 2));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ShowSlotActivity.this, "Chưa SetUp lịch chiếu phim!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}