package com.zblouse.fantasyfitness.quest;

import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.core.DomainService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuestService implements DomainService<Quest> {

    private MainActivity mainActivity;
    private QuestRepository questRepository;

    public QuestService(MainActivity mainActivity){
        this.mainActivity = mainActivity;
        this.questRepository = new QuestRepository(this);
    }

    public void fetchQuests(Map<String, Object> metadata){
        questRepository.fetchQuests(mainActivity.getCurrentUser().getUid(), metadata);
    }

    public void writeQuest(Quest quest, Map<String, Object> metadata){
        questRepository.writeQuest(quest, mainActivity.getCurrentUser().getUid(), metadata);
    }

    public void deleteQuest(Quest quest, Map<String, Object> metadata){
        questRepository.deleteQuest(quest, mainActivity.getCurrentUser().getUid(), metadata);
    }

    public List<Quest> generateQuests(String gameLocation, int questsToGenerate){
        List<Quest> questOptions = new ArrayList<>();
        for(int i=0; i<questsToGenerate; i++){
            questOptions.add(QuestGenerator.generateQuest(gameLocation,5,mainActivity.getDialogService()));
        }

        return questOptions;
    }

    public void questFetchResponse(List<Quest> quests, Map<String, Object> metadata){
        mainActivity.publishEvent(new QuestFetchResponseEvent(quests, metadata));
    }

    @Override
    public void repositoryResponse(Quest responseBody, Map<String, Object> metadata) {

    }

    @Override
    public void interDomainServiceResponse(Object responseObject, Map<String, Object> metadata) {

    }
}
