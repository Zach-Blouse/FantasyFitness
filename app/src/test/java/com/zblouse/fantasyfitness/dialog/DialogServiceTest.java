package com.zblouse.fantasyfitness.dialog;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.world.GameLocationService;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

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
        Dialog testDialog = new Dialog(testReferenceId,"flavorText", "optionText", new DialogAffect(DialogAffectType.NONE), false);
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
    public void initializeDialogsTest(){
        MainActivity mockMainActivity = Mockito.mock(MainActivity.class);
        DialogRepository mockDialogRepository = Mockito.mock(DialogRepository.class);
        DialogService testedDialogService = new DialogService(mockMainActivity, mockDialogRepository);
        testedDialogService.initializeDialogs();
        verify(mockDialogRepository,times(25)).writeDialog(any());
    }
}
