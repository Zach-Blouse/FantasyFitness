package com.zblouse.fantasyfitness.workout;

import com.zblouse.fantasyfitness.core.DomainService;
import com.zblouse.fantasyfitness.core.Repository;


import java.util.Map;

public class WorkoutRecordRepository implements Repository<WorkoutRecord> {

    private final WorkoutRecordFirestoreDatabase workoutRecordFirestoreDatabase;
    private final DomainService<WorkoutRecord> workoutRecordDomainService;

    public WorkoutRecordRepository(DomainService<WorkoutRecord> workoutRecordDomainService){
        this.workoutRecordDomainService = workoutRecordDomainService;
        this.workoutRecordFirestoreDatabase = new WorkoutRecordFirestoreDatabase();
    }

    public WorkoutRecordRepository(DomainService<WorkoutRecord> workoutRecordDomainService, WorkoutRecordFirestoreDatabase workoutRecordFirestoreDatabase){
        this.workoutRecordDomainService = workoutRecordDomainService;
        this.workoutRecordFirestoreDatabase = workoutRecordFirestoreDatabase;
    }

    public void writeWorkoutRecord(WorkoutRecord workoutRecord, Map<String, Object> metadata){
        workoutRecordFirestoreDatabase.write(workoutRecord,this,metadata);
    }

    public void readWorkoutRecord(String userId, Map<String, Object> metadata){
        workoutRecordFirestoreDatabase.read(userId,this,metadata);
    }

    public void updateWorkoutRecord(String userId, long timeMs, WorkoutRecordDistance distanceToUpdate, Map<String,Object> metadata){
        switch(distanceToUpdate){
            case MILE:
                workoutRecordFirestoreDatabase.updateField(userId,WorkoutRecordFirestoreDatabase.MILE_RECORD_KEY,timeMs,this,metadata);
                break;
            case FIVE_K:
                workoutRecordFirestoreDatabase.updateField(userId,WorkoutRecordFirestoreDatabase.FIVE_K_RECORD_KEY,timeMs,this,metadata);
                break;
            case TEN_K:
                workoutRecordFirestoreDatabase.updateField(userId,WorkoutRecordFirestoreDatabase.TEN_K_RECORD_KEY,timeMs,this,metadata);
                break;
            case TWENTY_FIVE_K:
                workoutRecordFirestoreDatabase.updateField(userId,WorkoutRecordFirestoreDatabase.TWENTY_FIVE_K_RECORD_KEY,timeMs,this,metadata);
                break;
            case MARATHON:
                workoutRecordFirestoreDatabase.updateField(userId,WorkoutRecordFirestoreDatabase.MARATHON_RECORD_KEY,timeMs,this,metadata);
                break;
        }
    }

    @Override
    public void readCallback(WorkoutRecord workoutRecord, Map<String, Object> metadata) {
        workoutRecordDomainService.repositoryResponse(workoutRecord,metadata);
    }

    @Override
    public void writeCallback(WorkoutRecord workoutRecord, Map<String, Object> metadata) {
        workoutRecordDomainService.repositoryResponse(workoutRecord,metadata);
    }

    @Override
    public void updateCallback(boolean success, Map<String, Object> metadata) {
        if(success){
            workoutRecordDomainService.repositoryResponse(null,metadata);
        }
        //TODO HANDLE FIREBASE ERROR
    }
}
