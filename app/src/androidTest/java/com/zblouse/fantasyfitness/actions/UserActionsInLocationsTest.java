package com.zblouse.fantasyfitness.actions;


import static android.app.Activity.RESULT_OK;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.zblouse.fantasyfitness.R;
import com.zblouse.fantasyfitness.activity.DeviceServiceType;
import com.zblouse.fantasyfitness.activity.LocationDeviceService;
import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.activity.PermissionDeviceService;
import com.zblouse.fantasyfitness.combat.cards.DeckFirestoreDatabase;
import com.zblouse.fantasyfitness.combat.cards.DeckRepository;
import com.zblouse.fantasyfitness.quest.QuestFirestoreDatabase;
import com.zblouse.fantasyfitness.quest.QuestRepository;
import com.zblouse.fantasyfitness.quest.QuestService;
import com.zblouse.fantasyfitness.user.UserFirestoreDatabase;
import com.zblouse.fantasyfitness.user.UserGameStateFirestoreDatabase;
import com.zblouse.fantasyfitness.user.UserGameStateRepository;
import com.zblouse.fantasyfitness.user.UserGameStateService;
import com.zblouse.fantasyfitness.user.UserRepository;
import com.zblouse.fantasyfitness.user.UserService;
import com.zblouse.fantasyfitness.workout.WorkoutService;
import com.zblouse.fantasyfitness.world.GameLocationService;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class UserActionsInLocationsTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Rule
    public IntentsTestRule<MainActivity> intentsTestRule =
            new IntentsTestRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule grantPermissionsRule = GrantPermissionRule.grant(Manifest.permission.POST_NOTIFICATIONS, Manifest.permission.FOREGROUND_SERVICE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);

    @Test
    public void woodlandsTest() throws InterruptedException {

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
        RandomActionResultTypeGenerator mockRandomActionResultTypeGenerator = Mockito.mock(RandomActionResultTypeGenerator.class);
        when(mockRandomActionResultTypeGenerator.getRandomActionResult(GameLocationService.WOODLANDS)).thenReturn(ActionResultType.NOTHING);

        UserGameStateService userGameStateService = new UserGameStateService();

        QuestService questService = new QuestService();

        activityRule.getScenario().onActivity(activity -> {

            activity.setFirebaseAuth(mockAuth);
            userService.setMainActivity(activity);
            workoutService.setMainActivity(activity);
            userGameStateService.setMainActivity(activity);
            questService.setMainActivity(activity);
            activity.setUserService(userService);
            activity.setWorkoutService(workoutService);
            activity.setDeviceService(DeviceServiceType.PERMISSION,mockPermissionDeviceService);
            activity.setDeviceService(DeviceServiceType.LOCATION,mockLocationDeviceService);
            activity.setUserGameStateService(userGameStateService);
            activity.getExploreActionService().setRandomActionResultTypeGenerator(mockRandomActionResultTypeGenerator);
            activity.setQuestService(questService);
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
        when(mockGameStateDocumentSnapshot.get(eq("gameLocation"), eq(String.class))).thenReturn(GameLocationService.WOODLANDS);
        when(mockGameStateDocumentSnapshot.get(eq("savedDistance"), eq(Double.class))).thenReturn(5500.0);
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

        //Set Up Quest Database

        CollectionReference mockQuestCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockFirestore.collection(eq("quest"))).thenReturn(mockQuestCollectionReference);
        DocumentReference mockQuestDocument = Mockito.mock(DocumentReference.class);
        when(mockQuestCollectionReference.document(eq("testUserId"))).thenReturn(mockQuestDocument);

        CollectionReference mockQuestListCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockQuestDocument.collection(eq("quests"))).thenReturn(mockQuestListCollectionReference);
        Task<QuerySnapshot> mockQueryTask = Mockito.mock(Task.class);
        when(mockQuestListCollectionReference.get()).thenReturn(mockQueryTask);
        ArgumentCaptor<OnCompleteListener<QuerySnapshot>> onCompleteListenerArgumentCaptorQuests = ArgumentCaptor.forClass(OnCompleteListener.class);
        when(mockQueryTask.isSuccessful()).thenReturn(true);
        List<QueryDocumentSnapshot> snapshots = new ArrayList<>();

        QuerySnapshot querySnapshot = Mockito.mock(QuerySnapshot.class);
        when(querySnapshot.iterator()).thenReturn(snapshots.iterator());
        when(mockQueryTask.getResult()).thenReturn(querySnapshot);

        QuestFirestoreDatabase questFirestoreDatabase = new QuestFirestoreDatabase(mockFirestore);
        QuestRepository questRepository = new QuestRepository(questService, questFirestoreDatabase);
        questService.setQuestRepository(questRepository);

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
        verify(mockGameStateReadTask).addOnCompleteListener(onCompleteListenerArgumentCaptorReadGameState.capture());
        onCompleteListenerArgumentCaptorReadGameState.getValue().onComplete(mockGameStateReadTask);

        onView(withId(R.id.dialog_card_view)).check(matches(not(isDisplayed())));
        onView(withId(R.id.marsh_button)).perform(click());

        verify(mockQueryTask).addOnCompleteListener(onCompleteListenerArgumentCaptorQuests.capture());
        onCompleteListenerArgumentCaptorQuests.getValue().onComplete(mockQueryTask);
        onView(withId(R.id.nothing_found_card_view)).check(matches(isDisplayed()));
    }

    @Test
    public void thanadelTest() throws InterruptedException {

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
        RandomActionResultTypeGenerator mockRandomActionResultTypeGenerator = Mockito.mock(RandomActionResultTypeGenerator.class);
        when(mockRandomActionResultTypeGenerator.getRandomActionResult(GameLocationService.THANADEL_VILLAGE)).thenReturn(ActionResultType.NOTHING);

        UserGameStateService userGameStateService = new UserGameStateService();

        QuestService questService = new QuestService();

        activityRule.getScenario().onActivity(activity -> {

            activity.setFirebaseAuth(mockAuth);
            userService.setMainActivity(activity);
            workoutService.setMainActivity(activity);
            userGameStateService.setMainActivity(activity);
            questService.setMainActivity(activity);
            activity.setUserService(userService);
            activity.setWorkoutService(workoutService);
            activity.setDeviceService(DeviceServiceType.PERMISSION,mockPermissionDeviceService);
            activity.setDeviceService(DeviceServiceType.LOCATION,mockLocationDeviceService);
            activity.setUserGameStateService(userGameStateService);
            activity.getExploreActionService().setRandomActionResultTypeGenerator(mockRandomActionResultTypeGenerator);
            activity.setQuestService(questService);
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
        when(mockGameStateDocumentSnapshot.get(eq("gameLocation"), eq(String.class))).thenReturn(GameLocationService.THANADEL_VILLAGE);
        when(mockGameStateDocumentSnapshot.get(eq("savedDistance"), eq(Double.class))).thenReturn(5500.0);
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

        //Set Up Quest Database

        CollectionReference mockQuestCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockFirestore.collection(eq("quest"))).thenReturn(mockQuestCollectionReference);
        DocumentReference mockQuestDocument = Mockito.mock(DocumentReference.class);
        when(mockQuestCollectionReference.document(eq("testUserId"))).thenReturn(mockQuestDocument);

        CollectionReference mockQuestListCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockQuestDocument.collection(eq("quests"))).thenReturn(mockQuestListCollectionReference);
        Task<QuerySnapshot> mockQueryTask = Mockito.mock(Task.class);
        when(mockQuestListCollectionReference.get()).thenReturn(mockQueryTask);
        ArgumentCaptor<OnCompleteListener<QuerySnapshot>> onCompleteListenerArgumentCaptorQuests = ArgumentCaptor.forClass(OnCompleteListener.class);
        when(mockQueryTask.isSuccessful()).thenReturn(true);
        List<QueryDocumentSnapshot> snapshots = new ArrayList<>();

        QuerySnapshot querySnapshot = Mockito.mock(QuerySnapshot.class);
        when(querySnapshot.iterator()).thenReturn(snapshots.iterator());
        when(mockQueryTask.getResult()).thenReturn(querySnapshot);

        QuestFirestoreDatabase questFirestoreDatabase = new QuestFirestoreDatabase(mockFirestore);
        QuestRepository questRepository = new QuestRepository(questService, questFirestoreDatabase);
        questService.setQuestRepository(questRepository);

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
        verify(mockGameStateReadTask).addOnCompleteListener(onCompleteListenerArgumentCaptorReadGameState.capture());
        onCompleteListenerArgumentCaptorReadGameState.getValue().onComplete(mockGameStateReadTask);

        onView(withId(R.id.dialog_card_view)).check(matches(not(isDisplayed())));
        onView(withId(R.id.inn_button)).perform(click());

        verify(mockQueryTask).addOnCompleteListener(onCompleteListenerArgumentCaptorQuests.capture());
        onCompleteListenerArgumentCaptorQuests.getValue().onComplete(mockQueryTask);
        onView(withId(R.id.dialog_card_view)).check(matches(isDisplayed()));
    }

    public void valleyOfMonstersTest() throws InterruptedException {

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
        RandomActionResultTypeGenerator mockRandomActionResultTypeGenerator = Mockito.mock(RandomActionResultTypeGenerator.class);
        when(mockRandomActionResultTypeGenerator.getRandomActionResult(GameLocationService.VALLEY_OF_MONSTERS)).thenReturn(ActionResultType.NOTHING);

        UserGameStateService userGameStateService = new UserGameStateService();

        QuestService questService = new QuestService();

        activityRule.getScenario().onActivity(activity -> {

            activity.setFirebaseAuth(mockAuth);
            userService.setMainActivity(activity);
            workoutService.setMainActivity(activity);
            userGameStateService.setMainActivity(activity);
            questService.setMainActivity(activity);
            activity.setUserService(userService);
            activity.setWorkoutService(workoutService);
            activity.setDeviceService(DeviceServiceType.PERMISSION,mockPermissionDeviceService);
            activity.setDeviceService(DeviceServiceType.LOCATION,mockLocationDeviceService);
            activity.setUserGameStateService(userGameStateService);
            activity.getExploreActionService().setRandomActionResultTypeGenerator(mockRandomActionResultTypeGenerator);
            activity.setQuestService(questService);
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
        when(mockGameStateDocumentSnapshot.get(eq("gameLocation"), eq(String.class))).thenReturn(GameLocationService.VALLEY_OF_MONSTERS);
        when(mockGameStateDocumentSnapshot.get(eq("savedDistance"), eq(Double.class))).thenReturn(5500.0);
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

        //Set Up Quest Database

        CollectionReference mockQuestCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockFirestore.collection(eq("quest"))).thenReturn(mockQuestCollectionReference);
        DocumentReference mockQuestDocument = Mockito.mock(DocumentReference.class);
        when(mockQuestCollectionReference.document(eq("testUserId"))).thenReturn(mockQuestDocument);

        CollectionReference mockQuestListCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockQuestDocument.collection(eq("quests"))).thenReturn(mockQuestListCollectionReference);
        Task<QuerySnapshot> mockQueryTask = Mockito.mock(Task.class);
        when(mockQuestListCollectionReference.get()).thenReturn(mockQueryTask);
        ArgumentCaptor<OnCompleteListener<QuerySnapshot>> onCompleteListenerArgumentCaptorQuests = ArgumentCaptor.forClass(OnCompleteListener.class);
        when(mockQueryTask.isSuccessful()).thenReturn(true);
        List<QueryDocumentSnapshot> snapshots = new ArrayList<>();

        QuerySnapshot querySnapshot = Mockito.mock(QuerySnapshot.class);
        when(querySnapshot.iterator()).thenReturn(snapshots.iterator());
        when(mockQueryTask.getResult()).thenReturn(querySnapshot);

        QuestFirestoreDatabase questFirestoreDatabase = new QuestFirestoreDatabase(mockFirestore);
        QuestRepository questRepository = new QuestRepository(questService, questFirestoreDatabase);
        questService.setQuestRepository(questRepository);

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
        verify(mockGameStateReadTask).addOnCompleteListener(onCompleteListenerArgumentCaptorReadGameState.capture());
        onCompleteListenerArgumentCaptorReadGameState.getValue().onComplete(mockGameStateReadTask);

        onView(withId(R.id.dialog_card_view)).check(matches(not(isDisplayed())));
        onView(withId(R.id.marsh_button)).perform(click());

        verify(mockQueryTask).addOnCompleteListener(onCompleteListenerArgumentCaptorQuests.capture());
        onCompleteListenerArgumentCaptorQuests.getValue().onComplete(mockQueryTask);
        onView(withId(R.id.nothing_found_card_view)).check(matches(isDisplayed()));
    }

    public void lastTowerTest() throws InterruptedException {

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
        RandomActionResultTypeGenerator mockRandomActionResultTypeGenerator = Mockito.mock(RandomActionResultTypeGenerator.class);
        when(mockRandomActionResultTypeGenerator.getRandomActionResult(GameLocationService.LAST_TOWER)).thenReturn(ActionResultType.NOTHING);

        UserGameStateService userGameStateService = new UserGameStateService();

        QuestService questService = new QuestService();

        activityRule.getScenario().onActivity(activity -> {

            activity.setFirebaseAuth(mockAuth);
            userService.setMainActivity(activity);
            workoutService.setMainActivity(activity);
            userGameStateService.setMainActivity(activity);
            questService.setMainActivity(activity);
            activity.setUserService(userService);
            activity.setWorkoutService(workoutService);
            activity.setDeviceService(DeviceServiceType.PERMISSION,mockPermissionDeviceService);
            activity.setDeviceService(DeviceServiceType.LOCATION,mockLocationDeviceService);
            activity.setUserGameStateService(userGameStateService);
            activity.getExploreActionService().setRandomActionResultTypeGenerator(mockRandomActionResultTypeGenerator);
            activity.setQuestService(questService);
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
        when(mockGameStateDocumentSnapshot.get(eq("savedDistance"), eq(Double.class))).thenReturn(5500.0);
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

        //Set Up Quest Database

        CollectionReference mockQuestCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockFirestore.collection(eq("quest"))).thenReturn(mockQuestCollectionReference);
        DocumentReference mockQuestDocument = Mockito.mock(DocumentReference.class);
        when(mockQuestCollectionReference.document(eq("testUserId"))).thenReturn(mockQuestDocument);

        CollectionReference mockQuestListCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockQuestDocument.collection(eq("quests"))).thenReturn(mockQuestListCollectionReference);
        Task<QuerySnapshot> mockQueryTask = Mockito.mock(Task.class);
        when(mockQuestListCollectionReference.get()).thenReturn(mockQueryTask);
        ArgumentCaptor<OnCompleteListener<QuerySnapshot>> onCompleteListenerArgumentCaptorQuests = ArgumentCaptor.forClass(OnCompleteListener.class);
        when(mockQueryTask.isSuccessful()).thenReturn(true);
        List<QueryDocumentSnapshot> snapshots = new ArrayList<>();

        QuerySnapshot querySnapshot = Mockito.mock(QuerySnapshot.class);
        when(querySnapshot.iterator()).thenReturn(snapshots.iterator());
        when(mockQueryTask.getResult()).thenReturn(querySnapshot);

        QuestFirestoreDatabase questFirestoreDatabase = new QuestFirestoreDatabase(mockFirestore);
        QuestRepository questRepository = new QuestRepository(questService, questFirestoreDatabase);
        questService.setQuestRepository(questRepository);

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
        verify(mockGameStateReadTask).addOnCompleteListener(onCompleteListenerArgumentCaptorReadGameState.capture());
        onCompleteListenerArgumentCaptorReadGameState.getValue().onComplete(mockGameStateReadTask);

        onView(withId(R.id.dialog_card_view)).check(matches(not(isDisplayed())));
        onView(withId(R.id.marsh_button)).perform(click());

        verify(mockQueryTask).addOnCompleteListener(onCompleteListenerArgumentCaptorQuests.capture());
        onCompleteListenerArgumentCaptorQuests.getValue().onComplete(mockQueryTask);
        onView(withId(R.id.dialog_card_view)).check(matches(isDisplayed()));
    }

    public void monasteryTest() throws InterruptedException {

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
        RandomActionResultTypeGenerator mockRandomActionResultTypeGenerator = Mockito.mock(RandomActionResultTypeGenerator.class);
        when(mockRandomActionResultTypeGenerator.getRandomActionResult(GameLocationService.MONASTARY)).thenReturn(ActionResultType.NOTHING);

        UserGameStateService userGameStateService = new UserGameStateService();

        QuestService questService = new QuestService();

        activityRule.getScenario().onActivity(activity -> {

            activity.setFirebaseAuth(mockAuth);
            userService.setMainActivity(activity);
            workoutService.setMainActivity(activity);
            userGameStateService.setMainActivity(activity);
            questService.setMainActivity(activity);
            activity.setUserService(userService);
            activity.setWorkoutService(workoutService);
            activity.setDeviceService(DeviceServiceType.PERMISSION,mockPermissionDeviceService);
            activity.setDeviceService(DeviceServiceType.LOCATION,mockLocationDeviceService);
            activity.setUserGameStateService(userGameStateService);
            activity.getExploreActionService().setRandomActionResultTypeGenerator(mockRandomActionResultTypeGenerator);
            activity.setQuestService(questService);
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
        when(mockGameStateDocumentSnapshot.get(eq("gameLocation"), eq(String.class))).thenReturn(GameLocationService.MONASTARY);
        when(mockGameStateDocumentSnapshot.get(eq("savedDistance"), eq(Double.class))).thenReturn(5500.0);
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

        //Set Up Quest Database

        CollectionReference mockQuestCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockFirestore.collection(eq("quest"))).thenReturn(mockQuestCollectionReference);
        DocumentReference mockQuestDocument = Mockito.mock(DocumentReference.class);
        when(mockQuestCollectionReference.document(eq("testUserId"))).thenReturn(mockQuestDocument);

        CollectionReference mockQuestListCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockQuestDocument.collection(eq("quests"))).thenReturn(mockQuestListCollectionReference);
        Task<QuerySnapshot> mockQueryTask = Mockito.mock(Task.class);
        when(mockQuestListCollectionReference.get()).thenReturn(mockQueryTask);
        ArgumentCaptor<OnCompleteListener<QuerySnapshot>> onCompleteListenerArgumentCaptorQuests = ArgumentCaptor.forClass(OnCompleteListener.class);
        when(mockQueryTask.isSuccessful()).thenReturn(true);
        List<QueryDocumentSnapshot> snapshots = new ArrayList<>();

        QuerySnapshot querySnapshot = Mockito.mock(QuerySnapshot.class);
        when(querySnapshot.iterator()).thenReturn(snapshots.iterator());
        when(mockQueryTask.getResult()).thenReturn(querySnapshot);

        QuestFirestoreDatabase questFirestoreDatabase = new QuestFirestoreDatabase(mockFirestore);
        QuestRepository questRepository = new QuestRepository(questService, questFirestoreDatabase);
        questService.setQuestRepository(questRepository);

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
        verify(mockGameStateReadTask).addOnCompleteListener(onCompleteListenerArgumentCaptorReadGameState.capture());
        onCompleteListenerArgumentCaptorReadGameState.getValue().onComplete(mockGameStateReadTask);

        onView(withId(R.id.dialog_card_view)).check(matches(not(isDisplayed())));
        onView(withId(R.id.marsh_button)).perform(click());

        verify(mockQueryTask).addOnCompleteListener(onCompleteListenerArgumentCaptorQuests.capture());
        onCompleteListenerArgumentCaptorQuests.getValue().onComplete(mockQueryTask);
        onView(withId(R.id.dialog_card_view)).check(matches(isDisplayed()));
    }

    public void arduwynTest() throws InterruptedException {

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
        RandomActionResultTypeGenerator mockRandomActionResultTypeGenerator = Mockito.mock(RandomActionResultTypeGenerator.class);
        when(mockRandomActionResultTypeGenerator.getRandomActionResult(GameLocationService.ARDUWYN)).thenReturn(ActionResultType.NOTHING);

        UserGameStateService userGameStateService = new UserGameStateService();

        QuestService questService = new QuestService();

        activityRule.getScenario().onActivity(activity -> {

            activity.setFirebaseAuth(mockAuth);
            userService.setMainActivity(activity);
            workoutService.setMainActivity(activity);
            userGameStateService.setMainActivity(activity);
            questService.setMainActivity(activity);
            activity.setUserService(userService);
            activity.setWorkoutService(workoutService);
            activity.setDeviceService(DeviceServiceType.PERMISSION,mockPermissionDeviceService);
            activity.setDeviceService(DeviceServiceType.LOCATION,mockLocationDeviceService);
            activity.setUserGameStateService(userGameStateService);
            activity.getExploreActionService().setRandomActionResultTypeGenerator(mockRandomActionResultTypeGenerator);
            activity.setQuestService(questService);
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

        //Set Up Quest Database

        CollectionReference mockQuestCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockFirestore.collection(eq("quest"))).thenReturn(mockQuestCollectionReference);
        DocumentReference mockQuestDocument = Mockito.mock(DocumentReference.class);
        when(mockQuestCollectionReference.document(eq("testUserId"))).thenReturn(mockQuestDocument);

        CollectionReference mockQuestListCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockQuestDocument.collection(eq("quests"))).thenReturn(mockQuestListCollectionReference);
        Task<QuerySnapshot> mockQueryTask = Mockito.mock(Task.class);
        when(mockQuestListCollectionReference.get()).thenReturn(mockQueryTask);
        ArgumentCaptor<OnCompleteListener<QuerySnapshot>> onCompleteListenerArgumentCaptorQuests = ArgumentCaptor.forClass(OnCompleteListener.class);
        when(mockQueryTask.isSuccessful()).thenReturn(true);
        List<QueryDocumentSnapshot> snapshots = new ArrayList<>();

        QuerySnapshot querySnapshot = Mockito.mock(QuerySnapshot.class);
        when(querySnapshot.iterator()).thenReturn(snapshots.iterator());
        when(mockQueryTask.getResult()).thenReturn(querySnapshot);

        QuestFirestoreDatabase questFirestoreDatabase = new QuestFirestoreDatabase(mockFirestore);
        QuestRepository questRepository = new QuestRepository(questService, questFirestoreDatabase);
        questService.setQuestRepository(questRepository);

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
        verify(mockGameStateReadTask).addOnCompleteListener(onCompleteListenerArgumentCaptorReadGameState.capture());
        onCompleteListenerArgumentCaptorReadGameState.getValue().onComplete(mockGameStateReadTask);

        onView(withId(R.id.dialog_card_view)).check(matches(not(isDisplayed())));
        onView(withId(R.id.marsh_button)).perform(click());

        verify(mockQueryTask).addOnCompleteListener(onCompleteListenerArgumentCaptorQuests.capture());
        onCompleteListenerArgumentCaptorQuests.getValue().onComplete(mockQueryTask);
        onView(withId(R.id.dialog_card_view)).check(matches(isDisplayed()));
    }

    public void northRoadTest() throws InterruptedException {

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
        RandomActionResultTypeGenerator mockRandomActionResultTypeGenerator = Mockito.mock(RandomActionResultTypeGenerator.class);
        when(mockRandomActionResultTypeGenerator.getRandomActionResult(GameLocationService.NORTH_ROAD)).thenReturn(ActionResultType.NOTHING);

        UserGameStateService userGameStateService = new UserGameStateService();

        QuestService questService = new QuestService();

        activityRule.getScenario().onActivity(activity -> {

            activity.setFirebaseAuth(mockAuth);
            userService.setMainActivity(activity);
            workoutService.setMainActivity(activity);
            userGameStateService.setMainActivity(activity);
            questService.setMainActivity(activity);
            activity.setUserService(userService);
            activity.setWorkoutService(workoutService);
            activity.setDeviceService(DeviceServiceType.PERMISSION,mockPermissionDeviceService);
            activity.setDeviceService(DeviceServiceType.LOCATION,mockLocationDeviceService);
            activity.setUserGameStateService(userGameStateService);
            activity.getExploreActionService().setRandomActionResultTypeGenerator(mockRandomActionResultTypeGenerator);
            activity.setQuestService(questService);
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
        when(mockGameStateDocumentSnapshot.get(eq("gameLocation"), eq(String.class))).thenReturn(GameLocationService.NORTH_ROAD);
        when(mockGameStateDocumentSnapshot.get(eq("savedDistance"), eq(Double.class))).thenReturn(5500.0);
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

        //Set Up Quest Database

        CollectionReference mockQuestCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockFirestore.collection(eq("quest"))).thenReturn(mockQuestCollectionReference);
        DocumentReference mockQuestDocument = Mockito.mock(DocumentReference.class);
        when(mockQuestCollectionReference.document(eq("testUserId"))).thenReturn(mockQuestDocument);

        CollectionReference mockQuestListCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockQuestDocument.collection(eq("quests"))).thenReturn(mockQuestListCollectionReference);
        Task<QuerySnapshot> mockQueryTask = Mockito.mock(Task.class);
        when(mockQuestListCollectionReference.get()).thenReturn(mockQueryTask);
        ArgumentCaptor<OnCompleteListener<QuerySnapshot>> onCompleteListenerArgumentCaptorQuests = ArgumentCaptor.forClass(OnCompleteListener.class);
        when(mockQueryTask.isSuccessful()).thenReturn(true);
        List<QueryDocumentSnapshot> snapshots = new ArrayList<>();

        QuerySnapshot querySnapshot = Mockito.mock(QuerySnapshot.class);
        when(querySnapshot.iterator()).thenReturn(snapshots.iterator());
        when(mockQueryTask.getResult()).thenReturn(querySnapshot);

        QuestFirestoreDatabase questFirestoreDatabase = new QuestFirestoreDatabase(mockFirestore);
        QuestRepository questRepository = new QuestRepository(questService, questFirestoreDatabase);
        questService.setQuestRepository(questRepository);

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
        verify(mockGameStateReadTask).addOnCompleteListener(onCompleteListenerArgumentCaptorReadGameState.capture());
        onCompleteListenerArgumentCaptorReadGameState.getValue().onComplete(mockGameStateReadTask);

        onView(withId(R.id.dialog_card_view)).check(matches(not(isDisplayed())));
        onView(withId(R.id.marsh_button)).perform(click());

        verify(mockQueryTask).addOnCompleteListener(onCompleteListenerArgumentCaptorQuests.capture());
        onCompleteListenerArgumentCaptorQuests.getValue().onComplete(mockQueryTask);
        onView(withId(R.id.nothing_found_card_view)).check(matches(isDisplayed()));
    }

    @Test
    public void faolynTest() throws InterruptedException {

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
        RandomActionResultTypeGenerator mockRandomActionResultTypeGenerator = Mockito.mock(RandomActionResultTypeGenerator.class);
        when(mockRandomActionResultTypeGenerator.getRandomActionResult(GameLocationService.FAOLYN)).thenReturn(ActionResultType.NOTHING);

        UserGameStateService userGameStateService = new UserGameStateService();

        QuestService questService = new QuestService();

        activityRule.getScenario().onActivity(activity -> {

            activity.setFirebaseAuth(mockAuth);
            userService.setMainActivity(activity);
            workoutService.setMainActivity(activity);
            userGameStateService.setMainActivity(activity);
            questService.setMainActivity(activity);
            activity.setUserService(userService);
            activity.setWorkoutService(workoutService);
            activity.setDeviceService(DeviceServiceType.PERMISSION,mockPermissionDeviceService);
            activity.setDeviceService(DeviceServiceType.LOCATION,mockLocationDeviceService);
            activity.setUserGameStateService(userGameStateService);
            activity.getExploreActionService().setRandomActionResultTypeGenerator(mockRandomActionResultTypeGenerator);
            activity.setQuestService(questService);
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
        when(mockGameStateDocumentSnapshot.get(eq("gameLocation"), eq(String.class))).thenReturn(GameLocationService.FAOLYN);
        when(mockGameStateDocumentSnapshot.get(eq("savedDistance"), eq(Double.class))).thenReturn(5500.0);
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

        //Set Up Quest Database

        CollectionReference mockQuestCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockFirestore.collection(eq("quest"))).thenReturn(mockQuestCollectionReference);
        DocumentReference mockQuestDocument = Mockito.mock(DocumentReference.class);
        when(mockQuestCollectionReference.document(eq("testUserId"))).thenReturn(mockQuestDocument);

        CollectionReference mockQuestListCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockQuestDocument.collection(eq("quests"))).thenReturn(mockQuestListCollectionReference);
        Task<QuerySnapshot> mockQueryTask = Mockito.mock(Task.class);
        when(mockQuestListCollectionReference.get()).thenReturn(mockQueryTask);
        ArgumentCaptor<OnCompleteListener<QuerySnapshot>> onCompleteListenerArgumentCaptorQuests = ArgumentCaptor.forClass(OnCompleteListener.class);
        when(mockQueryTask.isSuccessful()).thenReturn(true);
        List<QueryDocumentSnapshot> snapshots = new ArrayList<>();

        QuerySnapshot querySnapshot = Mockito.mock(QuerySnapshot.class);
        when(querySnapshot.iterator()).thenReturn(snapshots.iterator());
        when(mockQueryTask.getResult()).thenReturn(querySnapshot);

        QuestFirestoreDatabase questFirestoreDatabase = new QuestFirestoreDatabase(mockFirestore);
        QuestRepository questRepository = new QuestRepository(questService, questFirestoreDatabase);
        questService.setQuestRepository(questRepository);

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
        verify(mockGameStateReadTask).addOnCompleteListener(onCompleteListenerArgumentCaptorReadGameState.capture());
        onCompleteListenerArgumentCaptorReadGameState.getValue().onComplete(mockGameStateReadTask);

        onView(withId(R.id.dialog_card_view)).check(matches(not(isDisplayed())));
        onView(withId(R.id.inn_button)).perform(scrollTo());
        onView(withId(R.id.inn_button)).perform(click());

        verify(mockQueryTask).addOnCompleteListener(onCompleteListenerArgumentCaptorQuests.capture());
        onCompleteListenerArgumentCaptorQuests.getValue().onComplete(mockQueryTask);
        onView(withId(R.id.dialog_card_view)).check(matches(isDisplayed()));
    }

    public void riverlandsTest() throws InterruptedException {
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
        RandomActionResultTypeGenerator mockRandomActionResultTypeGenerator = Mockito.mock(RandomActionResultTypeGenerator.class);
        when(mockRandomActionResultTypeGenerator.getRandomActionResult(GameLocationService.RIVERLANDS)).thenReturn(ActionResultType.NOTHING);

        UserGameStateService userGameStateService = new UserGameStateService();

        QuestService questService = new QuestService();

        activityRule.getScenario().onActivity(activity -> {

            activity.setFirebaseAuth(mockAuth);
            userService.setMainActivity(activity);
            workoutService.setMainActivity(activity);
            userGameStateService.setMainActivity(activity);
            questService.setMainActivity(activity);
            activity.setUserService(userService);
            activity.setWorkoutService(workoutService);
            activity.setDeviceService(DeviceServiceType.PERMISSION,mockPermissionDeviceService);
            activity.setDeviceService(DeviceServiceType.LOCATION,mockLocationDeviceService);
            activity.setUserGameStateService(userGameStateService);
            activity.getExploreActionService().setRandomActionResultTypeGenerator(mockRandomActionResultTypeGenerator);
            activity.setQuestService(questService);
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
        when(mockGameStateDocumentSnapshot.get(eq("gameLocation"), eq(String.class))).thenReturn(GameLocationService.RIVERLANDS);
        when(mockGameStateDocumentSnapshot.get(eq("savedDistance"), eq(Double.class))).thenReturn(5500.0);
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

        //Set Up Quest Database

        CollectionReference mockQuestCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockFirestore.collection(eq("quest"))).thenReturn(mockQuestCollectionReference);
        DocumentReference mockQuestDocument = Mockito.mock(DocumentReference.class);
        when(mockQuestCollectionReference.document(eq("testUserId"))).thenReturn(mockQuestDocument);

        CollectionReference mockQuestListCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockQuestDocument.collection(eq("quests"))).thenReturn(mockQuestListCollectionReference);
        Task<QuerySnapshot> mockQueryTask = Mockito.mock(Task.class);
        when(mockQuestListCollectionReference.get()).thenReturn(mockQueryTask);
        ArgumentCaptor<OnCompleteListener<QuerySnapshot>> onCompleteListenerArgumentCaptorQuests = ArgumentCaptor.forClass(OnCompleteListener.class);
        when(mockQueryTask.isSuccessful()).thenReturn(true);
        List<QueryDocumentSnapshot> snapshots = new ArrayList<>();

        QuerySnapshot querySnapshot = Mockito.mock(QuerySnapshot.class);
        when(querySnapshot.iterator()).thenReturn(snapshots.iterator());
        when(mockQueryTask.getResult()).thenReturn(querySnapshot);

        QuestFirestoreDatabase questFirestoreDatabase = new QuestFirestoreDatabase(mockFirestore);
        QuestRepository questRepository = new QuestRepository(questService, questFirestoreDatabase);
        questService.setQuestRepository(questRepository);

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
        verify(mockGameStateReadTask).addOnCompleteListener(onCompleteListenerArgumentCaptorReadGameState.capture());
        onCompleteListenerArgumentCaptorReadGameState.getValue().onComplete(mockGameStateReadTask);

        onView(withId(R.id.dialog_card_view)).check(matches(not(isDisplayed())));
        onView(withId(R.id.marsh_button)).perform(click());

        verify(mockQueryTask).addOnCompleteListener(onCompleteListenerArgumentCaptorQuests.capture());
        onCompleteListenerArgumentCaptorQuests.getValue().onComplete(mockQueryTask);
        onView(withId(R.id.nothing_found_card_view)).check(matches(isDisplayed()));
    }

    @Test
    public void bridgetonTest() throws InterruptedException {

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
        RandomActionResultTypeGenerator mockRandomActionResultTypeGenerator = Mockito.mock(RandomActionResultTypeGenerator.class);
        when(mockRandomActionResultTypeGenerator.getRandomActionResult(GameLocationService.BRIDGETON)).thenReturn(ActionResultType.NOTHING);

        UserGameStateService userGameStateService = new UserGameStateService();

        QuestService questService = new QuestService();

        activityRule.getScenario().onActivity(activity -> {

            activity.setFirebaseAuth(mockAuth);
            userService.setMainActivity(activity);
            workoutService.setMainActivity(activity);
            userGameStateService.setMainActivity(activity);
            questService.setMainActivity(activity);
            activity.setUserService(userService);
            activity.setWorkoutService(workoutService);
            activity.setDeviceService(DeviceServiceType.PERMISSION,mockPermissionDeviceService);
            activity.setDeviceService(DeviceServiceType.LOCATION,mockLocationDeviceService);
            activity.setUserGameStateService(userGameStateService);
            activity.getExploreActionService().setRandomActionResultTypeGenerator(mockRandomActionResultTypeGenerator);
            activity.setQuestService(questService);
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
        when(mockGameStateDocumentSnapshot.get(eq("gameLocation"), eq(String.class))).thenReturn(GameLocationService.BRIDGETON);
        when(mockGameStateDocumentSnapshot.get(eq("savedDistance"), eq(Double.class))).thenReturn(5500.0);
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

        //Set Up Quest Database

        CollectionReference mockQuestCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockFirestore.collection(eq("quest"))).thenReturn(mockQuestCollectionReference);
        DocumentReference mockQuestDocument = Mockito.mock(DocumentReference.class);
        when(mockQuestCollectionReference.document(eq("testUserId"))).thenReturn(mockQuestDocument);

        CollectionReference mockQuestListCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockQuestDocument.collection(eq("quests"))).thenReturn(mockQuestListCollectionReference);
        Task<QuerySnapshot> mockQueryTask = Mockito.mock(Task.class);
        when(mockQuestListCollectionReference.get()).thenReturn(mockQueryTask);
        ArgumentCaptor<OnCompleteListener<QuerySnapshot>> onCompleteListenerArgumentCaptorQuests = ArgumentCaptor.forClass(OnCompleteListener.class);
        when(mockQueryTask.isSuccessful()).thenReturn(true);
        List<QueryDocumentSnapshot> snapshots = new ArrayList<>();

        QuerySnapshot querySnapshot = Mockito.mock(QuerySnapshot.class);
        when(querySnapshot.iterator()).thenReturn(snapshots.iterator());
        when(mockQueryTask.getResult()).thenReturn(querySnapshot);

        QuestFirestoreDatabase questFirestoreDatabase = new QuestFirestoreDatabase(mockFirestore);
        QuestRepository questRepository = new QuestRepository(questService, questFirestoreDatabase);
        questService.setQuestRepository(questRepository);

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
        verify(mockGameStateReadTask).addOnCompleteListener(onCompleteListenerArgumentCaptorReadGameState.capture());
        onCompleteListenerArgumentCaptorReadGameState.getValue().onComplete(mockGameStateReadTask);

        onView(withId(R.id.dialog_card_view)).check(matches(not(isDisplayed())));
        onView(withId(R.id.inn_button)).perform(scrollTo());
        onView(withId(R.id.inn_button)).perform(click());

        verify(mockQueryTask).addOnCompleteListener(onCompleteListenerArgumentCaptorQuests.capture());
        onCompleteListenerArgumentCaptorQuests.getValue().onComplete(mockQueryTask);
        onView(withId(R.id.dialog_card_view)).check(matches(isDisplayed()));
    }


    public void hillsTest() throws InterruptedException {

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
        RandomActionResultTypeGenerator mockRandomActionResultTypeGenerator = Mockito.mock(RandomActionResultTypeGenerator.class);
        when(mockRandomActionResultTypeGenerator.getRandomActionResult(GameLocationService.HILLS)).thenReturn(ActionResultType.NOTHING);

        UserGameStateService userGameStateService = new UserGameStateService();

        QuestService questService = new QuestService();

        activityRule.getScenario().onActivity(activity -> {

            activity.setFirebaseAuth(mockAuth);
            userService.setMainActivity(activity);
            workoutService.setMainActivity(activity);
            userGameStateService.setMainActivity(activity);
            questService.setMainActivity(activity);
            activity.setUserService(userService);
            activity.setWorkoutService(workoutService);
            activity.setDeviceService(DeviceServiceType.PERMISSION,mockPermissionDeviceService);
            activity.setDeviceService(DeviceServiceType.LOCATION,mockLocationDeviceService);
            activity.setUserGameStateService(userGameStateService);
            activity.getExploreActionService().setRandomActionResultTypeGenerator(mockRandomActionResultTypeGenerator);
            activity.setQuestService(questService);
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
        when(mockGameStateDocumentSnapshot.get(eq("gameLocation"), eq(String.class))).thenReturn(GameLocationService.HILLS);
        when(mockGameStateDocumentSnapshot.get(eq("savedDistance"), eq(Double.class))).thenReturn(5500.0);
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

        //Set Up Quest Database

        CollectionReference mockQuestCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockFirestore.collection(eq("quest"))).thenReturn(mockQuestCollectionReference);
        DocumentReference mockQuestDocument = Mockito.mock(DocumentReference.class);
        when(mockQuestCollectionReference.document(eq("testUserId"))).thenReturn(mockQuestDocument);

        CollectionReference mockQuestListCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockQuestDocument.collection(eq("quests"))).thenReturn(mockQuestListCollectionReference);
        Task<QuerySnapshot> mockQueryTask = Mockito.mock(Task.class);
        when(mockQuestListCollectionReference.get()).thenReturn(mockQueryTask);
        ArgumentCaptor<OnCompleteListener<QuerySnapshot>> onCompleteListenerArgumentCaptorQuests = ArgumentCaptor.forClass(OnCompleteListener.class);
        when(mockQueryTask.isSuccessful()).thenReturn(true);
        List<QueryDocumentSnapshot> snapshots = new ArrayList<>();

        QuerySnapshot querySnapshot = Mockito.mock(QuerySnapshot.class);
        when(querySnapshot.iterator()).thenReturn(snapshots.iterator());
        when(mockQueryTask.getResult()).thenReturn(querySnapshot);

        QuestFirestoreDatabase questFirestoreDatabase = new QuestFirestoreDatabase(mockFirestore);
        QuestRepository questRepository = new QuestRepository(questService, questFirestoreDatabase);
        questService.setQuestRepository(questRepository);

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
        verify(mockGameStateReadTask).addOnCompleteListener(onCompleteListenerArgumentCaptorReadGameState.capture());
        onCompleteListenerArgumentCaptorReadGameState.getValue().onComplete(mockGameStateReadTask);

        onView(withId(R.id.dialog_card_view)).check(matches(not(isDisplayed())));
        onView(withId(R.id.marsh_button)).perform(click());

        verify(mockQueryTask).addOnCompleteListener(onCompleteListenerArgumentCaptorQuests.capture());
        onCompleteListenerArgumentCaptorQuests.getValue().onComplete(mockQueryTask);
        onView(withId(R.id.nothing_found_card_view)).check(matches(isDisplayed()));
    }


    public void mountainPassTest() throws InterruptedException {

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
        RandomActionResultTypeGenerator mockRandomActionResultTypeGenerator = Mockito.mock(RandomActionResultTypeGenerator.class);
        when(mockRandomActionResultTypeGenerator.getRandomActionResult(GameLocationService.MOUNTAIN_PASS)).thenReturn(ActionResultType.NOTHING);

        UserGameStateService userGameStateService = new UserGameStateService();

        QuestService questService = new QuestService();

        activityRule.getScenario().onActivity(activity -> {

            activity.setFirebaseAuth(mockAuth);
            userService.setMainActivity(activity);
            workoutService.setMainActivity(activity);
            userGameStateService.setMainActivity(activity);
            questService.setMainActivity(activity);
            activity.setUserService(userService);
            activity.setWorkoutService(workoutService);
            activity.setDeviceService(DeviceServiceType.PERMISSION,mockPermissionDeviceService);
            activity.setDeviceService(DeviceServiceType.LOCATION,mockLocationDeviceService);
            activity.setUserGameStateService(userGameStateService);
            activity.getExploreActionService().setRandomActionResultTypeGenerator(mockRandomActionResultTypeGenerator);
            activity.setQuestService(questService);
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
        when(mockGameStateDocumentSnapshot.get(eq("gameLocation"), eq(String.class))).thenReturn(GameLocationService.MOUNTAIN_PASS);
        when(mockGameStateDocumentSnapshot.get(eq("savedDistance"), eq(Double.class))).thenReturn(5500.0);
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

        //Set Up Quest Database

        CollectionReference mockQuestCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockFirestore.collection(eq("quest"))).thenReturn(mockQuestCollectionReference);
        DocumentReference mockQuestDocument = Mockito.mock(DocumentReference.class);
        when(mockQuestCollectionReference.document(eq("testUserId"))).thenReturn(mockQuestDocument);

        CollectionReference mockQuestListCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockQuestDocument.collection(eq("quests"))).thenReturn(mockQuestListCollectionReference);
        Task<QuerySnapshot> mockQueryTask = Mockito.mock(Task.class);
        when(mockQuestListCollectionReference.get()).thenReturn(mockQueryTask);
        ArgumentCaptor<OnCompleteListener<QuerySnapshot>> onCompleteListenerArgumentCaptorQuests = ArgumentCaptor.forClass(OnCompleteListener.class);
        when(mockQueryTask.isSuccessful()).thenReturn(true);
        List<QueryDocumentSnapshot> snapshots = new ArrayList<>();

        QuerySnapshot querySnapshot = Mockito.mock(QuerySnapshot.class);
        when(querySnapshot.iterator()).thenReturn(snapshots.iterator());
        when(mockQueryTask.getResult()).thenReturn(querySnapshot);

        QuestFirestoreDatabase questFirestoreDatabase = new QuestFirestoreDatabase(mockFirestore);
        QuestRepository questRepository = new QuestRepository(questService, questFirestoreDatabase);
        questService.setQuestRepository(questRepository);

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
        verify(mockGameStateReadTask).addOnCompleteListener(onCompleteListenerArgumentCaptorReadGameState.capture());
        onCompleteListenerArgumentCaptorReadGameState.getValue().onComplete(mockGameStateReadTask);

        onView(withId(R.id.dialog_card_view)).check(matches(not(isDisplayed())));
        onView(withId(R.id.marsh_button)).perform(click());

        verify(mockQueryTask).addOnCompleteListener(onCompleteListenerArgumentCaptorQuests.capture());
        onCompleteListenerArgumentCaptorQuests.getValue().onComplete(mockQueryTask);
        onView(withId(R.id.nothing_found_card_view)).check(matches(isDisplayed()));
    }

    @Test
    public void farmlandsTest() throws InterruptedException {

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
        RandomActionResultTypeGenerator mockRandomActionResultTypeGenerator = Mockito.mock(RandomActionResultTypeGenerator.class);
        when(mockRandomActionResultTypeGenerator.getRandomActionResult(GameLocationService.FARMLANDS)).thenReturn(ActionResultType.NOTHING);

        UserGameStateService userGameStateService = new UserGameStateService();

        QuestService questService = new QuestService();

        activityRule.getScenario().onActivity(activity -> {

            activity.setFirebaseAuth(mockAuth);
            userService.setMainActivity(activity);
            workoutService.setMainActivity(activity);
            userGameStateService.setMainActivity(activity);
            questService.setMainActivity(activity);
            activity.setUserService(userService);
            activity.setWorkoutService(workoutService);
            activity.setDeviceService(DeviceServiceType.PERMISSION,mockPermissionDeviceService);
            activity.setDeviceService(DeviceServiceType.LOCATION,mockLocationDeviceService);
            activity.setUserGameStateService(userGameStateService);
            activity.getExploreActionService().setRandomActionResultTypeGenerator(mockRandomActionResultTypeGenerator);
            activity.setQuestService(questService);
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
        when(mockGameStateDocumentSnapshot.get(eq("gameLocation"), eq(String.class))).thenReturn(GameLocationService.FARMLANDS);
        when(mockGameStateDocumentSnapshot.get(eq("savedDistance"), eq(Double.class))).thenReturn(5500.0);
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

        //Set Up Quest Database

        CollectionReference mockQuestCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockFirestore.collection(eq("quest"))).thenReturn(mockQuestCollectionReference);
        DocumentReference mockQuestDocument = Mockito.mock(DocumentReference.class);
        when(mockQuestCollectionReference.document(eq("testUserId"))).thenReturn(mockQuestDocument);

        CollectionReference mockQuestListCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockQuestDocument.collection(eq("quests"))).thenReturn(mockQuestListCollectionReference);
        Task<QuerySnapshot> mockQueryTask = Mockito.mock(Task.class);
        when(mockQuestListCollectionReference.get()).thenReturn(mockQueryTask);
        ArgumentCaptor<OnCompleteListener<QuerySnapshot>> onCompleteListenerArgumentCaptorQuests = ArgumentCaptor.forClass(OnCompleteListener.class);
        when(mockQueryTask.isSuccessful()).thenReturn(true);
        List<QueryDocumentSnapshot> snapshots = new ArrayList<>();

        QuerySnapshot querySnapshot = Mockito.mock(QuerySnapshot.class);
        when(querySnapshot.iterator()).thenReturn(snapshots.iterator());
        when(mockQueryTask.getResult()).thenReturn(querySnapshot);

        QuestFirestoreDatabase questFirestoreDatabase = new QuestFirestoreDatabase(mockFirestore);
        QuestRepository questRepository = new QuestRepository(questService, questFirestoreDatabase);
        questService.setQuestRepository(questRepository);

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
        verify(mockGameStateReadTask).addOnCompleteListener(onCompleteListenerArgumentCaptorReadGameState.capture());
        onCompleteListenerArgumentCaptorReadGameState.getValue().onComplete(mockGameStateReadTask);

        onView(withId(R.id.dialog_card_view)).check(matches(not(isDisplayed())));
        onView(withId(R.id.inn_button)).perform(scrollTo());
        onView(withId(R.id.inn_button)).perform(click());

        verify(mockQueryTask).addOnCompleteListener(onCompleteListenerArgumentCaptorQuests.capture());
        onCompleteListenerArgumentCaptorQuests.getValue().onComplete(mockQueryTask);
        onView(withId(R.id.dialog_card_view)).check(matches(isDisplayed()));
    }
}
