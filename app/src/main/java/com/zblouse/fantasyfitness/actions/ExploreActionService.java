package com.zblouse.fantasyfitness.actions;

import com.zblouse.fantasyfitness.activity.MainActivity;

import java.util.Map;

public class ExploreActionService {

    public static final String EXPLORE_ACTION_LOCATION_KEY = "exploreActionLocation";
    public static final String EXPLORE_ACTION_BUTTON_PRESSED = "exploreActionButtonPressed";

    private MainActivity mainActivity;
    private NothingFoundActionResultGenerator nothingFoundActionResultGenerator;
    private DialogActionResultGenerator dialogActionResultGenerator;
    private RandomActionResultTypeGenerator randomActionResultTypeGenerator;

    public ExploreActionService(MainActivity mainActivity){
        this.mainActivity = mainActivity;
        this.nothingFoundActionResultGenerator = new NothingFoundActionResultGenerator();
        this.dialogActionResultGenerator = new DialogActionResultGenerator(mainActivity);
        this.randomActionResultTypeGenerator = new RandomActionResultTypeGenerator();
    }

    public void setMainActivity(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    public void setRandomActionResultTypeGenerator(RandomActionResultTypeGenerator randomActionResultTypeGenerator){
        this.randomActionResultTypeGenerator = randomActionResultTypeGenerator;
    }

    public void exploreAction(Map<String, Object> metadata){
        //TODO add other action result types as they are implemented on a random basis
        ActionResult actionResult;
        switch(randomActionResultTypeGenerator.getRandomActionResult()){
            case NOTHING:
                actionResult = nothingFoundActionResultGenerator.generate(metadata);
                break;
            case DIALOG:
                actionResult = dialogActionResultGenerator.generate(metadata);
                break;
            default:
                actionResult = nothingFoundActionResultGenerator.generate(metadata);
                break;
        }
        mainActivity.publishEvent(new ExploreActionEvent(actionResult, metadata));
    }
}
