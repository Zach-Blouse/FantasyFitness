package com.zblouse.fantasyfitness.workout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.R;
import com.zblouse.fantasyfitness.core.AuthenticationRequiredFragment;
import com.zblouse.fantasyfitness.core.Event;
import com.zblouse.fantasyfitness.core.EventListener;
import com.zblouse.fantasyfitness.core.EventType;

import java.util.Locale;

public class WorkoutFragment extends AuthenticationRequiredFragment implements EventListener {

    private LinearLayout layout;
    private Button startWorkoutButton;
    private Button pauseWorkoutButton;
    private Button stopWorkoutButton;
    private TextView workoutTimeTextView;
    private TextView workoutDistanceTextView;

    private static final int MILLIS_IN_HOUR = 3600000;
    private static final int MILLIS_IN_MINUTE = 60000;
    private static final int MILLIS_IN_SECOND = 1000;

    public WorkoutFragment() {
        super(R.layout.workout_fragment);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.OnCreateView();
        ((MainActivity)getActivity()).showNavigation();
        layout = (LinearLayout) inflater.inflate(R.layout.workout_fragment,container,false);
        workoutTimeTextView = layout.findViewById(R.id.workout_time);
        workoutDistanceTextView = layout.findViewById(R.id.workout_distance);
        startWorkoutButton = layout.findViewById(R.id.start_workout_button);
        pauseWorkoutButton = layout.findViewById(R.id.pause_workout_button);
        stopWorkoutButton = layout.findViewById(R.id.stop_workout_button);
        initialButtonSetup();
        ((ViewGroup)layout).removeView(pauseWorkoutButton);
        ((ViewGroup)layout).removeView(stopWorkoutButton);

        return layout;
    }

    @Override
    public void publishEvent(Event event) {
        if(event.getEventType().equals(EventType.TIME_UPDATE_EVENT)){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    workoutTimeTextView.setText(formatTimeDisplay(((WorkoutTimeUpdateEvent)event).getTime()));
                }
            });

        } else if(event.getEventType().equals(EventType.WORKOUT_DISTANCE_UPDATE_EVENT)){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    workoutDistanceTextView.setText(formatDistanceDisplay(((WorkoutDistanceUpdateEvent)event).getDistanceMeters()));
                }
            });
        }
    }

    private void startWorkout(){
        if(((MainActivity)getActivity()).getWorkoutService().startWorkout()) {
            ((MainActivity) getActivity()).hideNavigation();

            ((ViewGroup) layout).removeView(startWorkoutButton);
            ((ViewGroup) layout).addView(pauseWorkoutButton);
            ((ViewGroup) layout).addView(stopWorkoutButton);
        }
    }

    private void pauseWorkout(){
        ((MainActivity)getActivity()).getWorkoutService().pause();
        pauseWorkoutButton.setText(R.string.resume_workout);
        pauseWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unpauseWorkout();
            }
        });

    }

    private void unpauseWorkout(){
        ((MainActivity)getActivity()).getWorkoutService().unpause();
        pauseWorkoutButton.setText(R.string.pause_workout);
        pauseWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseWorkout();
            }
        });
    }

    private void stopWorkout(){
        ((MainActivity)getActivity()).getWorkoutService().pause();
        pauseWorkoutButton.setText(R.string.resume_workout);
        pauseWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unStopWorkout();
            }
        });

        stopWorkoutButton.setText(R.string.complete_workout);
        stopWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishWorkout();
            }
        });
    }

    private void unStopWorkout(){
        unpauseWorkout();
        stopWorkoutButton.setText(R.string.stop_workout);
        stopWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopWorkout();
            }
        });
    }

    private void finishWorkout(){
        ((MainActivity)getActivity()).showNavigation();

        ((MainActivity)getActivity()).getWorkoutService().stopWorkout();
        initialButtonSetup();
    }

    private String formatTimeDisplay(long timeMillis){
        long hours = timeMillis/MILLIS_IN_HOUR;
        long remainingMillis = timeMillis - (hours*MILLIS_IN_HOUR);
        long minutes = remainingMillis/MILLIS_IN_MINUTE;
        remainingMillis = remainingMillis - (minutes*MILLIS_IN_MINUTE);
        long seconds = remainingMillis/MILLIS_IN_SECOND;
        if (hours > 0) {
            return String.format(Locale.US, "%d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format(Locale.US, "%02d:%02d", minutes, seconds);
        }
    }

    private String formatDistanceDisplay(double distanceMeters){
        double distanceKm = distanceMeters/1000;
        return String.format(Locale.US, "%.2f",distanceKm) + " km";
    }

    private void initialButtonSetup(){
        if(((ViewGroup)layout).findViewById(startWorkoutButton.getId()) == null){
            ((ViewGroup)layout).addView(startWorkoutButton);

        }

        if(((ViewGroup)layout).findViewById(pauseWorkoutButton.getId()) == null){
            ((ViewGroup)layout).addView(pauseWorkoutButton);

        }

        if(((ViewGroup)layout).findViewById(stopWorkoutButton.getId()) == null){
            ((ViewGroup)layout).addView(stopWorkoutButton);

        }
        startWorkoutButton.setText(R.string.start_workout);
        startWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startWorkout();
            }
        });
        pauseWorkoutButton.setText(R.string.pause_workout);
        pauseWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseWorkout();
            }
        });
        stopWorkoutButton.setText(R.string.stop_workout);
        stopWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopWorkout();
            }
        });

        ((ViewGroup)layout).removeView(pauseWorkoutButton);
        ((ViewGroup)layout).removeView(stopWorkoutButton);
    }
}
