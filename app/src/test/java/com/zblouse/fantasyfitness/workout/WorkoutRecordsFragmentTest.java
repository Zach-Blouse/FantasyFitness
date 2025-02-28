package com.zblouse.fantasyfitness.workout;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.zblouse.fantasyfitness.R;
import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.user.UserService;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.HashMap;

@RunWith(RobolectricTestRunner.class)
public class WorkoutRecordsFragmentTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS);

    @Test
    public void onCreateTest() {
        UserService mockUserService = Mockito.mock(UserService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        WorkoutRecordService mockWorkoutRecordService = Mockito.mock(WorkoutRecordService.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        mainActivity.setWorkoutRecordService(mockWorkoutRecordService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        WorkoutRecordsFragment testedFragment = new WorkoutRecordsFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        assertNotNull(returnedView.findViewById(R.id.workout_records_title));
        assertNotNull(returnedView.findViewById(R.id.workout_records_display));
    }

    @Test
    public void publishEventWorkoutRecordReadTest() {
        UserService mockUserService = Mockito.mock(UserService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        WorkoutRecordService mockWorkoutRecordService = Mockito.mock(WorkoutRecordService.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        mainActivity.setWorkoutRecordService(mockWorkoutRecordService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        WorkoutRecordsFragment testedFragment = new WorkoutRecordsFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        assertNotNull(returnedView.findViewById(R.id.workout_records_title));
        assertNotNull(returnedView.findViewById(R.id.workout_records_display));

        WorkoutRecord testRecord = new WorkoutRecord("12356",1000L,2000L,3000L,4000L,5000L);
        WorkoutRecordReadEvent workoutRecordReadEvent = new WorkoutRecordReadEvent(testRecord, new HashMap<>());

        testedFragment.publishEvent(workoutRecordReadEvent);
        assertEquals("1 Mile Record 00:01\n5km Record 00:02\n10km Record 00:03\n25km Record 00:04\nMarathon Record 00:05",((TextView)returnedView.findViewById(R.id.workout_records_display)).getText());
    }

    @Test
    public void publishEventWorkoutRecordReadNoRecordsSetTest() {
        UserService mockUserService = Mockito.mock(UserService.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        WorkoutRecordService mockWorkoutRecordService = Mockito.mock(WorkoutRecordService.class);
        mainActivity.setFirebaseAuth(mockAuth);
        mainActivity.setUserService(mockUserService);
        mainActivity.setWorkoutRecordService(mockWorkoutRecordService);
        when(mockAuth.getCurrentUser()).thenReturn(mockUser);
        LayoutInflater layoutInflater = LayoutInflater.from(mainActivity);
        Bundle mockBundle = Mockito.mock(Bundle.class);
        WorkoutRecordsFragment testedFragment = new WorkoutRecordsFragment(mainActivity);
        View returnedView = testedFragment.onCreateView(layoutInflater, null, mockBundle);

        assertNotNull(returnedView.findViewById(R.id.workout_records_title));
        assertNotNull(returnedView.findViewById(R.id.workout_records_display));

        WorkoutRecord testRecord = new WorkoutRecord("12356",Long.MAX_VALUE,Long.MAX_VALUE,Long.MAX_VALUE,Long.MAX_VALUE,Long.MAX_VALUE);
        WorkoutRecordReadEvent workoutRecordReadEvent = new WorkoutRecordReadEvent(testRecord, new HashMap<>());

        testedFragment.publishEvent(workoutRecordReadEvent);
        assertEquals("1 Mile Record No Record\n5km Record No Record\n10km Record No Record\n25km Record No Record\nMarathon Record No Record",((TextView)returnedView.findViewById(R.id.workout_records_display)).getText());
    }
}