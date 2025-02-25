package com.zblouse.fantasyfitness.activity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.zblouse.fantasyfitness.R;

public class LocationForegroundService extends Service {

    private static final String CHANNEL_ID = "LOCATION_FOREGROUND_DEVICE_SERVICE";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        createNotificationChannel();
        String notificationText = intent.getStringExtra(LocationForegroundDeviceService.NOTIFICATION_TEXT);
        Log.e("LOCATIONFOREGROUNDSERVICE", "notification: " + notificationText);
        startForeground(1, getNotification(notificationText));
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        NotificationChannel serviceChannel = new NotificationChannel(
                CHANNEL_ID,
                "Location Service Channel",
                NotificationManager.IMPORTANCE_LOW
        );
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(serviceChannel);
        notificationManager.getNotificationChannel(CHANNEL_ID).setSound(null, null);

    }

    private Notification getNotification(String notificationText) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent,
                PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder builder = new
                NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Fantasy Fitness")
                .setContentText(notificationText)
                .setSmallIcon(R.drawable.run)
                .setContentIntent(pendingIntent)
                .setOngoing(true);

        builder.setForegroundServiceBehavior(Notification.FOREGROUND_SERVICE_IMMEDIATE);
        return builder.build();
    }
}

