package com.zblouse.fantasyfitness.combat.cards;

import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.firebase.auth.FirebaseUser;
import com.zblouse.fantasyfitness.activity.MainActivity;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class DeckServiceTest {

    @Test
    public void writeNewDeckTest(){
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        when(mockActivity.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.getUid()).thenReturn("testUid");
        DeckRepository mockRepository = Mockito.mock(DeckRepository.class);

        DeckService testedService = new DeckService();
        testedService.setMainActivity(mockActivity);
        testedService.setDeckRepository(mockRepository);

        testedService.writeNewDeck("deckName", Arrays.asList("1","2","3"));

        ArgumentCaptor<Deck> deckArgumentCaptor = ArgumentCaptor.forClass(Deck.class);
        verify(mockRepository).writeDeck(deckArgumentCaptor.capture(),anyMap());
        assert(deckArgumentCaptor.getValue().getCardUuids().containsAll(Arrays.asList("1","2","3")));
    }

    @Test
    public void fetchDeckTest(){
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        DeckRepository mockRepository = Mockito.mock(DeckRepository.class);

        DeckService testedService = new DeckService();
        testedService.setMainActivity(mockActivity);
        testedService.setDeckRepository(mockRepository);

        testedService.fetchDeck("userId","testDeck");

        verify(mockRepository).fetchDeck(eq("userId"),eq("testDeck"),anyMap());
    }

    @Test
    public void repositoryResponseLoadCardsTest(){
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        DeckRepository mockRepository = Mockito.mock(DeckRepository.class);
        CardService mockCardService = Mockito.mock(CardService.class);
        when(mockActivity.getCardService()).thenReturn(mockCardService);

        DeckService testedService = new DeckService();
        testedService.setMainActivity(mockActivity);
        testedService.setDeckRepository(mockRepository);

        Deck deck = new Deck("testUid","deckName",Arrays.asList("1", "2"));

        testedService.repositoryResponse(deck,new HashMap<>());

        ArgumentCaptor<List> listArgumentCaptor = ArgumentCaptor.forClass(List.class);

        verify(mockCardService).getCardList(listArgumentCaptor.capture(),anyMap());

        assert(listArgumentCaptor.getValue().containsAll(Arrays.asList("1", "2")));
    }

    @Test
    public void repositoryResponseNoLoadCardsTest(){
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        DeckRepository mockRepository = Mockito.mock(DeckRepository.class);
        CardService mockCardService = Mockito.mock(CardService.class);
        when(mockActivity.getCardService()).thenReturn(mockCardService);

        DeckService testedService = new DeckService();
        testedService.setMainActivity(mockActivity);
        testedService.setDeckRepository(mockRepository);

        Deck deck = new Deck("testUid","deckName",Arrays.asList("1", "2"));
        HealAbility testCharacterHealAbility =  new HealAbility("HealAbility","testAbility",AbilityTarget.SINGLE_ALLY,5);
        DamageAbility testCharacterDamageAbility = new DamageAbility("burn","burn description",AbilityTarget.SINGLE_ENEMY,DamageType.FIRE,AttackType.RANGED,5);
        CharacterCard testCharacterCard = new CharacterCard("testUser", UUID.randomUUID().toString(),
                "characterCard","testCharacterDescription",10,
                Arrays.asList( testCharacterHealAbility, testCharacterDamageAbility)
                ,new ArrayList<>());
        deck.setCards(Arrays.asList(testCharacterCard));

        testedService.repositoryResponse(deck,new HashMap<>());

        ArgumentCaptor<DeckFetchEvent> deckFetchEventArgumentCaptor = ArgumentCaptor.forClass(DeckFetchEvent.class);

        verify(mockActivity).publishEvent(deckFetchEventArgumentCaptor.capture());
        assert(deckFetchEventArgumentCaptor.getValue().getDeck().getCardUuids().containsAll(Arrays.asList("1", "2")));

    }
}
