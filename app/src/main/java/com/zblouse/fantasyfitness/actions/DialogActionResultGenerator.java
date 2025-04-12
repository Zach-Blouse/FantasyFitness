package com.zblouse.fantasyfitness.actions;

import com.zblouse.fantasyfitness.R;
import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.dialog.DialogService;
import com.zblouse.fantasyfitness.world.GameLocationService;

import java.util.Map;

public class DialogActionResultGenerator implements ActionResultGenerator {

    private MainActivity mainActivity;

    public DialogActionResultGenerator(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    @Override
    public ActionResult generate(Map<String, Object> metadata) {
        if(metadata.containsKey(ExploreActionService.EXPLORE_ACTION_LOCATION_KEY)) {
            String exploreActionLocation = (String) metadata.get(ExploreActionService.EXPLORE_ACTION_LOCATION_KEY);
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
