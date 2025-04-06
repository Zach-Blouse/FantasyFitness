package com.zblouse.fantasyfitness.combat.encounter;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.zblouse.fantasyfitness.combat.cards.Ability;
import com.zblouse.fantasyfitness.combat.cards.AbilityTarget;
import com.zblouse.fantasyfitness.combat.cards.AbilityType;
import com.zblouse.fantasyfitness.combat.cards.AttackType;
import com.zblouse.fantasyfitness.combat.cards.BuffAbility;
import com.zblouse.fantasyfitness.combat.cards.BuffType;
import com.zblouse.fantasyfitness.combat.cards.Card;
import com.zblouse.fantasyfitness.combat.cards.CardType;
import com.zblouse.fantasyfitness.combat.cards.CharacterCard;
import com.zblouse.fantasyfitness.combat.cards.DamageAbility;
import com.zblouse.fantasyfitness.combat.cards.DamageType;
import com.zblouse.fantasyfitness.combat.cards.HealAbility;
import com.zblouse.fantasyfitness.combat.cards.ItemCard;
import com.zblouse.fantasyfitness.combat.cards.ItemType;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class EncounterFirestoreDatabaseTest {

    @Test
    public void readTest(){
        FirebaseFirestore mockFirestore = Mockito.mock(FirebaseFirestore.class);
        CollectionReference mockCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockFirestore.collection(eq("encounters"))).thenReturn(mockCollectionReference);
        DocumentReference mockDocument = Mockito.mock(DocumentReference.class);
        when(mockCollectionReference.document(eq("testEncounter"))).thenReturn(mockDocument);
        Task mockTask = Mockito.mock(Task.class);
        when(mockDocument.get()).thenReturn(mockTask);
        when(mockTask.isSuccessful()).thenReturn(true);
        DocumentSnapshot mockDocumentSnapshot = Mockito.mock(DocumentSnapshot.class);
        when(mockTask.getResult()).thenReturn(mockDocumentSnapshot);
        when(mockDocumentSnapshot.exists()).thenReturn(true);
        when(mockDocumentSnapshot.getData()).thenReturn(generateGoblinEncounter());
        EncounterRepository mockRepository = Mockito.mock(EncounterRepository.class);

        EncounterFirestoreDatabase testedDatabase = new EncounterFirestoreDatabase(mockFirestore);

        testedDatabase.read("testEncounter",mockRepository,new HashMap<>());
        ArgumentCaptor<OnCompleteListener<Void>> onCompleteListenerArgumentCaptor = ArgumentCaptor.forClass(OnCompleteListener.class);
        verify(mockTask).addOnCompleteListener((OnCompleteListener<Void>) onCompleteListenerArgumentCaptor.capture());
        onCompleteListenerArgumentCaptor.getValue().onComplete(mockTask);

        ArgumentCaptor<Encounter> encounterArgumentCaptor = ArgumentCaptor.forClass(Encounter.class);
        verify(mockRepository).readCallback(encounterArgumentCaptor.capture(),anyMap());

        assertEquals("testEncounter",encounterArgumentCaptor.getValue().getEncounterName());
    }

    @Test
    public void writeTest(){
        FirebaseFirestore mockFirestore = Mockito.mock(FirebaseFirestore.class);
        CollectionReference mockCollectionReference = Mockito.mock(CollectionReference.class);
        when(mockFirestore.collection(eq("encounters"))).thenReturn(mockCollectionReference);
        DocumentReference mockDocument = Mockito.mock(DocumentReference.class);
        when(mockCollectionReference.document(eq("testEncounter"))).thenReturn(mockDocument);
        Task mockTask = Mockito.mock(Task.class);
        when(mockTask.isSuccessful()).thenReturn(true);
        when(mockDocument.set(any())).thenReturn(mockTask);

        EncounterRepository mockRepository = Mockito.mock(EncounterRepository.class);

        EncounterFirestoreDatabase testedDatabase = new EncounterFirestoreDatabase(mockFirestore);
        List<Card> encounterCards = new ArrayList<>();

        CharacterCard encounterCharacterCard1 = new CharacterCard("encounter", UUID.randomUUID().toString(),"Dark Zach","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.MELEE,1),3);
        encounterCards.add(encounterCharacterCard1);
        CharacterCard encounterCharacterCard2 = new CharacterCard("encounter", UUID.randomUUID().toString(),"Dark Zach 2","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.MELEE,1),15);
        encounterCards.add(encounterCharacterCard2);
        CharacterCard encounterCharacterCard3 = new CharacterCard("encounter", UUID.randomUUID().toString(),"Dark Ranged Zach","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.RANGED,1),5);
        encounterCards.add(encounterCharacterCard3);
        ItemCard testItem1 = new ItemCard("encounter", UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 4));
        encounterCards.add(testItem1);
        ItemCard testItem2 = new ItemCard("encounter", UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 4));
        encounterCards.add(testItem2);
        ItemCard testItem3 = new ItemCard("encounter", UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 4));
        encounterCards.add(testItem3);
        Encounter encounter = new Encounter("testEncounter", EncounterDifficultyLevel.EASY,encounterCards);
        testedDatabase.write(encounter,mockRepository,new HashMap<>());

        ArgumentCaptor<Encounter> encounterArgumentCaptor = ArgumentCaptor.forClass(Encounter.class);
        verify(mockDocument).set(encounterArgumentCaptor.capture());
        Encounter capturedEncounter = encounterArgumentCaptor.getValue();
        assertEquals(encounter.getEncounterName(), capturedEncounter.getEncounterName());
        assertEquals(encounter.getEncounterDifficultyLevel(), capturedEncounter.getEncounterDifficultyLevel());

        ArgumentCaptor<OnCompleteListener<Void>> onCompleteListenerArgumentCaptor = ArgumentCaptor.forClass(OnCompleteListener.class);
        verify(mockTask).addOnCompleteListener((OnCompleteListener<Void>) onCompleteListenerArgumentCaptor.capture());
        onCompleteListenerArgumentCaptor.getValue().onComplete(mockTask);

        ArgumentCaptor<Encounter> encounterArgumentCaptor2 = ArgumentCaptor.forClass(Encounter.class);
        verify(mockRepository).writeCallback(encounterArgumentCaptor2.capture(),anyMap());

        assertEquals("testEncounter",encounterArgumentCaptor2.getValue().getEncounterName());
    }


    private Map<String, Object> generateGoblinEncounter(){
        Map<String, Object> encounterMap = new HashMap<>();
        encounterMap.put(Encounter.ENCOUNTER_DIFFICULTY_LEVEL_FIELD, EncounterDifficultyLevel.EASY.toString());
        encounterMap.put(Encounter.ENCOUNTER_NAME_FIELD, "testEncounter");

        List<Card> encounterCards = new ArrayList<>();

        CharacterCard encounterCharacterCard1 = new CharacterCard("encounter", UUID.randomUUID().toString(),"Dark Zach","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.MELEE,1),3);
        encounterCards.add(encounterCharacterCard1);
        CharacterCard encounterCharacterCard2 = new CharacterCard("encounter", UUID.randomUUID().toString(),"Dark Zach 2","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.MELEE,1),15);
        encounterCards.add(encounterCharacterCard2);
        CharacterCard encounterCharacterCard3 = new CharacterCard("encounter", UUID.randomUUID().toString(),"Dark Ranged Zach","Dark Zach is a software engineer",new DamageAbility("Slap","slap", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.RANGED,1),5);
        encounterCards.add(encounterCharacterCard3);
        ItemCard testItem1 = new ItemCard("encounter", UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 4));
        encounterCards.add(testItem1);
        ItemCard testItem2 = new ItemCard("encounter", UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 4));
        encounterCards.add(testItem2);
        ItemCard testItem3 = new ItemCard("encounter", UUID.randomUUID().toString(), "test", "test", ItemType.EQUIPPABLE, new BuffAbility("test", "test", AbilityTarget.SINGLE_ALLY, BuffType.HEALTH, 4));
        encounterCards.add(testItem3);

        List<Map<String,Object>> cards = new ArrayList<>();
        for(Card card: encounterCards){
            cards.add(convertCardToMap(card));
        }
        encounterMap.put(Encounter.ENEMY_CARDS_FIELD, cards);

        return encounterMap;
    }

    private Map<String, Object> convertCardToMap(Card card){
        Map<String, Object> cardMap = new HashMap<>();
        cardMap.put(Card.USER_ID_FIELD,card.getUserId());
        cardMap.put(Card.CARD_UUID_FIELD, card.getCardUuid());
        cardMap.put(Card.CARD_DESCRIPTION_FIELD, card.getCardDescription());
        cardMap.put(Card.CARD_TYPE_FIELD, card.getCardType().toString());
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
        }
        return abilityMap;
    }
}
