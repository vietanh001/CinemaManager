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

public class FormAdapter extends RecyclerView.Adapter<FormAdapter.FormHolder>{
    private Context context;
    private ArrayList<String> mList;
    private ItemListener itemListener;
    private int selectedPosition = -1;

    public FormAdapter(Context context, ArrayList<String> mList) {
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

    public void setItemListener(FormAdapter.ItemListener itemListener){
        this.itemListener = itemListener;
    }

    @NonNull
    @Override
    public FormHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_form_view, parent, false);
        return new FormAdapter.FormHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FormHolder holder, int position) {
        String form = mList.get(position);
        holder.tvLoai.setText(form);
        if (position == selectedPosition) {
            holder.tvLoai.setTextColor(Color.parseColor("#FF0000"));
        } else {
            holder.tvLoai.setTextColor(Color.parseColor("#FFFFFF"));
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class FormHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView tvLoai;

        public FormHolder(@NonNull View view) {
            super(view);
            tvLoai = view.findViewById(R.id.tvLoai);
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
