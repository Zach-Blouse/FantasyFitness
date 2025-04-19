package com.zblouse.fantasyfitness.quest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.zblouse.fantasyfitness.R;
import com.zblouse.fantasyfitness.world.GameLocationService;

import org.junit.Test;

import java.util.UUID;

public class QuestObjectiveTest {

    @Test
    public void fightQuestObjectiveDescriptionTest(){
        QuestObjective testedObjective = new QuestObjective(QuestObjectiveType.FIGHT, UUID.randomUUID().toString(), GameLocationService.WOODLANDS, R.id.marsh_button,false);
        assertEquals("Go to Woodlands and visit the marsh and fight the monsters there.", testedObjective.getQuestObjectiveDescription());
    }

    @Test
    public void visitQuestObjectiveDescriptionTest(){
        QuestObjective testedObjective = new QuestObjective(QuestObjectiveType.VISIT, UUID.randomUUID().toString(), GameLocationService.THANADEL_VILLAGE, R.id.inn_button,false);
        assertEquals("Go to Thanadel Village and visit the inn", testedObjective.getQuestObjectiveDescription());
    }

    @Test
    public void questObjectiveConstructorTest(){
        QuestObjective testedObjective = new QuestObjective(QuestObjectiveType.FIGHT, "testuuid", GameLocationService.WOODLANDS, R.id.marsh_button,false);
        assertEquals(QuestObjectiveType.FIGHT, testedObjective.getQuestObjectiveType());
        assertEquals(R.id.marsh_button, testedObjective.getBuildingId());
        assertEquals("testuuid", testedObjective.getQuestObjectiveUuid());
        assertFalse(testedObjective.isObjectiveMet());
    }

    @Test
    public void questObjectiveEmptyConstructorTest(){
        QuestObjective testedObjective = new QuestObjective();
        testedObjective.setObjectiveMet(true);
        testedObjective.setQuestDialogId("questDialogId");
        assertEquals("questDialogId", testedObjective.getQuestDialogId());
        assertTrue(testedObjective.isObjectiveMet());
    }
}
