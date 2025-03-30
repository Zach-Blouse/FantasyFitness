package com.zblouse.fantasyfitness.actions;

import com.zblouse.fantasyfitness.core.Event;
import com.zblouse.fantasyfitness.core.EventType;

import java.util.Map;

public class ExploreActionEvent extends Event {

    private ActionResult actionResult;

    public ExploreActionEvent(ActionResult actionResult, Map<String, Object> metadata){
        super(EventType.EXPLORE_ACTION_EVENT, metadata);
        this.actionResult = actionResult;
    }

    public ActionResult getExploreActionResult(){
        return this.actionResult;
    }
}
