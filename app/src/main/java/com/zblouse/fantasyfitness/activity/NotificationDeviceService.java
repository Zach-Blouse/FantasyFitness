package com.zblouse.fantasyfitness.activity;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Random;

public class NotificationDeviceService extends DeviceService {

    private final MainActivity mainActivity;

    private static final String CHANNEL_ID = "notificationDeviceServiceChannel";

    public NotificationDeviceService(MainActivity mainActivity) {
        super(DeviceServiceType.NOTIFICATION);
        this.mainActivity = mainActivity;
        createNotificationChannel();
        if (!((PermissionDeviceService) mainActivity.getDeviceService(DeviceServiceType.PERMISSION)).hasPermission(Manifest.permission.POST_NOTIFICATIONS)) {
            ((PermissionDeviceService) mainActivity.getDeviceService(DeviceServiceType.PERMISSION)).requestPermission(Manifest.permission.POST_NOTIFICATIONS, 5);
        }
    }

    private void createNotificationChannel() {
        NotificationChannel serviceChannel = new NotificationChannel(
                CHANNEL_ID,
                "Location Service Channel",
                NotificationManager.IMPORTANCE_HIGH
        );
        NotificationManager notificationManager = mainActivity.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(serviceChannel);

    }

    public void sendNotification(String title, String description, int imageId){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mainActivity, CHANNEL_ID)
                .setSmallIcon(imageId)
                .setContentTitle(title)
                .setContentText(description)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        boolean notificationGranted = ActivityCompat.checkSelfPermission(mainActivity, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED;
        if(!notificationGranted){
            ActivityCompat.requestPermissions(mainActivity, new String[]{
                    Manifest.permission.POST_NOTIFICATIONS}, 5);
        }else {
            Random random = new Random();
            NotificationManagerCompat manager = NotificationManagerCompat.from(mainActivity);
            manager.notify(random.nextInt(), builder.build());
        }
    }
}
