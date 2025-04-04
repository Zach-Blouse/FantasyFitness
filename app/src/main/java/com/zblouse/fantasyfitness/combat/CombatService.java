package com.zblouse.fantasyfitness.combat;

import android.util.Log;

import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.combat.cards.AbilityTarget;
import com.zblouse.fantasyfitness.combat.cards.BuffAbility;
import com.zblouse.fantasyfitness.combat.cards.Card;
import com.zblouse.fantasyfitness.combat.cards.CardType;
import com.zblouse.fantasyfitness.combat.cards.CharacterCard;
import com.zblouse.fantasyfitness.combat.cards.Deck;
import com.zblouse.fantasyfitness.combat.cards.EffectCard;
import com.zblouse.fantasyfitness.combat.cards.HealAbility;
import com.zblouse.fantasyfitness.combat.cards.ItemCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class CombatService {

    private MainActivity mainActivity;

    private CombatStateModel combatStateModel;
    private boolean playerTurn = true;

    public CombatService(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    public void initializeCombat(){
        Log.e("CombatService", "Initializing Combat");
        mainActivity.getDeckService().fetchDeck(mainActivity.getCurrentUser().getUid(),"userDeck");
        combatStateModel = null;
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
        Log.e("CombatService", "Card dropped on card. Dropped Type: " + droppedCard.getCardType() + " Ability target: " + droppedCard.getAbility().getAbilityTarget() + " Target Card Type: " + targetCard.getCardType() + " targetCard played: " + targetCard.isPlayed());
        if(droppedCard.getCardType().equals(CardType.ITEM) && targetCard.isPlayed() && targetCard.getCardType().equals(CardType.CHARACTER) ){
            switch (droppedCard.getAbility().getAbilityType()){
                case BUFF:
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
                }
            }
            combatStateModel.getPlayerHand().remove(droppedCard);
            mainActivity.publishEvent(new CombatStateUpdateEvent(combatStateModel,new HashMap<>()));
        }
    }

    public void deckFetchReturned(Deck userDeck){

        List<CombatCardModel> userDeckCards = new ArrayList<>();
        for(Card card: userDeck.getCards()){
            CombatCardModel combatCardModel = new CombatCardModel(card.getCardName(), card.getCardDescription(), card.getCardType(), false);
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
        CombatDeckModel enemyDeckModel = new CombatDeckModel(new ArrayList<>());
        combatStateModel = new CombatStateModel(userDeckModel,enemyDeckModel,initialUserHand,new ArrayList<>());

        mainActivity.publishEvent(new CombatStateUpdateEvent(combatStateModel,new HashMap<>()));
    }

    public boolean isPlayerTurn(){
        return this.playerTurn;
    }
}
