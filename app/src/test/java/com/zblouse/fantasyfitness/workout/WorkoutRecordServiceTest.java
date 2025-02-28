package com.zblouse.fantasyfitness.workout;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.firebase.auth.FirebaseUser;
import com.zblouse.fantasyfitness.R;
import com.zblouse.fantasyfitness.activity.DeviceServiceType;
import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.activity.NotificationDeviceService;
import com.zblouse.fantasyfitness.core.DomainService;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

public class WorkoutRecordServiceTest {

    @Test
    public void fetchWorkoutRecordsTest(){
        String testUid = "testUid1";
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        when(mockActivity.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.getUid()).thenReturn(testUid);
        WorkoutRecordRepository mockWorkoutRecordRepository = Mockito.mock(WorkoutRecordRepository.class);
        WorkoutRecordService testedService = new WorkoutRecordService(mockActivity, mockWorkoutRecordRepository);

        testedService.fetchWorkoutRecords(new HashMap<>());
        ArgumentCaptor<Map> metadataArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        verify(mockWorkoutRecordRepository).readWorkoutRecord(eq(testUid),metadataArgumentCaptor.capture());
        assert(metadataArgumentCaptor.getValue().containsKey(DomainService.ORIGIN_FUNCTION));
        assertEquals("fetchRecords",metadataArgumentCaptor.getValue().get(DomainService.ORIGIN_FUNCTION));
    }

    @Test
    public void checkForRecordsValidRecordMileCheckTest(){
        String testUid = "testUid1";
        long workoutTime = 1500;
        double workoutDistance = 1620;
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        when(mockActivity.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.getUid()).thenReturn(testUid);
        WorkoutRecordRepository mockWorkoutRecordRepository = Mockito.mock(WorkoutRecordRepository.class);
        WorkoutRecordService testedService = new WorkoutRecordService(mockActivity, mockWorkoutRecordRepository);

        testedService.checkForRecords(workoutTime,workoutDistance);
        ArgumentCaptor<Map> metadataArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        verify(mockWorkoutRecordRepository).readWorkoutRecord(eq(testUid),metadataArgumentCaptor.capture());
        assert(metadataArgumentCaptor.getValue().containsKey(DomainService.ORIGIN_FUNCTION));
        assertEquals("checkForRecords",metadataArgumentCaptor.getValue().get(DomainService.ORIGIN_FUNCTION));
        assert(metadataArgumentCaptor.getValue().containsKey("workoutRecordDistance"));
        assertEquals(WorkoutRecordDistance.MILE,metadataArgumentCaptor.getValue().get("workoutRecordDistance"));
        assert(metadataArgumentCaptor.getValue().containsKey("workoutTime"));
        assertEquals(workoutTime,metadataArgumentCaptor.getValue().get("workoutTime"));
    }

    @Test
    public void checkForRecordsValidRecord5kCheckTest(){
        String testUid = "testUid1";
        long workoutTime = 1500;
        double workoutDistance = 5002;
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        when(mockActivity.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.getUid()).thenReturn(testUid);
        WorkoutRecordRepository mockWorkoutRecordRepository = Mockito.mock(WorkoutRecordRepository.class);
        WorkoutRecordService testedService = new WorkoutRecordService(mockActivity, mockWorkoutRecordRepository);

        testedService.checkForRecords(workoutTime,workoutDistance);
        ArgumentCaptor<Map> metadataArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        verify(mockWorkoutRecordRepository).readWorkoutRecord(eq(testUid),metadataArgumentCaptor.capture());
        assert(metadataArgumentCaptor.getValue().containsKey(DomainService.ORIGIN_FUNCTION));
        assertEquals("checkForRecords",metadataArgumentCaptor.getValue().get(DomainService.ORIGIN_FUNCTION));
        assert(metadataArgumentCaptor.getValue().containsKey("workoutRecordDistance"));
        assertEquals(WorkoutRecordDistance.FIVE_K,metadataArgumentCaptor.getValue().get("workoutRecordDistance"));
        assert(metadataArgumentCaptor.getValue().containsKey("workoutTime"));
        assertEquals(workoutTime,metadataArgumentCaptor.getValue().get("workoutTime"));
    }

