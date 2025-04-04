package com.zblouse.fantasyfitness.combat;

import java.util.Collections;
import java.util.List;

public class CombatDeckModel {

    private final List<CombatCardModel> remainingCards;

    public CombatDeckModel(List<CombatCardModel> initialDeck){
        this.remainingCards = initialDeck;
    }

    public List<CombatCardModel> getRemainingCards(){
        return this.remainingCards;
    }

    public void shuffle(){
        Collections.shuffle(remainingCards);
    }

    public CombatCardModel draw(){
        if(!remainingCards.isEmpty()){
            return remainingCards.remove(0);
        } else {
            return null;
        }
    }

    public boolean hasCardsRemaining(){
        return !remainingCards.isEmpty();
    }
}
