package com.example.image.show;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.image.R;
import com.example.image.model.Movie;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class InforMovieActivity extends AppCompatActivity implements View.OnClickListener {
    private View vBack;
    private YouTubePlayerView youTubePlayerView;
    private ImageView image;
    private TextView tvTenPhim, tvNgayPH, tvThoiLuong, tvYeuThich,
        tvNoiDung, tvKiemDuyet, tvTheLoai, tvDaoDien, tvDienVien, tvNgonNgu;
    private Button btDatVe;
    private Movie movie;
    private YouTubePlayerTracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infor_movie);
        initView();
        vBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btDatVe.setOnClickListener(this);
        Intent intent = getIntent();
        movie = (Movie) intent.getSerializableExtra("movie");
        setData();
    }

    private void setData() {
        tvTenPhim.setText(movie.getTenP());
        tvNgayPH.setText(movie.getNgayPH());
        tvThoiLuong.setText(movie.getThoiLuong()+" phút");
        tvYeuThich.setText(movie.getYeuThich());
        tvNoiDung.setText(movie.getNoiDung());
        tvKiemDuyet.setText(movie.getKiemDuyet());
        tvTheLoai.setText(movie.getTheLoai());
        tvDaoDien.setText(movie.getDaoDien());
        tvDienVien.setText(movie.getDienVien());
        tvNgonNgu.setText(movie.getNgonNgu());
        Glide.with(InforMovieActivity.this)
                .load(movie.getAnh())
                .into(image);
        setVideo();
    }

    private void setVideo() {
//        youTubePlayerView.getYouTubePlayerWhenReady(new YouTubePlayerCallback() {
//            @Override
//            public void onYouTubePlayer(@NonNull YouTubePlayer youTubePlayer) {
//                youTubePlayer.cueVideo(movie.getTrailer(), 0);
//            }
//        });
        getLifecycle().addObserver(youTubePlayerView);
        tracker = new YouTubePlayerTracker();
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                youTubePlayer.cueVideo(movie.getTrailer(), 0);
                youTubePlayer.addListener(tracker);
            }
        });
    }

    private void initView() {
        vBack = findViewById(R.id.vBack);
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        image = findViewById(R.id.image);
        image = findViewById(R.id.image);
        tvTenPhim = findViewById(R.id.tvTenPhim);
        tvNgayPH = findViewById(R.id.tvNgayPH);
        tvThoiLuong = findViewById(R.id.tvThoiLuong);
        tvYeuThich = findViewById(R.id.tvYeuThich);
        tvNoiDung = findViewById(R.id.tvNoiDung);
        tvKiemDuyet = findViewById(R.id.tvKiemDuyet);
        tvTheLoai = findViewById(R.id.tvTheLoai);
        tvDaoDien = findViewById(R.id.tvDaoDien);
        tvDienVien = findViewById(R.id.tvDienVien);
        tvNgonNgu = findViewById(R.id.tvNgonNgu);
        btDatVe = findViewById(R.id.btDatVe);
    }

    @Override
    public void onClick(View view) {
        if(view == btDatVe){
            Intent intent = new Intent(InforMovieActivity.this, ShowDateTimeActivity.class);
            intent.putExtra("maP", movie.getMa());
            startActivity(intent);
        }
    }
}