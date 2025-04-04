package com.zblouse.fantasyfitness.combat.cards;

import android.util.Log;

import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.core.DomainService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CardService implements DomainService<Card> {

    private MainActivity mainActivity;
    private CardRepository cardRepository;

    public CardService(MainActivity mainActivity){
        this.mainActivity = mainActivity;
        cardRepository = new CardRepository(this);
    }

    public void getCardList(List<String> cardUuids, Map<String, Object> metadata){
        Log.e("CardService", "Getting card list");
        cardRepository.fetchCardList(mainActivity.getCurrentUser().getUid(), cardUuids, metadata);
    }

    public void repositoryResponse(List<Card> cardList, Map<String, Object> metadata){
        Log.e("CardService", "card list repository response");
        if(metadata.containsKey(INTER_DOMAIN_SERVICE_ORIGIN_KEY)){
            metadata.put(INTER_DOMAIN_SERVICE_RESPONSE_CLASS_KEY,List.class);
            ((DomainService)metadata.get(INTER_DOMAIN_SERVICE_ORIGIN_KEY)).interDomainServiceResponse(cardList,metadata);
        } else {
            Log.e("CardService", "Metadata does not contain inter domain service origin");
        }
    }

    @Override
    public void repositoryResponse(Card responseBody, Map<String, Object> metadata) {

    }

    @Override
    public void interDomainServiceResponse(Object responseObject, Map<String, Object> metadata) {

    }

    public void initializeCards(){
        List<String> cardUuidList = new ArrayList<>();
        CharacterCard rynnCard = new CharacterCard(mainActivity.getCurrentUser().getUid(), UUID.randomUUID().toString(),"Rynn", "Rynn is a young sorcerer.",new HealAbility("Minor Heal","Rynn sends a small amount of divine energy to an ally.", AbilityTarget.SINGLE_ALLY,3),10);
        cardRepository.writeCard(rynnCard,new HashMap<>());
        cardUuidList.add(rynnCard.getCardUuid());
        CharacterCard varisCard = new CharacterCard(mainActivity.getCurrentUser().getUid(), UUID.randomUUID().toString(),"Varis", "Varis is a vampire.",new HealAbility("Bite","Varis sucks some blood from his blood bag, healing himself 2hp", AbilityTarget.SELF, 2),16);
        cardRepository.writeCard(varisCard,new HashMap<>());
        cardUuidList.add(varisCard.getCardUuid());
        CharacterCard tsugumiCard = new CharacterCard(mainActivity.getCurrentUser().getUid(), UUID.randomUUID().toString(),"Tsugumi", "Tsugumi is a coffee loving adventurer.",new DamageAbility("Kick","Tsugumi kicks the target", AbilityTarget.SINGLE_ENEMY, DamageType.NORMAL, AttackType.MELEE,2),16);
        cardRepository.writeCard(tsugumiCard,new HashMap<>());
        cardUuidList.add(tsugumiCard.getCardUuid());
        CharacterCard jellalCard = new CharacterCard(mainActivity.getCurrentUser().getUid(), UUID.randomUUID().toString(),"Je'llal", "Je'llal is a wizard magically transported from the distant past.",new DamageAbility("Gravity Bomb","Je'llal uses ancient magic to send a ball of gravity energy towards the enemy, damaging all enemies in the same row as the target.", AbilityTarget.ROW_ENEMY,DamageType.NORMAL, AttackType.RANGED, 3),10);
        cardRepository.writeCard(jellalCard,new HashMap<>());
        cardUuidList.add(jellalCard.getCardUuid());
        CharacterCard raleCard = new CharacterCard(mainActivity.getCurrentUser().getUid(), UUID.randomUUID().toString(),"Rale", "Rale is a green knight.",new DamageAbility("Smite","Rale uses divine magic to add some oomph to his attack.",AbilityTarget.SINGLE_ENEMY, DamageType.NORMAL, AttackType.MELEE, 3),20);
        cardRepository.writeCard(raleCard,new HashMap<>());
        cardUuidList.add(raleCard.getCardUuid());
        ItemCard healingPotion1 = new ItemCard(mainActivity.getCurrentUser().getUid(), UUID.randomUUID().toString(),"Basic Healing Potion", "Heals a character by 5hp.", ItemType.CONSUMABLE, new HealAbility("Healing Potion","heals",AbilityTarget.SINGLE_ALLY, 5));
        cardRepository.writeCard(healingPotion1, new HashMap<>());
        cardUuidList.add(healingPotion1.getCardUuid());
        ItemCard healingPotion2 = new ItemCard(mainActivity.getCurrentUser().getUid(), UUID.randomUUID().toString(),"Basic Healing Potion", "Heals a character by 5hp.",ItemType.CONSUMABLE, new HealAbility("Healing Potion","heals",AbilityTarget.SINGLE_ALLY, 5));
        cardRepository.writeCard(healingPotion2, new HashMap<>());
        cardUuidList.add(healingPotion2.getCardUuid());
        ItemCard swordCard1 = new ItemCard(mainActivity.getCurrentUser().getUid(),UUID.randomUUID().toString(),"Sword", "Don't hold it by the sharp end.",ItemType.EQUIPPABLE,new DamageAbility("Slash","Slash the enemy with the sword.",AbilityTarget.SINGLE_ENEMY, DamageType.NORMAL,AttackType.MELEE,5));
        cardRepository.writeCard(swordCard1, new HashMap<>());
        cardUuidList.add(swordCard1.getCardUuid());
        ItemCard swordCard2 = new ItemCard(mainActivity.getCurrentUser().getUid(),UUID.randomUUID().toString(),"Sword", "Don't hold it by the sharp end.",ItemType.EQUIPPABLE,new DamageAbility("Slash","Slash the enemy with the sword.",AbilityTarget.SINGLE_ENEMY, DamageType.NORMAL,AttackType.MELEE,5));
        cardRepository.writeCard(swordCard2, new HashMap<>());
        cardUuidList.add(swordCard2.getCardUuid());
        ItemCard helmetCard1 = new ItemCard(mainActivity.getCurrentUser().getUid(),UUID.randomUUID().toString(),"Helmet", "Makes it harder to hit your head.",ItemType.EQUIPPABLE,new BuffAbility("Helmet","Helmet protects your head.",AbilityTarget.SINGLE_ALLY,BuffType.HEALTH,5));
        cardRepository.writeCard(helmetCard1, new HashMap<>());
        cardUuidList.add(helmetCard1.getCardUuid());
        ItemCard helmetCard2 = new ItemCard(mainActivity.getCurrentUser().getUid(),UUID.randomUUID().toString(),"Helmet", "Makes it harder to hit your head.",ItemType.EQUIPPABLE,new BuffAbility("Helmet","Helmet protects your head.",AbilityTarget.SINGLE_ALLY,BuffType.HEALTH,5));
        cardRepository.writeCard(helmetCard2, new HashMap<>());
        cardUuidList.add(helmetCard2.getCardUuid());
        ItemCard healingMist1 = new ItemCard(mainActivity.getCurrentUser().getUid(), UUID.randomUUID().toString(),"Healing Mist", "Sprayable Healing potion that heals all characters 3hp.",ItemType.CONSUMABLE, new HealAbility("Healing Mist","heals", AbilityTarget.ALL_ALLY, 3));
        cardRepository.writeCard(healingMist1, new HashMap<>());
        cardUuidList.add(healingMist1.getCardUuid());
        ItemCard healingMist2 = new ItemCard(mainActivity.getCurrentUser().getUid(), UUID.randomUUID().toString(),"Healing Mist", "Sprayable Healing potion that heals all characters 3hp.",ItemType.CONSUMABLE, new HealAbility("Healing Mist","heals", AbilityTarget.ALL_ALLY, 3));
        cardRepository.writeCard(healingMist2, new HashMap<>());
        cardUuidList.add(healingMist2.getCardUuid());
        ItemCard bowCard1 = new ItemCard(mainActivity.getCurrentUser().getUid(),UUID.randomUUID().toString(),"Bow", "A well crafted, if simple bow.",ItemType.EQUIPPABLE,new DamageAbility("Arrow","Shoot an arrow at the enemy.",AbilityTarget.SINGLE_ENEMY, DamageType.NORMAL,AttackType.RANGED,3));
        cardRepository.writeCard(bowCard1, new HashMap<>());
        cardUuidList.add(bowCard1.getCardUuid());
        ItemCard bowCard2 = new ItemCard(mainActivity.getCurrentUser().getUid(),UUID.randomUUID().toString(),"Bow", "A well crafted, if simple bow.",ItemType.EQUIPPABLE,new DamageAbility("Arrow","Shoot an arrow at the enemy.",AbilityTarget.SINGLE_ENEMY, DamageType.NORMAL,AttackType.RANGED,3));
        cardRepository.writeCard(bowCard2, new HashMap<>());
        cardUuidList.add(bowCard2.getCardUuid());
        ItemCard monocle = new ItemCard(mainActivity.getCurrentUser().getUid(),UUID.randomUUID().toString(),"Monocle", "A fancy monocle. The character equipped with this item will have an easier time seeing the enemy and do an extra 3 damage on any attack",ItemType.EQUIPPABLE,new BuffAbility("Monocle","Monocle",AbilityTarget.SINGLE_ALLY, BuffType.ATTACK, 3));
        cardRepository.writeCard(monocle, new HashMap<>());
        cardUuidList.add(monocle.getCardUuid());

        mainActivity.getDeckService().writeNewDeck("userDeck",cardUuidList);
    }
}
