package com.example.image.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.image.AdminMainActivity;
import com.example.image.R;
import com.example.image.model.Movie;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class UpdateDeleteActivity extends AppCompatActivity implements View.OnClickListener {
    private DatabaseReference root;
    private StorageReference reference;
    private YouTubePlayerView youTubePlayerView;
    private ImageView image;
    private TextView tvNgayPH, tvTheLoai;
    private EditText etTenPhim, etThoiLuong, etLinkYoutube,
    etYeuThich, etNoiDung, etDaoDien, etDienVien;
    private Spinner spKiemDuyet, spNgonNgu;
    private Button btCapNhap, btXoa, btCheckTrailer;
    private Movie movie;
    private View vBack;
    private YouTubePlayerTracker tracker;
    private MaterialCardView selectCard;
    boolean [] selectedTheLoai;
    private Uri imageUri;
    private String videoId = "";
    private ProgressBar progressBar;
    ArrayList<Integer> theLoaiList = new ArrayList<>();
    String [] theLoaiArray = {"Hành động", "Viễn tưởng", "Phiêu lưu", "Khoa học", "Nghệ thuật", "Chính kịch", "Hài kịch",
            "Học đường", "Tình cảm", "Thần thoại", "Anh hùng"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete);
        root = FirebaseDatabase.getInstance().getReference("movie");
        reference = FirebaseStorage.getInstance().getReference();
        initView();
        progressBar.setVisibility(View.INVISIBLE);
        btCapNhap.setOnClickListener(this);
        btXoa.setOnClickListener(this);
        Intent intent = getIntent();
        movie = (Movie) intent.getSerializableExtra("movie");
        vBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdateDeleteActivity.this, ListMovieActivity.class);
                startActivity(intent);
                finish();
            }
        });

        setVideo();

        Glide.with(UpdateDeleteActivity.this)
                .load(movie.getAnh())
                .into(image);

        tvNgayPH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(UpdateDeleteActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                        String date = "";
                        if(d<10)
                            date = "0" + d;
                        else
                            date = String.valueOf(d);
                        if(m>8){
                            date += "/" + (m+1) + "/" + y;
                        }else{
                            date += "/0" + (m+1) + "/" + y;
                        }
                        tvNgayPH.setText(date);
                    }
                }, year, month, day);
                dialog.show();
            }
        });
        spKiemDuyet.setAdapter(new ArrayAdapter<>(this, R.layout.item_spinner_update_delete,
                getResources().getStringArray(R.array.spKiemDuyet)));
        spNgonNgu.setAdapter(new ArrayAdapter<>(this, R.layout.item_spinner_update_delete,
                getResources().getStringArray(R.array.spNgonNgu)));

        etTenPhim.setText(movie.getTenP());
        tvNgayPH.setText(movie.getNgayPH());
        etThoiLuong.setText(movie.getThoiLuong()+"");
        etYeuThich.setText(movie.getYeuThich()+"");
        etNoiDung.setText(movie.getNoiDung());
        tvTheLoai.setText(movie.getTheLoai());
        etDaoDien.setText(movie.getDaoDien());
        etDienVien.setText(movie.getDienVien());

        int p = 0;
        for(int i = 0; i<spKiemDuyet.getCount(); i++){
            if(spKiemDuyet.getItemAtPosition(i).toString().equalsIgnoreCase(movie.getKiemDuyet())){
                p = i;
                break;
            }
        }
        spKiemDuyet.setSelection(p);

        p = 0;
        for(int i = 0; i<spNgonNgu.getCount(); i++){
            if(spNgonNgu.getItemAtPosition(i).toString().equalsIgnoreCase(movie.getNgonNgu())){
                p = i;
                break;
            }
        }
        spNgonNgu.setSelection(p);

        selectedTheLoai = new boolean[theLoaiArray.length];
        selectCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTheLoaiDialog();
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 2);
            }
        });

        btCheckTrailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newVideoId = etLinkYoutube.getText().toString();
                if (!newVideoId.isEmpty()) {
                    videoId = newVideoId;
                } else{
                    Toast.makeText(UpdateDeleteActivity.this, "Không để trống đường link video!", Toast.LENGTH_SHORT).show();
                    return;
                }
                youTubePlayerView.getYouTubePlayerWhenReady(new YouTubePlayerCallback() {
                    @Override
                    public void onYouTubePlayer(@NonNull YouTubePlayer youTubePlayer) {
                        int n = videoId.length();
                        youTubePlayer.cueVideo(videoId.substring(n-11, n), 0);
                    }
                });
            }
        });
    }

    private void initView() {
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        image = findViewById(R.id.image);
        etTenPhim = findViewById(R.id.etTenPhim);
        tvNgayPH = findViewById(R.id.tvNgayPH);
        progressBar = findViewById(R.id.progressBar);
        etThoiLuong = findViewById(R.id.etThoiLuong);
        etYeuThich = findViewById(R.id.etYeuThich);
        etNoiDung = findViewById(R.id.etNoiDung);
        selectCard = findViewById(R.id.selectCard);
        tvTheLoai = findViewById(R.id.tvTheLoai);
        etDaoDien = findViewById(R.id.etDaoDien);
        etDienVien = findViewById(R.id.etDienVien);
        spNgonNgu = findViewById(R.id.spNgonNgu);
        spKiemDuyet = findViewById(R.id.spKiemDuyet);
        btCapNhap = findViewById(R.id.btCapNhap);
        etLinkYoutube = findViewById(R.id.etLinkYoutube);
        btCheckTrailer = findViewById(R.id.btCheckTrailer);
        btXoa = findViewById(R.id.btXoa);
        vBack = findViewById(R.id.vBack);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            image.setImageURI(imageUri);
        }
    }

    private void showTheLoaiDialog(){
        String s = tvTheLoai.getText().toString();
        ArrayList<String> list = new ArrayList<>(Arrays.asList(s.split(", ")));
        boolean[] selectedTheLoai = new boolean[theLoaiArray.length];

        // Duyệt qua các phần tử trong danh sách, thiết lập giá trị mặc định cho các checkbox
        for (int i = 0; i < theLoaiArray.length; i++) {
            for (String l : list){
                if (l.contains(theLoaiArray[i])) {
                    selectedTheLoai[i] = true;
                }
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateDeleteActivity.this);

        builder.setTitle("Thể loại");
        builder.setCancelable(false);

        builder.setMultiChoiceItems(theLoaiArray, selectedTheLoai, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which, boolean isChecked) {
                if(isChecked){
                    theLoaiList.add(which);
                } else{
                    theLoaiList.remove(isChecked);
                }
            }
        })  .setPositiveButton("Chọn", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i<theLoaiList.size(); i++){
                    stringBuilder.append(theLoaiArray[theLoaiList.get(i)]);
                    if(i != theLoaiList.size() - 1){
                        stringBuilder.append(", ");
                    }
                    tvTheLoai.setText(stringBuilder.toString());
                }
            }
        }).setNegativeButton("Đóng", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
            }
        }).setNeutralButton("Xóa hết", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                for (int i = 0; i < selectedTheLoai.length; i++){
                    selectedTheLoai[i] = false;
                    theLoaiList.clear();
                    tvTheLoai.setText("");
                }
            }
        });
        builder.show();
    }

    private void setVideo() {
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

    private void SaveMovie(String url){
        String tenPhim = etTenPhim.getText().toString();
        String theLoai = tvTheLoai.getText().toString();
        String daoDien = etDaoDien.getText().toString();
        String dienVien = etDienVien.getText().toString();
        String ngayPH = tvNgayPH.getText().toString();
        String noiDung = etNoiDung.getText().toString();
        String ngonNgu = spNgonNgu.getSelectedItem().toString();
        String kiemDuyet = spKiemDuyet.getSelectedItem().toString();
        String trailer = etLinkYoutube.getText().toString();
        String yeuThich = etYeuThich.getText().toString();

        if(tenPhim.isEmpty() || theLoai.isEmpty() || daoDien.isEmpty()
                || dienVien.isEmpty() || ngayPH.isEmpty() || noiDung.isEmpty()){
            Toast.makeText(this, "Không để trống các trường!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(etThoiLuong.getText().toString().isEmpty()){
            Toast.makeText(this, "Không để trống thời lượng!", Toast.LENGTH_SHORT).show();
            return;
        }

        int thoiLuong = Integer.valueOf(etThoiLuong.getText().toString());

        if(thoiLuong == 0){
            Toast.makeText(this, "Hãy nhập thời lượng phim!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(ngonNgu.equals("Ngôn ngữ")){
            Toast.makeText(this, "Hãy chọn ngôn ngữ!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(kiemDuyet.equals("Kiểm duyệt")){
            Toast.makeText(this, "Hãy chọn kiểm duyệt!", Toast.LENGTH_SHORT).show();
            return;
        }


        String maP = movie.getMa();
        Map<String, Object> updates = new HashMap<>();
        updates.put("tenP", tenPhim);
        updates.put("theLoai", theLoai);
        updates.put("daoDien", daoDien);
        updates.put("dienVien", dienVien);
        updates.put("ngayPH", ngayPH);
        updates.put("thoiLuong", thoiLuong);
        updates.put("noiDung", noiDung);
        updates.put("ngonNgu", ngonNgu);
        updates.put("kiemDuyet", kiemDuyet);
        updates.put("yeuThich", yeuThich);
        updates.put("anh", url);
        if (!trailer.isEmpty()){
            int n = trailer.length();
            updates.put("trailer", trailer.substring(n-11, n));
        } else{
            updates.put("trailer", movie.getTrailer());
        }
        root.child(maP).updateChildren(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(UpdateDeleteActivity.this, "Sửa thành công!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UpdateDeleteActivity.this, ListMovieActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onClick(View view) {
        if (view == btCapNhap){
            if(imageUri == null){
                SaveMovie(movie.getAnh());
            }else{
                String key = movie.getMa().toString() + "_img";
                StorageReference fileRef = reference.child(key);
                fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                SaveMovie(uri.toString());
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                });
            }
        }
        if (view == btXoa){
            String maP = movie.getMa();
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("Thông báo xóa");
            builder.setMessage("Bạn chắc muốn xóa phim \"" + movie.getTenP() + "\" không?");
            builder.setIcon(R.drawable.icon_delete_24);
            builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    root.child(maP).removeValue();
                    Intent intent = new Intent(UpdateDeleteActivity.this, ListMovieActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

            builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}