package com.zblouse.fantasyfitness.actions;

public abstract class ActionResult {

    protected ActionResultType actionResultType;

    public ActionResult(ActionResultType actionResultType){
        this.actionResultType = actionResultType;
    }

    public ActionResultType getActionResultType(){
        return this.actionResultType;
    }
}
