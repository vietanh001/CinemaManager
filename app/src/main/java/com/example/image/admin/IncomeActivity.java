package com.example.image.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.image.AdminMainActivity;
import com.example.image.R;
import com.example.image.adapter.TicketHistoryAdapter;
import com.example.image.adapter.admin.IncomeAdapter;
import com.example.image.model.Income;
import com.example.image.model.Ticket;
import com.example.image.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;

public class IncomeActivity extends AppCompatActivity implements IncomeAdapter.ItemListener, View.OnClickListener{
    private RecyclerView rvThongKe;
    private IncomeAdapter incomeAdapter;
    private ArrayList<Income> listIncome;
    private View vBack;
    private TextView tvBatDau, tvKetThuc;
    private Button btThongKe;
    private int soVe;
    private long doanhThu;
    private DatabaseReference root_ticket = FirebaseDatabase.getInstance().getReference("ticket");
    private DatabaseReference root_user = FirebaseDatabase.getInstance().getReference("user");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);
        initView();

        vBack.setOnClickListener(this);
        tvBatDau.setOnClickListener(this);
        tvKetThuc.setOnClickListener(this);
        btThongKe.setOnClickListener(this);
    }

    private void initView() {
        vBack = findViewById(R.id.vBack);
        tvBatDau = findViewById(R.id.tvBatDau);
        tvKetThuc = findViewById(R.id.tvKetThuc);
        btThongKe = findViewById(R.id.btThongKe);
        rvThongKe = findViewById(R.id.recyclerView);
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    private String ngayTK;
    private long c;

    @Override
    public void onClick(View view) {
        if(view == vBack){
            Intent intent = new Intent(IncomeActivity.this, AdminMainActivity.class);
            startActivity(intent);
            finish();
        }
        if(view == tvBatDau){
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(IncomeActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                    String date = "";
                    if(d<10)
                        date = "0" + d;
                    else
                        date = String.valueOf(d);
                    if(m>8){
                        date += "/" + (m+1) + "/" + y;
                    }else{
                        date += "/0" + (m+1) + "/" + y;
                    }
                    tvBatDau.setText(date);
                }
            }, year, month, day);
            dialog.show();
        }

        if(view == tvKetThuc){
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(IncomeActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                    String date = "";
                    if(d<10)
                        date = "0" + d;
                    else
                        date = String.valueOf(d);
                    if(m>8){
                        date += "/" + (m+1) + "/" + y;
                    }else{
                        date += "/0" + (m+1) + "/" + y;
                    }
                    tvKetThuc.setText(date);
                }
            }, year, month, day);
            dialog.show();
        }

        if(view == btThongKe){
            String bd = tvBatDau.getText().toString();
            String kt = tvKetThuc.getText().toString();
            if(bd.isEmpty() || kt.isEmpty()){
                Toast.makeText(this, "Không để trống ngày!", Toast.LENGTH_SHORT).show();
                return;
            }
            LinearLayoutManager manager = new LinearLayoutManager(IncomeActivity.this, RecyclerView.VERTICAL, false);
            rvThongKe.setLayoutManager(manager);
            listIncome = new ArrayList<>();
            incomeAdapter = new IncomeAdapter(IncomeActivity.this, listIncome);
            rvThongKe.setAdapter(incomeAdapter);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dateBD = LocalDate.parse(bd, formatter);
            LocalDate dateKT = LocalDate.parse(kt, formatter);
            long daysBetween = ChronoUnit.DAYS.between(dateBD, dateKT);
            ngayTK = bd;

            for(long i = 0; i <= daysBetween; i++){
                root_ticket.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        soVe = 0;
                        doanhThu = 0;
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                Ticket ticket = dataSnapshot1.getValue(Ticket.class);
                                if(ticket.getNgayChieu().equals(ngayTK)){
                                    String[] listVe = ticket.getGhe().split(",");
                                    soVe += listVe.length;
                                    doanhThu += Long.valueOf(ticket.getTongTien().replace(".", ""));
                                }
                            }
                        }
                        DecimalFormat decimalFormat = new DecimalFormat("#,###");
                        listIncome.add(new Income(ngayTK, soVe + "", decimalFormat.format(doanhThu)));
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        LocalDate date = LocalDate.parse(ngayTK, formatter);
                        ngayTK = date.plusDays(1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));;
                        incomeAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }


        }
    }
}