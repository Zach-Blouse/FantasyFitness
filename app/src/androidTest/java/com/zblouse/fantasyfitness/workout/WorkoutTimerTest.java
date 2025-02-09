package com.zblouse.fantasyfitness.workout;

import static android.app.Activity.RESULT_OK;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.app.Instrumentation;
import android.content.Intent;
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
import com.zblouse.fantasyfitness.MainActivity;
import com.zblouse.fantasyfitness.R;
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
public class WorkoutTimerTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Rule
    public IntentsTestRule<MainActivity> intentsTestRule =
            new IntentsTestRule<>(MainActivity.class);

    @Test
    public void workoutTimerTest() throws InterruptedException {

        //THIS TEST SETUP IS NEEDED TO AUTHENTICATE WITH THE APPLICATION

        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.EmailBuilder().build());
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build();
        com.firebase.ui.auth.data.model.User user = new User.Builder("google", "test@test.com")
                .setName("Test user")
                .build();
        IdpResponse response = new IdpResponse.Builder(user).build();
        FirebaseUser mockFirebaseUser = Mockito.mock(FirebaseUser.class);
        when(mockFirebaseUser.getUid()).thenReturn("testUserId");

        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        when(mockAuth.getCurrentUser()).thenReturn(mockFirebaseUser);
        UserService userService = new UserService();

        activityRule.getScenario().onActivity(activity -> {
            activity.setFirebaseAuth(mockAuth);
            userService.setMainActivity(activity);
            activity.setUserService(userService);
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
        //First run through is testing the button logic
        onView(withId(R.id.action_workout)).perform(click());
        onView(withId(R.id.workout_time)).check(matches(withText("00:00")));
        onView(withId(R.id.start_workout_button)).perform(click());
        onView(withId(R.id.pause_workout_button)).check(matches(withText(R.string.pause_workout)));
        onView(withId(R.id.stop_workout_button)).check(matches(withText(R.string.stop_workout)));
        onView(withId(R.id.pause_workout_button)).perform(click());
        onView(withId(R.id.pause_workout_button)).check(matches(withText(R.string.resume_workout)));
        onView(withId(R.id.pause_workout_button)).perform(click());
        onView(withId(R.id.pause_workout_button)).check(matches(withText(R.string.pause_workout)));
        onView(withId(R.id.stop_workout_button)).check(matches(withText(R.string.stop_workout)));
        onView(withId(R.id.stop_workout_button)).perform(click());
        onView(withId(R.id.pause_workout_button)).check(matches(withText(R.string.resume_workout)));
        onView(withId(R.id.stop_workout_button)).check(matches(withText(R.string.complete_workout)));
        onView(withId(R.id.pause_workout_button)).perform(click());
        onView(withId(R.id.pause_workout_button)).check(matches(withText(R.string.pause_workout)));
        onView(withId(R.id.stop_workout_button)).check(matches(withText(R.string.stop_workout)));
        onView(withId(R.id.stop_workout_button)).perform(click());
        onView(withId(R.id.pause_workout_button)).check(matches(withText(R.string.resume_workout)));
        onView(withId(R.id.stop_workout_button)).check(matches(withText(R.string.complete_workout)));
        onView(withId(R.id.stop_workout_button)).perform(click());
        onView(withId(R.id.start_workout_button)).check(matches(withText(R.string.start_workout)));
        //Now actually checking the timing
        onView(withId(R.id.start_workout_button)).perform(click());
        onView(withId(R.id.workout_time)).check(matches(withText("00:00")));
        Thread.sleep(5000);
        onView(withId(R.id.stop_workout_button)).perform(click());
        onView(withId(R.id.workout_time)).check(matches(withText("00:05")));

    }

    @Test
    public void workoutTimerPauseTest() throws InterruptedException {

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

        activityRule.getScenario().onActivity(activity -> {
            activity.setFirebaseAuth(mockAuth);
            userService.setMainActivity(activity);
            activity.setUserService(userService);
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
        onView(withId(R.id.workout_time)).check(matches(withText("00:00")));
        onView(withId(R.id.start_workout_button)).perform(click());
        onView(withId(R.id.workout_time)).check(matches(withText("00:00")));
        Thread.sleep(5000);
        onView(withId(R.id.pause_workout_button)).perform(click());
        onView(withId(R.id.workout_time)).check(matches(withText("00:05")));
        Thread.sleep(5000);
        onView(withId(R.id.workout_time)).check(matches(withText("00:05")));
        onView(withId(R.id.pause_workout_button)).perform(click());
        Thread.sleep(5000);
        onView(withId(R.id.workout_time)).check(matches(withText("00:10")));
        onView(withId(R.id.stop_workout_button)).perform(click());
        Thread.sleep(5000);
        onView(withId(R.id.workout_time)).check(matches(withText("00:10")));
    }

}
