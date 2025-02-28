package com.zblouse.fantasyfitness.workout;

import com.zblouse.fantasyfitness.R;
import com.zblouse.fantasyfitness.activity.DeviceServiceType;
import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.activity.NotificationDeviceService;
import com.zblouse.fantasyfitness.core.DomainService;

import java.util.HashMap;
import java.util.Map;

public class WorkoutRecordService implements DomainService<WorkoutRecord> {

    private MainActivity mainActivity;
    private WorkoutRecordRepository workoutRecordRepository;

    private static final String CHECK_FOR_RECORDS = "checkForRecords";
    private static final String INITIALIZE_RECORDS = "initializeRecords";
    private static final String FETCH_RECORDS = "fetchRecords";
    private static final String WORKOUT_TIME = "workoutTime";
    private static final String WORKOUT_RECORD_DISTANCE = "workoutRecordDistance";

    public WorkoutRecordService(MainActivity mainActivity){
        this.mainActivity = mainActivity;
        this.workoutRecordRepository = new WorkoutRecordRepository(this);
    }

    public WorkoutRecordService(MainActivity mainActivity, WorkoutRecordRepository workoutRecordRepository){
        this.mainActivity = mainActivity;
        this.workoutRecordRepository = workoutRecordRepository;
    }

    public WorkoutRecordService(){

    }

    public void setMainActivity(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    public void setWorkoutRecordRepository(WorkoutRecordRepository workoutRecordRepository){
        this.workoutRecordRepository = workoutRecordRepository;
    }

    public void fetchWorkoutRecords(Map<String, Object> metadata){

        metadata.put(ORIGIN_FUNCTION,FETCH_RECORDS);
        workoutRecordRepository.readWorkoutRecord(mainActivity.getCurrentUser().getUid(),metadata);

    }

    public void checkForRecords(long workoutTime, double workoutDistance){
        Map<String, Object> metadata = new HashMap<>();
        if(workoutDistance >= 1609.344 && workoutDistance < 1635){
            metadata.put(WORKOUT_RECORD_DISTANCE, WorkoutRecordDistance.MILE);
        } else if(workoutDistance >= 5000 && workoutDistance < 5025){
            metadata.put(WORKOUT_RECORD_DISTANCE, WorkoutRecordDistance.FIVE_K);
        } else if(workoutDistance >= 10000 && workoutDistance < 10025){
            metadata.put(WORKOUT_RECORD_DISTANCE, WorkoutRecordDistance.TEN_K);
        } else if(workoutDistance >= 25000 && workoutDistance < 25025){
            metadata.put(WORKOUT_RECORD_DISTANCE, WorkoutRecordDistance.TWENTY_FIVE_K);
        } else if(workoutDistance >= 42194.988 && workoutDistance < 42250){
            metadata.put(WORKOUT_RECORD_DISTANCE, WorkoutRecordDistance.MARATHON);
        } else {
            return;
        }

        metadata.put(ORIGIN_FUNCTION, CHECK_FOR_RECORDS);
        metadata.put(WORKOUT_TIME, workoutTime);
        workoutRecordRepository.readWorkoutRecord(mainActivity.getCurrentUser().getUid(),metadata);
    }

    public void initializeWorkoutRecords(){
        Map<String, Object> metadata = new HashMap<>();
        metadata.put(ORIGIN_FUNCTION, INITIALIZE_RECORDS);
        WorkoutRecord initialWorkoutRecord = new WorkoutRecord(mainActivity.getCurrentUser().getUid(),Long.MAX_VALUE,Long.MAX_VALUE,Long.MAX_VALUE,Long.MAX_VALUE,Long.MAX_VALUE);
        workoutRecordRepository.writeWorkoutRecord(initialWorkoutRecord,metadata);
    }

    @Override
    public void repositoryResponse(WorkoutRecord workoutRecord, Map<String, Object> metadata) {
        if(metadata.containsKey(ORIGIN_FUNCTION)) {
            if (metadata.get(ORIGIN_FUNCTION).equals(CHECK_FOR_RECORDS)) {
                if (metadata.containsKey(WORKOUT_RECORD_DISTANCE) && metadata.containsKey(WORKOUT_TIME)) {
                    long workoutTime = (long)metadata.get(WORKOUT_TIME);
                    switch ((WorkoutRecordDistance) metadata.get(WORKOUT_RECORD_DISTANCE)) {
                        case MILE:
                            if (workoutTime < workoutRecord.getMileRecord()){
                                ((NotificationDeviceService)mainActivity.getDeviceService(DeviceServiceType.NOTIFICATION)).sendNotification("New Record","Congratulations on your new record Mile time!", R.drawable.run);
                                workoutRecordRepository.updateWorkoutRecord(mainActivity.getCurrentUser().getUid(),workoutTime,WorkoutRecordDistance.MILE,new HashMap<>());
                            }
                            break;
                        case FIVE_K:
                            if (workoutTime < workoutRecord.getFiveKRecord()){
                                ((NotificationDeviceService)mainActivity.getDeviceService(DeviceServiceType.NOTIFICATION)).sendNotification("New Record","Congratulations on your new record 5k time!", R.drawable.run);
                                workoutRecordRepository.updateWorkoutRecord(mainActivity.getCurrentUser().getUid(),workoutTime,WorkoutRecordDistance.FIVE_K,new HashMap<>());
                            }
                            break;
                        case TEN_K:
                            if (workoutTime < workoutRecord.getTenKRecord()){
                                ((NotificationDeviceService)mainActivity.getDeviceService(DeviceServiceType.NOTIFICATION)).sendNotification("New Record","Congratulations on your new record 10k time!", R.drawable.run);
                                workoutRecordRepository.updateWorkoutRecord(mainActivity.getCurrentUser().getUid(),workoutTime,WorkoutRecordDistance.TEN_K,new HashMap<>());
                            }
                            break;
                        case TWENTY_FIVE_K:
                            if (workoutTime < workoutRecord.getTwentyFiveKRecord()){
                                ((NotificationDeviceService)mainActivity.getDeviceService(DeviceServiceType.NOTIFICATION)).sendNotification("New Record","Congratulations on your new record 25k time!", R.drawable.run);
                                workoutRecordRepository.updateWorkoutRecord(mainActivity.getCurrentUser().getUid(),workoutTime,WorkoutRecordDistance.TWENTY_FIVE_K,new HashMap<>());
                            }
                            break;
                        case MARATHON:
                            if (workoutTime < workoutRecord.getMarathonRecord()){
                                ((NotificationDeviceService)mainActivity.getDeviceService(DeviceServiceType.NOTIFICATION)).sendNotification("New Record","Congratulations on your new record Marathon time!", R.drawable.run);
                                workoutRecordRepository.updateWorkoutRecord(mainActivity.getCurrentUser().getUid(),workoutTime,WorkoutRecordDistance.MARATHON,new HashMap<>());
                            }
                            break;
                    }
                }
            } else if (metadata.get(ORIGIN_FUNCTION).equals(FETCH_RECORDS)){
                mainActivity.publishEvent(new WorkoutRecordReadEvent(workoutRecord,metadata));
            }
        }
    }

    @Override
    public void interDomainServiceResponse(Object responseObject, Map<String, Object> metadata) {

    }
}
