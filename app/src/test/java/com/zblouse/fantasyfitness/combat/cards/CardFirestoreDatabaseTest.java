package com.zblouse.fantasyfitness.combat.cards;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CardFirestoreDatabaseTest {

    @Test
    public void writeTest(){
        FirebaseFirestore mockFirestore = Mockito.mock(FirebaseFirestore.class);
        CollectionReference mockCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockFirestore.collection(eq("userCards"))).thenReturn(mockCollectionReference);
        DocumentReference mockDocument = Mockito.mock(DocumentReference.class);
        when(mockCollectionReference.document(eq("testUser"))).thenReturn(mockDocument);
        CollectionReference mockCollectionReference2 = Mockito.mock(CollectionReference.class);
        when(mockDocument.collection(eq("cards"))).thenReturn(mockCollectionReference2);
        DocumentReference mockDocument2 = Mockito.mock(DocumentReference.class);
        when(mockCollectionReference2.document(eq("testUuid"))).thenReturn(mockDocument2);

        Task mockTask = Mockito.mock(Task.class);
        when(mockTask.isSuccessful()).thenReturn(true);
        when(mockDocument2.set(any())).thenReturn(mockTask);
        CardRepository mockRepository = Mockito.mock(CardRepository.class);

        CardFirestoreDatabase testedDatabase = new CardFirestoreDatabase(mockFirestore);

        Card card = new ItemCard("testUser", "testUuid", "testCard", "testDescription", ItemType.CONSUMABLE,new HealAbility("HealAbility","testAbility",AbilityTarget.SINGLE_ALLY,5));
        testedDatabase.write(card,mockRepository,new HashMap<>());

        ArgumentCaptor<OnCompleteListener<Void>> onCompleteListenerArgumentCaptor = ArgumentCaptor.forClass(OnCompleteListener.class);
        verify(mockTask).addOnCompleteListener((OnCompleteListener<Void>) onCompleteListenerArgumentCaptor.capture());
        onCompleteListenerArgumentCaptor.getValue().onComplete(mockTask);

        verify(mockRepository).writeCallback(eq(card),anyMap());

    }

    @Test
    public void fetchListOfCardsTest(){
        FirebaseFirestore mockFirestore = Mockito.mock(FirebaseFirestore.class);
        CollectionReference mockCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockFirestore.collection(eq("userCards"))).thenReturn(mockCollectionReference);
        DocumentReference mockDocument = Mockito.mock(DocumentReference.class);
        when(mockCollectionReference.document(eq("testUser"))).thenReturn(mockDocument);
        CollectionReference mockCollectionReference2 = Mockito.mock(CollectionReference.class);
        when(mockDocument.collection(eq("cards"))).thenReturn(mockCollectionReference2);
        Query mockQuery = Mockito.mock(Query.class);
        when(mockCollectionReference2.whereIn(eq("cardUuid"), anyList())).thenReturn(mockQuery);
        Task mockTask = Mockito.mock(Task.class);
        when(mockQuery.get()).thenReturn(mockTask);
        when(mockTask.isSuccessful()).thenReturn(true);

        QueryDocumentSnapshot mockCharacterSnapshot = Mockito.mock(QueryDocumentSnapshot.class);

        HealAbility testCharacterHealAbility =  new HealAbility("HealAbility","testAbility",AbilityTarget.SINGLE_ALLY,5);
        DamageAbility testCharacterDamageAbility = new DamageAbility("burn","burn description",AbilityTarget.SINGLE_ENEMY,DamageType.FIRE,AttackType.RANGED,5);
        CharacterCard testCharacterCard = new CharacterCard("testUser", UUID.randomUUID().toString(),
                "characterCard","testCharacterDescription",10,
                Arrays.asList( testCharacterHealAbility, testCharacterDamageAbility)
                ,new ArrayList<>());
        when(mockCharacterSnapshot.getData()).thenReturn(convertCardToMap(testCharacterCard));

        QueryDocumentSnapshot mockItemSnapshot = Mockito.mock(QueryDocumentSnapshot.class);
        ItemCard testItemCard = new ItemCard("testUser",UUID.randomUUID().toString(),"testItemCard","testDescription", ItemType.EQUIPPABLE,new BuffAbility("damageBuff","test",AbilityTarget.SINGLE_ALLY,BuffType.ATTACK,5));
        when(mockItemSnapshot.getData()).thenReturn(convertCardToMap(testItemCard));

        QueryDocumentSnapshot mockEffectSnapshot = Mockito.mock(QueryDocumentSnapshot.class);
        EffectCard testEffectCard = new EffectCard("testUser",UUID.randomUUID().toString(),"testItemCard","testDescription",new BuffAbility("damageBuff","test",AbilityTarget.SINGLE_ALLY,BuffType.ATTACK,5));
        when(mockEffectSnapshot.getData()).thenReturn(convertCardToMap(testEffectCard));

        QueryDocumentSnapshot mockPermanentSnapshot = Mockito.mock(QueryDocumentSnapshot.class);
        PermanentCard testPermanentCard = new PermanentCard("testUser",UUID.randomUUID().toString(),"testItemCard","testDescription", new DebuffAbility("damageDebuff","test",AbilityTarget.SINGLE_ALLY,BuffType.ATTACK,5));
        when(mockPermanentSnapshot.getData()).thenReturn(convertCardToMap(testPermanentCard));

        QuerySnapshot querySnapshot = Mockito.mock(QuerySnapshot.class);
        when(querySnapshot.iterator()).thenReturn((Arrays.asList(mockCharacterSnapshot, mockItemSnapshot, mockEffectSnapshot, mockPermanentSnapshot)).iterator());
        when(mockTask.getResult()).thenReturn(querySnapshot);

        CardRepository mockRepository = Mockito.mock(CardRepository.class);

        CardFirestoreDatabase testedDatabase = new CardFirestoreDatabase(mockFirestore);

        testedDatabase.fetchListOfCards("testUser",Arrays.asList(testCharacterCard.getCardUuid(), testItemCard.getCardUuid()),mockRepository, new HashMap<>());

        ArgumentCaptor<OnCompleteListener<QuerySnapshot>> onCompleteListenerArgumentCaptor = ArgumentCaptor.forClass(OnCompleteListener.class);
        verify(mockTask).addOnCompleteListener((OnCompleteListener<QuerySnapshot>) onCompleteListenerArgumentCaptor.capture());
        onCompleteListenerArgumentCaptor.getValue().onComplete(mockTask);

        ArgumentCaptor<List> listCaptor = ArgumentCaptor.forClass(List.class);
        verify(mockRepository).listReadCallback(listCaptor.capture(),anyMap());

        List<Card> returnedList = listCaptor.getValue();
        assertEquals(4, returnedList.size());
        boolean itemCardCorrect = false;
        boolean characterCardCorrect = false;
        boolean permanentCardCorrect = false;
        boolean effectCardCorrect = false;

        for(Card returnedCard: returnedList){
            if(returnedCard.getCardType().equals(CardType.ITEM)) {
                ItemCard itemCard = (ItemCard) returnedCard;
                assertEquals(testItemCard.getCardName(), itemCard.getCardName());
                assertEquals(testItemCard.getCardDescription(), itemCard.getCardDescription());
                assertEquals(testItemCard.getCardUuid(), itemCard.getCardUuid());
                Ability returnedAbility = itemCard.getAbility();
                assertEquals(testItemCard.getAbility().getAbilityName(), returnedAbility.getAbilityName());
                assertEquals(testItemCard.getAbility().getAbilityDescription(), returnedAbility.getAbilityDescription());
                assertEquals(testItemCard.getAbility().getAbilityTarget(), returnedAbility.getAbilityTarget());
                itemCardCorrect = true;
            } else if(returnedCard.getCardType().equals(CardType.PERMANENT)){
                PermanentCard returnedPermanantCard = (PermanentCard) returnedCard;
                assertEquals(testPermanentCard.getCardName(),returnedPermanantCard.getCardName());
                assertEquals(testPermanentCard.getCardDescription(), returnedPermanantCard.getCardDescription());
                assertEquals(testPermanentCard.getCardUuid(), returnedPermanantCard.getCardUuid());
                Ability returnedAbility = returnedPermanantCard.getAbility();
                assertEquals(testPermanentCard.getAbility().getAbilityName(),returnedAbility.getAbilityName());
                assertEquals(testPermanentCard.getAbility().getAbilityDescription(), returnedAbility.getAbilityDescription());
                assertEquals(testPermanentCard.getAbility().getAbilityTarget(), returnedAbility.getAbilityTarget());
                permanentCardCorrect = true;
            } else if(returnedCard.getCardType().equals(CardType.EFFECT)){
                EffectCard returnedEffectCard = (EffectCard) returnedCard;
                assertEquals(testEffectCard.getCardName(),returnedEffectCard.getCardName());
                assertEquals(testEffectCard.getCardDescription(), returnedEffectCard.getCardDescription());
                assertEquals(testEffectCard.getCardUuid(), returnedEffectCard.getCardUuid());
                Ability returnedAbility = returnedEffectCard.getAbility();
                assertEquals(testEffectCard.getAbility().getAbilityName(),returnedAbility.getAbilityName());
                assertEquals(testEffectCard.getAbility().getAbilityDescription(), returnedAbility.getAbilityDescription());
                assertEquals(testEffectCard.getAbility().getAbilityTarget(), returnedAbility.getAbilityTarget());
                effectCardCorrect = true;
            } else if(returnedCard.getCardType().equals(CardType.CHARACTER)){
                CharacterCard returnedCharacterCard = (CharacterCard) returnedCard;
                assertEquals(testCharacterCard.getCardName(), returnedCharacterCard.getCardName());
                assertEquals(testCharacterCard.getCardUuid(), returnedCharacterCard.getCardUuid());
                assertEquals(testCharacterCard.getCardDescription(), returnedCharacterCard.getCardDescription());
                assertEquals(testCharacterCard.getMaxHealth(), returnedCharacterCard.getMaxHealth());
                boolean healAbilityCorrect = false;
                boolean damageAbilityCorrect = false;
                for(Ability returnedAbility: returnedCharacterCard.getAbilities()){
                    if(returnedAbility.getAbilityType().equals(AbilityType.DAMAGE)){
                        DamageAbility returnedDamageAbility = (DamageAbility)returnedAbility;
                        assertEquals(returnedDamageAbility.getAbilityTarget(), testCharacterDamageAbility.getAbilityTarget());
                        assertEquals(returnedDamageAbility.getAbilityName(), testCharacterDamageAbility.getAbilityName());
                        assertEquals(returnedDamageAbility.getAbilityDescription(), testCharacterDamageAbility.getAbilityDescription());
                        assertEquals(returnedDamageAbility.getDamageAmount(), testCharacterDamageAbility.getDamageAmount());
                        assertEquals(returnedDamageAbility.getDamageType(), testCharacterDamageAbility.getDamageType());
                        assertEquals(returnedDamageAbility.getAttackType(), testCharacterDamageAbility.getAttackType());
                        damageAbilityCorrect = true;
                    } else if(returnedAbility.getAbilityType().equals(AbilityType.HEAL)){
                        HealAbility returnedHealAbility = (HealAbility)returnedAbility;
                        assertEquals(returnedHealAbility.getAbilityTarget(), testCharacterHealAbility.getAbilityTarget());
                        assertEquals(returnedHealAbility.getAbilityName(), testCharacterHealAbility.getAbilityName());
                        assertEquals(returnedHealAbility.getAbilityDescription(), testCharacterHealAbility.getAbilityDescription());
                        assertEquals(returnedHealAbility.getHealAmount(), testCharacterHealAbility.getHealAmount());
                        healAbilityCorrect = true;
                    }
                }
                assert(damageAbilityCorrect && healAbilityCorrect);
                characterCardCorrect = true;
            }
        }
        assert(characterCardCorrect && itemCardCorrect && permanentCardCorrect && effectCardCorrect);
    }

    private Map<String, Object> convertCardToMap(Card card){
        Map<String, Object> cardMap = new HashMap<>();
        cardMap.put(Card.USER_ID_FIELD,card.getUserId());
        cardMap.put(Card.CARD_UUID_FIELD, card.getCardUuid());
        cardMap.put(Card.CARD_DESCRIPTION_FIELD, card.getCardDescription());
        cardMap.put(Card.CARD_TYPE_FIELD, card.getCardType().toString());
        cardMap.put(Card.CARD_NAME_FIELD, card.getCardName());
        if(card.getCardType().equals(CardType.CHARACTER)){
            CharacterCard characterCard = (CharacterCard)card;
            cardMap.put(CharacterCard.MAX_HEALTH_FIELD, (long)characterCard.getMaxHealth());
            List<Map<String,Object>> abilities = new ArrayList<>();
            for(Ability ability: characterCard.getAbilities()){
                abilities.add(convertAbilityToMap(ability));
            }
            cardMap.put(CharacterCard.ABILITIES_FIELD,abilities);
        } else if(card.getCardType().equals(CardType.ITEM)){
            ItemCard itemCard = (ItemCard)card;
            cardMap.put(ItemCard.ABILITY_FIELD, convertAbilityToMap(itemCard.getAbility()));
            cardMap.put(ItemCard.ITEM_TYPE_FIELD, itemCard.getItemType().toString());
        }else if(card.getCardType().equals(CardType.PERMANENT)){
            PermanentCard permanentCard = (PermanentCard)card;
            cardMap.put(PermanentCard.ABILITY_FIELD, convertAbilityToMap(permanentCard.getAbility()));
        }else if(card.getCardType().equals(CardType.EFFECT)){
            EffectCard effectCard = (EffectCard)card;
            cardMap.put(PermanentCard.ABILITY_FIELD, convertAbilityToMap(effectCard.getAbility()));
        }
        return cardMap;
    }

    private Map<String, Object> convertAbilityToMap(Ability ability){
        Map<String, Object> abilityMap = new HashMap<>();
        abilityMap.put(Ability.ABILITY_TARGET_FIELD, ability.getAbilityTarget().toString());
        abilityMap.put(Ability.ABILITY_TYPE_FIELD, ability.getAbilityType().toString());
        abilityMap.put(Ability.DESCRIPTION_FIELD, ability.getAbilityDescription());
        abilityMap.put(Ability.NAME_FIELD, ability.getAbilityName());
        if(ability.getAbilityType().equals(AbilityType.DAMAGE)){
            DamageAbility damageAbility = (DamageAbility)ability;
            abilityMap.put(DamageAbility.ATTACK_TYPE_FIELD, damageAbility.getAttackType().toString());
            abilityMap.put(DamageAbility.DAMAGE_AMOUNT_FIELD, (long)damageAbility.getDamageAmount());
            abilityMap.put(DamageAbility.DAMAGE_TYPE_FIELD, damageAbility.getDamageType().toString());
        } else if(ability.getAbilityType().equals(AbilityType.HEAL)){
            HealAbility healAbility = (HealAbility)ability;
            abilityMap.put(HealAbility.HEAL_AMOUNT_FIELD, (long)healAbility.getHealAmount());
        } else if(ability.getAbilityType().equals(AbilityType.BUFF)){
            BuffAbility buffAbility = (BuffAbility) ability;
            abilityMap.put(BuffAbility.BUFF_AMOUNT_FIELD, (long)buffAbility.getBuffAmount());
            abilityMap.put(BuffAbility.BUFF_TYPE_FIELD, buffAbility.getBuffType().toString());
        } else if(ability.getAbilityType().equals(AbilityType.DEBUFF)){
            DebuffAbility buffAbility = (DebuffAbility) ability;
            abilityMap.put(DebuffAbility.DEBUFF_AMOUNT_FIELD, (long)buffAbility.getDeuffAmount());
            abilityMap.put(DebuffAbility.BUFF_TYPE_FIELD, buffAbility.getBuffType().toString());
        }
        return abilityMap;
    }
}
