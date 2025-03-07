package com.zblouse.fantasyfitness.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.R;
import com.zblouse.fantasyfitness.core.AuthenticationRequiredFragment;
import com.zblouse.fantasyfitness.user.CreateAccountFragment;
import com.zblouse.fantasyfitness.workout.WorkoutRecordsFragment;

public class SettingsFragment  extends AuthenticationRequiredFragment {

    public SettingsFragment(){
        super(R.layout.settings_fragment);
    }

    public SettingsFragment(MainActivity mainActivity){
        super(R.layout.settings_fragment, mainActivity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.OnCreateView();
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.settings_fragment,container,false);
        mainActivity.showNavigation();
        Button logoutButton = layout.findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.logout();
            }
        });
        Button workoutRecordsButton = layout.findViewById(R.id.workout_records_button);
        workoutRecordsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new WorkoutRecordsFragment(mainActivity)).commit();
            }
        });

        return layout;
    }
}
