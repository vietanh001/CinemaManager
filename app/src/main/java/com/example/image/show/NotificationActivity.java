package com.example.image.show;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.image.MainActivity;
import com.example.image.R;

public class NotificationActivity extends BroadcastReceiver {

    final String CHANNEL_ID = "101";

    @SuppressLint("NotificationPermission")
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getSerializableExtra("myActicon") != null &&
        intent.getSerializableExtra("myAction").equals("NotificationTicket") &&
        intent.getSerializableExtra("tenPhim") != null &&
        intent.getSerializableExtra("diaDiem") != null &&
        intent.getSerializableExtra("ngayChieu") != null &&
        intent.getSerializableExtra("gioChieu") != null){
            Log.e("Rev","rev");
            NotificationManager manager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel1 = new NotificationChannel(
                        CHANNEL_ID,
                        "Channel 1",
                        NotificationManager.IMPORTANCE_HIGH
                );
                channel1.setDescription("This is Channel 1");
                manager.createNotificationChannel(channel1);
            }
            String tieuDe = "Đặt vé phim thành công!";
            String noiDung = "Phim: " + String.valueOf(intent.getSerializableExtra("tenPhim")) + "\n" +
                    "Địa điểm: " + String.valueOf(intent.getSerializableExtra("diaDiem")) + "\n" +
                    "Ngày chiếu: " + String.valueOf(intent.getSerializableExtra("ngayChieu")) + "\n" +
                    "Giờ chiếu: " + String.valueOf(intent.getSerializableExtra("gioChieu"));
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.icon_ticket_24)
                    .setContentTitle(tieuDe)
                    .setContentText(noiDung)
                    .setDefaults(NotificationCompat.DEFAULT_SOUND)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setCategory(NotificationCompat.CATEGORY_ALARM)
                    .setColor(Color.RED)
                    .setAutoCancel(true);
            Intent i = new Intent(context, ShowSlotActivity.class);
            PendingIntent pendingIntent =
                    PendingIntent.getActivity(
                            context,
                            0, i,
                            PendingIntent.FLAG_ONE_SHOT
                    );
            builder.setContentIntent(pendingIntent);
            manager.notify(12345, builder.build());
        }
    }
}