    @Test
    public void checkForRecordsValidRecord10kCheckTest(){
        String testUid = "testUid1";
        long workoutTime = 1500;
        double workoutDistance = 10002;
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        when(mockActivity.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.getUid()).thenReturn(testUid);
        WorkoutRecordRepository mockWorkoutRecordRepository = Mockito.mock(WorkoutRecordRepository.class);
        WorkoutRecordService testedService = new WorkoutRecordService(mockActivity, mockWorkoutRecordRepository);

        testedService.checkForRecords(workoutTime,workoutDistance);
        ArgumentCaptor<Map> metadataArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        verify(mockWorkoutRecordRepository).readWorkoutRecord(eq(testUid),metadataArgumentCaptor.capture());
        assert(metadataArgumentCaptor.getValue().containsKey(DomainService.ORIGIN_FUNCTION));
        assertEquals("checkForRecords",metadataArgumentCaptor.getValue().get(DomainService.ORIGIN_FUNCTION));
        assert(metadataArgumentCaptor.getValue().containsKey("workoutRecordDistance"));
        assertEquals(WorkoutRecordDistance.TEN_K,metadataArgumentCaptor.getValue().get("workoutRecordDistance"));
        assert(metadataArgumentCaptor.getValue().containsKey("workoutTime"));
        assertEquals(workoutTime,metadataArgumentCaptor.getValue().get("workoutTime"));
    }

    @Test
    public void checkForRecordsValidRecord25kCheckTest(){
        String testUid = "testUid1";
        long workoutTime = 1500;
        double workoutDistance = 25002;
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        when(mockActivity.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.getUid()).thenReturn(testUid);
        WorkoutRecordRepository mockWorkoutRecordRepository = Mockito.mock(WorkoutRecordRepository.class);
        WorkoutRecordService testedService = new WorkoutRecordService(mockActivity, mockWorkoutRecordRepository);

        testedService.checkForRecords(workoutTime,workoutDistance);
        ArgumentCaptor<Map> metadataArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        verify(mockWorkoutRecordRepository).readWorkoutRecord(eq(testUid),metadataArgumentCaptor.capture());
        assert(metadataArgumentCaptor.getValue().containsKey(DomainService.ORIGIN_FUNCTION));
        assertEquals("checkForRecords",metadataArgumentCaptor.getValue().get(DomainService.ORIGIN_FUNCTION));
        assert(metadataArgumentCaptor.getValue().containsKey("workoutRecordDistance"));
        assertEquals(WorkoutRecordDistance.TWENTY_FIVE_K,metadataArgumentCaptor.getValue().get("workoutRecordDistance"));
        assert(metadataArgumentCaptor.getValue().containsKey("workoutTime"));
        assertEquals(workoutTime,metadataArgumentCaptor.getValue().get("workoutTime"));
    }

    @Test
    public void checkForRecordsValidRecordMarathonCheckTest(){
        String testUid = "testUid1";
        long workoutTime = 1500;
        double workoutDistance = 42245;
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        when(mockActivity.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.getUid()).thenReturn(testUid);
        WorkoutRecordRepository mockWorkoutRecordRepository = Mockito.mock(WorkoutRecordRepository.class);
        WorkoutRecordService testedService = new WorkoutRecordService(mockActivity, mockWorkoutRecordRepository);

        testedService.checkForRecords(workoutTime,workoutDistance);
        ArgumentCaptor<Map> metadataArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        verify(mockWorkoutRecordRepository).readWorkoutRecord(eq(testUid),metadataArgumentCaptor.capture());
        assert(metadataArgumentCaptor.getValue().containsKey(DomainService.ORIGIN_FUNCTION));
        assertEquals("checkForRecords",metadataArgumentCaptor.getValue().get(DomainService.ORIGIN_FUNCTION));
        assert(metadataArgumentCaptor.getValue().containsKey("workoutRecordDistance"));
        assertEquals(WorkoutRecordDistance.MARATHON,metadataArgumentCaptor.getValue().get("workoutRecordDistance"));
        assert(metadataArgumentCaptor.getValue().containsKey("workoutTime"));
        assertEquals(workoutTime,metadataArgumentCaptor.getValue().get("workoutTime"));
    }

    @Test
    public void checkForRecordsInvalidRecordCheckTest(){
        String testUid = "testUid1";
        long workoutTime = 1500;
        double workoutDistance = 6874;
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        when(mockActivity.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.getUid()).thenReturn(testUid);
        WorkoutRecordRepository mockWorkoutRecordRepository = Mockito.mock(WorkoutRecordRepository.class);
        WorkoutRecordService testedService = new WorkoutRecordService(mockActivity, mockWorkoutRecordRepository);

        testedService.checkForRecords(workoutTime,workoutDistance);
        verify(mockWorkoutRecordRepository, times(0)).readWorkoutRecord(anyString(),anyMap());

    }

