package com.zblouse.fantasyfitness.workout;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.zblouse.fantasyfitness.core.FirestoreDatabase;
import com.zblouse.fantasyfitness.core.Repository;

import java.util.HashMap;
import java.util.Map;

public class WorkoutRecordFirestoreDatabase extends FirestoreDatabase {

    private static final String COLLECTION = "workoutRecords";

    public static final String MILE_RECORD_KEY = "mileRecord";
    public static final String FIVE_K_RECORD_KEY = "fiveKRecord";
    public static final String TEN_K_RECORD_KEY = "tenKRecord";
    public static final String TWENTY_FIVE_K_RECORD_KEY = "twentyFiveKRecord";
    public static final String MARATHON_RECORD_KEY = "marathonRecord";

    public WorkoutRecordFirestoreDatabase(){
        super();
    }

    public WorkoutRecordFirestoreDatabase(FirebaseFirestore firestore){
        super(firestore);
    }

    public void write(WorkoutRecord workoutRecord, Repository<WorkoutRecord> workoutRecordRepository, Map<String, Object> metadata){

        Map<String, Object> newWorkoutRecord = new HashMap<>();
        newWorkoutRecord.put(MILE_RECORD_KEY,workoutRecord.getMileRecord());
        newWorkoutRecord.put(FIVE_K_RECORD_KEY,workoutRecord.getFiveKRecord());
        newWorkoutRecord.put(TEN_K_RECORD_KEY,workoutRecord.getTenKRecord());
        newWorkoutRecord.put(TWENTY_FIVE_K_RECORD_KEY, workoutRecord.getTwentyFiveKRecord());
        newWorkoutRecord.put(MARATHON_RECORD_KEY, workoutRecord.getMarathonRecord());
        firestore.collection(COLLECTION).document(workoutRecord.getUserId()).set(newWorkoutRecord).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    workoutRecordRepository.writeCallback(workoutRecord, metadata);
                } else {
                    workoutRecordRepository.writeCallback(null, metadata);
                }
            }
        });

    }

    public void updateField(String userId, String fieldId, Object newValue, Repository<WorkoutRecord> repository, Map<String, Object> metadata){
        metadata.put(DATABASE_UPDATE_FIELD, fieldId);
        metadata.put(DATABASE_UPDATE_VALUE, newValue);
        firestore.collection(COLLECTION).document(userId).update(fieldId, newValue).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    repository.updateCallback(true, metadata);
                } else {
                    repository.updateCallback(false, metadata);
                }
            }
        });
    }

    public void read(String userId, Repository<WorkoutRecord> repository, Map<String, Object> metadata){
        firestore.collection(COLLECTION).document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot result = task.getResult();
                    if(result.exists()) {
                        WorkoutRecord workoutRecord = new WorkoutRecord(userId, result.get(MILE_RECORD_KEY, Long.class), result.get(FIVE_K_RECORD_KEY, Long.class), result.get(TEN_K_RECORD_KEY, Long.class), result.get(TWENTY_FIVE_K_RECORD_KEY, Long.class), result.get(MARATHON_RECORD_KEY, Long.class));
                        repository.readCallback(workoutRecord, metadata);
                    } else {
                        repository.readCallback(null, metadata);
                    }
                } else {
                    //TODO handle firebase error states
                    repository.readCallback(null, metadata);
                }
            }
        });
    }
}
