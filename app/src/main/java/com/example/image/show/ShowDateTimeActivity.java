package com.example.image.show;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.image.R;
import com.example.image.adapter.DateAdapter;
import com.example.image.adapter.FormAdapter;
import com.example.image.adapter.TimeAdapter;
import com.example.image.model.Date;
import com.example.image.model.DateTime;
import com.google.android.exoplayer2.C;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;

public class ShowDateTimeActivity extends AppCompatActivity
        implements DateAdapter.ItemListener, FormAdapter.ItemListener, View.OnClickListener{
    private Spinner spDiaDiem;
    private RecyclerView rvNgayChieu, rvGioChieu, rvLoai;
    private DateAdapter dateAdapter;
    private TimeAdapter timeAdapter;
    private FormAdapter formAdapter;
    private ArrayList<Date> listDate;
    private ArrayList<String> listTime;
    private ArrayList<String> listForm;
    private Button bt;
    private DatabaseReference root_date = FirebaseDatabase.getInstance().getReference("date");
    private DatabaseReference root_setUp = FirebaseDatabase.getInstance().getReference("setUp");
    private TextView tvNgay;
    private View vBack;
    private String maP, diaDiem = "";

    private String hinhThuc_c = "", gioChieu_c = "", ngayChieu_c = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_datetime);
        Intent intent = getIntent();
        maP = intent.getStringExtra("maP");
        initView();
        tvNgay.setText(dateNow());
        ShowDate();
        ShowForm();
        ShowTime();
        vBack.setOnClickListener(this);
        bt.setOnClickListener(this);

        dateAdapter.setItemListener(new DateAdapter.ItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                Date date = listDate.get(position);
                String thu = date.getThu();
                int ngay = Integer.valueOf(date.getNgay().substring(0, 2));
                int thang = Integer.valueOf(date.getNgay().substring(3, 5));
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                ngayChieu_c = String.format("%02d" ,ngay) + "-" + String.format("%02d" ,thang) + "-" + String.valueOf(year);
                String ngayThang = "ngày " + String.valueOf(ngay) + " tháng " + String.valueOf(thang);
                tvNgay.setText(thu + ", " + ngayThang);
                ShowForm();

                ShowTime();
                formAdapter.setItemListener(new FormAdapter.ItemListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        hinhThuc_c = listForm.get(position);
                        ShowTime();
                        timeAdapter.setItemListener(new DateAdapter.ItemListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                gioChieu_c = listTime.get(position);
                            }
                        });
                    }
                });
                timeAdapter.setItemListener(new DateAdapter.ItemListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        gioChieu_c = listTime.get(position);
                    }
                });
            }
        });

        formAdapter.setItemListener(new FormAdapter.ItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                hinhThuc_c = listForm.get(position);
                ShowTime();
                timeAdapter.setItemListener(new DateAdapter.ItemListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        gioChieu_c = listTime.get(position);
                    }
                });
            }
        });

        timeAdapter.setItemListener(new DateAdapter.ItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                gioChieu_c = listTime.get(position);
            }
        });

        spDiaDiem.setAdapter(new ArrayAdapter<>(this, R.layout.item_spinner_address,
                getResources().getStringArray(R.array.spDiaDiem)));

        spDiaDiem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                TextView selectedItemView = (TextView) view;
                selectedItemView.setTextColor(Color.RED);
                diaDiem = adapterView.getItemAtPosition(position).toString();
                ShowForm();
                ShowTime();
                formAdapter.setItemListener(new FormAdapter.ItemListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        hinhThuc_c = listForm.get(position);
                        ShowTime();
                        timeAdapter.setItemListener(new DateAdapter.ItemListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                gioChieu_c = listTime.get(position);
                            }
                        });
                    }
                });
                timeAdapter.setItemListener(new DateAdapter.ItemListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        gioChieu_c = listTime.get(position);
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void ShowForm(){
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        rvLoai.setLayoutManager(manager);
        listForm = new ArrayList<>();
        formAdapter = new FormAdapter(this, listForm);
        rvLoai.setAdapter(formAdapter);

        root_setUp.child(diaDiem)
                .child(ngayChieu_c)
                .child(maP)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            String hinhThuc = dataSnapshot.getKey();
                            listForm.add(hinhThuc);
                        }
                        formAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void ShowTime() {
        GridLayoutManager manager = new GridLayoutManager(this, 4);
        rvGioChieu.setLayoutManager(manager);
        listTime = new ArrayList<>();
        timeAdapter = new TimeAdapter(this, listTime);
        rvGioChieu.setAdapter(timeAdapter);
        if(hinhThuc_c.isEmpty()){
            return;
        }
        root_setUp.child(diaDiem)
                .child(ngayChieu_c)
                .child(maP)
                .child(hinhThuc_c)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            String gioChieu = dataSnapshot.getKey();
                            listTime.add(gioChieu);
                        }
                        timeAdapter.notifyDataSetChanged();
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

    private void initView() {
        spDiaDiem = findViewById(R.id.spDiaDiem);
        vBack = findViewById(R.id.vBack);
        rvNgayChieu = findViewById(R.id.rvNgayChieu);
        rvLoai = findViewById(R.id.rvLoai);
        rvGioChieu = findViewById(R.id.rvGioChieu);
        bt = findViewById(R.id.bt);
        tvNgay = findViewById(R.id.tvNgay);
    }

    private String dateNow(){
        LocalDate date = LocalDate.now();
        int dayOfMonth = date.getDayOfMonth();
        int monthValue = date.getMonthValue();
        String ngayThang = "ngày " + String.valueOf(dayOfMonth) + " tháng " + String.valueOf(monthValue);
        String kq = "Hôm nay, " + ngayThang;

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        ngayChieu_c = String.format("%02d", dayOfMonth) + "-" + String.format("%02d", monthValue) + "-" + String.valueOf(year);
        return kq;
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onClick(View view) {
        if(view == vBack){
            finish();
        }
        if(view == bt){

            String diaDiem = spDiaDiem.getSelectedItem().toString();
            if(diaDiem.equals("Địa điểm")){
                Toast.makeText(this, "Hãy chọn địa điểm!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(hinhThuc_c.isEmpty()){
                Toast.makeText(this, "Hãy chọn hình thức chiếu!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(gioChieu_c.isEmpty()){
                Toast.makeText(this, "Hãy chọn giờ chiếu phim!", Toast.LENGTH_SHORT).show();
                return;
            }

            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH) + 1;
            int year = calendar.get(Calendar.YEAR) ;
            String currentDay = String.format("%02d", day) + "-" + String.format("%02d", month) + "-" + year;

            if(currentDay.equals(ngayChieu_c)) {
                LocalTime current = LocalTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                LocalTime currentTime = LocalTime.parse(current.format(formatter));
                LocalTime targetTime = LocalTime.parse(gioChieu_c.trim() + ":00", formatter);
                long minutesDifference = currentTime.until(targetTime, ChronoUnit.MINUTES);
                if (minutesDifference >= 30) {
                    Intent intent1 = new Intent(ShowDateTimeActivity.this, ShowSlotActivity.class);
                    intent1.putExtra("maP", maP.trim());
                    intent1.putExtra("diaDiem_c", diaDiem.trim());
                    intent1.putExtra("hinhThuc_c", hinhThuc_c.trim());
                    intent1.putExtra("gioChieu_c", gioChieu_c.trim());
                    intent1.putExtra("ngayChieu_c", ngayChieu_c.trim());
                    startActivity(intent1);
                    finish();
                } else {
                    Toast.makeText(this, "Chỉ được đặt vé trước giờ chiếu tối thiểu 30 phút!", Toast.LENGTH_SHORT).show();
                }
            } else{
                Intent intent1 = new Intent(ShowDateTimeActivity.this, ShowSlotActivity.class);
                intent1.putExtra("maP", maP.trim());
                intent1.putExtra("diaDiem_c", diaDiem.trim());
                intent1.putExtra("hinhThuc_c", hinhThuc_c.trim());
                intent1.putExtra("gioChieu_c", gioChieu_c.trim());
                intent1.putExtra("ngayChieu_c", ngayChieu_c.trim());
                startActivity(intent1);
                finish();
            }
        }
    }
}