package com.example.image.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.image.AdminMainActivity;
import com.example.image.R;
import com.example.image.adapter.TicketHistoryAdapter;
import com.example.image.adapter.admin.AdminTicketHistoryAdapter;
import com.example.image.model.Ticket;
import com.example.image.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class ListTicketActivity extends AppCompatActivity implements AdminTicketHistoryAdapter.ItemListener, View.OnClickListener {
    private RecyclerView rvVeDaDat;
    private AdminTicketHistoryAdapter adminTicketHistoryAdapter;
    private ArrayList<Ticket> listTicket;
    private View vBack;
    private DatabaseReference root_ticket = FirebaseDatabase.getInstance().getReference("ticket");
    private DatabaseReference root_user = FirebaseDatabase.getInstance().getReference("user");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_ticket);
        initView();

        vBack.setOnClickListener(this);

        LinearLayoutManager manager = new LinearLayoutManager(ListTicketActivity.this, RecyclerView.VERTICAL, false);
        rvVeDaDat.setLayoutManager(manager);
        listTicket = new ArrayList<>();
        adminTicketHistoryAdapter = new AdminTicketHistoryAdapter(ListTicketActivity.this, listTicket);
        rvVeDaDat.setAdapter(adminTicketHistoryAdapter);

        root_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    String email = user.getUsername().toString().replace(".", "-");
                    ShowTicket(email);
                }
                adminTicketHistoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void ShowTicket(String email){
        root_ticket.child(email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Ticket ticket = dataSnapshot.getValue(Ticket.class);
                    listTicket.add(ticket);
                }
                Collections.sort(listTicket, (ticket1, ticket2) -> {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    Date d1 = null, d2 = null;
                    LocalTime t1 = null, t2 = null;
                    try {
                        d1 = sdf.parse(ticket1.getNgayDat());
                        d2 = sdf.parse(ticket2.getNgayDat());
                        t1 = LocalTime.parse(ticket1.getGioDat());
                        t2 = LocalTime.parse(ticket2.getGioDat());
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    if(d1.compareTo(d2) < 0)
                        return 1;
                    else if(d1.compareTo(d2) == 0 && t1.isBefore(t2))
                        return 1;
                    return -1;
                });
                adminTicketHistoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initView() {
        vBack = findViewById(R.id.vBack);
        rvVeDaDat = findViewById(R.id.rvVeDaDat);
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onClick(View view) {
        if(view == vBack){
            Intent intent = new Intent(ListTicketActivity.this, AdminMainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}