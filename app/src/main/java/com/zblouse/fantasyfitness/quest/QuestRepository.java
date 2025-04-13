package com.zblouse.fantasyfitness.quest;

import android.util.Log;

import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.core.Repository;

import java.util.List;
import java.util.Map;

public class QuestRepository implements Repository<Quest> {

    private QuestService questService;
    private QuestFirestoreDatabase questFirestoreDatabase;
    private QuestTemporaryDataCache questTemporaryDataCache;

    public QuestRepository(QuestService questService){
        this.questService = questService;
        this.questFirestoreDatabase = new QuestFirestoreDatabase();
        this.questTemporaryDataCache = new QuestTemporaryDataCache();
    }

    public QuestRepository(QuestService questService, QuestFirestoreDatabase questFirestoreDatabase){
        this.questService = questService;
        this.questFirestoreDatabase = questFirestoreDatabase;
        this.questTemporaryDataCache = new QuestTemporaryDataCache();
    }

    public boolean saveTempQuest(MainActivity mainActivity, Quest quest){
        try {
            questTemporaryDataCache.saveQuest(quest, mainActivity);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Quest loadTempQuest(MainActivity mainActivity, String questUuid){
        try {
            return questTemporaryDataCache.loadQuest(questUuid, mainActivity);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean deleteTempQuest(MainActivity mainActivity, String questUuid){
        return questTemporaryDataCache.deleteQuest(questUuid, mainActivity);
    }

    public void getQuest(String userId, String questUuid, Map<String, Object> metadata){
        questFirestoreDatabase.readQuest(userId, questUuid, this, metadata);
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
    public void readCallback(Quest quest, Map<String, Object> metadata) {
        questService.repositoryResponse(quest, metadata);
    }

    @Override
    public void writeCallback(Quest object, Map<String, Object> metadata) {

    }

    @Override
    public void updateCallback(boolean success, Map<String, Object> metadata) {

    }
}