    @Test
    public void initializeWorkoutRecordsTest(){
        String testUid = "testUid1";
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        when(mockActivity.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.getUid()).thenReturn(testUid);
        WorkoutRecordRepository mockWorkoutRecordRepository = Mockito.mock(WorkoutRecordRepository.class);
        WorkoutRecordService testedService = new WorkoutRecordService(mockActivity, mockWorkoutRecordRepository);

        testedService.initializeWorkoutRecords();
        ArgumentCaptor<Map> metadataArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        ArgumentCaptor<WorkoutRecord> workoutRecordArgumentCaptor = ArgumentCaptor.forClass(WorkoutRecord.class);
        verify(mockWorkoutRecordRepository).writeWorkoutRecord(workoutRecordArgumentCaptor.capture(),metadataArgumentCaptor.capture());
        assertEquals(Long.MAX_VALUE, (long)workoutRecordArgumentCaptor.getValue().getMileRecord());
        assertEquals(Long.MAX_VALUE, (long)workoutRecordArgumentCaptor.getValue().getFiveKRecord());
        assertEquals(Long.MAX_VALUE, (long)workoutRecordArgumentCaptor.getValue().getTenKRecord());
        assertEquals(Long.MAX_VALUE, (long)workoutRecordArgumentCaptor.getValue().getTwentyFiveKRecord());
        assertEquals(Long.MAX_VALUE, (long)workoutRecordArgumentCaptor.getValue().getMarathonRecord());
        assert(metadataArgumentCaptor.getValue().containsKey(DomainService.ORIGIN_FUNCTION));
        assertEquals("initializeRecords",metadataArgumentCaptor.getValue().get(DomainService.ORIGIN_FUNCTION));
    }

    @Test
    public void repositoryResponseFetchRecordsTest(){
        String testUid = "testUid1";
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        when(mockActivity.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.getUid()).thenReturn(testUid);
        WorkoutRecordRepository mockWorkoutRecordRepository = Mockito.mock(WorkoutRecordRepository.class);
        WorkoutRecordService testedService = new WorkoutRecordService(mockActivity, mockWorkoutRecordRepository);
        WorkoutRecord testRecord = new WorkoutRecord(testUid,1L,2L,3L,4L,5L);
        Map<String, Object> metadata = new HashMap<>();
        metadata.put(DomainService.ORIGIN_FUNCTION,"fetchRecords");

        testedService.repositoryResponse(testRecord, metadata);
        ArgumentCaptor<WorkoutRecordReadEvent> workoutRecordReadEventArgumentCaptor = ArgumentCaptor.forClass(WorkoutRecordReadEvent.class);
        verify(mockActivity).publishEvent(workoutRecordReadEventArgumentCaptor.capture());
        assertEquals(testRecord, workoutRecordReadEventArgumentCaptor.getValue().getWorkoutRecord());
    }

    @Test
    public void repositoryResponseCheckForRecordsMileRecordSetTest(){
        String testUid = "testUid1";
        long testWorkoutTime = 2500;
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        NotificationDeviceService mockNotificationDeviceService = Mockito.mock(NotificationDeviceService.class);
        when(mockActivity.getDeviceService(DeviceServiceType.NOTIFICATION)).thenReturn(mockNotificationDeviceService);
        when(mockActivity.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.getUid()).thenReturn(testUid);
        WorkoutRecordRepository mockWorkoutRecordRepository = Mockito.mock(WorkoutRecordRepository.class);
        WorkoutRecordService testedService = new WorkoutRecordService(mockActivity, mockWorkoutRecordRepository);
        WorkoutRecord testRecord = new WorkoutRecord(testUid,9999L,9999L,9999L,9999L,9999L);
        Map<String, Object> metadata = new HashMap<>();
        metadata.put(DomainService.ORIGIN_FUNCTION,"checkForRecords");
        metadata.put("workoutRecordDistance", WorkoutRecordDistance.MILE);
        metadata.put("workoutTime", testWorkoutTime);

        testedService.repositoryResponse(testRecord, metadata);
        verify(mockNotificationDeviceService).sendNotification(eq("New Record"),eq("Congratulations on your new record Mile time!"),eq(R.drawable.run));
        verify(mockWorkoutRecordRepository).updateWorkoutRecord(eq(testUid),eq(testWorkoutTime),eq(WorkoutRecordDistance.MILE),anyMap());
    }

