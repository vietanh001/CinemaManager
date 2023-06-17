package com.example.image.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.image.show.InforMovieActivity;
import com.example.image.R;
import com.example.image.adapter.PhotoAdapter;
import com.example.image.adapter.ShowAllAdapter;
import com.example.image.model.Movie;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FragmentHome extends Fragment implements ShowAllAdapter.ItemListener, PhotoAdapter.ItemListener {

    private RecyclerView recyclerView;
    private ArrayList<Movie> list;
    private ShowAllAdapter adapter;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("movie");
    private PhotoAdapter photoAdapter;
    private ViewPager2 viewPager2;
    private List<Movie> mListPhoto;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            int currentPosition = viewPager2.getCurrentItem();
            if (currentPosition == mListPhoto.size() - 1){
                viewPager2.setCurrentItem(0);
            } else {
                viewPager2.setCurrentItem(currentPosition + 1);
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.recyclerView);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(manager);
        list = new ArrayList<>();
        adapter = new ShowAllAdapter(getContext(), list);
        recyclerView.setAdapter(adapter);
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Movie movie = dataSnapshot.getValue(Movie.class);
                    list.add(movie);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        viewPager2 = view.findViewById(R.id.viewPager2);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        // Hiệu ứng chuyển đổi
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });
        viewPager2.setPageTransformer(compositePageTransformer);
        mListPhoto = new ArrayList<>();
        photoAdapter = new PhotoAdapter(getContext(), mListPhoto, viewPager2);
        viewPager2.setAdapter(photoAdapter);
        photoAdapter.setItemListener(new PhotoAdapter.ItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                Movie movie = photoAdapter.getMovie(position);
                Intent intent = new Intent(getActivity(), InforMovieActivity.class);
                intent.putExtra("movie", movie);
                startActivity(intent);
            }
        });

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Movie movie = dataSnapshot.getValue(Movie.class);
                    if(Float.valueOf(movie.getYeuThich()) - 9.0 >= 0.0 )
                        mListPhoto.add(movie);
                }
                photoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position){
                super.onPageSelected(position);
                mHandler.removeCallbacks(mRunnable);
                mHandler.postDelayed(mRunnable, 2000);
            }
        });
        adapter.setItemListener(new ShowAllAdapter.ItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                Movie movie = adapter.getMovie(position);
                Intent intent = new Intent(getActivity(), InforMovieActivity.class);
                intent.putExtra("movie", movie);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        mHandler.postDelayed(mRunnable, 2000);
    }

    @Override
    public void onItemClick(View view, int position) {

    }
}