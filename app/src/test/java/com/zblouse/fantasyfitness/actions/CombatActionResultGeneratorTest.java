package com.zblouse.fantasyfitness.actions;

import static org.junit.Assert.assertEquals;

import com.zblouse.fantasyfitness.world.GameLocationService;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class CombatActionResultGeneratorTest {

    @Test
    public void woodlandsCombatActionResultTest(){
        CombatActionResultGenerator testedGenerator = new CombatActionResultGenerator();

        Map<String, Object> metadata = new HashMap<>();
        metadata.put(ExploreActionService.EXPLORE_ACTION_LOCATION_KEY, GameLocationService.WOODLANDS);
        ActionResult result = testedGenerator.generate(metadata);
        assertEquals(ActionResultType.COMBAT, result.getActionResultType());
        CombatActionResult combatActionResult = (CombatActionResult) result;
        assertEquals("Goblin Attack", combatActionResult.getEncounterName());
    }

    @Test
    public void riverlandsCombatActionResultTest(){
        CombatActionResultGenerator testedGenerator = new CombatActionResultGenerator();

        Map<String, Object> metadata = new HashMap<>();
        metadata.put(ExploreActionService.EXPLORE_ACTION_LOCATION_KEY, GameLocationService.RIVERLANDS);
        ActionResult result = testedGenerator.generate(metadata);
        assertEquals(ActionResultType.COMBAT, result.getActionResultType());
        CombatActionResult combatActionResult = (CombatActionResult) result;
        assertEquals("Bandit", combatActionResult.getEncounterName());
    }
}
