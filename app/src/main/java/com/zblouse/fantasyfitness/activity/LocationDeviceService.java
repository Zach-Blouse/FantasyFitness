package com.zblouse.fantasyfitness.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.zblouse.fantasyfitness.core.EventListener;

import java.util.HashMap;

public class LocationDeviceService extends DeviceService{

    private final MainActivity mainActivity;
    private final FusedLocationProviderClient fusedLocationProviderClient;
    private final Handler handler;

    private boolean scanning;
    private final LocationRequest locationRequest = new LocationRequest.Builder(500).setPriority(Priority.PRIORITY_HIGH_ACCURACY).build();

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            //Check Accuracy
            if(location.hasAccuracy()) {
                if(location.getAccuracy() < 6.0) {
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        //send the location updates to the subscribers
                        LocationEvent locationEvent = new LocationEvent(location, new HashMap<>());
                        sendEvent(locationEvent);
                    }
                }
            }
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
            requestLocationUpdates();
        }
    }

    @Override
    public void unsubscribe(EventListener eventListener){
        super.unsubscribe(eventListener);
        if(eventListeners.isEmpty()){
            scanning = false;
            stopLocationUpdates();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestLocationUpdates(){
        if(((PermissionDeviceService)mainActivity.getDeviceService(DeviceServiceType.PERMISSION)).hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest,
                locationListener,
                Looper.getMainLooper());
        } else {
            ((PermissionDeviceService)mainActivity.getDeviceService(DeviceServiceType.PERMISSION)).requestPermission(Manifest.permission.ACCESS_FINE_LOCATION,5);
        }
    }

    private void stopLocationUpdates(){
        Log.e("LOCATION DEVICE SERVICE", "Stopping Location Updates");
        fusedLocationProviderClient.removeLocationUpdates(locationListener);
    }
}