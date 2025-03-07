package com.zblouse.fantasyfitness.workout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zblouse.fantasyfitness.R;
import com.zblouse.fantasyfitness.activity.DeviceServiceType;
import com.zblouse.fantasyfitness.activity.LocationForegroundDeviceService;
import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.core.AuthenticationRequiredFragment;
import com.zblouse.fantasyfitness.core.Event;
import com.zblouse.fantasyfitness.core.EventListener;
import com.zblouse.fantasyfitness.core.EventType;

import java.util.HashMap;
import java.util.Locale;

public class WorkoutRecordsFragment extends AuthenticationRequiredFragment implements EventListener {

    private LinearLayout layout;
    private TextView recordsTextView;

    private static final int MILLIS_IN_HOUR = 3600000;
    private static final int MILLIS_IN_MINUTE = 60000;
    private static final int MILLIS_IN_SECOND = 1000;

    public WorkoutRecordsFragment(){
        super(R.layout.workout_records_fragment);
    }

    public WorkoutRecordsFragment(MainActivity mainActivity) {
        super(R.layout.workout_records_fragment, mainActivity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.OnCreateView();
        mainActivity.showNavigation();
        layout = (LinearLayout) inflater.inflate(R.layout.workout_records_fragment,container,false);
        mainActivity.getWorkoutRecordService().fetchWorkoutRecords(new HashMap<>());

        recordsTextView = layout.findViewById(R.id.workout_records_display);
        return layout;
    }

    @Override
    public void publishEvent(Event event) {
        if(event.getEventType().equals(EventType.WORKOUT_RECORD_READ_EVENT)){
            WorkoutRecordReadEvent workoutRecordReadEvent = (WorkoutRecordReadEvent)event;
            String mileRecordString = "No Record";
            String fiveKRecordString = "No Record";
            String tenKRecordString = "No Record";
            String twentyFiveKRecordString = "No Record";
            String marathonRecordString = "No Record";
            if(workoutRecordReadEvent.getWorkoutRecord().getMileRecord() < Long.MAX_VALUE){
                mileRecordString = formatTime(workoutRecordReadEvent.getWorkoutRecord().getMileRecord());
            }
            if(workoutRecordReadEvent.getWorkoutRecord().getFiveKRecord() < Long.MAX_VALUE){
                fiveKRecordString = formatTime(workoutRecordReadEvent.getWorkoutRecord().getFiveKRecord());
            }
            if(workoutRecordReadEvent.getWorkoutRecord().getTenKRecord() < Long.MAX_VALUE){
                tenKRecordString = formatTime(workoutRecordReadEvent.getWorkoutRecord().getTenKRecord());
            }
            if(workoutRecordReadEvent.getWorkoutRecord().getTwentyFiveKRecord() < Long.MAX_VALUE){
                twentyFiveKRecordString = formatTime(workoutRecordReadEvent.getWorkoutRecord().getTwentyFiveKRecord());
            }
            if(workoutRecordReadEvent.getWorkoutRecord().getMarathonRecord() < Long.MAX_VALUE){
                marathonRecordString = formatTime(workoutRecordReadEvent.getWorkoutRecord().getMarathonRecord());
            }

            String recordDisplayString = "1 Mile Record " + mileRecordString
                    + "\n5km Record " + fiveKRecordString
                    + "\n10km Record " + tenKRecordString
                    + "\n25km Record " + twentyFiveKRecordString
                    + "\nMarathon Record " + marathonRecordString;
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    recordsTextView.setText(recordDisplayString);
                }
            });

        }
    }

    private String formatTime(long timeMillis){
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

}
