package com.zblouse.fantasyfitness.actions;

public class CombatActionResult extends ActionResult {

    private String encounterName;
    private String combatLocation;
    private int combatBuilding;

    public CombatActionResult(String encounterName, String combatLocation, int combatBuilding){
        super(ActionResultType.COMBAT);
        this.encounterName = encounterName;
        this.combatLocation = combatLocation;
        this.combatBuilding = combatBuilding;
    }

    public String getEncounterName(){
        return this.encounterName;
    }

    public String getCombatLocation(){
        return this.combatLocation;
    }

    public int getCombatBuilding(){
        return this.combatBuilding;
    }
}