    @Test
    public void repositoryResponseCheckForRecordsMileRecordNotSetTest(){
        String testUid = "testUid1";
        long testWorkoutTime = Long.MAX_VALUE;
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        NotificationDeviceService mockNotificationDeviceService = Mockito.mock(NotificationDeviceService.class);
        when(mockActivity.getDeviceService(DeviceServiceType.NOTIFICATION)).thenReturn(mockNotificationDeviceService);
        when(mockActivity.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.getUid()).thenReturn(testUid);
        WorkoutRecordRepository mockWorkoutRecordRepository = Mockito.mock(WorkoutRecordRepository.class);
        WorkoutRecordService testedService = new WorkoutRecordService(mockActivity, mockWorkoutRecordRepository);
        WorkoutRecord testRecord = new WorkoutRecord(testUid,9999L,9999L,9999L,9999L,9999L);
        Map<String, Object> metadata = new HashMap<>();
        metadata.put(DomainService.ORIGIN_FUNCTION,"checkForRecords");
        metadata.put("workoutRecordDistance", WorkoutRecordDistance.MILE);
        metadata.put("workoutTime", testWorkoutTime);

        testedService.repositoryResponse(testRecord, metadata);
        verify(mockNotificationDeviceService, times(0)).sendNotification(anyString(),anyString(),anyInt());
        verify(mockWorkoutRecordRepository, times(0)).updateWorkoutRecord(anyString(),anyLong(),any(),anyMap());
    }

    @Test
    public void repositoryResponseCheckForRecords5kRecordSetTest(){
        String testUid = "testUid1";
        long testWorkoutTime = 2500;
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        NotificationDeviceService mockNotificationDeviceService = Mockito.mock(NotificationDeviceService.class);
        when(mockActivity.getDeviceService(DeviceServiceType.NOTIFICATION)).thenReturn(mockNotificationDeviceService);
        when(mockActivity.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.getUid()).thenReturn(testUid);
        WorkoutRecordRepository mockWorkoutRecordRepository = Mockito.mock(WorkoutRecordRepository.class);
        WorkoutRecordService testedService = new WorkoutRecordService(mockActivity, mockWorkoutRecordRepository);
        WorkoutRecord testRecord = new WorkoutRecord(testUid,9999L,9999L,9999L,9999L,9999L);
        Map<String, Object> metadata = new HashMap<>();
        metadata.put(DomainService.ORIGIN_FUNCTION,"checkForRecords");
        metadata.put("workoutRecordDistance", WorkoutRecordDistance.FIVE_K);
        metadata.put("workoutTime", testWorkoutTime);

        testedService.repositoryResponse(testRecord, metadata);
        verify(mockNotificationDeviceService).sendNotification(eq("New Record"),eq("Congratulations on your new record 5k time!"),eq(R.drawable.run));
        verify(mockWorkoutRecordRepository).updateWorkoutRecord(eq(testUid),eq(testWorkoutTime),eq(WorkoutRecordDistance.FIVE_K),anyMap());
    }

    @Test
    public void repositoryResponseCheckForRecords5kRecordNotSetTest(){
        String testUid = "testUid1";
        long testWorkoutTime = Long.MAX_VALUE;
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        NotificationDeviceService mockNotificationDeviceService = Mockito.mock(NotificationDeviceService.class);
        when(mockActivity.getDeviceService(DeviceServiceType.NOTIFICATION)).thenReturn(mockNotificationDeviceService);
        when(mockActivity.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.getUid()).thenReturn(testUid);
        WorkoutRecordRepository mockWorkoutRecordRepository = Mockito.mock(WorkoutRecordRepository.class);
        WorkoutRecordService testedService = new WorkoutRecordService(mockActivity, mockWorkoutRecordRepository);
        WorkoutRecord testRecord = new WorkoutRecord(testUid,9999L,9999L,9999L,9999L,9999L);
        Map<String, Object> metadata = new HashMap<>();
        metadata.put(DomainService.ORIGIN_FUNCTION,"checkForRecords");
        metadata.put("workoutRecordDistance", WorkoutRecordDistance.FIVE_K);
        metadata.put("workoutTime", testWorkoutTime);

        testedService.repositoryResponse(testRecord, metadata);
        verify(mockNotificationDeviceService, times(0)).sendNotification(anyString(),anyString(),anyInt());
        verify(mockWorkoutRecordRepository, times(0)).updateWorkoutRecord(anyString(),anyLong(),any(),anyMap());
    }

