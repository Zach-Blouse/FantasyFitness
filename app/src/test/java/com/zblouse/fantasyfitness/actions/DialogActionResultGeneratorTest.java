package com.zblouse.fantasyfitness.actions;

import static org.junit.Assert.assertEquals;

import com.zblouse.fantasyfitness.R;
import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.dialog.DialogService;
import com.zblouse.fantasyfitness.world.GameLocationService;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DialogActionResultGeneratorTest {

    @Test
    public void innDialogActionResultTest(){
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        DialogActionResultGenerator testedDialogActionResultGenerator = new DialogActionResultGenerator(mockActivity);
        Map<String, Object> metadata = new HashMap<>();
        metadata.put(ExploreActionService.EXPLORE_ACTION_LOCATION_KEY, GameLocationService.THANADEL_VILLAGE);
        metadata.put(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED, R.id.inn_button);
        DialogActionResult dialogActionResult = (DialogActionResult)testedDialogActionResultGenerator.generate(new ArrayList<>(),metadata);
        assertEquals(DialogService.INNKEEPER_DIALOG_INIT,dialogActionResult.getInitialDialogReferenceId());
    }

    @Test
    public void thanadelGeneralStoreDialogActionResultTest(){
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        DialogActionResultGenerator testedDialogActionResultGenerator = new DialogActionResultGenerator(mockActivity);
        Map<String, Object> metadata = new HashMap<>();
        metadata.put(ExploreActionService.EXPLORE_ACTION_LOCATION_KEY, GameLocationService.THANADEL_VILLAGE);
        metadata.put(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED, R.id.general_store_button);
        DialogActionResult dialogActionResult = (DialogActionResult)testedDialogActionResultGenerator.generate(new ArrayList<>(),metadata);
        assertEquals(DialogService.THANADEL_GENERAL_STORE_DIALOG_INIT,dialogActionResult.getInitialDialogReferenceId());
    }

    @Test
    public void faolynGeneralStoreDialogActionResultTest(){
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        DialogActionResultGenerator testedDialogActionResultGenerator = new DialogActionResultGenerator(mockActivity);
        Map<String, Object> metadata = new HashMap<>();
        metadata.put(ExploreActionService.EXPLORE_ACTION_LOCATION_KEY, GameLocationService.FAOLYN);
        metadata.put(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED, R.id.general_store_button);
        DialogActionResult dialogActionResult = (DialogActionResult)testedDialogActionResultGenerator.generate(new ArrayList<>(),metadata);
        assertEquals(DialogService.FAOLYN_GENERAL_STORE_DIALOG_INIT,dialogActionResult.getInitialDialogReferenceId());
    }

    @Test
    public void bridgetonGeneralStoreDialogActionResultTest(){
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        DialogActionResultGenerator testedDialogActionResultGenerator = new DialogActionResultGenerator(mockActivity);
        Map<String, Object> metadata = new HashMap<>();
        metadata.put(ExploreActionService.EXPLORE_ACTION_LOCATION_KEY, GameLocationService.BRIDGETON);
        metadata.put(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED, R.id.general_store_button);
        DialogActionResult dialogActionResult = (DialogActionResult)testedDialogActionResultGenerator.generate(new ArrayList<>(),metadata);
        assertEquals(DialogService.BRIDGETON_GENERAL_STORE_DIALOG_INIT,dialogActionResult.getInitialDialogReferenceId());
    }

    @Test
    public void faolynBlacksmithDialogActionResultTest(){
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        DialogActionResultGenerator testedDialogActionResultGenerator = new DialogActionResultGenerator(mockActivity);
        Map<String, Object> metadata = new HashMap<>();
        metadata.put(ExploreActionService.EXPLORE_ACTION_LOCATION_KEY, GameLocationService.FAOLYN);
        metadata.put(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED, R.id.blacksmith_button);
        DialogActionResult dialogActionResult = (DialogActionResult)testedDialogActionResultGenerator.generate(new ArrayList<>(),metadata);
        assertEquals(DialogService.FAOLYN_BLACKSMITH_DIALOG_INIT,dialogActionResult.getInitialDialogReferenceId());
    }

    @Test
    public void bridgetonBlacksmithDialogActionResultTest(){
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        DialogActionResultGenerator testedDialogActionResultGenerator = new DialogActionResultGenerator(mockActivity);
        Map<String, Object> metadata = new HashMap<>();
        metadata.put(ExploreActionService.EXPLORE_ACTION_LOCATION_KEY, GameLocationService.BRIDGETON);
        metadata.put(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED, R.id.blacksmith_button);
        DialogActionResult dialogActionResult = (DialogActionResult)testedDialogActionResultGenerator.generate(new ArrayList<>(),metadata);
        assertEquals(DialogService.BRIDGETON_BLACKSMITH_DIALOG_INIT,dialogActionResult.getInitialDialogReferenceId());
    }
}
