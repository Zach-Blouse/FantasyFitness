package com.zblouse.fantasyfitness.workout;

import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

public class WorkoutRecordRepositoryTest {

    @Test
    public void writeWorkoutRecordTest(){
        String testUserId = "testUser1";
        WorkoutRecordService mockWorkoutRecordService = Mockito.mock(WorkoutRecordService.class);
        WorkoutRecordFirestoreDatabase mockWorkoutRecordFirestoreDatabase = Mockito.mock(WorkoutRecordFirestoreDatabase.class);

        WorkoutRecordRepository testedRepository = new WorkoutRecordRepository(mockWorkoutRecordService, mockWorkoutRecordFirestoreDatabase);
        WorkoutRecord testWorkoutRecord = new WorkoutRecord(testUserId,0L,1L,2L,3L,4L);
        Map<String, Object> metadata = new HashMap<>();

        testedRepository.writeWorkoutRecord(testWorkoutRecord, metadata);

        verify(mockWorkoutRecordFirestoreDatabase).write(testWorkoutRecord,testedRepository,metadata);
    }

    @Test
    public void readWorkoutRecordTest(){
        String testUserId = "testUser1";
        WorkoutRecordService mockWorkoutRecordService = Mockito.mock(WorkoutRecordService.class);
        WorkoutRecordFirestoreDatabase mockWorkoutRecordFirestoreDatabase = Mockito.mock(WorkoutRecordFirestoreDatabase.class);

        WorkoutRecordRepository testedRepository = new WorkoutRecordRepository(mockWorkoutRecordService, mockWorkoutRecordFirestoreDatabase);
        WorkoutRecord testWorkoutRecord = new WorkoutRecord(testUserId,0L,1L,2L,3L,4L);
        Map<String, Object> metadata = new HashMap<>();

        testedRepository.readWorkoutRecord(testUserId, metadata);

        verify(mockWorkoutRecordFirestoreDatabase).read(testUserId,testedRepository,metadata);
    }

    @Test
    public void readCallbackTest(){
        String testUserId = "testUser1";
        WorkoutRecordService mockWorkoutRecordService = Mockito.mock(WorkoutRecordService.class);
        WorkoutRecordFirestoreDatabase mockWorkoutRecordFirestoreDatabase = Mockito.mock(WorkoutRecordFirestoreDatabase.class);

        WorkoutRecordRepository testedRepository = new WorkoutRecordRepository(mockWorkoutRecordService, mockWorkoutRecordFirestoreDatabase);
        WorkoutRecord testWorkoutRecord = new WorkoutRecord(testUserId,0L,1L,2L,3L,4L);
        Map<String, Object> metadata = new HashMap<>();

        testedRepository.readCallback(testWorkoutRecord, metadata);

        verify(mockWorkoutRecordService).repositoryResponse(testWorkoutRecord,metadata);
    }

    @Test
    public void writeCallbackTest(){
        String testUserId = "testUser1";
        WorkoutRecordService mockWorkoutRecordService = Mockito.mock(WorkoutRecordService.class);
        WorkoutRecordFirestoreDatabase mockWorkoutRecordFirestoreDatabase = Mockito.mock(WorkoutRecordFirestoreDatabase.class);

        WorkoutRecordRepository testedRepository = new WorkoutRecordRepository(mockWorkoutRecordService, mockWorkoutRecordFirestoreDatabase);
        WorkoutRecord testWorkoutRecord = new WorkoutRecord(testUserId,0L,1L,2L,3L,4L);
        Map<String, Object> metadata = new HashMap<>();

        testedRepository.writeCallback(testWorkoutRecord, metadata);

        verify(mockWorkoutRecordService).repositoryResponse(testWorkoutRecord,metadata);
    }

    @Test
    public void updateCallbackSuccessTest(){
        String testUserId = "testUser1";
        WorkoutRecordService mockWorkoutRecordService = Mockito.mock(WorkoutRecordService.class);
        WorkoutRecordFirestoreDatabase mockWorkoutRecordFirestoreDatabase = Mockito.mock(WorkoutRecordFirestoreDatabase.class);

        WorkoutRecordRepository testedRepository = new WorkoutRecordRepository(mockWorkoutRecordService, mockWorkoutRecordFirestoreDatabase);
        WorkoutRecord testWorkoutRecord = new WorkoutRecord(testUserId,0L,1L,2L,3L,4L);
        Map<String, Object> metadata = new HashMap<>();

        testedRepository.updateCallback(true, metadata);

        verify(mockWorkoutRecordService).repositoryResponse(null,metadata);
    }

    @Test
    public void updateCallbackFailTest(){
        String testUserId = "testUser1";
        WorkoutRecordService mockWorkoutRecordService = Mockito.mock(WorkoutRecordService.class);
        WorkoutRecordFirestoreDatabase mockWorkoutRecordFirestoreDatabase = Mockito.mock(WorkoutRecordFirestoreDatabase.class);

        WorkoutRecordRepository testedRepository = new WorkoutRecordRepository(mockWorkoutRecordService, mockWorkoutRecordFirestoreDatabase);
        WorkoutRecord testWorkoutRecord = new WorkoutRecord(testUserId,0L,1L,2L,3L,4L);
        Map<String, Object> metadata = new HashMap<>();

        testedRepository.updateCallback(false, metadata);

        verify(mockWorkoutRecordService, times(0)).repositoryResponse(null,metadata);
    }

