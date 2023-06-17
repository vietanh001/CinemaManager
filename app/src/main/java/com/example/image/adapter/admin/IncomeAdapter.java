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
import com.example.image.model.Income;
import com.example.image.model.Ticket;

import java.util.ArrayList;

public class IncomeAdapter extends RecyclerView.Adapter<IncomeAdapter.IncomeHolder>{

    private Context context;
    private ArrayList<Income> mList;
    private DateAdapter.ItemListener itemListener;

    public IncomeAdapter(Context context, ArrayList<Income> mList) {
        this.context = context;
        this.mList = mList;
    }

    public void setmList(ArrayList<Income> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    public Income getIncome(int position){
        return mList.get(position);
    }

    public void setItemListener(DateAdapter.ItemListener itemListener){
        this.itemListener = itemListener;
    }

    @NonNull
    @Override
    public IncomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_income, parent, false);
        return new IncomeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IncomeHolder holder, int position) {
        Income income = mList.get(position);
        holder.tvNgay.setText("Ngày " + income.getNgay());
        holder.tvSoVeBan.setText(income.getSoVeBan() + " vé");
        holder.tvDoanhThu.setText(income.getDoanhThu() + " VNĐ");
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class IncomeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView tvNgay, tvSoVeBan, tvDoanhThu;

        public IncomeHolder(@NonNull View view) {
            super(view);
            tvNgay = view.findViewById(R.id.tvNgay);
            tvSoVeBan = view.findViewById(R.id.tvSoVeBan);
            tvDoanhThu = view.findViewById(R.id.tvDoanhThu);
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
