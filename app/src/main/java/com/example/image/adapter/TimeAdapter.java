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

import java.util.ArrayList;

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.TimeHolder> {

    private Context context;
    private ArrayList<String> mList;
    private DateAdapter.ItemListener itemListener;
    private int selectedPosition = -1;


    public TimeAdapter(Context context, ArrayList<String> mList) {
        this.context = context;
        this.mList = mList;
    }

    public void setmList(ArrayList<String> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    public String getTime(int position){
        return mList.get(position);
    }

    public void setItemListener(DateAdapter.ItemListener itemListener){
        this.itemListener = itemListener;
    }

    @NonNull
    @Override
    public TimeAdapter.TimeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_time_view, parent, false);
        return new TimeAdapter.TimeHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull TimeAdapter.TimeHolder holder, int position) {
        String time = mList.get(position);
        holder.tvGioChieu.setText(time);
        if (position == selectedPosition) {
            holder.tvGioChieu.setTextColor(Color.parseColor("#FF0000"));
        } else {
            holder.tvGioChieu.setTextColor(Color.parseColor("#FFFFFF"));
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public class TimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView tvGioChieu;

        public TimeHolder(@NonNull View view) {
            super(view);
            tvGioChieu = view.findViewById(R.id.tvGioChieu);
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

