package com.zblouse.fantasyfitness.combat;

import android.util.Log;

import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.combat.cards.Card;
import com.zblouse.fantasyfitness.combat.cards.CardType;
import com.zblouse.fantasyfitness.combat.cards.CharacterCard;
import com.zblouse.fantasyfitness.combat.cards.Deck;
import com.zblouse.fantasyfitness.combat.cards.EffectCard;
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

    public void deckFetchReturned(Deck userDeck){

        List<CombatCardModel> userDeckCards = new ArrayList<>();
        for(Card card: userDeck.getCards()){
            CombatCardModel combatCardModel = new CombatCardModel(card.getCardName(), card.getCardDescription(), card.getCardType());
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
