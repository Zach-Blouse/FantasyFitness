package com.zblouse.fantasyfitness.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.util.Log;

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
        Log.e("PERMISSION_DEVICE_SERVICE", "Requesting permission: " + permission);
        ActivityCompat.requestPermissions(mainActivity, new String[]{
                permission}, requestCode);
    }
}
