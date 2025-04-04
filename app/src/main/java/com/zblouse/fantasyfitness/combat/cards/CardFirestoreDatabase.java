package com.zblouse.fantasyfitness.combat.cards;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.zblouse.fantasyfitness.core.FirestoreDatabase;
import com.zblouse.fantasyfitness.core.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CardFirestoreDatabase extends FirestoreDatabase {

    private static final String TOP_COLLECTION = "userCards";
    private static final String USER_COLLECTION = "cards";
    private static final String CARD_TYPE_FIELD = "cardType";

    public CardFirestoreDatabase(){
        super();
    }

    public CardFirestoreDatabase(FirebaseFirestore firestore){
        super(firestore);
    }

    public void write(Card card, Repository<Card> cardRepository, Map<String, Object> metadata){

        firestore.collection(TOP_COLLECTION).document(card.getUserId()).collection(USER_COLLECTION).document(card.getCardUuid()).set(card).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    cardRepository.writeCallback(card, metadata);
                } else {
                    cardRepository.writeCallback(null, metadata);
                }
            }
        });
    }

    public void fetchListOfCards(String userId, List<String> cardUuids, CardRepository cardRepository, Map<String, Object> metadata){
        Log.e("CardFirestoreDatabase", "Getting card list");
        firestore.collection(TOP_COLLECTION).document(userId).collection(USER_COLLECTION).whereIn("cardUuid", cardUuids).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    List<Card> cardList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> documentData = document.getData();
                        CardType cardType = CardType.valueOf((String) documentData.get(CARD_TYPE_FIELD));
                        switch(cardType){
                            case ITEM:{
                                Card card = constructItemCardFromMap(documentData);
                                cardList.add(card);
                                break;
                            }
                            case EFFECT:{
                                Card card = constructEffectCardFromMap(documentData);
                                cardList.add(card);
                                break;
                            }
                            case CHARACTER:{
                                Card card = constructCharacterCardFromMap(documentData);
                                cardList.add(card);
                                break;
                            }
                            case PERMANENT:{
                                Card card = constructPermanentCardFromMap(documentData);
                                cardList.add(card);
                                break;
                            }
                        }

                    }
                    cardRepository.listReadCallback(cardList,metadata);
                } else {
                    //TODO handle firebase error states
                    Log.e("CardFirestoreDatabase", "Error reading from firebase");
                    cardRepository.readCallback(null, metadata);
                }
            }
        });
    }

    public void read(){

    }

    private ItemCard constructItemCardFromMap(Map<String,Object> data){
        String userId = (String)data.get(Card.USER_ID_FIELD);
        String cardUuid = (String)data.get(Card.CARD_UUID_FIELD);
        String cardName = (String)data.get(Card.CARD_NAME_FIELD);
        String cardDescription = (String)data.get(Card.CARD_DESCRIPTION_FIELD);
        Ability ability = constructAbilityFromMap((Map<String, Object>) data.get(ItemCard.ABILITY_FIELD));
        ItemType itemType = ItemType.valueOf((String)data.get(ItemCard.ITEM_TYPE_FIELD));
        return new ItemCard(userId, cardUuid, cardName, cardDescription,itemType,ability);
    }

    private EffectCard constructEffectCardFromMap(Map<String,Object> data){
        String userId = (String)data.get(Card.USER_ID_FIELD);
        String cardUuid = (String)data.get(Card.CARD_UUID_FIELD);
        String cardName = (String)data.get(Card.CARD_NAME_FIELD);
        String cardDescription = (String)data.get(Card.CARD_DESCRIPTION_FIELD);
        Ability ability = constructAbilityFromMap((Map<String, Object>) data.get(EffectCard.ABILITY_FIELD));
        return new EffectCard(userId, cardUuid, cardName, cardDescription,ability);
    }

    private PermanentCard constructPermanentCardFromMap(Map<String,Object> data){
        String userId = (String)data.get(Card.USER_ID_FIELD);
        String cardUuid = (String)data.get(Card.CARD_UUID_FIELD);
        String cardName = (String)data.get(Card.CARD_NAME_FIELD);
        String cardDescription = (String)data.get(Card.CARD_DESCRIPTION_FIELD);
        Ability ability = constructAbilityFromMap((Map<String, Object>) data.get(PermanentCard.ABILITY_FIELD));
        return new PermanentCard(userId, cardUuid, cardName, cardDescription,ability);
    }

    private CharacterCard constructCharacterCardFromMap(Map<String,Object> data){
        String userId = (String)data.get(Card.USER_ID_FIELD);
        String cardUuid = (String)data.get(Card.CARD_UUID_FIELD);
        String cardName = (String)data.get(Card.CARD_NAME_FIELD);
        String cardDescription = (String)data.get(Card.CARD_DESCRIPTION_FIELD);
        int maxHealth = ((Long)data.get(CharacterCard.MAX_HEALTH_FIELD)).intValue();
        int currentHealth = ((Long)data.get(CharacterCard.CURRENT_HEALTH_FIELD)).intValue();
        List<Ability> abilities = new ArrayList<>();
        for(Map<String,Object> abilityData: (List<Map<String,Object>>)data.get(CharacterCard.ABILITIES_FIELD)){
            abilities.add(constructAbilityFromMap(abilityData));
        }
        List<String> permanentCardsAffixedNames;
        if(data.containsKey(CharacterCard.PERMANENT_CARDS_AFFIXED_NAMES_FIELD)){
            permanentCardsAffixedNames = (ArrayList<String>)data.get(CharacterCard.PERMANENT_CARDS_AFFIXED_NAMES_FIELD);
        } else {
            permanentCardsAffixedNames = new ArrayList<>();
        }

        return new CharacterCard(userId, cardUuid, cardName, cardDescription,maxHealth,currentHealth,abilities,permanentCardsAffixedNames);
    }

    private Ability constructAbilityFromMap(Map<String,Object> abilityData){
        AbilityType abilityType = AbilityType.valueOf((String)abilityData.get(Ability.ABILITY_TYPE_FIELD));
        String abilityName = (String)abilityData.get(Ability.NAME_FIELD);
        String abilityDescription = (String)abilityData.get(Ability.DESCRIPTION_FIELD);
        AbilityTarget abilityTarget = AbilityTarget.valueOf((String)abilityData.get(Ability.ABILITY_TARGET_FIELD));
        switch (abilityType){
            case BUFF:{
                BuffType buffType = BuffType.valueOf((String)abilityData.get(BuffAbility.BUFF_TYPE_FIELD));
                int buffAmount = ((Long)abilityData.get(BuffAbility.BUFF_AMOUNT_FIELD)).intValue();
                return new BuffAbility(abilityName,abilityDescription,abilityTarget,buffType,buffAmount);
            }
            case DEBUFF:{
                BuffType buffType = BuffType.valueOf((String)abilityData.get(DebuffAbility.BUFF_TYPE_FIELD));
                int debuffAmount = ((Long)abilityData.get(DebuffAbility.DEBUFF_AMOUNT_FIELD)).intValue();
                return new DebuffAbility(abilityName,abilityDescription,abilityTarget,buffType,debuffAmount);
            }
            case DAMAGE:{
                DamageType damageType = DamageType.valueOf((String)abilityData.get(DamageAbility.DAMAGE_TYPE_FIELD));
                AttackType attackType = AttackType.valueOf((String)abilityData.get(DamageAbility.ATTACK_TYPE_FIELD));
                int damageAmount = ((Long)abilityData.get(DamageAbility.DAMAGE_AMOUNT_FIELD)).intValue();
                return new DamageAbility(abilityName,abilityDescription,abilityTarget,damageType,attackType, damageAmount);
            }
            case HEAL:{
                int healAmount = ((Long)abilityData.get(HealAbility.HEAL_AMOUNT_FIELD)).intValue();
                return new HealAbility(abilityName,abilityDescription,abilityTarget,healAmount);
            }
        }
        return null;
    }
}
