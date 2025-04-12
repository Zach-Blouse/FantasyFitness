package com.zblouse.fantasyfitness.actions;

import android.util.Log;

import com.zblouse.fantasyfitness.R;
import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.dialog.DialogService;
import com.zblouse.fantasyfitness.quest.Quest;
import com.zblouse.fantasyfitness.quest.QuestObjective;
import com.zblouse.fantasyfitness.quest.QuestObjectiveType;
import com.zblouse.fantasyfitness.world.GameLocationService;

import java.util.List;
import java.util.Map;

public class DialogActionResultGenerator implements ActionResultGenerator {

    private MainActivity mainActivity;

    public DialogActionResultGenerator(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    @Override
    public ActionResult generate(List<Quest> quests, Map<String, Object> metadata) {
        Log.e("DialogActionResultGenerator", "Location: " + (String) metadata.get(ExploreActionService.EXPLORE_ACTION_LOCATION_KEY));
        if(metadata.containsKey(ExploreActionService.EXPLORE_ACTION_LOCATION_KEY)) {
            Log.e("DialogActionResultGenerator", "Quests: " + quests.size());
            String exploreActionLocation = (String) metadata.get(ExploreActionService.EXPLORE_ACTION_LOCATION_KEY);
            for(Quest quest: quests){
                for(QuestObjective questObjective: quest.getQuestObjectives()){
                    Log.e("DialogActionResultGenerator","in Location: " + exploreActionLocation + " at building " + metadata.get(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED));
                    Log.e("DialogActionResultGenerator", "Looking at objective: " + questObjective.getQuestObjectiveUuid() + " location needed " + questObjective.getGameLocation() + " and building " + questObjective.getBuildingId() + " and objective is met=" + questObjective.isObjectiveMet());
                    if(!questObjective.isObjectiveMet() && questObjective.getQuestObjectiveType().equals(QuestObjectiveType.VISIT) && questObjective.getGameLocation().equals(exploreActionLocation) && (questObjective.getBuildingId() == (Integer)metadata.get(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED))){
                        Log.e("DialogActionResultGenerator", "Using QUest Objective");
                        return new DialogActionResult(mainActivity.getDialogService().fetchDialogOption(questObjective.getQuestDialogId()));
                    }
                }
            }
            if (GameLocationService.isWildernessLocation(exploreActionLocation)) {
                return new DialogActionResult(mainActivity.getDialogService().fetchDialogOption(DialogService.HERMIT_DIALOG_INIT));
            }
            if(metadata.containsKey(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED)){
                if(metadata.get(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED).equals(R.id.inn_button)){
                    return new DialogActionResult(mainActivity.getDialogService().fetchDialogOption(DialogService.INNKEEPER_DIALOG_INIT));
                }
            }
        }
        return new DialogActionResult(mainActivity.getDialogService().fetchDialogOption(DialogService.EMPTY_DIALOG_INIT));
    }
}
