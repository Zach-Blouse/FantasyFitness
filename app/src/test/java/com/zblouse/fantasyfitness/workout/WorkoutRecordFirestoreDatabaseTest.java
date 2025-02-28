package com.zblouse.fantasyfitness.workout;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.zblouse.fantasyfitness.user.UserGameState;
import com.zblouse.fantasyfitness.user.UserGameStateFirestoreDatabase;
import com.zblouse.fantasyfitness.user.UserGameStateRepository;
import com.zblouse.fantasyfitness.world.GameLocationService;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

public class WorkoutRecordFirestoreDatabaseTest {

    @Test
    public void writeTest() {
        String testUserId = "testUserId";
        FirebaseFirestore mockFirestore = Mockito.mock(FirebaseFirestore.class);
        CollectionReference mockCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockFirestore.collection(eq("workoutRecords"))).thenReturn(mockCollectionReference);
        DocumentReference mockDocument = Mockito.mock(DocumentReference.class);
        when(mockCollectionReference.document(eq(testUserId))).thenReturn(mockDocument);
        Task mockTask = Mockito.mock(Task.class);
        when(mockTask.isSuccessful()).thenReturn(true);
        when(mockDocument.set(anyMap())).thenReturn(mockTask);
        WorkoutRecordRepository mockWorkoutRecordRepository = Mockito.mock(WorkoutRecordRepository.class);

        WorkoutRecord testWorkoutRecord = new WorkoutRecord(testUserId,0L,1L,2L,3L,4L);

        WorkoutRecordFirestoreDatabase testedDatabase = new WorkoutRecordFirestoreDatabase(mockFirestore);

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("testKey", "testValue");

        testedDatabase.write(testWorkoutRecord,mockWorkoutRecordRepository, metadata);

