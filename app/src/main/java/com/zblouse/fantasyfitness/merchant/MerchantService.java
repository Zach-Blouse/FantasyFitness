package com.zblouse.fantasyfitness.merchant;

import com.zblouse.fantasyfitness.R;
import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.combat.cards.AbilityTarget;
import com.zblouse.fantasyfitness.combat.cards.AttackType;
import com.zblouse.fantasyfitness.combat.cards.BuffAbility;
import com.zblouse.fantasyfitness.combat.cards.BuffType;
import com.zblouse.fantasyfitness.combat.cards.Card;
import com.zblouse.fantasyfitness.combat.cards.DamageAbility;
import com.zblouse.fantasyfitness.combat.cards.DamageType;
import com.zblouse.fantasyfitness.combat.cards.HealAbility;
import com.zblouse.fantasyfitness.combat.cards.ItemCard;
import com.zblouse.fantasyfitness.combat.cards.ItemType;
import com.zblouse.fantasyfitness.core.DomainService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MerchantService implements DomainService<Merchant> {

    private MainActivity mainActivity;
    private MerchantRepository merchantRepository;

    public MerchantService(MainActivity mainActivity){
        this.mainActivity = mainActivity;
        this.merchantRepository = new MerchantRepository(mainActivity);
    }

    public Merchant getMerchantByTag(String tag){
        return merchantRepository.getMerchantByTag(tag);
    }

    public void buyCard(String merchantTag, Card card){
        Merchant merchant = getMerchantByTag(merchantTag);
        int price = merchant.getCardPriceMap().get(card);
        mainActivity.getUserGameStateService().modifyUserGameCurrency(mainActivity.getCurrentUser().getUid(),(-1)*price,new HashMap<>());
        ItemCard userCard = new ItemCard(mainActivity.getCurrentUser().getUid(), UUID.randomUUID().toString(), card.getCardName(),card.getCardDescription(),((ItemCard)card).getItemType(), ((ItemCard)card).getAbility());
        mainActivity.getCardService().writeCard(userCard);
    }

    @Override
    public void repositoryResponse(Merchant responseBody, Map<String, Object> metadata) {

    }

    @Override
    public void interDomainServiceResponse(Object responseObject, Map<String, Object> metadata) {

    }

    public void initializeMerchants(){
        Map<Card, Integer> faolynBlacksmithCardMap = new HashMap<>();
        Card greatSwordCard = new ItemCard("merchantId", "merchantId", "Great Sword", "It's a pretty great sword.", ItemType.EQUIPPABLE, new DamageAbility("Great Sword Slash","Slash one enemy with your Great Sword", AbilityTarget.SINGLE_ENEMY, DamageType.NORMAL, AttackType.MELEE,6));
        faolynBlacksmithCardMap.put(greatSwordCard,150);
        Card plateMailCard = new ItemCard("merchantId", "merchantId", "Plate Mail","Sturdy Plate Mail that is sure to protect you in battle.",ItemType.EQUIPPABLE,new BuffAbility("Plate Mail","Increases Max HP of the character equipping this by 5",AbilityTarget.SINGLE_ALLY, BuffType.HEALTH,5));
        faolynBlacksmithCardMap.put(plateMailCard,250);

        Merchant faolynBlacksmith = new Merchant(mainActivity.getString(R.string.faolyn_blacksmith), faolynBlacksmithCardMap);
        merchantRepository.writeMerchant(faolynBlacksmith);

        Map<Card, Integer> faolynGeneralStoreCardmap = new HashMap<>();
        Card healthPotionCard = new ItemCard("merchantId", "merchantId", "Health Potion", "A standard health potion. It doesn't taste good, but will restore health.",ItemType.CONSUMABLE,new HealAbility("Health Potion","Drinking it restores 4 health",AbilityTarget.SINGLE_ALLY,4));
        faolynGeneralStoreCardmap.put(healthPotionCard,50);
        Card lightningBowCard = new ItemCard("merchantId", "merchantId", "Lightning Bow", "A well crafted bow imbued with magical electricity that makes each arrow fired with it deal electric damage.",ItemType.EQUIPPABLE,new DamageAbility("Lightning Bow","Bow that deals electric energy",AbilityTarget.SINGLE_ENEMY,DamageType.ELECTRIC,AttackType.RANGED,5));
        faolynGeneralStoreCardmap.put(lightningBowCard,1000);
        Merchant faolynGeneralStore = new Merchant(mainActivity.getString(R.string.faolyn_general_store), faolynGeneralStoreCardmap);
        merchantRepository.writeMerchant(faolynGeneralStore);

        Map<Card, Integer> bridgetonBlacksmithCardMap = new HashMap<>();
        Card axCard = new ItemCard("merchantId", "merchantId", "Axe", "A big axe.", ItemType.EQUIPPABLE, new DamageAbility("Axe Slash","Do minor damage to an entire row of enemies", AbilityTarget.ROW_ENEMY, DamageType.NORMAL, AttackType.MELEE,2));
        bridgetonBlacksmithCardMap.put(axCard,100);
        Card chainMailCard = new ItemCard("merchantId", "merchantId", "Chain Mail","Decent Chain Mail that may block a few hits.",ItemType.EQUIPPABLE,new BuffAbility("Chain Mail","Increases Max HP of the character equipping this by 3",AbilityTarget.SINGLE_ALLY, BuffType.HEALTH,3));
        bridgetonBlacksmithCardMap.put(chainMailCard,150);

        Merchant bridgetonBlacksmith = new Merchant(mainActivity.getString(R.string.bridgeton_blacksmith), bridgetonBlacksmithCardMap);
        merchantRepository.writeMerchant(bridgetonBlacksmith);

        Map<Card, Integer> bridgetonGeneralStoreCardmap = new HashMap<>();
        bridgetonGeneralStoreCardmap.put(healthPotionCard,50);
        Merchant bridgetonGeneralStore = new Merchant(mainActivity.getString(R.string.bridgeton_general_store), bridgetonGeneralStoreCardmap);
        merchantRepository.writeMerchant(bridgetonGeneralStore);

        Map<Card, Integer> thanadelGeneralStoreCardmap = new HashMap<>();
        thanadelGeneralStoreCardmap.put(healthPotionCard,50);
        Merchant thanadelGeneralStore = new Merchant(mainActivity.getString(R.string.thanadel_general_store), thanadelGeneralStoreCardmap);
        Card slingCard = new ItemCard("merchantId", "merchantId", "Sling", "A basic sling", ItemType.EQUIPPABLE, new DamageAbility("Sling Pebble","Sling a pebble at an enemy. Does 1 damage to an enemy", AbilityTarget.SINGLE_ENEMY, DamageType.NORMAL, AttackType.RANGED,1));
        thanadelGeneralStoreCardmap.put(slingCard,25);
        merchantRepository.writeMerchant(thanadelGeneralStore);
    }

}
