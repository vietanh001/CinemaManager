package com.example.image.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;


import com.example.image.AdminMainActivity;
import com.example.image.R;
import com.example.image.adapter.admin.EditMovieAdapter;
import com.example.image.model.Movie;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class ListMovieActivity extends AppCompatActivity implements EditMovieAdapter.ItemListener{

    private RecyclerView recyclerView;
    private ArrayList<Movie> list;
    private TextView tvNhan;
    private View vBack;
    private EditMovieAdapter adapter;
    private SearchView searchView;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("movie");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_movie);
        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.searchView);
        tvNhan = findViewById(R.id.tvNhan);
        vBack = findViewById(R.id.vBack);
        recyclerView.setHasFixedSize(true);


        GridLayoutManager manager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(manager);
        list = new ArrayList<>();
        adapter = new EditMovieAdapter(ListMovieActivity.this, list);
        recyclerView.setAdapter(adapter);
        vBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListMovieActivity.this, AdminMainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Movie movie = dataSnapshot.getValue(Movie.class);
                    list.add(movie);
                }
                adapter.setmList(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        adapter.setItemListener(this);

        tvNhan.setVisibility(View.VISIBLE);
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    tvNhan.setVisibility(View.GONE);
                } else {
                    tvNhan.setVisibility(View.VISIBLE);
                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                firebaseSearch(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                firebaseSearch(s);
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void firebaseSearch(String searchText){
        list = new ArrayList<>();
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Movie movie = dataSnapshot.getValue(Movie.class);
                    if(movie.getTenP().toLowerCase(Locale.getDefault()).contains(searchText.toLowerCase(Locale.getDefault())))
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
        Intent intent = new Intent(this, UpdateDeleteActivity.class);
        intent.putExtra("movie", movie);
        startActivity(intent);
    }
}