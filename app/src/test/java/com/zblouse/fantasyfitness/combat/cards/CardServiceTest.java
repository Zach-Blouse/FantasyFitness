package com.zblouse.fantasyfitness.combat.cards;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.firebase.auth.FirebaseUser;
import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.combat.CombatService;
import com.zblouse.fantasyfitness.core.DomainService;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardServiceTest {

    @Test
    public void getCardListTest(){
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        when(mockActivity.getCurrentUser()).thenReturn(mockUser);
        String testUid = "testUid1";
        when(mockUser.getUid()).thenReturn(testUid);
        CardRepository mockRepository = Mockito.mock(CardRepository.class);

        CardService testedService = new CardService();
        testedService.setMainActivity(mockActivity);
        testedService.setCardRepository(mockRepository);

        List<String> testUuids = Arrays.asList("1", "2");
        testedService.getCardList(testUuids,new HashMap<>());

        verify(mockRepository).fetchCardList(eq(testUid),eq(testUuids),anyMap());
    }

    @Test
    public void repositoryResponseTest(){
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        DeckService deckService = Mockito.mock(DeckService.class);
        when(mockActivity.getCurrentUser()).thenReturn(mockUser);
        String testUid = "testUid1";
        when(mockUser.getUid()).thenReturn(testUid);
        CardRepository mockRepository = Mockito.mock(CardRepository.class);

        CardService testedService = new CardService();
        testedService.setMainActivity(mockActivity);
        testedService.setCardRepository(mockRepository);

        List<Card> testCards = Arrays.asList(new ItemCard("userId","cardUuid","cardName","cardDescription",ItemType.CONSUMABLE, new HealAbility("abilityName", "abilityDescription", AbilityTarget.SINGLE_ALLY,8)));
        Map<String, Object> metadata = new HashMap<>();
        metadata.put(DomainService.INTER_DOMAIN_SERVICE_ORIGIN_KEY, deckService);
        testedService.repositoryResponse(testCards,metadata);

        verify(deckService).interDomainServiceResponse(eq(testCards),anyMap());
    }

    @Test
    public void initializeCardsTest(){
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        DeckService deckService = Mockito.mock(DeckService.class);
        when(mockActivity.getDeckService()).thenReturn(deckService);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        when(mockActivity.getCurrentUser()).thenReturn(mockUser);
        String testUid = "testUid1";
        when(mockUser.getUid()).thenReturn(testUid);
        CardRepository mockRepository = Mockito.mock(CardRepository.class);

        CardService testedService = new CardService();
        testedService.setMainActivity(mockActivity);
        testedService.setCardRepository(mockRepository);

        testedService.initializeCards();

        verify(mockRepository, times(16)).writeCard(any(),anyMap());
        verify(deckService).writeNewDeck(eq("userDeck"), anyList());
    }
}
