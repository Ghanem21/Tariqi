package com.example.tariqi;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Settings;

public class Dialog extends AppCompatActivity {
    private NotificationHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), Settings.System.DEFAULT_NOTIFICATION_URI);
        mediaPlayer.start();

        AlertDialog.Builder dialog = new AlertDialog.Builder(getApplicationContext());
        dialog.setTitle("Alarm");
        dialog.setMessage("it's time of Trip");
        dialog.setIcon(R.drawable.alarm);
        dialog.setPositiveButton("Start", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mediaPlayer.stop();
            }
        });
        dialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mediaPlayer.stop();
                dialog.dismiss();
            }
        });
        dialog.setNeutralButton("Snooze", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                helper = new NotificationHelper(getApplicationContext());
                setNotification("snooze" , "You snooze a trip" , getApplicationContext());
                mediaPlayer.stop();
            }
        });
        dialog.create().show();
    }
    private void setNotification(String title , String message , Context context) {
        NotificationCompat.Builder builder = helper.getChannel(title,message,context);
        helper.getNotificationManager().notify(1,builder.build());
    }
}