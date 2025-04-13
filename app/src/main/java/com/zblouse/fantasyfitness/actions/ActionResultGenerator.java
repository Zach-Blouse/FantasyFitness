package com.zblouse.fantasyfitness.actions;

import com.zblouse.fantasyfitness.quest.Quest;

import java.util.List;
import java.util.Map;

public interface ActionResultGenerator {

    public ActionResult generate(List<Quest> quests, Map<String, Object> metadata);
}
