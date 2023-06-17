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

import com.example.image.AdminMainActivity;
import com.example.image.R;
import com.example.image.model.Movie;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.ArrayList;
import java.util.Calendar;

public class AddMovieActivity extends AppCompatActivity {

    private DatabaseReference root;
    private StorageReference reference;
    private EditText etMaPhim, etTenPhim, etDaoDien, etDienVien, etThoiLuong, etNoiDung, etLinkYoutube;
    private TextView tvTheLoai, tvNgayPH;
    private Spinner spKiemDuyet, spNgonNgu;
    private ImageView imageView;
    private YouTubePlayerView youTubePlayerView;
    private ProgressBar progressBarImage, progressBarVideo;
    private Button btAddMovie, btCheckTrailer;
    private View vBack;
    private Uri imageUri;
    private MaterialCardView selectCard;
    boolean [] selectedTheLoai;
    ArrayList<Integer> theLoaiList = new ArrayList<>();
    private String videoId;
    String [] theLoaiArray = {"Hành động", "Viễn tưởng", "Phiêu lưu", "Khoa học", "Nghệ thuật", "Chính kịch", "Hài kịch",
            "Học đường", "Tình cảm", "Anh hùng"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);
        initView();
        root = FirebaseDatabase.getInstance().getReference("movie");
        reference = FirebaseStorage.getInstance().getReference();

        selectedTheLoai = new boolean[theLoaiArray.length];
        selectCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTheLoaiDialog();
            }
        });

        tvNgayPH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(AddMovieActivity.this, new DatePickerDialog.OnDateSetListener() {
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

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 2);
            }
        });

        btAddMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageUri != null) {
                    AddMovie(imageUri);
                } else {
                    Toast.makeText(AddMovieActivity.this, "Hãy chọn ảnh tải lên!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        vBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddMovieActivity.this, AdminMainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btCheckTrailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newVideoId = etLinkYoutube.getText().toString();
                if (!newVideoId.isEmpty()) {
                    videoId = newVideoId;
                } else{
                    Toast.makeText(AddMovieActivity.this, "Không để trống đường link video!", Toast.LENGTH_SHORT).show();
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
        etMaPhim = findViewById(R.id.etMaPhim);
        etTenPhim = findViewById(R.id.etTenPhim);
        etDaoDien = findViewById(R.id.etDaoDien);
        etDienVien = findViewById(R.id.etDienVien);
        tvNgayPH = findViewById(R.id.tvNgayPH);
        etThoiLuong = findViewById(R.id.etThoiLuong);
        etNoiDung = findViewById(R.id.etNoiDung);
        etLinkYoutube = findViewById(R.id.etLinkYoutube);
        btCheckTrailer = findViewById(R.id.btCheckTrailer);
        selectCard = findViewById(R.id.selectCard);
        tvTheLoai = findViewById(R.id.tvTheLoai);
        spNgonNgu = findViewById(R.id.spNgonNgu);
        spKiemDuyet = findViewById(R.id.spKiemDuyet);
        imageView = findViewById(R.id.imageView);
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        vBack = findViewById(R.id.vBack);
        progressBarImage = findViewById(R.id.progressBarImage);
        progressBarVideo = findViewById(R.id.progressBarVideo);
        btAddMovie = findViewById(R.id.btAddMovie);
        spKiemDuyet.setAdapter(new ArrayAdapter<>(this, R.layout.item_spinner,
                getResources().getStringArray(R.array.spKiemDuyet)));
        spNgonNgu.setAdapter(new ArrayAdapter<>(this, R.layout.item_spinner,
                getResources().getStringArray(R.array.spNgonNgu)));
    }


    private void showTheLoaiDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(AddMovieActivity.this);

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
        }).setPositiveButton("Chọn", new DialogInterface.OnClickListener() {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }

    private void CheckValidate(){

    }


    private void AddMovie(Uri uriImage) {
//        Calendar c=Calendar.getInstance();
//        SimpleDateFormat curDate=new SimpleDateFormat("dd-MM-yyyy");
//        saveCurDate=curDate.format(c.getTime());
//        SimpleDateFormat curTime=new SimpleDateFormat("HH:mm:ss");
//        saveCurTime=curTime.format(c.getTime());
//        randomKey=saveCurDate+"-"+saveCurTime;
        String maPhim = etMaPhim.getText().toString();
        String tenPhim = etTenPhim.getText().toString();
        String theLoai = tvTheLoai.getText().toString();
        String daoDien = etDaoDien.getText().toString();
        String dienVien = etDienVien.getText().toString();
        String ngayPH = tvNgayPH.getText().toString();

        String noiDung = etNoiDung.getText().toString();
        String ngonNgu = spNgonNgu.getSelectedItem().toString();
        String kiemDuyet = spKiemDuyet.getSelectedItem().toString();

        String linkYoutube = etLinkYoutube.getText().toString();



        if(maPhim.isEmpty() || tenPhim.isEmpty() || theLoai.isEmpty() || daoDien.isEmpty()
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

        if(linkYoutube.isEmpty()){
            Toast.makeText(this, "Hãy nhập link youtube!", Toast.LENGTH_SHORT).show();
            return;
        }

        int n = videoId.length();
        String video = videoId.substring(n-11, n);

        root.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Movie movie = dataSnapshot.getValue(Movie.class);
                    if(movie.getMa().equals(maPhim)){
                        Toast.makeText(AddMovieActivity.this, "Trùng mã phim trong Firebase!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                String key = etMaPhim.getText().toString() + "_img";
                StorageReference fileRef = reference.child(key);
                fileRef.putFile(uriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String anh = uri.toString();
                                Movie movie = new Movie(maPhim, tenPhim, theLoai, daoDien, dienVien,
                                        ngayPH, thoiLuong, noiDung, ngonNgu, kiemDuyet, anh, video);
                                root.child(maPhim).setValue(movie);
                                progressBarImage.setVisibility(View.INVISIBLE);
                                Toast.makeText(AddMovieActivity.this, "Thêm phim thành công!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AddMovieActivity.this, ListMovieActivity.class);
                                startActivity(intent);
                            }
                        });
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        progressBarImage.setVisibility(View.VISIBLE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBarImage.setVisibility(View.VISIBLE);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}