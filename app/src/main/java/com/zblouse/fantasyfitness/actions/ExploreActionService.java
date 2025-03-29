package com.zblouse.fantasyfitness.actions;

import com.zblouse.fantasyfitness.activity.MainActivity;

import java.util.Map;

public class ExploreActionService {

    public static final String EXPLORE_ACTION_LOCATION_KEY = "exploreActionLocation";
    public static final String EXPLORE_ACTION_BUTTON_PRESSED = "exploreActionButtonPressed";

    private MainActivity mainActivity;
    private NothingFoundActionResultGenerator nothingFoundActionResultGenerator;

    public ExploreActionService(MainActivity mainActivity){
        this.mainActivity = mainActivity;
        this.nothingFoundActionResultGenerator = new NothingFoundActionResultGenerator();
    }

    public void setMainActivity(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    public void setNothingFoundActionResultGenerator(NothingFoundActionResultGenerator nothingFoundActionResultGenerator){
        this.nothingFoundActionResultGenerator = nothingFoundActionResultGenerator;
    }

    public void exploreAction(Map<String, Object> metadata){
        //TODO add other action result types as they are implemented on a random basis
        ActionResult actionResult = nothingFoundActionResultGenerator.generate(metadata);
        mainActivity.publishEvent(new ExploreActionEvent(actionResult, metadata));
    }
}
