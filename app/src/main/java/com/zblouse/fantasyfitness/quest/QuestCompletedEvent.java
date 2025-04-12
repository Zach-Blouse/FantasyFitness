package com.zblouse.fantasyfitness.quest;

import com.zblouse.fantasyfitness.core.Event;
import com.zblouse.fantasyfitness.core.EventType;

import java.util.Map;

public class QuestCompletedEvent extends Event {
    private final Quest quest;

    public QuestCompletedEvent(Quest quest, Map<String, Object> metadata){
        super(EventType.QUEST_COMPLETED_EVENT, metadata);
        this.quest = quest;
    }

    public Quest getQuest(){
        return this.quest;
    }
}
