package com.zblouse.fantasyfitness.combat.cards;

import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.HashMap;

public class DeckRepositoryTest {

    @Test
    public void writeDeckTest(){
        DeckService mockDeckService = Mockito.mock(DeckService.class);
        DeckFirestoreDatabase mockFirestoreDatabase = Mockito.mock(DeckFirestoreDatabase.class);

        DeckRepository testedRepository = new DeckRepository(mockFirestoreDatabase, mockDeckService);

        Deck deck = new Deck("testUserId","testDeck");
        testedRepository.writeDeck(deck,new HashMap<>());
        verify(mockFirestoreDatabase).writeDeck(eq(deck),eq(testedRepository),anyMap());
    }

    @Test
    public void fetchDeckTest(){
        DeckService mockDeckService = Mockito.mock(DeckService.class);
        DeckFirestoreDatabase mockFirestoreDatabase = Mockito.mock(DeckFirestoreDatabase.class);

        DeckRepository testedRepository = new DeckRepository(mockFirestoreDatabase, mockDeckService);

        testedRepository.fetchDeck("userId","deckName",new HashMap<>());
        verify(mockFirestoreDatabase).readDeck(eq("userId"),eq("deckName"),eq(testedRepository),anyMap());
    }

    @Test
    public void readCallbackTest(){
        DeckService mockDeckService = Mockito.mock(DeckService.class);
        DeckFirestoreDatabase mockFirestoreDatabase = Mockito.mock(DeckFirestoreDatabase.class);

        DeckRepository testedRepository = new DeckRepository(mockFirestoreDatabase, mockDeckService);

        Deck deck = new Deck("testUserId","testDeck");
        testedRepository.readCallback(deck,new HashMap<>());
        verify(mockDeckService).repositoryResponse(eq(deck), anyMap());
    }
}
