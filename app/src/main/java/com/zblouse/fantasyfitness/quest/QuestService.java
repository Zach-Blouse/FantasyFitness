package com.zblouse.fantasyfitness.quest;

import android.util.Log;

import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.core.DomainService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestService implements DomainService<Quest> {

    private static final String QUEST_OBJECTIVE_CHECK_FETCH = "questObjectiveCheckFetch";
    private static final String QUEST_UUID_METADATA_KEY = "questUuidMetadataKey";
    private static final String OBJECTIVE_UUID_METADATA_KEY = "questObjectiveUuidMetadataKey";
    private static final String COMBAT_OBJECTIVE_CHECK_LOCATION_KEY = "combatObjectiveLocationKey";
    private static final String COMBAT_OBJECTIVE_BUILDING_KEY = "combatObjectiveBuildingKey";

    private MainActivity mainActivity;
    private QuestRepository questRepository;

    public QuestService(MainActivity mainActivity){
        this.mainActivity = mainActivity;
        this.questRepository = new QuestRepository(this);
    }

    public void fetchQuests(Map<String, Object> metadata){
        questRepository.fetchQuests(mainActivity.getCurrentUser().getUid(), metadata);
    }

    public void startQuest(String questUuid){
        Quest quest = questRepository.loadTempQuest(mainActivity,questUuid);
        questRepository.writeQuest(quest, mainActivity.getCurrentUser().getUid(),new HashMap<>());
    }

    public void deleteQuest(Quest quest, Map<String, Object> metadata){
        questRepository.deleteQuest(quest, mainActivity.getCurrentUser().getUid(), metadata);
    }

    public List<Quest> generateQuests(String gameLocation, int questsToGenerate){
        List<Quest> questOptions = new ArrayList<>();
        for(int i=0; i<questsToGenerate; i++){
            Quest tempQuest = QuestGenerator.generateQuest(gameLocation,2,mainActivity.getDialogService());
            questRepository.saveTempQuest(mainActivity, tempQuest);
            questOptions.add(tempQuest);
        }

        return questOptions;
    }

    public void potentialCombatQuestObjectiveCompleted(String gameLocationId, int buildingId){
        Map<String, Object> metadata = new HashMap<>();
        metadata.put(QUEST_OBJECTIVE_CHECK_FETCH, true);
        metadata.put(COMBAT_OBJECTIVE_BUILDING_KEY, buildingId);
        metadata.put(COMBAT_OBJECTIVE_CHECK_LOCATION_KEY, gameLocationId);
        fetchQuests(metadata);
    }

    public void dialogQuestObjectiveCompleted(String questUuid, String objectiveUuid){
        Map<String, Object> metadata = new HashMap<>();
        metadata.put(QUEST_OBJECTIVE_CHECK_FETCH, true);
        metadata.put(QUEST_UUID_METADATA_KEY, questUuid);
        metadata.put(OBJECTIVE_UUID_METADATA_KEY, objectiveUuid);
        questRepository.getQuest(mainActivity.getCurrentUser().getUid(),questUuid, metadata);
    }

    public void questFetchResponse(List<Quest> quests, Map<String, Object> metadata){
        if(metadata.containsKey(QUEST_OBJECTIVE_CHECK_FETCH)){
            for(Quest quest: quests){
                for(QuestObjective objective: quest.getQuestObjectives()){
                    if(!objective.isObjectiveMet() && objective.getQuestObjectiveType().equals(QuestObjectiveType.FIGHT)){
                        if(objective.getGameLocation().equals(metadata.get(COMBAT_OBJECTIVE_CHECK_LOCATION_KEY)) && objective.getBuildingId() == (Integer)metadata.get(COMBAT_OBJECTIVE_BUILDING_KEY)){
                            Log.e("QuestService", "Setting Combat Objective Met");
                            objective.setObjectiveMet(true);
                            questRepository.writeQuest(quest, mainActivity.getCurrentUser().getUid(), metadata);
                        }
                    }
                }
                boolean allObjectivesMet = true;
                for(QuestObjective objective: quest.getQuestObjectives()){
                    if(!objective.isObjectiveMet()){
                        allObjectivesMet = false;
                    }
                }
                if(allObjectivesMet){
                    mainActivity.getUserGameStateService().modifyUserGameCurrency(mainActivity.getCurrentUser().getUid(),quest.getQuestReward(),new HashMap<>());
                    mainActivity.publishEvent(new QuestCompletedEvent(quest, new HashMap<>()));
                    deleteQuest(quest, new HashMap<>());
                }
            }
        } else {
            mainActivity.publishEvent(new QuestFetchResponseEvent(quests, metadata));
        }
    }

    @Override
    public void repositoryResponse(Quest quest, Map<String, Object> metadata) {
        if(metadata.containsKey(QUEST_OBJECTIVE_CHECK_FETCH)){
            for(QuestObjective objective: quest.getQuestObjectives()){
                if(objective.getQuestObjectiveUuid().equals(metadata.get(OBJECTIVE_UUID_METADATA_KEY))){
                    objective.setObjectiveMet(true);
                    break;
                }
            }
            questRepository.writeQuest(quest, mainActivity.getCurrentUser().getUid(), metadata);
            boolean allObjectivesMet = true;
            for(QuestObjective objective: quest.getQuestObjectives()){
                if(!objective.isObjectiveMet()){
                    allObjectivesMet = false;
                }
            }
            if(allObjectivesMet){
                mainActivity.getUserGameStateService().modifyUserGameCurrency(mainActivity.getCurrentUser().getUid(),quest.getQuestReward(),new HashMap<>());
                mainActivity.publishEvent(new QuestCompletedEvent(quest, new HashMap<>()));
                deleteQuest(quest, new HashMap<>());
            }
        }
    }

    @Override
    public void interDomainServiceResponse(Object responseObject, Map<String, Object> metadata) {

    }
}
