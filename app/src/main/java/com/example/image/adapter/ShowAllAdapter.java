package com.example.image.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.image.R;
import com.example.image.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class ShowAllAdapter extends RecyclerView.Adapter<ShowAllAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Movie> mList;
    private ItemListener itemListener;

    public ShowAllAdapter(Context context, ArrayList<Movie> mList) {
        this.context = context;
        this.mList = mList;
    }

    public void setmList(ArrayList<Movie> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    public Movie getMovie(int position){
        return mList.get(position);
    }


    public void setItemListener(ItemListener itemListener) {
        this.itemListener = itemListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_movie_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Movie movie = mList.get(position);
        Glide.with(context)
                .load(mList.get(position).getAnh())
                .into(holder.imageView);
        holder.tvTenPhim.setText(movie.getTenP());
        holder.tvTheLoai.setText(movie.getTheLoai());
        holder.tvYeuThich.setText(String.valueOf(movie.getYeuThich()));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView imageView;
        private TextView tvYeuThich, tvTenPhim, tvTheLoai;

        public MyViewHolder(@NonNull View view) {
            super(view);
            imageView = view.findViewById(R.id.image);
            tvYeuThich = view.findViewById(R.id.tvYeuThich);
            tvTenPhim = view.findViewById(R.id.tvTenPhim);
            tvTheLoai = view.findViewById(R.id.tvTheLoai);
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