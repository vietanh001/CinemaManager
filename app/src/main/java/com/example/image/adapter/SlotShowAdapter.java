package com.example.image.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.image.R;

import java.util.ArrayList;

public class SlotShowAdapter extends RecyclerView.Adapter<SlotShowAdapter.SlotHolder>{

    private Context context;
    private ArrayList<String> mList;
    private ArrayList<Integer> selectedPositions;
    private ItemListener itemListener;

    public SlotShowAdapter(Context context, ArrayList<String> mList) {
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
        return new SlotHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SlotHolder holder, int position) {
        String slot = mList.get(position);

        if (slot.matches("[A-Z]+")) {
            holder.ivSlot.setText(slot);
            holder.ivSlot.setTextSize(16);
            holder.ivSlot.setTextColor(Color.parseColor("#FFFFFF"));
            holder.ivSlot.setBackgroundColor(ContextCompat.getColor(context, R.color.colorBackground));
            holder.ivSlot.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }

        if(slot.equals("0")){
            holder.ivSlot.setBackgroundColor(Color.WHITE);
            holder.ivSlot.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }

        if(slot.equals("1")){
            holder.ivSlot.setBackgroundColor(ContextCompat.getColor(context, R.color.orange));
            holder.ivSlot.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }

        if(slot.equals("2")){
            holder.ivSlot.setBackgroundColor(Color.WHITE);
            Drawable drawable = holder.itemView.getContext().getDrawable(R.drawable.icon_done_24);
            holder.ivSlot.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        }

        if(slot.equals("3")){
            holder.ivSlot.setBackgroundColor(ContextCompat.getColor(context, R.color.orange));
            Drawable drawable = holder.itemView.getContext().getDrawable(R.drawable.icon_done_24);
            holder.ivSlot.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        }

        if(slot.equals("4") || slot.equals("5")){
            holder.ivSlot.setBackgroundColor(ContextCompat.getColor(context, R.color.gray));
            holder.ivSlot.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }

        if(!slot.matches("[A-Z]+")){
            if(selectedPositions.contains(position)){
                Drawable drawable = holder.itemView.getContext().getDrawable(R.drawable.icon_done_24);
                holder.ivSlot.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
            } else{
                holder.ivSlot.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String slot1 = mList.get(position);
                if(!slot1.equals("4") && !slot1.equals("5") && !slot1.matches("[A-Z]+")){
                    if(itemListener != null){
                        itemListener.onItemClick(v, position);
                    }
                    if(selectedPositions.contains(position)){
                        if(slot1.equals("2")){
                            mList.set(position, "0");
                        }
                        if(slot1.equals("3")){
                            mList.set(position, "1");
                        }
                        selectedPositions.remove(Integer.valueOf(position));
                    } else {
                        if(slot1.equals("0")){
                            mList.set(position, "2");
                        }
                        if(slot1.equals("1")){
                            mList.set(position, "3");
                        }
                        selectedPositions.add(position);
                    }
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
