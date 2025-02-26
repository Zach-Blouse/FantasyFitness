package com.zblouse.fantasyfitness.activity;


import android.Manifest;
import android.content.Intent;

public class LocationForegroundDeviceService extends DeviceService {

    public static final String NOTIFICATION_TEXT = "notificationText";
    private final MainActivity mainActivity;
    private boolean running;

    public LocationForegroundDeviceService(MainActivity mainActivity){
        super(DeviceServiceType.LOCATION_FOREGROUND);
        this.mainActivity = mainActivity;
    }

    public void startLocationForegroundService(){
        Intent startLocationForegroundService = new Intent(mainActivity, LocationForegroundService.class);

        startLocationForegroundService.putExtra(NOTIFICATION_TEXT,"00:00 0.00km");
        mainActivity.startService(startLocationForegroundService);
        running = true;
    }

    public void stopLocationForegroundService(){
        Intent stopLocationForegroundService = new Intent(mainActivity, LocationForegroundService.class);
        mainActivity.stopService(stopLocationForegroundService);
        running = false;
    }

    public void updateLocationForegroundServiceNotification(String notificationText){
        Intent startLocationForegroundService = new Intent(mainActivity, LocationForegroundService.class);
        startLocationForegroundService.putExtra(NOTIFICATION_TEXT,notificationText);
        mainActivity.startService(startLocationForegroundService);
        running = true;
    }
}