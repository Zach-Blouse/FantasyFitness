package com.zblouse.fantasyfitness.workout;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.zblouse.fantasyfitness.R;
import com.zblouse.fantasyfitness.activity.DeviceServiceType;
import com.zblouse.fantasyfitness.activity.LocationDeviceService;
import com.zblouse.fantasyfitness.activity.LocationForegroundDeviceService;
import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.activity.PermissionDeviceService;
import com.zblouse.fantasyfitness.user.UserService;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.HashMap;

@RunWith(RobolectricTestRunner.class)
public class WorkoutFragmentTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);

    @Test
    public void onCreateTest(){
        UserService mockUserService = Mockito.mock(UserService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        WorkoutFragment testedFragment = new WorkoutFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        assertNotNull(returnedView.findViewById(R.id.workout_time));
        assertNotNull(returnedView.findViewById(R.id.start_workout_button));
        assertNotNull(returnedView.findViewById(R.id.workout_distance));
        assertNull(returnedView.findViewById(R.id.pause_workout_button));
        assertNull(returnedView.findViewById(R.id.stop_workout_button));
    }

    @Test
    public void publishTimeUpdateEventTest(){
        UserService mockUserService = Mockito.mock(UserService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        WorkoutFragment testedFragment = new WorkoutFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);
        testedFragment.publishEvent(new WorkoutUpdateEvent(1000, 0, new HashMap<>()));

        assertEquals("00:01",((TextView)returnedView.findViewById(R.id.workout_time)).getText());

    }

    @Test
    public void publishDistanceUpdateEventTest(){
        UserService mockUserService = Mockito.mock(UserService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        WorkoutFragment testedFragment = new WorkoutFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);
        testedFragment.publishEvent(new WorkoutUpdateEvent(0, 1230, new HashMap<>()));

        assertEquals("1.23 km",((TextView)returnedView.findViewById(R.id.workout_distance)).getText());

    }

    @Test
    public void startWorkoutTest(){
        UserService mockUserService = Mockito.mock(UserService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        WorkoutService mockWorkoutService= Mockito.mock(WorkoutService.class);
        when(mockWorkoutService.startWorkout()).thenReturn(true);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        mainActivity.setWorkoutService(mockWorkoutService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        WorkoutFragment testedFragment = new WorkoutFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);
        returnedView.findViewById(R.id.start_workout_button).performClick();
        verify(mockWorkoutService).startWorkout();
        assertNotNull(returnedView.findViewById(R.id.workout_time));
        assertNull(returnedView.findViewById(R.id.start_workout_button));
        assertNotNull(returnedView.findViewById(R.id.workout_distance));
        assertNotNull(returnedView.findViewById(R.id.pause_workout_button));
        assertNotNull(returnedView.findViewById(R.id.stop_workout_button));
    }

    @Test
    public void startWorkoutNoPermissionsTest(){
        UserService mockUserService = Mockito.mock(UserService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        WorkoutService mockWorkoutService= Mockito.mock(WorkoutService.class);
        when(mockWorkoutService.startWorkout()).thenReturn(false);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        mainActivity.setWorkoutService(mockWorkoutService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        WorkoutFragment testedFragment = new WorkoutFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);
        returnedView.findViewById(R.id.start_workout_button).performClick();
        verify(mockWorkoutService).startWorkout();
        assertNotNull(returnedView.findViewById(R.id.workout_time));
        assertNotNull(returnedView.findViewById(R.id.start_workout_button));
        assertNotNull(returnedView.findViewById(R.id.workout_distance));
        assertNull(returnedView.findViewById(R.id.pause_workout_button));
        assertNull(returnedView.findViewById(R.id.stop_workout_button));
    }

    @Test
    public void pauseUnpauseTest(){
        UserService mockUserService = Mockito.mock(UserService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        WorkoutService mockWorkoutService= Mockito.mock(WorkoutService.class);
        when(mockWorkoutService.startWorkout()).thenReturn(true);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        mainActivity.setWorkoutService(mockWorkoutService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        WorkoutFragment testedFragment = new WorkoutFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);
        returnedView.findViewById(R.id.start_workout_button).performClick();
        assertEquals("Pause Workout",((TextView)returnedView.findViewById(R.id.pause_workout_button)).getText());
        returnedView.findViewById(R.id.pause_workout_button).performClick();
        verify(mockWorkoutService).pause();
        assertEquals("Resume Workout",((TextView)returnedView.findViewById(R.id.pause_workout_button)).getText());
        returnedView.findViewById(R.id.pause_workout_button).performClick();
        verify(mockWorkoutService).unpause();
        assertEquals("Pause Workout",((TextView)returnedView.findViewById(R.id.pause_workout_button)).getText());
    }

    @Test
    public void stopWorkoutCompleteTest(){
        UserService mockUserService = Mockito.mock(UserService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        WorkoutService mockWorkoutService= Mockito.mock(WorkoutService.class);
        when(mockWorkoutService.startWorkout()).thenReturn(true);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        mainActivity.setWorkoutService(mockWorkoutService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        WorkoutFragment testedFragment = new WorkoutFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);
        returnedView.findViewById(R.id.start_workout_button).performClick();
        returnedView.findViewById(R.id.stop_workout_button).performClick();
        verify(mockWorkoutService).pause();
        assertEquals("Resume Workout",((TextView)returnedView.findViewById(R.id.pause_workout_button)).getText());
        assertEquals("Complete Workout",((TextView)returnedView.findViewById(R.id.stop_workout_button)).getText());
        returnedView.findViewById(R.id.stop_workout_button).performClick();
        verify(mockWorkoutService).stopWorkout();
        assertNotNull(returnedView.findViewById(R.id.workout_time));
        assertNotNull(returnedView.findViewById(R.id.start_workout_button));
        assertNotNull(returnedView.findViewById(R.id.workout_distance));
        assertNull(returnedView.findViewById(R.id.pause_workout_button));
        assertNull(returnedView.findViewById(R.id.stop_workout_button));

    }

    @Test
    public void stopWorkoutResumeTest(){
        UserService mockUserService = Mockito.mock(UserService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        WorkoutService mockWorkoutService= Mockito.mock(WorkoutService.class);
        when(mockWorkoutService.startWorkout()).thenReturn(true);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        mainActivity.setWorkoutService(mockWorkoutService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        WorkoutFragment testedFragment = new WorkoutFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);
        returnedView.findViewById(R.id.start_workout_button).performClick();
        returnedView.findViewById(R.id.stop_workout_button).performClick();
        verify(mockWorkoutService).pause();
        assertEquals("Resume Workout",((TextView)returnedView.findViewById(R.id.pause_workout_button)).getText());
        assertEquals("Complete Workout",((TextView)returnedView.findViewById(R.id.stop_workout_button)).getText());
        returnedView.findViewById(R.id.pause_workout_button).performClick();
        verify(mockWorkoutService).unpause();
        assertEquals("Pause Workout",((TextView)returnedView.findViewById(R.id.pause_workout_button)).getText());
        assertEquals("Stop Workout",((TextView)returnedView.findViewById(R.id.stop_workout_button)).getText());

    }

    @Test
    public void workoutTimeAndDistanceNotificationSentTest(){
        UserService mockUserService = Mockito.mock(UserService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        LocationForegroundDeviceService mockLocationForegroundDeviceService = Mockito.mock(LocationForegroundDeviceService.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setDeviceService(DeviceServiceType.LOCATION_FOREGROUND,mockLocationForegroundDeviceService);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        WorkoutFragment testedFragment = new WorkoutFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);
        testedFragment.publishEvent(new WorkoutUpdateEvent(1000, 1500, new HashMap<>()));

        verify(mockLocationForegroundDeviceService).updateLocationForegroundServiceNotification(eq("00:01 1.50 km"));

    }

    @Test
    public void saveBundleTest(){
        UserService mockUserService = Mockito.mock(UserService.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        TimeTracker mockTimeTracker = Mockito.mock(TimeTracker.class);
        DistanceTracker mockDistanceTracker = Mockito.mock(DistanceTracker.class);
        long time = 3456L;
        double distance = 1500.0;
        when(mockTimeTracker.getTotalTimeMillis()).thenReturn(time);
        when(mockDistanceTracker.getTotalDistanceMeters()).thenReturn(distance);
        Handler mockHandler = Mockito.mock(Handler.class);
        PermissionDeviceService mockPermissionDeviceService = Mockito.mock(PermissionDeviceService.class);
        when(mockPermissionDeviceService.hasPermission(eq(Manifest.permission.ACCESS_FINE_LOCATION))).thenReturn(true);
        mainActivity.setDeviceService(DeviceServiceType.PERMISSION, mockPermissionDeviceService);

        WorkoutService workoutService = new WorkoutService(mainActivity, mockHandler, mockTimeTracker, mockDistanceTracker);
        workoutService.startWorkout();

        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        mainActivity.setWorkoutService(workoutService);

        WorkoutFragment testedFragment = new WorkoutFragment(mainActivity);
        Bundle bundle = new Bundle();
        testedFragment.onSaveInstanceState(bundle);

        assertEquals(time, bundle.getLong("workoutTime"));
        assertEquals(distance, bundle.getDouble("workoutDistance"),0);
        assert(bundle.getBoolean("workoutInProgress"));
    }

    @Test
    public void loadBundleTest(){

        UserService mockUserService = Mockito.mock(UserService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);

        WorkoutFragment testedFragment = new WorkoutFragment(mainActivity);

        TimeTracker mockTimeTracker = Mockito.mock(TimeTracker.class);
        DistanceTracker mockDistanceTracker = Mockito.mock(DistanceTracker.class);
        long time = 3456L;
        double distance = 1500.0;

        Handler mockHandler = Mockito.mock(Handler.class);
        PermissionDeviceService mockPermissionDeviceService = Mockito.mock(PermissionDeviceService.class);
        when(mockPermissionDeviceService.hasPermission(eq(Manifest.permission.ACCESS_FINE_LOCATION))).thenReturn(true);
        mainActivity.setDeviceService(DeviceServiceType.PERMISSION, mockPermissionDeviceService);

        WorkoutService workoutService = new WorkoutService(mainActivity, mockHandler, mockTimeTracker, mockDistanceTracker);

        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        mainActivity.setWorkoutService(workoutService);


        Bundle bundle = new Bundle();
        bundle.putDouble("workoutDistance", distance);
        bundle.putLong("workoutTime", time);
        bundle.putBoolean("workoutInProgress", true);
        testedFragment.onCreateView(layoutInflater, null, bundle);

        verify(mockTimeTracker).setTotalTimeMillis(eq(time));
        verify(mockDistanceTracker).setTotalDistanceMeters(eq(distance));

    }
}
