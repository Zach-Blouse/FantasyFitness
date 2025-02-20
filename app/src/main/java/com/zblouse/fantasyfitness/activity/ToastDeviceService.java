package com.zblouse.fantasyfitness.activity;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;

public class ToastDeviceService extends DeviceService {

    private final MainActivity mainActivity;

    public ToastDeviceService(MainActivity mainActivity){
        super(DeviceServiceType.TOAST);
        this.mainActivity = mainActivity;
    }

    public void sendToast(String toastString){
        Toast.makeText(mainActivity,toastString,Toast.LENGTH_SHORT).show();
    }
}
