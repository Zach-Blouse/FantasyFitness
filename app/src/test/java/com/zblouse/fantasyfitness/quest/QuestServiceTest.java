package com.zblouse.fantasyfitness.quest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.firebase.auth.FirebaseUser;
import com.zblouse.fantasyfitness.R;
import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.dialog.DialogService;
import com.zblouse.fantasyfitness.user.UserGameStateService;
import com.zblouse.fantasyfitness.world.GameLocation;
import com.zblouse.fantasyfitness.world.GameLocationService;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestServiceTest {

    @Test
    public void fetchQuestsTest(){
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        String testUserId = "testUserId";
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        when(mockActivity.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.getUid()).thenReturn(testUserId);
        QuestRepository mockRepository = Mockito.mock(QuestRepository.class);

        QuestService testedService = new QuestService();
        testedService.setQuestRepository(mockRepository);
        testedService.setMainActivity(mockActivity);

        testedService.fetchQuests(new HashMap<>());

        verify(mockRepository).fetchQuests(eq(testUserId), anyMap());
    }

    @Test
    public void startQuestSuccessTest(){
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        String testUserId = "testUserId";
        String testQuestUid = "testUuid";
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        when(mockActivity.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.getUid()).thenReturn(testUserId);
        QuestRepository mockRepository = Mockito.mock(QuestRepository.class);

        QuestObjective objective1 = new QuestObjective(QuestObjectiveType.FIGHT,"1", GameLocationService.WOODLANDS,1,false);
        QuestObjective objective2 = new QuestObjective(QuestObjectiveType.VISIT,"1", GameLocationService.THANADEL_VILLAGE,1,false);
        Quest quest = new Quest("testQuest", testQuestUid, 8, Arrays.asList(objective1, objective2));
        when(mockRepository.loadTempQuest(eq(mockActivity),eq(testQuestUid))).thenReturn(quest);

        QuestService testedService = new QuestService();
        testedService.setQuestRepository(mockRepository);
        testedService.setMainActivity(mockActivity);

        testedService.startQuest(testQuestUid);

        verify(mockRepository).writeQuest(eq(quest),eq(testUserId),anyMap());
        verify(mockRepository).deleteTempQuest(eq(mockActivity), eq(testQuestUid));

    }

    @Test
    public void startQuestExceptionTest(){
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        String testUserId = "testUserId";
        String testQuestUid = "testUuid";
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        when(mockActivity.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.getUid()).thenReturn(testUserId);
        QuestRepository mockRepository = Mockito.mock(QuestRepository.class);

        when(mockRepository.loadTempQuest(eq(mockActivity),eq(testQuestUid))).thenReturn(null);

        QuestService testedService = new QuestService();
        testedService.setQuestRepository(mockRepository);
        testedService.setMainActivity(mockActivity);

        testedService.startQuest(testQuestUid);

        verify(mockRepository, times(0)).writeQuest(any(),any(),anyMap());
        verify(mockRepository, times(0)).deleteTempQuest(any(), any());

    }

    @Test
    public void deleteQuestTest(){
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        String testUserId = "testUserId";
        String testQuestUid = "testUuid";
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        when(mockActivity.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.getUid()).thenReturn(testUserId);
        QuestRepository mockRepository = Mockito.mock(QuestRepository.class);

        QuestObjective objective1 = new QuestObjective(QuestObjectiveType.FIGHT,"1", GameLocationService.WOODLANDS,1,false);
        QuestObjective objective2 = new QuestObjective(QuestObjectiveType.VISIT,"1", GameLocationService.THANADEL_VILLAGE,1,false);
        Quest quest = new Quest("testQuest", testQuestUid, 8, Arrays.asList(objective1, objective2));

        QuestService testedService = new QuestService();
        testedService.setQuestRepository(mockRepository);
        testedService.setMainActivity(mockActivity);

        testedService.deleteQuest(quest, new HashMap<>());

        verify(mockRepository).deleteQuest(eq(quest),eq(testUserId),anyMap());

    }

    @Test
    public void generateQuestsTest(){
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        String testUserId = "testUserId";
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        when(mockActivity.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.getUid()).thenReturn(testUserId);
        DialogService mockDialogService = Mockito.mock(DialogService.class);
        when(mockActivity.getDialogService()).thenReturn(mockDialogService);
        QuestRepository mockRepository = Mockito.mock(QuestRepository.class);

        QuestService testedService = new QuestService();
        testedService.setQuestRepository(mockRepository);
        testedService.setMainActivity(mockActivity);

        List<Quest> questListResponse = testedService.generateQuests(GameLocationService.THANADEL_VILLAGE,2);

        verify(mockRepository, times(2)).saveTempQuest(eq(mockActivity), any(Quest.class));

        assertEquals(2, questListResponse.size());
    }

    @Test
    public void potentialCombatQuestObjectiveCompletedTest(){
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        String testUserId = "testUserId";
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        when(mockActivity.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.getUid()).thenReturn(testUserId);
        DialogService mockDialogService = Mockito.mock(DialogService.class);
        when(mockActivity.getDialogService()).thenReturn(mockDialogService);
        QuestRepository mockRepository = Mockito.mock(QuestRepository.class);

        QuestService testedService = new QuestService();
        testedService.setQuestRepository(mockRepository);
        testedService.setMainActivity(mockActivity);

        testedService.potentialCombatQuestObjectiveCompleted(GameLocationService.WOODLANDS, R.id.dark_forest_button);

        ArgumentCaptor<Map> metadataArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        verify(mockRepository).fetchQuests(eq(testUserId),metadataArgumentCaptor.capture());

        assertTrue(metadataArgumentCaptor.getValue().containsKey("questObjectiveCheckFetch"));
        assertTrue(metadataArgumentCaptor.getValue().containsKey("combatObjectiveBuildingKey"));
        assertEquals(R.id.dark_forest_button, metadataArgumentCaptor.getValue().get("combatObjectiveBuildingKey"));
        assertTrue(metadataArgumentCaptor.getValue().containsKey("combatObjectiveLocationKey"));
        assertEquals(GameLocationService.WOODLANDS, metadataArgumentCaptor.getValue().get("combatObjectiveLocationKey"));
    }

    @Test
    public void dialogQuestObjectiveCompletedTest(){
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        String testUserId = "testUserId";
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        when(mockActivity.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.getUid()).thenReturn(testUserId);
        DialogService mockDialogService = Mockito.mock(DialogService.class);
        when(mockActivity.getDialogService()).thenReturn(mockDialogService);
        QuestRepository mockRepository = Mockito.mock(QuestRepository.class);

        QuestService testedService = new QuestService();
        testedService.setQuestRepository(mockRepository);
        testedService.setMainActivity(mockActivity);

        String testQuestUuid = "questUuid";
        String objectiveUuid = "objectiveUuid";

        testedService.dialogQuestObjectiveCompleted(testQuestUuid, objectiveUuid);

        ArgumentCaptor<Map> metadataArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        verify(mockRepository).getQuest(eq(testUserId),eq(testQuestUuid),metadataArgumentCaptor.capture());

        assertTrue(metadataArgumentCaptor.getValue().containsKey("questObjectiveCheckFetch"));
        assertTrue(metadataArgumentCaptor.getValue().containsKey("questObjectiveUuidMetadataKey"));
        assertEquals(objectiveUuid, metadataArgumentCaptor.getValue().get("questObjectiveUuidMetadataKey"));
        assertTrue(metadataArgumentCaptor.getValue().containsKey("questUuidMetadataKey"));
        assertEquals(testQuestUuid, metadataArgumentCaptor.getValue().get("questUuidMetadataKey"));
    }

    @Test
    public void questFetchResponseNotObjectiveCheckTest(){
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        String testUserId = "testUserId";
        String testQuest1Uid = "testUuid1";
        String testQuest2Uid = "testUuid2";
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        when(mockActivity.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.getUid()).thenReturn(testUserId);
        QuestRepository mockRepository = Mockito.mock(QuestRepository.class);

        QuestObjective objective1 = new QuestObjective(QuestObjectiveType.FIGHT,"1", GameLocationService.WOODLANDS,1,false);
        QuestObjective objective2 = new QuestObjective(QuestObjectiveType.VISIT,"2", GameLocationService.THANADEL_VILLAGE,1,false);
        Quest quest1 = new Quest("testQuest", testQuest1Uid, 8, Arrays.asList(objective1, objective2));

        QuestObjective objective3 = new QuestObjective(QuestObjectiveType.FIGHT,"3", GameLocationService.WOODLANDS,1,false);
        QuestObjective objective4 = new QuestObjective(QuestObjectiveType.VISIT,"4", GameLocationService.THANADEL_VILLAGE,1,false);
        Quest quest2 = new Quest("testQuest", testQuest2Uid, 8, Arrays.asList(objective3, objective4));

        QuestService testedService = new QuestService();
        testedService.setQuestRepository(mockRepository);
        testedService.setMainActivity(mockActivity);

        testedService.questFetchResponse(Arrays.asList(quest1, quest2), new HashMap<>());

        ArgumentCaptor<QuestFetchResponseEvent> questFetchResponseEventArgumentCaptor = ArgumentCaptor.forClass(QuestFetchResponseEvent.class);

        verify(mockActivity).publishEvent(questFetchResponseEventArgumentCaptor.capture());

        assertEquals(quest1, questFetchResponseEventArgumentCaptor.getValue().getQuests().get(0));
        assertEquals(quest2, questFetchResponseEventArgumentCaptor.getValue().getQuests().get(1));
    }

    @Test
    public void questFetchResponseObjectiveCheckTest(){
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        String testUserId = "testUserId";
        String testQuest1Uid = "testUuid1";
        String testQuest2Uid = "testUuid2";
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        when(mockActivity.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.getUid()).thenReturn(testUserId);
        UserGameStateService mockUserGameStateService = Mockito.mock(UserGameStateService.class);
        when(mockActivity.getUserGameStateService()).thenReturn(mockUserGameStateService);
        QuestRepository mockRepository = Mockito.mock(QuestRepository.class);

        QuestObjective objective1 = new QuestObjective(QuestObjectiveType.FIGHT,"1", GameLocationService.WOODLANDS,R.id.dark_forest_button,false);
        QuestObjective objective2 = new QuestObjective(QuestObjectiveType.VISIT,"2", GameLocationService.THANADEL_VILLAGE,1,true);
        Quest quest1 = new Quest("testQuest", testQuest1Uid, 8, Arrays.asList(objective1, objective2));

        QuestObjective objective3 = new QuestObjective(QuestObjectiveType.FIGHT,"3", GameLocationService.RIVERLANDS,1,false);
        QuestObjective objective4 = new QuestObjective(QuestObjectiveType.VISIT,"4", GameLocationService.THANADEL_VILLAGE,1,false);
        Quest quest2 = new Quest("testQuest", testQuest2Uid, 15, Arrays.asList(objective3, objective4));

        QuestService testedService = new QuestService();
        testedService.setQuestRepository(mockRepository);
        testedService.setMainActivity(mockActivity);

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("questObjectiveCheckFetch", true);
        metadata.put("combatObjectiveLocationKey", GameLocationService.WOODLANDS);
        metadata.put("combatObjectiveBuildingKey",R.id.dark_forest_button);

        testedService.questFetchResponse(Arrays.asList(quest1, quest2), metadata);

        ArgumentCaptor<Quest> questArgumentCaptor = ArgumentCaptor.forClass(Quest.class);

        verify(mockRepository).writeQuest(questArgumentCaptor.capture(),eq(testUserId), anyMap());
        assertTrue(questArgumentCaptor.getValue().getQuestObjectives().get(0).isObjectiveMet());

        verify(mockUserGameStateService).modifyUserGameCurrency(eq(testUserId),eq(quest1.getQuestReward()),anyMap());
        verify(mockUserGameStateService, times(0)).modifyUserGameCurrency(eq(testUserId),eq(quest2.getQuestReward()),anyMap());

        ArgumentCaptor<QuestCompletedEvent> questCompletedEventArgumentCaptor = ArgumentCaptor.forClass(QuestCompletedEvent.class);
        verify(mockActivity).publishEvent(questCompletedEventArgumentCaptor.capture());

        assertEquals(quest1.getQuestUuid(),questCompletedEventArgumentCaptor.getValue().getQuest().getQuestUuid());

    }

    @Test
    public void repositoryResponseTest(){
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        String testUserId = "testUserId";
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        when(mockActivity.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.getUid()).thenReturn(testUserId);
        DialogService mockDialogService = Mockito.mock(DialogService.class);
        when(mockActivity.getDialogService()).thenReturn(mockDialogService);
        UserGameStateService mockUserGameStateService = Mockito.mock(UserGameStateService.class);
        when(mockActivity.getUserGameStateService()).thenReturn(mockUserGameStateService);
        QuestRepository mockRepository = Mockito.mock(QuestRepository.class);

        QuestService testedService = new QuestService();
        testedService.setQuestRepository(mockRepository);
        testedService.setMainActivity(mockActivity);

        String testQuest1Uid = "testUuid1";
        QuestObjective objective1 = new QuestObjective(QuestObjectiveType.FIGHT,"1", GameLocationService.WOODLANDS,R.id.dark_forest_button,true);
        QuestObjective objective2 = new QuestObjective(QuestObjectiveType.VISIT,"2", GameLocationService.THANADEL_VILLAGE,1,false);
        Quest quest1 = new Quest("testQuest", testQuest1Uid, 8, Arrays.asList(objective1, objective2));

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("questObjectiveCheckFetch", true);
        metadata.put("questObjectiveUuidMetadataKey","2");

        testedService.repositoryResponse(quest1, metadata);

        ArgumentCaptor<Quest> questArgumentCaptor = ArgumentCaptor.forClass(Quest.class);

        verify(mockRepository).writeQuest(questArgumentCaptor.capture(),eq(testUserId), anyMap());
        assertTrue(questArgumentCaptor.getValue().getQuestObjectives().get(1).isObjectiveMet());
        verify(mockUserGameStateService).modifyUserGameCurrency(eq(testUserId),eq(quest1.getQuestReward()),anyMap());

        ArgumentCaptor<QuestCompletedEvent> questCompletedEventArgumentCaptor = ArgumentCaptor.forClass(QuestCompletedEvent.class);
        verify(mockActivity).publishEvent(questCompletedEventArgumentCaptor.capture());

        assertEquals(quest1.getQuestUuid(),questCompletedEventArgumentCaptor.getValue().getQuest().getQuestUuid());
    }

}
