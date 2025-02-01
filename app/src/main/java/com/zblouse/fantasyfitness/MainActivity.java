package com.zblouse.fantasyfitness;

import android.os.Bundle;
import android.view.Menu;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.zblouse.fantasyfitness.home.UserHomeFragment;
import com.zblouse.fantasyfitness.settings.SettingsFragment;
import com.zblouse.fantasyfitness.user.LoginFragment;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private BottomNavigationView navigationView;
    private FirebaseUser currentUser;

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

        navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setOnItemSelectedListener(navigationListener);

        firebaseAuth = FirebaseAuth.getInstance();

        currentUser = firebaseAuth.getCurrentUser();
        if(currentUser == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new LoginFragment()).commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new UserHomeFragment()).commit();
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
        } else if (itemId == R.id.action_settings) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new SettingsFragment()).commit();
        } else if (itemId == R.id.action_workout){
        } else if (itemId == R.id.action_map){

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
                .replace(R.id.fragment_container, new LoginFragment()).commit();
    }

    public FirebaseUser getCurrentUser(){
        return this.currentUser;
    }

}