package com.zblouse.fantasyfitness.quest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;

import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.world.GameLocationService;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.robolectric.versioning.AndroidVersions;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class QuestRepositoryTest {

    @Test
    public void saveTempQuestTest() throws FileNotFoundException {
        QuestService mockQuestService = Mockito.mock(QuestService.class);
        QuestFirestoreDatabase mockFirestoreDatabase = Mockito.mock(QuestFirestoreDatabase.class);
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        FileOutputStream mockFileOutputStream = Mockito.mock(FileOutputStream.class);
        when(mockActivity.openFileOutput(eq("testUuid.tmp"), eq(Context.MODE_PRIVATE))).thenReturn(mockFileOutputStream);

        QuestRepository testedRepository = new QuestRepository(mockQuestService, mockFirestoreDatabase);

        QuestObjective objective1 = new QuestObjective(QuestObjectiveType.FIGHT,"1", GameLocationService.WOODLANDS,1,false);
        QuestObjective objective2 = new QuestObjective(QuestObjectiveType.VISIT,"1", GameLocationService.THANADEL_VILLAGE,1,false);

        Quest quest = new Quest("testQuest", "testUuid", 8, Arrays.asList(objective1, objective2));

        boolean response = testedRepository.saveTempQuest(mockActivity, quest);
        assertTrue(response);
    }

    @Test
    public void saveTempQuestErrorTest() throws FileNotFoundException {
        QuestService mockQuestService = Mockito.mock(QuestService.class);
        QuestFirestoreDatabase mockFirestoreDatabase = Mockito.mock(QuestFirestoreDatabase.class);
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        when(mockActivity.openFileOutput(eq("testUuid.tmp"), eq(Context.MODE_PRIVATE))).thenThrow(new FileNotFoundException());

        QuestRepository testedRepository = new QuestRepository(mockQuestService, mockFirestoreDatabase);

        QuestObjective objective1 = new QuestObjective(QuestObjectiveType.FIGHT,"1", GameLocationService.WOODLANDS,1,false);
        QuestObjective objective2 = new QuestObjective(QuestObjectiveType.VISIT,"1", GameLocationService.THANADEL_VILLAGE,1,false);

        Quest quest = new Quest("testQuest", "testUuid", 8, Arrays.asList(objective1, objective2));

        boolean response = testedRepository.saveTempQuest(mockActivity, quest);
        assertFalse(response);
    }

    @Test
    public void loadAndDeleteTempQuestTest() throws IOException {
        QuestService mockQuestService = Mockito.mock(QuestService.class);
        QuestFirestoreDatabase mockFirestoreDatabase = Mockito.mock(QuestFirestoreDatabase.class);
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        String testUuid = UUID.randomUUID().toString();
        QuestObjective objective1 = new QuestObjective(QuestObjectiveType.FIGHT,"1", GameLocationService.WOODLANDS,1,false);
        QuestObjective objective2 = new QuestObjective(QuestObjectiveType.VISIT,"2", GameLocationService.THANADEL_VILLAGE,1,false);

        Quest quest = new Quest("testQuest", testUuid, 8, Arrays.asList(objective1, objective2));

        FileOutputStream outputStream = new FileOutputStream(testUuid + ".tmp");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(quest);
        objectOutputStream.close();
        outputStream.close();

        FileInputStream fileInputStream = new FileInputStream(testUuid + ".tmp");
        when(mockActivity.openFileInput(eq(testUuid + ".tmp"))).thenReturn(fileInputStream);

        QuestRepository testedRepository = new QuestRepository(mockQuestService, mockFirestoreDatabase);

        Quest returnedQuest = testedRepository.loadTempQuest(mockActivity, testUuid);

        assertEquals(quest.getQuestUuid(), returnedQuest.getQuestUuid());
        assertEquals(quest.getQuestDescription(), returnedQuest.getQuestDescription());
        assertEquals(quest.getQuestName(), returnedQuest.getQuestName());
        assertEquals(quest.getQuestReward(), returnedQuest.getQuestReward());
        assertEquals(quest.getQuestObjectives().get(0).getQuestObjectiveUuid(), returnedQuest.getQuestObjectives().get(0).getQuestObjectiveUuid());
        assertEquals(quest.getQuestObjectives().get(1).getQuestObjectiveUuid(), returnedQuest.getQuestObjectives().get(1).getQuestObjectiveUuid());

        //test the delete to clean up data
        boolean response = testedRepository.deleteTempQuest(mockActivity,testUuid);
        assertTrue(response);
    }

    @Test
    public void loadTempQuestErrorTest() throws FileNotFoundException {
        QuestService mockQuestService = Mockito.mock(QuestService.class);
        QuestFirestoreDatabase mockFirestoreDatabase = Mockito.mock(QuestFirestoreDatabase.class);
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        when(mockActivity.openFileInput(eq("testUuid.tmp"))).thenThrow(new FileNotFoundException());

        QuestRepository testedRepository = new QuestRepository(mockQuestService, mockFirestoreDatabase);

        QuestObjective objective1 = new QuestObjective(QuestObjectiveType.FIGHT,"1", GameLocationService.WOODLANDS,1,false);
        QuestObjective objective2 = new QuestObjective(QuestObjectiveType.VISIT,"1", GameLocationService.THANADEL_VILLAGE,1,false);

        Quest response = testedRepository.loadTempQuest(mockActivity, "testUuid");
        assertNull(response);
    }

    @Test
    public void getQuestTest(){
        QuestService mockQuestService = Mockito.mock(QuestService.class);
        QuestFirestoreDatabase mockFirestoreDatabase = Mockito.mock(QuestFirestoreDatabase.class);
        QuestRepository testedRepository = new QuestRepository(mockQuestService, mockFirestoreDatabase);

        testedRepository.getQuest("testUser","testUuid", new HashMap<>());

        verify(mockFirestoreDatabase).readQuest(eq("testUser"),eq("testUuid"),eq(testedRepository), anyMap());
    }

    @Test
    public void writeQuestTest(){
        QuestService mockQuestService = Mockito.mock(QuestService.class);
        QuestFirestoreDatabase mockFirestoreDatabase = Mockito.mock(QuestFirestoreDatabase.class);
        QuestRepository testedRepository = new QuestRepository(mockQuestService, mockFirestoreDatabase);
        String testUuid = UUID.randomUUID().toString();
        QuestObjective objective1 = new QuestObjective(QuestObjectiveType.FIGHT,"1", GameLocationService.WOODLANDS,1,false);
        QuestObjective objective2 = new QuestObjective(QuestObjectiveType.VISIT,"2", GameLocationService.THANADEL_VILLAGE,1,false);

        Quest quest = new Quest("testQuest", testUuid, 8, Arrays.asList(objective1, objective2));

        testedRepository.writeQuest(quest, "testUserId", new HashMap<>());

        verify(mockFirestoreDatabase).writeQuest(eq(quest),eq("testUserId"),eq(testedRepository), anyMap());
    }

    @Test
    public void deleteQuestTest(){
        QuestService mockQuestService = Mockito.mock(QuestService.class);
        QuestFirestoreDatabase mockFirestoreDatabase = Mockito.mock(QuestFirestoreDatabase.class);
        QuestRepository testedRepository = new QuestRepository(mockQuestService, mockFirestoreDatabase);
        String testUuid = UUID.randomUUID().toString();
        QuestObjective objective1 = new QuestObjective(QuestObjectiveType.FIGHT,"1", GameLocationService.WOODLANDS,1,false);
        QuestObjective objective2 = new QuestObjective(QuestObjectiveType.VISIT,"2", GameLocationService.THANADEL_VILLAGE,1,false);

        Quest quest = new Quest("testQuest", testUuid, 8, Arrays.asList(objective1, objective2));

        testedRepository.deleteQuest(quest, "testUserId", new HashMap<>());

        verify(mockFirestoreDatabase).deleteQuest(eq(quest),eq("testUserId"),eq(testedRepository), anyMap());
    }

    @Test
    public void fetchQuestsTest(){
        QuestService mockQuestService = Mockito.mock(QuestService.class);
        QuestFirestoreDatabase mockFirestoreDatabase = Mockito.mock(QuestFirestoreDatabase.class);
        QuestRepository testedRepository = new QuestRepository(mockQuestService, mockFirestoreDatabase);

        testedRepository.fetchQuests("testUser", new HashMap<>());

        verify(mockFirestoreDatabase).fetchQuests(eq("testUser"),eq(testedRepository), anyMap());
    }

    @Test
    public void listReadCallbackTest(){
        QuestService mockQuestService = Mockito.mock(QuestService.class);
        QuestFirestoreDatabase mockFirestoreDatabase = Mockito.mock(QuestFirestoreDatabase.class);
        QuestRepository testedRepository = new QuestRepository(mockQuestService, mockFirestoreDatabase);

        String testUuid = UUID.randomUUID().toString();
        QuestObjective objective1 = new QuestObjective(QuestObjectiveType.FIGHT,"1", GameLocationService.WOODLANDS,1,false);
        QuestObjective objective2 = new QuestObjective(QuestObjectiveType.VISIT,"2", GameLocationService.THANADEL_VILLAGE,1,false);

        Quest quest1 = new Quest("testQuest", testUuid, 8, Arrays.asList(objective1, objective2));
        String testUuid2 = UUID.randomUUID().toString();
        QuestObjective objective3 = new QuestObjective(QuestObjectiveType.FIGHT,"3", GameLocationService.WOODLANDS,1,false);
        QuestObjective objective4 = new QuestObjective(QuestObjectiveType.VISIT,"4", GameLocationService.THANADEL_VILLAGE,1,false);

        Quest quest2 = new Quest("testQuest", testUuid2, 8, Arrays.asList(objective3, objective4));

        testedRepository.listReadCallback(Arrays.asList(quest1, quest2),new HashMap<>());

        ArgumentCaptor<List> listArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(mockQuestService).questFetchResponse(listArgumentCaptor.capture(),anyMap());

        assertEquals(quest1, listArgumentCaptor.getValue().get(0));
        assertEquals(quest2, listArgumentCaptor.getValue().get(1));
    }

    @Test
    public void readCallbackTest(){
        QuestService mockQuestService = Mockito.mock(QuestService.class);
        QuestFirestoreDatabase mockFirestoreDatabase = Mockito.mock(QuestFirestoreDatabase.class);
        QuestRepository testedRepository = new QuestRepository(mockQuestService, mockFirestoreDatabase);
        String testUuid = UUID.randomUUID().toString();
        QuestObjective objective1 = new QuestObjective(QuestObjectiveType.FIGHT,"1", GameLocationService.WOODLANDS,1,false);
        QuestObjective objective2 = new QuestObjective(QuestObjectiveType.VISIT,"2", GameLocationService.THANADEL_VILLAGE,1,false);

        Quest quest = new Quest("testQuest", testUuid, 8, Arrays.asList(objective1, objective2));

        testedRepository.readCallback(quest, new HashMap<>());

        verify(mockQuestService).repositoryResponse(eq(quest), anyMap());
    }
}
