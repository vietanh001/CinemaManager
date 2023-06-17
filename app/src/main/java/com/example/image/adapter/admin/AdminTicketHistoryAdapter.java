package com.example.image.adapter.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.image.R;
import com.example.image.adapter.DateAdapter;
import com.example.image.model.Ticket;

import java.util.ArrayList;

public class AdminTicketHistoryAdapter extends RecyclerView.Adapter<AdminTicketHistoryAdapter.TicketHistoryHolder>{

    private Context context;
    private ArrayList<Ticket> mList;
    private DateAdapter.ItemListener itemListener;

    public AdminTicketHistoryAdapter(Context context, ArrayList<Ticket> mList) {
        this.context = context;
        this.mList = mList;
    }

    public void setmList(ArrayList<Ticket> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    public Ticket getTicket(int position){
        return mList.get(position);
    }

    public void setItemListener(DateAdapter.ItemListener itemListener){
        this.itemListener = itemListener;
    }

    @NonNull
    @Override
    public TicketHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ticket_history_admin, parent, false);
        return new TicketHistoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketHistoryHolder holder, int position) {
        Ticket ticket = mList.get(position);
        holder.tvEmail.setText(ticket.getUsername());
        holder.tvTenKH.setText(ticket.getHoTen());
        holder.tvTenPhim.setText(ticket.getTenPhim());
        holder.tvDinhDang.setText(ticket.getDinhDang());
        holder.tvNgayDat.setText(ticket.getNgayDat());
        holder.tvGioDat.setText(ticket.getGioDat());
        holder.tvDiaDiem.setText(ticket.getDiaDiem());
        holder.tvNgayChieu.setText(ticket.getNgayChieu());
        holder.tvGioChieu.setText(ticket.getGioChieu());
        holder.tvPhongChieu.setText(ticket.getPhongChieu());
        holder.tvGhe.setText(ticket.getGhe());
        holder.tvTongTien.setText(ticket.getTongTien() + " VNĐ");
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class TicketHistoryHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView tvTenPhim, tvDinhDang, tvNgayDat, tvGioDat,
                tvDiaDiem, tvNgayChieu, tvGioChieu, tvPhongChieu, tvGhe, tvTongTien, tvEmail, tvTenKH;

        public TicketHistoryHolder(@NonNull View view) {
            super(view);
            tvEmail = view.findViewById(R.id.tvEmail);
            tvTenKH = view.findViewById(R.id.tvTenKH);
            tvTenPhim = view.findViewById(R.id.tvTenPhim);
            tvDinhDang = view.findViewById(R.id.tvDinhDang);
            tvNgayDat = view.findViewById(R.id.tvNgayDat);
            tvGioDat = view.findViewById(R.id.tvGioDat);
            tvDiaDiem = view.findViewById(R.id.tvDiaDiem);
            tvNgayChieu = view.findViewById(R.id.tvNgayChieu);
            tvGioChieu = view.findViewById(R.id.tvGioChieu);
            tvPhongChieu = view.findViewById(R.id.tvPhongChieu);
            tvGhe = view.findViewById(R.id.tvGhe);
            tvTongTien = view.findViewById(R.id.tvTongTien);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(itemListener != null){
                itemListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    public interface ItemListener{
        void onItemClick(View view, int position);
    }
}
