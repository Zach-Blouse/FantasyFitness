package com.zblouse.fantasyfitness.world;

import static android.app.Activity.RESULT_OK;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.swipeRight;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.Manifest;
import android.app.Instrumentation;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Looper;
import android.view.View;
import android.widget.Button;

import androidx.test.espresso.intent.Checks;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.espresso.matcher.ViewMatchers;
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
import com.zblouse.fantasyfitness.activity.LocationDeviceService;
import com.zblouse.fantasyfitness.activity.LocationEvent;
import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.activity.PermissionDeviceService;
import com.zblouse.fantasyfitness.user.UserFirestoreDatabase;
import com.zblouse.fantasyfitness.user.UserGameStateFirestoreDatabase;
import com.zblouse.fantasyfitness.user.UserGameStateRepository;
import com.zblouse.fantasyfitness.user.UserGameStateService;
import com.zblouse.fantasyfitness.user.UserRepository;
import com.zblouse.fantasyfitness.user.UserService;
import com.zblouse.fantasyfitness.workout.WorkoutService;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.HashMap;

@RunWith(AndroidJUnit4.class)
public class GameWorldTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Rule
    public IntentsTestRule<MainActivity> intentsTestRule =
            new IntentsTestRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule grantPermissionsRule = GrantPermissionRule.grant(Manifest.permission.POST_NOTIFICATIONS, Manifest.permission.FOREGROUND_SERVICE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);
    @Test
    public void gameWorldTest() throws InterruptedException {

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

        UserGameStateService userGameStateService = new UserGameStateService();

        activityRule.getScenario().onActivity(activity -> {

            activity.setFirebaseAuth(mockAuth);
            userService.setMainActivity(activity);
            workoutService.setMainActivity(activity);
            userGameStateService.setMainActivity(activity);
            activity.setUserService(userService);
            activity.setWorkoutService(workoutService);
            activity.setDeviceService(DeviceServiceType.PERMISSION,mockPermissionDeviceService);
            activity.setDeviceService(DeviceServiceType.LOCATION,mockLocationDeviceService);
            activity.setUserGameStateService(userGameStateService);
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
        onView(withId(R.id.quests_button)).check(matches(isDisplayed()));

        //FINISH AUTHENTICATION, TEST IS AT USER HOME
        //FOLLOWING THIS LINE IS THE BEGINNING OF THE ACTUAL TEST THIS TEST IS SUPPOSED TO BE TESTING

        onView(withId(R.id.action_map)).perform(click());
        onView(withId(R.id.imageView)).check(matches(isDisplayed()));
    }

    @Test
    public void gameLocationInfoDisplayTest() throws InterruptedException {

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
        UserGameStateService userGameStateService = new UserGameStateService();

        activityRule.getScenario().onActivity(activity -> {

            activity.setFirebaseAuth(mockAuth);
            userService.setMainActivity(activity);
            workoutService.setMainActivity(activity);
            userGameStateService.setMainActivity(activity);
            activity.setUserService(userService);
            activity.setWorkoutService(workoutService);
            activity.setDeviceService(DeviceServiceType.PERMISSION,mockPermissionDeviceService);
            activity.setDeviceService(DeviceServiceType.LOCATION,mockLocationDeviceService);
            activity.setUserGameStateService(userGameStateService);
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
        onView(withId(R.id.quests_button)).check(matches(isDisplayed()));

        //FINISH AUTHENTICATION, TEST IS AT USER HOME
        //FOLLOWING THIS LINE IS THE BEGINNING OF THE ACTUAL TEST THIS TEST IS SUPPOSED TO BE TESTING

        onView(withId(R.id.action_map)).perform(click());

        onView(withId(R.id.location_info_view)).check(matches(not(isDisplayed())));
        onView(withId(R.id.valley_of_monsters)).perform(click());
        onView(withId(R.id.location_info_view)).check(matches(isDisplayed()));
        onView(withId(R.id.location_info_name)).check(matches(withText(GameLocationService.VALLEY_OF_MONSTERS)));
        onView(withId(R.id.close_location_info_button)).perform(click());
        onView(withId(R.id.location_info_view)).check(matches(not(isDisplayed())));
    }

    @Test
    public void travelTest() throws InterruptedException {

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

        UserGameStateService userGameStateService = new UserGameStateService();

        activityRule.getScenario().onActivity(activity -> {

            activity.setFirebaseAuth(mockAuth);
            userService.setMainActivity(activity);
            workoutService.setMainActivity(activity);
            userGameStateService.setMainActivity(activity);
            activity.setUserService(userService);
            activity.setWorkoutService(workoutService);
            activity.setDeviceService(DeviceServiceType.PERMISSION,mockPermissionDeviceService);
            activity.setDeviceService(DeviceServiceType.LOCATION,mockLocationDeviceService);
            activity.setUserGameStateService(userGameStateService);
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
        when(mockGameStateDocumentSnapshot.get(eq("gameLocation"), eq(String.class))).thenReturn(GameLocationService.LAST_TOWER);
        when(mockGameStateDocumentSnapshot.get(eq("savedDistance"), eq(Double.class))).thenReturn(25000.0);
        when(mockGameStateDocumentSnapshot.get(eq("userGameCurrency"), eq(Integer.class))).thenReturn(7);
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
        onView(withId(R.id.quests_button)).check(matches(isDisplayed()));

        //FINISH AUTHENTICATION, TEST IS AT USER HOME
        //FOLLOWING THIS LINE IS THE BEGINNING OF THE ACTUAL TEST THIS TEST IS SUPPOSED TO BE TESTING

        onView(withId(R.id.action_map)).perform(click());
        verify(mockGameStateReadTask, times(2)).addOnCompleteListener(onCompleteListenerArgumentCaptorReadGameState.capture());
        onCompleteListenerArgumentCaptorReadGameState.getValue().onComplete(mockGameStateReadTask);
        onView(withId(R.id.world_map_horizontal)).perform(swipeRight());

        Thread.sleep(1000);
        onView(withId(R.id.location_info_view)).check(matches(not(isDisplayed())));
        onView(withId(R.id.valley_of_monsters)).perform(click());
        onView(withId(R.id.location_info_view)).check(matches(isDisplayed()));
        onView(withId(R.id.location_info_name)).check(matches(withText(GameLocationService.VALLEY_OF_MONSTERS)));
        onView(withId(R.id.travel_button)).perform(click());
        verify(mockGameStateReadTask, times(3)).addOnCompleteListener(onCompleteListenerArgumentCaptorReadGameState.capture());
        onCompleteListenerArgumentCaptorReadGameState.getValue().onComplete(mockGameStateReadTask);
        Thread.sleep(5000);
        verify(mockGameStateDocument).update(eq("gameLocation"), eq(GameLocationService.VALLEY_OF_MONSTERS));
        verify(mockGameStateUpdateTask).addOnCompleteListener(onCompleteListenerArgumentCaptorUpdateGameState.capture());
        onCompleteListenerArgumentCaptorUpdateGameState.getValue().onComplete(mockGameStateUpdateTask);
        
    }

    public static Matcher<View> withBackgroundColor(final int color) {
        Checks.checkNotNull(color);
        return new BoundedMatcher<View, Button>(Button.class) {
            @Override
            public boolean matchesSafely(Button button) {
                return color == ((ColorDrawable) button.getBackground()).getColor();

            }
            @Override
            public void describeTo(Description description) {
                description.appendText("with background color: ");
            }
        };
    }
}
