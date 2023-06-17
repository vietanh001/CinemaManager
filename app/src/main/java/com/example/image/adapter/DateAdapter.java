package com.example.image.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.image.R;
import com.example.image.model.Date;

import java.util.ArrayList;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.DateHolder> {

    private Context context;
    private ArrayList<Date> mList;
    private ItemListener itemListener;
    private int selectedPosition = -1;


    public DateAdapter(Context context, ArrayList<Date> mList) {
        this.context = context;
        this.mList = mList;
    }

    public void setmList(ArrayList<Date> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    public Date getDate(int position){
        return mList.get(position);
    }

    public void setItemListener(ItemListener itemListener){
        this.itemListener = itemListener;
    }

    @NonNull
    @Override
    public DateHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_date_view, parent, false);
        return new DateAdapter.DateHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DateHolder holder, int position) {
        Date date = mList.get(position);
        holder.tvThu.setText(date.getThu());
        holder.tvNgay.setText(date.getNgay());
        if (position == selectedPosition) {
            holder.tvThu.setTextColor(Color.parseColor("#FF0000"));
            holder.tvNgay.setTextColor(Color.parseColor("#FF0000"));
        } else {
            holder.tvThu.setTextColor(Color.parseColor("#FFFFFF"));
            holder.tvNgay.setTextColor(Color.parseColor("#FFFFFF"));
        }
        if (selectedPosition == -1 && position == 0) {
            selectedPosition = 0;
            holder.tvThu.setTextColor(Color.parseColor("#FF0000"));
            holder.tvNgay.setTextColor(Color.parseColor("#FF0000"));
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public class DateHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView tvThu, tvNgay;

        public DateHolder(@NonNull View view) {
            super(view);
            tvThu = view.findViewById(R.id.tvThu);
            tvNgay = view.findViewById(R.id.tvNgay);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(itemListener != null){
                int previousSelectedPosition = selectedPosition;
                selectedPosition = getAdapterPosition();
                notifyItemChanged(previousSelectedPosition);
                notifyItemChanged(selectedPosition);
                itemListener.onItemClick(view, getAdapterPosition());
            }
        }
    }
    public interface ItemListener{
        void onItemClick(View view, int position);
    }
}
