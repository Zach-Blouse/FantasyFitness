package com.zblouse.fantasyfitness.dialog;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.firebase.auth.FirebaseUser;
import com.zblouse.fantasyfitness.R;
import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.quest.Quest;
import com.zblouse.fantasyfitness.quest.QuestObjective;
import com.zblouse.fantasyfitness.quest.QuestObjectiveType;
import com.zblouse.fantasyfitness.quest.QuestService;
import com.zblouse.fantasyfitness.world.GameLocationService;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.HashMap;

public class DialogServiceTest {

    @Test
    public void fetchDialogOptionTest(){
        String testReferenceId = "testDialog";
        Dialog testDialog = new Dialog(testReferenceId,"flavorText", "optionText", new DialogAffect(DialogAffectType.NONE), false);
        MainActivity mockMainActivity = Mockito.mock(MainActivity.class);
        DialogRepository mockDialogRepository = Mockito.mock(DialogRepository.class);
        when(mockDialogRepository.readDialog(testReferenceId)).thenReturn(testDialog);
        DialogService testedDialogService = new DialogService(mockMainActivity, mockDialogRepository);
        testedDialogService.fetchBaseDialog(testReferenceId, false, new HashMap<>());
        ArgumentCaptor<BaseDialogFetchEvent> baseDialogFetchEventArgumentCaptor = ArgumentCaptor.forClass(BaseDialogFetchEvent.class);
        verify(mockMainActivity).publishEvent(baseDialogFetchEventArgumentCaptor.capture());
        assertEquals(testDialog, baseDialogFetchEventArgumentCaptor.getValue().getDialog());
    }

    @Test
    public void selectDialogOptionTest(){
        String testReferenceId = "testDialog";
        String testOption1ReferenceId = "testDialog1";
        String testOption2ReferenceId = "testDialog2";
        String testOption3ReferenceId = "testDialog3";
        String testOption4ReferenceId = "testDialog4";
        Dialog testDialog = new Dialog(1,testReferenceId,"flavorText", "optionText", testOption1ReferenceId, testOption2ReferenceId, testOption3ReferenceId, testOption4ReferenceId, new DialogAffect(DialogAffectType.NONE), false);

        MainActivity mockMainActivity = Mockito.mock(MainActivity.class);
        DialogRepository mockDialogRepository = Mockito.mock(DialogRepository.class);
        when(mockDialogRepository.readDialog(testReferenceId)).thenReturn(testDialog);
        DialogService testedDialogService = new DialogService(mockMainActivity, mockDialogRepository);
        testedDialogService.selectDialogOption(testDialog, GameLocationService.FAOLYN, new HashMap<String, Object>());
        ArgumentCaptor<DialogSelectedEvent> dialogSelectedEventArgumentCaptor = ArgumentCaptor.forClass(DialogSelectedEvent.class);
        verify(mockMainActivity).publishEvent(dialogSelectedEventArgumentCaptor.capture());
        assertEquals(testDialog, dialogSelectedEventArgumentCaptor.getValue().getNewDialog());
    }

    @Test
    public void selectDialogOptionQuestGenerateTest(){
        String testReferenceId = "testDialog";
        Dialog testDialog = new Dialog(testReferenceId,"flavorText", "optionText", new DialogAffect(DialogAffectType.QUEST_GENERATE), false);
        MainActivity mockMainActivity = Mockito.mock(MainActivity.class);
        FirebaseUser firebaseUser = Mockito.mock(FirebaseUser.class);
        when(mockMainActivity.getCurrentUser()).thenReturn(firebaseUser);
        when(firebaseUser.getUid()).thenReturn("userId");
        DialogRepository mockDialogRepository = Mockito.mock(DialogRepository.class);
        when(mockDialogRepository.readDialog(testReferenceId)).thenReturn(testDialog);
        QuestService mockQuestService = Mockito.mock(QuestService.class);
        when(mockMainActivity.getQuestService()).thenReturn(mockQuestService);
        QuestObjective objective1 = new QuestObjective(QuestObjectiveType.VISIT,"1", GameLocationService.FARMLANDS, R.id.inn_button,false);
        QuestObjective objective2 = new QuestObjective(QuestObjectiveType.FIGHT,"2", GameLocationService.WOODLANDS,R.id.cave_button,false);
        QuestObjective objective3 = new QuestObjective(QuestObjectiveType.VISIT,"3", GameLocationService.THANADEL_VILLAGE,R.id.inn_button,false);

        Quest quest1 = new Quest("testQuest", "testUuid1", 8, Arrays.asList(objective1, objective2, objective3));
        Quest quest2 = new Quest("testQuest", "testUuid2", 8, Arrays.asList(objective1, objective2, objective3));
        Quest quest3 = new Quest("testQuest", "testUuid3", 8, Arrays.asList(objective1, objective2, objective3));
        Quest quest4 = new Quest("testQuest", "testUuid4", 8, Arrays.asList(objective1, objective2, objective3));

        when(mockQuestService.generateQuests(eq(GameLocationService.THANADEL_VILLAGE),eq(3))).thenReturn(Arrays.asList(quest1, quest2, quest3, quest4));

        DialogService testedDialogService = new DialogService(mockMainActivity, mockDialogRepository);

        testedDialogService.selectDialogOption(testDialog, GameLocationService.THANADEL_VILLAGE, new HashMap<String, Object>());

        verify(mockDialogRepository, times(4)).writeQuestDialog(any(Dialog.class), eq("userId"), anyMap());
        ArgumentCaptor<DialogSelectedEvent> dialogSelectedEventArgumentCaptor = ArgumentCaptor.forClass(DialogSelectedEvent.class);
        verify(mockMainActivity).publishEvent(dialogSelectedEventArgumentCaptor.capture());
        assertEquals(testDialog, dialogSelectedEventArgumentCaptor.getValue().getNewDialog());
        assertNotNull(dialogSelectedEventArgumentCaptor.getValue().getNewDialog().getDialogOption1());
    }

