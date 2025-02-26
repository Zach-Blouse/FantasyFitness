package com.zblouse.fantasyfitness.workout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zblouse.fantasyfitness.activity.DeviceServiceType;
import com.zblouse.fantasyfitness.activity.LocationForegroundDeviceService;
import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.R;
import com.zblouse.fantasyfitness.core.AuthenticationRequiredFragment;
import com.zblouse.fantasyfitness.core.Event;
import com.zblouse.fantasyfitness.core.EventListener;
import com.zblouse.fantasyfitness.core.EventType;

import java.util.HashMap;
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

    public WorkoutFragment(MainActivity mainActivity) {
        super(R.layout.workout_fragment, mainActivity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.OnCreateView();
        mainActivity.showNavigation();
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
        if(event.getEventType().equals(EventType.WORKOUT_UPDATE_EVENT)){
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    workoutTimeTextView.setText(formatTimeDisplay(((WorkoutUpdateEvent)event).getTime()));
                    workoutDistanceTextView.setText(formatDistanceDisplay(((WorkoutUpdateEvent)event).getDistanceMeters()));
                    ((LocationForegroundDeviceService)mainActivity.getDeviceService(DeviceServiceType.LOCATION_FOREGROUND))
                            .updateLocationForegroundServiceNotification(formatNotificationDisplay(((WorkoutUpdateEvent)event).getTime(),((WorkoutUpdateEvent)event).getDistanceMeters()));
                }
            });

        } else if(event.getEventType().equals(EventType.WORKOUT_COMPLETE_EVENT)){
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    workoutTimeTextView.setText(formatTimeDisplay(((WorkoutCompleteEvent)event).getWorkoutTime()));
                    workoutDistanceTextView.setText(formatDistanceDisplay(((WorkoutCompleteEvent)event).getWorkoutDistanceMeters()));
                    mainActivity.getUserGameStateService().addUserGameDistance(mainActivity.getCurrentUser().getUid(),((WorkoutCompleteEvent)event).getWorkoutDistanceMeters(),new HashMap<>());
                    ((LocationForegroundDeviceService)mainActivity.getDeviceService(DeviceServiceType.LOCATION_FOREGROUND)).stopLocationForegroundService();
                }
            });
        } else if(event.getEventType().equals(EventType.WORKOUT_START_EVENT)){
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ((LocationForegroundDeviceService)mainActivity.getDeviceService(DeviceServiceType.LOCATION_FOREGROUND)).startLocationForegroundService();
                }
            });
        }
    }

    private void startWorkout(){
        if(mainActivity.getWorkoutService().startWorkout()) {
            mainActivity.hideNavigation();

            ((ViewGroup) layout).removeView(startWorkoutButton);
            ((ViewGroup) layout).addView(pauseWorkoutButton);
            ((ViewGroup) layout).addView(stopWorkoutButton);
        }
    }

    private void pauseWorkout(){
        mainActivity.getWorkoutService().pause();
        pauseWorkoutButton.setText(R.string.resume_workout);
        pauseWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unpauseWorkout();
            }
        });

    }

    private void unpauseWorkout(){
        mainActivity.getWorkoutService().unpause();
        pauseWorkoutButton.setText(R.string.pause_workout);
        pauseWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseWorkout();
            }
        });
    }

    private void stopWorkout(){
        mainActivity.getWorkoutService().pause();
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
        mainActivity.showNavigation();

        mainActivity.getWorkoutService().stopWorkout();
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

    private String formatNotificationDisplay(long time, double distance){
        return formatTimeDisplay(time) + " " + formatDistanceDisplay(distance);
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
