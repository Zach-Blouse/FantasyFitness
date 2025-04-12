package com.zblouse.fantasyfitness.actions;

import android.util.Log;

import com.zblouse.fantasyfitness.R;
import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.quest.Quest;
import com.zblouse.fantasyfitness.world.GameLocationBuildingUtil;

import java.util.List;
import java.util.Map;

public class ExploreActionService {

    public static final String EXPLORE_ACTION_LOCATION_KEY = "exploreActionLocation";
    public static final String EXPLORE_ACTION_BUTTON_PRESSED = "exploreActionButtonPressed";
    public static final String EXPLORE_ACTION_FETCH_QUESTS = "exploreActionFetchQuests";

    private MainActivity mainActivity;
    private NothingFoundActionResultGenerator nothingFoundActionResultGenerator;
    private DialogActionResultGenerator dialogActionResultGenerator;
    private RandomActionResultTypeGenerator randomActionResultTypeGenerator;
    private CombatActionResultGenerator combatActionResultGenerator;

    public ExploreActionService(MainActivity mainActivity){
        this.mainActivity = mainActivity;
        this.nothingFoundActionResultGenerator = new NothingFoundActionResultGenerator();
        this.dialogActionResultGenerator = new DialogActionResultGenerator(mainActivity);
        this.combatActionResultGenerator = new CombatActionResultGenerator();
        this.randomActionResultTypeGenerator = new RandomActionResultTypeGenerator();
    }

    public void setMainActivity(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    public void setRandomActionResultTypeGenerator(RandomActionResultTypeGenerator randomActionResultTypeGenerator){
        this.randomActionResultTypeGenerator = randomActionResultTypeGenerator;
    }

    public void exploreAction(List<Quest> quests, Map<String, Object> metadata){
        //TODO add other action result types as they are implemented on a random basis
        Log.e("ExploreActionService", "called exploreAction");
        ActionResult actionResult;
        ActionResultType actionResultType;
        if(((Integer)metadata.get(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED)).equals(R.id.inn_button)){
            Log.e("ExploreActionService", "Inn Button");
            actionResultType = ActionResultType.DIALOG;
        } else if(((Integer)metadata.get(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED)).equals(R.id.general_store_button)){
            Log.e("ExploreActionService", "General Store Button");
            actionResultType = ActionResultType.DIALOG;
        } else {
            Log.e("ExploreActionService", GameLocationBuildingUtil.getPrintableStringForBuilding((Integer)metadata.get(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED)));
            actionResultType = randomActionResultTypeGenerator.getRandomActionResult((String)metadata.get(EXPLORE_ACTION_LOCATION_KEY));
        }
        switch(actionResultType){
            case NOTHING:
                actionResult = nothingFoundActionResultGenerator.generate(quests, metadata);
                break;
            case DIALOG:
                actionResult = dialogActionResultGenerator.generate(quests, metadata);
                break;
            case COMBAT:
                actionResult = combatActionResultGenerator.generate(quests,metadata);
                break;
            default:
                actionResult = nothingFoundActionResultGenerator.generate(quests, metadata);
                break;
        }
        mainActivity.publishEvent(new ExploreActionEvent(actionResult, metadata));
    }

    public void exploreAction(Map<String, Object> metadata) {
        metadata.put(EXPLORE_ACTION_FETCH_QUESTS, true);
        mainActivity.getQuestService().fetchQuests(metadata);
    }
}
