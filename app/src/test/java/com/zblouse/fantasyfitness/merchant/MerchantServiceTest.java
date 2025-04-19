package com.zblouse.fantasyfitness.merchant;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.firebase.auth.FirebaseUser;
import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.combat.cards.AbilityTarget;
import com.zblouse.fantasyfitness.combat.cards.Card;
import com.zblouse.fantasyfitness.combat.cards.CardService;
import com.zblouse.fantasyfitness.combat.cards.HealAbility;
import com.zblouse.fantasyfitness.combat.cards.ItemCard;
import com.zblouse.fantasyfitness.combat.cards.ItemType;
import com.zblouse.fantasyfitness.user.UserGameStateService;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

public class MerchantServiceTest {

    @Test
    public void buyCardTest(){
        MerchantRepository merchantRepository = Mockito.mock(MerchantRepository.class);
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);
        when(mockActivity.getCurrentUser()).thenReturn(mockUser);
        when(mockUser.getUid()).thenReturn("testUuid");
        UserGameStateService mockuserGameStateService = Mockito.mock(UserGameStateService.class);
        when(mockActivity.getUserGameStateService()).thenReturn(mockuserGameStateService);
        CardService mockCardService = Mockito.mock(CardService.class);
        when(mockActivity.getCardService()).thenReturn(mockCardService);
        Card testCard = new ItemCard("NotUserId","cardUuid","cardName", "cardDescription", ItemType.EQUIPPABLE,new HealAbility("heal", "heals", AbilityTarget.SINGLE_ALLY,3));
        Map<Card, Integer> testMerchantMap = new HashMap<>();
        testMerchantMap.put(testCard, 7);
        Merchant testMerchant = new Merchant("merchantTag", testMerchantMap);
        when(merchantRepository.getMerchantByTag(eq("testMerchant"))).thenReturn(testMerchant);
        MerchantService testedService = new MerchantService(mockActivity);
        testedService.setMerchantRepository(merchantRepository);
        testedService.buyCard("testMerchant", testCard);
        verify(mockuserGameStateService).modifyUserGameCurrency(eq("testUuid"),eq(-7),anyMap());
        ArgumentCaptor<Card> cardArgumentCaptor = ArgumentCaptor.forClass(Card.class);
        verify(mockCardService).writeCard(cardArgumentCaptor.capture());
        assertEquals(testCard.getCardName(), cardArgumentCaptor.getValue().getCardName());
        assertEquals(testCard.getCardDescription(), cardArgumentCaptor.getValue().getCardDescription());
        assertEquals("testUuid", cardArgumentCaptor.getValue().getUserId());
        assertNotEquals(testCard.getCardUuid(), cardArgumentCaptor.getValue().getCardUuid());
    }
}