    @Test
    public void initializeDialogsTest(){
        MainActivity mockMainActivity = Mockito.mock(MainActivity.class);
        DialogRepository mockDialogRepository = Mockito.mock(DialogRepository.class);
        DialogService testedDialogService = new DialogService(mockMainActivity, mockDialogRepository);
        testedDialogService.initializeDialogs();
        verify(mockDialogRepository,times(39)).writeDialog(any());
    }

    @Test
    public void fetchDialogOptionsQuestDialogTest(){
        String testReferenceId = "testDialog";
        String testOption1ReferenceId = "testDialog1";
        String testOption2ReferenceId = "testDialog2";
        String testOption3ReferenceId = "testDialog3";
        String testOption4ReferenceId = "testDialog4";

        Dialog testDialog = new Dialog(testReferenceId,"flavorText", "optionText", new DialogAffect(DialogAffectType.NONE), true);
        testDialog.setDialogOption1(testOption1ReferenceId);
        testDialog.setDialogOption2(testOption2ReferenceId);
        testDialog.setDialogOption3(testOption3ReferenceId);
        testDialog.setDialogOption4(testOption4ReferenceId);
        MainActivity mockMainActivity = Mockito.mock(MainActivity.class);
        String testUserId = "testUserId";
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        when(mockMainActivity.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.getUid()).thenReturn(testUserId);
        DialogRepository mockDialogRepository = Mockito.mock(DialogRepository.class);
        when(mockDialogRepository.readDialog(testReferenceId)).thenReturn(testDialog);
        DialogService testedDialogService = new DialogService(mockMainActivity, mockDialogRepository);

        testedDialogService.fetchDialogOptions(testDialog,new HashMap<>());

        verify(mockDialogRepository).readQuestDialogs(eq(testUserId), eq(testOption1ReferenceId), eq(testOption2ReferenceId), eq(testOption3ReferenceId), eq(testOption4ReferenceId), anyMap());
    }

    @Test
    public void fetchDialogOptionsNotQuestDialogTest(){
        String testReferenceId = "testDialog";
        String testOption1ReferenceId = "testDialog1";
        String testOption2ReferenceId = "testDialog2";
        String testOption3ReferenceId = "testDialog3";
        String testOption4ReferenceId = "testDialog4";

        Dialog testDialog = new Dialog(testReferenceId,"flavorText", "optionText", new DialogAffect(DialogAffectType.NONE), false);
        testDialog.setDialogOption1(testOption1ReferenceId);
        testDialog.setDialogOption2(testOption2ReferenceId);
        testDialog.setDialogOption3(testOption3ReferenceId);
        testDialog.setDialogOption4(testOption4ReferenceId);
        MainActivity mockMainActivity = Mockito.mock(MainActivity.class);
        String testUserId = "testUserId";
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        when(mockMainActivity.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.getUid()).thenReturn(testUserId);
        DialogRepository mockDialogRepository = Mockito.mock(DialogRepository.class);
        when(mockDialogRepository.readDialog(testReferenceId)).thenReturn(testDialog);
        DialogService testedDialogService = new DialogService(mockMainActivity, mockDialogRepository);

        Dialog testDialog1 = new Dialog(testOption1ReferenceId,"flavorText", "optionText", new DialogAffect(DialogAffectType.NONE), false);
        when(mockDialogRepository.readDialog(eq(testOption1ReferenceId))).thenReturn(testDialog1);
        Dialog testDialog2 = new Dialog(testOption2ReferenceId,"flavorText", "optionText", new DialogAffect(DialogAffectType.NONE), false);
        when(mockDialogRepository.readDialog(eq(testOption2ReferenceId))).thenReturn(testDialog2);
        Dialog testDialog3 = new Dialog(testOption3ReferenceId,"flavorText", "optionText", new DialogAffect(DialogAffectType.NONE), false);
        when(mockDialogRepository.readDialog(eq(testOption3ReferenceId))).thenReturn(testDialog3);
        Dialog testDialog4 = new Dialog(testOption4ReferenceId,"flavorText", "optionText", new DialogAffect(DialogAffectType.NONE), false);
        when(mockDialogRepository.readDialog(eq(testOption4ReferenceId))).thenReturn(testDialog4);

        testedDialogService.fetchDialogOptions(testDialog,new HashMap<>());

        ArgumentCaptor<DialogFetchEvent> dialogFetchEventArgumentCaptor = ArgumentCaptor.forClass(DialogFetchEvent.class);

        verify(mockMainActivity).publishEvent(dialogFetchEventArgumentCaptor.capture());

        assertEquals(testDialog1,dialogFetchEventArgumentCaptor.getValue().getDialogOption1());
        assertEquals(testDialog2,dialogFetchEventArgumentCaptor.getValue().getDialogOption2());
        assertEquals(testDialog3,dialogFetchEventArgumentCaptor.getValue().getDialogOption3());
        assertEquals(testDialog4,dialogFetchEventArgumentCaptor.getValue().getDialogOption4());
    }

}
