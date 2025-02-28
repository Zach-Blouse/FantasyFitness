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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.Manifest;
import android.app.Instrumentation;
import android.os.Looper;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;

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
import com.zblouse.fantasyfitness.R;
import com.zblouse.fantasyfitness.activity.DeviceServiceType;
import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.activity.PermissionDeviceService;
import com.zblouse.fantasyfitness.user.UserFirestoreDatabase;
import com.zblouse.fantasyfitness.user.UserGameStateFirestoreDatabase;
import com.zblouse.fantasyfitness.user.UserGameStateRepository;
import com.zblouse.fantasyfitness.user.UserGameStateService;
import com.zblouse.fantasyfitness.user.UserRepository;
import com.zblouse.fantasyfitness.user.UserService;
import com.zblouse.fantasyfitness.world.GameLocationService;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

@RunWith(AndroidJUnit4.class)
public class WorkoutRecordsTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Rule
    public IntentsTestRule<MainActivity> intentsTestRule =
            new IntentsTestRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule grantPermissionsRule = GrantPermissionRule.grant(Manifest.permission.POST_NOTIFICATIONS, Manifest.permission.FOREGROUND_SERVICE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);


    @Test
    public void workoutRecordsTest() throws InterruptedException {

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
        PermissionDeviceService permissionDeviceService = Mockito.mock(PermissionDeviceService.class);
        when(permissionDeviceService.hasPermission(any())).thenReturn(true);
        UserGameStateService userGameStateService = new UserGameStateService();
        WorkoutRecordService workoutRecordService = new WorkoutRecordService();

        activityRule.getScenario().onActivity(activity -> {
            activity.setFirebaseAuth(mockAuth);
            userGameStateService.setMainActivity(activity);
            userService.setMainActivity(activity);
            workoutRecordService.setMainActivity(activity);
            activity.setUserService(userService);
            activity.setDeviceService(DeviceServiceType.PERMISSION, permissionDeviceService);
            activity.setUserGameStateService(userGameStateService);
            activity.setWorkoutRecordService(workoutRecordService);
        });

        //Set up mock firestore
        FirebaseFirestore mockFirestore = Mockito.mock(FirebaseFirestore.class);
        //Set up mock Workout Records firestore
        CollectionReference mockWorkoutRecordCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockFirestore.collection(eq("workoutRecords"))).thenReturn(mockWorkoutRecordCollectionReference);
        DocumentReference mockWorkoutRecordsDocument = Mockito.mock(DocumentReference.class);
        when(mockWorkoutRecordCollectionReference.document(eq("testUserId"))).thenReturn(mockWorkoutRecordsDocument);

        Task<DocumentSnapshot> mockWorkoutRecordReadTask = Mockito.mock(Task.class);
        when(mockWorkoutRecordsDocument.get()).thenReturn(mockWorkoutRecordReadTask);
        ArgumentCaptor<OnCompleteListener<DocumentSnapshot>> onCompleteListenerArgumentCaptorReadWorkoutRecord = ArgumentCaptor.forClass(OnCompleteListener.class);
        DocumentSnapshot mockWorkoutRecordsDocumentSnapshot = Mockito.mock(DocumentSnapshot.class);

        when(mockWorkoutRecordReadTask.isSuccessful()).thenReturn(true);
        when(mockWorkoutRecordReadTask.getResult()).thenReturn(mockWorkoutRecordsDocumentSnapshot);
        when(mockWorkoutRecordsDocumentSnapshot.exists()).thenReturn(true);
        when(mockWorkoutRecordsDocumentSnapshot.get(WorkoutRecordFirestoreDatabase.MILE_RECORD_KEY, Long.class)).thenReturn(6000L);
        when(mockWorkoutRecordsDocumentSnapshot.get(WorkoutRecordFirestoreDatabase.FIVE_K_RECORD_KEY, Long.class)).thenReturn(7000L);
        when(mockWorkoutRecordsDocumentSnapshot.get(WorkoutRecordFirestoreDatabase.TEN_K_RECORD_KEY, Long.class)).thenReturn(8000L);
        when(mockWorkoutRecordsDocumentSnapshot.get(WorkoutRecordFirestoreDatabase.TWENTY_FIVE_K_RECORD_KEY, Long.class)).thenReturn(9000L);
        when(mockWorkoutRecordsDocumentSnapshot.get(WorkoutRecordFirestoreDatabase.MARATHON_RECORD_KEY, Long.class)).thenReturn(10000L);
        when(mockWorkoutRecordReadTask.isSuccessful()).thenReturn(true);
        when(mockWorkoutRecordReadTask.getResult()).thenReturn(mockWorkoutRecordsDocumentSnapshot);
        when(mockWorkoutRecordsDocumentSnapshot.exists()).thenReturn(true);

        WorkoutRecordFirestoreDatabase workoutRecordFirestoreDatabase = new WorkoutRecordFirestoreDatabase(mockFirestore);
        WorkoutRecordRepository workoutRecordRepository = new WorkoutRecordRepository(workoutRecordService, workoutRecordFirestoreDatabase);
        workoutRecordService.setWorkoutRecordRepository(workoutRecordRepository);
        //Setup mock user firestore

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
        when(mockReadTask.isSuccessful()).thenReturn(true);
        when(mockReadTask.getResult()).thenReturn(mockDocumentSnapshot);
        when(mockDocumentSnapshot.exists()).thenReturn(true);

        UserFirestoreDatabase userFirestoreDatabase = new UserFirestoreDatabase(mockFirestore);
        UserRepository userRepository = new UserRepository(userService, userFirestoreDatabase);
        userService.setUserRepository(userRepository);

        //Set up mock Game State Firestore

        CollectionReference mockGameStateCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockFirestore.collection(eq("gameState"))).thenReturn(mockGameStateCollectionReference);
        DocumentReference mockGameStateDocument = Mockito.mock(DocumentReference.class);
        when(mockGameStateCollectionReference.document(eq("testUserId"))).thenReturn(mockGameStateDocument);

        Task<DocumentSnapshot> mockGameStateReadTask = Mockito.mock(Task.class);
        when(mockGameStateDocument.get()).thenReturn(mockGameStateReadTask);
        ArgumentCaptor<OnCompleteListener<DocumentSnapshot>> onCompleteListenerArgumentCaptorReadGameState = ArgumentCaptor.forClass(OnCompleteListener.class);
        DocumentSnapshot mockGameStateDocumentSnapshot = Mockito.mock(DocumentSnapshot.class);

        when(mockGameStateReadTask.isSuccessful()).thenReturn(true);
        when(mockGameStateReadTask.getResult()).thenReturn(mockGameStateDocumentSnapshot);
        when(mockGameStateDocumentSnapshot.exists()).thenReturn(true);
        when(mockGameStateDocumentSnapshot.get(eq("gameLocation"), eq(String.class))).thenReturn(GameLocationService.ARDUWYN);
        when(mockGameStateDocumentSnapshot.get(eq("savedDistance"), eq(Double.class))).thenReturn(5500.0);
        when(mockGameStateReadTask.isSuccessful()).thenReturn(true);
        when(mockGameStateReadTask.getResult()).thenReturn(mockGameStateDocumentSnapshot);
        when(mockGameStateDocumentSnapshot.exists()).thenReturn(true);

        Task<Void> mockGameStateUpdateTask = Mockito.mock(Task.class);
        when(mockGameStateDocument.update(anyString(), any())).thenReturn(mockGameStateUpdateTask);
        ArgumentCaptor<OnCompleteListener<Void>> onCompleteListenerArgumentCaptorUpdateGameState = ArgumentCaptor.forClass(OnCompleteListener.class);

        when(mockGameStateUpdateTask.isSuccessful()).thenReturn(true);

        UserGameStateFirestoreDatabase userGameStateFirestoreDatabase = new UserGameStateFirestoreDatabase(mockFirestore);
        UserGameStateRepository userGameStateRepository = new UserGameStateRepository(userGameStateFirestoreDatabase, userGameStateService);
        userGameStateService.setUserGameStateRepository(userGameStateRepository);

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

        onView(withId(R.id.action_settings)).perform(click());
        onView(withId(R.id.workout_records_button)).perform(click());
        verify(mockWorkoutRecordReadTask).addOnCompleteListener(onCompleteListenerArgumentCaptorReadWorkoutRecord.capture());
        onCompleteListenerArgumentCaptorReadWorkoutRecord.getValue().onComplete(mockWorkoutRecordReadTask);
        onView(withId(R.id.workout_records_title)).check(matches(withText("Workout Records")));
        onView(withId(R.id.workout_records_display)).check(matches(withText("1 Mile Record 00:06\n5km Record 00:07\n10km Record 00:08\n25km Record 00:09\nMarathon Record 00:10")));
    }
}
