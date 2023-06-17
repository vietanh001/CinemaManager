package com.example.image.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.image.AdminMainActivity;
import com.example.image.R;
import com.example.image.adapter.DateAdapter;
import com.example.image.model.Date;
import com.example.image.model.DateTime;
import com.example.image.model.Movie;
import com.example.image.model.Room;
import com.example.image.model.SetUp;
import com.example.image.model.Validate;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class SetUpActivity extends AppCompatActivity implements DateAdapter.ItemListener, View.OnClickListener{

    private RecyclerView rvNgayChieu;
    private DateAdapter dateAdapter;
    private ArrayList<Date> listDate;
    private Spinner spTenPhim, spSoPhong, spHinhThuc, spDiaDiem;
    private TextView tvGioChieu;
    private DatabaseReference root_date = FirebaseDatabase.getInstance().getReference("date");
    private DatabaseReference root_movie = FirebaseDatabase.getInstance().getReference("movie");
    private DatabaseReference root_room = FirebaseDatabase.getInstance().getReference("room");
    private DatabaseReference root_setUp = FirebaseDatabase.getInstance().getReference("setUp");
    private DatabaseReference root_validateSetUp = FirebaseDatabase.getInstance().getReference("validateSetUp");
    private Button bt, btSetUp;
    private TextView tvNgay;
    private View vBack;
    private String chooseDate = "";
    private LocalTime bd1, kt1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up);
        initView();
        tvNgay.setText(dateNow());
        vBack.setOnClickListener(this);
        bt.setOnClickListener(this);
        btSetUp.setOnClickListener(this);
        tvGioChieu.setOnClickListener(this);
        ShowDate();
        dateAdapter.setItemListener(this);

        ShowNameMovie();

        ShowNumberRoom();

        spDiaDiem.setAdapter(new ArrayAdapter<>(this, R.layout.item_spinner,
                getResources().getStringArray(R.array.spDiaDiem)));

        spHinhThuc.setAdapter(new ArrayAdapter<>(this, R.layout.item_spinner,
                getResources().getStringArray(R.array.spHinhThuc)));
    }

    private void initView() {
        vBack = findViewById(R.id.vBack);
        bt = findViewById(R.id.bt);
        btSetUp = findViewById(R.id.btSetUp);
        spTenPhim = findViewById(R.id.spTenPhim);
        spSoPhong = findViewById(R.id.spSoPhong);
        spHinhThuc = findViewById(R.id.spHinhThuc);
        spDiaDiem = findViewById(R.id.spDiaDiem);
        tvGioChieu = findViewById(R.id.tvGioChieu);
        rvNgayChieu = findViewById(R.id.rvNgayChieu);
        tvNgay = findViewById(R.id.tvNgay);
    }

    private void ShowNameMovie() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_spinner);
        adapter.setDropDownViewResource(R.layout.item_spinner);
        spTenPhim.setAdapter(adapter);
        adapter.add("Chọn phim");
        root_movie.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String movieName = dataSnapshot.child("tenP").getValue(String.class);
                    adapter.add(movieName);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void ShowNumberRoom(){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_spinner);
        adapter.setDropDownViewResource(R.layout.item_spinner);
        spSoPhong.setAdapter(adapter);
        adapter.add("Chọn phòng");
        root_room.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String numberRoom = dataSnapshot.child("tenPhong").getValue(String.class);
                    adapter.add(numberRoom);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    private void ShowDate() {
        LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        rvNgayChieu.setLayoutManager(manager);
        listDate = new ArrayList<>();
        dateAdapter = new DateAdapter(this, listDate);
        rvNgayChieu.setAdapter(dateAdapter);
        root_date.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Date date = dataSnapshot.getValue(Date.class);
                    listDate.add(date);
                }
                dateAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public String subDayOfWeek(String s){
        String day = "";
        switch (s){
            case "MONDAY":
                day = "Thứ 2";
                break;
            case "TUESDAY":
                day = "Thứ 3";
                break;
            case "WEDNESDAY":
                day = "Thứ 4";
                break;
            case "THURSDAY":
                day = "Thứ 5";
                break;
            case "FRIDAY":
                day = "Thứ 6";
                break;
            case "SATURDAY":
                day = "Thứ 7";
                break;
            case "SUNDAY":
                day = "Chủ nhật";
                break;
        }
        return day;
    }

    public String dayAndMonth(String day, String month){
        if(day.length() == 1)
            day = "0" + day;
        if(month.length() == 1)
            month = "0" + month;
        return day + "/" + month;
    }

    private String dateNow(){
        LocalDate date = LocalDate.now();
        int dayOfMonth = date.getDayOfMonth();
        int monthValue = date.getMonthValue();
        String ngayThang = "ngày " + String.valueOf(dayOfMonth) + " tháng " + String.valueOf(monthValue);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        chooseDate = String.format("%02d", dayOfMonth) + "-" + String.format("%02d", monthValue) + "-" + String.valueOf(year);

        String kq = "Hôm nay, " + ngayThang;
        return kq;
    }

    private void AddDate() {
        LocalDate currentDate = LocalDate.now();
        for (int i = 0; i < 7; i++) {
            LocalDate date = currentDate.plusDays(i);
            int dayOfMonth = date.getDayOfMonth();
            int monthValue = date.getMonthValue();
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            String dayOfWeekString = subDayOfWeek(dayOfWeek.toString());
            if (i == 0){
                root_date.child(i+"").setValue(
                        new Date("Hôm nay", dayAndMonth(String.valueOf(dayOfMonth), String.valueOf(monthValue))));
            }
            else{
                root_date.child(i+"").setValue(
                        new Date(dayOfWeekString, dayAndMonth(String.valueOf(dayOfMonth), String.valueOf(monthValue))));
            }
        }
    }

    private static boolean isOverlapping(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
        return start1.isBefore(end2) && start2.isBefore(end1);
    }


    @Override
    public void onItemClick(View view, int position) {
        Date date = listDate.get(position);
        String thu = date.getThu();
        int ngay = Integer.valueOf(date.getNgay().substring(0, 2));
        int thang = Integer.valueOf(date.getNgay().substring(3, 5));
        Calendar calendar = Calendar.getInstance();
        int nam = calendar.get(Calendar.YEAR);
        String ngayThang = "ngày " + String.valueOf(ngay) + " tháng " + String.valueOf(thang);
        tvNgay.setText(thu + ", " + ngayThang);
        chooseDate = String.format("%02d", ngay) + "-" + String.format("%02d", thang) + "-" + String.valueOf(nam);
    }

    String kq = "";
    boolean check;

    @Override
    public void onClick(View view) {
        if(view == tvGioChieu){
            final Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                            tvGioChieu.setText(selectedTime);
                        }
                    }, hour, minute, true);
            timePickerDialog.show();
        }
        if(view == vBack){
            Intent intent = new Intent(SetUpActivity.this, AdminMainActivity.class);
            startActivity(intent);
            finish();
        }

        if(view == bt){
            AddDate();
            ShowDate();
        }

        if(view == btSetUp){
            String tenP = spTenPhim.getSelectedItem().toString();
            String diaDiem = spDiaDiem.getSelectedItem().toString();
            String soP = spSoPhong.getSelectedItem().toString();
            String gioChieu = tvGioChieu.getText().toString();
            String hinhT = spHinhThuc.getSelectedItem().toString();
            if(tenP.equals("Chọn phim")){
                Toast.makeText(this, "Hãy chọn phim!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(diaDiem.equals("Địa điểm")){
                Toast.makeText(this, "Hãy chọn địa điểm chiếu phim!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(soP.equals("Chọn phòng")){
                Toast.makeText(this, "Hãy chọn phòng!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(soP.equals("Chọn giờ chiếu phim")){
                Toast.makeText(this, "Hãy chọn giờ chiếu phim!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(hinhT.equals("Chọn hình thức chiếu")){
                Toast.makeText(this, "Hãy chọn hình thức chiếu!", Toast.LENGTH_SHORT).show();
                return;
            }
            check = false;
            root_movie.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Movie movie = dataSnapshot.getValue(Movie.class);
                        if(movie.getTenP().equals(tenP)){
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                            bd1 = LocalTime.parse(gioChieu, formatter);
                            kt1 = bd1.plusMinutes(movie.getThoiLuong() + 15);
                            root_validateSetUp.child(diaDiem).child(chooseDate).child(soP).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for(DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                                        Validate validate = dataSnapshot1.getValue(Validate.class);
                                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                                        LocalTime bd2 = LocalTime.parse(validate.getGioChieu(), formatter);
                                        LocalTime kt2 = LocalTime.parse(validate.getGioHet(), formatter);
                                        if(isOverlapping(bd1, kt1, bd2, kt2)){
                                            check = true;
                                            kq = validate.getTenPhim();
                                            break;
                                        }
                                    }
                                    if(!check){
                                        if(movie.getTenP().equals(tenP)){
                                            root_room.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    for(DataSnapshot dataSnapshot2 : snapshot.getChildren()){
                                                        Room room = dataSnapshot2.getValue(Room.class);
                                                        if(room.getTenPhong().equals(soP)){
                                                            root_setUp.child(diaDiem).
                                                                    child(String.valueOf(chooseDate)).
                                                                    child(movie.getMa()).
                                                                    child(hinhT.trim()).
                                                                    child(gioChieu).
                                                                    setValue(new SetUp(movie.getMa(), diaDiem, soP, chooseDate, gioChieu, hinhT.trim(), room.getList()))
                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void unused) {
                                                                            Toast.makeText(SetUpActivity.this, "SetUp thông tin thành công!", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    });

                                                            root_validateSetUp.child(diaDiem)
                                                                    .child(chooseDate)
                                                                    .child(soP)
                                                                    .child(bd1.toString() + "-" + kt1.toString())
                                                                    .setValue(new Validate(movie.getTenP(), bd1.toString(), kt1.toString()));
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                        }
                                    }
                                    else{
                                        Toast.makeText(SetUpActivity.this, "Giờ này phòng đang chiếu phim " + kq + "!", Toast.LENGTH_SHORT).show();
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
    }
}