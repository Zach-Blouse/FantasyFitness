package com.zblouse.fantasyfitness.workout;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.Manifest;
import android.location.Location;
import android.os.Handler;

import com.zblouse.fantasyfitness.activity.DeviceServiceType;
import com.zblouse.fantasyfitness.activity.LocationDeviceService;
import com.zblouse.fantasyfitness.activity.LocationEvent;
import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.activity.PermissionDeviceService;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.HashMap;

public class WorkoutServiceTest {

    @Test
    public void startTest(){
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        TimeTracker mockTimeTracker = Mockito.mock(TimeTracker.class);
        DistanceTracker mockDistanceTracker = Mockito.mock(DistanceTracker.class);
        Handler mockHandler = Mockito.mock(Handler.class);
        PermissionDeviceService mockPermissionDeviceService = Mockito.mock(PermissionDeviceService.class);
        when(mockActivity.getDeviceService(eq(DeviceServiceType.PERMISSION))).thenReturn(mockPermissionDeviceService);
        when(mockPermissionDeviceService.hasPermission(eq(Manifest.permission.ACCESS_FINE_LOCATION))).thenReturn(true);
        LocationDeviceService mockLocationDeviceService = Mockito.mock(LocationDeviceService.class);
        when(mockActivity.getDeviceService(eq(DeviceServiceType.LOCATION))).thenReturn(mockLocationDeviceService);
        WorkoutService workoutService = new WorkoutService(mockActivity, mockHandler, mockTimeTracker, mockDistanceTracker);
        workoutService.startWorkout();
        verify(mockTimeTracker).start();
        verify(mockHandler).post(any());
        verify(mockDistanceTracker).start();
        verify(mockLocationDeviceService).subscribe(workoutService);
    }

    @Test
    public void pauseTest(){
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        TimeTracker mockTimeTracker = Mockito.mock(TimeTracker.class);
        DistanceTracker mockDistanceTracker = Mockito.mock(DistanceTracker.class);
        Handler mockHandler = Mockito.mock(Handler.class);
        WorkoutService workoutService = new WorkoutService(mockActivity, mockHandler, mockTimeTracker, mockDistanceTracker);
        when(mockTimeTracker.pause()).thenReturn(3456L);
        workoutService.pause();
        verify(mockTimeTracker).pause();
        verify(mockDistanceTracker).pause();
        ArgumentCaptor<WorkoutTimeUpdateEvent> timeUpdateEventArgumentCaptor = ArgumentCaptor.forClass(WorkoutTimeUpdateEvent.class);
        verify(mockActivity).publishEvent((WorkoutTimeUpdateEvent)timeUpdateEventArgumentCaptor.capture());
        assert(timeUpdateEventArgumentCaptor.getValue().getTime() == 3456L);
    }

    @Test
    public void unpauseTest(){
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        TimeTracker mockTimeTracker = Mockito.mock(TimeTracker.class);
        DistanceTracker mockDistanceTracker = Mockito.mock(DistanceTracker.class);
        Handler mockHandler = Mockito.mock(Handler.class);
        WorkoutService workoutService = new WorkoutService(mockActivity, mockHandler, mockTimeTracker, mockDistanceTracker);
        workoutService.unpause();
        verify(mockTimeTracker).unpause();
        verify(mockDistanceTracker).unpause();
    }

    @Test
    public void stopTest(){
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        TimeTracker mockTimeTracker = Mockito.mock(TimeTracker.class);
        DistanceTracker mockDistanceTracker = Mockito.mock(DistanceTracker.class);
        when(mockTimeTracker.stop()).thenReturn(3456L);
        Handler mockHandler = Mockito.mock(Handler.class);
        WorkoutService workoutService = new WorkoutService(mockActivity, mockHandler, mockTimeTracker, mockDistanceTracker);
        workoutService.stopWorkout();
        verify(mockTimeTracker).stop();
        verify(mockDistanceTracker).stop();
        verify(mockHandler).removeCallbacks(any());
        ArgumentCaptor<WorkoutTimeUpdateEvent> timeUpdateEventArgumentCaptor = ArgumentCaptor.forClass(WorkoutTimeUpdateEvent.class);
        verify(mockActivity).publishEvent((WorkoutTimeUpdateEvent)timeUpdateEventArgumentCaptor.capture());
        assert(timeUpdateEventArgumentCaptor.getValue().getTime() == 3456L);
    }

    @Test
    public void publishLocationEventTest(){
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        TimeTracker mockTimeTracker = Mockito.mock(TimeTracker.class);
        DistanceTracker mockDistanceTracker = Mockito.mock(DistanceTracker.class);
        when(mockDistanceTracker.update(any())).thenReturn(57.5);
        Handler mockHandler = Mockito.mock(Handler.class);
        WorkoutService workoutService = new WorkoutService(mockActivity, mockHandler, mockTimeTracker, mockDistanceTracker);
        workoutService.unpause();
        Location mockLocation = Mockito.mock(Location.class);
        LocationEvent locationEvent = new LocationEvent(mockLocation, new HashMap<>());
        workoutService.publishEvent(locationEvent);
        ArgumentCaptor<WorkoutDistanceUpdateEvent> distanceUpdateEventArgumentCaptor = ArgumentCaptor.forClass(WorkoutDistanceUpdateEvent.class);
        verify(mockActivity).publishEvent((WorkoutDistanceUpdateEvent)distanceUpdateEventArgumentCaptor.capture());
        assert(distanceUpdateEventArgumentCaptor.getValue().getDistanceMeters() == 57.5);

    }
}
