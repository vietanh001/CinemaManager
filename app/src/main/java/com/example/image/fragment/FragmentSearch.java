package com.example.image.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.image.R;
import com.example.image.adapter.admin.EditMovieAdapter;
import com.example.image.model.Movie;
import com.example.image.show.InforMovieActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FragmentSearch extends Fragment implements EditMovieAdapter.ItemListener{

    private RecyclerView recyclerView;
    private ArrayList<Movie> list;
    private EditMovieAdapter adapter;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("movie");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView);
        Bundle bundle = getArguments();
        recyclerView.setHasFixedSize(true);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(manager);
        list = new ArrayList<>();
        adapter = new EditMovieAdapter(getContext(), list);
        recyclerView.setAdapter(adapter);
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String searchQuery = "";
                if (bundle != null)
                    searchQuery = bundle.getString("key");
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Movie movie = dataSnapshot.getValue(Movie.class);
                    if(movie.getTenP().toLowerCase().contains(searchQuery.toLowerCase()))
                        list.add(movie);
                }
                adapter.setmList(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        adapter.setItemListener(this);
    }

    @Override
    public void onItemClick(View view, int position) {
        Movie movie = adapter.getMovie(position);
        Intent intent = new Intent(getContext(), InforMovieActivity.class);
        intent.putExtra("movie", movie);
        startActivity(intent);
    }
}