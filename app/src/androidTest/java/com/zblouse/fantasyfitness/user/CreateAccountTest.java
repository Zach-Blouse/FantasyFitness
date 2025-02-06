package com.zblouse.fantasyfitness.user;

import static android.app.Activity.RESULT_OK;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.app.Instrumentation;
import android.content.Intent;
import android.os.Looper;

import androidx.test.annotation.UiThreadTest;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.KickoffActivity;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
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

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;


@RunWith(AndroidJUnit4.class)
public class CreateAccountTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Rule
    public IntentsTestRule<MainActivity> intentsTestRule =
            new IntentsTestRule<>(MainActivity.class);

    @Test
    public void createAccountTest(){

        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.EmailBuilder().build());
        // Create and launch sign-in intent
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build();
        User user = new User.Builder("google","test@test.com")
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
        Task<Void> mockWriteTask = Mockito.mock(Task.class);
        when(mockDocument.set(any())).thenReturn(mockWriteTask);
        Task<DocumentSnapshot> mockReadTask = Mockito.mock(Task.class);
        when(mockDocument.get()).thenReturn(mockReadTask);
        ArgumentCaptor<OnCompleteListener<Void>> onCompleteListenerArgumentCaptorWrite = ArgumentCaptor.forClass(OnCompleteListener.class);
        ArgumentCaptor<OnCompleteListener<DocumentSnapshot>> onCompleteListenerArgumentCaptorRead = ArgumentCaptor.forClass(OnCompleteListener.class);
        DocumentSnapshot mockDocumentSnapshot = Mockito.mock(DocumentSnapshot.class);

        when(mockWriteTask.isSuccessful()).thenReturn(true);

        UserFirestoreDatabase userFirestoreDatabase = new UserFirestoreDatabase(mockFirestore);
        UserRepository userRepository = new UserRepository(userService,userFirestoreDatabase);
        userService.setUserRepository(userRepository);

        Instrumentation.ActivityResult firebaseResult = new Instrumentation.ActivityResult(RESULT_OK,response.toIntent());
        intending(hasComponent("com.firebase.ui.auth.KickoffActivity")).respondWith(firebaseResult);

        onView(withId(R.id.app_title)).check(matches(withText("Fantasy Fitness")));
        onView(withId(R.id.create_account_button)).perform(click());
        verify(mockReadTask).addOnCompleteListener(onCompleteListenerArgumentCaptorRead.capture());
        onCompleteListenerArgumentCaptorRead.getValue().onComplete(mockReadTask);
        when(mockReadTask.isSuccessful()).thenReturn(true);
        when(mockReadTask.getResult()).thenReturn(mockDocumentSnapshot);
        when(mockDocumentSnapshot.exists()).thenReturn(false);

        onView(withId(R.id.create_account_title)).check(matches(withText("Create Account")));
        onView(withId(R.id.username_edit_text)).perform(typeText("createAccountTestUsername"));
        onView(withId(R.id.create_account_button)).perform(click());
        Looper.prepare();
        verify(mockWriteTask).addOnCompleteListener(onCompleteListenerArgumentCaptorWrite.capture());
        onCompleteListenerArgumentCaptorWrite.getValue().onComplete(mockWriteTask);

        onView(withId(R.id.user_home_title)).check(matches(withText("Fantasy Fitness")));

    }

}
