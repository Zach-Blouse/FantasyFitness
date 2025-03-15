package com.zblouse.fantasyfitness.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationRequest;
import android.os.Handler;
import android.os.Looper;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnSuccessListener;
import com.zblouse.fantasyfitness.core.EventListener;

import java.util.HashMap;

public class LocationDeviceService extends DeviceService{

    private final MainActivity mainActivity;
    private final FusedLocationProviderClient fusedLocationProviderClient;
    private final Handler handler;

    private boolean scanning;

    private final Runnable locationRequestRunnable = new Runnable() {
        @Override
        public void run() {
            if (scanning) {
                requestLocation();
            }
            handler.postDelayed(this, 1000);
        }
    };

    public LocationDeviceService(MainActivity mainActivity){
        super(DeviceServiceType.LOCATION);
        this.mainActivity = mainActivity;
        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mainActivity);
        this.handler = new Handler(Looper.myLooper());
    }

    public LocationDeviceService(MainActivity mainActivity, FusedLocationProviderClient fusedLocationProviderClient){
        super(DeviceServiceType.LOCATION);
        this.mainActivity = mainActivity;
        this.fusedLocationProviderClient = fusedLocationProviderClient;
        this.handler = new Handler(Looper.myLooper());
    }

    @Override
    public void subscribe(EventListener eventListener){
        super.subscribe(eventListener);
        if(!scanning){
            scanning = true;
            handler.post(locationRequestRunnable);
        }
    }

    @Override
    public void unsubscribe(EventListener eventListener){
        super.unsubscribe(eventListener);
        if(eventListeners.isEmpty()){
            scanning = false;
            handler.removeCallbacks(locationRequestRunnable);
        }
    }

    @SuppressLint("MissingPermission")
    private void requestLocation(){
        if(((PermissionDeviceService)mainActivity.getDeviceService(DeviceServiceType.PERMISSION)).hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null).addOnSuccessListener(mainActivity, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    //Check Accuracy
                    if(location.hasAccuracy()) {
                        if(location.getAccuracy() < 5.0) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                //send the location updates to the subscribers
                                LocationEvent locationEvent = new LocationEvent(location, new HashMap<>());
                                sendEvent(locationEvent);
                            }
                        }
                    }
                }
            });
        } else {
            ((PermissionDeviceService)mainActivity.getDeviceService(DeviceServiceType.PERMISSION)).requestPermission(Manifest.permission.ACCESS_FINE_LOCATION,5);
        }
    }
}