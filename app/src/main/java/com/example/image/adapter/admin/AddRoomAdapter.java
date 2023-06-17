package com.example.image.adapter.admin;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.image.R;

import java.util.ArrayList;

public class AddRoomAdapter extends RecyclerView.Adapter<AddRoomAdapter.SlotHolder> {

    private Context context;
    private ArrayList<String> mList;
    private ArrayList<Integer> selectedPositions;
    private ItemListener itemListener;
    private int selectedPosition = -1;

    public AddRoomAdapter(Context context, ArrayList<String> mList) {
        this.context = context;
        this.mList = mList;
        this.selectedPositions = new ArrayList<>();
    }

    public void setmList(ArrayList<String> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    public String getSlot(int position){
        return mList.get(position);
    }

    public void setItemListener(ItemListener itemListener){
        this.itemListener = itemListener;
    }

    @NonNull
    @Override
    public SlotHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_slot, parent, false);
        return new AddRoomAdapter.SlotHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SlotHolder holder, int position) {
        String slot = mList.get(position);
        holder.ivSlot.setBackgroundColor(Color.WHITE);
        if(!slot.equals("0") && !slot.equals("1")){
            holder.ivSlot.setText(slot);
            holder.ivSlot.setTextSize(16);
            holder.ivSlot.setTextColor(Color.parseColor("#FFFFFF"));
            holder.ivSlot.setBackgroundColor(ContextCompat.getColor(context, R.color.colorBackground));
        }

        if(slot.equals("0") || slot.equals("1")){
            if(selectedPositions.contains(position)){
                mList.set(position, "1");
                holder.ivSlot.setBackgroundColor(ContextCompat.getColor(context, R.color.orange));
            } else {
                mList.set(position, "0");
                holder.ivSlot.setBackgroundColor(Color.WHITE);
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedPositions.contains(position)){
                    selectedPositions.remove(Integer.valueOf(position));
                } else {
                    selectedPositions.add(position);
                }
                notifyDataSetChanged();
            }
        });
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class SlotHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView ivSlot;

        public SlotHolder(@NonNull View view) {
            super(view);
            ivSlot = view.findViewById(R.id.ivSlot);
            ivSlot.setBackgroundResource(android.R.color.white);
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
