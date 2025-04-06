package com.zblouse.fantasyfitness.combat.encounter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.firebase.auth.FirebaseUser;
import com.zblouse.fantasyfitness.activity.MainActivity;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;

public class EncounterServiceTest {

    @Test
    public void fetchEncounterTest(){
        MainActivity mockActivity = Mockito.mock(MainActivity.class);

        EncounterRepository mockRepository = Mockito.mock(EncounterRepository.class);

        EncounterService testedService = new EncounterService();
        testedService.setMainActivity(mockActivity);
        testedService.setEncounterRepository(mockRepository);

        testedService.fetchEncounter("testEncounter",new HashMap<>());

        verify(mockRepository).fetchEncounter(eq("testEncounter"), anyMap());
    }

    @Test
    public void repositoryResponseTest(){
        MainActivity mockActivity = Mockito.mock(MainActivity.class);

        EncounterRepository mockRepository = Mockito.mock(EncounterRepository.class);

        EncounterService testedService = new EncounterService();
        testedService.setMainActivity(mockActivity);
        testedService.setEncounterRepository(mockRepository);

        Encounter encounter = new Encounter("testEncounter", EncounterDifficultyLevel.EASY,new ArrayList<>());

        testedService.repositoryResponse(encounter,new HashMap<>());

        ArgumentCaptor<EncounterFetchEvent> encounterFetchEventArgumentCaptor = ArgumentCaptor.forClass(EncounterFetchEvent.class);
        verify(mockActivity).publishEvent(encounterFetchEventArgumentCaptor.capture());

        assertNotNull(encounterFetchEventArgumentCaptor.getValue());
        assertEquals(encounter,encounterFetchEventArgumentCaptor.getValue().getEncounter());
    }

    @Test
    public void initializeEncountersTest(){
        MainActivity mockActivity = Mockito.mock(MainActivity.class);

        EncounterRepository mockRepository = Mockito.mock(EncounterRepository.class);

        EncounterService testedService = new EncounterService();
        testedService.setMainActivity(mockActivity);
        testedService.setEncounterRepository(mockRepository);

        testedService.initializeEncounters();

        ArgumentCaptor<Encounter> encounterArgumentMatcher = ArgumentCaptor.forClass(Encounter.class);

        verify(mockRepository, times(2)).writeEncounter(encounterArgumentMatcher.capture(),anyMap());

        Encounter encounter1 = encounterArgumentMatcher.getAllValues().get(0);
        assertEquals("Bandit", encounter1.getEncounterName());
        assertEquals(EncounterDifficultyLevel.MEDIUM, encounter1.getEncounterDifficultyLevel());

        Encounter encounter2 = encounterArgumentMatcher.getAllValues().get(1);
        assertEquals("Goblin Attack", encounter2.getEncounterName());
        assertEquals(EncounterDifficultyLevel.EASY, encounter2.getEncounterDifficultyLevel());
    }
}
