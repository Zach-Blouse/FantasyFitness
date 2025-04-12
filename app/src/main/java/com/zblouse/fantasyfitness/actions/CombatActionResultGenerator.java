package com.zblouse.fantasyfitness.actions;

import android.util.Log;

import com.zblouse.fantasyfitness.combat.encounter.EncounterDifficultyLevel;
import com.zblouse.fantasyfitness.quest.Quest;
import com.zblouse.fantasyfitness.world.GameLocationService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CombatActionResultGenerator implements ActionResultGenerator{

    private final Map<EncounterDifficultyLevel, List<String>> encounterDifficultyMap;

    public CombatActionResultGenerator(){
        encounterDifficultyMap = new HashMap<>();
        encounterDifficultyMap.put(EncounterDifficultyLevel.EASY, Arrays.asList("Goblin Attack"));
        encounterDifficultyMap.put(EncounterDifficultyLevel.MEDIUM,Arrays.asList("Bandit"));
        encounterDifficultyMap.put(EncounterDifficultyLevel.HARD, new ArrayList<>());
        encounterDifficultyMap.put(EncounterDifficultyLevel.DANGEROUS,new ArrayList<>());
        encounterDifficultyMap.put(EncounterDifficultyLevel.VALLEY,new ArrayList<>());
    }

    @Override
    public ActionResult generate(List<Quest> quests, Map<String, Object> metadata) {
        String locationName = (String)metadata.get(ExploreActionService.EXPLORE_ACTION_LOCATION_KEY);
        int buildingId = (Integer)metadata.get(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED);
        List<String> encounters = encounterDifficultyMap.get(GameLocationService.getLocationDifficulty(locationName));
        if(!encounters.isEmpty()){
            Collections.shuffle(encounters);
            return new CombatActionResult(encounters.get(0), locationName, buildingId);
        }
        //Should not fall into this case in ops, just a reminder for me in the future
        Log.e("CombatActionResultGenerator", "No encounters for difficulty: " + GameLocationService.getLocationDifficulty(locationName));
        return new CombatActionResult("Goblin Attack", locationName, buildingId);
    }
}
