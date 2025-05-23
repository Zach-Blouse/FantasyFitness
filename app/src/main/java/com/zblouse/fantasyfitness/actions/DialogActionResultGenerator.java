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
        if(metadata.containsKey(ExploreActionService.EXPLORE_ACTION_LOCATION_KEY)) {
            String exploreActionLocation = (String) metadata.get(ExploreActionService.EXPLORE_ACTION_LOCATION_KEY);
            for(Quest quest: quests){
                for(QuestObjective questObjective: quest.getQuestObjectives()){
                   if(!questObjective.isObjectiveMet() && questObjective.getQuestObjectiveType().equals(QuestObjectiveType.VISIT) && questObjective.getGameLocation().equals(exploreActionLocation) && (questObjective.getBuildingId() == (Integer)metadata.get(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED))){
                        return new DialogActionResult(questObjective.getQuestDialogId(), true);
                    } else if(!questObjective.isObjectiveMet()){
                       break;
                   }
                }
            }
            if (GameLocationService.isWildernessLocation(exploreActionLocation)) {
                return new DialogActionResult(DialogService.HERMIT_DIALOG_INIT, false);
            }
            if(metadata.containsKey(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED)){
                if(metadata.get(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED).equals(R.id.inn_button)){
                    return new DialogActionResult(DialogService.INNKEEPER_DIALOG_INIT, false);
                } else if(metadata.get(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED).equals(R.id.general_store_button)){
                    if(metadata.get(ExploreActionService.EXPLORE_ACTION_LOCATION_KEY).equals(GameLocationService.FAOLYN)){
                        return new DialogActionResult(DialogService.FAOLYN_GENERAL_STORE_DIALOG_INIT, false);
                    } else if(metadata.get(ExploreActionService.EXPLORE_ACTION_LOCATION_KEY).equals(GameLocationService.BRIDGETON)){
                        return new DialogActionResult(DialogService.BRIDGETON_GENERAL_STORE_DIALOG_INIT, false);
                    } else if(metadata.get(ExploreActionService.EXPLORE_ACTION_LOCATION_KEY).equals(GameLocationService.THANADEL_VILLAGE)){
                        return new DialogActionResult(DialogService.THANADEL_GENERAL_STORE_DIALOG_INIT, false);
                    } else if(metadata.get(ExploreActionService.EXPLORE_ACTION_LOCATION_KEY).equals(GameLocationService.ARDUWYN)){
                        return new DialogActionResult(DialogService.ARDUWYN_GENERAL_STORE_DIALOG_INIT, false);
                    }

                } else if(metadata.get(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED).equals(R.id.blacksmith_button)){
                    if(metadata.get(ExploreActionService.EXPLORE_ACTION_LOCATION_KEY).equals(GameLocationService.FAOLYN)){
                        return new DialogActionResult(DialogService.FAOLYN_BLACKSMITH_DIALOG_INIT, false);
                    } else if(metadata.get(ExploreActionService.EXPLORE_ACTION_LOCATION_KEY).equals(GameLocationService.BRIDGETON)){
                        return new DialogActionResult(DialogService.BRIDGETON_BLACKSMITH_DIALOG_INIT, false);
                    } else if(metadata.get(ExploreActionService.EXPLORE_ACTION_LOCATION_KEY).equals(GameLocationService.ARDUWYN)){
                        return new DialogActionResult(DialogService.ARDUWYN_BLACKSMITH_DIALOG_INIT, false);
                    }

                } else if(metadata.get(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED).equals(R.id.dwarven_tents_button)){
                    Log.e("DIALOG ACTION RESULTS GENREATOR", "tents");
                    return new DialogActionResult(DialogService.DWARVEN_TENTS_DIALOG_INIT, false);
                } else if(metadata.get(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED).equals(R.id.tower_button)){
                    return new DialogActionResult(DialogService.TOWER_DIALOG_INIT, false);
                } else if(metadata.get(ExploreActionService.EXPLORE_ACTION_BUTTON_PRESSED).equals(R.id.monastery_button)){
                    return new DialogActionResult(DialogService.MONASTERY_DIALOG_INIT, false);
                }
            }
        }
        return new DialogActionResult(DialogService.EMPTY_DIALOG_INIT, false);
    }
}
