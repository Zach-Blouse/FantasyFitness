package com.zblouse.fantasyfitness.actions;

import static org.junit.Assert.assertEquals;

import com.zblouse.fantasyfitness.world.GameLocationService;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class NothingFoundActionResultGeneratorTest {

    @Test
    public void wildernessTest(){
        NothingFoundActionResultGenerator testedGenerator = new NothingFoundActionResultGenerator();

        Map<String, Object> metadata = new HashMap<>();
        metadata.put(ExploreActionService.EXPLORE_ACTION_LOCATION_KEY, GameLocationService.WOODLANDS);
        ActionResult result = testedGenerator.generate(metadata);
        assertEquals(ActionResultType.NOTHING, result.getActionResultType());
        assertEquals("Everything seems quiet.",((NothingFoundActionResult)result).getFlavorText());
    }

    @Test
    public void faolynTest(){
        NothingFoundActionResultGenerator testedGenerator = new NothingFoundActionResultGenerator();

        Map<String, Object> metadata = new HashMap<>();
        metadata.put(ExploreActionService.EXPLORE_ACTION_LOCATION_KEY, GameLocationService.FAOLYN);
        ActionResult result = testedGenerator.generate(metadata);
        assertEquals(ActionResultType.NOTHING, result.getActionResultType());
        assertEquals("The city is bustling with activity, but nothing in particular catches your eye.",((NothingFoundActionResult)result).getFlavorText());
    }

    @Test
    public void thanadelTest(){
        NothingFoundActionResultGenerator testedGenerator = new NothingFoundActionResultGenerator();

        Map<String, Object> metadata = new HashMap<>();
        metadata.put(ExploreActionService.EXPLORE_ACTION_LOCATION_KEY, GameLocationService.THANADEL_VILLAGE);
        ActionResult result = testedGenerator.generate(metadata);
        assertEquals(ActionResultType.NOTHING, result.getActionResultType());
        assertEquals("You find nothing of note. The village is peaceful.",((NothingFoundActionResult)result).getFlavorText());
    }

    @Test
    public void defaultTest(){
        NothingFoundActionResultGenerator testedGenerator = new NothingFoundActionResultGenerator();

        Map<String, Object> metadata = new HashMap<>();
        metadata.put(ExploreActionService.EXPLORE_ACTION_LOCATION_KEY, GameLocationService.BRIDGETON);
        ActionResult result = testedGenerator.generate(metadata);
        assertEquals(ActionResultType.NOTHING, result.getActionResultType());
        assertEquals("Nothing of note catches your attention.",((NothingFoundActionResult)result).getFlavorText());
    }
}
