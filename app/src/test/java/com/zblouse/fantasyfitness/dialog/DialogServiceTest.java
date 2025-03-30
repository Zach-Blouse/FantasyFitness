package com.zblouse.fantasyfitness.dialog;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.zblouse.fantasyfitness.activity.MainActivity;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.HashMap;

public class DialogServiceTest {

    @Test
    public void fetchDialogOptionTest(){
        String testReferenceId = "testDialog";
        Dialog testDialog = new Dialog(testReferenceId,"flavorText", "optionText");
        MainActivity mockMainActivity = Mockito.mock(MainActivity.class);
        DialogRepository mockDialogRepository = Mockito.mock(DialogRepository.class);
        when(mockDialogRepository.readDialog(testReferenceId)).thenReturn(testDialog);
        DialogService testedDialogService = new DialogService(mockMainActivity, mockDialogRepository);
        Dialog resultDialog = testedDialogService.fetchDialogOption(testReferenceId);
        assertEquals(testDialog, resultDialog);
    }

    @Test
    public void selectDialogOptionTest(){
        String testReferenceId = "testDialog";
        Dialog testDialog = new Dialog(testReferenceId,"flavorText", "optionText");
        MainActivity mockMainActivity = Mockito.mock(MainActivity.class);
        DialogRepository mockDialogRepository = Mockito.mock(DialogRepository.class);
        when(mockDialogRepository.readDialog(testReferenceId)).thenReturn(testDialog);
        DialogService testedDialogService = new DialogService(mockMainActivity, mockDialogRepository);
        testedDialogService.selectDialogOption(testReferenceId,new HashMap<String, Object>());
        ArgumentCaptor<DialogSelectedEvent> dialogSelectedEventArgumentCaptor = ArgumentCaptor.forClass(DialogSelectedEvent.class);
        verify(mockMainActivity).publishEvent(dialogSelectedEventArgumentCaptor.capture());
        assertEquals(testDialog, dialogSelectedEventArgumentCaptor.getValue().getNewDialog());
    }

    @Test
    public void initializeDialogsTest(){
        MainActivity mockMainActivity = Mockito.mock(MainActivity.class);
        DialogRepository mockDialogRepository = Mockito.mock(DialogRepository.class);
        DialogService testedDialogService = new DialogService(mockMainActivity, mockDialogRepository);
        testedDialogService.initializeDialogs();
        verify(mockDialogRepository,times(7)).writeDialog(any());
    }
}
