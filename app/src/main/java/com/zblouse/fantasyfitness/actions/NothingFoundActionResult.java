package com.zblouse.fantasyfitness.actions;

public class NothingFoundActionResult extends ActionResult {

    private String flavorText;

    public NothingFoundActionResult(){
        super(ActionResultType.NOTHING);
    }

    public NothingFoundActionResult(String flavorText){
        super(ActionResultType.NOTHING);
        this.flavorText = flavorText;
    }

    public String getFlavorText(){
        return this.flavorText;
    }

    public void setFlavorText(String flavorText){
        this.flavorText = flavorText;
    }
}
