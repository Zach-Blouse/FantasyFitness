package com.zblouse.fantasyfitness.combat;

import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.core.DomainService;

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
        cardRepository.fetchCardList(cardUuids, metadata);
    }

    @Override
    public void repositoryResponse(Card responseBody, Map<String, Object> metadata) {

    }

    @Override
    public void interDomainServiceResponse(Object responseObject, Map<String, Object> metadata) {

    }

    public void initializeCards(){
        CharacterCard rynnCard = new CharacterCard(mainActivity.getCurrentUser().getUid(), UUID.randomUUID().toString(),"Rynn", "Rynn is a young sorcerer.",new DamageAbility("Firebolt","Rynn shoots a small ball of fire towards the enemy.", AbilityTarget.SINGLE_ENEMY, DamageType.FIRE, AttackType.RANGED,4),10);
        cardRepository.writeCard(rynnCard,new HashMap<>());
        CharacterCard varisCard = new CharacterCard(mainActivity.getCurrentUser().getUid(), UUID.randomUUID().toString(),"Varis", "Varis is a vampire.",new HealAbility("Bite","Varis sucks some blood from his blood bag, healing himself 2hp", AbilityTarget.SELF, 2),16);
        cardRepository.writeCard(varisCard,new HashMap<>());
        CharacterCard tsugumiCard = new CharacterCard(mainActivity.getCurrentUser().getUid(), UUID.randomUUID().toString(),"Tsugumi", "Tsugumi is a coffee loving adventurer.",new DamageAbility("Kick","Tsugumi kicks the target", AbilityTarget.SINGLE_ENEMY,DamageType.NORMAL, AttackType.MELEE,2),16);
        cardRepository.writeCard(tsugumiCard,new HashMap<>());
        CharacterCard jellalCard = new CharacterCard(mainActivity.getCurrentUser().getUid(), UUID.randomUUID().toString(),"Je'llal", "Je'llal is a wizard magically transported from the distant past.",new DamageAbility("Gravity Bomb","Je'llal uses ancient magic to send a ball of gravity energy towards the enemy, damaging all enemies in the same row as the target.", AbilityTarget.ROW_ENEMY,DamageType.NORMAL, AttackType.RANGED, 3),10);
        cardRepository.writeCard(jellalCard,new HashMap<>());
        CharacterCard raleCard = new CharacterCard(mainActivity.getCurrentUser().getUid(), UUID.randomUUID().toString(),"Rale", "Rale is a green knight.",new DamageAbility("Smite","Rale uses divine magic to add some oomph to his attack.",AbilityTarget.SINGLE_ENEMY, DamageType.NORMAL, AttackType.MELEE, 3),20);
        cardRepository.writeCard(raleCard,new HashMap<>());
        ItemCard healingPotion1 = new ItemCard(mainActivity.getCurrentUser().getUid(), UUID.randomUUID().toString(),"Basic Healing Potion", "Heals a character by 5hp.",ItemType.CONSUMABLE, new HealAbility("Healing Potion","heals",AbilityTarget.SINGLE_ALLY, 5));
        cardRepository.writeCard(healingPotion1, new HashMap<>());
        ItemCard healingPotion2 = new ItemCard(mainActivity.getCurrentUser().getUid(), UUID.randomUUID().toString(),"Basic Healing Potion", "Heals a character by 5hp.",ItemType.CONSUMABLE, new HealAbility("Healing Potion","heals",AbilityTarget.SINGLE_ALLY, 5));
        cardRepository.writeCard(healingPotion2, new HashMap<>());
        ItemCard swordCard1 = new ItemCard(mainActivity.getCurrentUser().getUid(),UUID.randomUUID().toString(),"Sword", "Don't hold it by the sharp end.",ItemType.EQUIPPABLE,new DamageAbility("Slash","Slash the enemy with the sword.",AbilityTarget.SINGLE_ENEMY, DamageType.NORMAL,AttackType.MELEE,5));
        cardRepository.writeCard(swordCard1, new HashMap<>());
        ItemCard swordCard2 = new ItemCard(mainActivity.getCurrentUser().getUid(),UUID.randomUUID().toString(),"Sword", "Don't hold it by the sharp end.",ItemType.EQUIPPABLE,new DamageAbility("Slash","Slash the enemy with the sword.",AbilityTarget.SINGLE_ENEMY, DamageType.NORMAL,AttackType.MELEE,5));
        cardRepository.writeCard(swordCard2, new HashMap<>());
        ItemCard helmetCard1 = new ItemCard(mainActivity.getCurrentUser().getUid(),UUID.randomUUID().toString(),"Helmet", "Makes it harder to hit your head.",ItemType.EQUIPPABLE,new BuffAbility("Helmet","Helmet protects your head.",AbilityTarget.SINGLE_ALLY,BuffType.HEALTH,5));
        cardRepository.writeCard(helmetCard1, new HashMap<>());
        ItemCard helmetCard2 = new ItemCard(mainActivity.getCurrentUser().getUid(),UUID.randomUUID().toString(),"Helmet", "Makes it harder to hit your head.",ItemType.EQUIPPABLE,new BuffAbility("Helmet","Helmet protects your head.",AbilityTarget.SINGLE_ALLY,BuffType.HEALTH,5));
        cardRepository.writeCard(helmetCard2, new HashMap<>());
        ItemCard healingMist1 = new ItemCard(mainActivity.getCurrentUser().getUid(), UUID.randomUUID().toString(),"Healing Mist", "Sprayable Healing potion that heals all characters 3hp.",ItemType.CONSUMABLE, new HealAbility("Healing Mist","heals", AbilityTarget.ALL_ALLY, 3));
        cardRepository.writeCard(healingMist1, new HashMap<>());
        ItemCard healingMist2 = new ItemCard(mainActivity.getCurrentUser().getUid(), UUID.randomUUID().toString(),"Healing Mist", "Sprayable Healing potion that heals all characters 3hp.",ItemType.CONSUMABLE, new HealAbility("Healing Mist","heals", AbilityTarget.ALL_ALLY, 3));
        cardRepository.writeCard(healingMist2, new HashMap<>());
        ItemCard bowCard1 = new ItemCard(mainActivity.getCurrentUser().getUid(),UUID.randomUUID().toString(),"Bow", "A well crafted, if simple bow.",ItemType.EQUIPPABLE,new DamageAbility("Arrow","Shoot an arrow at the enemy.",AbilityTarget.SINGLE_ENEMY, DamageType.NORMAL,AttackType.RANGED,3));
        cardRepository.writeCard(bowCard1, new HashMap<>());
        ItemCard bowCard2 = new ItemCard(mainActivity.getCurrentUser().getUid(),UUID.randomUUID().toString(),"Bow", "A well crafted, if simple bow.",ItemType.EQUIPPABLE,new DamageAbility("Arrow","Shoot an arrow at the enemy.",AbilityTarget.SINGLE_ENEMY, DamageType.NORMAL,AttackType.RANGED,3));
        cardRepository.writeCard(bowCard2, new HashMap<>());
        ItemCard monocle = new ItemCard(mainActivity.getCurrentUser().getUid(),UUID.randomUUID().toString(),"Monocle", "A fancy monocle. The character equipped with this item will have an easier time seeing the enemy and do an extra 3 damage on any attack",ItemType.EQUIPPABLE,new BuffAbility("Monocle","Monocle",AbilityTarget.SINGLE_ALLY, BuffType.ATTACK, 3));
        cardRepository.writeCard(monocle, new HashMap<>());
    }
}
