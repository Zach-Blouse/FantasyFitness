package com.zblouse.fantasyfitness.workout;

import static android.app.Activity.RESULT_OK;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.Manifest;
import android.app.Instrumentation;
import android.content.Intent;
import android.location.Location;
import android.os.Looper;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.zblouse.fantasyfitness.activity.DeviceServiceType;
import com.zblouse.fantasyfitness.activity.LocationDeviceService;
import com.zblouse.fantasyfitness.activity.LocationEvent;
import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.R;
import com.zblouse.fantasyfitness.activity.PermissionDeviceService;
import com.zblouse.fantasyfitness.core.EventListener;
import com.zblouse.fantasyfitness.user.UserFirestoreDatabase;
import com.zblouse.fantasyfitness.user.UserRepository;
import com.zblouse.fantasyfitness.user.UserService;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class WorkoutDistanceTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Rule
    public IntentsTestRule<MainActivity> intentsTestRule =
            new IntentsTestRule<>(MainActivity.class);

    @Test
    public void workoutDistanceTest() throws InterruptedException {

        //THIS TEST SETUP IS NEEDED TO AUTHENTICATE WITH THE APPLICATION
        com.firebase.ui.auth.data.model.User user = new User.Builder("google", "test@test.com")
                .setName("Test user")
                .build();
        IdpResponse response = new IdpResponse.Builder(user).build();
        FirebaseUser mockFirebaseUser = Mockito.mock(FirebaseUser.class);
        when(mockFirebaseUser.getUid()).thenReturn("testUserId");

        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        when(mockAuth.getCurrentUser()).thenReturn(mockFirebaseUser);
        UserService userService = new UserService();
        PermissionDeviceService mockPermissionDeviceService = Mockito.mock(PermissionDeviceService.class);
        when(mockPermissionDeviceService.hasPermission(any())).thenReturn(true);
        if (Looper.myLooper() == null) {
            Looper.prepare();
        }
        WorkoutService workoutService = new WorkoutService();

        LocationDeviceService mockLocationDeviceService = Mockito.mock(LocationDeviceService.class);

        activityRule.getScenario().onActivity(activity -> {

            activity.setFirebaseAuth(mockAuth);
            userService.setMainActivity(activity);
            workoutService.setMainActivity(activity);
            activity.setUserService(userService);
            activity.setWorkoutService(workoutService);
            activity.setDeviceService(DeviceServiceType.PERMISSION,mockPermissionDeviceService);
            activity.setDeviceService(DeviceServiceType.LOCATION,mockLocationDeviceService);
        });

        //Set up mock firestore
        FirebaseFirestore mockFirestore = Mockito.mock(FirebaseFirestore.class);

        CollectionReference mockCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockFirestore.collection(eq("users"))).thenReturn(mockCollectionReference);
        DocumentReference mockDocument = Mockito.mock(DocumentReference.class);
        when(mockCollectionReference.document(eq("testUserId"))).thenReturn(mockDocument);

        Task<DocumentSnapshot> mockReadTask = Mockito.mock(Task.class);
        when(mockDocument.get()).thenReturn(mockReadTask);
        ArgumentCaptor<OnCompleteListener<DocumentSnapshot>> onCompleteListenerArgumentCaptorRead = ArgumentCaptor.forClass(OnCompleteListener.class);
        DocumentSnapshot mockDocumentSnapshot = Mockito.mock(DocumentSnapshot.class);

        when(mockReadTask.isSuccessful()).thenReturn(true);
        when(mockReadTask.getResult()).thenReturn(mockDocumentSnapshot);
        when(mockDocumentSnapshot.exists()).thenReturn(true);
        when(mockDocumentSnapshot.get("UID")).thenReturn("testUid");
        when(mockDocumentSnapshot.get("USERNAME")).thenReturn("testUsername");

        UserFirestoreDatabase userFirestoreDatabase = new UserFirestoreDatabase(mockFirestore);
        UserRepository userRepository = new UserRepository(userService, userFirestoreDatabase);
        userService.setUserRepository(userRepository);

        Instrumentation.ActivityResult firebaseResult = new Instrumentation.ActivityResult(RESULT_OK, response.toIntent());
        intending(hasComponent("com.firebase.ui.auth.KickoffActivity")).respondWith(firebaseResult);

        onView(withId(R.id.app_title)).check(matches(withText("Fantasy Fitness")));
        onView(withId(R.id.login_button)).perform(click());
        if (Looper.myLooper() == null) {
            Looper.prepare();
        }
        verify(mockReadTask).addOnCompleteListener(onCompleteListenerArgumentCaptorRead.capture());
        onCompleteListenerArgumentCaptorRead.getValue().onComplete(mockReadTask);
        onView(withId(R.id.user_home_title)).check(matches(withText("Fantasy Fitness")));

        //FINISH AUTHENTICATION, TEST IS AT USER HOME
        //FOLLOWING THIS LINE IS THE BEGINNING OF THE ACTUAL TEST THIS TEST IS SUPPOSED TO BE TESTING

        onView(withId(R.id.action_workout)).perform(click());
        onView(withId(R.id.workout_distance)).check(matches(withText("0.00 km")));
        onView(withId(R.id.start_workout_button)).perform(click());
        verify(mockPermissionDeviceService).hasPermission(eq(Manifest.permission.ACCESS_FINE_LOCATION));
        Location location1 = new Location("provider");
        location1.setLatitude(10.10);
        location1.setLongitude(10.10);
        Location location2 = new Location("provider");
        location2.setLatitude(10.11);
        location2.setLongitude(10.11);
        Location location3 = new Location("provider");
        location3.setLatitude(10.12);
        location3.setLongitude(10.15);
        LocationEvent firstLocationEvent = new LocationEvent(location1);
        LocationEvent secondLocationEvent = new LocationEvent(location2);
        LocationEvent thirdLocationEvent = new LocationEvent(location3);
        workoutService.publishEvent(firstLocationEvent);
        onView(withId(R.id.workout_distance)).check(matches(withText("0.00 km")));
        workoutService.publishEvent(secondLocationEvent);
        onView(withId(R.id.workout_distance)).check(matches(withText("1.56 km")));
        workoutService.publishEvent(thirdLocationEvent);
        onView(withId(R.id.workout_distance)).check(matches(withText("6.08 km")));
    }
    @Test
    public void workoutDistanceWithPauseTest() throws InterruptedException {

        //THIS TEST SETUP IS NEEDED TO AUTHENTICATE WITH THE APPLICATION
        com.firebase.ui.auth.data.model.User user = new User.Builder("google", "test@test.com")
                .setName("Test user")
                .build();
        IdpResponse response = new IdpResponse.Builder(user).build();
        FirebaseUser mockFirebaseUser = Mockito.mock(FirebaseUser.class);
        when(mockFirebaseUser.getUid()).thenReturn("testUserId");

        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        when(mockAuth.getCurrentUser()).thenReturn(mockFirebaseUser);
        UserService userService = new UserService();
        PermissionDeviceService mockPermissionDeviceService = Mockito.mock(PermissionDeviceService.class);
        when(mockPermissionDeviceService.hasPermission(any())).thenReturn(true);
        if (Looper.myLooper() == null) {
            Looper.prepare();
        }
        WorkoutService workoutService = new WorkoutService();

        LocationDeviceService mockLocationDeviceService = Mockito.mock(LocationDeviceService.class);

        activityRule.getScenario().onActivity(activity -> {

            activity.setFirebaseAuth(mockAuth);
            userService.setMainActivity(activity);
            workoutService.setMainActivity(activity);
            activity.setUserService(userService);
            activity.setWorkoutService(workoutService);
            activity.setDeviceService(DeviceServiceType.PERMISSION,mockPermissionDeviceService);
            activity.setDeviceService(DeviceServiceType.LOCATION,mockLocationDeviceService);
        });

        //Set up mock firestore
        FirebaseFirestore mockFirestore = Mockito.mock(FirebaseFirestore.class);

        CollectionReference mockCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockFirestore.collection(eq("users"))).thenReturn(mockCollectionReference);
        DocumentReference mockDocument = Mockito.mock(DocumentReference.class);
        when(mockCollectionReference.document(eq("testUserId"))).thenReturn(mockDocument);

        Task<DocumentSnapshot> mockReadTask = Mockito.mock(Task.class);
        when(mockDocument.get()).thenReturn(mockReadTask);
        ArgumentCaptor<OnCompleteListener<DocumentSnapshot>> onCompleteListenerArgumentCaptorRead = ArgumentCaptor.forClass(OnCompleteListener.class);
        DocumentSnapshot mockDocumentSnapshot = Mockito.mock(DocumentSnapshot.class);

        when(mockReadTask.isSuccessful()).thenReturn(true);
        when(mockReadTask.getResult()).thenReturn(mockDocumentSnapshot);
        when(mockDocumentSnapshot.exists()).thenReturn(true);
        when(mockDocumentSnapshot.get("UID")).thenReturn("testUid");
        when(mockDocumentSnapshot.get("USERNAME")).thenReturn("testUsername");

        UserFirestoreDatabase userFirestoreDatabase = new UserFirestoreDatabase(mockFirestore);
        UserRepository userRepository = new UserRepository(userService, userFirestoreDatabase);
        userService.setUserRepository(userRepository);

        Instrumentation.ActivityResult firebaseResult = new Instrumentation.ActivityResult(RESULT_OK, response.toIntent());
        intending(hasComponent("com.firebase.ui.auth.KickoffActivity")).respondWith(firebaseResult);

        onView(withId(R.id.app_title)).check(matches(withText("Fantasy Fitness")));
        onView(withId(R.id.login_button)).perform(click());
        if (Looper.myLooper() == null) {
            Looper.prepare();
        }
        verify(mockReadTask).addOnCompleteListener(onCompleteListenerArgumentCaptorRead.capture());
        onCompleteListenerArgumentCaptorRead.getValue().onComplete(mockReadTask);
        onView(withId(R.id.user_home_title)).check(matches(withText("Fantasy Fitness")));

        //FINISH AUTHENTICATION, TEST IS AT USER HOME
        //FOLLOWING THIS LINE IS THE BEGINNING OF THE ACTUAL TEST THIS TEST IS SUPPOSED TO BE TESTING

        onView(withId(R.id.action_workout)).perform(click());
        onView(withId(R.id.workout_distance)).check(matches(withText("0.00 km")));
        onView(withId(R.id.start_workout_button)).perform(click());
        verify(mockPermissionDeviceService).hasPermission(eq(Manifest.permission.ACCESS_FINE_LOCATION));
        Location location1 = new Location("provider");
        location1.setLatitude(10.10);
        location1.setLongitude(10.10);
        Location location2 = new Location("provider");
        location2.setLatitude(10.11);
        location2.setLongitude(10.11);
        Location location3 = new Location("provider");
        location3.setLatitude(10.12);
        location3.setLongitude(10.15);
        Location pauseLocation = new Location("provider");
        pauseLocation.setLatitude(11.0);
        pauseLocation.setLongitude(11.0);
        LocationEvent firstLocationEvent = new LocationEvent(location1);
        LocationEvent secondLocationEvent = new LocationEvent(location2);
        LocationEvent thirdLocationEvent = new LocationEvent(location3);
        LocationEvent pauseLocationEvent = new LocationEvent(pauseLocation);
        workoutService.publishEvent(firstLocationEvent);
        onView(withId(R.id.workout_distance)).check(matches(withText("0.00 km")));
        workoutService.publishEvent(secondLocationEvent);
        onView(withId(R.id.workout_distance)).check(matches(withText("1.56 km")));
        onView(withId(R.id.pause_workout_button)).perform(click());
        workoutService.publishEvent(pauseLocationEvent);
        onView(withId(R.id.workout_distance)).check(matches(withText("1.56 km")));
        onView(withId(R.id.pause_workout_button)).perform(click());
        workoutService.publishEvent(secondLocationEvent);
        onView(withId(R.id.workout_distance)).check(matches(withText("1.56 km")));
        workoutService.publishEvent(thirdLocationEvent);
        onView(withId(R.id.workout_distance)).check(matches(withText("6.08 km")));
    }


}
