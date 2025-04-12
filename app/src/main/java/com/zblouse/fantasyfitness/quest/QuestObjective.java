package com.zblouse.fantasyfitness.quest;

import com.zblouse.fantasyfitness.world.GameLocationBuildingUtil;

public class QuestObjective {

    //TODO Future improvement, specify which combat the quest is for, or be able to generate specific combats for quests
    private final QuestObjectiveType questObjectiveType;
    private final String questObjectiveUuid;
    private final String gameLocation;
    private final int buildingId;
    private boolean objectiveMet;
    private String questDialog;

    public QuestObjective(QuestObjectiveType questObjectiveType, String questObjectiveUuid, String gameLocation, int buildingId, boolean objectiveMet){
        this.questObjectiveType = questObjectiveType;
        this.questObjectiveUuid = questObjectiveUuid;
        this.gameLocation = gameLocation;
        this.buildingId = buildingId;
        this.objectiveMet = objectiveMet;
    }

    public String getQuestObjectiveUuid(){
        return this.questObjectiveUuid;
    }

    public String getQuestObjectiveDescription(){
        String questObjectiveString = "";
        if(questObjectiveType.equals(QuestObjectiveType.FIGHT)){
            questObjectiveString = "Go to " + gameLocation + " and visit the " + GameLocationBuildingUtil.getPrintableStringForBuilding(buildingId) + " and fight the monsters there.";
        } else if(questObjectiveType.equals(QuestObjectiveType.VISIT)){
            questObjectiveString = "Go to " + gameLocation + " and visit the " + GameLocationBuildingUtil.getPrintableStringForBuilding(buildingId);
        }
        return questObjectiveString;
    }

    public String getQuestDialogId(){
        return this.questDialog;
    }

    public void setQuestDialogId(String questDialog){
        this.questDialog = questDialog;
    }

    public QuestObjectiveType getQuestObjectiveType(){
        return this.questObjectiveType;
    }

    public String getGameLocation(){
        return this.gameLocation;
    }

    public int getBuildingId(){
        return this.buildingId;
    }

    public void objectiveCompleted(){
        objectiveMet = true;
    }

    public boolean isObjectiveMet(){
        return this.objectiveMet;
    }
}
