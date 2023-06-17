package com.example.image.adapter.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.image.R;
import com.example.image.model.Movie;

import java.util.ArrayList;

public class EditMovieAdapter extends RecyclerView.Adapter<EditMovieAdapter.EditMovieHolder> {

    private Context context;
    private ArrayList<Movie> mList;
    private ItemListener itemListener;

    public EditMovieAdapter(Context context, ArrayList<Movie> mList) {
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
    public EditMovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.edit_movie_layout, parent, false);
        return new EditMovieHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EditMovieHolder holder, int position) {
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

    public class EditMovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView imageView;
        private TextView tvYeuThich, tvTenPhim, tvTheLoai;

        public EditMovieHolder(@NonNull View view) {
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