    @Test
    public void repositoryResponseCheckForRecords10kRecordSetTest(){
        String testUid = "testUid1";
        long testWorkoutTime = 2500;
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        NotificationDeviceService mockNotificationDeviceService = Mockito.mock(NotificationDeviceService.class);
        when(mockActivity.getDeviceService(DeviceServiceType.NOTIFICATION)).thenReturn(mockNotificationDeviceService);
        when(mockActivity.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.getUid()).thenReturn(testUid);
        WorkoutRecordRepository mockWorkoutRecordRepository = Mockito.mock(WorkoutRecordRepository.class);
        WorkoutRecordService testedService = new WorkoutRecordService(mockActivity, mockWorkoutRecordRepository);
        WorkoutRecord testRecord = new WorkoutRecord(testUid,9999L,9999L,9999L,9999L,9999L);
        Map<String, Object> metadata = new HashMap<>();
        metadata.put(DomainService.ORIGIN_FUNCTION,"checkForRecords");
        metadata.put("workoutRecordDistance", WorkoutRecordDistance.TEN_K);
        metadata.put("workoutTime", testWorkoutTime);

        testedService.repositoryResponse(testRecord, metadata);
        verify(mockNotificationDeviceService).sendNotification(eq("New Record"),eq("Congratulations on your new record 10k time!"),eq(R.drawable.run));
        verify(mockWorkoutRecordRepository).updateWorkoutRecord(eq(testUid),eq(testWorkoutTime),eq(WorkoutRecordDistance.TEN_K),anyMap());
    }

    @Test
    public void repositoryResponseCheckForRecords10kRecordNotSetTest(){
        String testUid = "testUid1";
        long testWorkoutTime = Long.MAX_VALUE;
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        NotificationDeviceService mockNotificationDeviceService = Mockito.mock(NotificationDeviceService.class);
        when(mockActivity.getDeviceService(DeviceServiceType.NOTIFICATION)).thenReturn(mockNotificationDeviceService);
        when(mockActivity.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.getUid()).thenReturn(testUid);
        WorkoutRecordRepository mockWorkoutRecordRepository = Mockito.mock(WorkoutRecordRepository.class);
        WorkoutRecordService testedService = new WorkoutRecordService(mockActivity, mockWorkoutRecordRepository);
        WorkoutRecord testRecord = new WorkoutRecord(testUid,9999L,9999L,9999L,9999L,9999L);
        Map<String, Object> metadata = new HashMap<>();
        metadata.put(DomainService.ORIGIN_FUNCTION,"checkForRecords");
        metadata.put("workoutRecordDistance", WorkoutRecordDistance.TEN_K);
        metadata.put("workoutTime", testWorkoutTime);

        testedService.repositoryResponse(testRecord, metadata);
        verify(mockNotificationDeviceService, times(0)).sendNotification(anyString(),anyString(),anyInt());
        verify(mockWorkoutRecordRepository, times(0)).updateWorkoutRecord(anyString(),anyLong(),any(),anyMap());
    }

    @Test
    public void repositoryResponseCheckForRecords25kRecordSetTest(){
        String testUid = "testUid1";
        long testWorkoutTime = 2500;
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        NotificationDeviceService mockNotificationDeviceService = Mockito.mock(NotificationDeviceService.class);
        when(mockActivity.getDeviceService(DeviceServiceType.NOTIFICATION)).thenReturn(mockNotificationDeviceService);
        when(mockActivity.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.getUid()).thenReturn(testUid);
        WorkoutRecordRepository mockWorkoutRecordRepository = Mockito.mock(WorkoutRecordRepository.class);
        WorkoutRecordService testedService = new WorkoutRecordService(mockActivity, mockWorkoutRecordRepository);
        WorkoutRecord testRecord = new WorkoutRecord(testUid,9999L,9999L,9999L,9999L,9999L);
        Map<String, Object> metadata = new HashMap<>();
        metadata.put(DomainService.ORIGIN_FUNCTION,"checkForRecords");
        metadata.put("workoutRecordDistance", WorkoutRecordDistance.TWENTY_FIVE_K);
        metadata.put("workoutTime", testWorkoutTime);

        testedService.repositoryResponse(testRecord, metadata);
        verify(mockNotificationDeviceService).sendNotification(eq("New Record"),eq("Congratulations on your new record 25k time!"),eq(R.drawable.run));
        verify(mockWorkoutRecordRepository).updateWorkoutRecord(eq(testUid),eq(testWorkoutTime),eq(WorkoutRecordDistance.TWENTY_FIVE_K),anyMap());
    }

