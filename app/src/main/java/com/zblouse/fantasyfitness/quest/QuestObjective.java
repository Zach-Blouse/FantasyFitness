package com.zblouse.fantasyfitness.quest;

import com.google.firebase.firestore.Exclude;
import com.zblouse.fantasyfitness.world.GameLocationBuildingUtil;

import java.io.Serializable;

public class QuestObjective implements Serializable {

    //TODO Future improvement, specify which combat the quest is for, or be able to generate specific combats for quests
    private QuestObjectiveType questObjectiveType;
    private String questObjectiveUuid;
    private String gameLocation;
    private int buildingId;
    private boolean objectiveMet;
    private String questDialog;

    public QuestObjective(){

    }

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

    @Exclude
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

    public void setObjectiveMet(boolean objectiveMet){
        this.objectiveMet = objectiveMet;
    }

    public boolean isObjectiveMet(){
        return this.objectiveMet;
    }
}
