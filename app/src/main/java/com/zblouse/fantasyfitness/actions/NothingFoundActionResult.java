package com.zblouse.fantasyfitness.actions;

public class NothingFoundActionResult extends ActionResult {

    private final String flavorText;

    public NothingFoundActionResult(String flavorText){
        super(ActionResultType.NOTHING);
        this.flavorText = flavorText;
    }

    public String getFlavorText(){
        return this.flavorText;
    }

}
