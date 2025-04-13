package com.zblouse.fantasyfitness.dialog;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

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

}