    @Test
    public void repositoryResponseCheckForRecords25kRecordNotSetTest(){
        String testUid = "testUid1";
        long testWorkoutTime = Long.MAX_VALUE;
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        NotificationDeviceService mockNotificationDeviceService = Mockito.mock(NotificationDeviceService.class);
        when(mockActivity.getDeviceService(DeviceServiceType.NOTIFICATION)).thenReturn(mockNotificationDeviceService);
        when(mockActivity.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.getUid()).thenReturn(testUid);
        WorkoutRecordRepository mockWorkoutRecordRepository = Mockito.mock(WorkoutRecordRepository.class);
        WorkoutRecordService testedService = new WorkoutRecordService(mockActivity, mockWorkoutRecordRepository);
        WorkoutRecord testRecord = new WorkoutRecord(testUid,9999L,9999L,9999L,9999L,9999L);
        Map<String, Object> metadata = new HashMap<>();
        metadata.put(DomainService.ORIGIN_FUNCTION,"checkForRecords");
        metadata.put("workoutRecordDistance", WorkoutRecordDistance.TWENTY_FIVE_K);
        metadata.put("workoutTime", testWorkoutTime);

        testedService.repositoryResponse(testRecord, metadata);
        verify(mockNotificationDeviceService, times(0)).sendNotification(anyString(),anyString(),anyInt());
        verify(mockWorkoutRecordRepository, times(0)).updateWorkoutRecord(anyString(),anyLong(),any(),anyMap());
    }

    @Test
    public void repositoryResponseCheckForRecordsMarathonRecordSetTest(){
        String testUid = "testUid1";
        long testWorkoutTime = 2500;
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        NotificationDeviceService mockNotificationDeviceService = Mockito.mock(NotificationDeviceService.class);
        when(mockActivity.getDeviceService(DeviceServiceType.NOTIFICATION)).thenReturn(mockNotificationDeviceService);
        when(mockActivity.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.getUid()).thenReturn(testUid);
        WorkoutRecordRepository mockWorkoutRecordRepository = Mockito.mock(WorkoutRecordRepository.class);
        WorkoutRecordService testedService = new WorkoutRecordService(mockActivity, mockWorkoutRecordRepository);
        WorkoutRecord testRecord = new WorkoutRecord(testUid,9999L,9999L,9999L,9999L,9999L);
        Map<String, Object> metadata = new HashMap<>();
        metadata.put(DomainService.ORIGIN_FUNCTION,"checkForRecords");
        metadata.put("workoutRecordDistance", WorkoutRecordDistance.MARATHON);
        metadata.put("workoutTime", testWorkoutTime);

        testedService.repositoryResponse(testRecord, metadata);
        verify(mockNotificationDeviceService).sendNotification(eq("New Record"),eq("Congratulations on your new record Marathon time!"),eq(R.drawable.run));
        verify(mockWorkoutRecordRepository).updateWorkoutRecord(eq(testUid),eq(testWorkoutTime),eq(WorkoutRecordDistance.MARATHON),anyMap());
    }

    @Test
    public void repositoryResponseCheckForRecordsMarathonRecordNotSetTest(){
        String testUid = "testUid1";
        long testWorkoutTime = Long.MAX_VALUE;
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        NotificationDeviceService mockNotificationDeviceService = Mockito.mock(NotificationDeviceService.class);
        when(mockActivity.getDeviceService(DeviceServiceType.NOTIFICATION)).thenReturn(mockNotificationDeviceService);
        when(mockActivity.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.getUid()).thenReturn(testUid);
        WorkoutRecordRepository mockWorkoutRecordRepository = Mockito.mock(WorkoutRecordRepository.class);
        WorkoutRecordService testedService = new WorkoutRecordService(mockActivity, mockWorkoutRecordRepository);
        WorkoutRecord testRecord = new WorkoutRecord(testUid,9999L,9999L,9999L,9999L,9999L);
        Map<String, Object> metadata = new HashMap<>();
        metadata.put(DomainService.ORIGIN_FUNCTION,"checkForRecords");
        metadata.put("workoutRecordDistance", WorkoutRecordDistance.MARATHON);
        metadata.put("workoutTime", testWorkoutTime);

        testedService.repositoryResponse(testRecord, metadata);
        verify(mockNotificationDeviceService, times(0)).sendNotification(anyString(),anyString(),anyInt());
        verify(mockWorkoutRecordRepository, times(0)).updateWorkoutRecord(anyString(),anyLong(),any(),anyMap());
    }
}
