package com.zblouse.fantasyfitness.combat;

import android.util.Log;

import com.zblouse.fantasyfitness.activity.MainActivity;
import com.zblouse.fantasyfitness.combat.cards.Card;
import com.zblouse.fantasyfitness.combat.cards.Deck;

public class CombatService {

    private MainActivity mainActivity;

    public CombatService(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    public void initializeCombat(){
        Log.e("CombatService", "Initializing Combat");
        mainActivity.getDeckService().fetchDeck(mainActivity.getCurrentUser().getUid(),"userDeck");
    }

    public void deckFetchReturned(Deck userDeck){
        for(Card card: userDeck.getCards()){
            Log.e("CombatFragment","Card: " + card.getCardName());
        }
    }
}
