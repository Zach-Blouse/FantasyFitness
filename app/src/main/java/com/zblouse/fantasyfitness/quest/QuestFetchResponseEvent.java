package com.zblouse.fantasyfitness.quest;

import com.zblouse.fantasyfitness.core.Event;
import com.zblouse.fantasyfitness.core.EventType;

import java.util.List;
import java.util.Map;

public class QuestFetchResponseEvent extends Event {

    private final List<Quest> quests;

    public QuestFetchResponseEvent(List<Quest> quests, Map<String, Object> metadata){
        super(EventType.QUEST_FETCH_RESPONSE_EVENT, metadata);
        this.quests = quests;
    }

    public List<Quest> getQuests(){
        return quests;
    }
}
