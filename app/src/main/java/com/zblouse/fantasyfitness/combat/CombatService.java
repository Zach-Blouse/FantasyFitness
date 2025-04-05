package com.zblouse.fantasyfitness.combat;

import android.util.Log;

import com.zblouse.fantasyfitness.activity.MainActivity;
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
import com.zblouse.fantasyfitness.combat.cards.Deck;
import com.zblouse.fantasyfitness.combat.cards.EffectCard;
import com.zblouse.fantasyfitness.combat.cards.HealAbility;
import com.zblouse.fantasyfitness.combat.cards.ItemCard;
import com.zblouse.fantasyfitness.combat.encounter.Encounter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class CombatService {

    private MainActivity mainActivity;

    private CombatStateModel combatStateModel;
    private boolean playerTurn = true;
    private boolean inSetup;

    public CombatService(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    public void initializeCombat(){
        Log.e("CombatService", "Initializing Combat");
        mainActivity.getDeckService().fetchDeck(mainActivity.getCurrentUser().getUid(),"userDeck");
        combatStateModel = new CombatStateModel();
        inSetup = true;
    }

    public boolean isInSetup(){
        return inSetup;
    }

    public void cardDroppedOnLine(CombatCardModel combatCardModel, CombatLine combatLine){
        if(combatCardModel.getCardType().equals(CardType.CHARACTER)){
            combatCardModel.setPlayed(true);
            if(combatLine.equals(CombatLine.PLAYER_BACK_LINE)){
                combatStateModel.addCardToPlayerBackLine(combatCardModel);
            } else if(combatLine.equals(CombatLine.PLAYER_FRONT_LINE)){
                combatStateModel.addCardToPlayerFrontLine(combatCardModel);
            }
        } else if(combatCardModel.getAbility().getAbilityTarget().equals(AbilityTarget.ROW_ALLY)){
            switch(combatLine) {
                case PLAYER_BACK_LINE: {
                    switch (combatCardModel.getAbility().getAbilityType()) {
                        case BUFF:
                        case DEBUFF: {
                            for(CombatCardModel cardModel: combatStateModel.getPlayerBackLine()){
                                cardModel.addAbility(combatCardModel.getAbility());
                            }
                            break;
                        }
                        case HEAL:{
                            for(CombatCardModel cardModel: combatStateModel.getPlayerBackLine()){
                                HealAbility healAbility = (HealAbility) combatCardModel.getAbility();
                                int currentHealth = cardModel.getCurrentHealth();
                                int newHealth = currentHealth + healAbility.getHealAmount();
                                if (newHealth <= cardModel.getMaxHealth()) {
                                    cardModel.setCurrentHealth(newHealth);
                                } else {
                                    cardModel.setCurrentHealth(cardModel.getMaxHealth());
                                }
                            }
                            break;
                        }
                    }
                    break;
                }
                case PLAYER_FRONT_LINE: {
                    switch (combatCardModel.getAbility().getAbilityType()) {
                        case BUFF:
                        case DEBUFF: {
                            for(CombatCardModel cardModel: combatStateModel.getPlayerFrontLine()){
                                cardModel.addAbility(combatCardModel.getAbility());
                            }
                            break;
                        }
                        case HEAL:{
                            for(CombatCardModel cardModel: combatStateModel.getPlayerFrontLine()){
                                HealAbility healAbility = (HealAbility) combatCardModel.getAbility();
                                int currentHealth = cardModel.getCurrentHealth();
                                int newHealth = currentHealth + healAbility.getHealAmount();
                                if (newHealth <= cardModel.getMaxHealth()) {
                                    cardModel.setCurrentHealth(newHealth);
                                } else {
                                    cardModel.setCurrentHealth(cardModel.getMaxHealth());
                                }
                            }
                            break;
                        }
                    }
                    break;
                }
            }
        } else if(combatCardModel.getAbility().getAbilityTarget().equals(AbilityTarget.ALL_ALLY)){

        }
        combatStateModel.getPlayerHand().remove(combatCardModel);
        mainActivity.publishEvent(new CombatStateUpdateEvent(combatStateModel,new HashMap<>()));
    }

    public void cardDroppedOnCard(CombatCardModel droppedCard, CombatCardModel targetCard){
        if(droppedCard.getCardType().equals(CardType.ITEM) && targetCard.isPlayed() && targetCard.getCardType().equals(CardType.CHARACTER) ){
            //TODO check whether an item is equiped or consumed. Currently ok beacuse heals are only consumed, but should check in the future for like a Healer's Kit item or whatever
            switch (droppedCard.getAbility().getAbilityType()){
                case BUFF:{
                    BuffAbility buffAbility = (BuffAbility) droppedCard.getAbility();
                    if(buffAbility.getBuffType().equals(BuffType.HEALTH)){
                        targetCard.setMaxHealth(targetCard.getMaxHealth()+buffAbility.getBuffAmount());
                        targetCard.setCurrentHealth(targetCard.getCurrentHealth() + buffAbility.getBuffAmount());
                    } else {
                        targetCard.addAbility(droppedCard.getAbility());
                    }
                    break;
                }
                case DAMAGE: {
                    targetCard.addAbility(droppedCard.getAbility());
                    break;
                }
                case HEAL:{
                    if(droppedCard.getAbility().getAbilityTarget().equals(AbilityTarget.SINGLE_ALLY)) {
                        HealAbility healAbility = (HealAbility) droppedCard.getAbility();
                        int currentHealth = targetCard.getCurrentHealth();
                        int newHealth = currentHealth + healAbility.getHealAmount();
                        if (newHealth <= targetCard.getMaxHealth()) {
                            targetCard.setCurrentHealth(newHealth);
                        } else {
                            targetCard.setCurrentHealth(targetCard.getMaxHealth());
                        }
                    }
                    break;
                }
            }
            combatStateModel.getPlayerHand().remove(droppedCard);
            mainActivity.publishEvent(new CombatStateUpdateEvent(combatStateModel,new HashMap<>()));
        }
    }

    public void encounterFetchReturned(Encounter encounter){


        List<CombatCardModel> enemyDeckCards = new ArrayList<>();
        for(Card card: encounter.getEnemyCards()){
            CombatCardModel combatCardModel = new CombatCardModel(card.getCardName(), card.getCardDescription(), card.getCardType(), false,false);
            if(card.getCardType().equals(CardType.CHARACTER)){
                CharacterCard characterCard = (CharacterCard)card;
                combatCardModel.setAbilities(characterCard.getAbilities());
                combatCardModel.setMaxHealth(characterCard.getMaxHealth());
                combatCardModel.setCurrentHealth(characterCard.getMaxHealth());
            } else if(card.getCardType().equals(CardType.ITEM)){
                ItemCard itemCard = (ItemCard)card;
                combatCardModel.setAbility(itemCard.getAbility());
            } else if(card.getCardType().equals(CardType.EFFECT)){
                EffectCard effectCard = (EffectCard) card;
                combatCardModel.setAbility(effectCard.getAbility());
            }
            enemyDeckCards.add(combatCardModel);
        }
        Collections.shuffle(enemyDeckCards);

        List<CombatCardModel> initialEnemyHand = new ArrayList<>();
        CombatCardModel initialCharacterCard = null;
        for(CombatCardModel card: enemyDeckCards){
            if(card.getCardType().equals(CardType.CHARACTER)){
                initialCharacterCard = card;
                break;
            }
        }
        enemyDeckCards.remove(initialCharacterCard);
        initialEnemyHand.add(initialCharacterCard);
        for(int i =0; i<=4; i++) {
            initialEnemyHand.add(enemyDeckCards.remove(0));
        }

        CombatDeckModel enemyDeckModel = new CombatDeckModel(enemyDeckCards);
        combatStateModel.setEnemyDeck(enemyDeckModel);
        combatStateModel.initialEnemyHand(initialEnemyHand);
        if(combatStateModel.fullyInitialized()) {
            enemyTurn(true);
            mainActivity.publishEvent(new CombatStateUpdateEvent(combatStateModel, new HashMap<>()));
        }
    }

    public void deckFetchReturned(Deck userDeck){

        List<CombatCardModel> userDeckCards = new ArrayList<>();
        for(Card card: userDeck.getCards()){
            CombatCardModel combatCardModel = new CombatCardModel(card.getCardName(), card.getCardDescription(), card.getCardType(), true,false);
            if(card.getCardType().equals(CardType.CHARACTER)){
                CharacterCard characterCard = (CharacterCard)card;
                combatCardModel.setAbilities(characterCard.getAbilities());
                combatCardModel.setMaxHealth(characterCard.getMaxHealth());
                combatCardModel.setCurrentHealth(characterCard.getMaxHealth());
            } else if(card.getCardType().equals(CardType.ITEM)){
                ItemCard itemCard = (ItemCard)card;
                combatCardModel.setAbility(itemCard.getAbility());
            } else if(card.getCardType().equals(CardType.EFFECT)){
                EffectCard effectCard = (EffectCard) card;
                combatCardModel.setAbility(effectCard.getAbility());
            }
            userDeckCards.add(combatCardModel);
        }
        Collections.shuffle(userDeckCards);

        List<CombatCardModel> initialUserHand = new ArrayList<>();
        CombatCardModel initialCharacterCard = null;
        for(CombatCardModel card: userDeckCards){
            if(card.getCardType().equals(CardType.CHARACTER)){
                initialCharacterCard = card;
                break;
            }
        }
        userDeckCards.remove(initialCharacterCard);
        initialUserHand.add(initialCharacterCard);
        for(int i =0; i<=4; i++) {
            initialUserHand.add(userDeckCards.remove(0));
        }

        CombatDeckModel userDeckModel = new CombatDeckModel(userDeckCards);
        combatStateModel.setUserDeck(userDeckModel);
        combatStateModel.initialPlayerHand(initialUserHand);

        if(combatStateModel.fullyInitialized()) {
            enemyTurn(true);
            mainActivity.publishEvent(new CombatStateUpdateEvent(combatStateModel, new HashMap<>()));
        }
    }

    public boolean isPlayerTurn(){
        return this.playerTurn;
    }

    public void endPlayerTurn(){
        this.playerTurn = false;
        enemyTurn(false);
    }

    private void enemyTurn(boolean initialSetup){
        List<CombatCardModel> characterCardsInHand = new ArrayList<>();
        List<CombatCardModel> rangedItemCardsInHand = new ArrayList<>();
        List<CombatCardModel> healthBuffItemCardsInHand = new ArrayList<>();
        List<CombatCardModel> damageBuffItemCardsInHand = new ArrayList<>();
        List<CombatCardModel> healItemCardsInHand = new ArrayList<>();
        List<CombatCardModel> meleeItemCardsInHand = new ArrayList<>();

        for(CombatCardModel card: combatStateModel.getEnemyHand()){
            if(card.getCardType().equals(CardType.CHARACTER)){
                characterCardsInHand.add(card);
            } else if(card.getCardType().equals(CardType.ITEM)){

                if(card.getAbility().getAbilityType().equals(AbilityType.DAMAGE)){
                    if(((DamageAbility)card.getAbility()).getAttackType().equals(AttackType.MELEE)){
                        meleeItemCardsInHand.add(card);
                    } else  if(((DamageAbility)card.getAbility()).getAttackType().equals(AttackType.RANGED)){
                        rangedItemCardsInHand.add(card);
                    }
                } else if(card.getAbility().getAbilityType().equals(AbilityType.BUFF)){
                    if(((BuffAbility)card.getAbility()).getBuffType().equals(BuffType.ATTACK)){
                        damageBuffItemCardsInHand.add(card);
                    } else if(((BuffAbility)card.getAbility()).getBuffType().equals(BuffType.HEALTH)){
                        healthBuffItemCardsInHand.add(card);
                    }
                } else if(card.getAbility().getAbilityType().equals(AbilityType.HEAL)){
                    healItemCardsInHand.add(card);
                }
            }
        }

        for(CombatCardModel characterCard: characterCardsInHand){
            for(Ability ability:characterCard.getAbilities()){
                if(ability.getAbilityType().equals(AbilityType.DAMAGE)){
                    if(((DamageAbility)ability).getAttackType().equals(AttackType.MELEE)){
                        playEnemyCardInLine(characterCard,CombatLine.ENEMY_FRONT_LINE);
                    }
                }
            }
        }

        characterCardsInHand.clear();

        for(CombatCardModel characterCard: characterCardsInHand){
            for(Ability ability:characterCard.getAbilities()){
                if(ability.getAbilityType().equals(AbilityType.HEAL)){
                    playEnemyCardInLine(characterCard,CombatLine.ENEMY_BACK_LINE);
                }
            }
            if(!characterCard.isPlayed()){
                if(!meleeItemCardsInHand.isEmpty()){
                    playEnemyCardInLine(characterCard,CombatLine.ENEMY_FRONT_LINE);
                    playEnemyCardOnEnemyCard(meleeItemCardsInHand.remove(0),characterCard);
                } else {
                    playEnemyCardInLine(characterCard,CombatLine.ENEMY_BACK_LINE);
                }
            }
        }

        for(CombatCardModel meleeItemCard: meleeItemCardsInHand) {
            CombatCardModel lowestAttackFrontLine = null;
            for (CombatCardModel frontLineCard : combatStateModel.getEnemyFrontLine()) {
                if (lowestAttackFrontLine == null) {
                    lowestAttackFrontLine = frontLineCard;
                } else {
                    DamageAbility highestMeleeCurrentLowestAbility = getHighestMeleeAttack(lowestAttackFrontLine);
                    DamageAbility highestMeleeNewCardAbility = getHighestMeleeAttack(frontLineCard);
                    if (highestMeleeCurrentLowestAbility != null) {
                        if (highestMeleeNewCardAbility != null) {
                            if (highestMeleeNewCardAbility.getDamageAmount() < highestMeleeCurrentLowestAbility.getDamageAmount()) {
                                lowestAttackFrontLine = frontLineCard;
                            }
                        } else {
                            lowestAttackFrontLine = frontLineCard;
                        }
                    }
                }
            }
            if(lowestAttackFrontLine != null){
                DamageAbility damageAbility = getHighestMeleeAttack(lowestAttackFrontLine);
                if(damageAbility == null){
                    playEnemyCardOnEnemyCard(meleeItemCard, lowestAttackFrontLine);
                }else if(((DamageAbility)meleeItemCard.getAbility()).getDamageAmount() > damageAbility.getDamageAmount()){
                    playEnemyCardOnEnemyCard(meleeItemCard, lowestAttackFrontLine);
                }
            }

        }

        for(CombatCardModel rangedItemCard: rangedItemCardsInHand) {
            CombatCardModel lowestAttackBackLine = null;
            for (CombatCardModel backLineCard : combatStateModel.getEnemyBackLine()) {
                if (lowestAttackBackLine == null) {
                    lowestAttackBackLine = backLineCard;
                } else {
                    DamageAbility highestRangedCurrentLowestAbility = getHighestRangedAttack(lowestAttackBackLine);
                    DamageAbility highestRangedNewCardAbility = getHighestRangedAttack(backLineCard);
                    if (highestRangedCurrentLowestAbility != null) {
                        if (highestRangedNewCardAbility != null) {
                            if (highestRangedNewCardAbility.getDamageAmount() < highestRangedCurrentLowestAbility.getDamageAmount()) {
                                lowestAttackBackLine = backLineCard;
                            }
                        } else {
                            lowestAttackBackLine = backLineCard;
                        }
                    }
                }
            }
            if(lowestAttackBackLine != null){
                DamageAbility damageAbility = getHighestRangedAttack(lowestAttackBackLine);
                if(damageAbility == null){
                    playEnemyCardOnEnemyCard(rangedItemCard, lowestAttackBackLine);
                }else if(((DamageAbility)rangedItemCard.getAbility()).getDamageAmount() > damageAbility.getDamageAmount()){
                    playEnemyCardOnEnemyCard(rangedItemCard, lowestAttackBackLine);
                }
            }
        }

        List<CombatCardModel> allEnemyCards = new ArrayList<>();
        allEnemyCards.addAll(combatStateModel.getEnemyBackLine());
        allEnemyCards.addAll(combatStateModel.getEnemyFrontLine());

        for(CombatCardModel healthItem: healItemCardsInHand){
            CombatCardModel mostHurt = null;
            for(CombatCardModel playedCard: allEnemyCards){
                if(playedCard.getCurrentHealth()<playedCard.getMaxHealth()){
                    if(mostHurt == null){
                        mostHurt = playedCard;
                    } else {
                        if(playedCard.getCurrentHealth() < mostHurt.getCurrentHealth()){
                            mostHurt = playedCard;
                        }
                    }
                }
            }
            if(mostHurt != null){
                //TODO consider adding logic for not healing if only a few hit points below max, not MVP though
                playEnemyCardOnEnemyCard(healthItem,mostHurt);
            }
        }

        for(CombatCardModel healthBuffItem: healthBuffItemCardsInHand){
            CombatCardModel lowestHp = null;
            for(CombatCardModel playedCard: allEnemyCards){
                if(lowestHp == null){
                    lowestHp = playedCard;
                } else {
                    if(playedCard.getCurrentHealth() < lowestHp.getCurrentHealth()){
                        lowestHp = playedCard;
                    }
                }
            }
            if(lowestHp != null){
                //TODO consider adding logic for not healing if only a few hit points below max, not MVP though
                playEnemyCardOnEnemyCard(healthBuffItem,lowestHp);
            }
        }

        for(CombatCardModel damageBuffItem : damageBuffItemCardsInHand){
            CombatCardModel lowestAttackWithAttack = null;
            DamageAbility lowestAttackAbility = null;
            for(CombatCardModel playedCard: allEnemyCards){
                DamageAbility playedCardsHighestAbility = getHighestAttack(playedCard);
                if(playedCardsHighestAbility != null){
                    if(lowestAttackWithAttack == null){
                        lowestAttackWithAttack = playedCard;
                        lowestAttackAbility = playedCardsHighestAbility;
                    } else if(playedCardsHighestAbility.getDamageAmount() < lowestAttackAbility.getDamageAmount()){
                        lowestAttackWithAttack = playedCard;
                        lowestAttackAbility = playedCardsHighestAbility;
                    }
                }
            }
            if(lowestAttackWithAttack != null){
                playEnemyCardOnEnemyCard(damageBuffItem,lowestAttackWithAttack);
            }
        }

        if(!initialSetup){
            //TODO implement targeting player's cards
        }
    }

    private void playEnemyCardInLine(CombatCardModel combatCardModel, CombatLine combatLine) {
        combatStateModel.getEnemyHand().remove(combatCardModel);
        combatCardModel.setPlayed(true);
        if (combatCardModel.getCardType().equals(CardType.CHARACTER)) {
            if (combatLine.equals(CombatLine.ENEMY_BACK_LINE)) {
                combatStateModel.addCardToEnemyBackLine(combatCardModel);
            } else if (combatLine.equals(CombatLine.ENEMY_FRONT_LINE)) {
                combatStateModel.addCardToEnemyFrontLine(combatCardModel);
            }
        } else if (combatCardModel.getCardType().equals(CardType.ITEM)) {
            if (combatCardModel.getAbility().getAbilityTarget().equals(AbilityTarget.ROW_ALLY)) {
                switch (combatLine) {
                    case ENEMY_BACK_LINE: {
                        switch (combatCardModel.getAbility().getAbilityType()) {
                            case BUFF:
                            case DEBUFF: {
                                for (CombatCardModel cardModel : combatStateModel.getEnemyBackLine()) {
                                    cardModel.addAbility(combatCardModel.getAbility());
                                }
                                break;
                            }
                            case HEAL: {
                                for (CombatCardModel cardModel : combatStateModel.getEnemyBackLine()) {
                                    HealAbility healAbility = (HealAbility) combatCardModel.getAbility();
                                    int currentHealth = cardModel.getCurrentHealth();
                                    int newHealth = currentHealth + healAbility.getHealAmount();
                                    if (newHealth <= cardModel.getMaxHealth()) {
                                        cardModel.setCurrentHealth(newHealth);
                                    } else {
                                        cardModel.setCurrentHealth(cardModel.getMaxHealth());
                                    }
                                }
                                break;
                            }
                        }
                        break;
                    }
                    case ENEMY_FRONT_LINE: {
                        switch (combatCardModel.getAbility().getAbilityType()) {
                            case BUFF:
                            case DEBUFF: {
                                for (CombatCardModel cardModel : combatStateModel.getEnemyFrontLine()) {
                                    cardModel.addAbility(combatCardModel.getAbility());
                                }
                                break;
                            }
                            case HEAL: {
                                for (CombatCardModel cardModel : combatStateModel.getEnemyFrontLine()) {
                                    HealAbility healAbility = (HealAbility) combatCardModel.getAbility();
                                    int currentHealth = cardModel.getCurrentHealth();
                                    int newHealth = currentHealth + healAbility.getHealAmount();
                                    if (newHealth <= cardModel.getMaxHealth()) {
                                        cardModel.setCurrentHealth(newHealth);
                                    } else {
                                        cardModel.setCurrentHealth(cardModel.getMaxHealth());
                                    }
                                }
                                break;
                            }
                        }
                        break;
                    }
                }
            }
        }
        mainActivity.publishEvent(new CombatStateUpdateEvent(combatStateModel, new HashMap<>()));
    }

    private void playEnemyCardOnEnemyCard(CombatCardModel droppedCard, CombatCardModel targetCard){
        combatStateModel.getEnemyHand().remove(droppedCard);
        droppedCard.setPlayed(true);
        if(droppedCard.getCardType().equals(CardType.ITEM) && targetCard.isPlayed() && targetCard.getCardType().equals(CardType.CHARACTER) ) {
            //TODO check whether an item is equiped or consumed. Currently ok beacuse heals are only consumed, but should check in the future for like a Healer's Kit item or whatever
            switch (droppedCard.getAbility().getAbilityType()) {
                case BUFF: {
                    BuffAbility buffAbility = (BuffAbility) droppedCard.getAbility();
                    if (buffAbility.getBuffType().equals(BuffType.HEALTH)) {
                        targetCard.setMaxHealth(targetCard.getMaxHealth() + buffAbility.getBuffAmount());
                        targetCard.setCurrentHealth(targetCard.getCurrentHealth() + buffAbility.getBuffAmount());
                    } else {
                        targetCard.addAbility(droppedCard.getAbility());
                    }
                    break;
                }
                case DAMAGE: {
                    targetCard.addAbility(droppedCard.getAbility());
                    break;
                }
                case HEAL: {
                    if (droppedCard.getAbility().getAbilityTarget().equals(AbilityTarget.SINGLE_ALLY)) {
                        HealAbility healAbility = (HealAbility) droppedCard.getAbility();
                        int currentHealth = targetCard.getCurrentHealth();
                        int newHealth = currentHealth + healAbility.getHealAmount();
                        if (newHealth <= targetCard.getMaxHealth()) {
                            targetCard.setCurrentHealth(newHealth);
                        } else {
                            targetCard.setCurrentHealth(targetCard.getMaxHealth());
                        }
                    }
                }
            }
        }
        mainActivity.publishEvent(new CombatStateUpdateEvent(combatStateModel, new HashMap<>()));
    }

    private DamageAbility getHighestMeleeAttack(CombatCardModel combatCardModel){
        if(combatCardModel.getCardType().equals(CardType.CHARACTER)){
            DamageAbility highestMelee = null;
            for(Ability ability: combatCardModel.getAbilities()){
                if(ability.getAbilityType().equals(AbilityType.DAMAGE)){
                    DamageAbility damageAbility = (DamageAbility) ability;
                    if(damageAbility.getAttackType().equals(AttackType.MELEE)){
                        if(highestMelee == null){
                            highestMelee = damageAbility;
                        } else {
                            if(highestMelee.getDamageAmount()<damageAbility.getDamageAmount()){
                                highestMelee = damageAbility;
                            }
                        }
                    }
                }
            }
            return highestMelee;
        } else {
            return null;
        }
    }

    private DamageAbility getHighestRangedAttack(CombatCardModel combatCardModel){
        if(combatCardModel.getCardType().equals(CardType.CHARACTER)){
            DamageAbility highestRanged = null;
            for(Ability ability: combatCardModel.getAbilities()){
                if(ability.getAbilityType().equals(AbilityType.DAMAGE)){
                    DamageAbility damageAbility = (DamageAbility) ability;
                    if(damageAbility.getAttackType().equals(AttackType.RANGED)){
                        if(highestRanged == null){
                            highestRanged = damageAbility;
                        } else {
                            if(highestRanged.getDamageAmount()<damageAbility.getDamageAmount()){
                                highestRanged = damageAbility;
                            }
                        }
                    }
                }
            }
            return highestRanged;
        } else {
            return null;
        }
    }

    private DamageAbility getHighestAttack(CombatCardModel combatCardModel){
        if(combatCardModel.getCardType().equals(CardType.CHARACTER)){
            DamageAbility highestDamage = null;
            for(Ability ability: combatCardModel.getAbilities()){
                if(ability.getAbilityType().equals(AbilityType.DAMAGE)){
                    DamageAbility damageAbility = (DamageAbility) ability;
                    if(highestDamage == null){
                        highestDamage = damageAbility;
                    } else {
                        if(highestDamage.getDamageAmount()<damageAbility.getDamageAmount()){
                            highestDamage = damageAbility;
                        }
                    }
                }
            }
            return highestDamage;
        } else {
            return null;
        }
    }

}
