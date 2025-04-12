package com.zblouse.fantasyfitness.quest;

import com.zblouse.fantasyfitness.core.Repository;

import java.util.List;
import java.util.Map;

public class QuestRepository implements Repository<Quest> {

    private QuestService questService;
    private QuestFirestoreDatabase questFirestoreDatabase;

    public QuestRepository(QuestService questService){
        this.questService = questService;
    }

    public void writeQuest(Quest quest, String userId, Map<String, Object> metadata){
        questFirestoreDatabase.writeQuest(quest, userId, this, metadata);
    }

    public void deleteQuest(Quest quest, String userId, Map<String, Object> metadata){
        questFirestoreDatabase.deleteQuest(quest, userId, this, metadata);
    }

    public void fetchQuests(String userId, Map<String, Object> metadata){
        questFirestoreDatabase.fetchQuests(userId, this, metadata);
    }

    public void listReadCallback(List<Quest> quests, Map<String, Object> metadata){
        questService.questFetchResponse(quests, metadata);
    }

    public void deleteCallback(Quest quest, Map<String, Object> metadata){

    }

    @Override
    public void readCallback(Quest object, Map<String, Object> metadata) {

    }

    @Override
    public void writeCallback(Quest object, Map<String, Object> metadata) {

    }

    @Override
    public void updateCallback(boolean success, Map<String, Object> metadata) {

    }
}
