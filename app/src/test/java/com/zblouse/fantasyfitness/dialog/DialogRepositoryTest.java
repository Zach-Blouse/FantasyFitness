package com.zblouse.fantasyfitness.dialog;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.HashMap;

public class DialogRepositoryTest {

    @Test
    public void writeDialogTest(){
        String testReferenceId = "testDialog";
        Dialog testDialog = new Dialog(testReferenceId,"flavorText", "optionText", new DialogAffect(DialogAffectType.NONE), false);
        DialogSqlDatabase mockDialogSqlDatabase = Mockito.mock(DialogSqlDatabase.class);
        DialogFirestoreDatabase mockFirestoreDatabase = Mockito.mock(DialogFirestoreDatabase.class);
        DialogService mockDialogService = Mockito.mock(DialogService.class);
        DialogRepository testedRepository = new DialogRepository(mockDialogService, mockDialogSqlDatabase,mockFirestoreDatabase);

        testedRepository.writeDialog(testDialog);
        ArgumentCaptor<Dialog> dialogArgumentCaptor = ArgumentCaptor.forClass(Dialog.class);
        verify(mockDialogSqlDatabase).addDialogToDatabase(dialogArgumentCaptor.capture());
        assertEquals(testDialog,dialogArgumentCaptor.getValue());
    }

    @Test
    public void readDialogTest(){
        String testReferenceId = "testDialog";
        Dialog testDialog = new Dialog(7,testReferenceId, "optionText","flavorText","option1","option2", "option3", "option4",new DialogAffect(DialogAffectType.NONE),false);
        DialogSqlDatabase mockDialogSqlDatabase = Mockito.mock(DialogSqlDatabase.class);
        when(mockDialogSqlDatabase.getDialogByReferenceId(eq(testReferenceId))).thenReturn(testDialog);
        DialogFirestoreDatabase mockFirestoreDatabase = Mockito.mock(DialogFirestoreDatabase.class);
        DialogService mockDialogService = Mockito.mock(DialogService.class);
        DialogRepository testedRepository = new DialogRepository(mockDialogService, mockDialogSqlDatabase,mockFirestoreDatabase);

        Dialog responseDialog = testedRepository.readDialog(testReferenceId);

        assertEquals(testDialog.getId(),responseDialog.getId());
    }

    @Test
    public void writeQuestDialogTest(){
        String testReferenceId = "testDialog";
        Dialog testDialog = new Dialog(testReferenceId,"flavorText", "optionText", new DialogAffect(DialogAffectType.NONE), true);
        DialogSqlDatabase mockDialogSqlDatabase = Mockito.mock(DialogSqlDatabase.class);
        DialogFirestoreDatabase mockFirestoreDatabase = Mockito.mock(DialogFirestoreDatabase.class);
        DialogService mockDialogService = Mockito.mock(DialogService.class);
        DialogRepository testedRepository = new DialogRepository(mockDialogService, mockDialogSqlDatabase,mockFirestoreDatabase);

        testedRepository.writeQuestDialog(testDialog,"testUser", new HashMap<>());

        verify(mockFirestoreDatabase).write(eq(testDialog), eq("testUser"), eq(testedRepository), anyMap());
    }

    @Test
    public void readQuestDialogsTest(){
        DialogSqlDatabase mockDialogSqlDatabase = Mockito.mock(DialogSqlDatabase.class);
        DialogFirestoreDatabase mockFirestoreDatabase = Mockito.mock(DialogFirestoreDatabase.class);
        DialogService mockDialogService = Mockito.mock(DialogService.class);
        DialogRepository testedRepository = new DialogRepository(mockDialogService, mockDialogSqlDatabase,mockFirestoreDatabase);
        String testUserId = "testUser";
        String testDialog1 = "testDialog1";
        String testDialog2 = "testDialog2";
        String testDialog3 = "testDialog3";
        String testDialog4 = "testDialog4";

        testedRepository.readQuestDialogs(testUserId,testDialog1, testDialog2, testDialog3, testDialog4,new HashMap<>());
        verify(mockFirestoreDatabase).readList(eq(testUserId),eq(Arrays.asList(testDialog1, testDialog2, testDialog3, testDialog4)),eq(testedRepository), anyMap());
    }

    @Test
    public void readQuestDialogTest(){
        DialogSqlDatabase mockDialogSqlDatabase = Mockito.mock(DialogSqlDatabase.class);
        DialogFirestoreDatabase mockFirestoreDatabase = Mockito.mock(DialogFirestoreDatabase.class);
        DialogService mockDialogService = Mockito.mock(DialogService.class);
        DialogRepository testedRepository = new DialogRepository(mockDialogService, mockDialogSqlDatabase,mockFirestoreDatabase);
        String testUserId = "testUser";
        String testDialog1 = "testDialog1";

        testedRepository.readQuestDialog(testUserId, testDialog1,new HashMap<>());

        verify(mockFirestoreDatabase).read(eq(testUserId), eq(testDialog1), eq(testedRepository), anyMap());
    }

    @Test
    public void readCallbackTest(){
        DialogSqlDatabase mockDialogSqlDatabase = Mockito.mock(DialogSqlDatabase.class);
        DialogFirestoreDatabase mockFirestoreDatabase = Mockito.mock(DialogFirestoreDatabase.class);
        DialogService mockDialogService = Mockito.mock(DialogService.class);
        DialogRepository testedRepository = new DialogRepository(mockDialogService, mockDialogSqlDatabase,mockFirestoreDatabase);
        String testReferenceId = "testDialog";
        Dialog testDialog = new Dialog(7,testReferenceId, "optionText","flavorText","option1","option2", "option3", "option4",new DialogAffect(DialogAffectType.NONE),false);

        testedRepository.readCallback(testDialog,new HashMap<>());

        verify(mockDialogService).repositoryResponse(eq(testDialog),anyMap());
    }

    @Test
    public void readListCallbackTest(){
        DialogSqlDatabase mockDialogSqlDatabase = Mockito.mock(DialogSqlDatabase.class);
        DialogFirestoreDatabase mockFirestoreDatabase = Mockito.mock(DialogFirestoreDatabase.class);
        DialogService mockDialogService = Mockito.mock(DialogService.class);
        DialogRepository testedRepository = new DialogRepository(mockDialogService, mockDialogSqlDatabase,mockFirestoreDatabase);
        String testReferenceId = "testDialog";
        Dialog testDialog1 = new Dialog(7,testReferenceId, "optionText","flavorText","option1","option2", "option3", "option4",new DialogAffect(DialogAffectType.NONE),false);
        String testReferenceId2 = "testDialog2";
        Dialog testDialog2 = new Dialog(7,testReferenceId2, "optionText","flavorText","option1","option2", "option3", "option4",new DialogAffect(DialogAffectType.NONE),false);


        testedRepository.readListCallback(Arrays.asList(testDialog1, testDialog2),new HashMap<>());

        verify(mockDialogService).repositoryListResponse(eq(Arrays.asList(testDialog1, testDialog2)),anyMap());
    }

}
