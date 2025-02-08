package com.zblouse.fantasyfitness.workout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.zblouse.fantasyfitness.MainActivity;
import com.zblouse.fantasyfitness.R;
import com.zblouse.fantasyfitness.core.AuthenticationRequiredFragment;
import com.zblouse.fantasyfitness.core.Event;
import com.zblouse.fantasyfitness.core.EventListener;

public class WorkoutFragment extends AuthenticationRequiredFragment implements EventListener {

    private LinearLayout layout;
    private Button startWorkoutButton;
    private Button pauseWorkoutButton;
    private Button stopWorkoutButton;

    public WorkoutFragment() {
        super(R.layout.workout_fragment);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.OnCreateView();
        ((MainActivity)getActivity()).showNavigation();
        layout = (LinearLayout) inflater.inflate(R.layout.workout_fragment,container,false);
        startWorkoutButton = layout.findViewById(R.id.start_workout_button);
        startWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startWorkout();
            }
        });

        pauseWorkoutButton = layout.findViewById(R.id.pause_workout_button);
        pauseWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseWorkout();
            }
        });

        stopWorkoutButton = layout.findViewById(R.id.stop_workout_button);
        stopWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopWorkout();
            }
        });

        ((ViewGroup)layout).removeView(pauseWorkoutButton);
        ((ViewGroup)layout).removeView(stopWorkoutButton);

        return layout;
    }

    @Override
    public void publishEvent(Event event) {

    }

    private void startWorkout(){
        ((MainActivity)getActivity()).hideNavigation();
        ((ViewGroup)layout).removeView(startWorkoutButton);
        ((ViewGroup)layout).addView(pauseWorkoutButton);
        ((ViewGroup)layout).addView(stopWorkoutButton);
    }

    private void pauseWorkout(){

    }

    private void stopWorkout(){
        ((MainActivity)getActivity()).showNavigation();
        ((ViewGroup)layout).addView(startWorkoutButton);
        ((ViewGroup)layout).removeView(pauseWorkoutButton);
        ((ViewGroup)layout).removeView(stopWorkoutButton);
    }
}
