package com.zblouse.fantasyfitness.activity;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

import android.Manifest;
import android.location.Location;
import android.os.Looper;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.zblouse.fantasyfitness.R;
import com.zblouse.fantasyfitness.core.EventListener;
import com.zblouse.fantasyfitness.home.UserHomeFragment;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class LocationDeviceServiceTest {

    @Test
    public void subscribeTest() throws InterruptedException {
        MainActivity mockMainActivity = Mockito.mock(MainActivity.class);
        PermissionDeviceService mockPermissionDeviceService = Mockito.mock(PermissionDeviceService.class);
        when(mockMainActivity.getDeviceService(eq(DeviceServiceType.PERMISSION))).thenReturn(mockPermissionDeviceService);
        when(mockPermissionDeviceService.hasPermission(eq(Manifest.permission.ACCESS_FINE_LOCATION))).thenReturn(true);
        FusedLocationProviderClient mockFusedLocationProviderClient = Mockito.mock(FusedLocationProviderClient.class);
        Task mockTask = Mockito.mock(Task.class);
        when(mockFusedLocationProviderClient.getCurrentLocation(eq(Priority.PRIORITY_HIGH_ACCURACY), eq(null))).thenReturn(mockTask);
        EventListener mockEventListener = Mockito.mock(EventListener.class);
        LocationDeviceService testedDeviceService = new LocationDeviceService(mockMainActivity, mockFusedLocationProviderClient);
        testedDeviceService.subscribe(mockEventListener);
        shadowOf(Looper.getMainLooper()).idle();
        Thread.sleep(2000);
        ArgumentCaptor<OnSuccessListener> onSuccessArgumentCaptor = ArgumentCaptor.forClass(OnSuccessListener.class);
        verify(mockTask).addOnSuccessListener(eq(mockMainActivity), onSuccessArgumentCaptor.capture());
        Location mockLocation = Mockito.mock(Location.class);
        onSuccessArgumentCaptor.getValue().onSuccess(mockLocation);
        ArgumentCaptor<LocationEvent> locationEventArgumentCaptor = ArgumentCaptor.forClass(LocationEvent.class);
        verify(mockEventListener).publishEvent(locationEventArgumentCaptor.capture());
        assertNotNull(locationEventArgumentCaptor.getValue());
    }

    @Test
    public void subscribeNoPermissionsTest() throws InterruptedException {
        MainActivity mockMainActivity = Mockito.mock(MainActivity.class);
        PermissionDeviceService mockPermissionDeviceService = Mockito.mock(PermissionDeviceService.class);
        FusedLocationProviderClient mockFusedLocationProviderClient = Mockito.mock(FusedLocationProviderClient.class);
        when(mockMainActivity.getDeviceService(eq(DeviceServiceType.PERMISSION))).thenReturn(mockPermissionDeviceService);
        when(mockPermissionDeviceService.hasPermission(eq(Manifest.permission.ACCESS_FINE_LOCATION))).thenReturn(false);
        EventListener mockEventListener = Mockito.mock(EventListener.class);
        LocationDeviceService testedDeviceService = new LocationDeviceService(mockMainActivity, mockFusedLocationProviderClient);
        testedDeviceService.subscribe(mockEventListener);
        shadowOf(Looper.getMainLooper()).idle();
        Thread.sleep(2000);
        verify(mockPermissionDeviceService).requestPermission(eq(Manifest.permission.ACCESS_FINE_LOCATION),anyInt());
    }
}
