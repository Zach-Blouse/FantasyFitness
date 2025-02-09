package com.zblouse.fantasyfitness.activity;

import android.Manifest;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

public class PermissionDeviceService extends DeviceService{

    private final MainActivity mainActivity;

    public PermissionDeviceService(MainActivity mainActivity){
        super(DeviceServiceType.PERMISSION);
        this.mainActivity = mainActivity;
    }

    public boolean hasPermission(String permission){
        return ActivityCompat.checkSelfPermission(mainActivity, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermission(String permission, int requestCode){
        ActivityCompat.requestPermissions(mainActivity, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION}, requestCode);
    }
}
