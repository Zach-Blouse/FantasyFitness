package com.zblouse.fantasyfitness.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.zblouse.fantasyfitness.R;
import com.zblouse.fantasyfitness.actions.ExploreActionEvent;
import com.zblouse.fantasyfitness.actions.ExploreActionService;
import com.zblouse.fantasyfitness.combat.CardService;
import com.zblouse.fantasyfitness.core.EventListener;
import com.zblouse.fantasyfitness.dialog.DialogService;
import com.zblouse.fantasyfitness.dialog.DialogSqlDatabase;
import com.zblouse.fantasyfitness.home.UserHomeFragment;
import com.zblouse.fantasyfitness.settings.SettingsFragment;
import com.zblouse.fantasyfitness.user.LoginFragment;
import com.zblouse.fantasyfitness.user.UserGameStateRepository;
import com.zblouse.fantasyfitness.user.UserGameStateService;
import com.zblouse.fantasyfitness.user.UserService;
import com.zblouse.fantasyfitness.core.Event;
import com.zblouse.fantasyfitness.workout.WorkoutFragment;
import com.zblouse.fantasyfitness.workout.WorkoutRecordService;
import com.zblouse.fantasyfitness.workout.WorkoutService;
import com.zblouse.fantasyfitness.world.GameLocationDistanceSqlDatabase;
import com.zblouse.fantasyfitness.world.GameLocationService;
import com.zblouse.fantasyfitness.world.GameLocationSqlDatabase;
import com.zblouse.fantasyfitness.world.GameWorldFragment;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private BottomNavigationView navigationView;
    private FirebaseUser currentUser;

    private UserService userService;
    private WorkoutService workoutService;
    private Map<DeviceServiceType, DeviceService> deviceServices;
    private GameLocationService gameLocationService;
    private UserGameStateService userGameStateService;
    private WorkoutRecordService workoutRecordService;
    private ExploreActionService exploreActionService;
    private DialogService dialogService;
    private CardService cardService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseApp.initializeApp(this);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        deviceServices = new HashMap<>();
        deviceServices.put(DeviceServiceType.LOCATION, new LocationDeviceService(this));
        deviceServices.put(DeviceServiceType.PERMISSION, new PermissionDeviceService(this));
        deviceServices.put(DeviceServiceType.TOAST, new ToastDeviceService(this));
        deviceServices.put(DeviceServiceType.LOCATION_FOREGROUND, new LocationForegroundDeviceService(this));
        deviceServices.put(DeviceServiceType.NOTIFICATION, new NotificationDeviceService(this));

        userService = new UserService(this);
        workoutService = new WorkoutService(this);
        exploreActionService = new ExploreActionService(this);
        cardService = new CardService(this);

        navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setOnItemSelectedListener(navigationListener);
        navigationView.setPadding(0,0,0,0);
        navigationView.setOnApplyWindowInsetsListener(null);

        firebaseAuth = FirebaseAuth.getInstance();

        deleteDatabase(GameLocationSqlDatabase.GAME_LOCATION_DATABASE_NAME);
        deleteDatabase(GameLocationDistanceSqlDatabase.GAME_LOCATION_DISTANCE_DATABASE_NAME);
        deleteDatabase(DialogSqlDatabase.DATABASE_NAME);
        gameLocationService = new GameLocationService(this);
        userGameStateService = new UserGameStateService(this);
        workoutRecordService = new WorkoutRecordService(this);
        dialogService = new DialogService(this);

        gameLocationService.initializeLocationDatabase();
        dialogService.initializeDialogs();
        currentUser = firebaseAuth.getCurrentUser();
        if(savedInstanceState == null) {
            if (currentUser == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new LoginFragment(this)).commit();
            } else {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new UserHomeFragment(this)).commit();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflates the UI elements in the action_bar menu
        getMenuInflater().inflate(R.menu.bottom_navigation_menu, menu);
        getSupportActionBar().hide();
        return true;
    }

    //Called when the bottom navigation view is touched
    private final BottomNavigationView.OnItemSelectedListener navigationListener = item -> {

        int itemId = item.getItemId();
        if (itemId == R.id.action_home) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new UserHomeFragment(this)).commit();
        } else if (itemId == R.id.action_settings) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new SettingsFragment(this)).commit();
        } else if (itemId == R.id.action_workout){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new WorkoutFragment(this)).commit();
        } else if (itemId == R.id.action_map){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new GameWorldFragment(this)).commit();
        }

        return true;
    };

    /**
     * On some fragments(notable the workout fragment) we don't want to display the BottomNavigationView
     */
    public void hideNavigation(){
        navigationView.setVisibility(BottomNavigationView.GONE);
    }

    /**
     * On most fragments we do want to display the BottomNavigationView
     */
    public void showNavigation(){
        navigationView.setVisibility(BottomNavigationView.VISIBLE);
    }

    public void logout(){
        firebaseAuth.signOut();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new LoginFragment(this)).commit();
    }

    public FirebaseUser getCurrentUser(){
        return firebaseAuth.getCurrentUser();
    }

    public UserService getUserService(){
        return this.userService;
    }

    public void setUserService(UserService userService){
        this.userService = userService;
    }

    public WorkoutService getWorkoutService(){
        return this.workoutService;
    }

    public void setWorkoutService(WorkoutService workoutService){
        this.workoutService = workoutService;
    }

    public void publishEvent(Event event){
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if(fragment instanceof EventListener){
            ((EventListener) fragment).publishEvent(event);
        }
    }

    public void setFirebaseAuth(FirebaseAuth firebaseAuth){
        this.firebaseAuth = firebaseAuth;
    }

    public DeviceService getDeviceService(DeviceServiceType deviceServiceType){
        if(deviceServices != null){
            return deviceServices.get(deviceServiceType);
        }
        return null;
    }

    public void setDeviceService(DeviceServiceType type, DeviceService deviceService){
        deviceServices.remove(type);
        deviceServices.put(type,deviceService);
    }

    public void setGameLocationService(GameLocationService gameLocationService){
        this.gameLocationService = gameLocationService;
    }

    public GameLocationService getGameLocationService(){
        return this.gameLocationService;
    }

    public UserGameStateService getUserGameStateService(){
        return this.userGameStateService;
    }

    public void setUserGameStateService(UserGameStateService userGameStateService){
        this.userGameStateService = userGameStateService;
    }

    public WorkoutRecordService getWorkoutRecordService(){
        return this.workoutRecordService;
    }

    public void setWorkoutRecordService(WorkoutRecordService workoutRecordService){
        this.workoutRecordService = workoutRecordService;
    }

    public ExploreActionService getExploreActionService(){
        return this.exploreActionService;
    }

    public void setExploreActionService(ExploreActionService exploreActionService){
        this.exploreActionService = exploreActionService;
    }

    public DialogService getDialogService(){
        return this.dialogService;
    }

    public void setDialogService(DialogService dialogService){
        this.dialogService = dialogService;
    }

    public CardService getCardService(){
        return this.cardService;
    }

    public void setCardService(CardService cardService){
        this.cardService = cardService;
    }

}