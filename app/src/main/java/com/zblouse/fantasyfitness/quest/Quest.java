package com.zblouse.fantasyfitness.quest;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.List;

public class Quest implements Serializable {

    private String questName;
    private String questUuid;
    private int questReward;
    private List<QuestObjective> questObjectives;

    public Quest(){

    }

    public Quest(String questName, String questUuid, int questReward, List<QuestObjective> questObjectives){
        this.questName = questName;
        this.questUuid = questUuid;
        this.questObjectives = questObjectives;
        this.questReward = questReward;
    }

    public String getQuestName(){
        return this.questName;
    }

    @Exclude
    public String getQuestDescription(){
        String description = "";
        for(QuestObjective objective: questObjectives){
            if(description.equals("")){
                if(objective.getQuestObjectiveType().equals(QuestObjectiveType.VISIT)){
                    description += "Travel to " + objective.getGameLocation();
                } else if(objective.getQuestObjectiveType().equals(QuestObjectiveType.FIGHT)){
                    description += "Fight monsters in the " + objective.getGameLocation();
                }
            } else {
                description += " and then ";
                if(objective.getQuestObjectiveType().equals(QuestObjectiveType.VISIT)){
                    description += "travel to " + objective.getGameLocation();
                } else if(objective.getQuestObjectiveType().equals(QuestObjectiveType.FIGHT)){
                    description += "fight monsters in the " + objective.getGameLocation();
                }
            }
        }

        return description;
    }

    public String getQuestUuid(){
        return this.questUuid;
    }

    public List<QuestObjective> getQuestObjectives(){
        return this.questObjectives;
    }

    public int getQuestReward(){
        return this.questReward;
    }
}