    @Test
    public void updateWorkoutRecordMileTest(){
        String testUserId = "testUser1";
        long testTime = 1000;
        WorkoutRecordService mockWorkoutRecordService = Mockito.mock(WorkoutRecordService.class);
        WorkoutRecordFirestoreDatabase mockWorkoutRecordFirestoreDatabase = Mockito.mock(WorkoutRecordFirestoreDatabase.class);

        WorkoutRecordRepository testedRepository = new WorkoutRecordRepository(mockWorkoutRecordService, mockWorkoutRecordFirestoreDatabase);

        testedRepository.updateWorkoutRecord(testUserId,testTime,WorkoutRecordDistance.MILE,new HashMap<>());

        verify(mockWorkoutRecordFirestoreDatabase).updateField(eq(testUserId),eq(WorkoutRecordFirestoreDatabase.MILE_RECORD_KEY),eq(testTime),eq(testedRepository),anyMap());
    }

    @Test
    public void updateWorkoutRecord5kTest(){
        String testUserId = "testUser1";
        long testTime = 1000;
        WorkoutRecordService mockWorkoutRecordService = Mockito.mock(WorkoutRecordService.class);
        WorkoutRecordFirestoreDatabase mockWorkoutRecordFirestoreDatabase = Mockito.mock(WorkoutRecordFirestoreDatabase.class);

        WorkoutRecordRepository testedRepository = new WorkoutRecordRepository(mockWorkoutRecordService, mockWorkoutRecordFirestoreDatabase);

        testedRepository.updateWorkoutRecord(testUserId,testTime,WorkoutRecordDistance.FIVE_K,new HashMap<>());

        verify(mockWorkoutRecordFirestoreDatabase).updateField(eq(testUserId),eq(WorkoutRecordFirestoreDatabase.FIVE_K_RECORD_KEY),eq(testTime),eq(testedRepository),anyMap());
    }

    @Test
    public void updateWorkoutRecord10KTest(){
        String testUserId = "testUser1";
        long testTime = 1000;
        WorkoutRecordService mockWorkoutRecordService = Mockito.mock(WorkoutRecordService.class);
        WorkoutRecordFirestoreDatabase mockWorkoutRecordFirestoreDatabase = Mockito.mock(WorkoutRecordFirestoreDatabase.class);

        WorkoutRecordRepository testedRepository = new WorkoutRecordRepository(mockWorkoutRecordService, mockWorkoutRecordFirestoreDatabase);

        testedRepository.updateWorkoutRecord(testUserId,testTime,WorkoutRecordDistance.TEN_K,new HashMap<>());

        verify(mockWorkoutRecordFirestoreDatabase).updateField(eq(testUserId),eq(WorkoutRecordFirestoreDatabase.TEN_K_RECORD_KEY),eq(testTime),eq(testedRepository),anyMap());
    }

    @Test
    public void updateWorkoutRecord25kTest(){
        String testUserId = "testUser1";
        long testTime = 1000;
        WorkoutRecordService mockWorkoutRecordService = Mockito.mock(WorkoutRecordService.class);
        WorkoutRecordFirestoreDatabase mockWorkoutRecordFirestoreDatabase = Mockito.mock(WorkoutRecordFirestoreDatabase.class);

        WorkoutRecordRepository testedRepository = new WorkoutRecordRepository(mockWorkoutRecordService, mockWorkoutRecordFirestoreDatabase);

        testedRepository.updateWorkoutRecord(testUserId,testTime,WorkoutRecordDistance.TWENTY_FIVE_K,new HashMap<>());

        verify(mockWorkoutRecordFirestoreDatabase).updateField(eq(testUserId),eq(WorkoutRecordFirestoreDatabase.TWENTY_FIVE_K_RECORD_KEY),eq(testTime),eq(testedRepository),anyMap());
    }

    @Test
    public void updateWorkoutRecordMarathonTest(){
        String testUserId = "testUser1";
        long testTime = 1000;
        WorkoutRecordService mockWorkoutRecordService = Mockito.mock(WorkoutRecordService.class);
        WorkoutRecordFirestoreDatabase mockWorkoutRecordFirestoreDatabase = Mockito.mock(WorkoutRecordFirestoreDatabase.class);

        WorkoutRecordRepository testedRepository = new WorkoutRecordRepository(mockWorkoutRecordService, mockWorkoutRecordFirestoreDatabase);

        testedRepository.updateWorkoutRecord(testUserId,testTime,WorkoutRecordDistance.MARATHON,new HashMap<>());

        verify(mockWorkoutRecordFirestoreDatabase).updateField(eq(testUserId),eq(WorkoutRecordFirestoreDatabase.MARATHON_RECORD_KEY),eq(testTime),eq(testedRepository),anyMap());
    }
}
