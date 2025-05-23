package com.zblouse.fantasyfitness.actions;

import static com.zblouse.fantasyfitness.actions.ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED;
import static com.zblouse.fantasyfitness.actions.ExploreActionService.EXPLORE_ACTION_LOCATION_KEY;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.zblouse.fantasyfitness.R;
import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.dialog.Dialog;
import com.zblouse.fantasyfitness.dialog.DialogAffect;
import com.zblouse.fantasyfitness.dialog.DialogAffectType;
import com.zblouse.fantasyfitness.dialog.DialogService;
import com.zblouse.fantasyfitness.quest.QuestService;
import com.zblouse.fantasyfitness.world.GameLocationService;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ExploreActionServiceTest {

    @Test
    public void exploreActionNothingTest(){
        MainActivity mockMainActivity = Mockito.mock(MainActivity.class);
        RandomActionResultTypeGenerator mockRandomActionResultTypeGenerator = Mockito.mock(RandomActionResultTypeGenerator.class);
        when(mockRandomActionResultTypeGenerator.getRandomActionResult(GameLocationService.WOODLANDS)).thenReturn(ActionResultType.NOTHING);
        ExploreActionService testedService = new ExploreActionService(mockMainActivity);
        testedService.setRandomActionResultTypeGenerator(mockRandomActionResultTypeGenerator);

        Map<String, Object> metadata = new HashMap<>();
        metadata.put(EXPLORE_ACTION_LOCATION_KEY, GameLocationService.WOODLANDS);
        metadata.put(EXPLORE_ACTION_BUTTON_PRESSED, R.id.dark_forest_button);

        testedService.exploreAction(new ArrayList(), metadata);

        ArgumentCaptor<ExploreActionEvent> exploreActionEventArgumentCaptor = ArgumentCaptor.forClass(ExploreActionEvent.class);
        verify(mockMainActivity).publishEvent(exploreActionEventArgumentCaptor.capture());
        assertEquals(NothingFoundActionResult.class, exploreActionEventArgumentCaptor.getValue().getExploreActionResult().getClass());
    }

    @Test
    public void exploreActionDialogTest(){
        MainActivity mockMainActivity = Mockito.mock(MainActivity.class);
        DialogService mockDialogService = Mockito.mock(DialogService.class);
        QuestService mockQuestService = Mockito.mock(QuestService.class);
        when(mockMainActivity.getDialogService()).thenReturn(mockDialogService);
        when(mockMainActivity.getQuestService()).thenReturn(mockQuestService);
        Dialog testDialog = new Dialog(DialogService.HERMIT_DIALOG_INIT,"hermit","hermit",new DialogAffect(DialogAffectType.NONE), false);

        RandomActionResultTypeGenerator mockRandomActionResultTypeGenerator = Mockito.mock(RandomActionResultTypeGenerator.class);
        when(mockRandomActionResultTypeGenerator.getRandomActionResult(GameLocationService.WOODLANDS)).thenReturn(ActionResultType.DIALOG);
        ExploreActionService testedService = new ExploreActionService(mockMainActivity);
        testedService.setRandomActionResultTypeGenerator(mockRandomActionResultTypeGenerator);

        Map<String, Object> metadata = new HashMap<>();
        metadata.put(EXPLORE_ACTION_LOCATION_KEY, GameLocationService.WOODLANDS);
        metadata.put(EXPLORE_ACTION_BUTTON_PRESSED, R.id.dark_forest_button);

        testedService.exploreAction(new ArrayList(), metadata);

        ArgumentCaptor<ExploreActionEvent> exploreActionEventArgumentCaptor = ArgumentCaptor.forClass(ExploreActionEvent.class);
        verify(mockMainActivity).publishEvent(exploreActionEventArgumentCaptor.capture());
        assertEquals(DialogActionResult.class, exploreActionEventArgumentCaptor.getValue().getExploreActionResult().getClass());
        assertEquals(testDialog.getReferenceId(),((DialogActionResult)exploreActionEventArgumentCaptor.getValue().getExploreActionResult()).getInitialDialogReferenceId());
    }
}
