package com.example.tariqi;

import android.app.AlertDialog;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.Settings;

import androidx.core.app.NotificationCompat;

public class AlertReceiver extends BroadcastReceiver {
    private NotificationHelper helper;
    @Override
    public void onReceive(Context context, Intent intent) {
        MediaPlayer mediaPlayer = MediaPlayer.create(context, Settings.System.DEFAULT_ALARM_ALERT_URI);
        mediaPlayer.start();
        helper = new NotificationHelper(context);
        setNotification("snooze" , "You snooze a trip" , context);

        /*AlertDialog.Builder dialog = new AlertDialog.Builder(context);
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
                setNotification("snooze" , "You snooze a trip" , context);
                mediaPlayer.stop();
            }
        });
        dialog.create().show();*/
    }

    private void setNotification(String title , String message ,Context context) {
        NotificationCompat.Builder builder = helper.getChannel(title,message,context);
        helper.getNotificationManager().notify(1,builder.build());
    }
}