        ArgumentCaptor<Map> mapArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        verify(mockDocument).set(mapArgumentCaptor.capture());
        assert(mapArgumentCaptor.getValue().containsKey(WorkoutRecordFirestoreDatabase.MILE_RECORD_KEY));
        assertEquals(testWorkoutRecord.getMileRecord(),mapArgumentCaptor.getValue().get(WorkoutRecordFirestoreDatabase.MILE_RECORD_KEY));
        assert(mapArgumentCaptor.getValue().containsKey(WorkoutRecordFirestoreDatabase.FIVE_K_RECORD_KEY));
        assertEquals(testWorkoutRecord.getFiveKRecord(),mapArgumentCaptor.getValue().get(WorkoutRecordFirestoreDatabase.FIVE_K_RECORD_KEY));
        assert(mapArgumentCaptor.getValue().containsKey(WorkoutRecordFirestoreDatabase.TEN_K_RECORD_KEY));
        assertEquals(testWorkoutRecord.getTenKRecord(),mapArgumentCaptor.getValue().get(WorkoutRecordFirestoreDatabase.TEN_K_RECORD_KEY));
        assert(mapArgumentCaptor.getValue().containsKey(WorkoutRecordFirestoreDatabase.TWENTY_FIVE_K_RECORD_KEY));
        assertEquals(testWorkoutRecord.getTwentyFiveKRecord(),mapArgumentCaptor.getValue().get(WorkoutRecordFirestoreDatabase.TWENTY_FIVE_K_RECORD_KEY));
        assert(mapArgumentCaptor.getValue().containsKey(WorkoutRecordFirestoreDatabase.MARATHON_RECORD_KEY));
        assertEquals(testWorkoutRecord.getMarathonRecord(),mapArgumentCaptor.getValue().get(WorkoutRecordFirestoreDatabase.MARATHON_RECORD_KEY));
    }

    @Test
    public void updateMileRecordFieldSuccessTest(){
        String testUserId = "testUser1";
        long newRecord = 10L;
        FirebaseFirestore mockFirestore = Mockito.mock(FirebaseFirestore.class);
        CollectionReference mockCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockFirestore.collection(eq("workoutRecords"))).thenReturn(mockCollectionReference);
        DocumentReference mockDocument = Mockito.mock(DocumentReference.class);
        when(mockCollectionReference.document(eq(testUserId))).thenReturn(mockDocument);
        Task mockTask = Mockito.mock(Task.class);
        when(mockTask.isSuccessful()).thenReturn(true);
        when(mockDocument.update(anyString(),any())).thenReturn(mockTask);
        WorkoutRecordRepository mockWorkoutRecordRepository = Mockito.mock(WorkoutRecordRepository.class);

        WorkoutRecordFirestoreDatabase testedDatabase = new WorkoutRecordFirestoreDatabase(mockFirestore);
        testedDatabase.updateField(testUserId,WorkoutRecordFirestoreDatabase.MILE_RECORD_KEY,newRecord,mockWorkoutRecordRepository,new HashMap<>());

        verify(mockDocument).update(eq(WorkoutRecordFirestoreDatabase.MILE_RECORD_KEY),eq(newRecord));

        ArgumentCaptor<OnCompleteListener<Void>> onCompleteListenerArgumentCaptor = ArgumentCaptor.forClass(OnCompleteListener.class);
        verify(mockTask).addOnCompleteListener((OnCompleteListener<Void>) onCompleteListenerArgumentCaptor.capture());
        onCompleteListenerArgumentCaptor.getValue().onComplete(mockTask);

        verify(mockWorkoutRecordRepository).updateCallback(eq(true),anyMap());
    }

    @Test
    public void updateMileRecordFieldFailedTest(){
        String testUserId = "testUser1";
        long newRecord = 10L;
        FirebaseFirestore mockFirestore = Mockito.mock(FirebaseFirestore.class);
        CollectionReference mockCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockFirestore.collection(eq("workoutRecords"))).thenReturn(mockCollectionReference);
        DocumentReference mockDocument = Mockito.mock(DocumentReference.class);
        when(mockCollectionReference.document(eq(testUserId))).thenReturn(mockDocument);
        Task mockTask = Mockito.mock(Task.class);
        when(mockTask.isSuccessful()).thenReturn(false);
        when(mockDocument.update(anyString(),any())).thenReturn(mockTask);
        WorkoutRecordRepository mockWorkoutRecordRepository = Mockito.mock(WorkoutRecordRepository.class);

        WorkoutRecordFirestoreDatabase testedDatabase = new WorkoutRecordFirestoreDatabase(mockFirestore);
        testedDatabase.updateField(testUserId,WorkoutRecordFirestoreDatabase.MILE_RECORD_KEY,newRecord,mockWorkoutRecordRepository,new HashMap<>());

        verify(mockDocument).update(eq(WorkoutRecordFirestoreDatabase.MILE_RECORD_KEY),eq(newRecord));

        ArgumentCaptor<OnCompleteListener<Void>> onCompleteListenerArgumentCaptor = ArgumentCaptor.forClass(OnCompleteListener.class);
        verify(mockTask).addOnCompleteListener((OnCompleteListener<Void>) onCompleteListenerArgumentCaptor.capture());
        onCompleteListenerArgumentCaptor.getValue().onComplete(mockTask);

        verify(mockWorkoutRecordRepository).updateCallback(eq(false),anyMap());
    }

    @Test
    public void readSuccessfulExistsTest(){
        String testUserId = "testUser1";
        long mileRecord = 255;
        long fiveKRecord = 256;
        long tenKRecord = 257;
        long twentyFiveKRecord = 258;
        long marathonRecord = 259;

        FirebaseFirestore mockFirestore = Mockito.mock(FirebaseFirestore.class);
        CollectionReference mockCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockFirestore.collection(eq("workoutRecords"))).thenReturn(mockCollectionReference);
        DocumentReference mockDocument = Mockito.mock(DocumentReference.class);
        when(mockCollectionReference.document(eq(testUserId))).thenReturn(mockDocument);
        Task mockTask = Mockito.mock(Task.class);
        when(mockTask.isSuccessful()).thenReturn(true);
        DocumentSnapshot mockDocumentSnapshot = Mockito.mock(DocumentSnapshot.class);
        when(mockTask.getResult()).thenReturn(mockDocumentSnapshot);
        when(mockDocumentSnapshot.exists()).thenReturn(true);
        when(mockDocumentSnapshot.get(eq(WorkoutRecordFirestoreDatabase.MILE_RECORD_KEY), eq(Long.class))).thenReturn(mileRecord);
        when(mockDocumentSnapshot.get(eq(WorkoutRecordFirestoreDatabase.FIVE_K_RECORD_KEY), eq(Long.class))).thenReturn(fiveKRecord);
        when(mockDocumentSnapshot.get(eq(WorkoutRecordFirestoreDatabase.TEN_K_RECORD_KEY), eq(Long.class))).thenReturn(tenKRecord);
        when(mockDocumentSnapshot.get(eq(WorkoutRecordFirestoreDatabase.TWENTY_FIVE_K_RECORD_KEY), eq(Long.class))).thenReturn(twentyFiveKRecord);
        when(mockDocumentSnapshot.get(eq(WorkoutRecordFirestoreDatabase.MARATHON_RECORD_KEY), eq(Long.class))).thenReturn(marathonRecord);

        when(mockDocument.get()).thenReturn(mockTask);
        WorkoutRecordRepository mockWorkoutRecordRepository = Mockito.mock(WorkoutRecordRepository.class);

        WorkoutRecordFirestoreDatabase testedDatabase = new WorkoutRecordFirestoreDatabase(mockFirestore);

        testedDatabase.read(testUserId,mockWorkoutRecordRepository,new HashMap<>());
        ArgumentCaptor<OnCompleteListener<Void>> onCompleteListenerArgumentCaptor = ArgumentCaptor.forClass(OnCompleteListener.class);
        verify(mockTask).addOnCompleteListener((OnCompleteListener<Void>) onCompleteListenerArgumentCaptor.capture());
        onCompleteListenerArgumentCaptor.getValue().onComplete(mockTask);

        ArgumentCaptor<WorkoutRecord> workoutRecordArgumentCaptor = ArgumentCaptor.forClass(WorkoutRecord.class);
        verify(mockWorkoutRecordRepository).readCallback(workoutRecordArgumentCaptor.capture(),anyMap());
        assertEquals(testUserId, workoutRecordArgumentCaptor.getValue().getUserId());
        assertEquals(mileRecord, (long)workoutRecordArgumentCaptor.getValue().getMileRecord());
        assertEquals(fiveKRecord, (long)workoutRecordArgumentCaptor.getValue().getFiveKRecord());
        assertEquals(tenKRecord, (long)workoutRecordArgumentCaptor.getValue().getTenKRecord());
        assertEquals(twentyFiveKRecord, (long)workoutRecordArgumentCaptor.getValue().getTwentyFiveKRecord());
        assertEquals(marathonRecord, (long)workoutRecordArgumentCaptor.getValue().getMarathonRecord());
    }

    @Test
    public void readSuccessfulDoesntExistTest(){
        String testUserId = "testUser1";
        long mileRecord = 255;
        long fiveKRecord = 256;
        long tenKRecord = 257;
        long twentyFiveKRecord = 258;
        long marathonRecord = 259;

        FirebaseFirestore mockFirestore = Mockito.mock(FirebaseFirestore.class);
        CollectionReference mockCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockFirestore.collection(eq("workoutRecords"))).thenReturn(mockCollectionReference);
        DocumentReference mockDocument = Mockito.mock(DocumentReference.class);
        when(mockCollectionReference.document(eq(testUserId))).thenReturn(mockDocument);
        Task mockTask = Mockito.mock(Task.class);
        when(mockTask.isSuccessful()).thenReturn(true);
        DocumentSnapshot mockDocumentSnapshot = Mockito.mock(DocumentSnapshot.class);
        when(mockTask.getResult()).thenReturn(mockDocumentSnapshot);
        when(mockDocumentSnapshot.exists()).thenReturn(false);
        when(mockDocumentSnapshot.get(eq(WorkoutRecordFirestoreDatabase.MILE_RECORD_KEY), eq(Long.class))).thenReturn(mileRecord);
        when(mockDocumentSnapshot.get(eq(WorkoutRecordFirestoreDatabase.FIVE_K_RECORD_KEY), eq(Long.class))).thenReturn(fiveKRecord);
        when(mockDocumentSnapshot.get(eq(WorkoutRecordFirestoreDatabase.TEN_K_RECORD_KEY), eq(Long.class))).thenReturn(tenKRecord);
        when(mockDocumentSnapshot.get(eq(WorkoutRecordFirestoreDatabase.TWENTY_FIVE_K_RECORD_KEY), eq(Long.class))).thenReturn(twentyFiveKRecord);
        when(mockDocumentSnapshot.get(eq(WorkoutRecordFirestoreDatabase.MARATHON_RECORD_KEY), eq(Long.class))).thenReturn(marathonRecord);

        when(mockDocument.get()).thenReturn(mockTask);
        WorkoutRecordRepository mockWorkoutRecordRepository = Mockito.mock(WorkoutRecordRepository.class);

        WorkoutRecordFirestoreDatabase testedDatabase = new WorkoutRecordFirestoreDatabase(mockFirestore);

        testedDatabase.read(testUserId,mockWorkoutRecordRepository,new HashMap<>());
        ArgumentCaptor<OnCompleteListener<Void>> onCompleteListenerArgumentCaptor = ArgumentCaptor.forClass(OnCompleteListener.class);
        verify(mockTask).addOnCompleteListener((OnCompleteListener<Void>) onCompleteListenerArgumentCaptor.capture());
        onCompleteListenerArgumentCaptor.getValue().onComplete(mockTask);

        verify(mockWorkoutRecordRepository).readCallback(eq(null),anyMap());

    }

    @Test
    public void readFailedTest(){
        String testUserId = "testUser1";
        long mileRecord = 255;
        long fiveKRecord = 256;
        long tenKRecord = 257;
        long twentyFiveKRecord = 258;
        long marathonRecord = 259;

        FirebaseFirestore mockFirestore = Mockito.mock(FirebaseFirestore.class);
        CollectionReference mockCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockFirestore.collection(eq("workoutRecords"))).thenReturn(mockCollectionReference);
        DocumentReference mockDocument = Mockito.mock(DocumentReference.class);
        when(mockCollectionReference.document(eq(testUserId))).thenReturn(mockDocument);
        Task mockTask = Mockito.mock(Task.class);
        when(mockTask.isSuccessful()).thenReturn(false);
        DocumentSnapshot mockDocumentSnapshot = Mockito.mock(DocumentSnapshot.class);
        when(mockTask.getResult()).thenReturn(mockDocumentSnapshot);
        when(mockDocumentSnapshot.exists()).thenReturn(false);
        when(mockDocumentSnapshot.get(eq(WorkoutRecordFirestoreDatabase.MILE_RECORD_KEY), eq(Long.class))).thenReturn(mileRecord);
        when(mockDocumentSnapshot.get(eq(WorkoutRecordFirestoreDatabase.FIVE_K_RECORD_KEY), eq(Long.class))).thenReturn(fiveKRecord);
        when(mockDocumentSnapshot.get(eq(WorkoutRecordFirestoreDatabase.TEN_K_RECORD_KEY), eq(Long.class))).thenReturn(tenKRecord);
        when(mockDocumentSnapshot.get(eq(WorkoutRecordFirestoreDatabase.TWENTY_FIVE_K_RECORD_KEY), eq(Long.class))).thenReturn(twentyFiveKRecord);
        when(mockDocumentSnapshot.get(eq(WorkoutRecordFirestoreDatabase.MARATHON_RECORD_KEY), eq(Long.class))).thenReturn(marathonRecord);

        when(mockDocument.get()).thenReturn(mockTask);
        WorkoutRecordRepository mockWorkoutRecordRepository = Mockito.mock(WorkoutRecordRepository.class);

        WorkoutRecordFirestoreDatabase testedDatabase = new WorkoutRecordFirestoreDatabase(mockFirestore);

        testedDatabase.read(testUserId,mockWorkoutRecordRepository,new HashMap<>());
        ArgumentCaptor<OnCompleteListener<Void>> onCompleteListenerArgumentCaptor = ArgumentCaptor.forClass(OnCompleteListener.class);
        verify(mockTask).addOnCompleteListener((OnCompleteListener<Void>) onCompleteListenerArgumentCaptor.capture());
        onCompleteListenerArgumentCaptor.getValue().onComplete(mockTask);

        verify(mockWorkoutRecordRepository).readCallback(eq(null),anyMap());

    }
}
