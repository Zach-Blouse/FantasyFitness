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
        Dialog testDialog = new Dialog(testReferenceId,"flavorText", "optionText");
        DialogSqlDatabase mockDialogSqlDatabase = Mockito.mock(DialogSqlDatabase.class);

        DialogRepository testedRepository = new DialogRepository(mockDialogSqlDatabase);

        testedRepository.writeDialog(testDialog);
        ArgumentCaptor<Dialog> dialogArgumentCaptor = ArgumentCaptor.forClass(Dialog.class);
        verify(mockDialogSqlDatabase).addDialogToDatabase(dialogArgumentCaptor.capture());
        assertEquals(testDialog,dialogArgumentCaptor.getValue());
    }

    @Test
    public void readDialogTest(){
        String testReferenceId = "testDialog";
        Dialog testDialog = new Dialog(testReferenceId,"flavorText", "optionText");
        DialogSqlDatabase mockDialogSqlDatabase = Mockito.mock(DialogSqlDatabase.class);
        when(mockDialogSqlDatabase.getDialogByReferenceId(eq(testReferenceId))).thenReturn(testDialog);
        DialogRepository testedRepository = new DialogRepository(mockDialogSqlDatabase);

        Dialog responseDialog = testedRepository.readDialog(testReferenceId);

        assertEquals(testDialog,responseDialog);
    }

}
