package com.example.image.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.image.R;
import com.example.image.adapter.TicketHistoryAdapter;
import com.example.image.model.Ticket;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class FragmentTicketHistory extends Fragment implements TicketHistoryAdapter.ItemListener{

    private RecyclerView rvVeDaDat;
    private TicketHistoryAdapter ticketHistoryAdapter;
    private ArrayList<Ticket> listTicket;
    private TextView tvTongTra;
    private long total = 0;
    private DatabaseReference root_ticket = FirebaseDatabase.getInstance().getReference("ticket");
    private FirebaseUser auth = FirebaseAuth.getInstance().getCurrentUser();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ticket_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        rvVeDaDat = view.findViewById(R.id.rvVeDaDat);
        tvTongTra = view.findViewById(R.id.tvTongTra);
        String email = auth.getEmail();
        email = email.substring(0, email.length() - 4) + "-com";
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rvVeDaDat.setLayoutManager(manager);
        listTicket = new ArrayList<>();
        ticketHistoryAdapter = new TicketHistoryAdapter(getContext(), listTicket);
        rvVeDaDat.setAdapter(ticketHistoryAdapter);
        root_ticket.child(email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Ticket ticket = dataSnapshot.getValue(Ticket.class);
                    total += Long.parseLong(ticket.getTongTien().replace(".", "").trim());
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
                DecimalFormat decimalFormat = new DecimalFormat("#,###");
                tvTongTra.setText(decimalFormat.format(total) + " VNĐ");
                ticketHistoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {

    }
}
