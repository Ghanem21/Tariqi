package com.example.tariqi;

import android.app.AlertDialog;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class AlertReceiver extends BroadcastReceiver {
    private NotificationHelper helper;
    @Override
    public void onReceive(Context context, Intent intent) {
        helper = new NotificationHelper(context);
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Alarm");
        dialog.setMessage("it's time of Trip");
        dialog.setIcon(R.drawable.alarm);
        dialog.setPositiveButton("Start", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setNeutralButton("Snooze", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setNotification("snooze" , "You snooze a trip");
            }
        });
        dialog.create().show();
    }

    private void setNotification(String title , String message) {
        NotificationCompat.Builder builder = helper.getChannel(title,message);
        helper.getNotificationManager().notify(1,builder.build());
    }
}
