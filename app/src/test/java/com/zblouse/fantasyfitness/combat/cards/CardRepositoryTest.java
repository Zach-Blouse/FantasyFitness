package com.zblouse.fantasyfitness.combat.cards;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CardRepositoryTest {

    @Test
    public void writeCardTest(){
        CardService mockCardService = Mockito.mock(CardService.class);
        CardFirestoreDatabase mockFirestoreDatabase = Mockito.mock(CardFirestoreDatabase.class);

        CardRepository cardRepository = new CardRepository(mockFirestoreDatabase, mockCardService);

        Card testCard = new ItemCard("userId", "cardUuid", "testCard", "cardDescription", ItemType.CONSUMABLE,new HealAbility("heal","abilityDescription",AbilityTarget.SINGLE_ALLY,5));

        cardRepository.writeCard(testCard,new HashMap<>());

        verify(mockFirestoreDatabase).write(eq(testCard), eq(cardRepository), anyMap());
    }

    @Test
    public void fetchCardListTest(){
        CardService mockCardService = Mockito.mock(CardService.class);
        CardFirestoreDatabase mockFirestoreDatabase = Mockito.mock(CardFirestoreDatabase.class);

        CardRepository cardRepository = new CardRepository(mockFirestoreDatabase, mockCardService);
        List<String> cardList = Arrays.asList("1","2");
        cardRepository.fetchCardList("userId", cardList,new HashMap<>());

        verify(mockFirestoreDatabase).fetchListOfCards(eq("userId"), eq(cardList), eq(cardRepository), anyMap());
    }

    @Test
    public void listReadCallbackTest(){
        CardService mockCardService = Mockito.mock(CardService.class);
        CardFirestoreDatabase mockFirestoreDatabase = Mockito.mock(CardFirestoreDatabase.class);

        CardRepository cardRepository = new CardRepository(mockFirestoreDatabase, mockCardService);
        List<Card> cardList = Arrays.asList(new ItemCard("userId", "cardUuid", "testCard", "cardDescription", ItemType.CONSUMABLE,new HealAbility("heal","abilityDescription",AbilityTarget.SINGLE_ALLY,5)));
        cardRepository.listReadCallback(cardList,new HashMap<>());

        verify(mockCardService).repositoryResponse(eq(cardList), anyMap());
    }

    @Test
    public void readCallbackTest(){
        CardService mockCardService = Mockito.mock(CardService.class);
        CardFirestoreDatabase mockFirestoreDatabase = Mockito.mock(CardFirestoreDatabase.class);

        CardRepository cardRepository = new CardRepository(mockFirestoreDatabase, mockCardService);
        Card testCard = new ItemCard("userId", "cardUuid", "testCard", "cardDescription", ItemType.CONSUMABLE,new HealAbility("heal","abilityDescription",AbilityTarget.SINGLE_ALLY,5));
        cardRepository.readCallback(testCard,new HashMap<>());

        verify(mockCardService).repositoryResponse(eq(testCard), anyMap());
    }

    @Test
    public void writeCallbackTest(){
        CardService mockCardService = Mockito.mock(CardService.class);
        CardFirestoreDatabase mockFirestoreDatabase = Mockito.mock(CardFirestoreDatabase.class);

        CardRepository cardRepository = new CardRepository(mockFirestoreDatabase, mockCardService);
        Card testCard = new ItemCard("userId", "cardUuid", "testCard", "cardDescription", ItemType.CONSUMABLE,new HealAbility("heal","abilityDescription",AbilityTarget.SINGLE_ALLY,5));
        cardRepository.writeCallback(testCard,new HashMap<>());

        verify(mockCardService).repositoryResponse(eq(testCard), anyMap());
    }

    @Test
    public void updateCallbackTest(){
        CardService mockCardService = Mockito.mock(CardService.class);
        CardFirestoreDatabase mockFirestoreDatabase = Mockito.mock(CardFirestoreDatabase.class);

        CardRepository cardRepository = new CardRepository(mockFirestoreDatabase, mockCardService);
        Card testCard = new ItemCard("userId", "cardUuid", "testCard", "cardDescription", ItemType.CONSUMABLE,new HealAbility("heal","abilityDescription",AbilityTarget.SINGLE_ALLY,5));
        cardRepository.updateCallback(true,new HashMap<>());

        verify(mockCardService).repositoryResponse((Card)isNull(), anyMap());
    }
}
