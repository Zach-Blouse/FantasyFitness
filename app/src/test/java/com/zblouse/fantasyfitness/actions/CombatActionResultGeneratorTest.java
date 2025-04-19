package com.zblouse.fantasyfitness.actions;

import static org.junit.Assert.assertEquals;

import com.zblouse.fantasyfitness.R;
import com.zblouse.fantasyfitness.world.GameLocationService;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CombatActionResultGeneratorTest {

    @Test
    public void woodlandsCombatActionResultTest(){
        CombatActionResultGenerator testedGenerator = new CombatActionResultGenerator();

        Map<String, Object> metadata = new HashMap<>();
        metadata.put(ExploreActionService.EXPLORE_ACTION_LOCATION_KEY, GameLocationService.WOODLANDS);
        metadata.put(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED, R.id.cave_button);
        ActionResult result = testedGenerator.generate(new ArrayList<>(), metadata);
        assertEquals(ActionResultType.COMBAT, result.getActionResultType());
        CombatActionResult combatActionResult = (CombatActionResult) result;
        assertEquals("Goblin Attack", combatActionResult.getEncounterName());
        assertEquals(GameLocationService.WOODLANDS, combatActionResult.getCombatLocation());
        assertEquals(R.id.cave_button,combatActionResult.getCombatBuilding());
    }

    @Test
    public void riverlandsCombatActionResultTest(){
        CombatActionResultGenerator testedGenerator = new CombatActionResultGenerator();

        Map<String, Object> metadata = new HashMap<>();
        metadata.put(ExploreActionService.EXPLORE_ACTION_LOCATION_KEY, GameLocationService.RIVERLANDS);
        metadata.put(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED, R.id.cave_button);
        ActionResult result = testedGenerator.generate(new ArrayList<>(),metadata);
        assertEquals(ActionResultType.COMBAT, result.getActionResultType());
        CombatActionResult combatActionResult = (CombatActionResult) result;
        assertEquals("Bandit", combatActionResult.getEncounterName());
        assertEquals(GameLocationService.RIVERLANDS, combatActionResult.getCombatLocation());
        assertEquals(R.id.cave_button,combatActionResult.getCombatBuilding());
    }
}
