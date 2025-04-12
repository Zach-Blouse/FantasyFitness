package com.zblouse.fantasyfitness.quest;

import java.util.List;

public class Quest {

    private final String questName;
    private final String questUuid;
    private final int questReward;
    private final List<QuestObjective> questObjectives;

    public Quest(String questName, String questUuid, int questReward, List<QuestObjective> questObjectives){
        this.questName = questName;
        this.questUuid = questUuid;
        this.questObjectives = questObjectives;
        this.questReward = questReward;
    }

    public String getQuestName(){
        return this.questName;
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
