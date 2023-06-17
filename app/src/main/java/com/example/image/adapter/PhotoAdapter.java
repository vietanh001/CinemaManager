package com.example.image.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.image.R;
import com.example.image.model.Movie;

import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {

    private Context context;
    private final List<Movie> mListPhoto;
    private ViewPager2 viewPager2;
    private ItemListener itemListener;


    public PhotoAdapter(Context context, List<Movie> mListPhoto, ViewPager2 viewPager2) {
        this.context = context;
        this.mListPhoto = mListPhoto;
        this.viewPager2 = viewPager2;
    }

    public void setItemListener(ItemListener itemListener) {
        this.itemListener = itemListener;
    }

    public PhotoAdapter(List<Movie> mListPhoto) {
        this.mListPhoto = mListPhoto;
    }

    public Movie getMovie(int position){
        return mListPhoto.get(position);
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_photo, parent,false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        Movie photo = mListPhoto.get(position);
        if(position == mListPhoto.size() - 2){
            viewPager2.post(runnable);
        }
        if(photo == null){
            return;
        }
        Glide.with(context).load(mListPhoto.get(position).getAnh()).into(holder.imgPhoto);
    }

    @Override
    public int getItemCount() {
        if(mListPhoto != null){
            return mListPhoto.size();
        }
        return 0;
    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView imgPhoto;

        public PhotoViewHolder(@NonNull View view) {
            super(view);
            imgPhoto = view.findViewById(R.id.img_photo);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(itemListener != null){
                itemListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            mListPhoto.addAll(mListPhoto);
            notifyDataSetChanged();
        }
    };

    public interface ItemListener{
        void onItemClick(View view, int position);
    }
}
