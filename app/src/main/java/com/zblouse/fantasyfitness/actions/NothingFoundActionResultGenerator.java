package com.zblouse.fantasyfitness.actions;

import com.zblouse.fantasyfitness.quest.Quest;
import com.zblouse.fantasyfitness.world.GameLocationService;

import java.util.List;
import java.util.Map;

public class NothingFoundActionResultGenerator implements ActionResultGenerator {

    @Override
    public ActionResult generate(List<Quest> quests, Map<String, Object> metadata) {

        if(metadata.containsKey(ExploreActionService.EXPLORE_ACTION_LOCATION_KEY)){
            String exploreActionLocation = (String)metadata.get(ExploreActionService.EXPLORE_ACTION_LOCATION_KEY);
            if(GameLocationService.isWildernessLocation(exploreActionLocation)){
                return new NothingFoundActionResult("Everything seems quiet.");
            } else {
                switch (exploreActionLocation) {
                    case GameLocationService.THANADEL_VILLAGE:
                        return new NothingFoundActionResult("You find nothing of note. The village is peaceful.");
                    case GameLocationService.FAOLYN:
                        return new NothingFoundActionResult("The city is bustling with activity, but nothing in particular catches your eye.");
                    default:
                        return new NothingFoundActionResult("Nothing of note catches your attention.");
                }
            }
        }
        return new NothingFoundActionResult("Nothing found.");

    }
}
