package com.zblouse.fantasyfitness.actions;

public class CombatActionResult extends ActionResult {

    private String encounterName;

    public CombatActionResult(String encounterName){
        super(ActionResultType.COMBAT);
        this.encounterName = encounterName;
    }

    public String getEncounterName(){
        return this.encounterName;
    }
}
